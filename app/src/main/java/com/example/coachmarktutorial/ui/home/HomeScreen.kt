package com.example.coachmarktutorial.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import com.example.coachmarktutorial.ui.coachmark.LocalCoachMarkState
import com.example.coachmarktutorial.ui.coachmark.coachMarkTarget
import com.example.coachmarktutorial.ui.components.FeedItem
import com.example.coachmarktutorial.ui.theme.Dimens

@Composable
fun HomeScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val coachMarkState = LocalCoachMarkState.current

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = Dimens.PaddingSmall)
        ) {
            itemsIndexed(posts) { index, post ->

                // 방금 쓴 글은 리스트의 맨 위(0번)에 있음
                val isNewPost = (index == 0)

                // FeedItem을 감싸서 클릭 이벤트와 타겟을 처리
                Box(
                    modifier = Modifier
                        .clickable {
                            // '작성된 글 확인' 단계이고, 첫 번째 글을 눌렀을 때
                            if (coachMarkState.currentTarget == CoachMarkTarget.HOME_POST_CHECK && isNewPost) {
                                // -> 다음 단계(FAB_SEARCH_OPEN)로 넘어감
                                // Enum 순서에 따라 자동으로
                                coachMarkState.moveNext()
                            }
                        }
                        // 첫 번째 글에만 하이라이트
                        .then(
                            if (isNewPost) Modifier.coachMarkTarget(CoachMarkTarget.HOME_POST_CHECK)
                            else Modifier
                        )
                ) {
                    FeedItem(post = post)
                }
            }
        }

        if (coachMarkState.currentTarget == CoachMarkTarget.TUTORIAL_COMPLETED) {
            Spacer(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(1.dp) // 아주 작은 투명 점
                    .coachMarkTarget(CoachMarkTarget.TUTORIAL_COMPLETED)
            )
            // 사용자가 화면을 터치해서 튜토리얼을 종료할 수 있게 처리
            // (CoachMarkOverlay가 클릭을 가로채지만,
            // overlay에서 moveNext()하면 currentTarget이 null이 되면서 종료됨)
        }
    }
}