package com.gsw.news.ui.sources

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivitySourcesBinding
import com.gsw.news.models.response.SourcesItem
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
import com.gsw.news.ui.article.ArticleActivity
import com.gsw.news.ui.search.SearchActivity
import com.gsw.news.ui.sources.adapter.AdapterSources
import java.util.*

class SourcesActivity : BaseActivity() {

    private val binding by viewBinding<ActivitySourcesBinding>()
    private val viewModel by viewModels<SourcesViewModel>()

    private val query: MutableMap<String, String> = mutableMapOf()
    private var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = intent.getStringExtra("category") ?: ""
        binding.apply {
            srlSources.setOnRefreshListener {
                query[Constant.qCategory] = category
                query[Constant.qApiKey] = Constant.apiKey
                viewModel.getSources(query = query)
                srlSources.setFalse()
            }
            incToolbar.apply {
                ivBack.setOnClickListener {
                    finish()
                }
                tvTitle.tText(category.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() })

                ivSearch.setOnClickListener {
                    startActivity(Intent(this@SourcesActivity, SearchActivity::class.java))
                }
            }
        }

        query[Constant.qCategory] = category
        query[Constant.qApiKey] = Constant.apiKey
        viewModel.getSources(query = query)
    }

    override fun observeChange() {
        viewModel.apply {
            observe(loading) {
                if (it) showLoading() else hideLoading()
            }
            observe(error) {
                binding.apply {
                    rvSources.toGone()
                    tvSourcesKosong.apply {
                        toVisible()
                        tText(it)
                    }
                }
            }
            observe(dataGetSources) {
                binding.apply {
                    tvSourcesKosong.toGone()
                    val adapterSources = AdapterSources(object : AdapterSources.CustomListeners {
                        override fun onItemSelected(item: SourcesItem) {
                            startActivity(
                                Intent(
                                    this@SourcesActivity,
                                    ArticleActivity::class.java
                                ).apply {
                                    putExtra("sources", item.id)
                                    putExtra("name", item.name)
                                })
                        }
                    })
                    adapterSources.submitList(it)
                    rvSources.apply {
                        toVisible()
                        adapter = adapterSources
                        hasFixedSize()
                    }
                }
            }
        }
    }
}