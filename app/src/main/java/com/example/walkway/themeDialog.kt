package com.example.walkway


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class themeDialog : DialogFragment(){


    interface statusListenr{
        fun sendCode(code : Int)
    }

    lateinit var status: statusListenr

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return null
        //return inflater.inflate(R.layout.framgent_dialog_loading, container, false )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //2초동안 멈추고

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //initListener

    }




}