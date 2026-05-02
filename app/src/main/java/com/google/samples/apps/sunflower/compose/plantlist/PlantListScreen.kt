/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.compose.plantlist

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.sunflower.viewmodels.PlantListViewModel
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource


@Composable
fun PlantListScreen(
    onPlantClick: (Plant) -> Unit,

    modifier: Modifier = Modifier,
    viewModel: PlantListViewModel = hiltViewModel(),
) {
    val plants by viewModel.plants.observeAsState(initial = emptyList())
    val searchQuery by viewModel.searchquery.observeAsState(initial = "")
    PlantListScreen(plants = plants, modifier, onPlantClick = onPlantClick, searchQuery = searchQuery, onSearchQueryChange = {viewModel.setSearchQuery(it)})
}

@Composable
fun PlantListScreen(
    plants: List<Plant>,
    modifier: Modifier = Modifier,
    onPlantClick: (Plant) -> Unit = {},
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .testTag("plant_list")
            .imePadding()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.card_side_margin),
                    vertical = dimensionResource(id = R.dimen.header_margin)
                ),
            placeholder = { Text(text = stringResource(id = R.string.search_plants)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear_search)
                        )
                    }
                }
            },
            singleLine = true,
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier.testTag("plant_list")
                .imePadding(),
            contentPadding = PaddingValues(
                horizontal = dimensionResource(id = R.dimen.card_side_margin),
                vertical = dimensionResource(id = R.dimen.header_margin)
            )
        ) {
            items(
                items = plants,
                key = { it.plantId }
            ) { plant ->
                PlantListItem(plant = plant) {
                    onPlantClick(plant)
                }
            }
        }
    }
}

@Preview
@Composable
private fun PlantListScreenPreview(
    @PreviewParameter(PlantListPreviewParamProvider::class) plants: List<Plant>
) {
    PlantListScreen(plants = plants)
}

private class PlantListPreviewParamProvider : PreviewParameterProvider<List<Plant>> {
    override val values: Sequence<List<Plant>> =
        sequenceOf(
            emptyList(),
            listOf(
                Plant("1", "Apple", "Apple", growZoneNumber = 1),
                Plant("2", "Banana", "Banana", growZoneNumber = 2),
                Plant("3", "Carrot", "Carrot", growZoneNumber = 3),
                Plant("4", "Dill", "Dill", growZoneNumber = 3),
            )
        )
}
