package com.example.coachmarktutorial.ui.main

import androidx.lifecycle.ViewModel
import com.example.coachmarktutorial.ui.coachmark.CoachMarkTarget
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    // 튜토리얼이 한 번이라도 시작되었는지 체크
    var isTutorialStarted: Boolean = false
}