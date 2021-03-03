package com.example.chatapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.Controlador.ControlChatteo
import com.example.chatapp.MensajeActivity
import com.example.chatapp.Modelos.Chat
import com.example.chatapp.Modelos.Usuarios
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class MensajeActivity : AppCompatActivity() {
    var nombreUsuario: TextView? = null
    var imageView: ImageView? = null
    var recyclerView: RecyclerView? = null
    var mensaje: EditText? = null
    var enviar: ImageButton? = null
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var intent = null
    var chatList: MutableList<Chat?>? = null
    var controlChatteo: ControlChatteo? = null
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mensaje)
        imageView = findViewById(R.id.imagenview_perfil)
        nombreUsuario = findViewById(R.id.username)
        enviar = findViewById(R.id.btEnvio)
        mensaje = findViewById(R.id.etEscritura)

        //RC mensajes
        recyclerView = findViewById(R.id.rcMensajes)
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        intent = getIntent() as Nothing?
        val userid = intent.getStringExtra("userid")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("users").child(userid!!)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarios = snapshot.getValue(Usuarios::class.java)
                nombreUsuario?.setText(usuarios?.getNomUser())
                if (usuarios != null) {
                    if (usuarios.urlPhoto == "default") {
                        imageView?.setImageResource(R.mipmap.ic_launcher)
                    }
                    leoMensaje(firebaseUser?.getUid(), userid, usuarios.urlPhoto)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        enviar?.setOnClickListener(View.OnClickListener {
            val msm = mensaje?.getText().toString()
            if (msm != " ") {
                enviarMensaje(firebaseUser?.getUid(), userid, msm)
            } else {
                Toast.makeText(this@MensajeActivity, "No puede enviar mensajes vacios", Toast.LENGTH_SHORT).show()
            }
            mensaje?.setText("")
        })
    }

    private fun enviarMensaje(emisor: String?, receptor: String?, mensaje: String?) {
        val reference = FirebaseDatabase.getInstance().reference
        val hashMap = HashMap<String?, Any?>()
        hashMap["emisor"] = emisor
        hashMap["receptor"] = receptor
        hashMap["mensaje"] = mensaje
        reference.child("Conversaciones").push().setValue(hashMap)
    }

    private fun leoMensaje(emisor: String?, receptor: String?, imagenUrl: String?) {
        chatList = ArrayList()
        reference = FirebaseDatabase.getInstance().getReference("Conversaciones")
        reference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                chatList?.clear()
                for (snapshot in datasnapshot.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat?.getReceptor() == emisor && chat?.getEmisor() == receptor ||
                            chat?.getReceptor() == receptor && chat?.getEmisor() == emisor) {
                        chatList?.add(chat)
                    }
                    controlChatteo = ControlChatteo(this@MensajeActivity, chatList, imagenUrl)
                    recyclerView?.setAdapter(controlChatteo)
                    recyclerView?.scrollToPosition(controlChatteo?.getItemCount()!! -1)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun setSupportActionBar(toolbar: Toolbar?) {}
}