package com.androiddevs.runningappyt.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.androiddevs.runningappyt.db.RunningDatabase
import com.androiddevs.runningappyt.other.Constants.KEY_FIRST_TIME_TOGGLE
import com.androiddevs.runningappyt.other.Constants.KEY_NAME
import com.androiddevs.runningappyt.other.Constants.KEY_WEIGhT
import com.androiddevs.runningappyt.other.Constants.RUNNING_DATABASE_NAME
import com.androiddevs.runningappyt.other.Constants.SHARED_PREFERNCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(@ApplicationContext app:Context)=Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        RUNNING_DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideRunDao(db:RunningDatabase)= db.getRunDao()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app:Context)=
        app.getSharedPreferences(SHARED_PREFERNCES_NAME,MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPreferences: SharedPreferences)=sharedPreferences.getString(KEY_NAME,"")?:""


    @Singleton
    @Provides
    fun provideWeight(sharedPreferences: SharedPreferences)=sharedPreferences.getFloat(KEY_WEIGhT,75f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences)=sharedPreferences.getBoolean(
        KEY_FIRST_TIME_TOGGLE,true)

    //fun provideImage(sharedPreferences: SharedPreferences)=sharedPreferences.get






}