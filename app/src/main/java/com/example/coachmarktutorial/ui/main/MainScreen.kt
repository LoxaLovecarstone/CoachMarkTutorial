package com.example.coachmarktutorial.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.coachmarktutorial.ui.coachmark.CoachMarkOverlay
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.LocalCoachMarkState
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.rememberCoachMarkState
import com.example.coachmarktutorial.ui.components.ExpandableFab
import com.example.coachmarktutorial.ui.navigation.MainNavGraph
import com.example.coachmarktutorial.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Route.Home.path
    val isHome = currentRoute == Route.Home.path

    var isFabExpanded by remember { mutableStateOf(false) }

    val coachMarkState = rememberCoachMarkState()

    LaunchedEffect(Unit) {
        if (!viewModel.isTutorialStarted) {
            // 처음 실행이라면: 1초 뒤 시작
            delay(1000)
            coachMarkState.setCurrentStep(CoachMarkTarget.REFRESH)
            viewModel.isTutorialStarted = true
        } // 그 이후는 아무것도 하지 않음
    }

    LaunchedEffect(currentRoute) {
        isFabExpanded = false
    }

    LaunchedEffect(coachMarkState.currentTarget) {
        when (coachMarkState.currentTarget) {
            CoachMarkTarget.POST_BUTTON -> {
                isFabExpanded = true
            }

            CoachMarkTarget.POST_CATEGORY -> {
                isFabExpanded = false
                navController.navigate("post?postId=-1") // 새 글 작성
            }

            // FAB를 열어야 하는 단계들에서는 닫힌 상태 보장
            // 처음 글 쓸 때(FAB)와 검색하러 갈 때(FAB_SEARCH_OPEN) 둘 다 해당
            CoachMarkTarget.FAB, CoachMarkTarget.FAB_SEARCH_OPEN -> {
                isFabExpanded = false
            }

            // 글쓰기 완료 후 FAB로 돌아오면 메뉴 열기 (검색 버튼 누를 땐 열림)
            CoachMarkTarget.FAB_SEARCH -> isFabExpanded = true

            // 검색 버튼 누르면 -> 검색 화면으로 이동
            CoachMarkTarget.SEARCH_INPUT -> {
                isFabExpanded = false
                navController.navigate(Route.Search.path)
            }

            // 튜토리얼 완료 단계가 되면 무조건 홈으로 이동!
            CoachMarkTarget.TUTORIAL_COMPLETED -> {
                navController.navigate(Route.Home.path) {
                    popUpTo(Route.Home.path) { inclusive = true }
                }
            }

            else -> {
                // 필요 시 다른 처리
            }
        }
    }


    CompositionLocalProvider(LocalCoachMarkState provides coachMarkState) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    MainTopBar(
                        currentRoute = currentRoute,
                        onBackClick = { navController.popBackStack() },
                        onRefreshClick = { /* TODO: Refresh Action */ }
                    )
                },
                bottomBar = {
                    MainBottomBar(
                        currentRoute = currentRoute,
                        onNavigate = { targetRoute ->
                            navController.navigate(targetRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                },
                floatingActionButton = {
                    if (isHome) {
                        // [동적 타겟] 현재 단계에 맞춰 FAB의 타겟 이름을 바꿔줌
                        // 검색하러 가는 단계면 FAB_SEARCH_OPEN, 아니면 기존 FAB
                        val fabTarget = if (coachMarkState.currentTarget == CoachMarkTarget.FAB_SEARCH_OPEN) {
                            CoachMarkTarget.FAB_SEARCH_OPEN
                        } else {
                            CoachMarkTarget.FAB
                        }

                        ExpandableFab(
                            modifier = Modifier.coachMarkTarget(fabTarget),
                            isExpanded = isFabExpanded,
                            onFabClick = {
                                isFabExpanded = !isFabExpanded

                                // FAB 클릭 시(메뉴 열기) 다음 단계가 '글 검색'이라면 넘겨줌
                                if (coachMarkState.currentTarget == CoachMarkTarget.FAB) {
                                    coachMarkState.moveNext() // -> POST_BUTTON (기존 흐름)
                                }
                                // 글 확인 후 검색하러 갈 때
                                else if (coachMarkState.currentTarget == CoachMarkTarget.FAB_SEARCH_OPEN) {
                                    coachMarkState.moveNext() // -> FAB_SEARCH (검색 흐름)
                                }
                                // [New] 엔딩 단계에서 FAB를 누르면 튜토리얼 완전 종료
                                else if (coachMarkState.currentTarget == CoachMarkTarget.TUTORIAL_COMPLETED) {
                                    coachMarkState.setCurrentStep(null) // 끝!
                                }
                            },
                            onPostClick = {
                                isFabExpanded = false
                                navController.navigate("post?postId=-1")
                            },
                            onSearchClick = {
                                isFabExpanded = false
                                navController.navigate(Route.Search.path)
                                // 검색 버튼 클릭 시 다음 단계(검색어 입력)로 넘겨줌
                                if (coachMarkState.currentTarget == CoachMarkTarget.FAB_SEARCH) {
                                    coachMarkState.moveNext() // -> SEARCH_INPUT
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                MainNavGraph(
                    navController = navController,
                    innerPadding = innerPadding
                )
            }
            CoachMarkOverlay()
        }
    }
}