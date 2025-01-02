package com.example.shoppingtracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "shopping_items",
    foreignKeys = [
        ForeignKey(
            entity = ShoppingEvent::class,
            parentColumns = ["id"],
            childColumns = ["event_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
    )

data class ShoppingItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo("item_id") val itemId: Long = 0,
    @ColumnInfo("event_id") val eventId: Long,
    @ColumnInfo("item_name") val itemName: String,
    val price: Double = 0.0,
    val quantity: Double = 1.1,
)