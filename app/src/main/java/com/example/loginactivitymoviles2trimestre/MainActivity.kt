package com.example.loginactivitymoviles2trimestre

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.loginactivitymoviles2trimestre.databinding.ActivityMainBinding
import com.example.loginactivitymoviles2trimestre.fragments.LoginFragment
import com.example.loginactivitymoviles2trimestre.fragments.RegistroFragment

class MainActivity : AppCompatActivity(), LoginFragment.OnFragmentChangeListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cargarFragment(LoginFragment.getInstance())
    }

    private fun cargarFragment(fragment: Fragment)
    {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        //frameLayout es el FrameLayout de activity main
        fragmentTransaction.commit()
    }

    override fun onFragmentChangeUno() {
        cargarFragment(RegistroFragment())
    }

}