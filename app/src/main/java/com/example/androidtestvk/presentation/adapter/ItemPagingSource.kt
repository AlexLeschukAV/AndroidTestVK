package com.example.androidtestvk.presentation.adapter

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.androidtestvk.domain.api.ApiService
import com.example.androidtestvk.domain.model.Product
import okio.IOException

class ItemPagingSource(
    private val apiService: ApiService,
    private val onError: (Throwable) -> Unit,
) : PagingSource<Int, Product>() {
    private val limit = 20
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val position = params.key ?: 0
        return try {
            val response = apiService.getProducts(position, limit)
            val dataList = response.body()?.products ?: emptyList()
            LoadResult.Page(
                data = dataList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (dataList.isEmpty()) null else position + limit
            )
        } catch (e: IOException) {
            onError(e)
            LoadResult.Error(e)
        } catch (e: Exception) {
            onError(e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}