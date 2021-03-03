package com.example.chatapp.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.chatapp.MainActivity
import com.example.chatapp.Modelos.Usuarios
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlin.jvm.java as java1

/**
 * A simple [Fragment] subclass.
 * Use the [profile_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class profile_fragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var username: TextView? = null
    var email: TextView? = null
    var imgPerfil: ImageView? = null
    var logOut: Button? = null
    var reference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    var storageReference: StorageReference? = null
    private var imageUri: Uri? = null
    private val subir: StorageTask<*>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_fragment, container, false)
        username = view.findViewById(R.id.currentNameUsuario)
        email = view.findViewById(R.id.currentEmail)
        logOut = view.findViewById(R.id.logOut)
        logOut?.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(context, MainActivity::class)
        })
        imgPerfil = view.findViewById(R.id.perfilImagenTouch)
        storageReference = FirebaseStorage.getInstance().getReference("uploads")
        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser!!.uid)
        email?.setText(FirebaseAuth.getInstance().currentUser!!.email)
        reference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuarios = snapshot.getValue(Usuarios::class)
                username?.setText(usuarios!!.nomUser)
                if (usuarios?.urlPhoto == "default") {
                    imgPerfil?.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(context!!).load(usuarios?.urlPhoto)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        imgPerfil?.setOnClickListener(View.OnClickListener { openGallery() })
        return view
    }

    private fun selectImagen() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(i, IMAGE_REQUEST)
    }

    private fun getFile(uri: Uri): String? {
        val contentResolver = context!!.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data!!.data
            imgPerfil!!.setImageURI(imageUri)
        }
    }

    companion object {
        private const val PICK_IMAGE = 100
        fun newInstance(param1: String?, param2: String?): profile_fragment {
            val fragment = profile_fragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }

        private const val IMAGE_REQUEST = 1
    }
}