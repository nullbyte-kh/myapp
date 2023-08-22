package com.example.myapplication.data

import android.util.Log
import com.example.myapplication.model.AdsViewModel
import com.example.myapplication.model.Content
import com.example.myapplication.model.PageContent

object ListUtils {

    fun generateList(from:Int, to:Int):ArrayList<Any> {
        val list = ArrayList<Any>()
        var position = 1
        var step = 2
        for (i in from..to) {
            if (position == step){
                list.add(Content(i))
                list.add(AdsViewModel())
                step += 2
            } else {
                list.add(Content(i))
            }
            position += 1
        }
        if((to - from + 1) % 2 == 1) {
            list.add(0)
            list.add(AdsViewModel())
        }
        return list
    }

    fun logAll() {
        val l = generateList(from = 0, to = 9)
        l.forEachIndexed{ i, v ->
            Log.d("List App", "Data is: $i , $v")
        }
    }

}