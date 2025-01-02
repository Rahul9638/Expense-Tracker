package com.example.shoppingtracker.ui.eventdetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.shoppingtracker.data.entities.ShoppingItem
import com.example.shoppingtracker.data.repos.ShoppingEventRepository
import com.example.shoppingtracker.data.repos.ShoppingItemRepository
import com.example.shoppingtracker.destinations.EventDetailRoute
import com.example.shoppingtracker.ui.addevent.AddEventDetails
import com.example.shoppingtracker.ui.addevent.toAddEventDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val shoppingEventRepository: ShoppingEventRepository,
    private val shoppingItemRepository: ShoppingItemRepository,
) : ViewModel() {

    val detailRoute: EventDetailRoute = savedStateHandle.toRoute<EventDetailRoute>();
    private val _eventDetailUiState = MutableStateFlow(EventDetailUiState())
    val eventDetailUiState = _eventDetailUiState.asStateFlow();

    init {
        viewModelScope.launch {
            shoppingEventRepository.getEventAndItem(detailRoute.eventId).collect { map ->
                val entry = map.entries.firstOrNull();
                Log.d("EventDetailViewModel", ":${map.toString()} ")
                _eventDetailUiState.update { value ->
                    value.copy(
                        eventDetailState = entry?.key?.toAddEventDetails()
                            ?: AddEventDetails(name = detailRoute.eventName),
                        itemList = entry?.value?.map { value ->
                            ItemUiState(itemDetails = value.toItemDetails())
                        } ?: emptyList(),
                    )

                }
            }
        }
    }

    fun updateUiStateItem(itemDetails: ItemDetails): Unit {
        _eventDetailUiState.update { state ->
            state.copy(
                itemList = state.itemList.map {
                    if (it.itemDetails.itemId == itemDetails.itemId) {
                        it.copy(itemDetails = itemDetails)
                    } else {
                        it
                    }
                },
            )

        }
    }

    fun updateItemEditMode(itemDetails: ItemDetails): Unit {
        _eventDetailUiState.update { state ->
            state.copy(
                itemList = state.itemList.map {
                    if (it.itemDetails.itemId == itemDetails.itemId) {
                        it.copy(isEditable = true)
                    } else {
                        it
                    }
                },
            )
        }
    }

    suspend fun addItem() {
        val shoppingItem = ShoppingItem(eventId = detailRoute.eventId, itemName = "Item")
        shoppingItemRepository.insert(shoppingItem);
    }

    suspend fun onDeleteItem(itemDetails: ItemDetails) {
        shoppingItemRepository.delete(itemDetails.toShoppingItem())
    }

    suspend fun updateItem(itemDetails: ItemDetails): Unit {
        shoppingItemRepository.update(itemDetails.toShoppingItem())
    }


}