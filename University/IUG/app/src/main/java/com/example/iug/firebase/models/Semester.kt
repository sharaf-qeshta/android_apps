package com.example.iug.firebase.models

data class Semester
(
    val id: String = "",
    val order: Int = 0,
    val startDate: Long = 0, // in seconds
    val endDate: Long = 0,
)