/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

        private var tonight = MutableLiveData<SleepNight?>()

        val nights = database.getAllNight()


        private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
        val navigateToSleepQuality : LiveData<SleepNight>
                get() = _navigateToSleepQuality

        private val _navigateToSleepDataQuality = MutableLiveData<Long>()
        val navigateToSleepDataQuality : LiveData<Long>
                get() = _navigateToSleepDataQuality

        fun onSLeepNightClicked(id: Long){
                _navigateToSleepDataQuality.value = id
        }
        fun onSleepDataQualityNavigated(){
                _navigateToSleepDataQuality.value = null
        }

        fun doneNavigating(){
                _navigateToSleepQuality.value = null
        }

        val startButtonVisible = Transformations.map(tonight){
                null == it
        }
        val stopButtonVisible = Transformations.map(tonight){
                null != it
        }
        val clearButtonVisible = Transformations.map(nights){
                it?.isNotEmpty()
        }

        private var _showSnackBarEvent = MutableLiveData<Boolean>()
        val showSnackBarEvent : LiveData<Boolean>
                get() = _showSnackBarEvent
        fun doneShowingSnackBar(){
                _showSnackBarEvent.value = false
        }
        init {
            initializeTonight()
        }

        private fun initializeTonight() {
                viewModelScope.launch {
                        tonight.value = getTonightFromDatabase()
                }
        }

        private suspend fun getTonightFromDatabase(): SleepNight? {
                var night = database.getTonight()
                if(night?.endTimeMilli != night?.startTimeMilli){
                        night = null
                }
                return night
        }

        fun onStartTracking(){
                viewModelScope.launch {
                        val newNight = SleepNight()
                        insert(newNight)
                        tonight.value = getTonightFromDatabase()

                }
        }

//      In Kotlin, the return@label syntax is used for specifying which function
//      among several nested ones this statement return from.
//     here we are specifying to return from launch() not the lambda
        fun onStopTracking(){

                viewModelScope.launch {
                        val oldNight = tonight.value ?: return@launch
                        oldNight.endTimeMilli = System.currentTimeMillis()
                        update(oldNight)
                        _navigateToSleepQuality.value = oldNight
                }
        }

        fun onClear(){
                viewModelScope.launch {
                        clear()
                        tonight.value = null
                        _showSnackBarEvent.value = true
                }
        }
        private suspend fun insert(newNight: SleepNight) {
                database.insert(newNight)
        }
        private suspend fun update(oldNight: SleepNight) {
                database.update(oldNight)
        }
        private suspend fun clear(){
                database.clear()
        }
        val nightString = Transformations.map(nights){ nights ->
                formatNights(nights, application.resources)
        }

}

