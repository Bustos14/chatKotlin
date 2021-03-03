package com.example.chatapp.Controlador

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.Modelos.Chat
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ControlChatteo     //Constructor de la clase para el RC de los usuarios
(private val context: Context?, private val listChat: MutableList<Chat?>?, private val imgUrl: String?) : RecyclerView.Adapter<ControlChatteo.ViewHolder?>() {
    var firebaseUser: FirebaseUser? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == ENVIO) {
            //Inflamos los items de los usuarios
            val view = LayoutInflater.from(context).inflate(R.layout.item_mensaje, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.item_mensaje_recibido, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = listChat?.get(position)
        holder.mostrarMensaje?.setText(chat?.getMensaje())
        if (chat != null) {
            if (imgUrl == "default") {
                holder.fotoPerfil?.setImageResource(R.mipmap.ic_launcher)
            } else {
                if (context != null) {
                    Glide.with(context).load(imgUrl).into(holder.fotoPerfil!!)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listChat!!.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mostrarMensaje: TextView?
        var fotoPerfil: ImageView?

        init {
            mostrarMensaje = itemView.findViewById(R.id.muestroMensaje)
            fotoPerfil = itemView.findViewById(R.id.perfilImagen)
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (listChat?.get(position)?.getEmisor() == firebaseUser?.getUid()) {
            ENVIO
        } else {
            RECIBO
        }
    }

    companion object {
        const val RECIBO = 0
        const val ENVIO = 1
    }
}