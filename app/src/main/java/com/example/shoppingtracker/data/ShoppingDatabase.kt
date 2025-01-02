package com.example.shoppingtracker.data

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shoppingtracker.data.daos.ShoppingEventDao
import com.example.shoppingtracker.data.daos.ShoppingItemDao
import com.example.shoppingtracker.data.entities.ShoppingEvent
import com.example.shoppingtracker.data.entities.ShoppingItem


@Database([ShoppingItem::class, ShoppingEvent::class], version = 1)
abstract class ShoppingDatabase : RoomDatabase() {

    abstract fun shoppingEventDao(): ShoppingEventDao;
    abstract fun shoppingItemDao(): ShoppingItemDao;

    companion object {
        private val DATABASE_NAME = "shopping_database";

        @Volatile
        var Instances: ShoppingDatabase? = null;

        fun getDatabase(context: Context): ShoppingDatabase {
            return Instances ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ShoppingDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    Instances = it;
                }
            }
        }
    }

}