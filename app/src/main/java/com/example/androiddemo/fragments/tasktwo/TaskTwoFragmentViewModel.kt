package com.example.androiddemo.fragments.tasktwo

import androidx.lifecycle.ViewModel
import com.example.androiddemo.data.offline.entities.LocationEntity
import com.example.androiddemo.repository.TaskTowRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers.io

class TaskTwoFragmentViewModel(private val repository: TaskTowRepository): ViewModel() {

    fun getLastFewLocations(): Flowable<List<LocationEntity>> {

        return repository.locationDAO
            .getLastFiveLocations()
            .observeOn(mainThread())
            .subscribeOn(io())
    }

    fun getCount(): Observable<Int> {
        return repository.locationDAO.getCount()
            .observeOn(mainThread())
            .subscribeOn(io())
    }
}