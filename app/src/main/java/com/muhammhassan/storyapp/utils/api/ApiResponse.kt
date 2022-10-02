package com.muhammhassan.storyapp.utils.api

class ApiResponse<T>(val status: Status,val data: T?,val message: String?) {
    companion object{
        fun <T> loading(): ApiResponse<T>{
            return ApiResponse(Status.LOADING, null, null)
        }

        fun <T : Any> success(data: T?): ApiResponse<T>{
            return ApiResponse(Status.SUCCESS, data, null)
        }

        fun <T> noData(): ApiResponse<T>{
            return ApiResponse(Status.NO_DATA, null, null)
        }

        fun <T> error(message: String): ApiResponse<T>{
            return ApiResponse(Status.ERROR, null, message)
        }
    }
}