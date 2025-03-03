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
import com.example.loginactivitymoviles2trimestre.fragments.viewModel.LoginViewModel
import com.google.firebase.auth.auth
import androidx.core.widget.addTextChangedListener
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    private val loginViewModel: LoginViewModel by viewModels()


    //Interface para pasar información del Fragment al Activity
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
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

        loginViewModel.passwordError.observe(viewLifecycleOwner) { error ->
            binding.password.error = error
        }
        loginViewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.user.error = error
        }
        binding.user.editText?.addTextChangedListener {
            loginViewModel.setEmail(it.toString())
        }
        binding.password.editText?.addTextChangedListener {
            loginViewModel.setPassword(it.toString())
        }

        // Crea un objeto SpannableString con el texto "Recuperar contraseña"
        val underlineText = SpannableString(getString(R.string.NuevaContrasenia))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)// Aplica un subrayado a todo el texto dentro del objeto SpannableString

        binding.NuevaContrasenia.text = underlineText// Asigna el texto subrayado al TextView con el ID 'NuevaContrasenia'


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
            signInWithGoogle()
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

    private fun signInWithGoogle(){
        val auth = FirebaseAuth.getInstance() //si no inicio esto aquí, no me inicia sesiñón

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false) //con esto en false ya me deja elegir cuenta
            .setServerClientId(getString(R.string.idWeb))
            //.setNonce(hashedNonce)
            .setAutoSelectEnabled(false) //con esto en false no selecciona automáticamente una cuenta de google
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            credentialManager = CredentialManager.create(context = requireContext())
            try {

                val result = credentialManager.getCredential(context = requireContext(), request = request)
                val credential = result.credential

                // Use googleIdTokenCredential and extract the ID to validate and
                // authenticate on your server.
                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                // You can use the members of googleIdTokenCredential directly for UX
                // purposes, but don't use them to store or control access to user
                // data. For that you first need to validate the token:
                val googleIdToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                if(authResult != null)
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Login exitoso", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_Login_to_Scaffold)
                    }

                }
                else
                {
                    withContext(Dispatchers.Main)
                    {
                        Toast.makeText(requireContext(), "Error en el login", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            catch (e: GetCredentialException)
            {
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(requireContext(), e.localizedMessage , Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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