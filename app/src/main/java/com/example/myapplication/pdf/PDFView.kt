package com.example.myapplication.pdf

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.example.myapplication.FilePDF


class PDFView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, restyle:Int = 0)
    : View(context, attributeSet, restyle){

        private var mWidth = 100
        private var pageNumber:Int? = null
        private var filter:ColorMatrixColorFilter? = null

    private val textPaint = TextPaint().apply {
        isAntiAlias = true
        color = Color.argb(255, 113, 114, 120)
        textAlign = Paint.Align.CENTER
    }

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(mWidth, (mWidth * 1.648).toInt())
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (pageNumber != null){
            val bitmap = bindBitmap(pageNumber!!, width)
            paint.colorFilter = filter
            canvas!!.drawBitmap(bitmap, 0f, 0f, paint)
        }
        else{
            textPaint.textSize = width / 3f
            canvas!!.apply {
                drawColor(Color.GRAY)
                drawText("PDF", width/2f, height/1.8f, textPaint)
            }

        }


    }

    private fun bindBitmap(page:Int, width:Int):Bitmap {
        return FilePDF(context).openPdfWithAndroidSDK(page, width)
    }

   fun openPage(page:Int, isDark:Boolean){
        pageNumber = page
        filter = ColorUtils.getFilter(isDark)
        invalidate()
    }
}