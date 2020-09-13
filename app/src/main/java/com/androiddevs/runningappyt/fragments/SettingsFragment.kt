package com.androiddevs.runningappyt.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.adapters.BestRunAdapter
import com.androiddevs.runningappyt.adapters.RunAdapter
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGhT
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment:Fragment(R.layout.fragment_settings) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadFromSharedPref()




        btnApplyChanges.setOnClickListener {
            val success=applyChangesToSharedPref()
            if (success){

                Snackbar.make(view,"Saved Successfully",Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(view,"Please Apply Some Changes",Snackbar.LENGTH_LONG).show()
            }
        }


        ivProfilePic.setOnClickListener {
            val intent=Intent()
            intent.apply {
                action=Intent.ACTION_GET_CONTENT
                type="image/*"

            }
            startActivityForResult(Intent.createChooser(intent,"Select Image From:"),1)
        }
    }

    private fun loadFromSharedPref(){
        val name=sharedPreferences.getString(KEY_NAME,"")
        val weight=sharedPreferences.getFloat(KEY_WEIGhT,75f)
        etName.setText(name)
        etWeight.setText(weight.toString())
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private fun applyChangesToSharedPref():Boolean{
        val userName=etName.text.toString()
        val userWeight=etWeight.text.toString()

        if(userName.isEmpty() || userWeight.isEmpty()){
            return false
        }

        sharedPreferences.edit()
            .putString(KEY_NAME,userName)
            .putFloat(KEY_WEIGhT,userWeight.toFloat())
            .apply()

        val toolBarText="Let's go $userName!"
        requireActivity().tvToolbarTitle.text=toolBarText
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==RESULT_OK && requestCode==1){
            val uri= data?.data
            ivProfilePic.setImageURI(uri)
            //still have to save it somehow
        }
    }




}