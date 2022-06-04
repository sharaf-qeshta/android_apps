package com.example.iug.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import com.example.iug.databinding.ActivityLoginBinding
import com.example.iug.firebase.Helper
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity()
{
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // if the user already logged in
        if (Helper().getCurrentUserId().isNotEmpty())
        {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding.textViewRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.textViewForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPassword::class.java))
        }

        binding.buttonLogin.setOnClickListener {
            login()
        }
    }

    private fun login()
    {
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()

        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()))
        {
            showErrorSnackBar("Invalid Email", true)
            return
        }

        if (password.length < 6)
        {
            showErrorSnackBar("Invalid Password", true)
            return
        }

        showProgressDialog()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                dismissProgressDialog()
                if (task.isSuccessful)
                {
                    // add the data to the shared preference
                    Helper().addStudentDetails(this@LoginActivity)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
                else
                {
                    dismissProgressDialog()
                    showErrorSnackBar("Invalid Data", true)
                }
            }
    }
}