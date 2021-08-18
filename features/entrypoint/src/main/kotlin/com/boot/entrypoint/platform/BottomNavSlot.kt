package com.boot.entrypoint.platform

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun <T> BtmSlot(screenItems: Set<T>, parser: BottomNavTypeClasses<T>) =
  parser.run {
    val navController = rememberNavController()
    //	val items = listOf(Screen.Recipes, Screen.Plan, Screen.Basket, Screen.Profile)
    navController.currentDestination
    Scaffold(
      bottomBar = {
        BottomNavigation {
          val navBackStackEntry by navController.currentBackStackEntryAsState()
          val currentDestination = navBackStackEntry?.destination
          screenItems.forEach { item ->
            BottomNavigationItem(
              icon = { Icon(item.icon(), contentDescription = null) },
              label = { Text(item.label()) },
              selected = currentDestination?.hierarchy?.any { it.route == item.route() } == true,
              onClick = {
                navController.navigate(item.route()) {
                  // Pop up to the start destination of the graph to
                  // avoid building up a large stack of destinations
                  // on the back stack as users select items
                  popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                  // Avoid multiple copies of the same destination when
                  // reselecting the same item
                  launchSingleTop = true
                  // Restore state when reselecting a previously selected item
                  restoreState = true
                }
              }
            )
          }
        }
      }
    ) { innerPadding ->
      NavHost(
        navController,
        startDestination = screenItems.first().route(),
        Modifier.padding(innerPadding)
      ) { screenItems.forEach { item -> composable(item.route()) { item.Screen() } } }
    }
  }
