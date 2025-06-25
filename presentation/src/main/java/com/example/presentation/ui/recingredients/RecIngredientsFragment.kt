package com.example.presentation.ui.recingredients

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
import com.example.presentation.databinding.FragmentRecIngredientsBinding
import com.example.presentation.ui.adapter.RecIngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RecIngredientsFragment : Fragment() {

    private var _binding: FragmentRecIngredientsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecIngredientsViewModel by viewModels()

    private val meatAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("seafood", ingredientId)
        }
    )
    private val seafoodAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("seafood", ingredientId)
        })
    private val vegetableAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("vegetable", ingredientId)
        }
    )
    private val fruitAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("fruit", ingredientId)
        }
    )
    private val processedAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("processed", ingredientId)
        }
    )
    private val etcAdapter = RecIngredientsAdapter(
        onItemClick = { ingredientId ->
            viewModel.toggleIngredientSelection("etc", ingredientId)
        }
    )

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
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        setupRecyclerViews()
        setupClickListeners()
        observeViewModelData()
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
            val selectedIngredients = viewModel.getSelectedIngredients()
            if (selectedIngredients.isEmpty()) {
                Toast.makeText(requireContext(), "재료를 선택해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.getRecRecipes(selectedIngredients)
        }
    }

    private fun observeViewModelData() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiModel.collect { uiState ->
                        binding.progressBar.isVisible = uiState.isLoading

                        if (uiState.isFetched) {
                            val recIngredientsUiState = RecIngredientsUiState(
                                isLoading = false,
                                isFetched = true,
                                searchKeywordList = ArrayList(uiState.searchKeywordList),
                                ingredientsList = ArrayList(uiState.ingredientsList)
                            )
                            val action =
                                RecIngredientsFragmentDirections.actionNavigationRecIngredientsToRecRecipeFragment(
                                    recIngredientsUiState
                                )
                            findNavController().navigate(action)
                        }
                        uiState.isFetched = false
                    }
                }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}