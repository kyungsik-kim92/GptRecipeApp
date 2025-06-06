package com.example.gptrecipeapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.gptrecipeapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBottomNavigation()
    }

    private fun setupBottomNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.navView.setupWithNavController(navController)

        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_search -> {
                    if (navController.currentDestination?.id != R.id.navigation_search) {
                        navController.navigate(R.id.navigation_search)
                    }
                    true
                }

                R.id.navigation_rec -> {
                    if (navController.currentDestination?.id != R.id.navigation_rec) {
                        navController.navigate(R.id.navigation_rec)
                    }
                    true
                }

                R.id.navigation_favorite -> {
                    navController.popBackStack(R.id.navigation_favorite, false)
                    if (navController.currentDestination?.id != R.id.navigation_favorite) {
                        navController.navigate(R.id.navigation_favorite)
                    }
                    true
                }


                else -> false
            }
        }
    }

}
