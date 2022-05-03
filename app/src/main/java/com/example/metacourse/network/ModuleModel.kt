package com.example.metacourse.network

data class ModuleModel (
    val id: Long,
    val title: String,
    val description: String,
    val lessons: Collection<LessonModel>,
    val videos: Collection<VideoModel>,
    val images: Collection<ImageModel>
)