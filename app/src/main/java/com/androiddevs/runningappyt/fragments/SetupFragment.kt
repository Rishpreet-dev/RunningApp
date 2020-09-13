package com.androiddevs.runningappyt.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGhT
import com.androiddevs.runningappyt.ui.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_setup.*
import javax.inject.Inject

@AndroidEntryPoint
class SetupFragment:Fragment(R.layout.fragment_setup) {


    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @set:Inject
    var isFirstScreenOpen=true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if(!isFirstScreenOpen){
            val navOptions=NavOptions.Builder().setPopUpTo(R.id.setupFragment,true).build()

            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions

            )
        }
        tvContinue.setOnClickListener {
            val success=writePersonalDataToSHaredPref()
            if(success){
                findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            }else{
                Snackbar.make(requireView(),"Please Fill The Details",Snackbar.LENGTH_LONG).show()
            }


        }
    }

    private fun writePersonalDataToSHaredPref():Boolean{
        val name=etName.text.toString()
        val weight=etWeight.text.toString()


        if(name.isEmpty() || weight.isEmpty()){
            return false
        }
        sharedPreferences.edit()
            .putString(KEY_NAME,name)
            .putFloat(KEY_WEIGhT,weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_TOGGLE,false)
            .apply()

        val toolbarText="Let's go $name!"
        requireActivity().tvToolbarTitle.text=toolbarText
        return true
    }
}