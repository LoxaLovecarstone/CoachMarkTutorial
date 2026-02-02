package com.example.coachmarktutorial.ui.slider

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import kotlin.math.roundToInt

@Composable
fun SliderScreen(
    viewModel: SliderViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val sliderValue by viewModel.sliderValue.collectAsState()

    // [화면 회전 로직]
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        if (activity == null) return@DisposableEffect onDispose { }

        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity.requestedOrientation = originalOrientation
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 중앙 숫자
        Text(
            text = sliderValue.roundToInt().toString(),
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 100.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 컨트롤 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // [왼쪽] 감소 버튼 그룹 (-10, -5, -1)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CircleTextButton(text = "-10", onClick = { viewModel.adjustValue(-10) })
                CircleTextButton(text = "-5", onClick = { viewModel.adjustValue(-5) })
                CircleTextButton(text = "-1", onClick = { viewModel.adjustValue(-1) })
            }

            // [중앙] 슬라이더 (0 ~ 200)
            Slider(
                value = sliderValue,
                onValueChange = { viewModel.updateValue(it) },
                valueRange = 0f..200f,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )

            // [오른쪽] 증가 버튼 그룹 (+1, +5, +10)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CircleTextButton(text = "+1", onClick = { viewModel.adjustValue(1) })
                CircleTextButton(text = "+5", onClick = { viewModel.adjustValue(5) })
                CircleTextButton(text = "+10", onClick = { viewModel.adjustValue(10) })
            }
        }
    }
}

// 텍스트가 들어가는 원형 버튼
@Composable
private fun CircleTextButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp), // 패딩 제거해서 글자 중앙 정렬
        modifier = Modifier.size(48.dp),      // 적당한 크기
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

// 유틸리티 함수
private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}