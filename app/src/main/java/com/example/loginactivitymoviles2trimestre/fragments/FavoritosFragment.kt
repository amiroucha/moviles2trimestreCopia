package com.example.loginactivitymoviles2trimestre.fragments
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.firestore.FirebaseFirestore

class FavoritosFragment : Fragment() {

    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: MonitorAdapter
    private lateinit var db: FirebaseFirestore
    private var listaFavoritos = mutableListOf<Monitor>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        // Inicializamos Firestore y la lista de favoritos
        db = FirebaseFirestore.getInstance()
        listaFavoritos = mutableListOf()

        //adapter con una lista vacía inicialmente
        adapter = MonitorAdapter(listaFavoritos)
        binding.recyclerMonitorFav.adapter = adapter

        mostraRecyclerView()

        setupSwipeRefresh()

        cargarFavoritos()
    }

    // Se ejecuta cuando el fragmento se reanuda
    override fun onResume() {
        super.onResume()
        cargarFavoritos() // Recargamos la lista de favoritos
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                cargarFavoritos() // Recargamos los datos
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1000) // Esperamos 1 segundo antes de recargar
        }
    }

    private fun cargarFavoritos() {
        // Limpiamos la lista de favoritos antes de cargar nuevos datos
        listaFavoritos.clear()

        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        // obtener solo los monitores marcados como favoritos
        db.collection("monitor")
            .whereEqualTo("favorito", true) //favorito = true
            .get()
            .addOnSuccessListener { documents ->
                // Recorremos los documentos obtenidos y los añadimos a la lista
                for (document in documents) {
                    val favorito = document.get("favorito") as Boolean
                    val mon = Monitor(
                        document.id.toInt(),
                        document.getString("nombre") ?: "",
                        document.getString("precio") ?: "",
                        favorito,
                        document.getString("url") ?: ""
                    )
                    listaFavoritos.add(mon)
                }

                // Ocultamos el ProgressBar y mostramos el RecyclerView con los datos cargados
                binding.progressBarFav.visibility = View.GONE
                binding.recyclerMonitorFav.visibility = View.VISIBLE
                // adapter.updateList(listaFavoritos)

                adapter = MonitorAdapter(listaFavoritos) // nuevo adaptador con la lista actualizada
                mostraRecyclerView() // Volvemos a configurar el RecyclerView
            }
            .addOnFailureListener { _ ->
                // Si hay un error , ocultamos el ProgressBar y mostramos un mensaje de error
                binding.progressBarFav.visibility = View.GONE
                Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostraRecyclerView() {
        binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMonitorFav.adapter = adapter
    }
}

