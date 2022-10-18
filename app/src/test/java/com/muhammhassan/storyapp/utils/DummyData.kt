package com.muhammhassan.storyapp.utils

import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.utils.api.ApiResponse
import java.io.File

object DummyData {
    val image: File = File("Testing")
    const val desc = "Description"
    const val latitude = 0.0
    const val longitude = 1.0

    private fun getListData(): List<StoriesResponseModel>{
        val listData = mutableListOf<StoriesResponseModel>()
        for(i in 1..20){
            listData.add(StoriesResponseModel(
                "Id",
                "Name",
                "Description",
                "photoUrl",
                0.0,
                0.0
            ))
        }

        return listData
    }

    val listResponse = ApiResponse.success(
        getListData()
    )
    val saveResponse = ApiResponse.success(Any())

    val userLoginRequest = LoginModel("email@test.com","123456")
    val userLoginResponse = LoginResponseModel("userId", "User name", "Token")
    val userRegisterRequest = RegisterModel("User name", "email@test.com", "123456")
}