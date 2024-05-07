package com.example.androiddemo.settingsapi

import android.app.Activity
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes

class StartLocationAlert(context: Activity): GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var context: Activity = context
    private var googleApiClient: GoogleApiClient

    private var listener: OnLocationStatusChangeListener? = null


    private companion object {
        private const val REQUEST_CHECK_SETTINGS = 100//0x3
        const val TAG = "StartLocationAlert"
    }

    private val instance: GoogleApiClient
        get() {
            return GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        }

    init {
        googleApiClient = instance
        if (googleApiClient != null) {
            googleApiClient.connect()
            if (googleApiClient.isConnected) {
                if (googleApiClient.hasConnectedApi(LocationServices.API)) {
                    Toast.makeText(context, "hasConnectedApi True", Toast.LENGTH_SHORT).show()

                }
                else {
                    Toast.makeText(context, "googleApiClient.hasConnectedApi(LocationServices.API) => False", Toast.LENGTH_SHORT).show()
                }
            }
            else if (googleApiClient.isConnecting) {
                Toast.makeText(context, "Please wait", Toast.LENGTH_SHORT).show()
            }
//            settingsRequest()
        }
    }

    fun addListener(listener: OnLocationStatusChangeListener) {
        this.listener = listener
    }

    private fun settingsRequest() {

        Log.e("settingsrequest", "Comes")
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        //locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        locationRequest.interval = 30 * 1000
        locationRequest.fastestInterval = 5 * 1000

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true) //this is the key ingredient
        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback { result ->
            val status = result.status
            val state = result.locationSettingsStates
            when (status.statusCode) {

                LocationSettingsStatusCodes.SUCCESS -> {
                    this.listener!!.onAccepted()
                }

                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    // Log.e("Application","Button Clicked1");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(context, REQUEST_CHECK_SETTINGS)
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                        Log.e("Applicationsett", e.toString())
                    }
                }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                    //Log.e("Application","Button Clicked2");
                    Toast.makeText(context, "Location is Enabled", Toast.LENGTH_SHORT).show()
                }

                LocationSettingsStatusCodes.CANCELED -> {
                    this.listener!!.onDenied()
                }
            }
        }
    }

    override fun onConnected(bundle: Bundle?) {
        //Toast.makeText(context, "Client Connected", Toast.LENGTH_SHORT).show()
        settingsRequest()
    }

    override fun onConnectionSuspended(i:Int) {

    }


    override fun onConnectionFailed(@NonNull connectionResult: ConnectionResult) {
        Toast.makeText(context, "Connection Failed", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "Error Message ${connectionResult.hashCode()}" )
    }

    interface OnLocationStatusChangeListener {
        fun onAccepted()
        fun onDenied()
    }
}