package com.example.androiddemo.fragments.mainmenufragment

import androidx.lifecycle.ViewModel

class MainMenuFragmentViewModel: ViewModel() {

    val menuItems: ArrayList<String> = ArrayList()

    init {
        menuItems.add("Task 1")
        menuItems.add("Task 2")
        menuItems.add("Task 3")
        menuItems.add("Task 4")
    }
}