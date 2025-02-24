package com.example.loginactivitymoviles2trimestre.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.login.viewmodels.RegisterViewModel
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import androidx.lifecycle.Observer
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat


class RegistroFragment : Fragment() {
    private lateinit var binding: FragmentRegistroBinding
    private lateinit var auth: FirebaseAuth
    private val registerViewModel: RegisterViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentRegistroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()

        //mensaje de alerta posicion
        val mensajeAlerta = view.findViewById<View>(R.id.avisoErrorREG)

        // Observamos los errores de cada campo
        registerViewModel.emailError.observe(viewLifecycleOwner, Observer { error ->
            binding.userReg.error = error
        })

        registerViewModel.passwordError.observe(viewLifecycleOwner, Observer { error ->
            binding.password.error = error
        })

        registerViewModel.dateError.observe(viewLifecycleOwner, Observer { error ->
            binding.fechanac.error = error
        })


        binding.userReg.editText?.addTextChangedListener {
            registerViewModel.setEmail(it.toString())
        }

        binding.password.editText?.addTextChangedListener {
            registerViewModel.setPassword(it.toString())
        }

        binding.fechanac.addTextChangedListener {
            registerViewModel.setDate(it.toString())
        }

        binding.botonRegistrar.setOnClickListener{

            if (registerViewModel.validateOnSubmit()) {
                val credenciales = validarCredenciales()
                val contrasenia = binding.password.editText?.text.toString().trim()
                val contraseniaRepetir = binding.password.editText?.text.toString().trim()
                if(credenciales !=null && contrasenia == contraseniaRepetir)//la contraseña y el usuario es correcto
                {
                    crearCuenta(
                        binding.userReg.editText?.text.toString(),
                        binding.password.editText?.text.toString(),
                        binding.passwordRepetir.editText?.text.toString(),
                        binding.fechanac.text.toString(),
                    )
                }
            } else {

                Snackbar.make(it, R.string.MensError, Snackbar.LENGTH_LONG).show()
            }
        }
        //Fecha nacimiento----------------------------------------------------------------
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            /*
            //le paso los datos al formato en si y lo guardo
            val fechaFormateada = getString(R.string.fechaFormato,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR))

            //se le aplica el formatro con datos de usuaruio
            binding.fechanac.setText(fechaFormateada)*/
            binding.fechanac.setText(
                "${calendar.get(Calendar.DAY_OF_MONTH)}/${calendar.get(Calendar.MONTH) + 1}/${calendar.get(Calendar.YEAR)}"
            )
        }
        binding.IconoFecha.setOnClickListener{
            showDatePicker(calendar, dateSetListener)
        }

        binding.cancelarBTN.setOnClickListener {

            findNavController().navigate(R.id.action_Registro_to_login)

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
    fun crearCuenta(email: String, password: String, passwordRepetir:String, fecha:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val db = FirebaseFirestore.getInstance()

                        val timestampFecha = try {
                            val sdf = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
                            val date = sdf.parse(fecha)
                            date?.let { Timestamp(it) }

                        } catch (e: Exception) {
                            null
                        }
                        val userData = hashMapOf(
                            "correo" to email,
                            "contraseña" to passwordRepetir,
                            "fecha" to timestampFecha,
                        )

                        db.collection("usuarios").document(email)
                            .set(userData)
                            .addOnSuccessListener {
                                findNavController().navigate(R.id.action_Registro_to_scaffold)
                            }
                            .addOnFailureListener { e ->
                                Snackbar.make(binding.root, "Error al guardar usuario: ${e.message}", Snackbar.LENGTH_LONG).show()
                            }
                    }
                } else {
                    Snackbar.make(binding.root, "Error al crear cuenta: ${task.exception?.message}", Snackbar.LENGTH_LONG).show()
                }
            }
    }

    /*de aqui abajo es para la comprobacion de los campos, que sean correctos y qwue no esten vacios*/
    private fun validarCredenciales(): Pair<String, String>? {
        //he tenido que añadir binding.root para que me pillase el findViewById
        val usuario = binding.userReg.editText?.text.toString().trim()
        val contrasenia = binding.password.editText?.text.toString().trim()
        val contraseniaRepetir = binding.password.editText?.text.toString().trim()
        return when { //compruebo que tengan un formato valido
            !esCorreoValido(usuario) -> {
                mostrarAlertaCorreoInvalido()
                null
            }
            !esContraseniaValida(contrasenia) -> {
                mostrarAlertaContraseniaInvalida()
                null
            }!esContraseniaValida(contraseniaRepetir) -> {
                mostrarAlertaContraseniaInvalida()
                null
            }
            else -> Pair(usuario, contrasenia)
        }
    }
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



}