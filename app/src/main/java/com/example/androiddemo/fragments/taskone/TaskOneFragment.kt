package com.example.androiddemo.fragments.taskone

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import com.example.androiddemo.R
import com.example.androiddemo.databinding.FragmentTaskOneBinding
import com.example.androiddemo.di.components.DaggerTaskOneComponent
import com.example.androiddemo.di.modules.CommonModule
import com.example.androiddemo.di.modules.TaskOneModule
import com.example.androiddemo.fragments.taskone.fragmentcomments.CommentsFragment
import com.example.androiddemo.fragments.taskone.fragmentnews.NewsFragment
import com.example.androiddemo.repository.PostsAndCommentsRepository
import com.example.androiddemo.viewmodelfactories.ViewModelFactoryWithPostAndCommentRepository
import javax.inject.Inject

class TaskOneFragment : Fragment() {

    private lateinit var binding: FragmentTaskOneBinding
    private lateinit var factory: ViewModelFactoryWithPostAndCommentRepository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager1: FragmentManager = requireActivity().supportFragmentManager
        val fragment1 = NewsFragment()
        fragmentManager1.beginTransaction()
            .add(binding.container1.id, fragment1).commit()

        val fragmentManager2: FragmentManager = requireActivity().supportFragmentManager
        val fragment2 = CommentsFragment()
        fragmentManager2.beginTransaction()
            .add(binding.container2.id, fragment2).commit()
    }

}