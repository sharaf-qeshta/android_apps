package com.example.iug.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.example.iug.R
import com.example.iug.databinding.ActivityMainBinding
import com.example.iug.firebase.Helper
import com.example.iug.fragments.*


class MainActivity : BaseActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    companion object
    {
        var firstTime = true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // default fragment
        addFragment(HomeFragment())

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout,
            R.string.open, R.string.close  )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_chat -> replaceFragment(ChatFragment())
                R.id.nav_account -> replaceFragment(ProfileFragment())
                R.id.nav_add_course -> replaceFragment(AddCourseFragment())
                R.id.nav_assignments -> replaceFragment(AssignmentsFragment())
                R.id.nav_semesters -> replaceFragment(SmestersFragment())
                R.id.nav_exit -> signOut()
            }
            // to keep the item checked when the user press on it
            it.isChecked = true
            binding.drawerLayout.closeDrawers()
            true
        }

    }

    @SuppressLint("SetTextI18n")
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(toggle.onOptionsItemSelected(item))
        {
            if (firstTime)
            {
                val student = Helper().getStudent(this@MainActivity)
                findViewById<TextView>(R.id.user_name).text = "${student.firstName} ${student.lastName}"
                findViewById<TextView>(R.id.user_email).text = student.email
                firstTime = false
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun addFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragments_container, fragment)
        transaction.commit()
    }

    fun replaceFragment(fragment: Fragment)
    {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragments_container, fragment)
        transaction.commit()
    }

    private fun signOut()
    {
        showProgressDialog()
        Helper().signCurrentUserOut()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
        dismissProgressDialog()
    }
}