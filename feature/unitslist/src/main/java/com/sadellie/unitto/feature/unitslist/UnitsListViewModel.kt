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

package com.sadellie.unitto.feature.unitslist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sadellie.unitto.data.database.UnitsEntity
import com.sadellie.unitto.data.database.UnitsRepository
import com.sadellie.unitto.data.model.AbstractUnit
import com.sadellie.unitto.data.model.UnitGroup
import com.sadellie.unitto.data.unitgroups.UnitGroupsRepository
import com.sadellie.unitto.data.units.AllUnitsRepository
import com.sadellie.unitto.data.userprefs.UserPreferences
import com.sadellie.unitto.data.userprefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UnitsListViewModel @Inject constructor(
    private val unitRepository: UnitsRepository,
    private val allUnitsRepository: AllUnitsRepository,
    private val mContext: Application,
    private val userPrefsRepository: UserPreferencesRepository,
    unitGroupsRepository: UnitGroupsRepository,
) : ViewModel() {

    private val _userPrefs: StateFlow<UserPreferences> =
        userPrefsRepository.userPreferencesFlow.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserPreferences()
        )
    private val _unitsToShow = MutableStateFlow(emptyMap<UnitGroup, List<AbstractUnit>>())
    private val _searchQuery = MutableStateFlow("")
    private val _chosenUnitGroup: MutableStateFlow<UnitGroup?> = MutableStateFlow(null)
    private val _shownUnitGroups = unitGroupsRepository.shownUnitGroups

    val mainFlow = combine(
        _userPrefs,
        _unitsToShow,
        _searchQuery,
        _chosenUnitGroup,
        _shownUnitGroups,
    ) { userPrefs, unitsToShow, searchQuery, chosenUnitGroup, shownUnitGroups ->
        return@combine SecondScreenUIState(
            favoritesOnly = userPrefs.unitConverterFavoritesOnly,
            unitsToShow = unitsToShow,
            searchQuery = searchQuery,
            chosenUnitGroup = chosenUnitGroup,
            shownUnitGroups = shownUnitGroups,
        )
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SecondScreenUIState())

    fun toggleFavoritesOnly(hideBrokenCurrencies: Boolean = true) {
        viewModelScope.launch {
            userPrefsRepository.updateUnitConverterFavoritesOnly(
                !_userPrefs.value.unitConverterFavoritesOnly
            )
            loadUnitsToShow(hideBrokenCurrencies)
        }
    }

    fun onSearchQueryChange(newValue: String, hideBrokenCurrencies: Boolean = true) {
        _searchQuery.update { newValue }
        loadUnitsToShow(hideBrokenCurrencies)
    }

    /**
     * Changes currently selected chip. Used only when navigating
     *
     * @param unit Will find group for unit with this id.
     */
    fun setSelectedChip(unit: String, hideBrokenCurrencies: Boolean = true) {
        _chosenUnitGroup.update { allUnitsRepository.getById(unit).group }
        loadUnitsToShow(hideBrokenCurrencies)
    }

    /**
     * Changes currently selected chip, but acts as toggle when the given [UnitGroup] is same as
     * already set [UnitGroup]. For example, if currently selected [UnitGroup] is [UnitGroup.TIME]
     * and the provided [UnitGroup] is also [UnitGroup.TIME], currently selected unit will be set
     * to null (toggle).
     *
     * @param unitGroup [UnitGroup], currently selected chip.
     */
    fun toggleSelectedChip(unitGroup: UnitGroup, hideBrokenCurrencies: Boolean = true) {
        val newUnitGroup = if (_chosenUnitGroup.value == unitGroup) null else unitGroup
        _chosenUnitGroup.update { newUnitGroup }
        loadUnitsToShow(hideBrokenCurrencies)
    }

    /**
     * Filters and groups [AllUnitsRepository.allUnits] in coroutine
     *
     * @param hideBrokenCurrencies Decide whether or not we are on left side. Need it because right side requires
     * us to mark disabled currency units
     */
    private fun loadUnitsToShow(
        hideBrokenCurrencies: Boolean
    ) {
        viewModelScope.launch {
            // This is mostly not UI related stuff and viewModelScope.launch uses Dispatchers.Main
            // So we switch to Default
            withContext(Dispatchers.Default) {
                val unitsToShow = allUnitsRepository.filterUnits(
                    hideBrokenCurrencies = hideBrokenCurrencies,
                    chosenUnitGroup = _chosenUnitGroup.value,
                    favoritesOnly = _userPrefs.value.unitConverterFavoritesOnly,
                    searchQuery = _searchQuery.value,
                    allUnitsGroups = _shownUnitGroups.value,
                    sorting = _userPrefs.value.unitConverterSorting
                )

                _unitsToShow.update { unitsToShow }
            }
        }
    }

    /**
     * Add or remove from favorites (changes to the opposite of current state, toggle)
     */
    fun favoriteUnit(unit: AbstractUnit) {
        viewModelScope.launch {
            // Changing unit in list to the opposite
            unit.isFavorite = !unit.isFavorite
            // Updating it in database
            unitRepository.insertUnits(
                UnitsEntity(
                    unitId = unit.unitId,
                    isFavorite = unit.isFavorite,
                    pairedUnitId = unit.pairedUnit,
                    frequency = unit.counter
                )
            )
        }
    }

    private fun loadUnitsFromDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            // Now we load units data from database
            val allUnits = unitRepository.getAll()
            allUnitsRepository.loadFromDatabase(mContext, allUnits)
        }
    }

    init {
        loadUnitsFromDatabase()
    }
}
