package com.eegets.measureview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pieImageView.setProgress(45)

//        pieImageView.postDelayed({
//            val params = pieImageView.layoutParams
//            params.width = dpToPixel(200f, this).toInt()
//            params.height = dpToPixel(200f, this).toInt()
//            pieImageView.layoutParams = params
//
//        }, 1000)
    }
}