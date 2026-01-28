package com.example.coachmarktutorial.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coachmarktutorial.data.model.post.Category
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.LocalCoachMarkState
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.theme.Dimens
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postId: Long = -1L, // -1이면 새 글, 아니면 수정
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    // 전역 상태인 코치마크 상태를 가져옴
    val coachMarkState = LocalCoachMarkState.current

    val isEditMode = postId != -1L

    // State 관리
    // 튜토리얼 중 값 자동 입력을 위해 ViewModel 대신 UI가 상태를 직접 들고 있음
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(Category.DAILY) }

    // [데이터 로드] 수정 모드일 때 기존 데이터 불러오기
    LaunchedEffect(Unit) {
        if (isEditMode) {
            val post = viewModel.getPost(postId)
            if (post != null) {
                title = post.title
                content = post.content
                selectedCategory = post.category
            }
        }
    }

    // [시나리오 시뮬레이션]
    // 코치마크 단계(Target)가 변할 때마다 UI 상태를 자동으로 변경해주는 로직
    LaunchedEffect(coachMarkState.currentTarget) {
        when (coachMarkState.currentTarget) {
            CoachMarkTarget.POST_CATEGORY -> {
                expanded = false // 메뉴 닫기 (초기화)
            }
            CoachMarkTarget.POST_CATEGORY_OPTION -> {
                // 메뉴 열기 (사용자가 입력창을 클릭했다고 가정)
                delay(100)
                expanded = true
            }
            CoachMarkTarget.POST_TITLE -> {
                expanded = false // 제목 입력 단계로 넘어가면 메뉴 닫기
            }

            // [작성 모드] 내용 입력 단계
            CoachMarkTarget.POST_CONTENT -> {
                if (!isEditMode) {
                    title = "코치마크로 작성된 자동 제목"
                }
            }

            // [작성 모드] 등록 버튼 단계
            CoachMarkTarget.POST_SUBMIT -> {
                if (!isEditMode) {
                    content = "이렇게 튜토리얼 중에 자동으로 내용을 채워줄 수 있습니다."
                }
            }

            // [수정 모드] 내용 입력 단계 (New)
            CoachMarkTarget.POST_EDIT_CONTENT -> {
                // 수정 화면 들어오자마자 여기 걸림.
                // 사용자가 '다음'을 누르면 -> POST_EDIT_SUBMIT으로 넘어감
            }

            // [수정 모드] 수정 버튼 단계 (New)
            CoachMarkTarget.POST_EDIT_SUBMIT -> {
                // 사용자가 '내용 수정' 단계에서 클릭하여 이 단계로 넘어오면
                // 비로소 그때 내용이 추가됩니다.
                if (isEditMode) content = "내용 교체됨"
            }

            // [저장 및 화면 닫기 트리거]
            // Case 1: 작성 모드 (POST_SUBMIT -> HOME_POST_CHECK)
            // 작성 모드일 때만 여기서 저장
            CoachMarkTarget.HOME_POST_CHECK -> {
                if (!isEditMode) {
                    viewModel.savePost(postId, title, selectedCategory, content) {
                        onSaveClick()
                    }
                }
            }

            // Case 2: 수정 모드 (POST_EDIT_SUBMIT -> SEARCH_RESULT_ITEM_DELETE)
            // 수정 완료 후, 삭제를 위해 검색 결과 화면으로 돌아가는 단계
            // (Enum 순서: POST_EDIT_SUBMIT -> SEARCH_RESULT_ITEM_DELETE)
            // [수정] 수정 모드일 때만 여기서 저장
            CoachMarkTarget.SEARCH_RESULT_ITEM_DELETE -> {
                if (isEditMode) {
                    viewModel.savePost(postId, title, selectedCategory, content) {
                        onSaveClick()
                    }
                }
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingLarge)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                // 인터랙션 제어
                // 튜토리얼('옵션 선택' 단계) 중일 때는 사용자가 임의로 메뉴를 닫지 못하게 막음
                if (coachMarkState.currentTarget != CoachMarkTarget.POST_CATEGORY_OPTION) {
                    expanded = !expanded
                }
            }
        ) {
            val targetStep = if (coachMarkState.currentTarget == CoachMarkTarget.POST_CATEGORY_OPTION) {
                CoachMarkTarget.POST_CATEGORY_OPTION
            } else {
                CoachMarkTarget.POST_CATEGORY
            }

            // 수정 모드일 때는 카테고리에 타겟을 달지 않음
            val categoryModifier = if (!isEditMode) Modifier.coachMarkTarget(targetStep) else Modifier

            OutlinedTextField(
                value = selectedCategory.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("카테고리") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .then(categoryModifier)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    if (coachMarkState.currentTarget != CoachMarkTarget.POST_CATEGORY_OPTION) {
                        expanded = false
                    }
                }
            ) {
                Category.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.label) },
                        onClick = {
                            if (coachMarkState.currentTarget == CoachMarkTarget.POST_CATEGORY_OPTION) {
                                if (category == Category.QUESTION) {
                                    selectedCategory = category
                                    expanded = false
                                    coachMarkState.moveNext()
                                }
                            } else {
                                selectedCategory = category
                                expanded = false
                            }
                        },
                        enabled = coachMarkState.currentTarget != CoachMarkTarget.POST_CATEGORY_OPTION
                                || category == Category.QUESTION
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        // 수정 모드일 때는 제목에 타겟을 달지 않음
        val titleModifier = if (!isEditMode) Modifier.coachMarkTarget(CoachMarkTarget.POST_TITLE) else Modifier

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier
                .fillMaxWidth()
                .then(titleModifier),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        // [수정] 내용 입력창 타겟 분리
        // 작성 모드면 POST_CONTENT, 수정 모드면 POST_EDIT_CONTENT
        val contentTarget = if (isEditMode) CoachMarkTarget.POST_EDIT_CONTENT else CoachMarkTarget.POST_CONTENT

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .coachMarkTarget(contentTarget), // 동적 타겟 적용
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        // [핵심] 저장 버튼 클릭 로직
        // 현재 모드에 따라 타겟을 다르게 설정 (문구 차별화를 위해)
        val submitTarget = if (isEditMode) CoachMarkTarget.POST_EDIT_SUBMIT else CoachMarkTarget.POST_SUBMIT

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.savePost(postId, title, selectedCategory, content) {
                        onSaveClick() // 코치마크와 상관없이 일반 클릭 시 닫기
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .coachMarkTarget(submitTarget), // 동적 타겟 적용
            enabled = title.isNotBlank() && content.isNotBlank()
        ) {
            Text(if (isEditMode) "수정하기" else "등록하기")
        }
    }
}