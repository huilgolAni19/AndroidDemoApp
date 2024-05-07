package com.example.androiddemo.fragments.taskone.fragmentnews

import android.net.http.HttpException
import androidx.lifecycle.ViewModel
import com.example.androiddemo.models.posts.Post
import com.example.androiddemo.repository.PostsAndCommentsRepository
import com.example.androiddemo.utils.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject

class NewsViewModel(private val repository: PostsAndCommentsRepository) : ViewModel() {

    val newsResponse: PublishSubject<Post> = PublishSubject.create()

    fun getNews(id: String) {
        repository.disposable += repository.postRestService.posts(id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({ post ->
                newsResponse.onNext(post)
            }, { error ->
                newsResponse.onError(error)
            })
    }
    override fun onCleared() {
        super.onCleared()
        repository.disposable.clear()
    }
}