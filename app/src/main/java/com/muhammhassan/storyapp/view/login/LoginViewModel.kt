package com.muhammhassan.storyapp.view.login

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.request.LoginModel
import com.muhammhassan.storyapp.data.model.response.LoginResponseModel
import com.muhammhassan.storyapp.data.usecase.LoginUseCase
import com.muhammhassan.storyapp.utils.Constant
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

    fun setToken(token: String, activity: Activity) {
        val sharedPref =
            activity.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(Constant.TOKEN_KEY, token)
        editor.apply()
    }
}