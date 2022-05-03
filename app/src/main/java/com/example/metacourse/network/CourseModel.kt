package com.example.metacourse.network

data class CourseModel(
    val id: Long,
    val title: String,
    val description: String,
    val duration: String,
    val categories: List<CategoryModel>,
    val rating: Double,
    val modules: List<ModuleModel>
)