package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class FilePDF(private val context: Context) {

    private lateinit var mPdfRenderer: PdfRenderer
    private lateinit var mPdfPage: PdfRenderer.Page

    //@SuppressLint("SuspiciousIndentation")
    fun openPdfWithAndroidSDK(pageNumber: Int, w:Int):Bitmap {
        // Copy sample.pdf from 'res/raw' folder into local cache so PdfRenderer can handle it
        val fileCopy = File(context.cacheDir, FILE_NAME)
        copyToLocalCache(fileCopy)

        // We will get a page from the PDF file by calling openPage
        val fileDescriptor = ParcelFileDescriptor.open(
            fileCopy,
            ParcelFileDescriptor.MODE_READ_WRITE
        )
        mPdfRenderer = PdfRenderer(fileDescriptor)
        mPdfPage = mPdfRenderer.openPage(pageNumber)

        Log.d("Sixe", "${mPdfPage.height}   ${mPdfPage.width}")
        // Create a new bitmap and render the page contents on to it

        val ratio = mPdfPage.height / mPdfPage.width.toFloat()
        val bitmap = Bitmap.createBitmap(
            w,
            (w * ratio ).toInt(),
            Bitmap.Config.ARGB_8888
        )

        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

        // Set the bitmap in the ImageView so we can view it
        Log.d("Sixe", "Bit:: ${bitmap.byteCount} ${mPdfPage.width} ${mPdfPage.height}")
        return bitmap
    }

    private fun copyToLocalCache(outputFile: File) {
        if (!outputFile.exists()) {

            val input: InputStream = context.resources.assets.open(FILE_NAME)
            val output = FileOutputStream(outputFile)

            val buffer = ByteArray(1024)
            var size: Int
            // Just copy the entire contents of the file
            while (input.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            input.close()
            output.close()

        }
    }
    companion object {
        const val FILE_NAME = "row.pdf"
    }
}