/*
 * Unitto is a unit converter for Android
 * Copyright (c) 2022-2023 Elshan Agaev
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

package com.sadellie.unitto.core.ui.common

import android.content.res.Configuration
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView

@Composable
fun BasicKeyboardButton(
    modifier: Modifier,
    onClick: () -> Unit,
    onLongClick: (() -> Unit)?,
    containerColor: Color,
    icon: ImageVector,
    iconColor: Color,
    allowVibration: Boolean,
    contentHeight: Float
) {
    val view = LocalView.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val cornerRadius: Int by animateIntAsState(
        targetValue = if (isPressed) 30 else 50,
        animationSpec = tween(easing = FastOutSlowInEasing),
    )

    UnittoButton(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
        shape = RoundedCornerShape(cornerRadius),
        containerColor = containerColor,
        contentPadding = PaddingValues(),
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.fillMaxHeight(contentHeight),
            tint = iconColor
        )
    }

    LaunchedEffect(key1 = isPressed) {
        if (isPressed and allowVibration) view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
    }
}

@Composable
fun KeyboardButtonLight(
    modifier: Modifier,
    icon: ImageVector,
    allowVibration: Boolean,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    BasicKeyboardButton(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        icon = icon,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        allowVibration = allowVibration,
        contentHeight = if (isPortrait()) 0.5f else 0.85f
    )
}

@Composable
fun KeyboardButtonFilled(
    modifier: Modifier,
    icon: ImageVector,
    allowVibration: Boolean,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    BasicKeyboardButton(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        icon = icon,
        iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        allowVibration = allowVibration,
        contentHeight = if (isPortrait()) 0.5f else 0.85f
    )
}

@Composable
fun KeyboardButtonAdditional(
    modifier: Modifier,
    icon: ImageVector,
    allowVibration: Boolean,
    onLongClick: (() -> Unit)? = null,
    onClick: () -> Unit
) {
    BasicKeyboardButton(
        modifier = modifier,
        onClick = onClick,
        onLongClick = onLongClick,
        containerColor = Color.Transparent,
        icon = icon,
        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        allowVibration = allowVibration,
        contentHeight = if (isPortrait()) 0.8f else 0.85f
    )
}

@Composable
private fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
