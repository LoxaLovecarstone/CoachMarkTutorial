package com.example.coachmarktutorial.ui.home

import androidx.lifecycle.ViewModel
import com.example.coachmarktutorial.data.model.post.Post
import com.example.coachmarktutorial.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    val posts: StateFlow<List<Post>> = postRepository.posts
}