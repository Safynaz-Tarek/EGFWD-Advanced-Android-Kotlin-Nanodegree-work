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
 *
 */

package com.example.android.devbyteviewer.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideosDatabase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

//Repostories are responsible for providing a simple API to our data sources
//In this repo, we need a videos DB so we pass it as a constructor param
//This repo is split into two parts, one part will load the videos from the offline cache
//and another to refresh the offline cache

class VideosRepository(private val database: VideosDatabase){

//    This is the property that everyone can use to observe videos from the repo
    val videos: LiveData<List<Video>> = Transformations.map(database.videoDao.getVideos()){
        it.asDomainModel()
}

    //This method refreshes the offline cache
    suspend fun refreshVideos(){
//        WithContext forces a kotlin coroutine to switch to the dispatcher specified
        withContext(Dispatchers.IO){
            try {
                val playlist = Network.devbytes.getPlaylist().await()
                database.videoDao.insertAll(*playlist.asDatabaseModel())
            }catch (e: Exception){
                Log.i("Rep","Failed to load")
            }

        }
    }
}