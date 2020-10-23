package com.example.walkway.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.walkway.R
import com.example.walkway.RetrofitGenerator.search
import com.example.walkway.model.search.SearchModel
import com.example.walkway.model.search.SearchResponse
import com.example.walkway.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_main_map.*
import kotlinx.android.synthetic.main.theme_select.view.*
import net.daum.android.map.coord.MapCoord
import net.daum.mf.map.api.MapCircle
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class MainMapActivity : AppCompatActivity(), MapView.POIItemEventListener {


    lateinit var mapView : MapView
    var SearchModel: SearchModel = SearchModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_map)

        mapView = MapView(this)

        mapView.setPOIItemEventListener(this)

        map_view.addView(mapView)

        getMyLocation()

        addMarker()

        //frame layout에서 view를 앞으로 가져오는 역할
        initView()

        themeBtn.setOnClickListener {
            themeBtn.isSelected = !themeBtn.isSelected
            showthemeAlert() // 다미 추가

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

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }

    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

        //여기에 마커 선택 했을 때 구현

        when(p1!!.itemName){

            "1" ->{
                Toast.makeText(applicationContext,"qwtqwt",Toast.LENGTH_SHORT).show()
            }
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


    fun addMarker(){


        val marker  = MapPOIItem()

        marker.itemName = "testMarker"
        marker.tag = 0




        if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(applicationContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED){

            val locationManager =  getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)


            val a = MapPoint.mapPointWithGeoCoord(location!!.latitude, location!!.longitude)
            marker.mapPoint = a

            marker.setMarkerType(MapPOIItem.MarkerType.BluePin)
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin)


            mapView.addPOIItem(marker);


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

    // 다미 테마 체크 박스
    private fun showthemeAlert(){
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.theme_select,null)
        val alertDialog = AlertDialog.Builder(this)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
        val theme_select = view.findViewById<Button>(R.id.btn_theme_select)

        var theme_entire_state = false
        var theme_night_state = false
        var theme_morning_state = false
        var theme_environment_state = false
        var theme_food_state = false
        var theme_animal_state = false
        var theme_date_state = false

        view.theme_entire.setOnClickListener{
            Toast.makeText(this, "전체 체크", Toast.LENGTH_SHORT).show()
            if(theme_entire_state == false){
                theme_entire_state= true
            }
            else{
                theme_entire_state= false
            }
        }
        view.theme_night.setOnClickListener{
            if(theme_night_state==false){
                theme_night_state = true

            }
            else{
                theme_night_state= false
            }
        }
        view.theme_morning.setOnClickListener{
            if(theme_morning_state==false){
                theme_morning_state = true

            }
            else{
                theme_morning_state= false
            }
        }
        view.theme_environment.setOnClickListener{
            if(theme_environment_state==false){
                theme_environment_state = true

            }
            else{
                theme_environment_state= false
            }
        }
        view.theme_food.setOnClickListener{
            if(theme_food_state==false){
                theme_food_state = true

            }
            else{
                theme_food_state= false
            }
        }
        view.theme_animal.setOnClickListener{
            if(theme_animal_state==false){
                theme_animal_state = true

            }
            else{
                theme_animal_state= false
            }
        }
        view.theme_date.setOnClickListener{
            if(theme_date_state==false){
                theme_date_state = true

            }
            else{
                theme_date_state= false
            }
        } // 코드 반복이 너무 심한데 더 간결하게 할 방법 없나..


        theme_select.setOnClickListener{
            //theme 선택한 값 api 전
            theme_select_walkway(
                theme_entire_state,
                theme_night_state,
                theme_morning_state,
                theme_environment_state,
                theme_food_state,
                theme_animal_state,
                theme_date_state)
            //api 호출된 값을 로컬에 저장도 해야함

            //가져온 지도의 출발점 목록을 핀으로 꼽는 프론트 함수 호출 필요함 (loop 돌려야함)


            //getStartPoint()

            //alertDialog.dismiss()

        }

    } // theme select 한 함수

    fun theme_select_walkway(
        theme_entire_state: Boolean,
        theme_night_state: Boolean,
        theme_morning_state: Boolean,
        theme_environment_state: Boolean,
        theme_food_state: Boolean,
        theme_animal_state: Boolean,
        theme_date_state: Boolean){

        SearchModel.Search(theme_entire_state,theme_night_state,theme_morning_state,theme_environment_state,theme_food_state,theme_animal_state,theme_date_state){
                isSuccess: Int, data: SearchResponse? ->
            // 이 안으로 진입이 안됨 여기에다가 toast 넣어도 안띄워짐
            if(data!!.statusCode==200){
                Toast.makeText(applicationContext,"서버가동성공",Toast.LENGTH_SHORT).show()
                // 안뜸
              Toast.makeText(applicationContext,data.body,Toast.LENGTH_SHORT).show()
                // 안뜸
            }else{
                Toast.makeText(applicationContext,"서버 오류입니다",Toast.LENGTH_SHORT).show()
            }

        }// theme_select_walkway 함수

        fun getStartPoint(){


        }


    }



}