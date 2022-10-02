package com.muhammhassan.storyapp.data.repository

import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val remoteDataSource: RemoteDataSource): UserRepository {
    override fun login(user: LoginModel): Flow<ApiResponse<LoginResponseModel>> = flow{
        emit(ApiResponse.loading())
        val response = remoteDataSource.login(user)
        emit(response)
    }

    override fun register(user: RegisterModel): Flow<ApiResponse<Any>> = flow {
        emit(ApiResponse.loading())
        val response = remoteDataSource.register(user)
        emit(response)
    }

    companion object{
        private var INSTANCE: UserRepositoryImpl? = null

        fun getInstance(remoteDataSource: RemoteDataSource): UserRepositoryImpl
            = INSTANCE ?: synchronized(this){
                val instance = UserRepositoryImpl(remoteDataSource)
            INSTANCE = instance
            instance
        }
    }
}