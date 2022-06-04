package com.example.iug.activities

import android.os.Bundle
import android.util.Patterns
import com.example.iug.R
import com.example.iug.databinding.ActivityRegisterBinding
import com.example.iug.firebase.Helper
import com.example.iug.firebase.models.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.Help

class RegisterActivity : BaseActivity()
{
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewLogin.setOnClickListener {
            onBackPressed()
        }

        binding.buttonRegister.setOnClickListener {
            register()
        }
    }


    private fun register()
    {
        val email  = binding.editTextEmail.text.toString().trim()
        val pass1 = binding.editTextPassword.text.toString().trim()
        val pass2 = binding.editTextConfirmPassword.text.toString().trim()
        val firstName = binding.editTextFirstName.text.toString().trim()
        val lastName = binding.editTextLastName.text.toString().trim()

        if (firstName.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.error_message_enter_first_name), true)
            return
        }

        if (lastName.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.error_message_enter_last_name), true)
            return
        }

        if (!(Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotEmpty()))
        {
            showErrorSnackBar(resources.getString(R.string.error_message_invalid_email), true)
            return
        }

        if (pass1.isEmpty())
        {
            showErrorSnackBar(resources.getString(R.string.error_message_invalid_password), true)
            return
        }

        if (pass1 != pass2 && pass1.length < 6)
        {
            showErrorSnackBar(resources.getString(R.string.error_message_enter_password_mismatch), true)
            return
        }

        /* keep rolling until the process finished */
        showProgressDialog()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pass1)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    val firebaseUser = task.result.user
                    val student = Student(firebaseUser!!.uid, "",
                        "", 0.0, firstName, lastName, 0, -1, email
                        , "", "")
                    Helper().registerStudent(student = student, activity = this@RegisterActivity)
                }
                else
                {
                    dismissProgressDialog()
                    showErrorSnackBar("Something Went Wrong", true)
                }
            }
    }
}