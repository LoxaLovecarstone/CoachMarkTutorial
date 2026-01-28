package com.example.coachmarktutorial.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.LocalCoachMarkState
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.components.FeedItem
import com.example.coachmarktutorial.ui.theme.Dimens
import kotlinx.coroutines.delay

@Composable
fun SearchScreen(
    onPostClick: (Long) -> Unit, // 검색 결과 클릭 시 상세로 이동 (나중을 위해)
    onEditClick: (Long) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    // 코치마크 상태
    val coachMarkState = LocalCoachMarkState.current

    // 컨텍스트 메뉴 상태 관리
    val density = LocalDensity.current
    var isContextMenuExpanded by remember { mutableStateOf(false) }
    var contextMenuOffset by remember { mutableStateOf(DpOffset.Zero) }

    val searchInputTarget = if (coachMarkState.currentTarget == CoachMarkTarget.SEARCH_DELETE_CONFIRM) {
        CoachMarkTarget.SEARCH_DELETE_CONFIRM
    } else {
        CoachMarkTarget.SEARCH_INPUT
    }

    LaunchedEffect(coachMarkState.currentTarget) {
        when (coachMarkState.currentTarget) {
            CoachMarkTarget.SEARCH_INPUT -> {
                delay(500)  // 검색어 자동 입력
                viewModel.onQueryChange("코치마크")
            }
            // 롱프레스 유도 단계: 메뉴는 닫혀있어야 함
            // [수정] 삭제를 위한 롱프레스 단계(SEARCH_RESULT_ITEM_DELETE)도 여기에 포함
            CoachMarkTarget.SEARCH_RESULT_ITEM, CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE -> {
                isContextMenuExpanded = false
            }
            // 수정/삭제 단계: 메뉴가 열려 있어야 함 (자동으로 열어주거나 유지)
            CoachMarkTarget.CONTEXT_EDIT, CoachMarkTarget.CONTEXT_DELETE -> {
                if (!isContextMenuExpanded) isContextMenuExpanded = true
            }
            else -> Unit
        }
    }

    // 결과 나오면 -> 롱프레스 유도 단계로 이동
    LaunchedEffect(results.size, coachMarkState.currentTarget) {
        if (coachMarkState.currentTarget == CoachMarkTarget.SEARCH_INPUT && results.isNotEmpty()) {
            delay(800)
            coachMarkState.moveNext() // -> SEARCH_RESULT_ITEM
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingLarge)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onQueryChange,
            placeholder = { Text("제목이나 내용으로 검색...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.onQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Text")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                // 검색 입력창을 코치마크 타겟으로 설정
                .coachMarkTarget(searchInputTarget),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        if (query.isNotEmpty() && results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "검색 결과가 없습니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(vertical = Dimens.PaddingSmall)
            ) {
                itemsIndexed(results) { index, post ->
                    // 첫 번째 아이템만 타겟팅
                    val isTargetItem = (index == 0)

                    // Box에 동적 타겟 적용
                    // 메뉴 아이템 대신, 이 'Box(리스트 아이템)'가 계속 타겟 역할을 해줘야 함
                    val targetStep = when (coachMarkState.currentTarget) {
                        CoachMarkTarget.CONTEXT_EDIT -> CoachMarkTarget.CONTEXT_EDIT
                        CoachMarkTarget.CONTEXT_DELETE -> CoachMarkTarget.CONTEXT_DELETE
                        CoachMarkTarget.SEARCH_RESULT_ITEM -> CoachMarkTarget.SEARCH_RESULT_ITEM
                        // [수정] 삭제 유도 단계 타겟 매핑 추가
                        CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE -> CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE
                        else -> null
                    }

                    val itemModifier = if (isTargetItem && targetStep != null) {
                        Modifier.coachMarkTarget(targetStep)
                    } else {
                        Modifier
                    }

                    // Box로 감싸서 LongPress 감지 및 메뉴 표시
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // 롱프레스 감지 로직
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = { onPostClick(post.id) },
                                    onLongPress = { pressOffset ->
                                        // 롱프레스 위치에 메뉴 띄우기 위해 오프셋 저장
                                        contextMenuOffset = DpOffset(
                                            with(density) { pressOffset.x.toDp() },
                                            with(density) { pressOffset.y.toDp() }
                                        )
                                        isContextMenuExpanded = true

                                        // 튜토리얼 중이라면 다음 단계(수정 메뉴 선택)로 이동
                                        // [Case 1] 처음 롱프레스 -> 수정하기 메뉴로
                                        if (coachMarkState.currentTarget == CoachMarkTarget.SEARCH_RESULT_ITEM && isTargetItem) {
                                            coachMarkState.moveNext() // -> CONTEXT_EDIT
                                        }
                                        // [Case 2] 수정 후 돌아와서 롱프레스 -> 삭제하기 메뉴로
                                        else if (coachMarkState.currentTarget == CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE && isTargetItem) {
                                            coachMarkState.moveNext() // -> CONTEXT_DELETE
                                        }
                                    }
                                )
                            }
                            .then(itemModifier)
                    ) {
                        FeedItem(post = post)

                        // 컨텍스트 메뉴
                        if (isTargetItem) {
                            DropdownMenu(
                                expanded = isContextMenuExpanded,
                                onDismissRequest = {
                                    // 튜토리얼 중이 아닐 때만 닫기 허용
                                    val isTutorial = coachMarkState.currentTarget == CoachMarkTarget.CONTEXT_EDIT ||
                                            coachMarkState.currentTarget == CoachMarkTarget.CONTEXT_DELETE
                                    if (!isTutorial) isContextMenuExpanded = false
                                },
                                offset = contextMenuOffset
                            ) {
                                // 1. 수정하기 메뉴
                                DropdownMenuItem(
                                    text = { Text("수정하기") },
                                    onClick = {
                                        if (coachMarkState.currentTarget == CoachMarkTarget.CONTEXT_EDIT) {
                                            isContextMenuExpanded = false

                                            // [수정] 이제 수정 전용 단계(POST_EDIT_CONTENT)로 점프합니다!
                                            // POST_CONTENT가 아닌 POST_EDIT_CONTENT로 보내야 PostScreen에서 수정 모드 로직이 바로 작동함
                                            coachMarkState.setCurrentStep(CoachMarkTarget.POST_EDIT_CONTENT)

                                            onEditClick(post.id)
                                        } else if (coachMarkState.currentTarget == null) {
                                            isContextMenuExpanded = false
                                            onEditClick(post.id)
                                        }
                                    },
                                    modifier = Modifier
                                )

                                // 2. 삭제하기 메뉴
                                DropdownMenuItem(
                                    text = { Text("삭제하기", color = MaterialTheme.colorScheme.error) },
                                    onClick = {
                                        if (coachMarkState.currentTarget == CoachMarkTarget.CONTEXT_DELETE) {
                                            viewModel.deletePost(post)
                                            isContextMenuExpanded = false

                                            // [Fix] 삭제 후 홈으로 가지 않고 '삭제 확인' 단계로 변경
                                            coachMarkState.setCurrentStep(CoachMarkTarget.SEARCH_DELETE_CONFIRM)
                                        } else if (coachMarkState.currentTarget == null) {
                                            viewModel.deletePost(post)
                                            isContextMenuExpanded = false
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}