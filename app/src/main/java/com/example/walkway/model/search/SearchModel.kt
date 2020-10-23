package com.example.walkway.model.search

import android.app.Activity
import android.util.Log
import com.example.walkway.RetrofitGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchModel{
    constructor()

    constructor(con: Activity) {

    }

//    class Search(theme_entire: Boolean, theme_night: Boolean, theme_morning: Boolean, theme_environment: Boolean, theme_food: Boolean, theme_animal: Boolean, theme_date: Boolean, function: (Int, SearchResponse?) -> Unit) {
//x
//    }

    fun Search(
        theme_entire: Boolean,
        theme_night: Boolean,
        theme_morning: Boolean,
        theme_environment: Boolean,
        theme_food: Boolean,
        theme_animal: Boolean,
        theme_date: Boolean,
        onResult: (isSuccess: Int, data: SearchResponse?) -> Unit
    ) {

        val SearchRequest = SearchRequest(
            theme_entire,
            theme_night,
            theme_morning,
            theme_environment,
            theme_food,
            theme_animal,
            theme_date
        )
        val call = RetrofitGenerator.search().search(SearchRequest)
//        val call = RetrofitGenerator.login().login(loginRequest)
        Log.d("getToken", SearchRequest.toString())



        call.enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {

                if (response.body() != null) {
                    Log.d("getToken", response.body().toString())

                    onResult(response.code(), response.body())
                } else {

                    onResult(response.code(), null)
                }
            }
        })


    }

}