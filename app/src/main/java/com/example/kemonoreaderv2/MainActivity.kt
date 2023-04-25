package com.example.kemonoreaderv2

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.kemonoreaderv2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("asd","Main Activity")
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        val navHost = supportFragmentManager.findFragmentById(R.id.container_fragment) as
                NavHostFragment
        val navController = navHost.navController
//        binding.menuToolbar.setupWithNavController(navController)
//        binding.menuToolbar.visibility = View.INVISIBLE
//
    }

}