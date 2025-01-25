package com.example.loginactivitymoviles2trimestre.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentContactoBinding
import com.google.android.material.snackbar.Snackbar


class ContactoFragment : Fragment() {
    private lateinit var binding: FragmentContactoBinding //Declaro una variable de vinculacion
    private val cALLPHONEPERMISSIONREQUEST = 123
    private val lOCATIONPERMISSIONREQUESTCODE = 124
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentContactoBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        //asiganr el permiso de llamar al botn de llamar de activityContacto
        binding.botonllamar.setOnClickListener{
            //Verificamos si tenemos el permiso o no
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            }else{ //si no tenemos el permiso se lo solicitamos al usuario
                //el usuario ya habia rechazado el permiso anteriormente, entonces tiene que activarlo desde ajustes
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),android.Manifest.permission.CALL_PHONE )){
                    Snackbar.make(binding.root, R.string.permisoRechazado, Snackbar.LENGTH_LONG).show()
                }else { //se pide el permiso

                    // Si el permiso ya fue rechazado antes le aviso
                    if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.CALL_PHONE)) {
                        Snackbar.make(binding.root, R.string.permisoRechazado, Snackbar.LENGTH_LONG).show()
                    } else {// Si es la primera vez que se pide el permiso, se lo pido

                        ActivityCompat.requestPermissions(
                            requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),
                            cALLPHONEPERMISSIONREQUEST
                        )
                    }
                }
            }
        }
        //asiganr el permiso de correo al botn de correo de activityContacto
        binding.botoncorreo.setOnClickListener{
            enviarEmail()
        }
        //Mensaje de Wasap
        binding.botonWhatsApp.setOnClickListener{
            enviarMensaje()
        }
        //abirir maps y pedir la ubicacion  dandole o al icono o a la imagen
        binding.botonMapa.setOnClickListener{
            // Verificamos permisos de ubicación
            verificarPermisoYAbrirMapa()
        }
        binding.mapaImagen.setOnClickListener{
            verificarPermisoYAbrirMapa()
        }
    }
    private fun verificarPermisoYAbrirMapa() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            abrirMapa()
        } else {
            // Si no tenemos el permiso, lo solicitamos
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                lOCATIONPERMISSIONREQUESTCODE
            )
        }
    }
    private fun abrirMapa() {
        //val direccion = "Calle Isidro Vivancos Muñoz, Parque Logístico del, 30156 Murcia"
        val latitude="37.82819578871782"
        val longitude="-1.0984185636505475"
        val zoom="15"
        // Crear la URI con formato de Google Maps
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?z=$zoom")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri) //creo el intent

        // Intent para seleccionar la app adecuada
        try {
            startActivity(mapIntent)
        } catch (e: Exception) {
            Snackbar.make(binding.root, R.string.noAppMaps, Snackbar.LENGTH_LONG).show()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //compruebo si el código de solicitud = al de la ubicación (permiso de ACCESS_FINE_LOCATION)
        if (requestCode == lOCATIONPERMISSIONREQUESTCODE) {
            // Abre el mapa, sin importar si el permiso fue concedido o no
            abrirMapa()
        }
    }

    //metodo para hacer la llamada
    private fun makePhoneCall(){
        //intent para hacer la llamada
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:123123321"))
        //busca una app para hacer la accion
        if (intent.resolveActivity(requireContext().packageManager) != null){
            startActivity(intent)
        }
        else{
            Snackbar.make(binding.root, R.string.llamadaNoPosible, Snackbar.LENGTH_LONG).show()
        }
    }
    private fun enviarEmail(){
        //SendTo : app de envar datos
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            //mail to lo limita a app de correos
            data = Uri.parse("mailto:") //email destinatario
        }
        try {
            startActivity(emailIntent)
        }catch (e: Exception){
            // Si no hay aplicaciones de correo  enseño un mensaje de error
            Snackbar.make(
                binding.root,
                R.string.noCorreoApp,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }
    private fun enviarMensaje(){
        val numeroTelefono = "1234567890" // Número de teléfono con código de país sin el "+"

        val mensaje = R.string.bienvenidaWas.toString() //to string porq sino lo pilla como un int

        //direccion de was + telefono + mensaje codificado para que sea seguro y no de problemas en la URI
        val irWhatsappurl = "https://api.whatsapp.com/send?phone=$numeroTelefono&text=${Uri.encode(mensaje)}"

        val uri = Uri.parse(irWhatsappurl)//parsear la  uri

        //crear intent para abrir wasap
        //uso ACtionView por acceder a was por una url
        val wasIntent  = Intent(Intent.ACTION_VIEW, uri)
        try {
            startActivity(wasIntent)

        }catch (e: Exception){
            Snackbar.make(binding.root, R.string.noAppWas, Snackbar.LENGTH_LONG).show()
        }
    }
}