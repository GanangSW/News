package com.gsw.news.repo

import com.gsw.news.data.api.ApiService.api
import com.gsw.news.models.response.ResponseGetArticle
import com.gsw.news.models.response.ResponseGetSources
import com.gsw.news.other.workOnBackground
import io.reactivex.Observable
import retrofit2.Response

fun callGetArticle(query: Map<String, String>): Observable<Response<ResponseGetArticle>> =
    api.getArticle(query = query).workOnBackground()