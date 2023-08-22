package com.example.myapplication.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class PdfUtils(private val context: Context) {

    private lateinit var renderer: PdfRenderer

    fun openPdf(page:Int):Bitmap? {

        try {
            val file = File("${context.cacheDir}/row.pdf")
            if (!file.exists()) {
                file.delete()
                val input = context.assets.open("row.pdf")
                val outputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                input.close()
                outputStream.close()
            }

            val fd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(fd)
            val openPage = renderer.openPage(page)
            val screen = context.resources.displayMetrics.widthPixels
            val sc = (openPage.height / openPage.width.toFloat()) * screen
            val bitmap = Bitmap.createBitmap(screen, sc.toInt(), Bitmap.Config.ARGB_8888)
            val bm = Bitmap.createBitmap(screen, sc.toInt(), Bitmap.Config.ARGB_8888)
            val canvas = Canvas()
            canvas.setBitmap(bitmap)
            val filter = ColorMatrixColorFilter(
                floatArrayOf(
                    -1f, 0.05f, 0f, 0f, 1f,
                    0f, -1f, 0f, 0f, 28f,
                    0f, 0f, -1f, 0f, 45f,
                    0f, 0f, 0f, 0.84f, 0.8f
                )
            )
            val paint = Paint().apply {
                colorFilter = filter
            }
            openPage.render(bm, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            canvas.drawColor(Color.WHITE)
            canvas.drawBitmap(bm, 0f, 0f, paint)
            openPage.close()
            renderer.close()
            return bitmap

        } catch (e: Exception) {
//            e.stackTrace
            Log.d("PDF utils", "error bitmap : ${e.message}")
            return null
        }
    }
}