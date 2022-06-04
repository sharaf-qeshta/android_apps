package com.example.iug.firebase.models

/**
 * add image field
 * */
data class Student
(
    val id: String = "",
    val facultyID: String = "",
    val currentSemesterID: String = "",
    val balance: Double = 0.0,
    val firstName: String = "",
    val lastName: String = "",
    val age: Int = 0,
    val gender: Int = -1, // not determined
    val email: String = "",
    val phone: String = "",
    val address: String = "",
    val image: String = ""
)