package com.muhammhassan.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel

object DiffUtils {
    val storyDiffUtil = object: DiffUtil.ItemCallback<StoriesResponseModel>(){
        override fun areItemsTheSame(
            oldItem: StoriesResponseModel,
            newItem: StoriesResponseModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: StoriesResponseModel,
            newItem: StoriesResponseModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}