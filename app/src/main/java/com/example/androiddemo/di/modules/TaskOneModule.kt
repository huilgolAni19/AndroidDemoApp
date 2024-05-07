package com.example.androiddemo.di.modules

import com.example.androiddemo.data.online.CommentsRestService
import com.example.androiddemo.data.online.PostRestService
import com.example.androiddemo.repository.PostsAndCommentsRepository
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class TaskOneModule {

    @Singleton
    @Provides
    fun providePostRestService(retrofit: Retrofit): PostRestService {
        return retrofit.create(PostRestService::class.java)
    }

    @Singleton
    @Provides
    fun providesCommentsRestService(retrofit: Retrofit): CommentsRestService {
        return retrofit.create(CommentsRestService::class.java)
    }

    @Singleton
    @Provides
    fun providePostAndCommentRestService(
        postRestService: PostRestService,
        commentsRestService: CommentsRestService,
        disposable: CompositeDisposable
    ): PostsAndCommentsRepository {
        return PostsAndCommentsRepository(
            disposable, postRestService, commentsRestService
        )
    }
}