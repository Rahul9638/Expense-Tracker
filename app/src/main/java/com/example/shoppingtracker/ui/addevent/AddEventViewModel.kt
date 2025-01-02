package com.example.shoppingtracker.ui.addevent

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shoppingtracker.data.repos.ShoppingEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AddEventViewModel @Inject constructor(
    private val shoppingEventRepository: ShoppingEventRepository,
) : ViewModel() {
    var addEventUiState by mutableStateOf(AddEventUiState(addEventDetails = AddEventDetails()));

    fun updateUiState(event: AddEventDetails) {
        addEventUiState =
            AddEventUiState(addEventDetails = event, isEntryValid = validateInput(event))
    }

    private fun validateInput(eventDetails: AddEventDetails = addEventUiState.addEventDetails): Boolean {
        return with(eventDetails) {
            name.isNotBlank() && eventDate.isNotBlank();
        }
    }

    suspend fun saveEvent() {
        if (validateInput()) {
            shoppingEventRepository.insert(addEventUiState.addEventDetails.toShoppingEvent())
        }
    }
}