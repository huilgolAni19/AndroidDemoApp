package com.example.androiddemo.di.components

import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskOneModule
import com.example.androiddemo.fragments.taskone.fragmentcomments.CommentsFragment
import com.example.androiddemo.fragments.taskone.fragmentnews.NewsFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        CommonModule::class,
        TaskOneModule::class
    ]
)
interface TaskOneComponent {

    fun inject(fragment: NewsFragment)

    fun inject(fragment: CommentsFragment)
}