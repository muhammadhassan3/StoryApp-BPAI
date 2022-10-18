package com.muhammhassan.storyapp.data.datasource

import android.app.Application
import android.content.Context
import com.muhammhassan.storyapp.utils.Constant

class LocalDataSource(app: Application) {
    private val sharedPrefs = app.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String){
        sharedPrefs.edit().putString(Constant.TOKEN_KEY, token).apply()
    }

    fun removeToken(){
        sharedPrefs.edit().remove(Constant.TOKEN_KEY).apply()
    }

    fun getToken(): String?{
        return sharedPrefs.getString(Constant.TOKEN_KEY, null)
    }

    companion object{
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(app: Application): LocalDataSource = INSTANCE ?: synchronized(this){
            val instance = LocalDataSource(app)
            INSTANCE = instance
            instance
        }
    }
}