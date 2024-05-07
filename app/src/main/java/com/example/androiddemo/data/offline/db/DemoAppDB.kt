package com.example.androiddemo.data.offline.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androiddemo.data.offline.daos.LocationDAO
import com.example.androiddemo.data.offline.entities.LocationEntity

@Database(
    entities = [
        LocationEntity::class
    ],
    version = 1
)
abstract class DemoAppDB: RoomDatabase() {

    abstract fun locationDao(): LocationDAO

    companion object {
        private var INSTANCE: DemoAppDB? = null

        fun instance(context: Context): DemoAppDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DemoAppDB::class.java,
                "DemoAppDB").build()
            }
            return INSTANCE!!
        }
    }
}