package com.muhammhassan.storyapp.view.list

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.paging.PagingData
import com.muhammhassan.storyapp.data.model.response.StoriesResponseModel
import com.muhammhassan.storyapp.data.usecase.StoryListUseCase
import com.muhammhassan.storyapp.utils.Constant
import kotlinx.coroutines.flow.Flow

class ListStoryViewModel(val useCase: StoryListUseCase, private val mApp: Application) :
    AndroidViewModel(mApp) {
    fun getStoryList(): Flow<PagingData<StoriesResponseModel>> = useCase.getPagingData()
    fun logout() {
        val sharedPref = mApp.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().remove(Constant.TOKEN_KEY).apply()
    }
}