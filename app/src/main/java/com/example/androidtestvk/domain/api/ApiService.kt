package com.example.androidtestvk.domain.api

import com.example.androidtestvk.domain.model.ObjectProduct
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProducts(@Query("skip") page: Int, @Query("limit") limit: Int): Response<ObjectProduct>
}