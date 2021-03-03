package com.example.chatapp.Controlador

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.MensajeActivity
import com.example.chatapp.Modelos.Usuarios
import com.example.chatapp.R

class ControlUsuarios     //Constructor de la clase para el RC de los usuarios
(private val context: Context?, private val lUsers: MutableList<Usuarios?>?) : RecyclerView.Adapter<ControlUsuarios.ViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Inflamos los items de los usuarios
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuarios = lUsers?.get(position)
        holder.username?.setText(usuarios?.getNomUser())
        if (usuarios?.getUrlPhoto() == "default") {
            holder.fotoPerfil?.setImageResource(R.mipmap.ic_launcher)
        } else {
            if (context != null) {
                Glide.with(context).load(usuarios?.getUrlPhoto()).into(holder.fotoPerfil!!)
            }
        }
        holder.itemView.setOnClickListener {
            val i = Intent(context, MensajeActivity::class.java)
            i.putExtra("userid", usuarios?.getId())
            if (context != null) {
                context.startActivity(i)
            }
        }
    }

    override fun getItemCount(): Int {
        return lUsers?.size!!
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView?
        var fotoPerfil: ImageView?

        init {
            username = itemView.findViewById(R.id.txtUsername)
            fotoPerfil = itemView.findViewById(R.id.imgUsuario)
        }
    }
}