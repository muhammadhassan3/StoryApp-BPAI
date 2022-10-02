package com.muhammhassan.storyapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.databinding.LayoutStoryListBinding
import com.muhammhassan.storyapp.utils.DiffUtils.storyDiffUtil
import com.muhammhassan.storyapp.utils.Extension.loadImage
import com.muhammhassan.storyapp.view.adapter.viewholder.StoryViewHolder

class StoryListAdapter(private val onClick: (item: StoriesResponseModel, image: ImageView) -> Unit) :
    PagingDataAdapter<StoriesResponseModel, StoryViewHolder>(storyDiffUtil) {
    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.apply {
            binding.apply {
                val item = getItem(position)
                tvTitle.text = item?.name ?: root.context.resources.getString(R.string.no_data)

                item?.let {
                    imgCover.loadImage(item.photoUrl)
                }
            }

            itemView.setOnClickListener {
                getItem(position)?.let { item -> onClick.invoke(item, binding.imgCover) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding =
            LayoutStoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }
}