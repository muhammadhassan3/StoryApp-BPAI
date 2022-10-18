package com.muhammhassan.storyapp.view.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.usecase.LoginUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val useCase: LoginUseCase, private val mApplication: Application) :
    AndroidViewModel(mApplication) {
    private val _response = MutableLiveData<ApiResponse<LoginResponseModel>>()
    val response: LiveData<ApiResponse<LoginResponseModel>> get() = _response

    fun login(email: String?, password: String?) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _response.value =
                ApiResponse.error(mApplication.applicationContext.getString(R.string.invalid_email_password))
        } else {
            viewModelScope.launch {
                useCase.login(LoginModel(email, password)).collect {
                    _response.postValue(it)
                }
            }
        }
    }

    fun setToken(token: String) {
        useCase.saveToken(token)
    }

    fun getToken(): String?{
        return useCase.getToken()
    }
}