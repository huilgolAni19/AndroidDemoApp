package com.example.androiddemo.fragments.taskfour

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import com.example.androiddemo.databinding.FragmentTaskFourBinding
import com.example.androiddemo.utils.plusAssign
import com.jakewharton.rxbinding4.view.clicks
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class TaskFourFragment : Fragment() {


    private lateinit var binding: FragmentTaskFourBinding
    private lateinit var disposable: CompositeDisposable


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskFourBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposable = CompositeDisposable()

        disposable +=  binding.buttonMessage
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                SimpleTooltip.Builder(requireContext())
                    .anchorView(binding.buttonMessage)
                    .text("You can send message")
                    .gravity(Gravity.END)
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }

        disposable +=  binding.buttonUpload
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                SimpleTooltip.Builder(requireContext())
                    .anchorView(binding.buttonUpload)
                    .text("You can upload pic")
                    .gravity(Gravity.START)
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }

        disposable +=  binding.buttonPost
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                SimpleTooltip.Builder(requireContext())
                    .anchorView(binding.buttonPost)
                    .text("You can send Post")
                    .gravity(Gravity.TOP)
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }

        disposable +=  binding.buttonProfile
            .clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe {
                SimpleTooltip.Builder(requireContext())
                    .anchorView(binding.buttonProfile)
                    .text("You can edit Profile")
                    .gravity(Gravity.BOTTOM)
                    .animated(true)
                    .transparentOverlay(false)
                    .build()
                    .show()
            }
    }
}