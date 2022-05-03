package com.example.metacourse.network

data class LessonModel(
    val id: Long,
    val title: String,
    val description: String,
    val texts: Collection<TextModel>,
)
