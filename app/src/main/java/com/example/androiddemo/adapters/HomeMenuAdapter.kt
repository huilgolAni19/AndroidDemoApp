package com.example.androiddemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.databinding.HomeMenuItemBinding

class HomeMenuAdapter(private val items: ArrayList<String>,
                      private val itemClick: (String, Int) -> Unit):
    RecyclerView.Adapter<HomeMenuAdapter.HomeMenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMenuViewHolder {
        val binding: HomeMenuItemBinding = HomeMenuItemBinding
            .inflate(LayoutInflater.from(parent.context))
        return HomeMenuViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HomeMenuViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    inner class HomeMenuViewHolder(private var mBinding: HomeMenuItemBinding):
        RecyclerView.ViewHolder(mBinding.root) {
            fun bind(value: String, position: Int) {
                mBinding.textViewHomeMenu.text = value
                mBinding.textViewHomeMenu.setOnClickListener {
                    itemClick.invoke(value, position)
                }
                mBinding.root.setOnClickListener {
                    itemClick.invoke(value, position)
                }
            }
        }
}
