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
            false,
            true,
            true,
            theme_environment,
            theme_food,
            theme_animal,
            theme_date
        )
        val call = RetrofitGenerator.search().search(SearchRequest)

        Log.d("getToken", SearchRequest.toString())

        call.enqueue(object : Callback<SearchResponse> {

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                Log.d("getToken44", call.toString())
                Log.d("getToken44", t.message.toString())
            }

            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                Log.d("getToken22", "222222")
                if (response.body() != null) {
                    Log.d("getToken33", response.body().toString())

                    onResult(response.code(), response.body())
                } else {

                    onResult(response.code(), null)
                }
            }
        })


    }

}