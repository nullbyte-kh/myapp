package com.example.myapplication.pdf

import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter

object ColorUtils {
      fun getFilter(isDark:Boolean):ColorMatrixColorFilter {
          return if(isDark){
              val colorMatrixInverted = ColorMatrix(
                  floatArrayOf(
                      0f, 0f, 0f, 0f, 255f,
                      0f, 0f, 0f, 0f, 255f,
                      0f, 0f, 0f, 0f, 255f,
                      0f, 0f, 0.1f, 0.8f, 0.8f
                  )
              )
              ColorMatrixColorFilter(colorMatrixInverted)
      }else{
              val colorMatrixInverted = ColorMatrix(
                  floatArrayOf(
                      -1f, 0.05f, 0f, 0f, 1f,
                      0f, -1f, 0f, 0f, 28f,
                      0f, 0f, -1f, 0f, 45f,
                      0f, 0f, 0f, 1f, 1f
                  )
              )
              ColorMatrixColorFilter(colorMatrixInverted)
          }
      }

    fun getTextColor(isDark: Boolean):Int {
        return if (isDark){
            Color.argb(200, 250, 245, 250)
        }else{
            Color.argb(200, 33, 34, 39)

        }

    }
}