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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.*

class ListaFragment : Fragment() {

    private lateinit var binding: FragmentListaBinding
    private lateinit var adapter: MonitorAdapter
    private var monitores = mutableListOf<Monitor>()
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

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
                .addOnFailureListener { exception ->
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




/*
            //METER LAS IMAGENES DE LA NUEVA FORMA EXPLICADA
            Monitor("KOORUI 24″ Full HD", "99,99€", false, R.drawable.monitor1),
            Monitor("Philips 24E1N1100A", "74,90€", false, R.drawable.monitor2),
            Monitor("Acer EK220Q H3", "67,99€", true, R.drawable.monitor3),
            Monitor("LG 22MR410-B", "76,99€", false, R.drawable.monitor4),
            Monitor("MSI Pro MP243X", "83,00€", true, R.drawable.monitor5),
            Monitor("Lenovo D24-45", "78,90€", true, R.drawable.monitor6),
            Monitor("SAMSUNG LS24C310EAUXEN", "94,99€", false, R.drawable.monitor7)

* */