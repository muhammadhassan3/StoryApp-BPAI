package com.muhammhassan.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.muhammhassan.storyapp.utils.api.ApiResponse
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataUtil {
    fun <T> getValue(liveData: LiveData<ApiResponse<T>>): ApiResponse<T> {
        val data = arrayOfNulls<ApiResponse<T>>(1)
        val latch = CountDownLatch(1)

        val observer = object : Observer<ApiResponse<T>> {
            override fun onChanged(t: ApiResponse<T>?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }
        }

        liveData.observeForever(observer)

        try {
            latch.await(2, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return data[0] as ApiResponse<T>
    }

    fun <T> LiveData<T>.value(): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)

        val observer = object : Observer<T> {
            override fun onChanged(o: T) {
                data[0] = o
                latch.countDown()
                this@value.removeObserver(this)
            }
        }

        this.observeForever(observer)

        try {
            latch.await(2, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return data[0] as T
    }
}