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
import kotlinx.coroutines.tasks.await

class ListaFragment : Fragment() {

    private lateinit var binding: FragmentListaBinding
    private lateinit var adapter: MonitorAdapter
    private var monitores = mutableListOf<Monitor>()
    val db = Firebase.firestore

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

        // Inicializamos el RecyclerView con una lista vacía
        setupRecyclerView()
        //swipe de recarga
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Simular una recarga de 2 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                // Recargar los datos
                setupRecyclerView()
                obtenerMonitores()
                // Detener la animación de carga
                binding.swipeRefreshLayout.isRefreshing = false
            }, 2000) // 2 segundos
        }

        // Cargamos los datos de Firestore
        obtenerMonitores()
    }


    private fun obtenerMonitores() {
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


                // Mostramos el RecyclerView
                binding.recyclerViewMonitorLista.visibility = View.VISIBLE
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setupRecyclerView() {
        adapter = MonitorAdapter(requireContext(), mutableListOf()) // Inicialmente vacío
        binding.recyclerViewMonitorLista.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMonitorLista.adapter = adapter
    }



    /*
        override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
            inflater.inflate(R.menu.toolbar, menu)

            val searchItem = menu.findItem(R.id.action_search)
            val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView

            searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    adapter.filter(newText.orEmpty())
                    return true
                }
            })
        }*/
}


/*
*
*
*  private lateinit var binding: FragmentListaBinding
    private lateinit var adapter: MonitorAdapter
    private var monitorLista = mutableListOf<Monitor>()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentListaBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //nombre
        //precio
        //favorito
        //url
        //Datos a mostrar
        val monitorLista = listOf(
            //METER LAS IMAGENES DE LA NUEVA FORMA EXPLICADA
            Monitor("KOORUI 24″ Full HD", "99,99€", false, R.drawable.monitor1),
            Monitor("Philips 24E1N1100A", "74,90€", false, R.drawable.monitor2),
            Monitor("Acer EK220Q H3", "67,99€", true, R.drawable.monitor3),
            Monitor("LG 22MR410-B", "76,99€", false, R.drawable.monitor4),
            Monitor("MSI Pro MP243X", "83,00€", true, R.drawable.monitor5),
            Monitor("Lenovo D24-45", "78,90€", true, R.drawable.monitor6),
            Monitor("SAMSUNG LS24C310EAUXEN", "94,99€", false, R.drawable.monitor7)

        )
        // Referencia al RecyclerView en el layout, al archivo activity_favoritos
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewMonitorLista)

        // Configuración del RecyclerView
        //establecer un layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MonitorAdapter(requireContext(), monitorLista)

    }

        /*
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            binding.recyclerViewMonitorLista.visibility = View.GONE

            lifecycleScope.launch {
                //delay(2500)


                async { obtenerMonitor() }.await()


                binding.recyclerViewMonitorLista.visibility = View.VISIBLE

                //setupSearchView()

            }
        }
        private fun obtenerMonitor() {
            val monitorLista = mutableListOf<Monitor>()
            db.collection("restaurantes")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val monitor = Monitor(
                            document.getString("nombre") ?: "",
                            document.getString("precio") ?: "",
                            document.getBoolean("fav") ?: false,
                            document.getString("imagen")?.toInt() ?: 0,
                        )
                        monitorLista.add(monitor)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    setupRecyclerView(monitorLista)
                    binding.recyclerViewMonitorLista.visibility = View.VISIBLE
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }

        }
        private fun setupRecyclerView(monitor: List<Monitor>) {
            binding.recyclerViewMonitorLista.layoutManager =
                LinearLayoutManager(requireContext())
            adapter = MonitorAdapter(requireContext(), monitor)
            binding.recyclerViewMonitorLista.adapter = adapter
        }

       /* private fun setupSearchView() {
            binding..setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filtrarMonitor(newText.orEmpty())
                    return true
                }
            })
        }*/

        private fun filtrarMonitor(query: String) {
            val listaFiltrada = monitorLista.filter { it.nombre.contains(query, ignoreCase = true) }
            adapter.actualizarLista(listaFiltrada)
        }*/
*
*
*
*
* */