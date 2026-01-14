package com.example.coachmarktutorial.ui.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    currentRoute: String,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    // 타이틀 계산 로직
    val currentTitle = when (currentRoute) {
        Route.Post.path -> Route.Post.title
        Route.Search.path -> Route.Search.title
        Route.ProfileEdit.path -> Route.ProfileEdit.title
        else -> Route.bottomBarRoutes.find { it.path == currentRoute }?.title ?: "Coach Mark"
    }

    val isHome = currentRoute == Route.Home.path
    val isProfile = currentRoute == Route.Profile.path

    TopAppBar(
        title = {
            Text(
                text = currentTitle,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            // 홈이나 프로필이 아니면 뒤로가기 버튼 표시
            if (!isHome && !isProfile) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        actions = {
            if (isHome) {
                IconButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.coachMarkTarget(CoachMarkTarget.REFRESH)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        }
    )
}