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

package com.example.android.marsrealestate.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET

//Retrofit is a library that creates a network API for our APP
//Based on the content from our web service
//It fetches data from our web service and route it through a separate converter library
//That knows how to decode data and return it in the form of useful objects
private const val BASE_URL = "https://mars.udacity.com/"


//Scalar converter supports returning strings and other primitive types
private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService{
//    Retrofit append the endpoint realestate the base url and creates a call object
//    The call object is used to start the request
    @GET("realestate")
    fun getProperties():
            Call<String>
}

// The create call is expensive and our app needs only one service instance we implement
// it using a public object, call this object will return a retrofit object that implement MarsAPIservice
object MarsApi{
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}