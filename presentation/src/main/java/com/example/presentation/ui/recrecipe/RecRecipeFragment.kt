package com.example.presentation.ui.recrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentRecRecipeBinding
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.adapter.SearchKeywordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecRecipeFragment : Fragment() {
    private var _binding: FragmentRecRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecRecipeViewModel by viewModels()

    private val searchKeywordAdapter = SearchKeywordAdapter { searchKeyword ->
        viewModel.setSearchKeyword(searchKeyword)
        viewModel.getIngredients()
    }

    private val args: RecRecipeFragmentArgs by navArgs()


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
        viewModel.setSearchKeywordList(args.recIngredientsUiModel.searchKeywordList)
        viewModel.setIngredientsList(args.recIngredientsUiModel.ingredientsList)

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
                        val uniteUiState = UniteUiState(
                            searchKeyword = uiState.searchKeyword,
                            ingredientsList = uiState.ingredientsList,
                            recipeList = uiState.recipeList,
                            wellbeingRecipeList = uiState.wellbeingRecipeList
                        )
                        val action =
                            RecRecipeFragmentDirections.actionNavigationRecRecipeToNavigationRecipe(
                                uniteUiState
                            )
                        findNavController().navigate(action)
                    }
                    uiState.isFetched = false
                }
            }
        }
    }
}