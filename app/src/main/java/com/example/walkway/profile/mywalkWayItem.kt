package com.example.walkway.profile


import androidx.recyclerview.widget.RecyclerView
import com.example.walkway.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.mywalkway_item.view.*

class mywalkWayItem(var name: String, var img : String) : Item<GroupieViewHolder>( ){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.myWalkwayName.setText(name)

    }


    override fun getLayout(): Int {

       return R.layout.mywalkway_item
    }
}