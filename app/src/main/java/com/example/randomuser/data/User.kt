package com.example.randomuser.data

data class User(
    val id: String,
    val gender: String,
    val firstName: String,
    val lastName: String,
    val location: String,
    val email: String,
    val phone: String,
    val largePictureUrl: String,
    val thumbnailPictureUrl: String,
    val nat: String
)
