package com.example.androiddemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddemo.databinding.CommentItemBinding
import com.example.androiddemo.models.comments.Comments
import com.example.androiddemo.models.comments.CommentsItem

class CommentsAdapter(private val comments: ArrayList<CommentsItem>):
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding: CommentItemBinding = CommentItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    inner class CommentsViewHolder(private val binding: CommentItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(comment: CommentsItem) {
                binding.postId.text = comment.postId.toString()
                binding.commentBody.text = comment.body
            }
    }
}