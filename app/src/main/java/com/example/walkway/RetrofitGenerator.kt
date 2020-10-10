package com.example.walkway


import com.example.walkway.model.SignupModelInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitGenerator {

    val builder = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)

    //log 찍는 방법.
    init{
        val httpLoggingInterceptor  =  HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        builder.addInterceptor(httpLoggingInterceptor)
    }

    //retrofit 재설정.
    val okHttpClient = builder.build()

    private val retrofit = Retrofit.Builder().client(okHttpClient)
        .baseUrl("http://api.tingting.kr")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun login()  = retrofit.create(SignupModelInterface::class.java)


}