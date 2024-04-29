package com.dkproject.swingassignment.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="search_history")


class searchDataStore @Inject constructor(
    private val context: Context,
    private val gson: Gson
){
    companion object{
        private val SEARCH_KEY = stringPreferencesKey("search_history")
    }

    suspend fun setSearchHistory(searchList:List<String>){
         val searchData:String = gson.toJson(searchList)
        Log.d("setSearchHistory", searchData.toString())
        context.dataStore.edit {mutablePreferences ->
            mutablePreferences[SEARCH_KEY] = searchData
        }
    }

    fun getAllSearchHistory():Flow<List<String>>{
        return context.dataStore.data.map {preferences->
             gson.fromJson(preferences[SEARCH_KEY],object:TypeToken<List<String>>(){}.type)?: emptyList()
        }
    }


}