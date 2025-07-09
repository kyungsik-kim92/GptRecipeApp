package com.example.presentation.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.presentation.databinding.FragmentFavoriteBinding
import com.example.presentation.model.FavoriteModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()

    private val favoriteAdapter = FavoriteAdapter { favoriteModel ->
        viewModel.onFavoriteItemClick(favoriteModel)
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
        observeUiState()
        observeEvents()
    }


    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is FavoriteUiState.Loading -> {
                            showLoading()
                        }

                        is FavoriteUiState.Success -> {
                            showSuccessState(state)
                        }

                        is FavoriteUiState.Error -> {
                            showErrorState(state)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToRecipe(favoriteModel: FavoriteModel) {
        val uniteUiState = UniteUiState(
            searchKeyword = favoriteModel.searchKeyword,
            ingredientsList = favoriteModel.ingredientsList,
            recipeList = favoriteModel.recipeList,
            wellbeingRecipeList = favoriteModel.wellbeingRecipeList
        )
        val action = FavoriteFragmentDirections.actionNavigationFavoriteToNavigationRecipe(
            recipeId = favoriteModel.id,
            uniteUiModel = uniteUiState
        )
        findNavController().navigate(action)
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is FavoriteUiEvent.ShowSuccess -> {
                            showSuccessMessage(event.message)
                        }

                        is FavoriteUiEvent.ShowError -> {
                            showErrorMessage(event.message)
                        }

                        is FavoriteUiEvent.NavigateToRecipe -> {
                            navigateToRecipe(event.favoriteModel)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.rvFavoriteList.isVisible = false
        binding.layoutEmpty.isVisible = false
    }

    private fun showSuccessState(state: FavoriteUiState.Success) {
        binding.rvFavoriteList.isVisible = state.favoriteList.isNotEmpty()
        binding.layoutEmpty.isVisible = state.favoriteList.isEmpty()

        favoriteAdapter.submitList(state.favoriteList)
    }

    private fun showErrorState(state: FavoriteUiState.Error) {
        binding.rvFavoriteList.isVisible = false
        binding.layoutEmpty.isVisible = true
    }

    private fun showSuccessMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}