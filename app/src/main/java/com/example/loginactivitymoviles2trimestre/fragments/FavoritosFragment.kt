package com.example.loginactivitymoviles2trimestre.fragments
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritosFragment : Fragment() {
    private lateinit var binding: FragmentFavoritosBinding
    private lateinit var adapter: MonitorAdapter
    private var monitoresLista = mutableListOf<Monitor>()
    val db = Firebase.firestore
    var auth = Firebase.auth

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
        val monitores = arrayListOf<Monitor>()
        auth = FirebaseAuth.getInstance()
        //conseguir los id de los monitores fav almacenados en la coleccion usuarios
/*
        db.collection("usuarios").document(auth.uid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Obtenemos el array "fav" (IDs de los monitores)
                    val favIds = document.get("fav") as? List<String>

                    if (!favIds.isNullOrEmpty()) {
                        // Obtenemos todos los documentos de la colección "monitores"
                        db.collection("monitores").get()
                            .addOnSuccessListener { documents ->
                                for (doc in documents) {
                                    // Si el id del documento está en la lista de favoritos
                                    if (favIds.contains(doc.id)) {
                                        // Construimos el objeto Monitor (ajusta según tus campos)
                                        val monitor = Monitor(
                                            nombre = doc.getString("nombre") ?: "",
                                            precio =doc.getString("precio") ?: "",
                                            favo = true,
                                            imagen = doc.getString("imagen") ?: "",
                                            // Agrega más campos si tu modelo lo requiere
                                        )
                                        // Agregamos a la lista de monitores favoritos
                                        monitores.add(monitor)
                                    }
                                }
                                // En este punto, monitorList contiene los monitores favoritos
                                // Aquí puedes actualizar tu UI (RecyclerView, etc.) con monitorList
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), "Error al recoger los monitores favoritos", Toast.LENGTH_LONG).show()                            }
                    } else {
                        // El array "fav" está vacío o no existe
                    }
                } else {
                    // El documento del usuario no existe o es nulo
                }
            }
            .addOnFailureListener {
                // Manejo de error al obtener el documento del usuario
            }
*/
    }
}