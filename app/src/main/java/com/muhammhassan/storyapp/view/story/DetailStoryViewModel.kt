package com.muhammhassan.storyapp.view.story

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.usecase.DetailStoryUseCase
import com.muhammhassan.storyapp.utils.Utils.uriToFile
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.launch
import java.io.File

class DetailStoryViewModel(private val useCase: DetailStoryUseCase, private val mApp: Application) :
    AndroidViewModel(mApp) {
    private val _data = MutableLiveData<ApiResponse<StoriesResponseModel>>()
    val data: LiveData<ApiResponse<StoriesResponseModel>> get() = _data
    var image: File? = null
    private var desc: String? = null
    var type: Int = 0 //0 = Add; 1 = Detail

    fun setData(data: StoriesResponseModel) {
        _data.value = ApiResponse.success(data)
    }

    fun setImage(image: String) {
        this.image = File(image)
    }

    fun setImage(uri: Uri, context: Context) {
        this.image = uriToFile(uri, context)
    }

    fun setText(desc: String) {
        this.desc = desc
    }

    fun save() {
        if (image != null && !desc.isNullOrEmpty()) {
            viewModelScope.launch {
                useCase.save(image!!, desc!!).collect {
                    _data.postValue(ApiResponse(it.status, null, it.message))
                }
            }
        } else {
            _data.value = ApiResponse.error(mApp.applicationContext.getString(R.string.check_input))
        }
    }
}