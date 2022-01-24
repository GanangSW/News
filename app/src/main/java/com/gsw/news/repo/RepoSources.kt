package com.gsw.news.repo

import com.gsw.news.data.api.ApiService.api
import com.gsw.news.models.response.ResponseGetSources
import com.gsw.news.other.workOnBackground
import io.reactivex.Observable
import retrofit2.Response

fun callGetSources(query: Map<String, String>): Observable<Response<ResponseGetSources>> =
    api.getSources(query = query).workOnBackground()