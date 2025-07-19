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
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeUiState()
        observeEvent()
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteList.adapter = favoriteAdapter
    }


    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.uiState = state
                    when (state) {
                        is FavoriteUiState.Success -> {
                            favoriteAdapter.submitList(state.favoriteList)
                        }
                        else -> {
                            favoriteAdapter.submitList(emptyList())
                        }
                    }
                }
            }
        }
    }

    private fun routeToRecipe(favoriteModel: FavoriteModel) {
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

    private fun observeEvent() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is FavoriteUiEvent.ShowSuccess -> {}

                        is FavoriteUiEvent.ShowError -> {}

                        is FavoriteUiEvent.RouteToRecipe -> {
                            routeToRecipe(event.favoriteModel)
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}