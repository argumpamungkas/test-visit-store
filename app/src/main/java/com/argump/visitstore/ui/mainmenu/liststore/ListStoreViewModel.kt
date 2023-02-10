package com.argump.visitstore.ui.mainmenu.liststore

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.argump.visitstore.util.Constant
import com.argump.visitstore.model.DataStore
import com.argump.visitstore.persistence.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.dsl.module

val moduleListStoreViewModel = module {
    factory { ListStoreViewModel(get()) }
}

class ListStoreViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val sharedPref = SharedPreferences(application)

    fun getListStore(): ArrayList<DataStore> {
//        val getListStore = sharedPref.getStore(Constant.STORE)
//        val gson = Gson()
//        val type = object : TypeToken<ArrayList<JsonObject>>() {}.type
//        val jsonArrayList = gson.fromJson<ArrayList<JsonObject>>(getListStore, type)
//        val dataStoreList = ArrayList<DataStore>()
//        for (data in jsonArrayList) {
//
//            val dataStore = gson.fromJson(data, DataStore::class.java)
//            dataStoreList.add(dataStore)
//        }

        val getListStore = sharedPref.getStore(Constant.STORE)

        val typeJsonStore = object : TypeToken<ArrayList<DataStore>>() {}.type
        val dataStoreList = Gson().fromJson<ArrayList<DataStore>>(getListStore, typeJsonStore)
        return dataStoreList



    }
}