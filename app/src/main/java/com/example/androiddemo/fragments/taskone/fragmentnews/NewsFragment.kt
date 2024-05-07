package com.example.androiddemo.fragments.taskone.fragmentnews

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.androiddemo.R
import com.example.androiddemo.databinding.FragmentNewsBinding
import com.example.androiddemo.di.components.DaggerTaskOneComponent
import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskOneModule
import com.example.androiddemo.fragments.taskone.TaskOneViewModel
import com.example.androiddemo.repository.PostsAndCommentsRepository
import com.example.androiddemo.utils.*
import com.example.androiddemo.viewmodelfactories.ViewModelFactoryWithPostAndCommentRepository
import com.jakewharton.rxbinding4.view.clicks
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers.mainThread
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

class NewsFragment : Fragment() {


    private lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentNewsBinding
    @Inject
    lateinit var repository: PostsAndCommentsRepository
    private lateinit var factory: ViewModelFactoryWithPostAndCommentRepository
    private var id: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inject()
        factory = ViewModelFactoryWithPostAndCommentRepository(repository)
        viewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]

        repository.disposable += viewModel.newsResponse
            .subscribeOn(mainThread())
            .observeOn(mainThread())
            .subscribe (
                { post ->
                    binding.textViewPostId.text = "PostId: ${post.id}"
                    binding.textViewHeading.text = post.title
                    binding.textViewBody.text = post.body
                } , {
                    log("${it.message}")
                })

        id = Random.nextInt(1, 101)

        if (isInternetAvailable(requireContext())) {
            viewModel.getNews(id.toString())
        } else {
            showAlert(requireActivity(),"Warning", "No Internet Connection..")
        }

        repository.disposable += binding.buttonViewComments.clicks()
            .throttleFirst(100, TimeUnit.MILLISECONDS)
            .observeOn(mainThread())
            .subscribeOn(mainThread())
            .subscribe {
                senBroadCast(id)
            }
    }

    private fun inject() = DaggerTaskOneComponent.builder().apply {
        commonModule(CommonModule(requireContext()))
        taskOneModule(TaskOneModule())
    }.build().inject(this)
}