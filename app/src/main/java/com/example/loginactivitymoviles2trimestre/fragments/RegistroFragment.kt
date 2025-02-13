package com.example.loginactivitymoviles2trimestre.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentRegistroBinding
import com.example.loginactivitymoviles2trimestre.R.id.textoFechaSeleccionada
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
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
            findNavController()
                .navigate(R.id.action_Registro_to_scaffold)
            /*
            val usuario = binding.userReg.editText?.text.toString().trim()
            val contrasenia = binding.password.editText?.text.toString().trim()
            if(usuario.isNotEmpty() && contrasenia.isNotEmpty()){
                if (esCorreoValido(usuario) && esContraseniaValida(contrasenia)) {
                    //se pone el guion para decir que no se va  ausar ningun parametro del OnclickListener
                    val intent = Intent(this, FavoritosActivity::class.java)
                    startActivity(intent)

                } else if (!esCorreoValido(usuario)) {
                    mostrarAlertaCorreoInvalido()
                } else {
                    mostrarAlertaContraseniaInvalida()
                }
            }else
            { // Mostrar un mensaje de error si uno de los campos está vacío
                mensajeCamposVacios(mensajeAlerta)
            }*/
        }
        /*
        val calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            binding.fechanac.setText(
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            )
        }

        binding.IconoFecha.setOnClickListener{
            showDatePicker(calendar, dateSetListener)
        }

        binding.IconoFecha.setOnClickListener {
            showDatePicker(calendar, dateSetListener)
        }
    }
    private fun showDatePicker(calendar: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener) {
        DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
        */
    //para la fecha de nacimiento---------------------------------------------------------
        /*
        //enlazado con seleccionarFecha
        val botonSeleccionarFecha = view.findViewById<Button>(R.id.botonSeleccionarFecha)
        //TextView donde se mostrará la fecha seleccionada
        val textoFechaSeleccionada = view.findViewById<TextView>(textoFechaSeleccionada)

        // Definir las fechas mínima y máxima
        val calendarMin = Calendar.getInstance().apply {
            set(1925, Calendar.JANUARY, 1) // Año 1925, mes enero, día 1
        }
        val calendarMax = Calendar.getInstance().apply {
            set(2010, Calendar.JANUARY, 1) // Año 2010, mes enero, día 1
        }

        // Obtener los timestamps de las fechas mínima y máxima
        //con el calendar es la forma mas facil que encontre
        val fechaMinima = calendarMin.timeInMillis
        val fechaMaxima = calendarMax.timeInMillis

        //constraints de fecha (minima y maxima)
        //uso el CalendarConstraints
        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(fechaMinima)  // Fecha mínima
            .setEnd(fechaMaxima)    // Fecha máxima
            .build()

        botonSeleccionarFecha.setOnClickListener {
            // Crear el Material DatePicker, selecctor de fecha con restricciones
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.TextSelecciona) //Selecciona tu fecha de nacimiento
                .setCalendarConstraints(constraintsBuilder)  // Aplicar las restricciones
                .build()

            // Mostrar el selector de fecha, el calendario
            //he tenido que cambiar getSupportFragmentManager() por childFragmentManager porque no me lo reconocia
            datePicker.show(childFragmentManager, "MATERIAL_DATE_PICKER")

            // Manejar la fecha seleccionada
            datePicker.addOnPositiveButtonClickListener { selection ->

                val fechaSeleccionada = convertirFecha(selection as Long)

                // Actualizar el TextView con la fecha seleccionada
                textoFechaSeleccionada.text = buildString {
                    append(getString(R.string.fechNacer))
                    append("  ")
                    append(fechaSeleccionada)
                }
            }


        }*/


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

    // Método para convertir un timestamp a una cadena de texto
    // fecha en milisegundos a fecha con el formato deseado (dd/MM/yyyy)
    private fun convertirFecha(timestamp: Long): String {
        //especifico formato dd/MM/yyyy
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        //se hace la conversion y se manda
        return formatter.format(Date(timestamp))
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
        AlertDialog.Builder(requireContext()) /*uso required context en vez de this, y salta excepcion si el fragment no esta vinculado a un activity*/
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