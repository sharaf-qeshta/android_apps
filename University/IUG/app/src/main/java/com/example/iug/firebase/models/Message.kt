package com.example.iug.firebase.models

data class Message
(
    val id: String = "",
    val content: String = "",
    val date: Long = 0, // in milliseconds
    val senderID: String = ""
)