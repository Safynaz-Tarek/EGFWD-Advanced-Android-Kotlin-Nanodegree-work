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

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//We have only one table so we only pass the sleepNight class in the list
//if we had more we will add them to the list
//Version starts at One & when we update the schema we update the number
//ExportSchema saves the schema of a the DB to a folder, good for complex DB that change often as it will provide for version history
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase: RoomDatabase(){
  abstract val sleepDatabaseDao: SleepDatabaseDao

//  the Companion object allows clients to access the methods for creating
//  or getting the DB without instantiating the class

  companion object{

//      Var is volatitle to make sure the value of Instance is always up to date and the same to all execution threads
//      The value of a volatile var will never be cached & all writes and reads will be done from main memory
//      which means that changes made by one thread to instance are visiable to all other threads immediately

      @Volatile
      private var INSTANCE: SleepDatabase? = null

      fun getInstance(context: Context) : SleepDatabase{
          synchronized(this){
              var instance = INSTANCE
              if(instance == null){
                  instance = Room.databaseBuilder(
                      context.applicationContext,
                      SleepDatabase::class.java,
                      "sleep_history_database"
                  ).fallbackToDestructiveMigration()
                      .build()
//        Migration means if we change DB schema for ex by changing a number or type of columns,
//        we need a way to convert the existing tables and data into new schema, migration object is an object the defines how you take
//        all rows with your old schema and convert them to rows in the new schema so if we upgrade from one version of our app with one DB
//        schema to a newer version of the app with a newer DB schema their data is not lost
//        here we wipe & rebuild the DB instead of migrating
                  INSTANCE = instance
              }
              return instance
          }
      }

  }
}
