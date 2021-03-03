package com.example.chatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.chatapp.Modelos.Usuarios
import com.example.chatapp.Perfil
import com.example.chatapp.fragments.UserFragment
import com.example.chatapp.fragments.profile_fragment
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class Perfil : AppCompatActivity() {
    //Firebase
    var firebaseUser: FirebaseUser? = null
    var myRef: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Usuario y BD
        firebaseUser = FirebaseAuth.getInstance().currentUser
        myRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser!!.getUid())
        myRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.getValue(Usuarios::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
        //TAB
        val tabLayout = findViewById<TabLayout?>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager?>(R.id.vPager)
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(profile_fragment(), "Perfil")
        viewPagerAdapter.addFragment(UserFragment(), "Contactos")
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    //Inflamos el menu en esta activida
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    //Logica para el uso del menu item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOutButton -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@Perfil, MainActivity::class.java))
                finish()
                return true
            }
        }
        return false
    }
} //Logica para el Tab

internal class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private val fragments: ArrayList<Fragment?>?
    private val tittles: ArrayList<String?>?
    override fun getItem(position: Int): Fragment {
        return fragments?.get(position)!!
    }

    override fun getCount(): Int {
        return fragments?.size!!
    }

    fun addFragment(fragment: Fragment?, title: String?) {
        fragments?.add(fragment)
        tittles?.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tittles?.get(position)
    }

    init {
        fragments = ArrayList()
        tittles = ArrayList()
    }
}