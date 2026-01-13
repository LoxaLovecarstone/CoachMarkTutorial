package com.example.coachmarktutorial.data.model.post

import com.example.coachmarktutorial.data.model.post.Category
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Post(
    val id: Long,
    val title: String,
    val content: String,
    val category: Category,
    val authorName: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm")
        return createdAt.format(formatter)
    }
}