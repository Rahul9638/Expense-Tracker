package com.example.shoppingtracker.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppingtracker.customcomposables.EmptyListComposable
import com.example.shoppingtracker.customcomposables.ShoppingAppBar
import com.example.shoppingtracker.data.entities.ShoppingEvent
import kotlinx.coroutines.launch

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navigateToAddEvent: () -> Unit,
    navigateToEventDetail: (ShoppingEvent) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var coroutineScope = rememberCoroutineScope();
    Scaffold(topBar = {
        ShoppingAppBar(title = "Shopping Events", canNavigateBack = false)
    }, floatingActionButton = {
        FloatingActionButton(onClick = navigateToAddEvent) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }
    }) {
        if (homeUiState.events.isEmpty()) {
            EmptyListComposable(
                message = "No Events \n Add events to track", modifier = modifier.padding(it)
            )
            return@Scaffold
        }
        Column(modifier = modifier.padding(it)) {
            CustomEventList(
                onDeleteEvent = {
                    coroutineScope.launch {
                        viewModel.deleteEvent(it)
                    }
                },
                navigateToEventDetail = navigateToEventDetail,
                modifier = modifier
                    .fillMaxSize()
                    .padding(it), events = homeUiState.events
            )
        }
    }

}

@Composable
fun CustomEventList(
    events: List<ShoppingEvent>,
    modifier: Modifier,
    navigateToEventDetail: (ShoppingEvent) -> Unit,
    onDeleteEvent: (ShoppingEvent) -> Unit,
) {
    LazyColumn {
        items(events) { event ->
            ShoppingEventDetail(
                event,
                onEventTap = navigateToEventDetail,
                onDeleteEvent = onDeleteEvent,
            )
        }
    }
}

@Composable
fun ShoppingEventDetail(
    shoppingEvent: ShoppingEvent,
    modifier: Modifier = Modifier,
    onEventTap: (ShoppingEvent) -> Unit,
    onDeleteEvent: (ShoppingEvent) -> Unit,
) {

    var onDelete by remember {
        mutableStateOf(false)
    }
    if (onDelete) {
        AlertDialog(
            title = {
                Text(text = "Delete Event")
            },
            text = {
                Text(text = "Are you sure want to delete ${shoppingEvent.name}");
            },
            dismissButton = {
                IconButton(onClick = {
                    onDelete = false;
                }) {
                    Icon(Icons.Default.Close, contentDescription = null)
                }
            },
            onDismissRequest = {
                onDelete = false;
            },
            confirmButton = {
                IconButton(onClick = {
                    onDelete = false;
                    onDeleteEvent(shoppingEvent)
                }) {
                    Icon(Icons.Filled.Done, contentDescription = null);
                }
            },
        )
    }
    ListItem(
        tonalElevation = 10.dp,
        modifier = Modifier
            .padding(10.dp)
            .clickable {
                onEventTap(shoppingEvent)
            },
        headlineContent = {
            Text(text = shoppingEvent.name)
        },
        supportingContent = {
            Text(text = shoppingEvent.eventDate)
        },
        trailingContent = {
            Text(
                text = "\$ ${shoppingEvent.totalCost}",
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            IconButton(onClick = { onDelete = true }) {
                Icon(Icons.Filled.Delete, contentDescription = null)
            }
        }
    )
}

