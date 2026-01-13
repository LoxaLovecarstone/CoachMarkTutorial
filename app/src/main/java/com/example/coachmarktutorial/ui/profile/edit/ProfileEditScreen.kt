package com.example.coachmarktutorial.ui.profile.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.coachmarktutorial.ui.theme.Dimens

@Composable
fun ProfileEditScreen(
    onSaveSuccess: () -> Unit, // 저장 완료 시 뒤로가기
    viewModel: ProfileEditViewModel = hiltViewModel()
) {
    // 초기값은 뷰모델에서 받아옴
    var name by remember { mutableStateOf(viewModel.currentProfile.name) }
    var statusMessage by remember { mutableStateOf(viewModel.currentProfile.statusMessage) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingLarge)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("이름") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        OutlinedTextField(
            value = statusMessage,
            onValueChange = { statusMessage = it },
            label = { Text("상태 메시지") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Button(
            onClick = {
                viewModel.updateProfile(name, statusMessage) {  // 후행 람다 문법
                    onSaveSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank()
        ) {
            Text("저장 완료")
        }
    }
}