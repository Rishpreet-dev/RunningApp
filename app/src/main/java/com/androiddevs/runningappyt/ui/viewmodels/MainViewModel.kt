package com.androiddevs.runningappyt.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.other.SortType
import com.androiddevs.runningappyt.repositories.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    val mainRepository: MainRepository
): ViewModel() {

    val sortRunsByDate=mainRepository.getAllRunsSortedByTimeStamp()
    val sortRunsByDistance=mainRepository.getAllRunsSortedByDistance()
    val sortRunsByCaloriesBurnt=mainRepository.getAllRunsSortedByCaloriesBurnt()
    val sortRunsByAverageSpeed=mainRepository.getAllRunsSortedByAvgSpeed()
    val sortRunsByRunTime=mainRepository.getAllRunsSortedByTimeInMillis()

    //runs livedata which we will observe in the run fragmenrt
    val runs=MediatorLiveData<List<Run>>()

    var sortType=SortType.DATE

    init {
        //sortrunsbydate acts as a source to the RUN livedata
        runs.addSource(sortRunsByDate){result->
            //this lambda fun is called everytime thwere is a change in the livedata selection
            if(sortType==SortType.DATE){
                result?.let {
                    runs.value=it
                }
            }
        }

        runs.addSource(sortRunsByCaloriesBurnt){result->
            //this lambda fun is called everytime thwere is a change in the livedata selection
            if(sortType==SortType.CALORIES_BURNT){
                result?.let {
                    runs.value=it
                }
            }
        }

        runs.addSource(sortRunsByRunTime){result->
            //this lambda fun is called everytime thwere is a change in the livedata selection
            if(sortType==SortType.RUNNING_TIME){
                result?.let {
                    runs.value=it
                }
            }
        }

        runs.addSource(sortRunsByDistance){result->
            //this lambda fun is called everytime thwere is a change in the livedata selection
            if(sortType==SortType.DISTANCE){
                result?.let {
                    runs.value=it
                }
            }
        }

        runs.addSource(sortRunsByAverageSpeed){result->
            //this lambda fun is called everytime thwere is a change in the livedata selection
            if(sortType==SortType.AVG_SPEED){
                result?.let {
                    runs.value=it
                }
            }
        }
    }


    fun sortRuns(sortType: SortType)=when(sortType){
        SortType.AVG_SPEED->sortRunsByAverageSpeed.value?.let { runs.value=it }
        SortType.DISTANCE->sortRunsByDistance.value?.let { runs.value=it }
        SortType.DATE->sortRunsByDate.value?.let { runs.value=it }
        SortType.CALORIES_BURNT->sortRunsByCaloriesBurnt.value?.let { runs.value=it }
        SortType.RUNNING_TIME->sortRunsByRunTime.value?.let { runs.value=it }
    }.also {
        this.sortType=sortType
    }

    fun insertRun(run: Run)=viewModelScope.launch {
        mainRepository.insertRun(run)
    }

    fun deleteRun(run:Run)=viewModelScope.launch {
        mainRepository.deleteRun(run)
    }
}