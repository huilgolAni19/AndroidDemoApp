package com.example.androiddemo.repository

import com.example.androiddemo.data.online.CommentsRestService
import com.example.androiddemo.data.online.PostRestService
import io.reactivex.rxjava3.disposables.CompositeDisposable

data class PostsAndCommentsRepository(
    var disposable: CompositeDisposable,
    var postRestService: PostRestService,
    var commentsRestService: CommentsRestService
)
