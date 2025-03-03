package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.databinding.FragmentListaBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*

class ListaFragment : Fragment() {

    private lateinit var binding: FragmentListaBinding
    private lateinit var adapter: MonitorAdapter
    private var monitores = mutableListOf<Monitor>()
    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewMonitorLista.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        // Inicializamos el RecyclerView con una lista vacía
        setupRecyclerView()
        //swipe de recarga
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Simular una recarga de 2 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                // Recargar los datos
                obtenerMonitores()
                // Detener la animación de carga
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1000) // 1 segundos
        }

        // Cargamos los datos de Firestore
        obtenerMonitores()
    }

    // Cuando el Fragment se reanuda, actualizamos la lista de monitores
    override fun onResume() {
        super.onResume()
        obtenerMonitores()
    }

    private fun obtenerMonitores() {
        // Mostrar ProgressBar y ocultar RecyclerView al iniciar la carga
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewMonitorLista.visibility = View.GONE
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000) // Esperar 1.5 segundos
            db.collection("monitor")
                .get()
                .addOnSuccessListener { documents ->
                    monitores.clear()
                    for (document in documents) {
                        val monitor = Monitor(
                            document.id.toInt(),
                            document.get("nombre") as String,
                            document.get("precio") as String,
                            document.get("favorito") as Boolean,
                            document.get("url") as String,
                        )
                        monitores.add(monitor)
                    }

                    // Actualizamos los datos en el RecyclerView
                    adapter.updateList(monitores)

                    binding.progressBar.visibility = View.GONE
                    // Mostramos el RecyclerView
                    binding.recyclerViewMonitorLista.visibility = View.VISIBLE
                }
                .addOnFailureListener { _ ->
                    binding.progressBar.visibility = View.GONE //por si falla que no se quede alli
                    Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun setupRecyclerView() {
        adapter = MonitorAdapter(mutableListOf()) // Inicialmente vacío
        binding.recyclerViewMonitorLista.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMonitorLista.adapter = adapter
    }

}