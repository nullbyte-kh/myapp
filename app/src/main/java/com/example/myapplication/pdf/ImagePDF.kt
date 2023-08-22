package com.example.myapplication.pdf

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ImagePDF @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, restyle:Int = 0): AppCompatImageView(context, attributeSet, restyle) {

    private var bitmap:Bitmap? = null

    init {
        bitmap = PdfUtils(context).openPdf(3)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
//        if (bitmap != null){
//            val value = floatArrayOf(
//                0f, 0f, 0f, 0f, 255f,
//                0f, 0f, 0f, 0f, 255f,
//                0f, 0f, 0f, 0f, 255f,
//                0f, 0f, 0f, 0.8f, 0.8f
//            )
//            val paint = Paint().apply {
//                color = Color.WHITE
//                val colorMatrix = ColorMatrix().apply {
//                    set(value)
////                    setScale(1f, 1f, 1f, 1f)
////                    setSaturation(0f)
//
//                }
//                colorFilter = ColorMatrixColorFilter(colorMatrix)
//            }
//
//            canvas?.drawBitmap(bitmap!!, 0f, 0f, paint)
//        }
        bindBitmap(canvas!!)
    }

    private fun bindBitmap(canvas : Canvas) {

        val value = floatArrayOf(
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0f, 255f,
            0f, 0f, 0f, 0.8f, 0.8f
        )
        val paint = Paint().apply {
            color = Color.WHITE
            val colorMatrix = ColorMatrix().apply {
                set(value)
//                    setScale(1f, 1f, 1f, 1f)
//                    setSaturation(0f)

            }
            colorFilter = ColorMatrixColorFilter(colorMatrix)
        }

        val find = Bitmap.createBitmap(canvas.width, canvas.height, Bitmap.Config.ARGB_8888)
        val canvass = Canvas()
        canvass.setBitmap(find)
        canvass.drawBitmap(find, 0f, 0f, paint)
        setImageBitmap(find)
    }
}