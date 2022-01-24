package com.gsw.news.other

class Resource<T>(val data: T?, val totalResult: Int?) {
    companion object {
        fun <T> success(data: T, totalResult: Int): Resource<T> {
            return Resource(data, totalResult)
        }
    }
}

data class ResponseError(val code: String, val message: String)