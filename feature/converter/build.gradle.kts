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

plugins {
    id("unitto.library")
    id("unitto.library.compose")
    id("unitto.library.feature")
    id("unitto.android.hilt")
}

android {
    namespace = "com.sadellie.unitto.feature.converter"
}

dependencies {
    testImplementation(libs.junit)
    testImplementation(libs.org.jetbrains.kotlinx.coroutines.test)
    testImplementation(libs.org.robolectric)
    testImplementation(libs.androidx.room.runtime)
    testImplementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    testImplementation(libs.androidx.datastore)

    implementation(libs.com.github.sadellie.exprk)
    implementation(libs.com.github.sadellie.themmo)
    implementation(libs.com.squareup.moshi)
    implementation(libs.com.squareup.retrofit2)

    implementation(project(mapOf("path" to ":data:common")))
    implementation(project(mapOf("path" to ":data:database")))
    implementation(project(mapOf("path" to ":data:model")))
    implementation(project(mapOf("path" to ":data:userprefs")))
    implementation(project(mapOf("path" to ":data:units")))
}
