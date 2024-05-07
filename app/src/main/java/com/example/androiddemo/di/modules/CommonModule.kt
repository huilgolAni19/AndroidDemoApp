package com.example.androiddemo.di.modules

import android.content.Context
import com.example.androiddemo.data.offline.db.DemoAppDB
import com.example.androiddemo.data.online.RetrofitController
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class CommonModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return RetrofitController.getInstance().retrofit
    }

    @Singleton
    @Provides
    fun provideDB(): DemoAppDB {
        return DemoAppDB.instance(context)
    }

    @Singleton
    @Provides
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}