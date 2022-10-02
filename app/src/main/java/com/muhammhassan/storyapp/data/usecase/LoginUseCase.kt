package com.muhammhassan.storyapp.data.usecase

import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
    fun login(user: LoginModel): Flow<ApiResponse<LoginResponseModel>>
}