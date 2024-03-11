package com.example.androidtestvk.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.androidtestvk.R
import com.example.androidtestvk.databinding.FragmentMainBinding
import com.example.androidtestvk.presentation.adapter.LoadStateAdapter
import com.example.androidtestvk.presentation.adapter.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val adapter: ProductAdapter by lazy { ProductAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        binding.rwList.adapter = adapter

        val footer = LoadStateAdapter(adapter::retry)

        binding.rwList.adapter = adapter.withLoadStateFooter(footer = footer)

        lifecycleScope.launch {
            viewModel.flow.collectLatest(adapter::submitData)
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible =
                    loadStates.refresh is LoadState.Loading || loadStates.append is LoadState.Loading
                            || loadStates.prepend is LoadState.Loading
            }
        }
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            showDialog(errorMessage)
        }
        binding.tvError.setOnClickListener {
            binding.tvError.visibility = View.GONE
            adapter.refresh()
        }
    }

    private fun showDialog(e: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.error_load))
        builder.setMessage("$e   Пробовать еще раз?")
        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            adapter.refresh()
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
            binding.tvError.isVisible = true
        }
        builder.show()
    }
}