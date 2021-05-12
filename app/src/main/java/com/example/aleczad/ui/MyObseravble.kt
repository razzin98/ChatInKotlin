package com.example.aleczad.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class MyObseravble : ViewModel()
{
    val data = MutableLiveData<String>()

    fun data(item: String) {
        data.value = item
    }

}