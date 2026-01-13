package com.example.coachmarktutorial.ui.profile.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coachmarktutorial.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val currentProfile = profileRepository.profile.value

    fun updateProfile(name: String, message: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            profileRepository.updateProfile(name, message)
            onSuccess() // 저장 완료 후 콜백
        }
    }
}