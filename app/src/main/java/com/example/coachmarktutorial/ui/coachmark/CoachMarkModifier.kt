package com.example.coachmarktutorial.ui.coachmark

import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.onGloballyPositioned


fun Modifier.coachMarkTarget(
    target: CoachMarkTarget
): Modifier = composed { // 이 블록 내부는 @Composable과 같은 권한을 지님
    // 따라서 Composable 함수에 접근이 가능함
    // @Composable의 런타임 기능으로는 state, remember 등이 있음

    // 즉 이 확장함수는 state에 접근이 가능해짐
    val state = LocalCoachMarkState.current

    // this.onGloballyPositioned 는 화면에 UI가 그려질 때마다 실행됨.
    // 그려진 좌표를 알려줌
    this.onGloballyPositioned { coordinates ->
        state.onTargetLayout(target, coordinates)
    }
}