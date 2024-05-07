package com.example.androiddemo.fragments.tasktwo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.adapters.LocationAdapter
import com.example.androiddemo.data.offline.entities.LocationEntity
import com.example.androiddemo.databinding.FragmentTaskTwoBinding
import com.example.androiddemo.di.components.DaggerTaskTwoComponent
import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskTwoModule
import com.example.androiddemo.repository.TaskTowRepository
import com.example.androiddemo.services.LocationClientServices
import com.example.androiddemo.settingsapi.StartLocationAlert
import com.example.androiddemo.utils.isServiceRunning
import com.example.androiddemo.utils.log
import com.example.androiddemo.utils.plusAssign
import com.example.androiddemo.utils.showToast
import com.example.androiddemo.viewmodelfactories.ViewModelFactoryWithTaskTwoRepository
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.visibility
import com.permissionx.guolindev.PermissionX
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class TaskTwoFragment : Fragment() {

    private var _binding: FragmentTaskTwoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: TaskTwoFragmentViewModel

    @Inject
    lateinit var repository: TaskTowRepository
    private lateinit var factory: ViewModelFactoryWithTaskTwoRepository
    private lateinit var locationManager: LocationManager
    private var locationsList: ArrayList<LocationEntity> = ArrayList()

    private lateinit var adapter: LocationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTaskTwoBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        inject()

        factory = ViewModelFactoryWithTaskTwoRepository(repository)
        viewModel = ViewModelProvider(this, factory)[TaskTwoFragmentViewModel::class.java]

        checkAndGrantPermission()


        adapter = LocationAdapter(locationsList)
        binding.recycleViewLocation.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewLocation.itemAnimator = DefaultItemAnimator()
        binding.recycleViewLocation.adapter = adapter

        repository.disposable += binding.buttonStartLocation
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                log("Start Location Clicked")
                binding.buttonStartLocation.visibility = View.GONE
                binding.buttonStopService.visibility = View.VISIBLE
                val location = Intent(requireContext(), LocationClientServices::class.java)
                ContextCompat.startForegroundService(requireContext(), location)
            }

        repository.disposable += binding.buttonStopService
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                log("Stop Location Clicked")
                binding.buttonStartLocation.visibility = View.VISIBLE
                binding.buttonStopService.visibility = View.GONE
                val location = Intent(requireContext(), LocationClientServices::class.java)
                requireContext().stopService(location)
            }

       repository.disposable += binding.buttonShowLocations.clicks()
           .throttleFirst(100, TimeUnit.MILLISECONDS)
           .subscribeOn(mainThread())
           .observeOn(mainThread())
           .subscribe {

               repository.disposable += viewModel.getLastFewLocations().subscribe {
                    locationsList.clear()
                    locationsList.addAll(it)
                   adapter.notifyDataSetChanged()
               }

               repository.disposable += viewModel.getCount().subscribe {
                    binding.textViewCount.text = "Total Row $it"
               }
           }

        if (isServiceRunning(requireContext(), LocationClientServices::class.java)) {
            binding.buttonStartLocation.visibility = View.GONE
            binding.buttonStopService.visibility = View.VISIBLE
            log("Service Is Running")
        } else {
            binding.buttonStartLocation.visibility = View.VISIBLE
            binding.buttonStopService.visibility = View.GONE
        }

    }

    private fun init() {
        locationManager = requireContext()
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun inject() = DaggerTaskTwoComponent.builder().apply {
        commonModule(CommonModule(requireContext()))
        taskTwoModule(TaskTwoModule())
    }.build().inject(this)


    override fun onResume() {
        super.onResume()
        checkAndTurnOnLocation()
    }

    private fun checkAndGrantPermission() {
        PermissionX.init(requireActivity())
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
            .explainReasonBeforeRequest()
            .request { allGranted, _, _ ->
                if(allGranted) {

                } else {
                    showToast(requireContext(), "Few permission are not granted.")
                }
            }
    }

    private fun checkAndTurnOnLocation() {
        log("Location Enabled: ${locationManager.isLocationEnabled}")
        if(!locationManager.isLocationEnabled) {
            log("Location Is Not Enabled Enabling GPS Location Via SettingsApi")
            val startLocationAlert = StartLocationAlert(requireActivity())
            startLocationAlert.addListener(object: StartLocationAlert.OnLocationStatusChangeListener{
                override fun onAccepted() {
                    showToast(requireContext(), "Turning on Location.")
                }

                override fun onDenied() {
                    showToast(requireContext(), "Denied..")
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}