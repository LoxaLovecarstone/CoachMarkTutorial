package com.example.coachmarktutorial.ui.coachmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot

class CoachMarkState {

    // 각 타겟의 위치를 저장함
    private val _targetPositions = mutableStateMapOf<CoachMarkTarget, Rect>()
    val targetPositions: Map<CoachMarkTarget, Rect> get() = _targetPositions

    // 현재 단계를 보여줌. null 이면 코치마크가 꺼짐
    var currentTarget by mutableStateOf<CoachMarkTarget?>(null)
        private set  // 세터는 이 클래스 내부에서만 작동 가능

    fun moveNext() {  // 코치마크 단계 넘기기
        val targets = CoachMarkTarget.entries
        val currentIndex = targets.indexOf(currentTarget)
        if (currentIndex != -1 && currentIndex < targets.lastIndex) {
            currentTarget = targets[currentIndex + 1]
        } else {
            currentTarget = null
        }
    }

    // 특정 단계를 지정함
    fun setCurrentStep(target: CoachMarkTarget?) {
        currentTarget = target
    }

    // LayoutCoordinates에는 위치 및 크기 정보가 담김
    fun onTargetLayout(target: CoachMarkTarget, coordinates: LayoutCoordinates) {
        if (coordinates.isAttached) {  // 화면에 정상적으로 붙어있다면
            val position = coordinates.positionInRoot()  // 화면 전체(Root) 기준으로 좌상단을 구함
            val size = coordinates.size  // 버튼 너비 높이
            _targetPositions[target] = Rect(  // 클릭될 곳을 직사각형 범위로 정함
                left = position.x,
                top = position.y,
                right = position.x + size.width,
                bottom = position.y + size.height
            )
        }
    }
}

// 컴포저블에서 이 클래스의 상태를 받기 위한 헬퍼 함수
@Composable
fun rememberCoachMarkState(): CoachMarkState {
    return remember { CoachMarkState() }
}

// 앱 어디서든 State에 접근할 수 있게 해주는 State
// 만약 Provider가 없으면 에러를 띄워서 개발자에게 알려줌
val LocalCoachMarkState = compositionLocalOf<CoachMarkState> {
    error("CoachMarkState not provided! MainScreen에서 Provider로 감싸야 합니다.")
}