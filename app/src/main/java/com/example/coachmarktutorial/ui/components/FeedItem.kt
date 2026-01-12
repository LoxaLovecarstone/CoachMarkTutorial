package com.example.coachmarktutorial.ui.components

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.coachmarktutorial.ui.theme.Dimens // 우리가 만든 Dimens

@Composable
fun FeedItem(index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.PaddingLarge, vertical = Dimens.PaddingSmall),
        shape = RoundedCornerShape(Dimens.CornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(Dimens.PaddingLarge)) {
            // 1. 상단 프로필 영역
            Row(verticalAlignment = Alignment.CenterVertically) {
                // 프로필 이미지 (더미)
                Box(
                    modifier = Modifier
                        .size(Dimens.ProfileSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f))
                )
                Spacer(modifier = Modifier.width(Dimens.PaddingMedium))

                // 이름 및 시간
                Column {
                    Text(
                        text = "User $index",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "2 hours ago",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

            // 2. 메인 이미지 영역 (더미)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.FeedImageHeight)
                    .clip(RoundedCornerShape(Dimens.PaddingSmall))
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest), // 약간 진한 회색
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Feed Image",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

            // 3. 본문 텍스트
            Text(
                text = "디자인 시스템을 적용하니까 코드가 훨씬 깔끔해졌죠? ($index 번째 게시글)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}