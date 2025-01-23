package com.example.loginactivitymoviles2trimestre.fragments

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.loginactivitymoviles2trimestre.R
import com.example.loginactivitymoviles2trimestre.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
   // private lateinit var progressBar: ProgressBar

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
        val underlineText = SpannableString(getString(R.string.NuevaContrasenia))  // (obtenido desde el archivo de recursos de strings.xml).
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)// Aplica un subrayado a todo el texto dentro del objeto SpannableString

        binding.NuevaContrasenia.text = underlineText// Asigna el texto subrayado al TextView con el ID 'NuevaContrasenia'

        val accionCerrar = getString(R.string.Cerrar)

        binding.NuevaContrasenia.setOnClickListener { //no necesito comprobar la contraseña porque se busca cambiarla
            val mensaje = getString(R.string.NuevaPassword)
            Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_INDEFINITE)
                .setAction(accionCerrar) {}
                .setAnchorView(posicionMensaje)
                .show()
        }

        binding.botonAcceder.setOnClickListener {

            findNavController()
                .navigate(R.id.action_Login_to_Scaffold)
            //me daba advertencia con la progress bar, asi que lo he dejado sin ella de momento
//            if (validarCredenciales()){
//                // Muestra la barra de progreso y redirige después de 3 segundos
//                mostrarBarraProgreso()
//            }
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
    private fun validarCredenciales(): Boolean {
        //he tenido que añadir binding.root para que me pillase el findViewById
        val mensajeAlerta = binding.root.findViewById<View>(R.id.avisoError)
        val usuario = binding.user.editText?.text.toString().trim()
        val contrasenia = binding.password.editText?.text.toString().trim()
        return when {
            usuario.isEmpty() || contrasenia.isEmpty() -> {
                mensajeCamposVacios(mensajeAlerta)
                false
            }
            !esCorreoValido(usuario) -> {
                mostrarAlertaCorreoInvalido()
                false
            }
            !esContraseniaValida(contrasenia) -> {
                mostrarAlertaContraseniaInvalida()
                false
            }
            else -> true
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
        /*uso required context en vez de this, y salta excepcion si el fragment no esta vinculado a un activity*/
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