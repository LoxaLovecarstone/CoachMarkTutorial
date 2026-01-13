package com.example.coachmarktutorial.ui.profile

import androidx.lifecycle.ViewModel
import com.example.coachmarktutorial.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    profileRepository: ProfileRepository
) : ViewModel() {
    val profile = profileRepository.profile
}