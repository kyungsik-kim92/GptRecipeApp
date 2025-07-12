package com.example.presentation.ui.wellbeingrecipe

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
import androidx.navigation.fragment.navArgs
import com.example.presentation.databinding.FragmentWellbeingRecipeBinding
import com.example.presentation.model.WellbeingRecipeModel
import com.example.presentation.ui.adapter.WellbeingRecipeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WellbeingRecipeFragment : Fragment() {
    private var _binding: FragmentWellbeingRecipeBinding? = null
    private val binding get() = _binding!!
    private val wellbeingRecipeAdapter = WellbeingRecipeAdapter()

    private val viewModel: WellbeingRecipeViewModel by viewModels()

    private val args by navArgs<WellbeingRecipeFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWellbeingRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setWellBeingRecipeList(args.recipeUiModel.wellbeingRecipeModel)
        setupBackButton()
        setupRecyclerView()
        observeUiState()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupRecyclerView() {
        binding.rvRecipeList.adapter = wellbeingRecipeAdapter
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        WellbeingRecipeUiState.Loading -> {}
                        is WellbeingRecipeUiState.Success -> showRecipeList(state.wellBeingRecipeList)
                        is WellbeingRecipeUiState.Error -> {}
                    }
                }
            }
        }
    }

    private fun showRecipeList(recipeList: List<WellbeingRecipeModel>) {
        binding.rvRecipeList.isVisible = true
        wellbeingRecipeAdapter.submitList(recipeList)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}