package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.Registro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class Registro : AppCompatActivity(), View.OnClickListener {
    //Declaramos los botones
    private var guardaDatos: Button? = null
    private var cancelar: Button? = null
    private var etEmail: EditText? = null
    private var password: EditText? = null
    private var nomUser: EditText? = null
    private var auth: FirebaseAuth? = null

    //Firebase
    var database: FirebaseDatabase? = null
    var myRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        //Componentes necesarios para el registro
        guardaDatos = findViewById<View?>(R.id.successButton) as Button
        cancelar = findViewById<View?>(R.id.cancelButton) as Button
        etEmail = findViewById<View?>(R.id.regEmail) as EditText
        password = findViewById<View?>(R.id.regPass) as EditText
        nomUser = findViewById<View?>(R.id.etUsername) as EditText
        guardaDatos?.setOnClickListener(this)
        cancelar?.setOnClickListener(this)


        //Firebase auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        myRef = database?.getReference()
    }

    private fun registrarUsuario() {

        //Obtenemos el email y contraseña de las casillas de texto
        val email = etEmail?.getText().toString()
        val pass = password?.getText().toString()
        val nombreUsuario = nomUser?.getText().toString()
        //Comprobamos si han introducido email y contraseña
        if (nombreUsuario.isEmpty()) {
            Toast.makeText(this, "Introduce un nombre de usuario", Toast.LENGTH_SHORT).show()
        } else {
            if (email.isEmpty() && pass.isEmpty()) {
                Toast.makeText(this, "Falta ingresar email o contraseña", Toast.LENGTH_SHORT).show()
            } else {
                //Creamos nuevo usuario
                auth?.createUserWithEmailAndPassword(email, pass)?.addOnCompleteListener(this) { task ->
                    //Comprobamos si tiene exito el registro
                    if (task.isSuccessful) {
                        Toast.makeText(this@Registro, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        val firebaseUser = auth?.getCurrentUser()
                        val idUs = firebaseUser?.getUid()
                        //Mapeo
                        myRef = FirebaseDatabase.getInstance().getReference("users").child(idUs!!)
                        val hashMap = HashMap<String?, Any?>()
                        hashMap["id"] = idUs
                        hashMap["nomUser"] = nombreUsuario
                        hashMap["urlPhoto"] = "default"
                        myRef?.setValue(hashMap)
                        //Acceso al perfil si es exitoso
                        val acceso = Intent(this@Registro, Perfil::class.java)
                        startActivity(acceso)
                        //Destruimos la actividad
                        finish()
                    } else {
                        Toast.makeText(this@Registro, "Registro exitoso", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.successButton -> registrarUsuario()
            R.id.cancelButton -> {
                val goBack = Intent(this@Registro, MainActivity::class.java)
                startActivity(goBack)
                finish()
            }
        }
    }
}