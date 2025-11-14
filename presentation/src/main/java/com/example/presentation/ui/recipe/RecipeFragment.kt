package com.example.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentRecipeBinding
import com.example.presentation.ui.adapter.IngredientsAdapter
import com.example.presentation.ui.adapter.RecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels()

    private var ingredientsAdapter = IngredientsAdapter(
        isClickable = false,
        onItemClick = {})
    private var recipeAdapter = RecipeAdapter()

    private val args: RecipeFragmentArgs by navArgs()
    private var currentTab = TabType.INGREDIENTS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
        setupBackButton()
        setupWellbeingButton()
        setupShoppingListButton()
        setupRecyclerView()
        setupTabButton()
        setupSubscribeButton()
        observeUiState()
        observeEvents()
    }

    private fun setupData() {
        args.uniteUiModel?.let { uniteUiModel ->
            viewModel.setupInitialData(
                searchKeyword = uniteUiModel.searchKeyword,
                ingredientsList = uniteUiModel.ingredientsList,
                recipeList = uniteUiModel.recipeList,
                wellbeingRecipeList = uniteUiModel.wellbeingRecipeList
            )
            viewModel.checkIfFavoriteByName(uniteUiModel.searchKeyword)
        }

        if (args.recipeId != 0L) {
            viewModel.findRecipeById(args.recipeId)
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupWellbeingButton() {
        binding.btnWellBeing.setOnClickListener {
            viewModel.routeToWellbeing()
        }
    }

    private fun setupShoppingListButton() {
        binding.btnShoppingList.setOnClickListener {
            val currentState = viewModel.uiState.value

            if (currentState.isSubscribe) {
                viewModel.generateShoppingListFromCurrentRecipe()
            } else {
                Toast.makeText(
                    requireContext(),
                    "먼저 레시피를 즐겨찾기에 추가해주세요!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvIngredientsList.adapter = ingredientsAdapter
        binding.rvRecipeList.adapter = recipeAdapter
    }

    private fun setupTabButton() {
        binding.btnIngredients.setOnClickListener {
            switchTab(TabType.INGREDIENTS)
        }

        binding.btnRecipe.setOnClickListener {
            switchTab(TabType.RECIPE)

            val currentState = viewModel.uiState.value
            if (currentState.recipeList.isEmpty()) {
                viewModel.getRecipe()
            }
        }

        switchTab(TabType.INGREDIENTS)
    }


    private fun switchTab(tabType: TabType) {
        currentTab = tabType
        binding.isIngredients = (tabType == TabType.INGREDIENTS)
        binding.isRecipe = (tabType == TabType.RECIPE)

    }

    private fun setupSubscribeButton() {
        binding.btnSubscribe.setOnClickListener {
            val currentState = viewModel.uiState.value

            if (currentState.isSubscribe) {
                viewModel.deleteRecipe()
            } else {
                viewModel.insertRecipe()
            }
        }
    }


    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.uiState = state
                    updateRecyclerView(state)
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is RecipeUiEvent.ShowSuccess -> {
                            showToast(event.message)
                        }

                        is RecipeUiEvent.ShowError -> {
                            showToast(event.message)
                        }

                        is RecipeUiEvent.RouteToWellbeing -> {
                            routeToWellbeing(event.recipeUiState)
                        }
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(state: RecipeUiState) {
        if (state.ingredientsList.isNotEmpty()) {
            ingredientsAdapter.submitList(state.ingredientsList)
        }

        if (state.recipeList.isNotEmpty()) {
            recipeAdapter.submitList(state.recipeList)
        }
    }

    private fun routeToWellbeing(recipeUiState: RecipeUiState) {
        val action =
            RecipeFragmentDirections.actionNavigationRecipeToWellbeingRecipeFragment(
                recipeUiState
            )
        findNavController().navigate(action)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    enum class TabType {
        INGREDIENTS, RECIPE
    }
}