package com.muhammhassan.storyapp.data.usecase

import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface DetailStoryUseCase {
    fun save(image: File, desc: String): Flow<ApiResponse<Any>>
}