package com.example.androiddemo.di.components

import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskTwoModule
import com.example.androiddemo.fragments.tasktwo.TaskTwoFragment
import com.example.androiddemo.services.LocationClientServices
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CommonModule::class,
        TaskTwoModule::class
    ]
)
interface TaskTwoComponent {

    fun inject(fragment: TaskTwoFragment)

    fun inject(services: LocationClientServices)
}