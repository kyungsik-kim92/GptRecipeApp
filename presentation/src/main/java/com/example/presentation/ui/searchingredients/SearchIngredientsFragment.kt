package com.example.presentation.ui.searchingredients

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
import com.example.presentation.databinding.FragmentSearchIngredientsBinding
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.adapter.IngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchIngredientsFragment : Fragment() {
    private var _binding: FragmentSearchIngredientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchIngredientsViewModel by viewModels()
    private val ingredientsAdapter = IngredientsAdapter(
        isClickable = true,
        onItemClick = { ingredientsId ->
            viewModel.toggleIngredientSelection(ingredientsId)
        })
    private val args: SearchIngredientsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setSearchKeyword(args.searchUiModel.searchKeyword)
        if (viewModel.uiModel.value.ingredientsList.isEmpty()) {
            viewModel.setIngredientsList(args.searchUiModel.ingredientsList)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvIngredientsList.adapter = ingredientsAdapter
        addObserver()

        binding.btnSearch.setOnClickListener {
            val ingredientsList =
                viewModel.uiModel.value.ingredientsList.filter { it.isSelected }
            if (ingredientsList.isEmpty()) {
                return@setOnClickListener
            }
            viewModel.getRecipeByIngredients(ingredientsList)
        }
    }

    private fun addObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiModel.collect {
                    binding.progressBar.isVisible = it.isLoading

                    with(binding) {
                        tvRecipeTitle.text = it.searchKeyword
                        ingredientsAdapter.submitList(it.ingredientsList)
                    }
                    if (it.isFetched) {
                        routeToRecipe(it)
                    }
                    it.isFetched = false
                }
            }
        }
    }

    private fun routeToRecipe(uiState: UniteUiState) {
        val selectedIngredients = uiState.ingredientsList.filter { it.isSelected }

        val uniteUiState = UniteUiState(
            searchKeyword = uiState.searchKeyword,
            ingredientsList = selectedIngredients,
            recipeList = uiState.recipeList,
            wellbeingRecipeList = uiState.wellbeingRecipeList,
            isFetched = false
        )
        val action = SearchIngredientsFragmentDirections
            .actionNavigationSearchIngredientsToRecipeFragment(uniteUiState)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
