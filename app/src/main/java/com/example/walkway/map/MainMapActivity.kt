package com.example.walkway.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.walkway.R
import com.example.walkway.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_main_map.*
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MainMapActivity : AppCompatActivity() {


    lateinit var mapView : MapView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        mapView = MapView(this)


        map_view.addView(mapView)

        getMyLocation()


        //frame layout에서 view를 앞으로 가져오는 역할
        initView()

        themeBtn.setOnClickListener {
            themeBtn.isSelected = !themeBtn.isSelected

        }

        distanceBtn.setOnClickListener {
            distanceBtn.isSelected = !distanceBtn.isSelected

        }

        hamburgerBtn.setOnClickListener {

            if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.openDrawer(Gravity.LEFT) ;
            }

        }

        myProfile.setOnClickListener{

            drawer.closeDrawer(Gravity.LEFT)
            startActivity(Intent(this, ProfileActivity::class.java))

        }



    }




    //현재위치 가져오기
    fun getMyLocation() {

        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED){

            val locationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location!!.latitude, location.longitude), true);

            mapView.setZoomLevel(1, true);

            mapView.currentLocationTrackingMode

            //지도에 점 찍기
            val mapPoint = MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude)
            val mpaCircle = MapCircle(mapPoint,3,R.color.browser_actions_divider_color,R.color.colorAccent)
            mapView.addCircle(mpaCircle)

        }else{

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.ACCESS_COARSE_LOCATION), 200)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==200 ){

            getMyLocation()
        }

    }

    private fun initView(){

        hamburgerBtn.bringToFront()
        currentBtn.bringToFront()
        themeBtn.bringToFront()
        distanceBtn.bringToFront()

    }


}