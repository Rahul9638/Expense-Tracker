package com.example.shoppingtracker.ui.eventdetails

import com.example.shoppingtracker.data.entities.ShoppingItem
import com.example.shoppingtracker.ui.addevent.AddEventDetails

data class ItemDetails(
    val itemId: Long = 0,
    val eventId: Long = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)

data class ItemUiState(
    val isEditable: Boolean = false,
    val itemDetails: ItemDetails,
)

data class EventDetailUiState(
    val eventDetailState: AddEventDetails = AddEventDetails(),
    val itemList: List<ItemUiState> = emptyList<ItemUiState>()
)


fun ShoppingItem.toItemDetails(): ItemDetails = ItemDetails(
    itemId = itemId,
    eventId = eventId,
    name = itemName,
    price = price.toString(),
    quantity = quantity.toString()
)


fun ItemDetails.toShoppingItem(): ShoppingItem {
    return ShoppingItem(
        itemId = itemId,
        price = price.toDoubleOrNull() ?: 0.0,
        itemName = name,
        eventId = eventId,
        quantity = quantity.toDoubleOrNull() ?: 0.0,
        )
}