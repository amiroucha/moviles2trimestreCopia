package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
   // private lateinit var progressBar: ProgressBar
    val basDatos =  Firebase.firestore

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

        // Inicializa el ProgressBar
      //  progressBar = view.findViewById(R.id.progressBar)

        val posicionMensaje = view.findViewById<View>(R.id.anchor_view)

        // Crea un objeto SpannableString con el texto "Recuperar contraseña"
        val underlineText = SpannableString(getString(R.string.NuevaContrasenia))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)// Aplica un subrayado a todo el texto dentro del objeto SpannableString

        binding.NuevaContrasenia.text = underlineText// Asigna el texto subrayado al TextView con el ID 'NuevaContrasenia'

        val accionCerrar = getString(R.string.Cerrar)

        binding.NuevaContrasenia.setOnClickListener { //no necesito comprobar la contraseña porque se busca cambiarla
            val mensaje = getString(R.string.NuevaPassword)
            Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_INDEFINITE)
                .setAction(accionCerrar) {}
                .setAnchorView(posicionMensaje)
                .show()
            //meter un alertDialog y que sea meter una pantalla pequeña de correo y que te mande un correo apra regenerarla
            //alli tengo que meter un bloque de logica que tiene firebase para hacer eso
        }

        binding.botonAcceder.setOnClickListener {
            val credenciales = validarCredenciales()

            if(credenciales != null) {
                val (usuario, contrasenia) = credenciales
                FirebaseAuth.getInstance().signInWithEmailAndPassword(usuario, contrasenia)
                    .addOnCompleteListener { task ->
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

                    }
            }


            //me daba advertencia con la progress bar, asi que lo he dejado sin ella de momento
//            if (validarCredenciales()){
//                // Muestra la barra de progreso y redirige después de 3 segundos
//                mostrarBarraProgreso()
//            } tiene que estar en la vista de favoritos y de lista fragment
        }


        /* TODAVIA NO TENGO CONTACTO
        binding.botonGoogle.setOnClickListener{
            if (validarCredenciales()){
                //redirigir a Contactos
                val intent = Intent(this, ContactoActivity::class.java)
                startActivity(intent)
            }
        }*/
        binding.botonFacebook.setOnClickListener{
            //enseñar el mensaje snackbar
            Snackbar.make(binding.root, getString(R.string.irContacto), Snackbar.LENGTH_LONG).show()

        }

        binding.botonRegistrar.setOnClickListener{
            //redirigir a REgistro
            findNavController()
                .navigate(R.id.action_Login_to_Registro)
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
    private fun validarCredenciales(): Pair<String, String>? {
        //he tenido que añadir binding.root para que me pillase el findViewById
        val usuario = binding.user.editText?.text.toString().trim()
        val contrasenia = binding.password.editText?.text.toString().trim()
        val mensajeAlerta = binding.root.findViewById<View>(R.id.avisoError)

        return when {
            usuario.isEmpty() || contrasenia.isEmpty() -> {
                mensajeCamposVacios(mensajeAlerta)
                null
            }
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

    private fun mensajeCamposVacios(view: View) {
        val mensaje = getString(R.string.MensError)
        val ok = getString(R.string.ok)
        Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_SHORT)
            .setAction(ok) {}
            .setAnchorView(view)
            .show()
    }
    private fun errorAutenticacion() {
        mostrarAlerta(
            titulo = getString(R.string.errorAutenticacion),
            mensaje = getString(R.string.errorAutenticacionFrase)
        )
    }

}