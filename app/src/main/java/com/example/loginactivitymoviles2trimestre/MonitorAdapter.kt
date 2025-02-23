package com.example.loginactivitymoviles2trimestre

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.loginactivitymoviles2trimestre.databinding.FragmentItemBinding

//Adapter para recyclerView

class MonitorAdapter(private val context: Context, private var monitoresLista: List<Monitor>
) : RecyclerView.Adapter<MonitorAdapter.MonitorViewHolder>()  {

    // Infla el layout para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonitorViewHolder
    {
        //LayoutItemBinding debe de ser  =  al nombre del acticity Item para poder importar la clase
        val binding = FragmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MonitorViewHolder(binding)
    }
    // Asigna los valores de los datos a cada vista
    override fun onBindViewHolder(holder: MonitorViewHolder, position: Int)
    {
        holder.bind(monitoresLista[position])
    }
    fun actualizarLista(nuevaLista: List<Monitor>) {
        monitoresLista = nuevaLista
        notifyDataSetChanged()


    }

    // Devuelve el tamaño de la lista
    override fun getItemCount(): Int = monitoresLista.size

    class MonitorViewHolder(private val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data : Monitor){ //esta funcion se llama por cada item de monitorAdapter
            // Acceder a las vistas directamente a través del binding
            binding.titulo.text = data.nombre //asigno el nombre del monitor
            binding.precio.text = data.precio //asigno el precio
            Glide
                .with(binding.root)
                .load(data.imagen) //imagen en monitor
                .into(binding.imageMonitor) //la de layoutIterm

            if (data.favo) { // Si es favorito
                binding.favorito.setImageResource(R.drawable.fav_selected)
            } else { // Si no es favorito
                binding.favorito.setImageResource(R.drawable.fav_unselected)
            }
            /* Evento OnClick */
            binding.favorito.setOnClickListener{
                if (data.favo) {
                    binding.favorito.setImageResource(R.drawable.fav_unselected)//en el layoutIterm
                }else {
                    binding.favorito.setImageResource(R.drawable.fav_selected)
                }
                data.favo = !data.favo
            }

        }
    }
}


