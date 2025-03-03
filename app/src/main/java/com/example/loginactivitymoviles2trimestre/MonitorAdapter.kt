package com.example.loginactivitymoviles2trimestre
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginactivitymoviles2trimestre.databinding.FragmentItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.FieldValue


//Adapter para recyclerView
class MonitorAdapter( private var monitoresLista: MutableList<Monitor>)
    : RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder>() {

    // Guarda la lista original para restaurarla al filtrar
    private var itemsOriginal: List<Monitor> = ArrayList(monitoresLista)
    private val auth: FirebaseAuth = Firebase.auth



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitorViewHolder {
        val binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false
        )
        return MonitorViewHolder(binding, auth)
    }
    //actualizar los datos de la vista.
    override fun onBindViewHolder(holder: MonitorViewHolder, position: Int) {
        holder.bind(monitoresLista[position])


    }

    override fun getItemCount(): Int = monitoresLista.size

    fun updateList(nuevaLista: List<Monitor>) {
        monitoresLista.clear()
        monitoresLista.addAll(nuevaLista)
        itemsOriginal = ArrayList(nuevaLista) // Guardamos la lista original para filtrar
        notifyDataSetChanged()
    }

    //para cargar los monitores
    class MonitorViewHolder(private val binding: FragmentItemBinding ,
                            private var auth: FirebaseAuth) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Monitor) {
            binding.titulo.text = data.nombre
            binding.precio.text = data.precio

            // Cargar la imagen usando Glide
            Glide.with(binding.root.context)
                .load(data.imagen) // URL de la imagen
                .placeholder(R.drawable.monitorcarga) // Imagen mientras se carga
                .into(binding.imageMonitor) // imagen xml

            // Actualizar el ícono de favorito
            if (data.favo) {
                binding.favorito.setImageResource(R.drawable.fav_selected)
            } else {
                binding.favorito.setImageResource(R.drawable.fav_unselected)
            }

            // Configurar el clic en el botón de favoritos
            binding.favorito.setOnClickListener {
                data.favo = !data.favo
                if (data.favo) {
                    binding.favorito.setImageResource(R.drawable.fav_selected)
                } else {
                    binding.favorito.setImageResource(R.drawable.fav_unselected)
                }

                // Actualizar Firestore
                val db = Firebase.firestore
                db.collection("monitor")
                    .document(data.id.toString())
                    .update("favorito", data.favo)
                    .addOnSuccessListener {
                        // Actualizar la lista de favoritos del usuario en Firestore
                        val usuario = auth.currentUser?.email.toString()
                        val usuarioRef = db.collection("usuarios").document(usuario)

                        if (data.favo) {
                            // Añadir a favoritos
                            usuarioRef.update("fav", FieldValue.arrayUnion(data.id))
                        } else {
                            // Eliminar de favoritos
                            usuarioRef.update("fav", FieldValue.arrayRemove(data.id))
                        }
                    }
                    .addOnFailureListener { exception ->
                        println("Error al actualizar favorito: ${exception.message}")
                    }
            }
        }
    }
}




