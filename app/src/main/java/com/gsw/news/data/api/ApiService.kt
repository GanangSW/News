package com.gsw.news.data.api

import com.gsw.news.other.Constant
import retrofit2.Retrofit

object ApiService {

    private fun <S> createService(serviceClass: Class<S>): S {

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.baseUrl)
            .addCallAdapterFactory(ApiWorker.rxCallAdapter)
            .addConverterFactory(ApiWorker.gsonConverter)
            .client(ApiWorker.client)
            .build()

        return retrofit.create(serviceClass)
    }

    val api by lazy {
        createService(
            ApiInterface::class.java
        )
    }
}