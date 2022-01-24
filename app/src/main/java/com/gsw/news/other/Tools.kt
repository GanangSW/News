package com.gsw.news.other

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.icu.util.ULocale
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*
import android.icu.util.ULocale.getCountry
import android.icu.util.ULocale.getCountry
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.ParseException


@Suppress("DEPRECATION")
class Tools {

    companion object {
        private var toast: Toast? = null
        private var progressDialog: ProgressDialog? = null

        fun Context.toastCenter(msg: String) {
            if (toast != null) {
                toast?.cancel()
            }
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            toast?.setGravity(Gravity.CENTER, 0, 0)
            toast?.show()
        }

        fun View.toVisible() {
            this.visibility = View.VISIBLE
        }

        fun View.toGone() {
            this.visibility = View.GONE
        }

        fun View.toInvisible() {
            this.visibility = View.INVISIBLE
        }

        fun Context.showLoading() {
            if (progressDialog == null || progressDialog?.isShowing == false) {
                progressDialog = ProgressDialog(this)
                progressDialog?.apply {
                    setMessage("Loading...")
                    setCancelable(false)
                    show()
                }
            } else {
                progressDialog?.dismiss()
                progressDialog?.cancel()
            }
        }

        fun hideLoading() {
            try {
                if (progressDialog == null || progressDialog?.isShowing == true) {
                    progressDialog?.dismiss()
                    progressDialog?.cancel()
                }
            } catch (e: UninitializedPropertyAccessException) {
                progressDialog?.dismiss()
                progressDialog?.cancel()
                //  Log.d("TAG","AlertDialog UninitializedPropertyAccessException")
            }
        }

        fun SwipeRefreshLayout.setFalse() {
            if (this.isRefreshing) {
                this.isRefreshing = false
            }
        }

        inline fun View.debounceClick(
            intervalInMillis: Int = 1000,
            crossinline listener: (view: View) -> Unit
        ) {
            var lastClick = 0L
            setOnClickListener {
                val diff = System.currentTimeMillis() - lastClick
                lastClick = System.currentTimeMillis()
                if (diff > intervalInMillis) {
                    listener(it)
                }
            }
        }

        fun Context.colorStatusBar(color: Int) {
            (this as Activity).window.run {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                statusBarColor = ContextCompat.getColor(context, color)
            }
        }

        fun ImageView.loadImageGlide(context: Context, url: String?) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            circularProgressDrawable.setColorSchemeColors(Color.GREEN)
            val requestOptions = RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(android.R.drawable.ic_dialog_info)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .transform(CenterCrop(), RoundedCorners(8))
            Glide.with(context).load(url).apply(requestOptions).into(this)
        }

        fun TextView.tText(value: String?) {
            if (value.isNullOrEmpty() || value == "null") this.text = "" else this.text = value
        }

        fun DateFormat(existingStringDate: String?): String? {
            val newDate: String?
            val simpleDateFormat = SimpleDateFormat("E, d MMM yyyy", Locale(getCountry()))
            newDate = try {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(existingStringDate)
                simpleDateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
                existingStringDate
            }
            return newDate
        }

        private fun getCountry(): String {
            val locale = Locale.getDefault()
            val country = locale.country
            return country.toLowerCase(Locale.getDefault())
        }

        fun EditText.afterTextChanged(onTextChanged: (CharSequence?) -> Unit) {
            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(editable: Editable?){
                    onTextChanged.invoke(editable)
                }
            })
        }
    }
}