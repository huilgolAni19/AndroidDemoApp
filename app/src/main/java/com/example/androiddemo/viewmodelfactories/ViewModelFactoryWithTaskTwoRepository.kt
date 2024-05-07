package com.example.androiddemo.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.fragments.tasktwo.TaskTwoFragmentViewModel
import com.example.androiddemo.repository.TaskTowRepository
import com.example.androiddemo.services.LocationViewModel

class ViewModelFactoryWithTaskTwoRepository(private val repository: TaskTowRepository):
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskTwoFragmentViewModel::class.java)) {
            return TaskTwoFragmentViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return LocationViewModel(repository) as T
        }
        return super.create(modelClass)
    }
}