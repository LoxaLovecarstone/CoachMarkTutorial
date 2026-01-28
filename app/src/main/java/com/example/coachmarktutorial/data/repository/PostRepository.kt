package com.example.coachmarktutorial.data.repository

import com.example.coachmarktutorial.data.model.post.Category
import com.example.coachmarktutorial.data.model.post.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor() {
    private val _posts = MutableStateFlow<List<Post>>(
        listOf(
            Post(1, "Hilt가 뭔가요?", "의존성 주입이 어렵네요.", Category.QUESTION, "Newbie"),
            Post(2, "오늘 점심 메뉴 추천", "돈까스 vs 제육", Category.DAILY, "Hungry"),
            Post(3, "컴포즈 꿀팁 공유", "Scaffold는 하나만 쓰세요.", Category.INFO, "Expert"),
            Post(4, "코치마크 구현 중", "Canvas 어렵다...", Category.DAILY, "Dev"),
            Post(5, "안드로이드 개발자 구인", "연봉 1억", Category.INFO, "HR")
        )
    )
    val posts: StateFlow<List<Post>> = _posts.asStateFlow()

    fun addPost(title: String, category: Category, content: String, authorName: String) {
        val newPost = Post(
            id = System.currentTimeMillis(),
            title = title,
            category = category,
            content = content,
            authorName = authorName
        )
        // 현재 리스트에 새 글을 합쳐서 갱신
        _posts.update { currentList ->
            listOf(newPost) + currentList
        }
    }

    fun deletePost(postId: Long) {
        _posts.update { currentList ->
            currentList.filter { it.id != postId }
        }
    }

    fun getPost(postId: Long): Post? {
        return _posts.value.find { it.id == postId }
    }

    fun updatePost(id: Long, title: String, category: Category, content: String) {
        _posts.update { currentList ->
            currentList.map { post ->
                if (post.id == id) {
                    // copy를 사용해 기존 ID와 작성자 등은 유지하고 내용만 바꿈
                    post.copy(
                        title = title,
                        category = category,
                        content = content
                    )
                } else {
                    post
                }
            }
        }
    }
}