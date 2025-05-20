package com.example.gptrecipeapp.ui.recrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gptrecipeapp.ApiService
import com.example.gptrecipeapp.RepositoryImpl
import com.example.gptrecipeapp.UniteUiModel
import com.example.gptrecipeapp.databinding.FragmentRecRecipeBinding
import com.example.gptrecipeapp.ui.adapter.SearchKeywordAdapter
import kotlinx.coroutines.launch

class RecRecipeFragment : Fragment() {
    private var _binding: FragmentRecRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecRecipeViewModel

    private val searchKeywordAdapter = SearchKeywordAdapter { searchKeyword ->
        viewModel.setSearchKeyword(searchKeyword)
        viewModel.getRecipeByIngredients()
    }

    private val args: RecRecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiService.create()
        val repository = RepositoryImpl(apiService)
        viewModel = RecRecipeViewModel(repository)
        viewModel.setSearchKeywordList(args.recIngredientsUiModel.searchKeywordList)
        viewModel.setIngredientsList(args.recIngredientsUiModel.ingredientsList)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvSearchKeywordList.adapter = searchKeywordAdapter
        addObserver()
    }

    private fun addObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiModel.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    searchKeywordAdapter.submitList(uiState.searchKeywordList)
                    if (uiState.isFetched) {
                        val uniteUiModel = UniteUiModel(
                            searchKeyword = uiState.searchKeyword,
                            ingredientsList = uiState.ingredientsList,
                            recipeList = uiState.recipeList
                        )
                        val action =
                            RecRecipeFragmentDirections.actionNavigationRecRecipeToNavigationRecipe(
                                uniteUiModel
                            )
                        findNavController().navigate(action)
                    }
                    uiState.isFetched = false
                }
            }
        }
    }
}