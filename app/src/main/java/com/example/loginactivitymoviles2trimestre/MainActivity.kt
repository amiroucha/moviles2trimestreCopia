package com.example.loginactivitymoviles2trimestre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.example.loginactivitymoviles2trimestre.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //Nuevo método para configurar el componente Navigation
    override fun onSupportNavigateUp(): Boolean
    {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
             as NavHostFragment
        //obtenemos el navigation
        val navController = navHostFragment.navController
        //funcionamiento del nav
        return navController.navigateUp() || super.onSupportNavigateUp()

    }


}