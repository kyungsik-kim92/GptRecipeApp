package com.example.gptrecipeapp.ui.wellbeingrecipe

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
import com.example.gptrecipeapp.databinding.FragmentWellbeingRecipeBinding
import com.example.gptrecipeapp.ui.adapter.WellbeingRecipeAdapter
import kotlinx.coroutines.launch

class WellbeingRecipeFragment : Fragment() {
    private var _binding: FragmentWellbeingRecipeBinding? = null
    private val binding get() = _binding!!
    private val wellbeingRecipeAdapter = WellbeingRecipeAdapter()

    private val viewModel by viewModels<WellbeingRecipeViewModel>()

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
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.rvRecipeList.adapter = wellbeingRecipeAdapter
        addObserver()

    }

    private fun addObserver() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiModel.collect {
                    wellbeingRecipeAdapter.submitList(it.wellBeingRecipeList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}