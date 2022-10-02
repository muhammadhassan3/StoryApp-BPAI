package com.muhammhassan.storyapp.utils

import android.content.Context
import com.muhammhassan.storyapp.utils.Constant.SHARED_PREF_NAME
import com.muhammhassan.storyapp.utils.Constant.TOKEN_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(
            TOKEN_KEY, null
        )
        val request = chain.request()
            .newBuilder()
        if (token != null) {
            request.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}