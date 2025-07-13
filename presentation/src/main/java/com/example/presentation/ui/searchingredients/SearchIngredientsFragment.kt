package com.example.presentation.ui.searchingredients

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentSearchIngredientsBinding
import com.example.presentation.model.IngredientsModel
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
        setupBackButton()
        setupRecyclerView()
        observeUiState()
        observeEvents()
        setupData()

        binding.btnSearch.setOnClickListener {
            val customIngredient = binding.etIngredients.text.toString().trim()

            if (customIngredient.isNotEmpty()) {
                val currentIngredients = viewModel.uiState.value.ingredientsList
                val isDuplicate = currentIngredients.any {
                    it.ingredients.equals(customIngredient, ignoreCase = true)
                }
                if (!isDuplicate) {
                    val customIngredientModel = IngredientsModel(
                        id = "custom_${System.currentTimeMillis()}",
                        ingredients = customIngredient,
                        isSelected = true
                    )
                    val updatedIngredients = currentIngredients + customIngredientModel
                    viewModel.setIngredientsList(updatedIngredients)

                    binding.etIngredients.text.clear()
                    hideKeyboard()
                } else {
                    Toast.makeText(requireContext(), "이미 선택된 재료입니다", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val selectedIngredients =
                viewModel.uiState.value.ingredientsList.filter { it.isSelected }

            if (selectedIngredients.isEmpty()) {
                Toast.makeText(requireContext(), "재료를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.getRecipeByIngredients()
        }

    }

    private fun setupRecyclerView() {
        binding.rvIngredientsList.adapter = ingredientsAdapter
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupData() {
        viewModel.setSearchResult(args.searchUiState)
    }


    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etIngredients.windowToken, 0)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is SearchIngredientsUiState.Idle -> updateUI(
                            state.searchKeyword,
                            state.ingredientsList
                        )

                        is SearchIngredientsUiState.Loading -> {
                            showLoading()
                            updateUI(
                                state.searchKeyword,
                                state.ingredientsList
                            )

                        }

                        is SearchIngredientsUiState.Success -> updateUI(
                            state.searchKeyword,
                            state.ingredientsList
                        )

                        is SearchIngredientsUiState.Error -> updateUI(
                            state.searchKeyword,
                            state.ingredientsList
                        )
                    }
                }
            }
        }
    }

    private fun observeEvents() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collect { event ->
                    when (event) {
                        is SearchIngredientsUiEvent.RouteToRecipe -> {
                            routeToRecipe(event.uniteUiState)
                        }

                        is SearchIngredientsUiEvent.ShowError -> {}

                        is SearchIngredientsUiEvent.ShowSuccess -> {}
                    }
                }
            }
        }
    }

    private fun updateUI(searchKeyword: String, ingredientsList: List<IngredientsModel>) {
        binding.tvRecipeTitle.text = searchKeyword
        ingredientsAdapter.submitList(ingredientsList)
    }

    private fun routeToRecipe(uiState: UniteUiState) {
        val selectedIngredients = uiState.ingredientsList.filter { it.isSelected }

        val uniteUiState = UniteUiState(
            searchKeyword = uiState.searchKeyword,
            ingredientsList = selectedIngredients,
            recipeList = uiState.recipeList,
            wellbeingRecipeList = uiState.wellbeingRecipeList
        )
        val action = SearchIngredientsFragmentDirections
            .actionNavigationSearchIngredientsToRecipeFragment(uniteUiState)
        findNavController().navigate(action)
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
