package com.gsw.news.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gsw.news.R
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivitySearchBinding
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.other.Constant
import com.gsw.news.other.Tools
import com.gsw.news.other.Tools.Companion.afterTextChanged
import com.gsw.news.other.Tools.Companion.setFalse
import com.gsw.news.other.Tools.Companion.showLoading
import com.gsw.news.other.Tools.Companion.tText
import com.gsw.news.other.Tools.Companion.toGone
import com.gsw.news.other.Tools.Companion.toVisible
import com.gsw.news.other.Tools.Companion.toastCenter
import com.gsw.news.other.observe
import com.gsw.news.other.viewBinding
import com.gsw.news.ui.article.ArticleViewModel
import com.gsw.news.ui.article.adapter.AdapterArticle
import com.gsw.news.ui.article.detail.DetailArticleActivity

class SearchActivity : BaseActivity() {

    private val binding by viewBinding<ActivitySearchBinding>()
    private val viewModel by viewModels<SearchViewModel>()

    private val query: MutableMap<String, String> = mutableMapOf()
    private var newData: MutableList<ArticlesItem> = mutableListOf()
    private var page = 1
    private var load = true
    private lateinit var llm: LinearLayoutManager
    private lateinit var adapterArticle: AdapterArticle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            srlArticle.setOnRefreshListener {
                etSearch.setText("")
                initData()
                srlArticle.setFalse()
            }

            etSearch.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    query.clear()
                    query["q"] = etSearch.text.toString().trim()
                    query["page"] = page.toString()
                    query[Constant.qApiKey] = Constant.apiKey
                    viewModel.getEverything(query = query)
                    return@setOnEditorActionListener true
                }
                false
            }
        }
        llm = LinearLayoutManager(this)

        initData()

        adapterArticle = AdapterArticle(object : AdapterArticle.CustomListeners {
            override fun onItemSelected(item: ArticlesItem) {
                startActivity(
                    Intent(
                        this@SearchActivity,
                        DetailArticleActivity::class.java
                    ).apply {
                        putExtra("url", item.url)
                    })
            }
        })
    }

    private fun initData() {
        page = 1
        query.clear()
        query["q"] = "a"
        query["page"] = page.toString()
        query[Constant.qApiKey] = Constant.apiKey
        viewModel.getEverything(query = query)
    }

    override fun observeChange() {
        viewModel.apply {
            observe(loading) {
                if (it) showLoading() else Tools.hideLoading()
            }
            observe(error) {
                binding.apply {
                    rvArticle.toGone()
                    tvArticleKosong.apply {
                        toVisible()
                        tText(it)
                    }
                }
            }
            observe(dataGetEverything) {
                binding.apply {
                    tvArticleKosong.toGone()
                    rvArticle.apply {
                        toVisible()
                        layoutManager = llm
                        adapter = adapterArticle
                        hasFixedSize()
                        addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrolled(
                                recyclerView: RecyclerView,
                                dx: Int,
                                dy: Int
                            ) {
                                if (dy > 0) {
                                    val visibleItemCount = llm.childCount
                                    val totalItemCount = llm.itemCount
                                    val pastVisibleItems =
                                        llm.findFirstVisibleItemPosition()
                                    if (!load) {
                                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                            load = true
                                            page += 1
                                            if (it.totalResult!! > 20) {
                                                initDataMore()
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                    for (element in it.data!!) {
                        newData.add(element)
                    }
                    val data = newData.distinctBy { it.title }
                    setData(data)
                    load = false
                }
            }

        }

    }

    private fun setData(data: List<ArticlesItem>) {
        adapterArticle.submitList(data)
    }

    private fun initDataMore() {
        query["page"] = page.toString()
        query[Constant.qApiKey] = Constant.apiKey
        viewModel.getEverythingMore(query = query)
        viewModel.apply {
            observe(loading2) {
                if (it) binding.pbArticle.toVisible() else binding.pbArticle.toGone()
            }
            observe(error2) {
                toastCenter("Last Page..")
            }
            observe(dataGetEverythingMore) {
                binding.apply {
                    rvArticle.apply {
                        addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrolled(
                                recyclerView: RecyclerView,
                                dx: Int,
                                dy: Int
                            ) {
                                if (dy > 0) {
                                    val visibleItemCount = llm.childCount
                                    val totalItemCount = llm.itemCount
                                    val pastVisibleItems =
                                        llm.findFirstVisibleItemPosition()
                                    if (!load) {
                                        if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                            load = true
                                            page += 1
                                            if (it.totalResult!! > 20) {
                                                initDataMore()
                                            }
                                        }
                                    }
                                }
                            }
                        })
                    }
                    for (element in it.data!!) {
                        newData.add(element)
                    }
                    val data = newData.distinctBy { it.title }
                    setData(data)
                    load = false
                }
            }
        }
    }
}