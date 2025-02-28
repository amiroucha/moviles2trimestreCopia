package com.example.loginactivitymoviles2trimestre.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class FavoritosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MonitorAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var listaMonitores = mutableListOf<Monitor>()
    private var listaFavoritos = mutableListOf<Int>() // Lista de IDs favoritos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoritos, container, false)
        recyclerView = view.findViewById(R.id.recyclerMonitorFav)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore

        cargarFavoritos()

        return view
    }

    private fun cargarFavoritos() {
        val usuario = auth.currentUser?.email ?: return

        db.collection("usuarios").document(usuario).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    listaFavoritos = (document.get("fav") as? List<Long>)?.map { it.toInt() }?.toMutableList() ?: mutableListOf()
                }
                if (listaFavoritos.isNotEmpty()) {
                    cargarMonitoresFavoritos()
                } else {
                    actualizarRecyclerView()
                }
            }
            .addOnFailureListener {
                println("Error al obtener favoritos")
                actualizarRecyclerView()
            }
    }

    private fun cargarMonitoresFavoritos() {
        if (listaFavoritos.isEmpty()) {
            actualizarRecyclerView()
            return
        }

        db.collection("monitor").whereIn("id", listaFavoritos).get()
            .addOnSuccessListener { result ->
                listaMonitores.clear()
                for (document in result) {
                    val monitor = Monitor(
                        document.id.toInt(),
                        document.get("nombre") as String,
                        document.get("precio") as String,
                        document.get("favorito") as Boolean,
                        document.get("url") as String,
                    )
                    listaMonitores.add(monitor)
                }
                actualizarRecyclerView()
            }
            .addOnFailureListener {
                println("Error al obtener monitores favoritos")
                actualizarRecyclerView()
            }
    }

    private fun actualizarRecyclerView() {
        if (!::adapter.isInitialized) {
            adapter = MonitorAdapter(listaMonitores)
            recyclerView.adapter = adapter
        } else {
            adapter.updateList(listaMonitores)
        }
    }
}