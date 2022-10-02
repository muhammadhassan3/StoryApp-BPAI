package com.muhammhassan.storyapp.data.interactor

import com.muhammhassan.storyapp.data.repository.StoryRepository
import com.muhammhassan.storyapp.data.usecase.DetailStoryUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

class DetailStoryInteractor(private val repository: StoryRepository) : DetailStoryUseCase {
    override fun save(image: File, desc: String): Flow<ApiResponse<Any>> =
        repository.saveStory(image, desc)

    companion object{
        private var INSTANCE: DetailStoryInteractor? = null

        fun getInstance(repository: StoryRepository) = INSTANCE ?: synchronized(this){
            val instance = DetailStoryInteractor(repository)
            INSTANCE = instance
            instance
        }
    }
}