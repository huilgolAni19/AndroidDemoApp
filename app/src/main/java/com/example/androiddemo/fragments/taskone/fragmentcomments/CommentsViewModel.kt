package com.example.androiddemo.fragments.taskone.fragmentcomments

import androidx.lifecycle.ViewModel
import com.example.androiddemo.models.comments.Comments
import com.example.androiddemo.repository.PostsAndCommentsRepository
import com.example.androiddemo.utils.plusAssign
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.schedulers.Schedulers.io
import io.reactivex.rxjava3.subjects.PublishSubject

class CommentsViewModel(private val repository: PostsAndCommentsRepository) : ViewModel() {

    val commentsResponse: PublishSubject<Comments> = PublishSubject.create()

    fun fetchCommentById(id: String) {
        repository.disposable += repository.commentsRestService.getComments(id)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(
                { comments ->
                    commentsResponse.onNext(comments)
                }, { error ->
                    commentsResponse.onError(error)
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        repository.disposable.clear()
    }
}