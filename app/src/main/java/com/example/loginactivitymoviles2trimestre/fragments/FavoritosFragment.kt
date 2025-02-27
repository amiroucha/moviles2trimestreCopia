package com.example.loginactivitymoviles2trimestre.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.databinding.FragmentFavoritosBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

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
        adapter = MonitorAdapter(monitoresLista)
        auth = FirebaseAuth.getInstance()
        val favoritos = db.collection("usuarios")
        val user = auth.currentUser!!.email!!.toString()
        //conseguir los id de los monitores fav almacenados en la coleccion usuarios

        favoritos.document(user)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    // array "fav" en usuarios
                    val arrayFavoritos = document.get("fav") as? List<*>

                    if (arrayFavoritos!= null) {
                        val monitorFav = db.collection("monitor")
                        // documentos de la colección "monitor"
                        monitorFav.get().addOnSuccessListener { documents ->
                                for (doc in documents) {
                                    // Si el id del documento está en la lista de favoritos
                                    if (arrayFavoritos.contains(doc.id)) {
                                        monitores.add(
                                            Monitor(
                                                Integer.parseInt(doc.getString("id").toString()),
                                                doc.getString("nombre").toString(),
                                                doc.getString("precio").toString(),
                                                true,
                                                doc.getString("url").toString(),
                                            )
                                        )
                                    }
                                }
                                // Actualizamos los datos en el RecyclerView

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
    }
}