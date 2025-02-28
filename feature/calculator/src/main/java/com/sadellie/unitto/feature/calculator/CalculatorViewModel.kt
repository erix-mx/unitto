/*
 * Unitto is a unit converter for Android
 * Copyright (c) 2023 Elshan Agaev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sadellie.unitto.feature.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadellie.unitto.core.base.Token
import com.sadellie.unitto.data.calculator.CalculatorHistoryRepository
import com.sadellie.unitto.data.common.setMinimumRequiredScale
import com.sadellie.unitto.data.common.toStringWith
import com.sadellie.unitto.data.common.trimZeros
import com.sadellie.unitto.data.userprefs.UserPreferences
import com.sadellie.unitto.data.userprefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mariuszgromada.math.mxparser.Expression
import java.math.BigDecimal
import javax.inject.Inject
import org.mariuszgromada.math.mxparser.License as MathParserLicense
import org.mariuszgromada.math.mxparser.mXparser as MathParser

@HiltViewModel
internal class CalculatorViewModel @Inject constructor(
    private val userPrefsRepository: UserPreferencesRepository,
    private val calculatorHistoryRepository: CalculatorHistoryRepository,
    private val textFieldController: TextFieldController
) : ViewModel() {
    private val _userPrefs: StateFlow<UserPreferences> =
        userPrefsRepository.userPreferencesFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserPreferences()
        )

    private val _output: MutableStateFlow<String> = MutableStateFlow("")
    private val _history = calculatorHistoryRepository.historyFlow

    val uiState = combine(
        textFieldController.input, _output, _history, _userPrefs
    ) { input, output, history, userPrefs ->
        return@combine CalculatorUIState(
            input = input,
            output = output,
            radianMode = userPrefs.radianMode,
            history = history,
            allowVibration = userPrefs.enableVibrations
        )
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L), CalculatorUIState()
    )

    fun addSymbol(symbol: String) = textFieldController.addToInput(symbol)

    fun deleteSymbol() = textFieldController.delete()

    fun clearSymbols() = textFieldController.clearInput()

    fun toggleCalculatorMode() {
        viewModelScope.launch {
            userPrefsRepository.updateRadianMode(!_userPrefs.value.radianMode)
        }
    }

    // Called when user clicks "=" on a keyboard
    fun evaluate() {
        // Input and output can change while saving in history. This way we cache it here (i think)
        val output = _output.value

        // Output can be empty when input and output are identical (for exampel when user entered
        // just a number, not expression
        if (output.isEmpty()) return
        if (!Expression(textFieldController.inputTextWithoutFormatting().clean).checkSyntax()) return

        // Save to history
        viewModelScope.launch(Dispatchers.IO) {
            calculatorHistoryRepository.add(
                expression = textFieldController.inputTextWithoutFormatting(),
                result = output
            )
            textFieldController.clearInput()
            textFieldController.addToInput(output)
        }

        _output.update { "" }
    }

    fun clearHistory() = viewModelScope.launch(Dispatchers.IO) {
        calculatorHistoryRepository.clear()
    }

    fun onCursorChange(selection: IntRange) = textFieldController.moveCursor(selection)

    private fun calculateInput() {
        val currentInput = textFieldController.inputTextWithoutFormatting()
        // Input is empty, don't calculate
        if (currentInput.isEmpty()) {
            _output.update { "" }
            return
        }

        val calculated = Expression(currentInput.clean).calculate()

        // Calculation error, return NaN
        if (calculated.isNaN() or calculated.isInfinite()) {
            _output.update { calculated.toString() }
            return
        }

        val calculatedBigDecimal = calculated
            .toBigDecimal()
            .setMinimumRequiredScale(_userPrefs.value.digitsPrecision)
            .trimZeros()

        // Output will be empty if it's same as input
        try {
            val inputBigDecimal = BigDecimal(currentInput)

            // Input and output are identical values
            if (inputBigDecimal.compareTo(calculatedBigDecimal) == 0) {
                _output.update { "" }
                return
            }
        } catch (e: NumberFormatException) {
            // Cannot compare input and output
        }
        _output.update {
            calculatedBigDecimal.toStringWith(_userPrefs.value.outputFormat)
        }
        return
    }

    /**
     * Clean input so that there are no syntax errors
     */
    private val String.clean: String
        get() {
            val leftBrackets = count { it.toString() == Token.leftBracket }
            val rightBrackets = count { it.toString() == Token.rightBracket }
            val neededBrackets = leftBrackets - rightBrackets
            return replace(Token.minusDisplay, Token.minus)
                .plus(Token.rightBracket.repeat(neededBrackets.coerceAtLeast(0)))
        }

    init {
        /**
         * mxParser uses some unnecessary rounding for doubles. It causes expressions like 9999^9999
         * to load CPU very much. We use BigDecimal to achieve same result without CPU overload.
         */
        MathParserLicense.iConfirmNonCommercialUse("Sad Ellie")
        MathParser.setCanonicalRounding(false)
        MathParser.removeBuiltinTokens("log")
        MathParser.modifyBuiltinToken("lg", Token.log.dropLast(1))

        // Observe and invoke calculation without UI lag.
        viewModelScope.launch(Dispatchers.Default) {
            combine(_userPrefs, textFieldController.input) { userPrefs, _ ->
                if (userPrefs.radianMode) MathParser.setRadiansMode() else MathParser.setDegreesMode()
            }.collectLatest {
                calculateInput()
            }
        }
    }
}
