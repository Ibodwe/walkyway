package com.example.walkway.model

import com.example.walkway.model.entitiy.LoginRequest
import com.example.walkway.model.entitiy.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface SignupModelInterface{

    @Headers("Accept: application/json")
    @POST("/api/v1/login")
    fun login(@Body user : LoginRequest) : Call<LoginResponse>


}