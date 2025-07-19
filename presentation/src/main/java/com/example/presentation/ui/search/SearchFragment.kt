package com.example.presentation.ui.search

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.R
import com.example.presentation.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by viewModels()

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
        setupClickListener()
        observeUiState()
        observeEvents()

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
                        is SearchUiState.Loading -> {}
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
                        is SearchUiEvent.ShowError -> {}
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}