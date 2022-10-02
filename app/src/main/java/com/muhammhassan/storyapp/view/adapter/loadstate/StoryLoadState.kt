package com.muhammhassan.storyapp.view.adapter.loadstate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.muhammhassan.storyapp.databinding.LayoutStoryPlaceholderBinding
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.view.adapter.viewholder.StoryPlaceholderViewHolder

class StoryLoadState(private val retry: () -> Unit): LoadStateAdapter<StoryPlaceholderViewHolder>() {
    override fun onBindViewHolder(holder: StoryPlaceholderViewHolder, loadState: LoadState) {
        holder.binding.apply {
            if(loadState is LoadState.Loading){
                if(!loadState.endOfPaginationReached){
                    errorContent.gone()
                    noDataContent.gone()
                    shimmer.apply{
                        startShimmer()
                        show()
                    }
                }
            }else{
                errorContent.show()
                btnRetry.setOnClickListener{
                    retry.invoke()
                }
                noDataContent.gone()
                shimmer.apply {
                    stopShimmer()
                    gone()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): StoryPlaceholderViewHolder {
        val binding = LayoutStoryPlaceholderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryPlaceholderViewHolder(binding)
    }
}