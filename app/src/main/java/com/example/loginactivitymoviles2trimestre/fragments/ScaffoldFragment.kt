package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentScaffoldBinding
import com.google.firebase.auth.FirebaseAuth

class ScaffoldFragment : Fragment()
{
    private lateinit var binding: FragmentScaffoldBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentScaffoldBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser?.email
        val header = binding.navigationView.getHeaderView(0)
        val textView: TextView= header.findViewById(R.id.textViewEmail)

        if (user != null){
            textView.text = user
        }else{
            textView.text = getString(R.string.usuario)
        }

        /* TOOLBAR ------------------------------------------------------------*/
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        // Manejar la selección del item1
                        true
                    }

                    R.id.action_settings -> {
                        // Manejar la selección del item2
                        true
                    }

                    R.id.cerrarSesion -> {
                        logOut()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /* DRAWERLAYOUT ----------------------------------------------------*/
        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_scaffold) as NavHostFragment
        val navController = navHostFragment.navController
        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {

                item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.listaFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_contactos -> {
                    navController.navigate(R.id.contactoFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_fav -> {
                    navController.navigate(R.id.favoritosFragment)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.salir -> {
                    logOut()
                    true
                }

                else -> false
            }
        }

        /* BOTTOM NAVIGATION MENU ------------------------------------------------------------*/

        //binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bnm_listaMonitores -> {
                    navController.navigate(R.id.tabsfragment)
                    true
                }
                R.id.bnm_home-> {
                    // Handle Dashboard navigation
                    navController.navigate(R.id.contactoFragment)
                    true
                }
                R.id.bnm_contactoFragment -> {
                    // Handle Home navigation
                    navController.navigate(R.id.contactoFragment)
                    true
                }

                else -> false
            }
        }
    }


    private fun logOut(){

        // Después de cerrar sesión, redirigir al LoginActivity
        val firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance()
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_Scaffold_to_Login)
    }

}