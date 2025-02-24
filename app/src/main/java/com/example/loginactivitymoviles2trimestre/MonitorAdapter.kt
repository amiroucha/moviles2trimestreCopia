package com.example.loginactivitymoviles2trimestre

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginactivitymoviles2trimestre.databinding.FragmentItemBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

//Adapter para recyclerView

class MonitorAdapter(private val context: Context, private var monitoresLista: MutableList<Monitor>)
    : RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder>() {

    // Guarda la lista original para restaurarla al filtrar
    private var itemsOriginal: List<Monitor> = ArrayList(monitoresLista)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitorViewHolder {
        val binding = FragmentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MonitorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MonitorViewHolder, position: Int) {
        holder.bind(monitoresLista[position])
    }

    override fun getItemCount(): Int = monitoresLista.size

    fun filter(query: String) {
        val filteredList = if (query.isEmpty()) {
            itemsOriginal
        } else {
            itemsOriginal.filter { it.nombre.contains(query, ignoreCase = true) }
        }
        updateData(filteredList)
    }
    fun updateData(nuevaLista: List<Monitor>) {
        monitoresLista.clear()
        monitoresLista.addAll(nuevaLista)
        itemsOriginal = ArrayList(nuevaLista) // Guardamos la lista original para filtrar
        notifyDataSetChanged()
    }


    class MonitorViewHolder(private val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Monitor) {
            binding.titulo.text = data.nombre
            binding.precio.text = data.precio

            if (data.favo) {
                binding.favorito.setImageResource(R.drawable.fav_selected)
            } else {
                binding.favorito.setImageResource(R.drawable.fav_unselected)
            }

            binding.favorito.setOnClickListener {
                data.favo = !data.favo
                if (data.favo) {
                    binding.favorito.setImageResource(R.drawable.fav_selected)
                } else {
                    binding.favorito.setImageResource(R.drawable.fav_unselected)
                }

                val db = Firebase.firestore
                db.collection("monitores")
                    .document(data.id.toString())
                    .update("favorito", data.favo)
            }
        }
    }
}



