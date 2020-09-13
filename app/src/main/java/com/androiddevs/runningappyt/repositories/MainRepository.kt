package com.androiddevs.runningappyt.repositories

import com.androiddevs.runningappyt.db.Run
import com.androiddevs.runningappyt.db.RunDao
import javax.inject.Inject

class MainRepository@Inject constructor(
    val runDao:RunDao) {

    suspend fun insertRun(run:Run)=runDao.insertRun(run)

    suspend fun deleteRun(run:Run)=runDao.deleteRun(run)

    fun getAllRunsSortedByTimeStamp()=runDao.getAllRunsSortedByTimestamp()

    fun getAllRunsSortedByDistance()=runDao.getAllRunsSortedByDistanceInMetres()
    fun getAllRunsSortedByCaloriesBurnt()=runDao.getAllRunsSortedByCaloriesBurnt()
    fun getAllRunsSortedByAvgSpeed()=runDao.getAllRunsSortedByAvgSpeedInKMH()
    fun getAllRunsSortedByTimeInMillis()=runDao.getAllRunsSortedByTimeInMillis()

    fun getTotalDistance()=runDao.getTotalDistance()
    fun getTotalCaloriesBurnt()=runDao.getTotalCaloriesBurnt()
    fun getTotalTimeSpent()=runDao.getTotalTimeInMillis()
    fun getTotalAvgSpeed()=runDao.getTotalAvgSpeed()


}