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

package com.sadellie.unitto.data.units.collections

import com.sadellie.unitto.data.model.AbstractUnit
import com.sadellie.unitto.data.model.DefaultUnit
import com.sadellie.unitto.data.model.UnitGroup
import com.sadellie.unitto.data.units.MyUnitIDS
import com.sadellie.unitto.data.units.R
import java.math.BigDecimal

internal val dataCollection: List<AbstractUnit> by lazy {
    listOf(
        DefaultUnit(MyUnitIDS.bit,       BigDecimal.valueOf(1),                          UnitGroup.DATA, R.string.bit,       R.string.bit_short),
        DefaultUnit(MyUnitIDS.kibibit,   BigDecimal.valueOf(1_024),                      UnitGroup.DATA, R.string.kibibit,   R.string.kibibit_short),
        DefaultUnit(MyUnitIDS.kilobit,   BigDecimal.valueOf(1_000),                      UnitGroup.DATA, R.string.kilobit,   R.string.kilobit_short),
        DefaultUnit(MyUnitIDS.megabit,   BigDecimal.valueOf(1_000_000),                  UnitGroup.DATA, R.string.megabit,   R.string.megabit_short),
        DefaultUnit(MyUnitIDS.mebibit,   BigDecimal.valueOf(1_048_576),                  UnitGroup.DATA, R.string.mebibit,   R.string.mebibit_short),
        DefaultUnit(MyUnitIDS.gigabit,   BigDecimal.valueOf(1_000_000_000),              UnitGroup.DATA, R.string.gigabit,   R.string.gigabit_short),
        DefaultUnit(MyUnitIDS.terabit,   BigDecimal.valueOf(1_000_000_000_000),          UnitGroup.DATA, R.string.terabit,   R.string.terabit_short),
        DefaultUnit(MyUnitIDS.petabit,   BigDecimal.valueOf(1_000_000_000_000_000),      UnitGroup.DATA, R.string.petabit,   R.string.petabit_short),
        DefaultUnit(MyUnitIDS.exabit,    BigDecimal.valueOf(1_000_000_000_000_000_000),  UnitGroup.DATA, R.string.exabit,    R.string.exabit_short),
        DefaultUnit(MyUnitIDS.byte,      BigDecimal.valueOf(8),                          UnitGroup.DATA, R.string.byte_,     R.string.byte_short),
        DefaultUnit(MyUnitIDS.kibibyte,  BigDecimal.valueOf(8_192),                      UnitGroup.DATA, R.string.kibibyte,  R.string.kibibyte_short),
        DefaultUnit(MyUnitIDS.kilobyte,  BigDecimal.valueOf(8_000),                      UnitGroup.DATA, R.string.kilobyte,  R.string.kilobyte_short),
        DefaultUnit(MyUnitIDS.megabyte,  BigDecimal.valueOf(8_000_000),                  UnitGroup.DATA, R.string.megabyte,  R.string.megabyte_short),
        DefaultUnit(MyUnitIDS.mebibyte,  BigDecimal.valueOf(8_388_608),                  UnitGroup.DATA, R.string.mebibyte,  R.string.mebibyte_short),
        DefaultUnit(MyUnitIDS.gigabyte,  BigDecimal.valueOf(8_000_000_000),              UnitGroup.DATA, R.string.gigabyte,  R.string.gigabyte_short),
        DefaultUnit(MyUnitIDS.terabyte,  BigDecimal.valueOf(8_000_000_000_000),          UnitGroup.DATA, R.string.terabyte,  R.string.terabyte_short),
        DefaultUnit(MyUnitIDS.petabyte,  BigDecimal.valueOf(8_000_000_000_000_000),      UnitGroup.DATA, R.string.petabyte,  R.string.petabyte_short),
        DefaultUnit(MyUnitIDS.exabyte,   BigDecimal.valueOf(8_000_000_000_000_000_000),  UnitGroup.DATA, R.string.exabyte,   R.string.exabyte_short),
    )
}