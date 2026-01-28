package com.example.coachmarktutorial.ui.navigation

import android.R.attr.type
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        composable(
            route = "post?postId={postId}",
            arguments = listOf(navArgument("postId") {
                type = NavType.LongType
                defaultValue = -1L
            })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getLong("postId") ?: -1L

            PostScreen(
                postId = postId, // PostScreen에 전달
                onBackClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }
        composable(Route.Search.path) {
            SearchScreen(
                onPostClick = { /* 상세 이동 (생략) */ },
                onEditClick = { postId ->
                    // [New] 수정 화면으로 이동 (ID 전달)
                    navController.navigate("post?postId=$postId")
                },
            )
        }
        composable(Route.ProfileEdit.path) {
            ProfileEditScreen(onSaveSuccess = { navController.popBackStack() })
        }

    }
}