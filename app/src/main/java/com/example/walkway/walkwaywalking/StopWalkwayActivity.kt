package com.example.walkway.walkwaywalking

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.walkway.R

class StopWalkwayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_walkway)
    }
    
    // '산책을 중단하시겠습니까?' alert 창 띄우기
//    private fun showSettingPopup() {
//        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view = inflater.inflate(R.layout.alert_popup, null)
//        val textView: TestView = view.findViewById(R.id.textView)
//        textView.text = "산책을 중단하시겠습니까?"
//
//        val alertDialog = AlertDialog.Builder(this)
//            .setTitle("AlertDialog Exam")
//            .setPositiveButton("예") { dialog, which ->
//                Toast.makeText(applicationContext, "산책이 중단되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//            .setNeutralButton("아니오", null)
//            .create()
//
//        alertDialog.setView(view)
//        alertDialog.show()
//    }

}