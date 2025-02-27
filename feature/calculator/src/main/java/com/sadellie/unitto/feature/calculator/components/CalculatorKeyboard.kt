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

package com.sadellie.unitto.feature.calculator.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.sadellie.unitto.core.base.Token
import com.sadellie.unitto.core.ui.Formatter
import com.sadellie.unitto.core.ui.common.ColumnWithConstraints
import com.sadellie.unitto.core.ui.common.KeyboardButtonAdditional
import com.sadellie.unitto.core.ui.common.KeyboardButtonFilled
import com.sadellie.unitto.core.ui.common.KeyboardButtonLight
import com.sadellie.unitto.core.ui.common.RowWithConstraints
import com.sadellie.unitto.core.ui.common.key.UnittoIcons
import com.sadellie.unitto.core.ui.common.key.unittoicons.AcTan
import com.sadellie.unitto.core.ui.common.key.unittoicons.ArCos
import com.sadellie.unitto.core.ui.common.key.unittoicons.ArSin
import com.sadellie.unitto.core.ui.common.key.unittoicons.Backspace
import com.sadellie.unitto.core.ui.common.key.unittoicons.Comma
import com.sadellie.unitto.core.ui.common.key.unittoicons.Cos
import com.sadellie.unitto.core.ui.common.key.unittoicons.Deg
import com.sadellie.unitto.core.ui.common.key.unittoicons.Divide
import com.sadellie.unitto.core.ui.common.key.unittoicons.Dot
import com.sadellie.unitto.core.ui.common.key.unittoicons.E
import com.sadellie.unitto.core.ui.common.key.unittoicons.Equal
import com.sadellie.unitto.core.ui.common.key.unittoicons.Exp
import com.sadellie.unitto.core.ui.common.key.unittoicons.ExponentWide
import com.sadellie.unitto.core.ui.common.key.unittoicons.Factorial
import com.sadellie.unitto.core.ui.common.key.unittoicons.Inv
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key0
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key1
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key2
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key3
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key4
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key5
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key6
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key7
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key8
import com.sadellie.unitto.core.ui.common.key.unittoicons.Key9
import com.sadellie.unitto.core.ui.common.key.unittoicons.LeftBracket
import com.sadellie.unitto.core.ui.common.key.unittoicons.Ln
import com.sadellie.unitto.core.ui.common.key.unittoicons.Log
import com.sadellie.unitto.core.ui.common.key.unittoicons.Minus
import com.sadellie.unitto.core.ui.common.key.unittoicons.Modulo
import com.sadellie.unitto.core.ui.common.key.unittoicons.Multiply
import com.sadellie.unitto.core.ui.common.key.unittoicons.Percent
import com.sadellie.unitto.core.ui.common.key.unittoicons.Pi
import com.sadellie.unitto.core.ui.common.key.unittoicons.Plus
import com.sadellie.unitto.core.ui.common.key.unittoicons.Rad
import com.sadellie.unitto.core.ui.common.key.unittoicons.RightBracket
import com.sadellie.unitto.core.ui.common.key.unittoicons.Sin
import com.sadellie.unitto.core.ui.common.key.unittoicons.SquareRootWide
import com.sadellie.unitto.core.ui.common.key.unittoicons.Tan

@Composable
internal fun CalculatorKeyboard(
    modifier: Modifier,
    radianMode: Boolean,
    allowVibration: Boolean,
    addSymbol: (String) -> Unit,
    clearSymbols: () -> Unit,
    deleteSymbol: () -> Unit,
    toggleAngleMode: () -> Unit,
    evaluate: () -> Unit
) {
    if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitKeyboard(
            modifier = modifier,
            radianMode = radianMode,
            allowVibration = allowVibration,
            addSymbol = addSymbol,
            toggleAngleMode = toggleAngleMode,
            deleteSymbol = deleteSymbol,
            clearSymbols = clearSymbols,
            evaluate = evaluate
        )
    } else {
        LandscapeKeyboard(
            modifier = modifier,
            radianMode = radianMode,
            allowVibration = allowVibration,
            addSymbol = addSymbol,
            toggleAngleMode = toggleAngleMode,
            deleteSymbol = deleteSymbol,
            clearSymbols = clearSymbols,
            evaluate = evaluate
        )
    }
}

@Composable
private fun PortraitKeyboard(
    modifier: Modifier,
    radianMode: Boolean,
    allowVibration: Boolean,
    addSymbol: (String) -> Unit,
    toggleAngleMode: () -> Unit,
    deleteSymbol: () -> Unit,
    clearSymbols: () -> Unit,
    evaluate: () -> Unit
) {
    val fractionalIcon = remember { if (Formatter.fractional == Token.dot) UnittoIcons.Dot else UnittoIcons.Comma }
    var showAdditional: Boolean by remember { mutableStateOf(false) }
    var invMode: Boolean by remember { mutableStateOf(false) }
    val expandRotation: Float by animateFloatAsState(
        targetValue = if (showAdditional) 0f else 180f,
        animationSpec = tween(easing = FastOutSlowInEasing)
    )

    ColumnWithConstraints(
        modifier = modifier
    ) { constraints ->
        fun verticalFraction(fraction: Float): Dp = constraints.maxHeight * fraction
        fun horizontalFraction(fraction: Float): Dp = constraints.maxWidth * fraction

        val weightModifier = Modifier.weight(1f)
        val mainButtonModifier = Modifier
            .fillMaxSize()
            .weight(1f)
            .padding(horizontalFraction(0.015f), verticalFraction(0.009f))
        val additionalButtonModifier = Modifier
            .weight(1f)
            .height(verticalFraction(0.09f))

        Spacer(modifier = Modifier.height(verticalFraction(0.025f)))

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(horizontalFraction(0.03f))
        ) {
            // Additional buttons
            Crossfade(invMode, weightModifier) {
                if (it) {
                    AdditionalButtonsPortraitInverse(
                        modifier = additionalButtonModifier,
                        allowVibration = allowVibration,
                        addSymbol = addSymbol,
                        showAdditional = showAdditional,
                        radianMode = radianMode,
                        toggleAngleMode = toggleAngleMode,
                        toggleInvMode = { invMode = !invMode }
                    )
                } else {
                    AdditionalButtonsPortrait(
                        modifier = additionalButtonModifier,
                        allowVibration = allowVibration,
                        addSymbol = addSymbol,
                        showAdditional = showAdditional,
                        radianMode = radianMode,
                        toggleAngleMode = toggleAngleMode,
                        toggleInvMode = { invMode = !invMode }
                    )
                }
            }

            Box(
                modifier = Modifier.size(verticalFraction(0.09f)),
                contentAlignment = Alignment.Center
            ) {
                // Expand/Collapse
                IconButton(
                    onClick = { showAdditional = !showAdditional },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    Icon(Icons.Default.ExpandLess, null, Modifier.rotate(expandRotation))
                }
            }
        }

        Spacer(modifier = Modifier.height(verticalFraction(0.025f)))

        Row(weightModifier) {
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.LeftBracket, allowVibration) { addSymbol(Token.leftBracket) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.RightBracket, allowVibration) { addSymbol(Token.rightBracket) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Percent, allowVibration) { addSymbol(Token.percent) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Divide, allowVibration) { addSymbol(Token.divideDisplay) }
        }
        Row(weightModifier) {
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key7, allowVibration) { addSymbol(Token._7) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key8, allowVibration) { addSymbol(Token._8) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key9, allowVibration) { addSymbol(Token._9) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Multiply, allowVibration) { addSymbol(Token.multiplyDisplay) }
        }
        Row(weightModifier) {
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key4, allowVibration) { addSymbol(Token._4) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key5, allowVibration) { addSymbol(Token._5) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key6, allowVibration) { addSymbol(Token._6) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Minus, allowVibration) { addSymbol(Token.minusDisplay) }
        }
        Row(weightModifier) {
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key1, allowVibration) { addSymbol(Token._1) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key2, allowVibration) { addSymbol(Token._2) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key3, allowVibration) { addSymbol(Token._3) }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Plus, allowVibration) { addSymbol(Token.plus) }
        }
        Row(weightModifier) {
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Key0, allowVibration) { addSymbol(Token._0) }
            KeyboardButtonLight(mainButtonModifier, fractionalIcon, allowVibration) { addSymbol(Token.dot) }
            KeyboardButtonLight(mainButtonModifier, UnittoIcons.Backspace, allowVibration, clearSymbols) { deleteSymbol() }
            KeyboardButtonFilled(mainButtonModifier, UnittoIcons.Equal, allowVibration) { evaluate() }
        }

        Spacer(modifier = Modifier.height(verticalFraction(0.015f)))
    }
}

@Composable
private fun AdditionalButtonsPortrait(
    modifier: Modifier,
    allowVibration: Boolean,
    addSymbol: (String) -> Unit,
    showAdditional: Boolean,
    radianMode: Boolean,
    toggleAngleMode: () -> Unit,
    toggleInvMode: () -> Unit
) {
    Column {
        Row {
            KeyboardButtonAdditional(modifier, UnittoIcons.SquareRootWide, allowVibration) { addSymbol(Token.sqrt) }
            KeyboardButtonAdditional(modifier, UnittoIcons.Pi, allowVibration) { addSymbol(Token.pi) }
            KeyboardButtonAdditional(modifier, UnittoIcons.ExponentWide, allowVibration) { addSymbol(Token.exponent) }
            KeyboardButtonAdditional(modifier, UnittoIcons.Factorial, allowVibration) { addSymbol(Token.factorial) }
        }
        AnimatedVisibility(showAdditional) {
            Column {
                Row {
                    KeyboardButtonAdditional(modifier, if (radianMode) UnittoIcons.Rad else UnittoIcons.Deg, allowVibration) { toggleAngleMode() }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Sin, allowVibration) { addSymbol(Token.sin) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Cos, allowVibration) { addSymbol(Token.cos) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Tan, allowVibration) { addSymbol(Token.tan) }
                }
                Row {
                    KeyboardButtonAdditional(modifier, UnittoIcons.Inv, allowVibration) { toggleInvMode() }
                    KeyboardButtonAdditional(modifier, UnittoIcons.E, allowVibration) { addSymbol(Token.e) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Ln, allowVibration) { addSymbol(Token.ln) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Log, allowVibration) { addSymbol(Token.log) }
                }
            }
        }
    }
}

@Composable
private fun AdditionalButtonsPortraitInverse(
    modifier: Modifier,
    allowVibration: Boolean,
    addSymbol: (String) -> Unit,
    showAdditional: Boolean,
    radianMode: Boolean,
    toggleAngleMode: () -> Unit,
    toggleInvMode: () -> Unit
) {
    Column {
        Row {
            KeyboardButtonAdditional(modifier, UnittoIcons.Modulo, allowVibration) { addSymbol(Token.modulo) }
            KeyboardButtonAdditional(modifier, UnittoIcons.Pi, allowVibration) { addSymbol(Token.pi) }
            KeyboardButtonAdditional(modifier, UnittoIcons.ExponentWide, allowVibration) { addSymbol(Token.exponent) }
            KeyboardButtonAdditional(modifier, UnittoIcons.Factorial, allowVibration) { addSymbol(Token.factorial) }
        }
        AnimatedVisibility(showAdditional) {
            Column {
                Row {
                    KeyboardButtonAdditional(modifier, if (radianMode) UnittoIcons.Rad else UnittoIcons.Deg, allowVibration) { toggleAngleMode() }
                    KeyboardButtonAdditional(modifier, UnittoIcons.ArSin, allowVibration) { addSymbol(Token.arSin) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.ArCos, allowVibration) { addSymbol(Token.arCos) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.AcTan, allowVibration) { addSymbol(Token.acTan) }
                }
                Row {
                    KeyboardButtonAdditional(modifier, UnittoIcons.Inv, allowVibration) { toggleInvMode() }
                    KeyboardButtonAdditional(modifier, UnittoIcons.E, allowVibration) { addSymbol(Token.e) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Exp, allowVibration) { addSymbol(Token.exp) }
                    KeyboardButtonAdditional(modifier, UnittoIcons.Log, allowVibration) { addSymbol(Token.log) }
                }
            }
        }
    }
}

@Composable
private fun LandscapeKeyboard(
    modifier: Modifier,
    radianMode: Boolean,
    allowVibration: Boolean,
    addSymbol: (String) -> Unit,
    toggleAngleMode: () -> Unit,
    deleteSymbol: () -> Unit,
    clearSymbols: () -> Unit,
    evaluate: () -> Unit
) {
    val fractionalIcon = remember { if (Formatter.fractional == Token.dot) UnittoIcons.Dot else UnittoIcons.Comma }
    var invMode: Boolean by remember { mutableStateOf(false) }

    RowWithConstraints(modifier) { constraints ->
        val buttonModifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(constraints.maxWidth * 0.005f, constraints.maxHeight * 0.02f)

        Crossfade(invMode, Modifier.weight(3f)) {
            Row {
                if (it) {
                    AdditionalButtonsLandscapeInverse(
                        modifier = Modifier.weight(1f),
                        buttonModifier = buttonModifier,
                        allowVibration = allowVibration,
                        radianMode = radianMode,
                        addSymbol = addSymbol,
                        toggleAngleMode = toggleAngleMode,
                        toggleInvMode = { invMode = !invMode }
                    )
                } else {
                    AdditionalButtonsLandscape(
                        modifier = Modifier.weight(1f),
                        buttonModifier = buttonModifier,
                        allowVibration = allowVibration,
                        radianMode = radianMode,
                        addSymbol = addSymbol,
                        toggleAngleMode = toggleAngleMode,
                        toggleInvMode = { invMode = !invMode }
                    )
                }
            }
        }

        Column(Modifier.weight(1f)) {
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key7, allowVibration) { addSymbol(Token._7) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key4, allowVibration) { addSymbol(Token._4) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key1, allowVibration) { addSymbol(Token._1) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key0, allowVibration) { addSymbol(Token._0) }
        }
        Column(Modifier.weight(1f)) {
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key8, allowVibration) { addSymbol(Token._8) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key5, allowVibration) { addSymbol(Token._5) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key2, allowVibration) { addSymbol(Token._2) }
            KeyboardButtonLight(buttonModifier, fractionalIcon, allowVibration) { addSymbol(Token.dot) }
        }
        Column(Modifier.weight(1f)) {
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key9, allowVibration) { addSymbol(Token._9) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key6, allowVibration) { addSymbol(Token._6) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Key3, allowVibration) { addSymbol(Token._3) }
            KeyboardButtonLight(buttonModifier, UnittoIcons.Backspace, allowVibration, clearSymbols) { deleteSymbol() }
        }

        Column(Modifier.weight(1f)) {
            KeyboardButtonFilled(buttonModifier, UnittoIcons.LeftBracket, allowVibration) { addSymbol(Token.leftBracket) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Multiply, allowVibration) { addSymbol(Token.multiplyDisplay) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Minus, allowVibration) { addSymbol(Token.minusDisplay) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Plus, allowVibration) { addSymbol(Token.plus) }
        }
        Column(Modifier.weight(1f)) {
            KeyboardButtonFilled(buttonModifier, UnittoIcons.RightBracket, allowVibration) { addSymbol(Token.rightBracket) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Divide, allowVibration) { addSymbol(Token.divideDisplay) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Percent, allowVibration) { addSymbol(Token.percent) }
            KeyboardButtonFilled(buttonModifier, UnittoIcons.Equal, allowVibration) { evaluate() }
        }
    }
}

@Composable
private fun AdditionalButtonsLandscape(
    modifier: Modifier,
    buttonModifier: Modifier,
    allowVibration: Boolean,
    radianMode: Boolean,
    addSymbol: (String) -> Unit,
    toggleAngleMode: () -> Unit,
    toggleInvMode: () -> Unit
) {
    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, if (radianMode) UnittoIcons.Rad else UnittoIcons.Deg, allowVibration) { toggleAngleMode() }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Inv, allowVibration) { toggleInvMode() }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Sin, allowVibration) { addSymbol(Token.sin) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.E, allowVibration) { addSymbol(Token.e) }
    }

    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.SquareRootWide, allowVibration) { addSymbol(Token.sqrt) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.ExponentWide, allowVibration) { addSymbol(Token.exponent) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Cos, allowVibration) { addSymbol(Token.cos) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Ln, allowVibration) { addSymbol(Token.ln) }
    }

    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Pi, allowVibration) { addSymbol(Token.pi) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Factorial, allowVibration) { addSymbol(Token.factorial) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Tan, allowVibration) { addSymbol(Token.tan) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Log, allowVibration) { addSymbol(Token.log) }
    }
}

@Composable
private fun AdditionalButtonsLandscapeInverse(
    modifier: Modifier,
    buttonModifier: Modifier,
    allowVibration: Boolean,
    radianMode: Boolean,
    addSymbol: (String) -> Unit,
    toggleAngleMode: () -> Unit,
    toggleInvMode: () -> Unit
) {
    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, if (radianMode) UnittoIcons.Rad else UnittoIcons.Deg, allowVibration) { toggleAngleMode() }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Inv, allowVibration) { toggleInvMode() }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.ArSin, allowVibration) { addSymbol(Token.arSin) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.E, allowVibration) { addSymbol(Token.e) }
    }

    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Modulo, allowVibration) { addSymbol(Token.modulo) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.ExponentWide, allowVibration) { addSymbol(Token.exponent) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.ArCos, allowVibration) { addSymbol(Token.arCos) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Exp, allowVibration) { addSymbol(Token.exp) }
    }

    Column(modifier) {
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Pi, allowVibration) { addSymbol(Token.pi) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Factorial, allowVibration) { addSymbol(Token.factorial) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.AcTan, allowVibration) { addSymbol(Token.acTan) }
        KeyboardButtonAdditional(buttonModifier, UnittoIcons.Log, allowVibration) { addSymbol(Token.log) }
    }
}

@Preview
@Composable
private fun PreviewCalculatorKeyboard() {
    CalculatorKeyboard(
        modifier = Modifier,
        radianMode = true,
        addSymbol = {},
        clearSymbols = {},
        deleteSymbol = {},
        toggleAngleMode = {},
        evaluate = {},
        allowVibration = false
    )
}
