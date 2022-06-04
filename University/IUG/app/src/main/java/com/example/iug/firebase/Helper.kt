package com.example.iug.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.iug.activities.BaseActivity
import com.example.iug.activities.MainActivity
import com.example.iug.firebase.models.Message
import com.example.iug.firebase.models.Student
import com.example.iug.firebase.relations.StudentCourse
import com.example.iug.fragments.BaseFragment
import com.example.iug.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson

class Helper
{
    private val fireStore = FirebaseFirestore.getInstance()

    fun registerStudent(activity: BaseActivity, student: Student)
    {
        fireStore.collection(Constants.STUDENTS)
            .document(student.id)
            .set(student, SetOptions.merge())
            .addOnSuccessListener{
                activity.dismissProgressDialog()
                activity.showErrorSnackBar("Account Registered", false)
            }
            .addOnFailureListener {
                activity.dismissProgressDialog()
                activity.showErrorSnackBar("Something Went Wrong", true)
            }
    }

    fun updateStudent(fragment: BaseFragment, student: Student)
    {
        fireStore.collection(Constants.STUDENTS)
            .document(student.id)
            .set(student, SetOptions.merge())
            .addOnSuccessListener{
                fragment.dismissProgressDialog()
                fragment.showErrorSnackBar("update was successfully saved", false)
                MainActivity.firstTime = true
                addToSharedPreference(fragment.requireActivity(), student)
            }
            .addOnFailureListener {
                fragment.dismissProgressDialog()
                fragment.showErrorSnackBar("Something Went Wrong", true)
            }
    }

    fun getCurrentUserId(): String
    {
        val currentUser = FirebaseAuth.getInstance().currentUser

        var id = ""
        if (currentUser != null)
            id = currentUser.uid
        return id
    }

    fun addStudentDetails(activity: Activity)
    {
        fireStore.collection(Constants.STUDENTS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                val student = document.toObject(Student::class.java)!!
                addToSharedPreference(activity, student)
            }
            .addOnFailureListener {
                // TODO deal with this
            }
    }

    fun getStudent(activity: Activity): Student
    {
        val sharedPreference =
            activity.getSharedPreferences(Constants.IUG_PREFERENCES, Context.MODE_PRIVATE)

        val json = sharedPreference.getString(Constants.LOGGED_IN_USER, "")
        return Gson().fromJson(json, Student::class.java)
    }

    private fun addToSharedPreference(activity: Activity, student: Student)
    {
        // add data to shared preferences
        val sharedPreference =
            activity.getSharedPreferences(Constants.IUG_PREFERENCES,
                Context.MODE_PRIVATE)

        val editor = sharedPreference.edit()
        editor.putString(Constants.LOGGED_IN_USER, Gson().toJson(student) )
        editor.apply()
    }

    fun signCurrentUserOut()
    {
        FirebaseAuth.getInstance().signOut()
    }


    fun deleteStudentCourse(courseID: String)
    {
        fireStore.collection(Constants.STUDENT_COURSES).document("${getCurrentUserId()}${courseID}")
            .delete()
    }

    fun addStudentCourse(courseID: String)
    {
        val studentCourse = StudentCourse("${getCurrentUserId()}${courseID}", getCurrentUserId(), courseID)
        fireStore.collection(Constants.STUDENT_COURSES).document(studentCourse.id)
            .set(studentCourse, SetOptions.merge())
            .addOnSuccessListener {
                Log.e("sharaf", "followed")
            }
            .addOnFailureListener {
                Log.e("sharaf", "something went wrong")
            }
    }

    fun sendMessage(teacherID: String, content: String)
    {
        val message = Message(content = content, date = System.currentTimeMillis(), senderID = getCurrentUserId())
        fireStore.collection("${getCurrentUserId()}$teacherID")
            .add(message)
            .addOnSuccessListener{

            }
            .addOnFailureListener {

            }
    }
}