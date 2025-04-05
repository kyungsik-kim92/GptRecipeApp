package com.example.gptrecipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gptrecipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_container)?.findNavController()
        navController?.let {
            binding.navView.setupWithNavController(it)
        }
    }
}
