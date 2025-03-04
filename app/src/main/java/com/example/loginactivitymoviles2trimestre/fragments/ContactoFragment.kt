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
    private lateinit var binding: FragmentContactoBinding
    private val cALLPHONEPERMISSIONREQUEST = 123 // Código de solicitud para el permiso de llamadas

    private val lOCATIONPERMISSIONREQUESTCODE = 124 // Código de solicitud para el permiso de ubicación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Se infla el layout
        binding = FragmentContactoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.botonllamar.setOnClickListener{
            // ver si tenemos permiso para hacer llamadas
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:123123321"))

                //ver si existe app para llamar
                if (intent.resolveActivity(requireContext().packageManager) != null){
                    startActivity(intent) // Iniciar la llamada
                } else {
                    Snackbar.make(binding.root, R.string.llamadaNoPosible, Snackbar.LENGTH_LONG).show()
                }
            } else {
                // no tenemos el permiso, lo pedimos al usuario
                if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.CALL_PHONE)) {
                    // Si se rechazo antes aviso de que ya me lo ha rechazado
                    Snackbar.make(binding.root, R.string.permisoRechazado, Snackbar.LENGTH_LONG).show()
                } else {
                    // Solicitar el permiso
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(android.Manifest.permission.CALL_PHONE),
                        cALLPHONEPERMISSIONREQUEST
                    )
                }
            }
        }

        binding.botoncorreo.setOnClickListener{
            enviarEmail()
        }

        binding.botonWhatsApp.setOnClickListener{
            enviarMensaje()
        }

        binding.botonMapa.setOnClickListener{
            verificarPermisoYAbrirMapa() // Verificar permisos y abrir mapa si es posible
        }
        binding.mapaImagen.setOnClickListener{
            verificarPermisoYAbrirMapa() // Permite abrir el mapa tocando la imagen
        }
    }

    private fun verificarPermisoYAbrirMapa() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            abrirMapa() // Si permiso abrir el mapa
        } else {
            // no permiso, pedirlo
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                lOCATIONPERMISSIONREQUESTCODE
            )
        }
    }

    // abrir Google Maps con ubicacion de amazon
    private fun abrirMapa() {
        val latitude = "37.82819578871782"
        val longitude = "-1.0984185636505475"
        val zoom = "15"

        // Crear la URI con la ubicación y el zoom en Google Maps
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?z=$zoom")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri) // Crear el intent

        // Intent para seleccionar  maps
        try {
            startActivity(mapIntent) // Intentar abrir maps
        } catch (e: Exception) {
            // Si no hay maps, enseñar mensaje de error
            Snackbar.make(binding.root, R.string.noAppMaps, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun enviarEmail(){
        val destinatario = "amazon@gmail.com" // Dirección de correo de destino

        // Crear el intent para enviar email
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$destinatario")
        }
        try {
            startActivity(emailIntent) // Intentar abrir la app de correo
        } catch (e: Exception) {
            // Si no hay correo instalado, mostrar un mensaje de error
            Snackbar.make(
                binding.root,
                R.string.noCorreoApp,
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun enviarMensaje(){
        val numeroTelefono = "1234567890" // Número de teléfono sin el + !!!!

        val mensaje = R.string.bienvenidaWas.toString()

        // Crear la URL de WhatsApp con el número de teléfono y el mensaje
        val irWhatsappurl = "https://api.whatsapp.com/send?phone=$numeroTelefono&text=${Uri.encode(mensaje)}"
        val uri = Uri.parse(irWhatsappurl) // Convertir la URL en URI

        // Crear intent para abrir WhatsApp
        val wasIntent = Intent(Intent.ACTION_VIEW, uri)

        try {
            startActivity(wasIntent) // Intentar abrir whatsApp
        } catch (e: Exception) {
            // Si no hay whatsApp, mostrar mensaje de error
            Snackbar.make(binding.root, R.string.noAppWas, Snackbar.LENGTH_LONG).show()
        }
    }
}
