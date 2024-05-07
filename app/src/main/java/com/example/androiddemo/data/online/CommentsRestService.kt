package com.example.androiddemo.data.online

import com.example.androiddemo.models.comments.Comments
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentsRestService {

    @GET("posts/{id}/comments")
    fun getComments(@Path("id") id: String): Flowable<Comments>
}