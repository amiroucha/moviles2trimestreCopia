package com.example.loginactivitymoviles2trimestre.fragments
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: MonitorAdapter
    private lateinit var db: FirebaseFirestore
    private var listaFavoritos = mutableListOf<Monitor>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritosBinding.inflate(inflater,container,false)
        db = FirebaseFirestore.getInstance()
        listaFavoritos = mutableListOf()

        adapter = MonitorAdapter(listaFavoritos)
        binding.recyclerMonitorFav.adapter = adapter

        return binding.root
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        mostraRecyclerView()
        // Configurar SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Simular una recarga de 2 segundos
            Handler(Looper.getMainLooper()).postDelayed({
                // Recargar los datos
                cargarFavoritos()
                // Detener la animación de carga
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1000) // 1 segundos
        }
        // Cargar favoritos al iniciar el fragmento

        cargarFavoritos()

    }

    private fun cargarFavoritos() {
        listaFavoritos.clear()
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        db.collection("monitor")
            .whereEqualTo("favorito", true)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val favorito = document.getBoolean("favorito") ?: false
                    val mon = Monitor(
                        document.id.hashCode(), // Considera usar el id original si es posible
                        document.getString("nombre") ?: "",
                        document.getString("precio") ?: "",
                        favorito,
                        document.getString("url") ?: ""
                    )
                    listaFavoritos.add(mon)
                }

                // Actualizar la UI
                binding.progressBarFav.visibility = View.GONE
                binding.recyclerMonitorFav.visibility = View.VISIBLE
                adapter = MonitorAdapter(listaFavoritos)
                binding.recyclerMonitorFav.adapter = adapter
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                binding.progressBarFav.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }


    private fun mostraRecyclerView() {
        adapter = MonitorAdapter(mutableListOf())
        binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMonitorFav.adapter = adapter
    }

}
