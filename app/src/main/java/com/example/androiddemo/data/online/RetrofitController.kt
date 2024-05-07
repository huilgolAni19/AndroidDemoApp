package com.example.androiddemo.data.online

import com.example.androiddemo.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSession

class RetrofitController private constructor() {

    companion object {
        private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val client = OkHttpClient().newBuilder()
            .hostnameVerifier(HostnameVerifier { _: String, _: SSLSession ->
                HttpsURLConnection.getDefaultHostnameVerifier()
                return@HostnameVerifier true
            })
            .apply {
                addInterceptor(httpLoggingInterceptor)
                connectTimeout(20, TimeUnit.SECONDS)
                readTimeout(20, TimeUnit.SECONDS)
            }.build()

        fun getInstance(): RetrofitController {
            return RetrofitController()
        }
    }

    val retrofit: Retrofit

        get() = Retrofit.Builder().apply {
            addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(Const.BASE_URL)
            client(client)
        }.build()
}