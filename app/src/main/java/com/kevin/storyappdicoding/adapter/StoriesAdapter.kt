package com.kevin.storyappdicoding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.data.model.Story
import com.kevin.storyappdicoding.databinding.StoryItemBinding
import com.kevin.storyappdicoding.utils.Utilities.setImageResource

class StoriesAdapter(detailCallback: (Story) -> Unit):
    ListAdapter<Story, StoriesAdapter.StoryViewHolder>(DiffCallback()) {
    private lateinit var context: Context

    inner class StoryViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            binding.apply {
                userImage.setImageResource(item.photoUrl)
                userName.text = item.name
            }
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        context = parent.context
        return StoryViewHolder(
            StoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}