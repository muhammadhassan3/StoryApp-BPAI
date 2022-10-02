package com.muhammhassan.storyapp.data.usecase

import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface RegisterUseCase {
    fun register(user: RegisterModel): Flow<ApiResponse<Any>>
}