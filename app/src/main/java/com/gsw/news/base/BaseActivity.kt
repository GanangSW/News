package com.gsw.news.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gsw.news.R
import com.gsw.news.other.Tools.Companion.colorStatusBar

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        colorStatusBar(R.color.white)
        observeChange()
    }

    abstract fun observeChange()
}