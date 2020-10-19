package com.example.walkway.model

import com.example.walkway.model.entitiy.login.LoginRequest
import com.example.walkway.model.entitiy.login.LoginResponse
import com.example.walkway.model.signup.SignupRequest
import com.example.walkway.model.signup.SignupResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface SignupModelInterface{

    @Headers("Accept: application/json")
    @POST("/dev/api/v1/login")
    fun login(@Body user : LoginRequest) : Call<LoginResponse>

    @Headers("Accept: application/json")
    @POST("/dev/api/v1/signup")
    fun signup(@Body user : SignupRequest) : Call<SignupResponse>


}