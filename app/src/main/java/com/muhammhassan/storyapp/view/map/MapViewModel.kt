package com.muhammhassan.storyapp.view.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.usecase.MapUseCase
import com.muhammhassan.storyapp.utils.api.ApiResponse
import kotlinx.coroutines.launch

class MapViewModel(private val useCase: MapUseCase) : ViewModel() {
    private val _data = MutableLiveData<ApiResponse<List<StoriesResponseModel>>>()
    val data: LiveData<ApiResponse<List<StoriesResponseModel>>> get() = _data
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> get() = _status

    fun setStatus(status: String){
        _status.value = status
    }

    fun getStoriesList(){
        viewModelScope.launch {
            useCase.getStoryWithLocation().collect{
                _data.postValue(it)
            }
        }
    }
}