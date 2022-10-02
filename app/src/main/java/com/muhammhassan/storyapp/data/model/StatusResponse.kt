package com.muhammhassan.storyapp.data.model

import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel

data class StatusResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResponseModel,
    val listStory: List<StoriesResponseModel>
)