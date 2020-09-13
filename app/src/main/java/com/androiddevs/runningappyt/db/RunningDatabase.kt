package com.androiddevs.runningappyt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Run::class],version = 1)
@TypeConverters(Converters::class)
abstract class RunningDatabase:RoomDatabase() {

    abstract  fun getRunDao():RunDao


    //Singleton Functionality is not mentioned here
    //bcoz we will be using Dagger
    //which takes care of all
    //thats why its small compared to News app
}