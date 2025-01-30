package com.example.designpatterntest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.designpatterntest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        setupNavController()
        setupBottomAndDrawerNav()
        setupAppBarConfiguration()
        handleBackButtonForNonTopLevel()

    }

    private fun setupAppBarConfiguration() {
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.favoriteFragment), // Add your top-level destinations here
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setupBottomAndDrawerNav() {
        binding.bottomNav.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.drawerIcon.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun handleBackButtonForNonTopLevel() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.favoriteFragment -> {
                    // Show custom Drawer icon for top-level destinations
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
                    binding.toolbar.drawerIcon.visibility = View.VISIBLE
                }

                else -> {
                    // Show Back button for non-top-level destinations
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    binding.toolbar.drawerIcon.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
