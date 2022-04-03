package com.example.college

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.TestLooperManager
import android.util.Patterns
import android.widget.Toast
import com.example.college.databinding.ActivityMainBinding
import com.example.college.testing.Test
import com.google.firebase.auth.FirebaseAuth



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        if (mAuth!!.currentUser != null)
        {
            println(mAuth!!.currentUser?.displayName)
            startActivity(Intent(this, Profile::class.java))
            finish()
            return
        }

        binding.login.setOnClickListener {
            loginIn()
        }

        binding.register.setOnClickListener {
            val i = Intent(this, Register::class.java)
            startActivity(i)
        }

        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }



    }

    private fun loginIn()
    {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()

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

        if (password.isEmpty())
        {
            binding.password.error = "Password Cannot Be Empty"
            binding.password.requestFocus()
            return
        }

        if (password.length < 6)
        {
            binding.password.error = "The Password Should Be At Least 6 Characters"
            binding.password.requestFocus()
            return
        }

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnSuccessListener {
                val user = FirebaseAuth.getInstance().currentUser

                if (user!!.isEmailVerified){
                    Toast.makeText(this, "Signed In", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Profile::class.java))
                    finish()
                }else{
                    user.sendEmailVerification()
                    Toast.makeText(this, "Check Your Inbox", Toast.LENGTH_LONG).show()
                }


            }?.addOnFailureListener {
                Toast.makeText(this, "Fail to Sign In", Toast.LENGTH_LONG).show()
            }

    }
}