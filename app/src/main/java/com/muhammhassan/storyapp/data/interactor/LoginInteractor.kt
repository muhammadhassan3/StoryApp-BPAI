package com.muhammhassan.storyapp.data.interactor

import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.repository.UserRepository
import com.muhammhassan.storyapp.data.usecase.LoginUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

class LoginInteractor(private val userRepository: UserRepository) : LoginUseCase {
    override fun login(user: LoginModel): Flow<ApiResponse<LoginResponseModel>> =
        userRepository.login(user)

    override fun saveToken(token: String) {
        userRepository.saveToken(token)
    }

    override fun getToken(): String? {
        return userRepository.getToken()
    }

    companion object {
        private var INSTANCE: LoginInteractor? = null

        fun getInstance(userRepository: UserRepository): LoginInteractor =
            INSTANCE ?: synchronized(this) {
                val instance = LoginInteractor(userRepository)
                INSTANCE = instance
                instance
            }
    }
}