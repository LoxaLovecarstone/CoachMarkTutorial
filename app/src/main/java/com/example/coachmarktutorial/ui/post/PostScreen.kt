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
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    // 전역 상태인 코치마크 상태를 가져옴
    val coachMarkState = LocalCoachMarkState.current

    // State 관리
    // 튜토리얼 중 값 자동 입력을 위해 ViewModel 대신 UI가 상태를 직접 들고 있음
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(Category.DAILY) }

    // 시나리오 시뮬레이션
    // 코치마크 단계(Target)가 변할 때마다 UI 상태를 자동으로 변경해주는 로직
    LaunchedEffect(coachMarkState.currentTarget) {
        when (coachMarkState.currentTarget) {
            CoachMarkTarget.POST_CATEGORY -> {
                expanded = false // 메뉴 닫기 (초기화)
            }
            CoachMarkTarget.POST_CATEGORY_OPTION -> {
                // 메뉴 열기 (사용자가 입력창을 클릭했다고 가정)
                // UI 렌더링 안정성을 위해 약간의 딜레이 후 오픈
                delay(100)
                expanded = true
            }
            CoachMarkTarget.POST_TITLE -> {
                expanded = false // 제목 입력 단계로 넘어가면 메뉴 닫기
            }
            CoachMarkTarget.POST_CONTENT -> {
                title = "코치마크로 작성된 자동 제목" // 제목 자동 입력
            }
            CoachMarkTarget.POST_SUBMIT -> {
                content = "이렇게 튜토리얼 중에 자동으로 내용을 채워줄 수 있습니다." // 내용 자동 입력
            }
            CoachMarkTarget.SEARCH_INPUT -> {
                // 튜토리얼 종료: 실제 데이터 저장 후 화면 이동
                viewModel.savePost(title, selectedCategory, content) {
                    onSaveClick()
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
            // 드롭다운 메뉴(Popup)는 오버레이(Canvas)보다 상위 레이어(Window)에 그려짐.
            // 따라서 메뉴 아이템에 구멍을 뚫으면 좌표가 어긋나거나(0,0), 메뉴에 가려짐.
            // 해결책: 메뉴가 열려있는 'POST_CATEGORY_OPTION' 단계에서도
            // 타겟을 '입력창(POST_CATEGORY)' 위치에 계속 유지하여 배경만 어둡게 하고 메뉴는 밝게 띄움.
            val targetStep = if (coachMarkState.currentTarget == CoachMarkTarget.POST_CATEGORY_OPTION) {
                CoachMarkTarget.POST_CATEGORY_OPTION
            } else {
                CoachMarkTarget.POST_CATEGORY
            }

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
                    .coachMarkTarget(targetStep) // 입력창 위치 정보를 수집
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    // [닫기 방지]
                    // 튜토리얼 중엔 배경을 클릭해도 메뉴가 닫히지 않게 함 (사용자가 정답을 누를 때까지 대기)
                    if (coachMarkState.currentTarget != CoachMarkTarget.POST_CATEGORY_OPTION) {
                        expanded = false
                    }
                }
            ) {
                Category.entries.forEach { category ->
                    // 메뉴 아이템 처리
                    // 여기서는 Modifier.coachMarkTarget을 제거함 (Popup 좌표 문제 해결)
                    // 대신 로직으로 정답/오답을 처리함

                    DropdownMenuItem(
                        text = { Text(category.label) },
                        onClick = {
                            // 튜토리얼 진행 중일 때 ('옵션 선택' 단계)
                            if (coachMarkState.currentTarget == CoachMarkTarget.POST_CATEGORY_OPTION) {
                                // '질문'을 클릭해야만 넘어감
                                if (category == Category.QUESTION) {
                                    selectedCategory = category
                                    expanded = false
                                    coachMarkState.moveNext() // 다음 단계로 이동
                                }
                                // 다른 걸 누르면 무시함 (메뉴 안 닫힘)
                            } else {
                                // 일반 사용 모드
                                selectedCategory = category
                                expanded = false
                            }
                        },
                        modifier = Modifier,

                        // 시각적 힌트
                        // 튜토리얼 중일 땐 정답('질문')만 활성화하고 나머지는 흐리게 처리(disable)
                        enabled = coachMarkState.currentTarget != CoachMarkTarget.POST_CATEGORY_OPTION
                                || category == Category.QUESTION
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier
                .fillMaxWidth()
                .coachMarkTarget(CoachMarkTarget.POST_TITLE),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .coachMarkTarget(CoachMarkTarget.POST_CONTENT)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.savePost(title, selectedCategory, content) {
                        onSaveClick()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .coachMarkTarget(CoachMarkTarget.POST_SUBMIT),
            enabled = title.isNotBlank() && content.isNotBlank()
        ) {
            Text("등록하기")
        }
    }
}