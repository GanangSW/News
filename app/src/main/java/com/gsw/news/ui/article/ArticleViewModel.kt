package com.gsw.news.ui.article

import androidx.lifecycle.MutableLiveData
import com.gsw.news.base.BaseViewModel
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.models.response.SourcesItem
import com.gsw.news.other.Resource
import com.gsw.news.other.SingleLiveEvent
import com.gsw.news.repo.callGetArticle
import com.gsw.news.repo.callGetSources

class ArticleViewModel : BaseViewModel() {

    val dataGetArticle: SingleLiveEvent<Resource<List<ArticlesItem>>> by lazy { SingleLiveEvent() }
    val dataGetArticleMore: SingleLiveEvent<Resource<List<ArticlesItem>>> by lazy { SingleLiveEvent() }

    fun getArticle(query: Map<String, String>) {
        launch {
            callGetArticle(query = query)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe({
                    when {
                        it.isSuccessful -> {
                            it.body()?.apply {
                                if (status == "ok") {
                                    if (!articles.isNullOrEmpty()) {
                                        dataGetArticle.value =
                                            totalResults?.let { it1 -> Resource.success(data = articles, totalResult = it1) }
                                    } else {
                                        error.value = "Data Empty"
                                    }
                                } else {
                                    error.value = "Error"
                                }
                            }
                        }
                        it.code() == 429 -> {
                            error.value = "Harap coba lagi nanti"
                        }
                        else -> {
                            error.value = it.message()
                        }
                    }
                }, {
                    error.value = it.message
                })
        }
    }

    fun getArticleMore(query: Map<String, String>) {
        launch {
            callGetArticle(query = query)
                .doOnSubscribe { loading2.value = true }
                .doOnTerminate { loading2.value = false }
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            if (status == "ok") {
                                if (!articles.isNullOrEmpty()) {
                                    dataGetArticle.value =
                                        totalResults?.let { it1 -> Resource.success(data = articles, totalResult = it1) }
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