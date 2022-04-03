package com.example.college

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.college.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.logo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.reset.setOnClickListener {
            resetPassword()
        }
    }


    private fun resetPassword(){
        val email = binding.email.text.toString().trim()

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

        mAuth?.sendPasswordResetEmail(email)
            ?.addOnSuccessListener {
                Toast.makeText(this, "Check Your Inbox", Toast.LENGTH_LONG).show()
            }?.addOnFailureListener {
                Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
            }
    }
}