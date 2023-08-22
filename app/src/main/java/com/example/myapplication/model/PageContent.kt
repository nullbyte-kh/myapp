package com.example.myapplication.model

data class PageContent(val title:String, val fromPage:Int, val toPage:Int, val type:Int) {
    companion object {
        const val CHAPTER = 0
        const val LESSON = 1
    }
}