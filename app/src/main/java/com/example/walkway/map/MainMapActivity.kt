package com.example.walkway.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walkway.R
import kotlinx.android.synthetic.main.activity_main_map.*
import net.daum.mf.map.api.MapView

class MainMapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)


        map_view.addView(MapView(this))



    }
}