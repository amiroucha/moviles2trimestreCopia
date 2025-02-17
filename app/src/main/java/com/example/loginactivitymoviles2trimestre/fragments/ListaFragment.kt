package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            MonitorFragment("KOORUI 24″ Full HD"," 99,99€" ,false,R.drawable.monitor1),
            MonitorFragment("Philips 24E1N1100A"," 74,90€" ,false,R.drawable.monitor2),
            MonitorFragment("Acer EK220Q H3"," 67,99€" ,true,R.drawable.monitor3),
            MonitorFragment("LG 22MR410-B","76,99€" ,false,R.drawable.monitor4),
            MonitorFragment("MSI Pro MP243X"," 83,00€" ,true,R.drawable.monitor5),
            MonitorFragment("Lenovo D24-45"," 78,90€" ,true,R.drawable.monitor6),
            MonitorFragment("SAMSUNG LS24C310EAUXEN"," 94,99€" ,false,R.drawable.monitor7)

        )
        // Referencia al RecyclerView en el layout, al archivo activity_favoritos
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerV)

        // Configuración del RecyclerView
        //establecer un layout manager
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MonitorAdapter(monitorLista)
    }

}