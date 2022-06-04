package com.example.iug.fragments

import android.app.Dialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.iug.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment:  Fragment()
{
    fun showErrorSnackBar(message: String, errorMessage: Boolean)
    {
        val snackBar =
            Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage)
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(requireActivity(), R.color.snack_bar_error)
            )
        else
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(requireActivity(), R.color.snack_bar_success)
            )

        snackBar.show()
    }


    private lateinit var mProgressBar: Dialog

    /**
     * add the progress bar to the screen
     * */
    fun showProgressDialog()
    {
        mProgressBar = Dialog(requireActivity())

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
        if (mProgressBar.isShowing)
            mProgressBar.dismiss()
    }
}