package com.insbaixcamp.game.ui.wordle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WordleViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Wordle Fragment"
    }
    val text: LiveData<String> = _text
}