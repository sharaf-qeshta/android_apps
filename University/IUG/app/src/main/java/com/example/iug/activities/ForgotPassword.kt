package com.example.iug.activities

import android.os.Bundle
import android.util.Patterns
import com.example.iug.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : BaseActivity()
{
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSubmit.setOnClickListener {
            sendEmail()
        }
    }

    private fun sendEmail()
    {
        val email = binding.editTextEmail.text.toString().trim()
        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()))
        {
            showErrorSnackBar("Email is Invalid", true)
            return
        }

        // go and try to send an email
        showProgressDialog()
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                dismissProgressDialog()
                if (task.isSuccessful)
                    showErrorSnackBar("Check your inbox and follow the link", false)
                else
                    showErrorSnackBar("Something Went Wrong", true)
            }
    }
}