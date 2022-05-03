package com.example.metacourse.network

import java.util.*

data class UserModel(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val dateOfBirth: Date,
//    val roles: Collection<RoleModel>,
//    val coursesTaken: Collection<CourseModel>,
//    val coursesCreated: Collection<Course>
)