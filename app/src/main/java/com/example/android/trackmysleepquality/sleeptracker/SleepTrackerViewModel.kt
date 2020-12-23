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
import androidx.lifecycle.*
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application) {


    //variable to change and update tonight and also observe
    var tonight = MutableLiveData<SleepNight?>()

    //get all nights - ROOM takes care of updating this LiveData if anything changes. ROOM as mechanism to
    // getAllNights in the background
    val nights = database.getAllNights()

    val nightsString = Transformations.map(nights){nights -> formatNights(nights, application.resources)}

    private val _eventNavigateToSleepDetail = MutableLiveData<SleepNight>()
    val eventNavigateToSleepDetail: LiveData<SleepNight>
    get() = _eventNavigateToSleepDetail

    val startButtonVisible = Transformations.map(tonight){ tonight ->

        tonight ==null
    }
    val stopButtonVisible = Transformations.map(tonight){


        tonight ->

        tonight != null
    }
    val clearButtonVisible = Transformations.map(nights){


        it.isNotEmpty()
    }

    //logic for showing the snackbar
    private val _showSnackBarEvent = MutableLiveData<Boolean>()
            val showSnackBarEvent :LiveData<Boolean>
                               get() = _showSnackBarEvent

    fun onSnackBarEventFinished() {
        _showSnackBarEvent.value = false
    }

    init {
        //make tonight initialized as soon as possible so that we can use it

        initializeTonight()

    }

    private fun initializeTonight() {

        viewModelScope.launch {

            tonight.value = getTonightFromDatabase()
        }

    }

    private suspend fun getTonightFromDatabase(): SleepNight? {

        return withContext(IO) {


            var night = database.getTonight()

            if (night?.endTimeMilli != night?.startTimeMilli) {

                night = null
            }

            night
        }

    }


 fun onStartTracking() {

        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {

        withContext(IO) {

            database.insert(night)
        }
    }


    fun onStopTracking() {

        viewModelScope.launch {

            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _eventNavigateToSleepDetail.value = oldNight
        }



    }


    private suspend fun update(night: SleepNight) {

        withContext(IO) {

            database.update(night)
        }
    }


    fun onClear() {


        viewModelScope.launch {

            clear()
            tonight.value = null


        }

        _showSnackBarEvent.value = true
    }


    private suspend fun clear() {

        withContext(IO) {

            database.clear()
        }}



    fun navigationComplete()   {
    _eventNavigateToSleepDetail.value = null
    }


}

