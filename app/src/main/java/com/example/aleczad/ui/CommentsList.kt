package com.example.aleczad.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class CommentsList : ViewModel()
{
    val login = MutableLiveData<String>()
    val date = MutableLiveData<String>()
    val comment = MutableLiveData<String>()

    fun login(item: String) {
        login.value = item
    }
    fun date(item: String) {
        date.value = item
    }
    fun comment(item: String) {
        comment.value = item
    }
}