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
    // monitores obtenidos
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

        setupRecyclerView()

        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                obtenerMonitores() // Recargar los datos
                binding.swipeRefreshLayout.isRefreshing = false // Detener la animación de carga
            }, 1000) // Esperar 1 segundo antes de actualizar
        }

        // Cargamos los monitores
        obtenerMonitores()
    }

    // asegurarnos de que la lista siempre esté actualizada
    override fun onResume() {
        super.onResume()
        obtenerMonitores()
    }

    private fun obtenerMonitores() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerViewMonitorLista.visibility = View.GONE

        CoroutineScope(Dispatchers.Main).launch {
            delay(700) // Simulamos una espera de 1 segundo

            db.collection("monitor")
                .get()
                .addOnSuccessListener { documents ->
                    monitores.clear() // Limpiamos la lista antes de agregar los nuevos datos

                    for (document in documents) {
                        val monitor = Monitor(
                            document.id.toInt(),
                            document.get("nombre") as String,
                            document.get("precio") as String,
                            document.get("favorito") as Boolean,
                            document.get("url") as String
                        )
                        monitores.add(monitor) // Agregamos el monitor a la lista
                    }

                    adapter.updateList(monitores)

                    binding.progressBar.visibility = View.GONE
                    binding.recyclerViewMonitorLista.visibility = View.VISIBLE
                }
                .addOnFailureListener { _ ->
                    // Si falla, ocultamos el ProgressBar y mensaje de error
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
                }
        }
    }



    private fun setupRecyclerView() {
        adapter = MonitorAdapter(mutableListOf())
        binding.recyclerViewMonitorLista.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMonitorLista.adapter = adapter // Asignamos el adaptador al RecyclerView
    }
}
