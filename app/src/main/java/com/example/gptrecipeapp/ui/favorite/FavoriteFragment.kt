package com.example.gptrecipeapp.ui.favorite

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
import com.example.gptrecipeapp.FavoriteModel
import com.example.gptrecipeapp.UniteUiModel
import com.example.gptrecipeapp.databinding.FragmentFavoriteBinding
import com.example.gptrecipeapp.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter = FavoriteAdapter { favoriteModel ->
        navigateToRecipe(favoriteModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvFavoriteList.adapter = favoriteAdapter
        addObserver()
    }


    private fun addObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiModel.collect {
                    binding.rvFavoriteList.isVisible = it.favoriteList.isNotEmpty()
                    binding.layoutEmpty.isVisible = it.favoriteList.isEmpty()

                    favoriteAdapter.submitList(it.favoriteList)
                }
            }
        }
    }

    private fun navigateToRecipe(favoriteModel: FavoriteModel) {
        val uniteUiModel = UniteUiModel(
            searchKeyword = favoriteModel.searchKeyword,
            ingredientsList =favoriteModel.ingredientsList,
            recipeList = favoriteModel.recipeList,
            wellbeingRecipeList = favoriteModel.wellbeingRecipeList
        )
        val action = FavoriteFragmentDirections.actionNavigationFavoriteToNavigationRecipe(
            recipeId = favoriteModel.id,
            uniteUiModel = uniteUiModel
        )
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}