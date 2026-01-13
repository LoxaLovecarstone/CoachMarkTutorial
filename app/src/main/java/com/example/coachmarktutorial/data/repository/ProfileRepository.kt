package com.example.coachmarktutorial.data.repository

import com.example.coachmarktutorial.data.model.profile.Profile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor() {
    private val _profile = MutableStateFlow(
        Profile("Team Leader Kim", "Android Developer & Coach Mark Master")
    )
    val profile: StateFlow<Profile> = _profile.asStateFlow()

    fun updateProfile(name: String, statusMessage: String) {
        _profile.value = Profile(name, statusMessage)
    }
}