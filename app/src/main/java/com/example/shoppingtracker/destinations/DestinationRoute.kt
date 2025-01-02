package com.example.shoppingtracker.destinations

import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

@Serializable
data class EventDetailRoute(val eventId: Long, val eventName: String)

@Serializable
object AddEventRoute