package com.example.coachmarktutorial.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coachmarktutorial.data.model.post.Category
import com.example.coachmarktutorial.data.model.post.Post
import com.example.coachmarktutorial.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val postRepository: PostRepository
): ViewModel() {

    fun savePost(
        id: Long, // [New] ID를 받아서 신규/수정 구분
        title: String,
        category: Category,
        content: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            if (id == -1L) {
                // 신규 작성
                postRepository.addPost(
                    title = title,
                    category = category,
                    content = content,
                    authorName = "Me"
                )
            } else {
                postRepository.updatePost(
                    id = id,
                    title = title,
                    category = category,
                    content = content
                )
            }
            onSuccess()
        }
    }

    fun getPost(postId: Long): Post? {
        return postRepository.getPost(postId)
    }
}