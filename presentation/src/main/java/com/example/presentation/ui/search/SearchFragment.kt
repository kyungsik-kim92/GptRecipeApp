package com.example.presentation.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.databinding.FragmentSearchBinding
import com.example.presentation.ui.adapter.SearchKeywordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

    private lateinit var searchKeywordAdapter: SearchKeywordAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupClickListener()
        setupSearchEditText()
        observeUiState()
        observeEvents()
        observeRecentSearches()

    }

    private fun setupRecyclerView() {
        searchKeywordAdapter = SearchKeywordAdapter(
            onItemClick = { keyword ->
                viewModel.onRecentSearchClick(keyword)
                hideRecentSearches()
            },
            onDeleteClick = { keyword ->
                viewModel.deleteSearchHistory(keyword)
            }
        )
        binding.rvRecentSearches.adapter = searchKeywordAdapter

        binding.rvRecentSearches.itemAnimator = null
        binding.rvRecentSearches.setHasFixedSize(true)
    }

    private fun setupSearchEditText() {
        binding.etRecipe.setOnFocusChangeListener { _, hasFocus ->
            updateRecentSearchesVisibility()
        }
    }


    private fun hideRecentSearches() {
        binding.rvRecentSearches.visibility = View.GONE
        binding.ivWoman.visibility = View.VISIBLE
        binding.tvHint.visibility = View.VISIBLE
    }

    private fun observeRecentSearches() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentSearches.collect { searches ->
                    searchKeywordAdapter.submitList(searches)
                }
            }
        }
    }

    private fun updateRecentSearchesVisibility() {
        val searches = viewModel.recentSearches.value
        val hasFocus = binding.etRecipe.hasFocus()

        if (hasFocus && searches.isNotEmpty()) {
            binding.rvRecentSearches.visibility = View.VISIBLE
            binding.ivWoman.visibility = View.GONE
            binding.tvHint.visibility = View.GONE
        } else if (hasFocus && searches.isEmpty()) {
            binding.rvRecentSearches.visibility = View.GONE
            binding.ivWoman.visibility = View.VISIBLE
            binding.tvHint.visibility = View.VISIBLE
        } else {
            hideRecentSearches()
        }
    }

    private fun setupClickListener() {
        binding.btnSearch.setOnClickListener {
            search()
        }
        binding.etRecipe.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                search()
                true
            } else {
                false
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.uiState = state
                    when (state) {
                        is SearchUiState.Idle -> {}
                        is SearchUiState.Loading -> {
                            hideRecentSearches()
                        }

                        is SearchUiState.Success -> {}
                        is SearchUiState.Error -> {}

                    }
                }
            }
        }
    }

    private fun search() {
        val searchKeyword = binding.etRecipe.text.toString().trim()
        if (searchKeyword.isBlank()) {
            return
        }
        viewModel.getIngredientsByRecipe(searchKeyword)
        hideKeyboard()
        hideRecentSearches()
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is SearchUiEvent.RouteToIngredients -> {
                            routeToIngredients(event.searchUiState)
                        }

                        is SearchUiEvent.ShowSuccess -> {}
                        is SearchUiEvent.ShowError -> {
                            Toast.makeText(requireContext(), event.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }
    }

    private fun routeToIngredients(searchUiState: SearchUiState.Success) {
        val action = SearchFragmentDirections.actionNavigationSearchToNavigationSearchIngredients(
            searchUiState
        )
        findNavController().navigate(action)
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etRecipe.windowToken, 0)
        binding.etRecipe.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}