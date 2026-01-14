package com.example.coachmarktutorial.ui.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coachmarktutorial.ui.home.HomeScreen
import com.example.coachmarktutorial.ui.post.PostScreen
import com.example.coachmarktutorial.ui.profile.ProfileScreen
import com.example.coachmarktutorial.ui.profile.edit.ProfileEditScreen
import com.example.coachmarktutorial.ui.search.SearchScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues
) {
    val context = LocalContext.current // Toast용 Context

    NavHost(
        navController = navController,
        startDestination = Route.Home.path,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Route.Home.path) {
            HomeScreen()
        }
        composable(Route.Profile.path) {
            ProfileScreen(
                onEditClick = {
                    navController.navigate(Route.ProfileEdit.path)
                }
            )
        }
        composable(Route.Post.path) {
            PostScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }
        composable(Route.Search.path) {
            SearchScreen(
                onPostClick = { postId ->
                    Toast.makeText(context, "Clicked post: $postId", Toast.LENGTH_SHORT).show()
                }
            )
        }
        composable(Route.ProfileEdit.path) {
            ProfileEditScreen(
                onSaveSuccess = { navController.popBackStack() }
            )
        }
    }
}