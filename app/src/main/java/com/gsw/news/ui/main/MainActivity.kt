package com.gsw.news.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gsw.news.R
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivityMainBinding
import com.gsw.news.models.response.ModelCategories
import com.gsw.news.other.Tools.Companion.toInvisible
import com.gsw.news.other.viewBinding
import com.gsw.news.ui.main.adapter.AdapterCategories
import com.gsw.news.ui.search.SearchActivity
import com.gsw.news.ui.sources.SourcesActivity

class MainActivity : BaseActivity() {

    private val binding by viewBinding<ActivityMainBinding>()

    private val categories: MutableList<ModelCategories> = mutableListOf()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            incToolbar.apply {
                ivBack.toInvisible()
                ivSearch.setOnClickListener {
                    startActivity(Intent(this@MainActivity, SearchActivity::class.java))
                }
            }
        }

        categories.add(
            ModelCategories(
                id = "1",
                categories = "business",
                image = resources.getDrawable(R.drawable.ic_round_business_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "2",
                categories = "entertainment",
                image = resources.getDrawable(R.drawable.ic_round_apps_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "3",
                categories = "general",
                image = resources.getDrawable(R.drawable.ic_round_article_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "4",
                categories = "health",
                image = resources.getDrawable(R.drawable.ic_round_health_and_safety_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "5",
                categories = "science",
                image = resources.getDrawable(R.drawable.ic_round_science_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "6",
                categories = "sports",
                image = resources.getDrawable(R.drawable.ic_round_sports_soccer_24)
            )
        )
        categories.add(
            ModelCategories(
                id = "7",
                categories = "technology",
                image = resources.getDrawable(R.drawable.ic_round_wb_incandescent_24)
            )
        )

        val adapterCategories = AdapterCategories(object : AdapterCategories.CustomListeners {
            override fun onItemSelected(item: ModelCategories) {
                startActivity(Intent(this@MainActivity, SourcesActivity::class.java).apply {
                    putExtra("category", item.categories)
                })
            }
        })
        adapterCategories.submitList(categories)
        binding.rvCategories.adapter = adapterCategories
    }

    override fun observeChange() = Unit
}