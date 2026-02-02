package com.example.coachmarktutorial.ui.slider

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun SliderScreen() {
    val context = LocalContext.current

    // 화면 회전 로직
    DisposableEffect(Unit) {
        val activity = context.findActivity()

        // Activity를 찾지 못했다면 아무것도 하지 않고 빈 onDispose 반환
        if (activity == null) {
            return@DisposableEffect onDispose { }
        }

        val originalOrientation = activity.requestedOrientation

        // 가로 모드로 고정
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            // 화면을 떠날 때 원래대로 복구
            activity.requestedOrientation = originalOrientation
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "가로 모드 고정 화면",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

// [유틸리티 함수] Context에서 Activity를 찾아내는 함수
private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}