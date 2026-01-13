package com.example.coachmarktutorial.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.coachmarktutorial.data.model.post.Category
import com.example.coachmarktutorial.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: PostViewModel = hiltViewModel()
) {
    // 입력 상태 관리
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    // 카테고리 선택 상태 관리
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(Category.DAILY) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(Dimens.PaddingLarge)
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCategory.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("카테고리") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                Category.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.label) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("제목") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("내용을 입력하세요") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

        Button(
            onClick = {
                if (title.isNotBlank() && content.isNotBlank()) {
                    viewModel.savePost(title, selectedCategory, content) {
                        onSaveClick()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && content.isNotBlank()
        ) {
            Text("등록하기")
        }
    }
}