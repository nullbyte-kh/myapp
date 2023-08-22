package com.example.myapplication.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.data.DataManager
import com.example.myapplication.model.PageContent

class ContentModel: ViewModel() {

    var currentValue = MutableLiveData<PageContent>(DataManager.lesson[0])

    fun updateValue(pageContent: PageContent) {
        currentValue.value = pageContent
    }
}