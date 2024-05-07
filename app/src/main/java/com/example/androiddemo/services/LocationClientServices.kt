package com.example.androiddemo.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.example.androiddemo.data.offline.entities.LocationEntity
import com.example.androiddemo.di.components.DaggerTaskTwoComponent
import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskTwoModule
import com.example.androiddemo.repository.TaskTowRepository
import com.example.androiddemo.utils.getNotification
import com.example.androiddemo.utils.log
import com.example.androiddemo.utils.plusAssign
import com.example.androiddemo.viewmodelfactories.ViewModelFactoryWithTaskTwoRepository
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import io.reactivex.rxjava3.schedulers.Schedulers.io
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationClientServices: Service(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest
    private lateinit var mLocationCallback: LocationCallback

    @Inject
    lateinit var repository: TaskTowRepository
    private lateinit var factory: ViewModelFactoryWithTaskTwoRepository
    private lateinit var viewModel: LocationViewModel

    companion object {
        private const val LOCATION_REQUEST_INTERVAL: Long = 10000
        private const val LOCATION_REQUEST_DISPLACEMENT: Float = 0.05f
        private const val NOTIFICATION_ID = 100
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        buildGoogleApiClient()
        log("Location Service Started")
        showNotificationAndStartForegroundService()
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location: Location = locationResult.locations[0]
                log("Lat: ${location.latitude} Lon: ${location.longitude}")
                val data = LocationEntity(
                    lat = location.latitude,
                    lon = location.longitude
                )
               repository.disposable += repository.locationDAO.insertLocation(data)
                    .subscribeOn(io())
                   .observeOn(io())
                   .subscribe({
                       log("Inserted")
                   } , {
                        log("${it.message}")
                   })
            }
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        return START_NOT_STICKY
    }


    suspend fun insertLocation(d: LocationEntity) {
        withContext(Dispatchers.IO) {
        }
    }
    override fun onConnected(bundle: Bundle?) {
        log("GoogleApi Client Connected....")
        createLocationRequest()
    }

    override fun onConnectionSuspended(i: Int) {
        log("GoogleApi Client Suspended.")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        log("GoogleApi Client Failed")
    }

    /**
     * Method used for building GoogleApiClient and add connection callback
     */
    @Synchronized
    private fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient.connect()
    }

    @SuppressLint("VisibleForTests")
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = LOCATION_REQUEST_INTERVAL
        mLocationRequest.fastestInterval = LOCATION_REQUEST_INTERVAL
        mLocationRequest.smallestDisplacement = LOCATION_REQUEST_DISPLACEMENT
        requestLocationUpdate()
    }

    private fun showNotificationAndStartForegroundService() {
        val notification = getNotification(this, "Location Service", "Location Service Started", com.example.androiddemo.R.mipmap.ic_launcher, NOTIFICATION_ID)
        startForeground(NOTIFICATION_ID, notification)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        mFusedLocationProviderClient
            .requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    override fun onCreate() {
        super.onCreate()
        inject()
        log("Location On Created")
//        factory = ViewModelFactoryWithTaskTwoRepository(repository)
    }

    private fun removeLocationUpdate() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
    }

    private fun inject() = DaggerTaskTwoComponent.builder().apply {
        commonModule(CommonModule(this@LocationClientServices))
        taskTwoModule(TaskTwoModule())
    }.build().inject(this)

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdate()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }
}