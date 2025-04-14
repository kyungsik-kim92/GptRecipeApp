package com.example.gptrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gptrecipeapp.adapter.RecIngredientsAdapter
import com.example.gptrecipeapp.databinding.FragmentRecIngredientsBinding
import com.example.gptrecipeapp.model.IngredientsModel

class RecIngredientsFragment : Fragment() {

    private var _binding: FragmentRecIngredientsBinding? = null
    private val binding get() = _binding!!

    private lateinit var meatAdapter: RecIngredientsAdapter
    private lateinit var seafoodAdapter: RecIngredientsAdapter
    private lateinit var vegetableAdapter: RecIngredientsAdapter
    private lateinit var fruitAdapter: RecIngredientsAdapter
    private lateinit var processedAdapter: RecIngredientsAdapter
    private lateinit var etcAdapter: RecIngredientsAdapter

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
            requireActivity().onBackPressed()
        }

        with(binding.rvMeatList) {
            meatAdapter = RecIngredientsAdapter()
            adapter = meatAdapter
            meatAdapter.submitList(getMeatList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getMeatList(): ArrayList<IngredientsModel> {
        return arrayListOf<IngredientsModel>(
            IngredientsModel(ingredients = "닭고기", initialIsSelected = false),
            IngredientsModel(ingredients = "돼지고기", initialIsSelected = false),
            IngredientsModel(ingredients = "소고기", initialIsSelected = false),
            IngredientsModel(ingredients = "양고기", initialIsSelected = false),
            IngredientsModel(ingredients = "오리고기", initialIsSelected = false)
        )
    }
}