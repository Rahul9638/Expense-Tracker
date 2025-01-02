package com.example.shoppingtracker.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppingtracker.data.entities.ShoppingEvent
import com.example.shoppingtracker.data.repos.ShoppingEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val eventRepository: ShoppingEventRepository
) : ViewModel() {


    private val _homeUiState = MutableStateFlow(HomeUiState());
    val homeUiState = _homeUiState.asStateFlow()

    init {
        viewModelScope.launch {
            eventRepository.getEvents().collect { events ->
                Log.d("HomeViewModel", "total length ${events.size}")
                _homeUiState.update { state ->
                    state.copy(events = events)
                }
                Log.d("HomeViewModel", "updated events length ${_homeUiState.value.events.size}")

            }
        }
    }

    suspend fun deleteEvent(it: ShoppingEvent) {
        eventRepository.delete(it);
    }
}