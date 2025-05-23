package com.example.gptrecipeapp.ui.search

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gptrecipeapp.ApiService
import com.example.gptrecipeapp.RepositoryImpl
import com.example.gptrecipeapp.SearchUiModel
import com.example.gptrecipeapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiService.create()
        val repository = RepositoryImpl(apiService)
        viewModel = SearchViewModel(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun addObserver() {
        lifecycleScope.launch {
            viewModel.uiModel.collect {
                binding.progressBar.isVisible = it.isLoading
                if (it.isFetched) {
                    val searchUiModel = SearchUiModel(
                        searchKeyword = it.searchKeyword,
                        isFetched = it.isFetched,
                        isLoading = it.isLoading,
                        ingredientsList = it.ingredientsList
                    )
                    val action =
                        SearchFragmentDirections.actionNavigationSearchToNavigationSearchIngredients(
                            searchUiModel
                        )
                    findNavController().navigate(action)
                }
                it.isFetched = false

            }
        }
    }

    private fun search() {
        val searchKeyword = binding.etRecipe.text.toString()
        if (searchKeyword.isBlank()) {
            return
        }
        viewModel.getIngredientsByRecipe(searchKeyword)
        addObserver()
        hideKeyboard()
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