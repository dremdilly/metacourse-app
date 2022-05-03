package com.example.metacourse.network

import java.util.*

data class PersonModel(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth: String,
//    val password: String,
//    val roles: List<RoleModel>,
//    val coursesTaken: List<CourseModel>,
//    val coursesCreated: List<CourseModel>
)
