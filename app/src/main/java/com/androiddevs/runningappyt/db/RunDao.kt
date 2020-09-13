package com.androiddevs.runningappyt.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDao {

    //Normal CRUD Operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)


    //Selecting and sorting in different ways
    @Query("SELECT * FROM running_table ORDER BY timestamp DESC")
    fun getAllRunsSortedByTimestamp():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis DESC")
    fun getAllRunsSortedByTimeInMillis():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurnt DESC")
    fun getAllRunsSortedByCaloriesBurnt():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH DESC")
    fun getAllRunsSortedByAvgSpeedInKMH():LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceInMetres DESC")
    fun getAllRunsSortedByDistanceInMetres():LiveData<List<Run>>


    //Getting Total of everything
    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun getTotalTimeInMillis():LiveData<Long>

    @Query("SELECT SUM(caloriesBurnt) FROM running_table")
    fun getTotalCaloriesBurnt():LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    fun getTotalAvgSpeed():LiveData<Float>

    @Query("SELECT SUM(distanceInMetres) FROM running_table")
    fun getTotalDistance():LiveData<Int>
}