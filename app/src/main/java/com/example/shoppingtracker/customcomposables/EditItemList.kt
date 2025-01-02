package com.example.shoppingtracker.customcomposables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.shoppingtracker.ui.eventdetails.ItemDetails

@Composable
fun EditItemList(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
    onItemUpdate: (ItemDetails) -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItem(
        trailingContent = {
            IconButton(onClick = {
                onItemUpdate(itemDetails);
            }) {
                Icon(Icons.Default.Done, contentDescription = null)
            }
        },
        headlineContent = {
            OutlinedTextField(
                value = itemDetails.name,
                label = {
                    Text(text = "Name")
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                onValueChange = {
                    onValueChange(itemDetails.copy(name = it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 4.dp)
            )
        },
        supportingContent = {
            Row {
                OutlinedTextField(
                    value = itemDetails.quantity,
                    label = {
                        Text(text = "Quantity")
                    },

                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Number,
                    ),
                    onValueChange = {
                        onValueChange(itemDetails.copy(quantity = it))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 4.dp)
                )
                OutlinedTextField(
                    value = itemDetails.price,
                    label = {
                        Text(text = "Price")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    ),
                    onValueChange = {
                        onValueChange(itemDetails.copy(price = it))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(all = 4.dp)
                )
            }
        }

    )
}