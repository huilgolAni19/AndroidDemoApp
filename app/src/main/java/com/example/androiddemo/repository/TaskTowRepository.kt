package com.example.androiddemo.repository

import com.example.androiddemo.data.offline.daos.LocationDAO
import io.reactivex.rxjava3.disposables.CompositeDisposable

data class TaskTowRepository(
    val locationDAO: LocationDAO,
    val disposable: CompositeDisposable
)
