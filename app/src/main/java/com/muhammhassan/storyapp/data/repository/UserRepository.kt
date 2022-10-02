package com.muhammhassan.storyapp.data.repository

import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun login(user: LoginModel): Flow<ApiResponse<LoginResponseModel>>
    fun register(user: RegisterModel): Flow<ApiResponse<Any>>
}