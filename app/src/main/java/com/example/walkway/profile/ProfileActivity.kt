package com.example.walkway.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.walkway.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)



        myWalkwayRecyclerview.adapter = adapter
        myWalkwayRecyclerview.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        adapter.add(mywalkWayItem("오전의 중랑천","123"))


    }


}