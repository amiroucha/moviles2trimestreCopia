package com.example.loginactivitymoviles2trimestre.fragments
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: MonitorAdapter
    private var monitoresLista = mutableListOf<Monitor>()
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/*
        //Datos a mostrar
        val monitorLista = listOf(
            //METER LAS IMAGENES DE LA NUEVA FORMA EXPLICADA
            Monitor("KOORUI 24″ Full HD", " 99,99€", false, R.drawable.monitor1),
            Monitor("Philips 24E1N1100A", " 74,90€", false, R.drawable.monitor2),
            Monitor("Acer EK220Q H3", " 67,99€", true, R.drawable.monitor3),
            Monitor("LG 22MR410-B", "76,99€", false, R.drawable.monitor4),
            Monitor("MSI Pro MP243X", " 83,00€", true, R.drawable.monitor5),
            Monitor("Lenovo D24-45", " 78,90€", true, R.drawable.monitor6),
            Monitor("SAMSUNG LS24C310EAUXEN", " 94,99€", false, R.drawable.monitor7)

        )
        // Referencia al RecyclerView en el layout, al archivo activity_favoritos
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerMonitorFav)

        // Configuración del RecyclerView
        //establecer un layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MonitorAdapter(requireContext(), monitorLista)
*/
    }


/*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.recyclerMonitorFav.visibility = View.GONE

    lifecycleScope.launch {
        delay(2500)
        binding.recyclerMonitorFav.visibility = View.VISIBLE


        async { monitoresLista = obtenerRestaurantes() }.await()
        setupRecyclerView()
        //setupSearchView()

    }
}

private fun obtenerRestaurantes(): MutableList<Monitor> {
    val restaurantesLista = mutableListOf<Monitor>()
    db.collection("restaurantes")
        .whereEqualTo("fav", true)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val monitor = Monitor(
                    document.getString("nombre") ?: "",
                    document.getString("precio") ?: "",
                    document.getBoolean("fav") ?: false,
                    document.getString("imagen")?.toInt() ?: 0,
                )
                restaurantesLista.add(monitor)
                Log.d(TAG, "${document.id} => ${document.data}")
            }
        }
        .addOnFailureListener { exception ->
            Log.w(TAG, "Error getting documents: ", exception)
        }

    return restaurantesLista

}
private fun setupRecyclerView() {
    binding.recyclerMonitorFav.layoutManager = LinearLayoutManager(requireContext())
    adapter = MonitorAdapter(requireContext(), monitoresLista)
    binding.recyclerMonitorFav.adapter = adapter
}

private fun setupSearchView() {
    binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
        androidx.appcompat.widg.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            filtrarRestaurantes(newText.orEmpty())
            return true
        }
    })
}

private fun filtrarRestaurantes(query: String) {
    val listaFiltrada = monitoresLista.filter { it.nombre.contains(query, ignoreCase = true) }
    adapter.actualizarLista(listaFiltrada)
}*/


}
