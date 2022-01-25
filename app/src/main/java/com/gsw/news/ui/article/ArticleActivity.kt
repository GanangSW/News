package com.gsw.news.ui.article

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gsw.news.R
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivityArticleBinding
import com.gsw.news.models.response.ArticlesItem
import com.gsw.news.other.Constant
import com.gsw.news.other.Tools.Companion.hideLoading
import com.gsw.news.other.Tools.Companion.setFalse
import com.gsw.news.other.Tools.Companion.showLoading
import com.gsw.news.other.Tools.Companion.tText
import com.gsw.news.other.Tools.Companion.toGone
import com.gsw.news.other.Tools.Companion.toVisible
import com.gsw.news.other.Tools.Companion.toastCenter
import com.gsw.news.other.observe
import com.gsw.news.other.viewBinding
import com.gsw.news.ui.article.adapter.AdapterArticle
import com.gsw.news.ui.article.detail.DetailArticleActivity
import com.gsw.news.ui.search.SearchActivity
import java.util.*

class ArticleActivity : BaseActivity() {

    private val binding by viewBinding<ActivityArticleBinding>()
    private val viewModel by viewModels<ArticleViewModel>()

    private val query: MutableMap<String, String> = mutableMapOf()
    private var sources = ""
    private var name = ""
    private var newData: MutableList<ArticlesItem> = mutableListOf()
    private var page = 1
    private var load = true
    private lateinit var llm: LinearLayoutManager
    private lateinit var adapterArticle: AdapterArticle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sources = intent.getStringExtra("sources") ?: ""
        name = intent.getStringExtra("name") ?: ""
        binding.apply {
            srlArticle.setOnRefreshListener {
                initData()
                srlArticle.setFalse()
            }
            incToolbar.apply {
                ivBack.setOnClickListener {
                    finish()
                }
                tvTitle.tText(name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })
                ivSearch.setOnClickListener {
                    startActivity(Intent(this@ArticleActivity, SearchActivity::class.java))
                }
            }
        }
        llm = LinearLayoutManager(this)

        initData()

        adapterArticle = AdapterArticle(object : AdapterArticle.CustomListeners {
            override fun onItemSelected(item: ArticlesItem) {
                startActivity(
                    Intent(
                        this@ArticleActivity,
                        DetailArticleActivity::class.java
                    ).apply {
                        putExtra("url", item.url)
                    })
            }
        })
    }

    private fun initData() {
        newData.clear()
        page = 1
        query.clear()
        query[Constant.qSources] = sources
        query[Constant.qPage] = page.toString()
        query[Constant.qApiKey] = Constant.apiKey
        viewModel.getArticle(query = query)
    }

    override fun observeChange() {
        viewModel.apply {
            observe(loading) {
                if (it) showLoading() else hideLoading()
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
            observe(dataGetArticle) {
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

            observe(loading2) {
                if (it) binding.pbArticle.toVisible() else binding.pbArticle.toGone()
            }
            observe(error2) {
                toastCenter(it)
            }
            observe(dataGetArticleMore) {
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

    private fun initDataMore() {
        query[Constant.qSources] = sources
        query[Constant.qPage] = page.toString()
        query[Constant.qApiKey] = Constant.apiKey
        viewModel.getArticleMore(query = query)
    }

    private fun setData(data: List<ArticlesItem>) {
        adapterArticle.submitList(data)
    }
}