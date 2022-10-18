package com.muhammhassan.storyapp.data.repository

import com.muhammhassan.storyapp.data.datasource.LocalDataSource
import com.muhammhassan.storyapp.data.datasource.RemoteDataSource
import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository(
    private val dataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) :
    UserRepository {
    override fun login(user: LoginModel): Flow<ApiResponse<LoginResponseModel>> = flow {
        emit(dataSource.login(user))
    }

    override fun register(user: RegisterModel): Flow<ApiResponse<Any>> = flow {
        emit(dataSource.register(user))
    }

    override fun saveToken(token: String) {
        localDataSource.saveToken(token)
    }

    override fun getToken(): String? {
        return localDataSource.getToken()
    }

    override fun logout() {
        localDataSource.removeToken()
    }
}