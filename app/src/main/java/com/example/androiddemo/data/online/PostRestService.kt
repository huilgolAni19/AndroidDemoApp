package com.example.androiddemo.data.online

import com.example.androiddemo.models.posts.Post
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import retrofit2.http.Path

interface PostRestService {

    @GET("posts/{id}")
    fun posts(@Path("id") id: String): Flowable<Post>
}