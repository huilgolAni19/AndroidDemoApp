package com.example.androiddemo.di.modules

import com.example.androiddemo.data.offline.daos.LocationDAO
import com.example.androiddemo.data.offline.db.DemoAppDB
import com.example.androiddemo.repository.TaskTowRepository
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class TaskTwoModule {

    @Singleton
    @Provides
    fun provideLocationDAO(db: DemoAppDB): LocationDAO {
        return db.locationDao()
    }

    @Singleton
    @Provides
    fun provideTaskTwoRepo(disposable: CompositeDisposable,dao: LocationDAO): TaskTowRepository {
        return TaskTowRepository(dao, disposable)
    }
}