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

package com.sadellie.unitto.core.base

/**
 * Output format here means whether or now use engineering notation
 */
object OutputFormat {
    // Never use engineering notation
    const val PLAIN = 0
    // Use format that a lower API returns
    const val ALLOW_ENGINEERING = 1
    // App will try it's best to use engineering notation
    const val FORCE_ENGINEERING = 2
}

/**
 * Available formats. Used in settings
 */
val OUTPUT_FORMAT: Map<Int, Int> by lazy {
    mapOf(
        OutputFormat.PLAIN to R.string.plain,
        OutputFormat.ALLOW_ENGINEERING to R.string.allow_engineering,
        OutputFormat.FORCE_ENGINEERING to R.string.force_engineering,
    )
}
