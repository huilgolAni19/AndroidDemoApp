package com.example.androiddemo.services

import androidx.lifecycle.ViewModel
import com.example.androiddemo.data.offline.entities.LocationEntity
import com.example.androiddemo.repository.TaskTowRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationViewModel(private val repository: TaskTowRepository): ViewModel() {

    suspend fun insert(locationEntity: LocationEntity)  {
        withContext(Dispatchers.IO) {
            repository.locationDAO.insertLocation(locationEntity)
        }
    }
}