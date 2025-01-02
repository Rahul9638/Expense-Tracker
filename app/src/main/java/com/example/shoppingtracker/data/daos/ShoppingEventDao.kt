package com.example.shoppingtracker.data.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppingtracker.data.entities.ShoppingEvent
import com.example.shoppingtracker.data.entities.ShoppingItem
import kotlinx.coroutines.flow.Flow


@Dao
interface ShoppingEventDao {
    @Insert
    suspend fun insert(shoppingEvent: ShoppingEvent)

    @Update
    suspend fun update(shoppingEvent: ShoppingEvent)

    @Delete
    suspend fun delete(shoppingEvent: ShoppingEvent)

    @Query(
        "select se.id,se.name, se.initial_budget,se.event_date,se.completed, (sum(i.price * i.quantity)) as total_cost " +
                "from shopping_events as se left join shopping_items as i on se.id = i.event_id " +
                "group by se.id"
    )
    fun getAllEvent(): Flow<List<ShoppingEvent>>


    @Query(
        "select se.name, se.initial_budget,se.id, se.event_date,se.completed," +
                "(select sum(i.price * i.quantity) from shopping_items as i where i.event_id = se.id) as total_cost," +
                "i.item_id, i.event_id,i.item_name, i.price, i.quantity" +
                " from shopping_events as se left join shopping_items as i on se.id = i.event_id " +
                "where se.id = :id"
    )
    fun getEventAndItem(id: Long): Flow<Map<ShoppingEvent, List<ShoppingItem>>>


}