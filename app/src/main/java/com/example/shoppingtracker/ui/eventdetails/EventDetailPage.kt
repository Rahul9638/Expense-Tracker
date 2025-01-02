package com.example.shoppingtracker.ui.eventdetails

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppingtracker.customcomposables.EditItemList
import com.example.shoppingtracker.customcomposables.EmptyListComposable
import com.example.shoppingtracker.customcomposables.ShoppingAppBar
import com.example.shoppingtracker.ui.addevent.AddEventDetails
import kotlinx.coroutines.launch

@Composable
fun EventDetailPage(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit,
    navigateDown: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.eventDetailUiState.collectAsState();
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState();

    Scaffold(
        topBar = {
            ShoppingAppBar(
                title = "${viewModel.detailRoute.eventName}",
                canNavigateBack = true,
                navigateUp = navigateUp
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.addItem();
                        if (uiState.itemList.isNotEmpty()) {
                            lazyListState.animateScrollToItem(index = uiState.itemList.size - 1);
                        }
                    }
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text(text = "Add Item")
            }
        }
    ) {
        if (uiState.itemList.isEmpty()) {
            EmptyListComposable(
                message = "No items \n Add Items to proceed",
                modifier = modifier.padding(it)
            )
            return@Scaffold
        }
        ShoppingListItemComposable(
            listState = lazyListState,
            modifier = modifier.padding(it),
            eventDetails = uiState.eventDetailState,
            items = uiState.itemList,
            onEditModeChange = viewModel::updateItemEditMode,
            onValueChange = viewModel::updateUiStateItem,
            onItemDelete = {
                coroutineScope.launch {
                    viewModel.onDeleteItem(itemDetails = it)
                }
            },
            onItemUpdate = {
                coroutineScope.launch {
                    viewModel.updateItem(it)
                }
            }
        )
    }

}

@Composable
fun ShoppingListItemComposable(
    modifier: Modifier = Modifier,
    eventDetails: AddEventDetails,
    items: List<ItemUiState>,
    onEditModeChange: (ItemDetails) -> Unit,
    listState: LazyListState,
    onValueChange: (ItemDetails) -> Unit,
    onItemUpdate: (ItemDetails) -> Unit,
    onItemDelete: (ItemDetails) -> Unit,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        item {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                headlineContent = {
                    Text(
                        text = "Budget :\n$ ${eventDetails.initialBudget}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                trailingContent = {
                    Text(
                        text = "\$${eventDetails.totalCost}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
        items(items, key = {
            it.itemDetails.itemId
        }) { itemUiState ->
            SingleItemView(
                onValueChange = onValueChange,
                onItemUpdate = onItemUpdate,
                itemUiState = itemUiState,
                modifier = modifier,
                onEditModeChange = onEditModeChange,
                onItemDelete = onItemDelete
            )
        }
        item {
            Spacer(modifier = Modifier.size(60.dp))
        }
    }
}

@Composable
fun SingleItemView(
    onValueChange: (ItemDetails) -> Unit,
    onItemUpdate: (ItemDetails) -> Unit,
    onItemDelete: (ItemDetails) -> Unit,
    onEditModeChange: (ItemDetails) -> Unit,
    itemUiState: ItemUiState,
    modifier: Modifier = Modifier
) {
    var onDelete by remember {
        mutableStateOf(false)
    }
    val item = itemUiState.isEditable;
    if (onDelete) {
        AlertDialog(
            title = {
                Text(text = "Delete Item")
            },
            text = {
                Text(text = "Are you sure want to delete ${itemUiState.itemDetails.name}");
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
                    onItemDelete(itemUiState.itemDetails)
                }) {
                    Icon(Icons.Filled.Done, contentDescription = null);
                }
            },
        )
    }
    if (item) {
        EditItemList(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onValueChange,
            onItemUpdate = onItemUpdate,
            modifier = modifier,
        )
    } else {
        ListItem(
            leadingContent = {
                IconButton(onClick = {
                    onEditModeChange(itemUiState.itemDetails)
                }) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                }
            },
            headlineContent = {
                Text(text = itemUiState.itemDetails.name)
            },
            supportingContent = {
                Text(text = "Quantity : ${itemUiState.itemDetails.quantity}")
            },
            trailingContent = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = itemUiState.itemDetails.price)
                    IconButton(
                        onClick = {
                            onDelete = true;
                        },
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = null)
                    }
                }
            }
        )
    }

}