package com.example.androiddemo.models.comments


import com.google.gson.annotations.SerializedName

data class CommentsItem(
    @SerializedName("body")
    var body: String,
    @SerializedName("email")
    var email: String,
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("postId")
    var postId: Int
)