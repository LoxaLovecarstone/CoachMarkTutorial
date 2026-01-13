package com.example.coachmarktutorial.data.model.post

enum class Category(val label: String) {
    DAILY("일상"),
    QUESTION("질문"),
    INFO("정보");

    companion object {
        fun getAllLabels(): List<String> = entries.map { it.label }
        fun fromLabel(label: String): Category? = entries.find { it.label == label }
    }
}