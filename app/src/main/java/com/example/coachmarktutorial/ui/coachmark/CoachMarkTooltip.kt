package com.example.coachmarktutorial.ui.coachmark

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CoachMarkTooltip(
    target: CoachMarkTarget,
    isLastStep: Boolean,
    onNext: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (title, description) = target.texts

    Card(
        modifier = modifier
            .padding(16.dp)
            .widthIn(max = 280.dp), // 너무 넓어지지 않게 제한
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 버튼 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("건너뛰기", color = Color.Gray)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNext) {
                    Text(if (isLastStep) "완료" else "다음")
                }
            }
        }
    }
}