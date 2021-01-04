package com.example.udacity_project_two.overview

import com.squareup.moshi.Json

data class PictureOfTheDay(
    //Set @field to verify this is an image file
    @field:Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
    )