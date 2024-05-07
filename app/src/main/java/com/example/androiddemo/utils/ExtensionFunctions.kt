package com.example.androiddemo.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androiddemo.activities.MainActivity
import com.example.androiddemo.app.AndroidDemo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    this.add(disposable)
}

fun log(message: String) = Log.e("TAG", message)

fun showAlert(context: Activity, title: String, message: String) = AlertDialog.Builder(context)
    .setTitle(title)
    .setMessage(message)
    .setCancelable(false)
    .setPositiveButton("OK") { d, _ ->
        d.dismiss()
    }
    .create().show()

fun showToast(context: Context, message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
    for (service in manager?.getRunningServices(Int.MAX_VALUE) ?: emptyList()) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun Fragment.senBroadCast(pid: Int) {
    val broadCast = Intent("PID")
    val bundle = Bundle()
    bundle.putString("PostId", pid.toString())
    broadCast.putExtras(bundle)
    LocalBroadcastManager
        .getInstance(requireContext())
        .sendBroadcast(broadCast)
}

fun getNotification(
    context: Context,
    title: String,
    content: String,
    icon: Int,
    requestCode: Int,
    flag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE
    else PendingIntent.FLAG_ONE_SHOT
): Notification {

    val notificationIntent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent =
        PendingIntent.getActivity(context,
            requestCode,
            notificationIntent,
            flag)

    return NotificationCompat.Builder(context, AndroidDemo.CHANNEL_ID)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentIntent(pendingIntent)
        .setContentText(content).build()
}