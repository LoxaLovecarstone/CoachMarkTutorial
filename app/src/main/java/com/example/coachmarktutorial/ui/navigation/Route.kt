package com.example.coachmarktutorial.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Route(
    val path: String,
    val title: String,
    val icon: ImageVector? = null // 바텀바에 쓸 아이콘. 아이콘 없으면 null
) {

    // 바텀 내비
    data object Home : Route("home", "My Feed", Icons.Default.Home)
    data object Profile : Route("profile", "My Profile", Icons.Default.AccountCircle)

    // 바텀 내비 없는 것
    data object Post : Route("post", "새 글 작성")
    data object Search : Route("search", "검색")
    data object ProfileEdit : Route("profile_edit", "프로필 수정")

    // 바텀바에 보여줄 화면들만 모아둔 리스트
    companion object {
        val bottomBarRoutes: List<Route>
            get() = listOf(Home, Profile)
    }
}