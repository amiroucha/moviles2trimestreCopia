package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginactivitymoviles2trimestre.Monitor
import com.example.loginactivitymoviles2trimestre.MonitorAdapter
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentListaBinding


class ListaFragment : Fragment() {
    private lateinit var binding: FragmentListaBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentListaBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        //Datos a mostrar
        val monitorLista = listOf(
            //METER LAS IMAGENES DE LA NUEVA FORMA EXPLICADA
            Monitor("KOORUI 24″ Full HD"," 99,99€" ,false,R.drawable.monitor1),
            Monitor("Philips 24E1N1100A"," 74,90€" ,false,R.drawable.monitor2),
            Monitor("Acer EK220Q H3"," 67,99€" ,true,R.drawable.monitor3),
            Monitor("LG 22MR410-B","76,99€" ,false,R.drawable.monitor4),
            Monitor("MSI Pro MP243X"," 83,00€" ,true,R.drawable.monitor5),
            Monitor("Lenovo D24-45"," 78,90€" ,true,R.drawable.monitor6),
            Monitor("SAMSUNG LS24C310EAUXEN"," 94,99€" ,false,R.drawable.monitor7)

        )
        // Referencia al RecyclerView en el layout, al archivo activity_favoritos
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerV)

        // Configuración del RecyclerView
        //establecer un layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MonitorAdapter(requireContext(), monitorLista)
    }

}