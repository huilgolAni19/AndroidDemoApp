package com.example.androiddemo.fragments.taskone.fragmentcomments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.adapters.CommentsAdapter
import com.example.androiddemo.databinding.FragmentCommentsBinding
import com.example.androiddemo.di.components.DaggerTaskOneComponent
import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskOneModule
import com.example.androiddemo.models.comments.CommentsItem
import com.example.androiddemo.repository.PostsAndCommentsRepository
import com.example.androiddemo.utils.isInternetAvailable
import com.example.androiddemo.utils.log
import com.example.androiddemo.utils.plusAssign
import com.example.androiddemo.viewmodelfactories.ViewModelFactoryWithPostAndCommentRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

class CommentsFragment : Fragment() {


    private lateinit var viewModel: CommentsViewModel
    private lateinit var binding: FragmentCommentsBinding
    @Inject
    lateinit var repository: PostsAndCommentsRepository

    private lateinit var adapter: CommentsAdapter
    private var comments: ArrayList<CommentsItem> = ArrayList()

    private lateinit var factory: ViewModelFactoryWithPostAndCommentRepository

    private val postIdReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "PID") {
                intent?.let { ii ->
                    ii.extras?.let { bundle ->
                        val pid: String = bundle.getString("PostId", "")
                        log("From Comments: $pid")
                        if(isInternetAvailable(requireContext())) {
                            viewModel.fetchCommentById(pid)
                        } else {
                            log("Not Internet")
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("InComments Fragments")
        inject()
        factory = ViewModelFactoryWithPostAndCommentRepository(repository)
        viewModel = ViewModelProvider(this, factory)[CommentsViewModel::class.java]
        binding.recycleViewComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewComments.itemAnimator = DefaultItemAnimator()
        adapter = CommentsAdapter(comments)
        binding.recycleViewComments.adapter = adapter

        repository.disposable += viewModel.commentsResponse
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe({
                if (it.size > 0) {
                    comments.clear()
                    comments.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }, {
                log("${it.message}")
            })

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(postIdReceiver, IntentFilter("PID"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(postIdReceiver)
    }

    private fun inject() = DaggerTaskOneComponent.builder().apply {
        commonModule(CommonModule(requireContext()))
        taskOneModule(TaskOneModule())
    }.build().inject(this)
}