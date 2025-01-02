package com.example.shoppingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shoppingtracker.destinations.AddEventRoute
import com.example.shoppingtracker.destinations.EventDetailRoute
import com.example.shoppingtracker.destinations.HomeRoute
import com.example.shoppingtracker.ui.addevent.AddEventPage
import com.example.shoppingtracker.ui.eventdetails.EventDetailPage
import com.example.shoppingtracker.ui.home.HomePage
import com.example.shoppingtracker.ui.theme.ShoppingTrackerTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp(modifier: Modifier = Modifier) {

    ShoppingTrackerTheme {
        val navController = rememberNavController();
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
        ) {
            composable<HomeRoute> {
                HomePage(
                    navigateToAddEvent = {
                        navController.navigate(route = AddEventRoute)
                    },
                    navigateToEventDetail = { shoppingEvent ->
                        navController.navigate(
                            route = EventDetailRoute(
                                eventId = shoppingEvent.id,
                                eventName = shoppingEvent.name
                            )
                        )
                    },
                )
            }

            composable<AddEventRoute> {
                AddEventPage(
                    navigateUp = { navController.navigateUp() },
                    navigateDown = { navController.popBackStack() },
                )
            }

            composable<EventDetailRoute> {
                EventDetailPage(navigateUp = {
                    navController.navigateUp();
                }, navigateDown = { /*TODO*/ })
            }

        }
    }
}