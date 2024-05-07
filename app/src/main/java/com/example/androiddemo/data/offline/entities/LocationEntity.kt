package com.example.androiddemo.data.offline.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "TBL_Location",
    indices = [
        Index(
            value = ["id"],
            unique = true
        )
    ]
)
data class LocationEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "Lat")
    @SerializedName("Lat")
    var lat: Double,

    @ColumnInfo(name = "Lon")
    @SerializedName("Lon")
    var lon: Double
)