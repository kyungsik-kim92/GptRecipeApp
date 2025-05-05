package com.example.gptrecipeapp.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val searchKeyword = binding.etRecipe.text.toString()
            if (searchKeyword.isBlank()) {
                return@setOnClickListener
            }
            viewModel.getIngredientsByRecipe(searchKeyword)
            addObserver()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}