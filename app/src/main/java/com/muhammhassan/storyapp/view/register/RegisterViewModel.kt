package com.muhammhassan.storyapp.view.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.request.RegisterModel
import com.muhammhassan.storyapp.data.usecase.RegisterUseCase
import com.muhammhassan.storyapp.utils.Utils
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val useCase: RegisterUseCase, private val mApp: Application) : AndroidViewModel(mApp) {
    private val _response = MutableLiveData<ApiResponse<Any>>()
    val response: LiveData<ApiResponse<Any>> get() = _response

    fun register(name: String?, email: String?, password: String?){
        val context = mApp.applicationContext
        if(name.isNullOrEmpty()){
            _response.value = ApiResponse.error(context.getString(R.string.invalid_name))
        }else if(email.isNullOrEmpty() || !Utils.isEmailValid(email)){
            _response.value = ApiResponse.error(context.getString(R.string.invalid_email))
        }else if(password.isNullOrEmpty() || !Utils.isPasswordValid(password)){
            _response.value = ApiResponse.error(context.getString(R.string.password_less_than_six))
        }else{
            viewModelScope.launch {
                useCase.register(RegisterModel(name, email, password)).collect{
                    _response.postValue(it)
                }
            }
        }
    }
}