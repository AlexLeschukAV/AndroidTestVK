package com.example.androidtestvk.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.androidtestvk.domain.api.ApiService
import com.example.androidtestvk.presentation.adapter.ItemPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiService: ApiService,
) : ViewModel() {
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    val flow = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ItemPagingSource(apiService){error -> handleError(error)} }
    ).flow.cachedIn(viewModelScope)

    private fun handleError(error: Throwable) {
        _error.postValue(error.localizedMessage)
    }
}