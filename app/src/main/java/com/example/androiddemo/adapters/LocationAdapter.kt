package com.example.androiddemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.data.offline.entities.LocationEntity
import com.example.androiddemo.databinding.LoactionItemRowBinding

class LocationAdapter(private val array: ArrayList<LocationEntity>):
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {



    inner class LocationViewHolder(private val binding: LoactionItemRowBinding)
        : RecyclerView.ViewHolder(binding.root) {
            fun bind(entity: LocationEntity) {
                binding.textViewId.text = entity.id!!.toString()
                binding.textViewLat.text = entity.lat.toString()
                binding.textViewLon.text = entity.lon.toString()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val binding: LoactionItemRowBinding = LoactionItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return LocationViewHolder(binding)
    }

    override fun getItemCount(): Int = array.size

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        holder.bind(array[position])
    }
}