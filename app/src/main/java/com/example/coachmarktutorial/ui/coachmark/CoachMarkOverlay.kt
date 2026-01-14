package com.example.coachmarktutorial.ui.coachmark

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity // import 추가
import androidx.compose.ui.unit.IntOffset

@Composable
fun CoachMarkOverlay(
    modifier: Modifier = Modifier,
    overlayColor: Color = Color.Black.copy(alpha = 0.7f)
) {
    val state = LocalCoachMarkState.current
    val currentTarget = state.currentTarget
    val density = LocalDensity.current

    if (currentTarget == null) return

    val targetRect = state.targetPositions[currentTarget]

    val alpha by animateFloatAsState(
        targetValue = if (targetRect != null) 1f else 0f,
        label = "AlphaAnimation"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer(alpha = 0.99f)
    ) {
        val screenHeight = constraints.maxHeight

        // 배경 및 구멍 그리기
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(color = overlayColor, alpha = alpha)

            if (targetRect != null) {
                val holeTopLeft = Offset(targetRect.left, targetRect.top)
                val holeSize = Size(targetRect.width, targetRect.height)

                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = holeTopLeft,
                    size = holeSize,
                    cornerRadius = CornerRadius(16f, 16f),
                    blendMode = BlendMode.Clear
                )
            }
        }

        // 툴팁 배치
        if (targetRect != null) {
            val targetCenterY = targetRect.top + (targetRect.height / 2)
            val isBottomTarget = targetCenterY > (screenHeight / 2)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { this.alpha = alpha }
            ) {
                CoachMarkTooltip(
                    target = currentTarget,
                    isLastStep = CoachMarkTarget.entries.last() == currentTarget,
                    onNext = { state.moveNext() },
                    onDismiss = { state.setCurrentStep(null) },
                    modifier = Modifier
                        .align(Alignment.TopCenter) // 기본적으로 상단 중앙 정렬 기준
                        .offset {
                            //
                            val yPosition = if (isBottomTarget) {
                                // 타겟 위에 배치: (타겟 Top - 200dp 정도 위)
                                (targetRect.top - 200 * density.density).toInt().coerceAtLeast(0)
                            } else {
                                // 타겟 아래에 배치: (타겟 Bottom + 10dp 여백)
                                (targetRect.bottom + 10 * density.density).toInt()
                            }

                            IntOffset(x = 0, y = yPosition)
                        }
                )
            }
        }
    }
}