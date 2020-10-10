package com.example.walkway.model.login

import android.app.Activity
import android.util.Log
import com.example.walkway.RetrofitGenerator
import com.example.walkway.model.entitiy.LoginRequest
import com.example.walkway.model.entitiy.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModel{

    constructor()

    constructor(con: Activity) {

    }

    fun Login(
        pw: String,
        email: String,
        onResult: (isSuccess: Int, data: LoginResponse?) -> Unit
    ) {

        val loginRequest = LoginRequest(pw, email)
        val call = RetrofitGenerator.login().login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                if(response.body()!=null){
                    Log.d("getToken",response.body().toString())

                    onResult(response.code(),response.body())
                }else{

                    onResult(response.code(),null)
                }
            }
        })

    }
}