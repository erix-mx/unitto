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

package com.sadellie.unitto.feature.unitslist

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sadellie.unitto.data.model.AbstractUnit
import com.sadellie.unitto.feature.unitslist.components.ChipsRow
import com.sadellie.unitto.feature.unitslist.components.SearchBar
import com.sadellie.unitto.feature.unitslist.components.SearchPlaceholder
import com.sadellie.unitto.feature.unitslist.components.UnitGroupHeader
import com.sadellie.unitto.feature.unitslist.components.UnitListItem

/**
 * Left side screen. Unit to convert from.
 *
 * @param viewModel [UnitsListViewModel].
 * @param currentUnitId Currently selected [AbstractUnit] (by ID).
 * @param navigateUp Action to navigate up. Called when user click back button.
 * @param navigateToSettingsAction Action to perform when clicking settings chip at the end.
 * @param selectAction Action to perform when user clicks on [UnitListItem].
 */
@Composable
internal fun LeftSideScreen(
    viewModel: UnitsListViewModel,
    currentUnitId: String?,
    navigateUp: () -> Unit,
    navigateToSettingsAction: () -> Unit,
    selectAction: (AbstractUnit) -> Unit
) {
    val uiState = viewModel.mainFlow.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val chipsRowLazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    val elevatedColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
    val needToTint by remember {
        derivedStateOf { scrollBehavior.state.overlappedFraction > 0.01f }
    }

    val chipsBackground = animateColorAsState(
        if (needToTint) elevatedColor else MaterialTheme.colorScheme.surface,
        tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column(
                Modifier.background(chipsBackground.value)
            ) {
                SearchBar(
                    title = stringResource(R.string.units_screen_from),
                    value = uiState.value.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    favoritesOnly = uiState.value.favoritesOnly,
                    favoriteAction = { viewModel.toggleFavoritesOnly() },
                    navigateUpAction = navigateUp,
                    focusManager = focusManager,
                    scrollBehavior = scrollBehavior
                )
                ChipsRow(
                    chosenUnitGroup = uiState.value.chosenUnitGroup,
                    items = uiState.value.shownUnitGroups,
                    selectAction = { viewModel.toggleSelectedChip(it) },
                    lazyListState = chipsRowLazyListState,
                    navigateToSettingsAction = navigateToSettingsAction
                )
            }
        }
    ) { paddingValues ->
        Crossfade(
            targetState = uiState.value.unitsToShow.isEmpty(),
            modifier = Modifier.padding(paddingValues)
        ) { noUnits ->
            if (noUnits) {
                SearchPlaceholder(navigateToSettingsAction = navigateToSettingsAction)
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    uiState.value.unitsToShow.forEach { (unitGroup, listOfUnits) ->
                        item(unitGroup.name) {
                            UnitGroupHeader(Modifier.animateItemPlacement(), unitGroup)
                        }
                        items(listOfUnits, { it.unitId }) { unit ->
                            UnitListItem(
                                modifier = Modifier.animateItemPlacement(),
                                unit = unit,
                                isSelected = currentUnitId == unit.unitId,
                                selectAction = {
                                    selectAction(it)
                                    viewModel.onSearchQueryChange("")
                                    focusManager.clearFocus(true)
                                    navigateUp()
                                },
                                favoriteAction = { viewModel.favoriteUnit(it) },
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(uiState.value.shownUnitGroups, currentUnitId) {
        if (currentUnitId == null) return@LaunchedEffect
        // This is still wrong, but works good enough.
        // Ideally we shouldn't use uiState.value.shownUnitGroups
        viewModel.setSelectedChip(currentUnitId)
        val groupToSelect = uiState.value.shownUnitGroups.indexOf(uiState.value.chosenUnitGroup)
        if (groupToSelect > -1) {
            chipsRowLazyListState.animateScrollToItem(groupToSelect)
        }
    }
}
