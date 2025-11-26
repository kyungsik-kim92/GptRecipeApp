package com.example.presentation.ui.recrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentRecRecipeBinding
import com.example.presentation.model.SearchHistoryModel
import com.example.presentation.model.UniteUiState
import com.example.presentation.ui.adapter.SearchKeywordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecRecipeFragment : Fragment() {
    private var _binding: FragmentRecRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecRecipeViewModel by viewModels()

    private val searchKeywordAdapter = SearchKeywordAdapter(
        onItemClick = { searchKeyword ->
            viewModel.setSearchKeyword(searchKeyword)
            viewModel.getIngredients()
        },
        onDeleteClick = {}
    )

    private val args: RecRecipeFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecRecipeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton()
        setupRecyclerView()
        setupData()
        observeUiState()
        observeEvents()
    }

    private fun setupRecyclerView() {
        binding.rvSearchKeywordList.adapter = searchKeywordAdapter
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupData() {
        viewModel.setupData(args.recIngredientsUiModel)
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.uiState = state
                    when (state) {
                        is RecRecipeUiState.Idle -> {
                            updateUI(state.searchKeywordList)
                        }

                        is RecRecipeUiState.Loading -> {}

                        is RecRecipeUiState.Success -> {
                            updateUI(state.searchKeywordList)
                        }

                        is RecRecipeUiState.Error -> {
                            updateUI(state.searchKeywordList)
                        }
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
                        is RecRecipeUiEvent.RouteToRecipe -> {
                            routeToRecipe(event.uniteUiState)
                        }

                        is RecRecipeUiEvent.ShowError -> {}

                        is RecRecipeUiEvent.ShowSuccess -> {}
                    }
                }
            }
        }
    }

    private fun updateUI(searchKeywordList: List<String>) {
        val models = searchKeywordList.mapIndexed { index, keyword ->
            SearchHistoryModel(
                id = index.toLong(),
                keyword = keyword,
                searchedAt = System.currentTimeMillis()
            )
        }
        searchKeywordAdapter.submitList(models)
    }

    private fun routeToRecipe(uniteUiState: UniteUiState) {
        val action = RecRecipeFragmentDirections.actionNavigationRecRecipeToNavigationRecipe(
            uniteUiState
        )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}