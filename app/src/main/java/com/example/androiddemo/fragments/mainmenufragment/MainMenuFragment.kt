package com.example.androiddemo.fragments.mainmenufragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddemo.R
import com.example.androiddemo.adapters.HomeMenuAdapter
import com.example.androiddemo.databinding.FragmentMainMenuBinding


class MainMenuFragment : Fragment() {

    private var _binding: FragmentMainMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: HomeMenuAdapter
    private lateinit var viewModel: MainMenuFragmentViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        viewModel = ViewModelProvider(this)[MainMenuFragmentViewModel::class.java]
        adapter = HomeMenuAdapter(viewModel.menuItems) { name, id ->
            when(id) {

                0 -> {
                    navController.navigate(R.id.action_MainMenuFragment_to_taskOneFragment)
                }

                1 -> {
                    navController.navigate(R.id.action_MainMenuFragment_to_TaskTwoFragment)
                }

                2 -> {
                    navController.navigate(R.id.action_MainMenuFragment_to_taskThreeFragment)
                }

                3 -> {
                    navController.navigate(R.id.action_MainMenuFragment_to_taskFourFragment)
                }
            }
        }

        binding.recycleViewMainMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleViewMainMenu.itemAnimator = DefaultItemAnimator()
        binding.recycleViewMainMenu.adapter = adapter
    }

    private fun init() {
        navController = findNavController()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}