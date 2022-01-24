package com.gsw.news.other

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

fun <T> Observable<T>.workOnBackground(): Observable<T> =
    subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .handleNetworkError()

fun <T> Observable<T>.handleNetworkError(): Observable<T> =
    onErrorResumeNext { e: Throwable ->
        when (e) {
            is OfflineException -> return@onErrorResumeNext Observable.error(Exception("Jaringan tidak tersedia"))
            is UnknownHostException -> return@onErrorResumeNext Observable.error(Exception("Jaringan tidak tersedia"))
            is SocketTimeoutException -> return@onErrorResumeNext Observable.error(Exception("Koneksi terhenti. Silahkan coba lagi"))
            is HttpException -> {
                val responseBody = e.response()?.errorBody()
                return@onErrorResumeNext Observable.error(Exception(responseBody?.run {
                    getErrorMessage(
                        responseBody
                    )
                }))
            }
            is IOException -> return@onErrorResumeNext Observable.error(Exception("Tidak terhubung server"))
            else -> return@onErrorResumeNext Observable.error(e)
        }
    }

fun getErrorMessage(responseBody: ResponseBody): String? {
    return try {
        val jsonObject = JSONObject(responseBody.string())
        jsonObject.getString("message")
    } catch (e: Exception) {
        e.message
    }

}

class OfflineException : IOException() {
    override val message: String
        get() = "no internet!"
}