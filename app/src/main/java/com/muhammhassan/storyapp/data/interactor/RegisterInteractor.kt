package com.muhammhassan.storyapp.data.interactor

import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.repository.UserRepository
import com.muhammhassan.storyapp.data.usecase.RegisterUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow

class RegisterInteractor(private val repository: UserRepository): RegisterUseCase {
    override fun register(user: RegisterModel): Flow<ApiResponse<Any>> = repository.register(user)

    companion object{
        private var INSTANCE: RegisterInteractor? = null

        fun getInstance(repository: UserRepository): RegisterInteractor = INSTANCE ?: synchronized(this){
            val instance = RegisterInteractor(repository)
            INSTANCE = instance
            instance
        }
    }
}