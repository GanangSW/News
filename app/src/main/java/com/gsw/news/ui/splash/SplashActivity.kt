package com.gsw.news.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gsw.news.R
import com.gsw.news.base.BaseActivity
import com.gsw.news.databinding.ActivitySplashBinding
import com.gsw.news.other.viewBinding
import com.gsw.news.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val binding by viewBinding<ActivitySplashBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            Handler(mainLooper).postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 1000)
        }
    }

    override fun observeChange() = Unit
}