package com.example.androidtestvk.domain.model

import com.google.gson.annotations.SerializedName

data class ObjectProduct(
    @SerializedName("products") var products: ArrayList<Product> = arrayListOf(),
    @SerializedName("total") var total: Int,
    @SerializedName("skip") var skip: Int,
    @SerializedName("limit") var limit: Int

)
