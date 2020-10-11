package com.example.walkway.model

import com.example.walkway.model.entitiy.login.LoginRequest
import com.example.walkway.model.entitiy.login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface searchInterface{

    @Headers("Accept: application/json")
    @POST("/dev/api/v1/login")
    fun login(@Body user : LoginRequest) : Call<LoginResponse>


}