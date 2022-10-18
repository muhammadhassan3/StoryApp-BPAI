package com.muhammhassan.storyapp.utils.api

import com.muhammhassan.storyapp.data.model.StatusResponse
import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @POST("register")
    suspend fun register(@Body body: RegisterModel): Response<StatusResponse>

    @POST("login")
    suspend fun login(@Body body: LoginModel): Response<StatusResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part image: MultipartBody.Part,
        @Part("description") desc: RequestBody,
        @Part("lat") latitude: RequestBody?,
        @Part("lon") longitude: RequestBody?
    ): Response<StatusResponse>

    @GET("stories")
    suspend fun getStory(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<StatusResponse>

    @GET("stories?location=1")
    suspend fun getStoryWithLocation(): Response<StatusResponse>
}