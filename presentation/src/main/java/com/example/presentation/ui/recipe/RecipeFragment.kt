package com.example.presentation.ui.recipe

import android.os.Bundle
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
import com.example.presentation.model.RecipeUiModel
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

//            viewModel.checkIfFavoriteByName(uniteUiModel.searchKeyword)
//        }
//
//        if (args.recipeId != 0L) {
//            viewModel.findRecipe(args.recipeId)
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

//            btnRecipe.setOnClickListener {
//                with(binding) {
//                    isIngredients = false
//                    isRecipe = true
//                    if (viewModel.uiState.value.recipeList.isEmpty()) {
//                        viewModel.getRecipe()
//                    }
//                }
//            }
            btnWellBeing.setOnClickListener {
                routeWellbeing()
            }
//            with(btnSubscribe) {
//                setOnClickListener {
//                    val flag = !(viewModel.uiState.value.isSubscribe)
//                    viewModel.setSubscribe(flag)
//                    if (flag) {
//                        viewModel.insertRecipe()
//                        Toast.makeText(this.context, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show()
//                    } else {
//                        viewModel.deleteRecipe()
//                        Toast.makeText(this.context, "즐겨찾기에 제거되었습니다", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
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
        val uiModel = viewModel.uiState.value
        val recipeUiModel = RecipeUiModel(
            id = uiModel.id,
            searchKeyword = uiModel.searchKeyword,
            recipeList = uiModel.recipeList,
            ingredientsList = uiModel.ingredientsList,
            isLoading = uiModel.isLoading,
            isSubscribe = uiModel.isSubscribe,
            wellbeingRecipeModel = uiModel.wellbeingRecipeModel
        )
        val action =
            RecipeFragmentDirections.actionNavigationRecipeToWellbeingRecipeFragment(
                recipeUiModel
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}