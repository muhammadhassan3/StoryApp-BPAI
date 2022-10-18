package com.muhammhassan.storyapp.view.list

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import kotlinx.coroutines.flow.Flow

class ListStoryViewModel(val useCase: StoryListUseCase) :
    ViewModel() {
    fun getStoryList(): Flow<PagingData<StoriesResponseModel>> = useCase.getPagingData()
    fun logout() {
        useCase.logout()
    }
}