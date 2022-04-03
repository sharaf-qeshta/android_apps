package com.example.college

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.college.databinding.ActivityRegisterBinding
import com.example.college.db.DatabaseHelper
import com.example.college.db_modules.User
import com.google.firebase.auth.FirebaseAuth


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private var mAuth: FirebaseAuth? = null

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var pass1: String
    private lateinit var pass2: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()


        binding.register.setOnClickListener {
            register()
        }

        binding.collegeLogo.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }




    }

    private fun register()
    {
        name = binding.name.text.toString().trim()
        email = binding.email.text.toString().trim()
        pass1 = binding.password.text.toString().trim()
        pass2 = binding.password2.text.toString().trim()


        if (name.isEmpty())
        {
            binding.name.error = "Name Cannot Be Empty"
            binding.name.requestFocus()
            return
        }


        if (email.isEmpty())
        {
            binding.email.error = "Email Cannot Be Empty"
            binding.email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            binding.email.error = "Provide a Valid Email"
            binding.email.requestFocus()
            return
        }

        if (pass1.isEmpty())
        {
            binding.password.error = "Password Cannot Be Empty"
            binding.password.requestFocus()
            return
        }

        if (pass2.isEmpty())
        {
            binding.password2.error = "Retype The Password"
            binding.password2.requestFocus()
            return
        }

        if (pass1 != pass2)
        {
            binding.password2.error = "Type The Same Password In The Two Fields"
            binding.password2.requestFocus()
            return
        }


        if (pass1.length < 6)
        {
            binding.password.error = "The Password Should Be At Least 6 Characters"
            binding.password.requestFocus()
            return
        }

        mAuth?.createUserWithEmailAndPassword(email, pass1)
            ?.addOnSuccessListener {
                val db = DatabaseHelper(0)
                val userid = FirebaseAuth.getInstance().currentUser?.uid
                val user = User(name, email)

                db.addUser(user, userid).addOnSuccessListener {
                    Toast.makeText(this, "user registered", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "fail to register the user", Toast.LENGTH_LONG).show()
                }
            }?.addOnFailureListener {
                Toast.makeText(this, "fail to register the user", Toast.LENGTH_LONG).show()
            }


    }
}