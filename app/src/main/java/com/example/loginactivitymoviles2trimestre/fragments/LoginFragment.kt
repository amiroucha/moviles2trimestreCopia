package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.example.loginactivitymoviles2trimestre.fragments.viewmodelCarp.LoginViewModel
import com.google.firebase.auth.auth
import androidx.lifecycle.Observer
import androidx.core.widget.addTextChangedListener




class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    private val loginViewModel: LoginViewModel by viewModels()


    //Interface para pasar información del Fragment al Activity
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        //para establecer la conexion con el activity

        auth = Firebase.auth
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_Login_to_Scaffold)
            return
        }

        loginViewModel.emailError.observe(viewLifecycleOwner, Observer { error ->
            binding.user.error = error
        })

        loginViewModel.passwordError.observe(viewLifecycleOwner, Observer { error ->
            binding.password.error = error
        })


        binding.user.editText?.addTextChangedListener {
            loginViewModel.setEmail(it.toString())
        }

        binding.password.editText?.addTextChangedListener {
            loginViewModel.setPassword(it.toString())
        }

        val posicionMensaje = view.findViewById<View>(R.id.anchor_view)

        // Crea un objeto SpannableString con el texto "Recuperar contraseña"
        val underlineText = SpannableString(getString(R.string.NuevaContrasenia))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)// Aplica un subrayado a todo el texto dentro del objeto SpannableString

        binding.NuevaContrasenia.text = underlineText// Asigna el texto subrayado al TextView con el ID 'NuevaContrasenia'

        val accionCerrar = getString(R.string.Cerrar)

        binding.NuevaContrasenia.setOnClickListener { //no necesito comprobar la contraseña porque se busca cambiarla
            showResetPasswordDialog()
        }

        binding.botonAcceder.setOnClickListener {

            if (loginViewModel.validateOnSubmit()) {//comprueba que no esten vacios
                val credenciales = validarCredenciales()
                if (credenciales != null) {
                    val (usuario, contrasenia) = credenciales

                    binding.botonAcceder.isEnabled = false // Deshabilita el botón para evitar múltiples toques

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario, contrasenia)
                        .addOnCompleteListener { task ->
                            Handler(Looper.getMainLooper()).postDelayed({

                                binding.botonAcceder.isEnabled = true // Rehabilita el botón

                                if (task.isSuccessful) {
                                    // Usuario autenticado, navegar a Scaffold
                                    val navHostFragment =
                                        activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment)
                                                as NavHostFragment
                                    val navController = navHostFragment.navController
                                    navController.navigate(R.id.action_Login_to_Scaffold)
                                } else {
                                    errorAutenticacion()
                                }
                            }, 2000) // Espera 2 segundos antes de continuar
                        }
                }
            }

        }



        binding.botonGoogle.setOnClickListener{

        }

        binding.botonFacebook.setOnClickListener{
            //enseñar el mensaje snackbar
            Snackbar.make(binding.root, getString(R.string.irContacto), Snackbar.LENGTH_LONG).show()

        }

        binding.botonRegistrar.setOnClickListener{
            //redirigir a REgistro
            findNavController().navigate(R.id.action_Login_to_Registro)
//            val intent = Intent(this, RegistroActivity::class.java)
//            startActivity(intent)
        }

    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        //_binding = null
    }

    companion object {
        //Patrón Singleton
        private var instance: LoginFragment? = null

        fun getInstance(): LoginFragment
        {
            if (instance == null)
            {
                instance = LoginFragment()
            }
            //Usas !! para garantizar que no sera null
            return instance!!
        }
    }
/* La progress bar me da problemas con la memoria, asi que de momento lo comento todo
    private fun mostrarBarraProgreso() {
        // Muestra la barra de progreso
        progressBar.visibility = View.VISIBLE

        // Usamos una corrutina para esperar 3 segundos
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // Esperar 3 segundos

            // Después de esperar, oculta la barra de progreso
            progressBar.visibility = View.GONE

            // Redirige a FavoritosActivity
            /*--------------------------------------------------------------------------------------------------
            todavia no tengo favoritos
            val intent = Intent(this@MainActivity, FavoritosActivity::class.java)
            startActivity(intent)*/
        }
    }*/

    private fun showResetPasswordDialog() {
        val builder = android.app.AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val dialogView = inflater.inflate(R.layout.fragment_alert_dialog, null)

        val emailInput = dialogView.findViewById<TextInputLayout>(R.id.correoResetPass)
            .editText

        val sendButton = dialogView.findViewById<Button>(R.id.sendEmailResetPassword)

        builder.setView(dialogView)
        val alertDialog = builder.create()

        sendButton.setOnClickListener {
            val email = emailInput?.text.toString().trim()
            if (email.isNotEmpty()) {
                resetPassword(email)
                alertDialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Introduce un correo válido", Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()

    }
    private fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Snackbar.make(binding.root, R.string.enviarCorreo, Snackbar.LENGTH_LONG)
                        .setAction(R.string.Cerrar) {}.show()
                } else {
                    val errorMessage = when (task.exception?.message) {

                        else -> "Error al enviar el correo. Inténtalo de nuevo."
                    }
                    Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
                        .setAction(R.string.Cerrar) {}.show()
                }
            }
    }

    private fun validarCredenciales(): Pair<String, String>? {
        //he tenido que añadir binding.root para que me pillase el findViewById
        val usuario = binding.user.editText?.text.toString().trim()
        val contrasenia = binding.password.editText?.text.toString().trim()

        return when { //compruebo que tengan un formato valido
            !esCorreoValido(usuario) -> {
                mostrarAlertaCorreoInvalido()
                null
            }
            !esContraseniaValida(contrasenia) -> {
                mostrarAlertaContraseniaInvalida()
                null
            }
            else -> Pair(usuario, contrasenia)
        }
    }
    // Función para validar el correo electrónico

    private fun esCorreoValido(correo: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return correo.matches(emailRegex.toRegex())
    }
    // Función para validar la contraseña
    private fun esContraseniaValida(contrasenia: String): Boolean {
        return contrasenia.length in 6..10
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

    private fun errorAutenticacion() {
        mostrarAlerta(
            titulo = getString(R.string.errorAutenticacion),
            mensaje = getString(R.string.errorAutenticacionFrase)
        )
    }

}