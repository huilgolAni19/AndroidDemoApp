package com.example.androiddemo.data.offline.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androiddemo.data.offline.entities.LocationEntity
import io.reactivex.rxjava3.core.Completable

import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable


@Dao
interface LocationDAO {

    @Insert
    fun insertLocation(location: LocationEntity): Completable

    @Query("SELECT COUNT(*) FROM TBL_Location")
    fun getCount(): Observable<Int>

    @Query("SELECT * FROM TBL_Location ORDER BY id DESC LIMIT 5")
    fun getLastFiveLocations(): Flowable<List<LocationEntity>>
}