package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentScaffoldBinding

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        /* TOOLBAR */
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object: MenuProvider {

                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater)
                {
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
                        else -> false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        /* DRAWERLAYOUT */

        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationView.setNavigationItemSelectedListener {

            item -> when(item.itemId)
                    {
                            R.id.nav_home -> {
                                true
                            }
                            R.id.nav_notifications -> {
                                true
                            }
                            R.id.nav_fav -> {
                                true
                            }

                            else -> false
                    }
        }

        /* BOTTOM NAVIGATION MENU */

        val navHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_scaffold) as NavHostFragment
        val navController = navHostFragment.navController
        //binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
                        when (item.itemId) {
                            R.id.contactoFragment -> {
                                // Handle Home navigation
                                navController.navigate(R.id.contactoFragment)
                                true
                            }
                            R.id.listaFragment -> {
                                // Handle Dashboard navigation
                                navController.navigate(R.id.listaFragment)
                                true
                            }
                            R.id.bnm_notifications -> {
                                // Handle Notifications navigation
                                true
                            }
                            else -> false
            }
        }





    }

}