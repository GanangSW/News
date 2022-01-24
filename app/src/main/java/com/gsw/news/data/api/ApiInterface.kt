package com.gsw.news.data.api

import com.gsw.news.models.response.ResponseGetArticle
import com.gsw.news.models.response.ResponseGetSources
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("top-headlines?sortBy=publishedAt")
    fun getArticle(@QueryMap query: Map<String, String>): Observable<Response<ResponseGetArticle>>

    @GET("top-headlines/sources")
    fun getSources(@QueryMap query: Map<String, String>): Observable<Response<ResponseGetSources>>

    @GET("everything?sortBy=publishedAt")
    fun getEveryting(@QueryMap query: Map<String, String>): Observable<Response<ResponseGetArticle>>

}