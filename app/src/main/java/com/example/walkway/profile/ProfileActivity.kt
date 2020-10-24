package com.example.walkway.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.walkway.R
import com.example.walkway.upload.ImgUploadActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.mywalkway_item.view.*

class ProfileActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        imgUpload.setOnClickListener {
            //check()
            val intent = Intent(this, ImgUploadActivity::class.java)
            startActivity(intent)
        }


        Glide.with(applicationContext).load("https://serverless-img-bucket.s3.ap-northeast-2.amazonaws.com/account_profile/na.png").into(userProfile)
        myWalkwayRecyclerview.adapter = adapter

        myWalkwayRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.add(mywalkWayItem("한강","https://serverless-img-bucket.s3.ap-northeast-2.amazonaws.com/walkway_img/han.png"))
        adapter.add(mywalkWayItem("봉은사","https://serverless-img-bucket.s3.ap-northeast-2.amazonaws.com/walkway_img/bong.png"))
        adapter.add(mywalkWayItem("코엑스","https://serverless-img-bucket.s3.ap-northeast-2.amazonaws.com/walkway_img/bong.png"))



    }

}