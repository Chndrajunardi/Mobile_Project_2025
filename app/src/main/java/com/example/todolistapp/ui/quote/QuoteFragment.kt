package com.example.todolistapp.ui.quote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.todolistapp.databinding.FragmentQuoteBinding
import com.google.android.material.snackbar.Snackbar

class QuoteFragment : Fragment() {

    private var _binding: FragmentQuoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var quoteViewModel: QuoteViewModel
    private lateinit var quoteAdapter: QuoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        quoteViewModel = ViewModelProvider(this)[QuoteViewModel::class.java]
        _binding = FragmentQuoteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSwipeRefresh()
        observeQuotes()
        observeError()
        observeLoading()

        return root
    }

    private fun setupRecyclerView() {
        quoteAdapter = QuoteAdapter()
        binding.recyclerViewQuotes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = quoteAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            quoteViewModel.refreshQuotes()
        }
    }

    private fun observeQuotes() {
        quoteViewModel.quotes.observe(viewLifecycleOwner) { quotes ->
            quoteAdapter.submitList(quotes)
        }
    }

    private fun observeError() {
        quoteViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun observeLoading() {
        quoteViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}