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

package com.sadellie.unitto.feature.calculator.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.sadellie.unitto.core.base.TopLevelDestinations
import com.sadellie.unitto.feature.calculator.CalculatorRoute

private val calculatorRoute: String by lazy { TopLevelDestinations.Calculator.route }

fun NavGraphBuilder.calculatorScreen(
    navigateToMenu: () -> Unit,
    navigateToSettings: () -> Unit
) {
    composable(
        route = calculatorRoute,
        deepLinks = listOf(
            navDeepLink { uriPattern = "app://com.sadellie.unitto/$calculatorRoute" }
        )) {
        CalculatorRoute(
            navigateToMenu = navigateToMenu,
            navigateToSettings = navigateToSettings
        )
    }
}
