package com.example.iug.activities

import android.app.Dialog
import android.os.Build
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.iug.R
import com.google.android.material.snackbar.Snackbar

open class BaseActivity: AppCompatActivity()
{
    fun dismissActionBar()
    {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        else
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
    }


    fun showErrorSnackBar(message: String, errorMessage: Boolean)
    {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage)
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.snack_bar_error)
            )
        else
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(this@BaseActivity, R.color.snack_bar_success)
            )

        snackBar.show()
    }


    private lateinit var mProgressBar: Dialog

    /**
     * add the progress bar to the screen
     * */
    fun showProgressDialog()
    {
        mProgressBar = Dialog(this)

        mProgressBar.setContentView(R.layout.dialog_progress)

        mProgressBar.setCancelable(false)
        mProgressBar.setCanceledOnTouchOutside(false)

        mProgressBar.show()
    }


    /**
     * remove the progress bar from the screen
     * */
    fun dismissProgressDialog()
    {
        mProgressBar.dismiss()
    }

}