package com.example.presentation.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.R
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
        setupUI()
        addObserver()
        loadArgData()
    }

    private fun loadArgData() {
        args.uniteUiModel?.let { uniteUiModel ->
            viewModel.setSearchKeyword(uniteUiModel.searchKeyword)
            viewModel.setIngredientsList(uniteUiModel.ingredientsList)
            viewModel.setRecipeList(uniteUiModel.recipeList)
            viewModel.setWellBeingRecipeList(uniteUiModel.wellbeingRecipeList)

            viewModel.checkIfFavoriteByName(uniteUiModel.searchKeyword)
        }

        if (args.recipeId != 0L) {
            viewModel.findRecipeById(args.recipeId)
        }
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        with(binding) {
            rvIngredientsList.adapter = ingredientsAdapter
            rvRecipeList.adapter = recipeAdapter
            isIngredients = true
            isRecipe = false

            btnIngredients.setOnClickListener {
                with(binding) {
                    isIngredients = true
                    isRecipe = false
                }
            }

            btnRecipe.setOnClickListener {
                with(binding) {
                    isIngredients = false
                    isRecipe = true
                    if (viewModel.uiState.value.recipeList.isEmpty()) {
                        viewModel.getRecipe()
                    }
                }
            }
            btnWellBeing.setOnClickListener {
                routeWellbeing()
            }
            with(btnSubscribe) {
                setOnClickListener {
                    val currentState = viewModel.uiState.value
                    if (currentState.isSubscribe) {
                        viewModel.deleteRecipe()
                        Toast.makeText(requireContext(), "즐겨찾기에서 제거되었습니다", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.insertRecipe()
                        Toast.makeText(requireContext(), "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun addObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.tvRecipeTitle.text = it.searchKeyword
                    ingredientsAdapter.submitList(it.ingredientsList)
                    recipeAdapter.submitList(it.recipeList)
                    binding.progressBar.isVisible = it.isLoading

                    updateSubscribeButton(it.isSubscribe)
                    Log.d("RecipeFragment", "웰빙 레시피 수: ${it.wellbeingRecipeModel.size}")
                }
            }
        }
    }

    private fun updateSubscribeButton(isSubscribe: Boolean) {
        binding.btnSubscribe.background = if (isSubscribe) {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_clamp_subscribe_fill)
        } else {
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_clamp_subscribe_outline)
        }
    }

    private fun routeWellbeing() {
        val uiState = viewModel.uiState.value
        if (uiState.wellbeingRecipeModel.isEmpty()) {
            Toast.makeText(requireContext(), "웰빙 레시피가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val recipeUiState = RecipeUiState(
            id = uiState.id,
            searchKeyword = uiState.searchKeyword,
            recipeList = uiState.recipeList,
            ingredientsList = uiState.ingredientsList,
            isLoading = uiState.isLoading,
            isSubscribe = uiState.isSubscribe,
            wellbeingRecipeModel = uiState.wellbeingRecipeModel
        )
        val action =
            RecipeFragmentDirections.actionNavigationRecipeToWellbeingRecipeFragment(
                recipeUiState
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}