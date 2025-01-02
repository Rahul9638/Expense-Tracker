package com.example.shoppingtracker.ui.home

import com.example.shoppingtracker.data.entities.ShoppingEvent

data class HomeUiState(
    val events: List<ShoppingEvent> = emptyList()
)