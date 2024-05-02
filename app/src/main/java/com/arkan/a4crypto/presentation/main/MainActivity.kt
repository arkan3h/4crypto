package com.arkan.a4crypto.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.arkan.a4crypto.R
import com.arkan.a4crypto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        setupBottomNav()
        setOnclickAction()
    }

    private fun setOnclickAction() {
        binding.navBottom.fab.setOnClickListener {
            navigateToFavorite()
        }
    }

    private fun setupBottomNav() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navBottom.navView.setupWithNavController(navController)
    }

    fun navigateToFavorite() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navBottom.navView.selectedItemId = R.id.menu_tab_favorite
        navController.navigate(R.id.menu_tab_favorite)
    }
}
