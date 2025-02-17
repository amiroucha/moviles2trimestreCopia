package com.example.loginactivitymoviles2trimestre.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentRegistroBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*



class RegistroFragment : Fragment() {
    private lateinit var binding: FragmentRegistroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentRegistroBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //mensaje de alerta posicion
        val mensajeAlerta = view.findViewById<View>(R.id.avisoErrorREG)
       //Todavia no tengo fAVORITOS, pero ahora acceder me lleva al scaffold

        binding.botonRegistrar.setOnClickListener{
            val usuario = binding.userReg.editText?.text.toString().trim()
            val contrasenia = binding.password.editText?.text.toString().trim()
            if(usuario.isNotEmpty() && contrasenia.isNotEmpty()){
                if (esCorreoValido(usuario) && esContraseniaValida(contrasenia)) {
                    //se pone el guion para decir que no se va  ausar ningun parametro del OnclickListener
                    findNavController()
                        .navigate(R.id.action_Registro_to_scaffold)
                } else if (!esCorreoValido(usuario)) {
                    mostrarAlertaCorreoInvalido()
                } else {
                    mostrarAlertaContraseniaInvalida()
                }
            }else
            { // Mostrar un mensaje de error si uno de los campos está vacío
                mensajeCamposVacios(mensajeAlerta)
            }
        }

        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            //le paso los datos al formato en si y lo guardo
            val fechaFormateada = getString(R.string.fechaFormato,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR))

            //se le aplica el formatro con datos de usuaruio
            binding.fechanac.setText(fechaFormateada)
        }

        binding.IconoFecha.setOnClickListener{
            showDatePicker(calendar, dateSetListener)
        }

        binding.IconoFecha.setOnClickListener {
            showDatePicker(calendar, dateSetListener)
        }


        /* Todavia no tengo Contactos
        binding.botonGoogle.setOnClickListener{
            val usuario = binding.userReg.editText?.text.toString().trim()
            val contrasenia = binding.password.editText?.text.toString().trim()
            if(usuario.isNotEmpty() && contrasenia.isNotEmpty()){
                if (esCorreoValido(usuario) && esContraseniaValida(contrasenia)) {
                    //se pone el guion para decir que no se va  ausar ningun parametro del OnclickListener
                    //redirigir a Contactos
                    val intent = Intent(this, ContactoActivity::class.java)
                    startActivity(intent)

                } else if (!esCorreoValido(usuario)) {
                    mostrarAlertaCorreoInvalido()
                } else {
                    mostrarAlertaContraseniaInvalida()
                }
            }else
            { // Mostrar un mensaje de error si uno de los campos está vacío
                mensajeCamposVacios(mensajeAlerta)
            }
        }
        binding.botonFacebook.setOnClickListener{
            //enseñar el mensaje snackbar
            Snackbar.make(binding.root, getString(R.string.irContacto), Snackbar.LENGTH_LONG).show()
        }
        */
    }
    //FUNCIONES-----------------------------------------------------------------------------------------
    private fun showDatePicker(calendar: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    /*de aqui abajo es para la comprobacion de los campos, que sean correctos y qwue no esten vacios*/

    // Función para validar el correo electrónico
    private fun esCorreoValido(correo: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
        return correo.matches(emailRegex.toRegex())
    }
    // Función para validar la contraseña
    private fun esContraseniaValida(contrasenia: String): Boolean {
        return contrasenia.length in 8..10
    }

    private fun mostrarAlerta(titulo: String, mensaje: String) {
        val ok = getString(R.string.ok)
        /*uso require context en vez de this, y salta excepcion si el fragment no esta vinculado a un activity*/
        AlertDialog.Builder(requireContext())
            .setTitle(titulo)
            .setMessage(mensaje)
            .setPositiveButton(ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
    private fun mostrarAlertaCorreoInvalido() {
        mostrarAlerta(
            titulo = getString(R.string.nocorreo),
            mensaje = getString(R.string.nuevocorreo)
        )
    }
    private fun mostrarAlertaContraseniaInvalida() {
        mostrarAlerta(
            titulo = getString(R.string.nocontra),
            mensaje = getString(R.string.nuevacontra)
        )
    }

    private fun mensajeCamposVacios(view: View) {
        val mensaje = getString(R.string.MensError)
        val ok = getString(R.string.ok)
        Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT)
            .setAction(ok) {}
            .setAnchorView(view)
            .show()
    }

}