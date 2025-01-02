package com.example.shoppingtracker.customcomposables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingAppBar(
    modifier: Modifier = Modifier,
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {}
) {

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}