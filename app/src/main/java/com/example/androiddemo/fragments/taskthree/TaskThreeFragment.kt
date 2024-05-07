package com.example.androiddemo.fragments.taskthree

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import com.example.androiddemo.R
import com.example.androiddemo.app.AndroidDemo
import com.example.androiddemo.databinding.FragmentTaskThreeBinding
import com.example.androiddemo.utils.plusAssign
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class TaskThreeFragment : Fragment() {

    private lateinit var binding: FragmentTaskThreeBinding
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val notificationId = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskThreeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable += binding.buttonShowNotification
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .observeOn(mainThread())
            .subscribeOn(mainThread())
            .subscribe {
                showNotification()
            }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.taskThreeFragment)
            .createPendingIntent()

        val soundUri = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${requireActivity().packageName}/raw/notification_tone"
        val uri: Uri = Uri.parse(soundUri)

        val notification = NotificationCompat.Builder(requireContext(), AndroidDemo.CHANNEL_ID)
            .setContentTitle("Simple Notification Title")
            .setContentText("Simple Notification Body")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSound(uri)
            .build()

        NotificationManagerCompat.from(requireActivity()).notify(notificationId, notification)
    }
}