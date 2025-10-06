package com.example.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shoppinglist.navigation.Screen
import com.example.shoppinglist.screens.*
import com.example.shoppinglist.ui.theme.ShoppingListTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShoppingListTheme {
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                text = "Menu",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(16.dp)
                            )
                            NavigationDrawerItem(
                                label = { Text("Settings ⚙️") },
                                selected = currentRoute == Screen.Settings.route,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    navController.navigate(Screen.Settings.route)
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Shopping List") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                                    titleContentColor = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        },
                        bottomBar = {
                            NavigationBar {
                                val items = listOf(Screen.Home, Screen.Profile)
                                items.forEach { screen ->
                                    NavigationBarItem(
                                        selected = currentRoute == screen.route,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(Screen.Home.route)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            when (screen) {
                                                Screen.Home -> Icon(Icons.Default.Home, null)
                                                Screen.Profile -> Icon(Icons.Default.Person, null)
                                                else -> {}
                                            }
                                        },
                                        label = {
                                            Text(
                                                text = screen.route.replaceFirstChar { it.uppercase() }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Home.route) { HomeScreen() }
                            composable(Screen.Profile.route) { ProfileScreen() }
                            composable(Screen.Settings.route) { SettingsScreen() }
                        }
                    }
                }
            }
        }
    }
}
