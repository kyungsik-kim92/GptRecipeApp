package com.example.presentation.ui.shoppinglist

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.presentation.databinding.FragmentShoppingListBinding
import com.example.presentation.ui.adapter.ShoppingListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShoppingListFragment : Fragment() {
    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShoppingListViewModel by viewModels()

    private lateinit var shoppingListAdapter: ShoppingListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListAdapter = ShoppingListAdapter(
            onItemChecked = { item, isChecked ->
                viewModel.onItemCheckedChanged(item.id, isChecked)
            }
        )
        setupRecyclerView()
        observeUiState()
    }

    private fun setupRecyclerView() {
        binding.rvShoppingList.apply {
            adapter = shoppingListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ShoppingListUiState.Idle -> {
                            binding.progressBar.visibility = View.GONE
                        }

                        is ShoppingListUiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvShoppingList.visibility = View.GONE
                        }

                        is ShoppingListUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvShoppingList.visibility = View.VISIBLE

                            shoppingListAdapter.submitList(state.items)

                            binding.tvTotalCount.text = "전체: ${state.totalCount}개"
                            binding.tvCheckedCount.text = "완료: ${state.checkedCount}개"
                        }

                        is ShoppingListUiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                state.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            viewModel.clearError()
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