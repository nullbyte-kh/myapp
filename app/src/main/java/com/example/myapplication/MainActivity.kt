package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.NativeAd
import com.facebook.ads.NativeAdLayout
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var nativeAd: NativeAd
    private lateinit var native:NativeAdLayout
    private lateinit var customAdLayout: LinearLayout
    private lateinit var adView: View

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//Intent(this, MainActivity2::class.java).also {
//    startActivity(it)
//}
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            Intent(this@MainActivity, HomeScreen::class.java).also {
                startActivity(it)
                finish()
            }
        }
        else{
            MainScope().launch {
                delay(1000)
                Intent(this@MainActivity, HomeScreen::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }
}