package com.example.randomuser.data

import kotlinx.serialization.Serializable

data class ApiResponse(
    val results: List<User>?
)

@Serializable
data class User(
    val login: Login?,
    val gender: String?,
    val name: Name?,
    val location: Location?,
    val email: String?,
    val phone: String?,
    val picture: Picture?,
    val nat: String?
)

@Serializable
data class Login(
    val uuid: String?
)

@Serializable
data class Name(
    val first: String?,
    val last: String?
)

@Serializable
data class Location(
    val street: Street?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postcode: String?,
    val coordinates: Coordinates?,
    val timezone: Timezone?
)

@Serializable
data class Street(
    val number: Int?,
    val name: String?
)

@Serializable
data class Coordinates(
    val latitude: String?,
    val longitude: String?
)

@Serializable
data class Timezone(
    val offset: String?,
    val description: String?
)

@Serializable
data class Picture(
    val large: String?,
    val thumbnail: String?
)