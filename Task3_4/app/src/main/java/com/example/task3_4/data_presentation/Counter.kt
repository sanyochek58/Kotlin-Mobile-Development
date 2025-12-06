package com.example.task3_4.data_presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class Counter: ViewModel() {

    var counter: MutableState<String> = mutableStateOf("0")

    fun onCheckCount(count: String){
        counter.value = count
        if(counter.value != ""){
            counter.value = addValue(value = count.toInt() + 1)
        }else{
            counter.value = "No value"
        }
    }

    fun addValue(value: Int): String{
        counter.value = value.toString();
        return counter.value;
    }
}