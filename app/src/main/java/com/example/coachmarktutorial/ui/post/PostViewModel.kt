package com.example.coachmarktutorial.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coachmarktutorial.data.model.post.Category
import com.example.coachmarktutorial.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    fun savePost(
        title: String,
        category: Category,
        content: String,
        onSuccess: () -> Unit // 저장이 끝나면 화면을 닫기 위한 콜백
    ) {
        viewModelScope.launch {
            postRepository.addPost(
                title = title,
                category = category,
                content = content,
                authorName = "Me" // 내 이름 (나중엔 프로필에서 가져오도록 함)
            )
            onSuccess()
        }
    }
}