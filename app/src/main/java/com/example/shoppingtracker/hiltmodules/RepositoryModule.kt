package com.example.shoppingtracker.hiltmodules

import com.example.shoppingtracker.data.repos.ShoppingEventRepository
import com.example.shoppingtracker.data.repos.ShoppingEventRepositoryImpl
import com.example.shoppingtracker.data.repos.ShoppingItemRepository
import com.example.shoppingtracker.data.repos.ShoppingItemRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindShoppingEventRepository(impl: ShoppingEventRepositoryImpl): ShoppingEventRepository;

    @Binds
    abstract fun bindShoppingItemRepository(impl: ShoppingItemRepositoryImpl): ShoppingItemRepository;
}