package com.example.walkway.model

import com.example.walkway.model.entitiy.login.LoginRequest
import com.example.walkway.model.entitiy.login.LoginResponse
import com.example.walkway.model.search.SearchRequest
import com.example.walkway.model.search.SearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface searchInterface{

    @Headers("Accept: application/json")

    @GET("/dev/api/v1/login")
    fun login(@Body user : LoginRequest) : Call<LoginResponse>
    @POST("/dev/api/v1/walkway/search/theme")
    fun search(@Body user: SearchRequest) : Call<SearchResponse>



}