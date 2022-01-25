package com.gsw.news.ui.search

import androidx.lifecycle.MutableLiveData
import com.gsw.news.base.BaseViewModel
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.models.response.SourcesItem
import com.gsw.news.other.Resource
import com.gsw.news.other.SingleLiveEvent
import com.gsw.news.repo.callGetArticle
import com.gsw.news.repo.callGetEverything
import com.gsw.news.repo.callGetSources

class SearchViewModel : BaseViewModel() {

    val dataGetEverything: SingleLiveEvent<Resource<List<ArticlesItem>>> by lazy { SingleLiveEvent() }
    val dataGetEverythingMore: SingleLiveEvent<Resource<List<ArticlesItem>>> by lazy { SingleLiveEvent() }

    fun getEverything(query: Map<String, String>) {
        launch {
            callGetEverything(query = query)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            if (status == "ok") {
                                if (!articles.isNullOrEmpty()) {
                                    dataGetEverything.value =
                                        Resource(data = articles, totalResult = totalResults)
                                } else {
                                    error.value = "Data Empty"
                                }
                            } else {
                                error.value = "Error"
                            }
                        }
                    } else {
                        error.value = it.message()
                    }
                }, {
                    error.value = it.message
                })
        }
    }

    fun getEverythingMore(query: Map<String, String>) {
        launch {
            callGetEverything(query = query)
                .doOnSubscribe { loading2.value = true }
                .doOnTerminate { loading2.value = false }
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            if (status == "ok") {
                                if (!articles.isNullOrEmpty()) {
                                    dataGetEverythingMore.value =
                                        Resource(data = articles, totalResult = totalResults)
                                } else {
                                    error2.value = "Data Empty"
                                }
                            } else {
                                error2.value = "Error"
                            }
                        }
                    } else {
                        error2.value = it.message()
                    }
                }, {
                    error2.value = it.message
                })
        }
    }

}