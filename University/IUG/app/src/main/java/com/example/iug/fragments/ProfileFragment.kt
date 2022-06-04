package com.example.iug.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.iug.databinding.FragmentProfileBinding
import com.example.iug.firebase.Helper
import com.example.iug.firebase.models.Student

class ProfileFragment : BaseFragment()
{
    private lateinit var binding: FragmentProfileBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        loadData()

        binding.buttonSave.setOnClickListener {
            update()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    private fun loadData()
    {
        val student = Helper().getStudent(requireActivity())

        binding.editTextFirstName.setText(student.firstName)
        binding.editTextLastName.setText(student.lastName)
        binding.editTextEmail.setText(student.email)
        binding.editTextMobileNumber.setText(student.phone)

        if (student.gender == 1)
        {
            binding.radioButtonFemale.isChecked = false
            binding.radioButtonMale.isChecked = true
        }
        else if (student.gender == 2)
        {
            binding.radioButtonFemale.isChecked = true
            binding.radioButtonMale.isChecked = false
        }
    }

    private fun update()
    {
        val firstName = binding.editTextFirstName.text.toString().trim()
        val lastName = binding.editTextLastName.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val phone = binding.editTextMobileNumber.text.toString().trim()
        var gender = -1
        if (binding.radioButtonFemale.isChecked)
            gender = 2
        else if (binding.radioButtonMale.isChecked)
            gender = 1

        val student = Helper().getStudent(requireActivity())

        var goForward = firstName != student.firstName ||
                    lastName != student.lastName ||
                    email != student.email ||
                    phone != student.phone ||
                    gender != student.gender
        if (!goForward)
        {
            showErrorSnackBar("update was successfully saved", false)
            return
        }
        else
        {
            goForward = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    || email.isNotEmpty() || firstName.isNotEmpty() || lastName.isNotEmpty()
            if (goForward)
            {
                showProgressDialog()
                val updatedStudent = Student(student.id, "",
                    "", 0.0, firstName, lastName, 0, gender, email, phone, "", "")
                Helper().updateStudent(this@ProfileFragment, updatedStudent)

            }
            else
                showErrorSnackBar("Write Valid Data", true)
        }
    }
}