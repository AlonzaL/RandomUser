package com.example.randomuser.data.db

import androidx.room.TypeConverter
import com.example.randomuser.data.model.*
import com.google.gson.Gson

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromLogin(login: Login?): String? {
        return gson.toJson(login)
    }

    @TypeConverter
    fun toLogin(json: String?): Login? {
        return gson.fromJson(json, Login::class.java)
    }

    @TypeConverter
    fun fromName(name: Name?): String? {
        return gson.toJson(name)
    }

    @TypeConverter
    fun toName(json: String?): Name? {
        return gson.fromJson(json, Name::class.java)
    }

    @TypeConverter
    fun fromLocation(location: Location?): String? = gson.toJson(location)

    @TypeConverter
    fun toLocation(json: String?): Location? = gson.fromJson(json, Location::class.java)

    @TypeConverter
    fun fromPicture(picture: Picture?): String? = gson.toJson(picture)

    @TypeConverter
    fun toPicture(json: String?): Picture? = gson.fromJson(json, Picture::class.java)

    @TypeConverter
    fun fromDob(dob: Dob?): String? = gson.toJson(dob)

    @TypeConverter
    fun toDob(json: String?): Dob? = gson.fromJson(json, Dob::class.java)
}