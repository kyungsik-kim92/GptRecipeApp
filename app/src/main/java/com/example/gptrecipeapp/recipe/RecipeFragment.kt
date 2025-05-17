package com.example.gptrecipeapp.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gptrecipeapp.ApiService
import com.example.gptrecipeapp.RepositoryImpl
import com.example.gptrecipeapp.databinding.FragmentRecipeBinding
import com.example.gptrecipeapp.ui.adapter.IngredientsAdapter
import com.example.gptrecipeapp.ui.adapter.RecipeAdapter
import kotlinx.coroutines.launch

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: RecipeViewModel

    private var ingredientsAdapter = IngredientsAdapter()
    private var recipeAdapter = RecipeAdapter()

    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiService.create()
        val repository = RepositoryImpl(apiService)
        viewModel = RecipeViewModel(repository)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvIngredientsList.adapter = ingredientsAdapter
        binding.rvRecipeList.adapter = recipeAdapter
        addObserver()
        binding.isIngredients = true
        binding.isRecipe = false

        args.searchIngredientsUiModel.searchKeyword.let { searchKeyword ->
            viewModel.setSearchKeyword(searchKeyword)
        }

        args.searchIngredientsUiModel.ingredientsList.let { ingredientsList ->
            viewModel.setIngredientsList(ingredientsList)
        }

        args.searchIngredientsUiModel.recipeList.let { recipeList ->
            viewModel.setRecipeList(recipeList)
        }

    }

    private fun addObserver() {
        lifecycleScope.launch {
            viewModel.uiModel.collect {
                binding.tvRecipeTitle.text = it.searchKeyword
                ingredientsAdapter.submitList(it.ingredientsList)
                recipeAdapter.submitList(it.recipeList)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}