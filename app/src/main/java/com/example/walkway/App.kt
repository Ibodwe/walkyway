package com.example.walkway

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.kakao.sdk.common.KakaoSdk

class App : Application() {



    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {

        super.onCreate()


        KakaoSdk.init(this, "3278e1057237737ede6f7b53202294a5")



    }
}