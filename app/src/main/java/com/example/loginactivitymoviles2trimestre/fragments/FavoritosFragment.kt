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
    // Declaración de variables de vinculación y Firebase
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: MonitorAdapter // Adaptador para el RecyclerView
    private lateinit var db: FirebaseFirestore // Referencia a Firestore
    private var listaFavoritos = mutableListOf<Monitor>() // Lista que almacenará los monitores favoritos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflamos la vista del fragmento y vinculamos el binding
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultamos el RecyclerView y mostramos el ProgressBar mientras se cargan los datos
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        // Inicializamos Firestore y la lista de favoritos
        db = FirebaseFirestore.getInstance()
        listaFavoritos = mutableListOf()

        // Configuramos el adaptador con una lista vacía inicialmente
        adapter = MonitorAdapter(listaFavoritos)
        binding.recyclerMonitorFav.adapter = adapter

        // Configuramos el RecyclerView
        mostraRecyclerView()

        // Configuramos el swipe para refrescar la lista
        setupSwipeRefresh()

        // Cargamos los monitores favoritos desde Firestore
        cargarFavoritos()
    }

    // Se ejecuta cuando el fragmento se reanuda (por ejemplo, al volver de otra pantalla)
    override fun onResume() {
        super.onResume()
        cargarFavoritos() // Recargamos la lista de favoritos
    }

    private fun setupSwipeRefresh() {
        // Configuramos la funcionalidad de deslizar para refrescar la lista de favoritos
        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                cargarFavoritos() // Recargamos los datos
                binding.swipeRefreshLayout.isRefreshing = false // Ocultamos el icono de refresco
            }, 1000) // Esperamos 1 segundo antes de recargar
        }
    }

    private fun cargarFavoritos() {
        // Limpiamos la lista de favoritos antes de cargar nuevos datos
        listaFavoritos.clear()

        // Ocultamos el RecyclerView y mostramos el ProgressBar mientras se cargan los datos
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        // Consulta a Firestore para obtener solo los monitores marcados como favoritos
        db.collection("monitor")
            .whereEqualTo("favorito", true) // Filtramos por aquellos con la propiedad favorito = true
            .get()
            .addOnSuccessListener { documents ->
                // Recorremos los documentos obtenidos y los añadimos a la lista
                for (document in documents) {
                    val favorito = document.get("favorito") as Boolean
                    val mon = Monitor(
                        document.id.toInt(),
                        document.getString("nombre") ?: "", // Si el campo es nulo, asignamos una cadena vacía
                        document.getString("precio") ?: "",
                        favorito,
                        document.getString("url") ?: ""
                    )
                    listaFavoritos.add(mon)
                }

                // Ocultamos el ProgressBar y mostramos el RecyclerView con los datos cargados
                binding.progressBarFav.visibility = View.GONE
                binding.recyclerMonitorFav.visibility = View.VISIBLE

                // Aquí podríamos actualizar el adaptador en lugar de crear uno nuevo cada vez
                // adapter.updateList(listaFavoritos)

                adapter = MonitorAdapter(listaFavoritos) // Creamos un nuevo adaptador con la lista actualizada
                mostraRecyclerView() // Volvemos a configurar el RecyclerView
            }
            .addOnFailureListener { _ ->
                // Si hay un error en la consulta, ocultamos el ProgressBar y mostramos un mensaje de error
                binding.progressBarFav.visibility = View.GONE
                Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostraRecyclerView() {
        // Configuramos el RecyclerView con un LinearLayoutManager para mostrar los elementos en una lista vertical
        binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMonitorFav.adapter = adapter // Asignamos el adaptador al RecyclerView
    }
}

