package com.example.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.Controlador.ControlUsuarios
import com.example.chatapp.Modelos.Usuarios
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class UserFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var controlUsuarios: ControlUsuarios? = null
    private var listausuarios: MutableList<Usuarios?>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        recyclerView = view.findViewById(R.id.rvUsers)
        listausuarios = ArrayList()
        leoUsuarios()
        return view
    }

    private fun leoUsuarios() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(datasnapshot: DataSnapshot) {
                listausuarios?.clear()
                for (snapshot in datasnapshot.children) {
                    val usuarios = snapshot.getValue(Usuarios::class)!!
                    if (usuarios?.id != firebaseUser?.getUid()) {
                        listausuarios?.add(usuarios)
                    }
                    controlUsuarios = ControlUsuarios(context, listausuarios)
                    recyclerView?.setAdapter(controlUsuarios)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}