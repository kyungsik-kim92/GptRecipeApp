package com.example.gptrecipeapp.ui.searchingredients

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
import com.example.gptrecipeapp.databinding.FragmentSearchIngredientsBinding
import com.example.gptrecipeapp.ui.adapter.IngredientsAdapter
import kotlinx.coroutines.launch

class SearchIngredientsFragment : Fragment() {
    private var _binding: FragmentSearchIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SearchIngredientsViewModel
    private val ingredientsAdapter = IngredientsAdapter(isClickable = true)
    private val args: SearchIngredientsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiService.create()
        val repository = RepositoryImpl(apiService)
        viewModel = SearchIngredientsViewModel(repository)
        viewModel.setSearchKeyword(args.searchUiModel.searchKeyword)
        viewModel.setIngredientsList(args.searchUiModel.ingredientsList)
    }

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
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.rvIngredientsList.adapter = ingredientsAdapter
        addObserver()

        binding.btnSearch.setOnClickListener {
            val ingredientsList =
                ArrayList(ingredientsAdapter.currentList.filter { it.isSelected.value })
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
                        // 레시피 검색어
                        with(tvRecipeTitle) {
                            text = it.searchKeyword
                        }
                        // 재료 어댑터
                        with(ingredientsAdapter) {
                            submitList(it.ingredientsList)
                        }
                    }
                    if (it.isFetched) {
                        val selectedIngredients =
                            ArrayList(it.ingredientsList.filter { ingredients ->
                                ingredients.isSelected.value
                            })

                        val uniteUiModel = UniteUiModel(
                            searchKeyword = it.searchKeyword,
                            ingredientsList = selectedIngredients,
                            recipeList = it.recipeList,
                            wellbeingRecipeList = it.wellbeingRecipeList
                        )

                        val action = SearchIngredientsFragmentDirections
                            .actionNavigationSearchIngredientsToRecipeFragment(uniteUiModel)
                        findNavController().navigate(action)
                    }
                    it.isFetched = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}