package com.example.walkway.model.signup

import android.content.Context
import android.util.Log
import com.example.walkway.RetrofitGenerator
import com.example.walkway.model.entitiy.login.LoginRequest
import com.example.walkway.model.entitiy.login.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupModel{

    constructor(context: Context)

    fun Signup(
        userEmail: String,
        userPassword: String,
        userNickName : String,
        onResult: (isSuccess: Int, data: SignupResponse?) -> Unit
    ) {

        val signupRequest = SignupRequest(userEmail, userPassword,userNickName)
        val call = RetrofitGenerator.login().signup(signupRequest)

        Log.d("getToken",signupRequest.toString())



        call.enqueue(object : Callback<SignupResponse> {
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<SignupResponse>,
                response: Response<SignupResponse>
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