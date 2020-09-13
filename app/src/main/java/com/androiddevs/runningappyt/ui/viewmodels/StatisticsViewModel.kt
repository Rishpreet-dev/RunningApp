package com.androiddevs.runningappyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.androiddevs.runningappyt.repositories.MainRepository

class StatisticsViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {


    val totalTimeRun=mainRepository.getTotalTimeSpent()
    val totalCaloriesBurnt=mainRepository.getTotalCaloriesBurnt()
    val totalAvgSpeed=mainRepository.getTotalAvgSpeed()
    val totalDistance=mainRepository.getTotalDistance()

    val runsSortedByDate=mainRepository.getAllRunsSortedByTimeStamp()
}