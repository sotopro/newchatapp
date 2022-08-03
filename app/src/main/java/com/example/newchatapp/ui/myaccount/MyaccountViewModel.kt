package com.example.newchatapp.ui.myaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyaccountViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my account Fragment"
    }
    val text: LiveData<String> = _text
}