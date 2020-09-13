package com.androiddevs.runningappyt.db

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class Run(

    //Bitmap is a complex Object
    var img:Bitmap?=null,
    //you have to create a TypeConverter for that
    var timestamp:Long=0L,
    var avgSpeedInKMH:Float=0f,
    var distanceInMetres:Int=0,
    var caloriesBurnt:Int=0,
    var timeInMillis:Long=0L


) {
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}