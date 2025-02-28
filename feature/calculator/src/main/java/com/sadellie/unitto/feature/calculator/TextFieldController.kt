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

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.sadellie.unitto.core.base.Token
import com.sadellie.unitto.core.base.Separator
import com.sadellie.unitto.core.ui.UnittoFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.abs

class TextFieldController @Inject constructor() {
    var input: MutableStateFlow<TextFieldValue> = MutableStateFlow(TextFieldValue())

    // Internally we don't care about user preference here, because during composition this
    // symbols will be replaced to those that user wanted.
    // We do this because it adds unnecessary logic: it requires additional logic to observe and
    // react to formatting preferences at this level.
    private val localFormatter: UnittoFormatter by lazy {
        UnittoFormatter().also {
            it.setSeparator(Separator.COMMA)
        }
    }

    private val cursorFixer by lazy { CursorFixer() }

    fun addToInput(symbols: String) {

        val text = input.value.text
        val selection = input.value.selection
        val lastToEndDistance = text.length - selection.end

        val newInput = if (text.isEmpty()) {
            symbols
        } else {
            text.replaceRange(selection.start, selection.end, symbols)
        }

        val inputFormatted = newInput.fixFormat()
        val newSelectionStartEnd = inputFormatted.length - lastToEndDistance
        val fixedCursor = fixCursor(
            newPosition = newSelectionStartEnd..newSelectionStartEnd,
            currentInput = inputFormatted
        ) ?: newSelectionStartEnd..newSelectionStartEnd

        input.update {
            it.copy(
                text = inputFormatted,
                selection = TextRange(fixedCursor)
            )
        }
    }

    /**
     * Method to call when pasting from clipbaord. It filters input before calling [addToInput].
     */
    fun pasteSymbols(symbols: String) = addToInput(symbols.filterUnknownSymbols())

    fun moveCursor(newPosition: IntRange) {
        input.update {
            it.copy(
                selection = TextRange(fixCursor(newPosition) ?: return)
            )
        }
    }

    fun delete() {
        val selection = input.value.selection
        val distanceFromEnd = input.value.text.length - selection.end

        val deleteRangeStart = when (selection.end) {
            // Don't delete if at the start of the text field
            0 -> return
            // We don't have anything selected (cursor in one position)
            // like this 1234|56 => after deleting will be like this 123|56
            // Cursor moved one symbol left
            selection.start -> {
                // We default to 1 here. It means that cursor is not placed after illegal token
                // Just a number or a binary operator or something else, can delete by one symbol
                val amountOfSymbolsToDelete: Int =
                    cursorFixer.tokenLengthInFront(input.value.text, selection.end) ?: 1
                selection.start - amountOfSymbolsToDelete
            }
            // We have multiple symbols selected
            // like this 123[45]6 => after deleting will be like this 123|6
            // Cursor will be placed where selection start was
            else -> selection.start
        }

        input.update {
            val newText = it.text
                .removeRange(deleteRangeStart, it.selection.end)
                .fixFormat()
            it.copy(
                text = newText,
                selection = TextRange((newText.length - distanceFromEnd).coerceAtLeast(0))
            )
        }
    }

    fun clearInput() = input.update { TextFieldValue() }

    fun inputTextWithoutFormatting() = input.value.text
        .replace(localFormatter.grouping, "")
        .replace(localFormatter.fractional, Token.dot)

    private fun fixCursor(
        newPosition: IntRange,
        currentInput: String = input.value.text
    ): IntRange? {
        if (newPosition.last > currentInput.length) return null

        val fixedLeftCursor = cursorFixer.fixCursorIfNeeded(currentInput, newPosition.first)
        val fixedRightCursor = cursorFixer.fixCursorIfNeeded(currentInput, newPosition.last)

        return fixedLeftCursor..fixedRightCursor
    }

    private fun String.fixFormat(): String = localFormatter.reFormat(this)

    private fun String.filterUnknownSymbols() = localFormatter.filterUnknownSymbols(this)

    private fun TextRange(range: IntRange): TextRange = TextRange(range.first, range.last)

    inner class CursorFixer {
        private val illegalTokens by lazy {
            listOf(
                Token.arSin,
                Token.arCos,
                Token.acTan,
                Token.cos,
                Token.sin,
                Token.exp,
                Token.ln,
                Token.log,
                Token.tan
            )
        }

        fun fixCursorIfNeeded(str: String, pos: Int): Int {
            // Best position if we move cursor left
            val bestLeft = bestPositionLeft(str, pos)
            // Best position if we move cursor right
            val bestRight = bestPositionRight(str, pos)

            return listOf(bestLeft, bestRight)
                .minBy { abs(it - pos) }
        }

        fun tokenLengthInFront(str: String, pos: Int): Int? {
            illegalTokens.forEach {
                if (pos.afterToken(str, it)) return it.length
            }

            return null
        }

        private fun bestPositionLeft(str: String, pos: Int): Int {
            var cursorPosition = pos
            while (placedIllegally(str, cursorPosition)) cursorPosition--
            return cursorPosition
        }

        private fun bestPositionRight(str: String, pos: Int): Int {
            var cursorPosition = pos
            while (placedIllegally(str, cursorPosition)) cursorPosition++
            return cursorPosition
        }

        private fun placedIllegally(str: String, pos: Int): Boolean {
            // For things like "123,|456" - this is illegal
            if (pos.afterToken(str, localFormatter.grouping)) return true

            // For things like "123,456+c|os(8)" - this is illegal
            illegalTokens.forEach {
                if (pos.atToken(str, it)) return true
            }

            return false
        }

        /**
         * Don't use if token is 1 symbol long, it wouldn't make sense! Use [afterToken] instead.
         * @see [afterToken]
         */
        private fun Int.atToken(str: String, token: String): Boolean {
            val checkBound = (token.length - 1).coerceAtLeast(1)

            val stringToScan = str.substring(
                startIndex = (this - checkBound).coerceAtLeast(0),
                endIndex = (this + checkBound).coerceAtMost(str.length)
            )

            return stringToScan.contains(token)
        }

        private fun Int.afterToken(str: String, token: String): Boolean {
            val stringToScan = str.substring(
                startIndex = (this - token.length).coerceAtLeast(0),
                endIndex = this
            )

            return stringToScan.contains(token)
        }
    }
}
