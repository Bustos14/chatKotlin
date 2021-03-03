package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    //Declaramos los botones//
    private var buttonSingUp: Button? = null
    private var buttonLogin: Button? = null
    private var txtEmail: EditText? = null
    private var txtPassword: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Firebase
        auth = FirebaseAuth.getInstance()


        //Login
        setContentView(R.layout.activity_main)
        buttonSingUp = findViewById<View?>(R.id.singUpButton) as Button
        buttonLogin = findViewById<View?>(R.id.loginButton) as Button
        txtEmail = findViewById<View?>(R.id.emailEditText) as EditText
        txtPassword = findViewById<View?>(R.id.passwordEditText) as EditText
        buttonSingUp!!.setOnClickListener(this)
        buttonLogin!!.setOnClickListener(this)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        //Comprobando si existe el usuario y si existe lo guardamos
    }

    private fun loguearUsuario() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        val email = txtEmail?.getText().toString()
        val password = txtPassword?.getText().toString()

        //Verificamos que las cajas de texto no esten vacías
        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Se debe ingresar un email y una contraseña validos", Toast.LENGTH_LONG).show()
            return
        } else {
            //loguear usuario
            auth?.signInWithEmailAndPassword(email, password)
                    ?.addOnCompleteListener(this) { task ->
                        //Comprobamos si tiene exito
                        if (task.isSuccessful) {
                            val acceso = Intent(this@MainActivity, Perfil::class.java)
                            startActivity(acceso)
                            //Destruimos si
                            finish()
                        } else {
                            if (task.exception is FirebaseAuthUserCollisionException) { //si se presenta una colisión
                                Toast.makeText(this@MainActivity, "No existe este usuario", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
        }
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.singUpButton -> {
                val goReg = Intent(this@MainActivity, Registro::class.java)
                startActivity(goReg)
                finish()
            }
            R.id.loginButton -> loguearUsuario()
        }
    }
}