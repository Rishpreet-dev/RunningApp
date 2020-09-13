package com.androiddevs.runningappyt.other

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.androiddevs.runningappyt.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CancelTrackingDialog: DialogFragment() {

    //implementing Listener
    private var mListener:(()->Unit)?=null

    fun setUpListener(listener:()->Unit){
        mListener=listener
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        return MaterialAlertDialogBuilder(requireContext(),R.style.AlertDialogTheme)
            .setTitle("Cancel the Run?")
            .setMessage("Are you sure you want to cancel and delete this run?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Yes"){_,_->
                mListener?.let {
                    it()
                }
            }
            .setNegativeButton("No"){dialogInterface,_->
                dialogInterface.cancel()
            }
            .create()

    }

}