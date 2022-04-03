package com.example.college

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.college.databinding.ActivityProfileBinding
import com.example.college.db.DatabaseHelper
import com.example.college.db_modules.User
import com.example.college.fragments.ChatsFragment
import com.example.college.fragments.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val settings = SettingsFragment()
    private val chats = ChatsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replace(chats) // main one

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chats -> replace(chats)
                R.id.settings -> replace(settings)
            }
            true
        }
    }


    private fun replace(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }


}