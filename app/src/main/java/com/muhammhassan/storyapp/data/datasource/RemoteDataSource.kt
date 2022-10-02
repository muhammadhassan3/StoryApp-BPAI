package com.muhammhassan.storyapp.data.datasource

import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.Utils.parsingError
import com.muhammhassan.storyapp.utils.api.ApiInterface
import com.muhammhassan.storyapp.utils.api.ApiResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RemoteDataSource(private val api: ApiInterface) {

    suspend fun login(user: LoginModel): ApiResponse<LoginResponseModel> {
        return try {
            val response = api.login(user)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                if (data.error) {
                    ApiResponse.error(data.message)
                } else ApiResponse.success(data.loginResult)
            } else ApiResponse.error(parsingError(response))
        } catch (e: Exception) {
            ApiResponse.error(e.message.toString())
        }
    }

    suspend fun register(user: RegisterModel): ApiResponse<Any> {
        return try {
            val response = api.register(user)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                if (data.error) {
                    ApiResponse.error(data.message)
                } else ApiResponse.success(null)
            } else {
                ApiResponse.error(parsingError(response))
            }
        } catch (e: Exception) {
            ApiResponse.error(e.message.toString())
        }
    }

    suspend fun getAllStories(page: Int, size: Int): ApiResponse<List<StoriesResponseModel>> {
        return try {
            val response = api.getStory(page, size)
            if (response.isSuccessful && response.body() != null) {
                val data = response.body()!!
                if (data.error) {
                    ApiResponse.error(data.message)
                } else {
                    if (data.listStory.isNotEmpty()) {
                        ApiResponse.success(data.listStory)
                    } else ApiResponse.noData()
                }
            } else ApiResponse.error(parsingError(response))
        } catch (e: Exception) {
            ApiResponse.error(e.message.toString())
        }
    }

    suspend fun saveStory(image: File, desc: String): ApiResponse<Any> {
        val description = desc.toRequestBody("text/plain".toMediaType())
        val imagepart = image.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo",
            image.name,
            imagepart
        )

        return try {
            val response = api.addStory(imageMultipart, description)
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.error) {
                    ApiResponse.error(response.body()?.message.toString())
                } else ApiResponse.success(null)
            } else ApiResponse.error(parsingError(response))
        } catch (e: Exception) {
            ApiResponse.error(e.message.toString())
        }
    }

    companion object {
        private var INSTANCE: RemoteDataSource? = null

        fun getInstance(api: ApiInterface): RemoteDataSource = INSTANCE ?: synchronized(this) {
            val instance = RemoteDataSource(api)
            INSTANCE = instance
            instance
        }
    }
}