package com.example.walkway.model

import com.example.walkway.model.search.SearchRequest
import com.example.walkway.model.search.SearchResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface searchInterface{

    @Headers("Accept: application/json")
    @POST("/dev/api/v1/walkway/search/theme")
    fun search(@Body user: SearchRequest) : Call<SearchResponse>


}