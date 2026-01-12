package com.example.coachmarktutorial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.coachmarktutorial.ui.main.MainScreen // 방금 만든 거 임포트
import com.example.coachmarktutorial.ui.theme.CoachMarkTutorialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoachMarkTutorialTheme {
                // 이제 여기서 바로 MainScreen을 호출합니다.
                MainScreen()
            }
        }
    }
}