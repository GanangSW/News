package com.gsw.news.ui.article.detail

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gsw.news.R
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivityDetailArticleBinding
import com.gsw.news.other.Tools.Companion.debounceClick
import com.gsw.news.other.Tools.Companion.hideLoading
import com.gsw.news.other.Tools.Companion.showLoading
import com.gsw.news.other.Tools.Companion.toGone
import com.gsw.news.other.Tools.Companion.toVisible
import com.gsw.news.other.viewBinding

class DetailArticleActivity : BaseActivity() {

    private val binding by viewBinding<ActivityDetailArticleBinding>()

    private var articleUrl = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleUrl = intent.getStringExtra("url") ?: ""
        Log.d("TAG", "onCreate: $articleUrl")
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvDetailArticleLoading.toVisible()
            webView.apply {
                webView.settings.javaScriptEnabled = true

                webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        if (url != null) {
                            view?.loadUrl(url)
                        }
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        tvDetailArticleLoading.toGone()
                    }
                }
                webView.loadUrl(articleUrl)
            }
        }

    }

    override fun observeChange() = Unit
}