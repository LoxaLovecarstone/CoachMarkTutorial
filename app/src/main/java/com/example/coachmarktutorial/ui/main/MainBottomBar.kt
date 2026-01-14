package com.example.coachmarktutorial.ui.main

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.coachmarktutorial.ui.navigation.Route

@Composable
fun MainBottomBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    // 바텀바를 보여줄 경로인지 확인
    val showBottomBar = currentRoute in Route.bottomBarRoutes.map { it.path }

    if (showBottomBar) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Route.bottomBarRoutes.forEach { route ->
                NavigationBarItem(
                    icon = { Icon(route.icon!!, contentDescription = route.title) },
                    label = { Text(route.title) },
                    selected = currentRoute == route.path,
                    onClick = { onNavigate(route.path) }
                )
            }
        }
    }
}