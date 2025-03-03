package com.example.loginactivitymoviles2trimestre.fragments
import android.net.http.HttpException
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

        // Cargar favoritos al iniciar el fragmento
        CoroutineScope(Dispatchers.IO).launch {
            cargarFavoritos()
        }

        // Configurar SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            CoroutineScope(Dispatchers.IO).launch {
                delay(1000) // Simula una espera de 1 segundo
                cargarFavoritos()
                withContext(Dispatchers.Main) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
    private suspend fun cargarFavoritos() {
        listaFavoritos.clear()

        withContext(Dispatchers.Main) {
            binding.recyclerMonitorFav.visibility = View.GONE
            binding.progressBarFav.visibility = View.VISIBLE
        }

        try {
            val result = db.collection("monitor")
                .whereEqualTo("favorito", true)
                .get().await()
            for (document in result) {
                val favorito = document.get("favorito") as Boolean
                val mon = Monitor(
                    document.id.hashCode(), // Usando hashCode como ID no me da error
                    document.get("nombre") as String,
                    document.get("precio") as String,
                    favorito,
                    document.get("url") as String,
                )
                listaFavoritos.add(mon)
            }

            //actualizar la vista
            withContext(Dispatchers.Main) {
                binding.progressBarFav.visibility = View.GONE
                binding.recyclerMonitorFav.visibility = View.VISIBLE
                // Actualizar el adaptador
                adapter = MonitorAdapter(listaFavoritos)
                binding.recyclerMonitorFav.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        } catch (e: Exception) {
            Log.e("FirebaseError", "Error al obtener amigos: ", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun mostraRecyclerView() {
        adapter = MonitorAdapter(mutableListOf())
        binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMonitorFav.adapter = adapter
    }

}
