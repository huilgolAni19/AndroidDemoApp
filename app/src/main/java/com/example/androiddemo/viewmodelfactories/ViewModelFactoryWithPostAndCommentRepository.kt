package com.example.androiddemo.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.fragments.taskone.fragmentcomments.CommentsViewModel
import com.example.androiddemo.fragments.taskone.fragmentnews.NewsViewModel
import com.example.androiddemo.repository.PostsAndCommentsRepository

class ViewModelFactoryWithPostAndCommentRepository
    (private val repository: PostsAndCommentsRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommentsViewModel::class.java)) {
            return CommentsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
            return NewsViewModel(repository) as T
        }
        return super.create(modelClass)
    }
}