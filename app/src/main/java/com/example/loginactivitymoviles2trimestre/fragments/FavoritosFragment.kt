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
        //db = FirebaseFirestore.getInstance()
       // listaFavoritos = mutableListOf()

        //adapter = MonitorAdapter(listaFavoritos)
       // binding.recyclerMonitorFav.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE
        db = FirebaseFirestore.getInstance()
        listaFavoritos = mutableListOf()

        adapter = MonitorAdapter(listaFavoritos)
        binding.recyclerMonitorFav.adapter = adapter
        mostraRecyclerView()
        setupSwipeRefresh()
        cargarFavoritos()
    }
    // Cuando el Fragment se reanuda, actualizamos la lista de monitores
    override fun onResume() {
        super.onResume()
        cargarFavoritos()
    }
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                cargarFavoritos()
                binding.swipeRefreshLayout.isRefreshing = false
            }, 1000)
        }
    }

    private fun cargarFavoritos() {
        listaFavoritos.clear()
        binding.recyclerMonitorFav.visibility = View.GONE
        binding.progressBarFav.visibility = View.VISIBLE

        db.collection("monitor")
            .whereEqualTo("favorito", true)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val favorito = document.get("favorito") as Boolean
                    val mon = Monitor(
                        document.id.hashCode(),
                        document.getString("nombre") ?: "",
                        document.getString("precio") ?: "",
                        favorito,
                        document.getString("url") ?: ""
                    )

                    listaFavoritos.add(mon)
                }

                binding.progressBarFav.visibility = View.GONE
                binding.recyclerMonitorFav.visibility = View.VISIBLE

                // adapter.updateList(listaFavoritos)
                adapter = MonitorAdapter(listaFavoritos)
                mostraRecyclerView()
            }
            .addOnFailureListener { _ ->
                binding.progressBarFav.visibility = View.GONE //por si falla que no se quede alli
                Toast.makeText(requireContext(), "Error cargando los monitores", Toast.LENGTH_SHORT).show()
            }
    }

    private fun mostraRecyclerView() {
        binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerMonitorFav.adapter = adapter
    }
}
