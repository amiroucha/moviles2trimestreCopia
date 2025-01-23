package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
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

                            R.id.nav_dashboard -> {

                                true
                            }

                            R.id.nav_notifications -> {
                                true
                            }

                            else -> false
                    }
        }

        /* BOTTOM NAVIGATION MENU */

        binding.bottomNavigation.setOnItemSelectedListener {
            item ->
                        when (item.itemId) {
                            R.id.bnm_home -> {
                                // Handle Home navigation
                                true
                            }
                            R.id.bnm_dashboard -> {
                                // Handle Dashboard navigation
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