package com.example.presentation.ui.recingredients

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
import com.example.presentation.databinding.FragmentRecIngredientsBinding
import com.example.presentation.ui.adapter.RecIngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecIngredientsFragment : Fragment() {

    private var _binding: FragmentRecIngredientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecIngredientsViewModel by viewModels()

    private val meatAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("meat", ingredientId)
    }
    private val seafoodAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("seafood", ingredientId)
    }
    private val vegetableAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("vegetable", ingredientId)
    }
    private val fruitAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("fruit", ingredientId)
    }
    private val processedAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("processed", ingredientId)
    }
    private val etcAdapter = RecIngredientsAdapter { ingredientId ->
        viewModel.toggleIngredientSelection("etc", ingredientId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecIngredientsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton()
        setupRecyclerViews()
        setupClickListeners()
        observeUiState()
        observeEvents()
        observeCategoryLists()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerViews() {
        binding.rvMeatList.adapter = meatAdapter
        binding.rvSeafoodList.adapter = seafoodAdapter
        binding.rvVegetableList.adapter = vegetableAdapter
        binding.rvFruitList.adapter = fruitAdapter
        binding.rvProcessedList.adapter = processedAdapter
        binding.rvEtcList.adapter = etcAdapter
    }

    private fun setupClickListeners() {
        binding.btnSearch.setOnClickListener {
            viewModel.getRecRecipes()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is RecIngredientsUiState.Idle -> {}
                        is RecIngredientsUiState.Loading -> showLoading()
                        is RecIngredientsUiState.Success -> {}
                        is RecIngredientsUiState.Error -> {}
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
                        is RecIngredientsUiEvent.RouteToRecRecipe -> {
                            routeToRecRecipe(event.recIngredientsUiState)
                        }

                        is RecIngredientsUiEvent.ShowError -> {}
                        is RecIngredientsUiEvent.ShowSuccess -> {}
                    }
                }
            }
        }
    }

    private fun observeCategoryLists() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.meatList.collect { list ->
                        meatAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.seafoodList.collect { list ->
                        seafoodAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.vegetableList.collect { list ->
                        vegetableAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.fruitList.collect { list ->
                        fruitAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.processedList.collect { list ->
                        processedAdapter.submitList(list)
                    }
                }
                launch {
                    viewModel.etcList.collect { list ->
                        etcAdapter.submitList(list)
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.isVisible = true
        binding.btnSearch.isEnabled = false
    }

    private fun routeToRecRecipe(recIngredientsUiState: RecIngredientsUiState.Success) {
        val action = RecIngredientsFragmentDirections
            .actionNavigationRecIngredientsToRecRecipeFragment(recIngredientsUiState)
        findNavController().navigate(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}