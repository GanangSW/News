package com.gsw.news.ui.article.detail

import android.annotation.SuppressLint
import android.net.http.SslError
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.ProgressBar
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
        articleUrl =
            intent.getStringExtra("url") ?: throw IllegalStateException("$articleUrl Missing..")
        Log.d("TAG", "onCreate: $articleUrl")
        binding.ivBack.setOnClickListener {
            finish()
        }
        initWebView()
        setWebClient()
        binding.wvDetailArticle.loadUrl(articleUrl)
    }

    private fun setWebClient() {
        binding.apply {
            wvDetailArticle.webChromeClient = object :WebChromeClient(){
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    pbDetailArticle.progress = newProgress
                    if (newProgress < 100 && pbDetailArticle.visibility == ProgressBar.GONE){
                        pbDetailArticle.visibility = ProgressBar.VISIBLE
                    }
                    if (newProgress == 100){
                        pbDetailArticle.visibility = ProgressBar.GONE
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        binding.apply {
            wvDetailArticle.apply {
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    domStorageEnabled = true
                }
                webViewClient = object :WebViewClient(){
                    override fun onReceivedSslError(
                        view: WebView?,
                        handler: SslErrorHandler?,
                        error: SslError?
                    ) {
                        handler?.proceed()
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && binding.wvDetailArticle.canGoBack()){
            binding.wvDetailArticle.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun observeChange() = Unit
}