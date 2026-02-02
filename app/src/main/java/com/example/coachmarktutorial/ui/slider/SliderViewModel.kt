package com.example.coachmarktutorial.ui.slider

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SliderViewModel @Inject constructor() : ViewModel() {

    //기본값 100, 범위 0 ~ 200
    private val _sliderValue = MutableStateFlow(100f)
    val sliderValue: StateFlow<Float> = _sliderValue.asStateFlow()

    // 슬라이더 드래그 시 호출
    fun updateValue(newValue: Float) {
        _sliderValue.value = newValue
    }

    // [수정] 증감 로직 통합 (amount: +1, +5, -10 등)
    fun adjustValue(amount: Int) {
        val newValue = _sliderValue.value + amount
        // [수정] 범위 0f ~ 200f로 제한
        _sliderValue.value = newValue.coerceIn(0f, 200f)
    }
}