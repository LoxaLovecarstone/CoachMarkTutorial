package com.example.coachmarktutorial.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coachmarktutorial.data.model.post.Post
import com.example.coachmarktutorial.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    // 검색어
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // 검색 결과 리스트
    // 검색어(_searchQuery)나 데이터(postRepository.posts) 중 하나라도 바뀌면
    // 자동으로 이 블록이 재실행되어 결과를 갱신합니다.
    private val _searchResults = MutableStateFlow<List<Post>>(emptyList())
    val searchResults: StateFlow<List<Post>> = combine(
        _searchQuery,
        postRepository.posts
    ) { query, posts ->
        if (query.isBlank()) {
            emptyList()
        } else {
            posts.filter { post ->
                post.title.contains(query, ignoreCase = true) ||
                        post.content.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // 검색어 입력 시 실행되는 함수
    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery

        if (newQuery.isBlank()) {
            _searchResults.value = emptyList() // 검색어 없으면 빈 화면
            return
        }

        // 전체 리스트(repository.posts.value)에서 필터링함
        // (실제 서버라면 여기서 repository.search(newQuery)를 호출했을 것임)
        val allPosts = postRepository.posts.value
        _searchResults.value = allPosts.filter { post ->
            post.title.contains(newQuery, ignoreCase = true) ||
                    post.content.contains(newQuery, ignoreCase = true)
        }
    }

    fun deletePost(post: Post) {
        postRepository.deletePost(post.id)

        // 현재 검색어로 다시 필터링
        onQueryChange(_searchQuery.value)
    }
}