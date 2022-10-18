package com.muhammhassan.storyapp.data.interactor

import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.repository.StoryRepository
import com.muhammhassan.storyapp.data.repository.UserRepository
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import kotlinx.coroutines.flow.Flow

class StoryListInteractor(
    private val repository: StoryRepository,
    private val userRepository: UserRepository
) : StoryListUseCase {
    override fun getPagingData(): Flow<PagingData<StoriesResponseModel>> =
        repository.getPagingStories()

    override fun logout() {
        userRepository.logout()
    }

    companion object {
        private var INSTANCE: StoryListInteractor? = null

        fun getInstance(
            repository: StoryRepository,
            userRepository: UserRepository
        ): StoryListInteractor =
            INSTANCE ?: synchronized(this) {
                val instance = StoryListInteractor(repository, userRepository)
                INSTANCE = instance
                instance
            }
    }
}