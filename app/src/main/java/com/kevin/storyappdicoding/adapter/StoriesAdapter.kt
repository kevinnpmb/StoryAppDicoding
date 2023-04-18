package com.kevin.storyappdicoding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kevin.storyappdicoding.R
import com.kevin.storyappdicoding.database.model.Story
import com.kevin.storyappdicoding.databinding.StoryItemBinding
import com.kevin.storyappdicoding.utils.Utilities.setImageResource
import com.squareup.picasso.Picasso

class StoriesAdapter(val detailCallback: (Story) -> Unit) :
    ListAdapter<Story, StoriesAdapter.StoryViewHolder>(DiffCallback()) {
    private lateinit var context: Context

    inner class StoryViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Story) {
            binding.apply {
                userImage.setImageResource(item.photoPath)
                userName.text = item.name
                root.setOnClickListener {
                    detailCallback.invoke(item)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Story>() {
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
        getItem(position)?.let { story ->
            holder.bind(story)
        }
    }
}