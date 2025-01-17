package com.example.androiddemo.models.posts


import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("body")
    var body: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("userId")
    var userId: Int
)