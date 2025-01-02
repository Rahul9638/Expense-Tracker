package com.example.shoppingtracker.ui.addevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppingtracker.customcomposables.ShoppingAppBar
import com.example.shoppingtracker.utils.formatDate
import kotlinx.coroutines.launch

@Composable
fun AddEventPage(
    navigateUp: () -> Unit,
    navigateDown: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEventViewModel = hiltViewModel(),
) {
    var coroutineScope = rememberCoroutineScope()
    Scaffold(topBar = {
        ShoppingAppBar(
            title = "Add Event",
            canNavigateBack = true,
            navigateUp = navigateUp,
        )
    }) {
        EventForm(
            modifier = modifier.padding(it),
            uiState = viewModel.addEventUiState,
            onEventValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveEvent();
                    navigateDown()
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventForm(
    uiState: AddEventUiState,
    onEventValueChange: (AddEventDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var openDatePickerDialog by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TextInputFields(onEventValueChange = onEventValueChange, uiState = uiState)
        Spacer(modifier = Modifier.size(10.dp))
        DatePickerButtonUI(datePickerState = datePickerState, onSelectRequested = {
            openDatePickerDialog = true
        })
        DatePickerUI(
            shouldOpenDialog = openDatePickerDialog,
            datePickerState = datePickerState,
            onDismissRequest = {
                openDatePickerDialog = false;
            },
            onClickCancelButton = {
                openDatePickerDialog = false
            },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let {
                    onEventValueChange(uiState.addEventDetails.copy(eventDate = formatDate(it)!!))
                }
                openDatePickerDialog = false
            },
        )

        Button(
            enabled = uiState.isEntryValid,
            onClick = onSaveClick, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Text(text = "Save")
        }
    }
}

@Composable
fun TextInputFields(
    onEventValueChange: (AddEventDetails) -> Unit,
    uiState: AddEventUiState,
    modifier: Modifier = Modifier,
) {
    Column() {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = uiState.addEventDetails.name,
            keyboardActions = KeyboardActions {
                ImeAction.Next
            },
            onValueChange = {
                onEventValueChange(uiState.addEventDetails.copy(name = it))
            },
            label = {
                Text(text = "Event Name")
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = uiState.addEventDetails.initialBudget,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions {
                ImeAction.Done

            },
            onValueChange = {
                onEventValueChange(uiState.addEventDetails.copy(initialBudget = it))
            },
            label = {
                Text(text = "Initial Budget (Optional)")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButtonUI(
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
    onSelectRequested: () -> Unit,
) {

    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        ElevatedButton(onClick = onSelectRequested) {
            Text(text = "Select Date")
        }
        Text(text = formatDate(datePickerState.selectedDateMillis) ?: "Nothing Selected")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(
    shouldOpenDialog: Boolean,
    datePickerState: DatePickerState,
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
    onClickCancelButton: () -> Unit,
) {

    if (shouldOpenDialog) {
        val confirmEnabled by remember {
            derivedStateOf {
                datePickerState.selectedDateMillis != null
            }
        }
        DatePickerDialog(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            dismissButton = {
                TextButton(onClick = onClickCancelButton) {
                    Text(text = "CANCEL")
                }
            },
            confirmButton = {
                TextButton(
                    enabled = confirmEnabled,
                    onClick = onConfirmButton
                ) {
                    Text(text = "Ok")
                }
            },

            ) {
            DatePicker(state = datePickerState)

        }
    }


}

@Preview(showBackground = true)
@Composable
private fun EventFromPreviewe() {
    EventForm(
        uiState = AddEventUiState(AddEventDetails(), isEntryValid = false),
        onEventValueChange = {},
        onSaveClick = { /*TODO*/ })

}