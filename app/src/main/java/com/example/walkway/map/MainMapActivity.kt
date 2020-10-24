package com.example.walkway.map

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.walkway.R
import com.example.walkway.model.search.SearchModel
import com.example.walkway.model.search.SearchResponse
import com.example.walkway.profile.ProfileActivity
import com.example.walkway.walkingwalkway.StartWalkwayActivity
import kotlinx.android.synthetic.main.activity_main_map.*
import kotlinx.android.synthetic.main.theme_select.view.*
import net.daum.mf.map.api.*

class MainMapActivity() : AppCompatActivity(), MapView.CurrentLocationEventListener,
    MapView.MapViewEventListener {

    var SearchModel: SearchModel = SearchModel(this)

    private val LOG_TAG = "MainMapActivity"
    private var mapView: MapView? = null
    private var mapViewContainer: ViewGroup? = null
    var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map)
        //지도를 띄우자
        // java code
        mapView = MapView(this)
        mapViewContainer = findViewById<View>(R.id.map_view) as ViewGroup
        mapViewContainer!!.addView(mapView)
        mapView!!.setMapViewEventListener(this)
        mapView!!.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading)
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
        val button = findViewById<View>(R.id.startBtn) as Button /*페이지 전환버튼*/
        button.setOnClickListener {
            val intent = Intent(applicationContext, StartWalkwayActivity::class.java)
            startActivityForResult(intent, sub) //액티비티 띄우기
        }
        initView()
        startBtn.setVisibility(View.INVISIBLE);
        stopBtn.setVisibility(View.INVISIBLE);


        // 테마 버튼 클릭
        themeBtn.setOnClickListener {
            themeBtn.isSelected = !themeBtn.isSelected
            showthemeAlert()
        }

        hanBtn.setOnClickListener {
            hanBtn.isSelected = !hanBtn.isSelected
            showhanAlert()

        }

        foodBtn.setOnClickListener {
            foodBtn.isSelected = !foodBtn.isSelected
            showthemeAlert()
        }
        // 거리 버튼 클릭
        // 원래는 핀 버튼 클릭 시 진행해야하는 과정을 거리 버튼으로 대신함
        distanceBtn.setOnClickListener {
            distanceBtn.isSelected = !distanceBtn.isSelected

            //check()


//
//            btn1 = (Button) findbyViewId(R.id.button1); // 버튼이 btn1 이라면,
//            bt1.setVisibility(View.VISIBLE); // 화면에 보이게 한다.

            themeBtn.setVisibility(View.INVISIBLE); // 화면에 안보이게 한다.
            distanceBtn.setVisibility(View.INVISIBLE);
            val view = layoutInflater.inflate(R.layout.activity_start_walkway, null)
            val startbtn = view.findViewById<Button>(R.id.start)

            startBtn.setVisibility(View.VISIBLE);
            startBtn.setOnClickListener() {
                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.VISIBLE);
            }
        }

        // 산책 중단 버튼을 누르면 "산책을 중단하시겠습니까?" alert 창이 뜬다.
        stopBtn.setOnClickListener {
            showStarRangeAlert()
        }

        hamburgerBtn.setOnClickListener {
            if (!drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.openDrawer(Gravity.LEFT);
            }
        }
        myProfile.setOnClickListener {
            drawer.closeDrawer(Gravity.LEFT)
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViewContainer!!.removeAllViews()
    }

    override fun onCurrentLocationUpdate(
        mapView: MapView?,
        currentLocation: MapPoint,
        accuracyInMeters: Float
    ) {
        val mapPointGeo: MapPoint.GeoCoordinate = currentLocation.getMapPointGeoCoord()
        Log.i(
            LOG_TAG,
            java.lang.String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo.latitude,
                mapPointGeo.longitude,
                accuracyInMeters
            )
        )
    }

    override fun onCurrentLocationDeviceHeadingUpdate(mapView: MapView?, v: Float) {}
    override fun onCurrentLocationUpdateFailed(mapView: MapView?) {}
    override fun onCurrentLocationUpdateCancelled(mapView: MapView?) {}
    private fun onFinishReverseGeoCoding(result: String) {
//        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }

    // ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.size == REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            var check_result = true

            // 모든 퍼미션을 허용했는지 체크합니다.
            for (result: Int in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                Log.d("@@@", "start")
                //위치 값을 가져올 수 있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있다
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                ) {
                    Toast.makeText(
                        this@MainMapActivity,
                        "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this@MainMapActivity,
                        "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun checkRunTimePermission() {

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainMapActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)
            // 3.  위치 값을 가져올 수 있음
        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.
            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainMapActivity,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {
                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(
                    this@MainMapActivity,
                    "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                    Toast.LENGTH_LONG
                ).show()
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MainMapActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(
                    this@MainMapActivity, REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this@MainMapActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage(
            "앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                    + "위치 설정을 수정하시겠습니까?"
        )
        builder.setCancelable(true)
        builder.setPositiveButton("설정") { dialog, id ->
            val callGPSSettingIntent =
                Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(
                callGPSSettingIntent,
                GPS_ENABLE_REQUEST_CODE
            )
        }
        builder.setNegativeButton(
            "취소"
        ) { dialog, id -> dialog.cancel() }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->                 //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
    }

    fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }

    override fun onMapViewInitialized(mapView: MapView?) {}
    override fun onMapViewCenterPointMoved(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewZoomLevelChanged(mapView: MapView?, i: Int) {}
    override fun onMapViewSingleTapped(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewDoubleTapped(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewLongPressed(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewDragStarted(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewDragEnded(mapView: MapView?, mapPoint: MapPoint?) {}
    override fun onMapViewMoveFinished(mapView: MapView?, mapPoint: MapPoint?) {}

    companion object {
        val sub = 1001 /*다른 액티비티를 띄우기 위한 요청코드(상수)*/
        private val GPS_ENABLE_REQUEST_CODE = 2001
        private val PERMISSIONS_REQUEST_CODE = 100
    }

    // 다미 테마 체크 박스
    private fun showthemeAlert() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.theme_select, null)
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

        view.theme_entire.setOnClickListener {
            if (theme_entire_state == false) {
                theme_entire_state = true
            } else {
                theme_entire_state = false
            }
        }
        view.theme_night.setOnClickListener {
            if (theme_night_state == false) {
                theme_night_state = true

            } else {
                theme_night_state = false
            }
        }
        view.theme_morning.setOnClickListener {
            if (theme_morning_state == false) {
                theme_morning_state = true

            } else {
                theme_morning_state = false
            }
        }
        view.theme_environment.setOnClickListener {
            if (theme_environment_state == false) {
                theme_environment_state = true

            } else {
                theme_environment_state = false
            }
        }
        view.theme_food.setOnClickListener {
            if (theme_food_state == false) {
                theme_food_state = true

            } else {
                theme_food_state = false
            }
        }
        view.theme_animal.setOnClickListener {
            if (theme_animal_state == false) {
                theme_animal_state = true

            } else {
                theme_animal_state = false
            }
        }
        view.theme_date.setOnClickListener {
            if (theme_date_state == false) {
                theme_date_state = true

            } else {
                theme_date_state = false
            }
        } // 코드 반복이 너무 심한데 더 간결하게 할 방법 없나..


        theme_select.setOnClickListener {
            //theme 선택한 값 api 전
            theme_select_walkway(
                theme_entire_state,
                theme_night_state,
                theme_morning_state,
                theme_environment_state,
                theme_food_state,
                theme_animal_state,
                theme_date_state
            )
            //api 호출된 값을 로컬에 저장도 해야함

            //가져온 지도의 출발점 목록을 핀으로 꼽는 프론트 함수 호출 필요함 (loop 돌려야함)


            getStartPoint1()
            getStartPoint2()
            getStartPoint3()

            alertDialog.dismiss()
            themeBtn.setVisibility(View.INVISIBLE);
            distanceBtn.setVisibility(View.INVISIBLE);
        }

    } // 다미 theme select 한 함수

    private fun showhanAlert() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_choose_walkway, null)
        val alertDialog = AlertDialog.Builder(this)
            .create()
        alertDialog.setView(view)
        alertDialog.show()
        val han_select = view.findViewById<Button>(R.id.hanChooseBtn)


        han_select.setOnClickListener {
            getWalkwayPath_1()
            hanBtn.isSelected = !hanBtn.isSelected
            //check()

            alertDialog.dismiss()

//
//            btn1 = (Button) findbyViewId(R.id.button1); // 버튼이 btn1 이라면,
//            bt1.setVisibility(View.VISIBLE); // 화면에 보이게 한다.

            foodBtn.setVisibility(View.INVISIBLE); // 화면에 안보이게 한다.
            hanBtn.setVisibility(View.INVISIBLE);
            val view = layoutInflater.inflate(R.layout.activity_start_walkway, null)
            val startbtn = view.findViewById<Button>(R.id.start)

            startBtn.setVisibility(View.VISIBLE);
            startBtn.setOnClickListener() {
                startBtn.setVisibility(View.INVISIBLE);
                stopBtn.setVisibility(View.VISIBLE);
            }


        }

    } // 다미 theme select 한 함수

    //다미 back-end 테마 lambda
    fun theme_select_walkway(
        theme_entire_state: Boolean,
        theme_night_state: Boolean,
        theme_morning_state: Boolean,
        theme_environment_state: Boolean,
        theme_food_state: Boolean,
        theme_animal_state: Boolean,
        theme_date_state: Boolean
    ) {

        SearchModel.Search(
            theme_entire_state,
            theme_night_state,
            theme_morning_state,
            theme_environment_state,
            theme_food_state,
            theme_animal_state,
            theme_date_state
        ) { isSuccess: Int, data: SearchResponse? ->
            // 이 안으로 진입이 안됨 여기에다가 toast 넣어도 안띄워짐
            if (data!!.statusCode == 200) {
                Toast.makeText(applicationContext, "서버가동성공", Toast.LENGTH_SHORT).show()
                // 안뜸
                //Toast.makeText(applicationContext,data.body,Toast.LENGTH_SHORT).show()
                // 안뜸
            } else {
                Toast.makeText(applicationContext, "서버 오류입니다", Toast.LENGTH_SHORT).show()
            }

        }// theme_select_walkway 함수

        fun getStartPoint() {


        }


    }
    //다미 back-end 테마 lambda


    // 산책로 출발 지점 가져오기
    fun getStartPoint1() = if (ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_DENIED
        && ActivityCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_DENIED
    ) {

        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        val MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.510363, 127.067945)
        //                //37.546363, 127.062802
        val marker = MapPOIItem()
        marker.itemName = "한강"
        marker.tag = 0
        marker.mapPoint = MARKER_POINT
        marker.markerType = MapPOIItem.MarkerType.YellowPin // 기본으로 제공하는 BluePin 마커 모양.

        marker.selectedMarkerType =
            MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView?.addPOIItem(marker)

    } else {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 200
        )
    }

    fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
        //여기에 마커 선택 했을 때 구현
        when (p1!!.itemName) {

            "1" -> {
                Toast.makeText(applicationContext, "qwtqwt", Toast.LENGTH_SHORT).show()
            }
        }

    }

    // 산책로 경로 가져오기
    fun getStartPoint2() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) {

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_DENIED
                && ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_DENIED
            ) {

                val MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.514238, 127.059948)
                //                //37.546363, 127.062802

                val marker = MapPOIItem()
                marker.itemName = "봉은사"
                marker.tag = 0
                marker.mapPoint = MARKER_POINT
                marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

                marker.selectedMarkerType =
                    MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
                marker.selectedMarkerType =
                    MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

                mapView?.addPOIItem(marker)


            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 200
                )
            }
        }
    }

    fun getStartPoint3() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) {

            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            val MARKER_POINT = MapPoint.mapPointWithGeoCoord(37.509355, 127.058400)
            //                //37.546363, 127.062802

            val marker = MapPOIItem()
            marker.itemName = "맛집"
            marker.tag = 0
            marker.mapPoint = MARKER_POINT
            marker.markerType = MapPOIItem.MarkerType.BluePin // 기본으로 제공하는 BluePin 마커 모양.

            marker.selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.selectedMarkerType =
                MapPOIItem.MarkerType.RedPin // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            mapView?.addPOIItem(marker)


        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
        }
    }

    fun getWalkwayPath_1() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) {

            val polyline = MapPolyline()
            polyline.tag = 1000
            polyline.lineColor = Color.argb(128, 51, 51, 204) // Polyline 컬러 지정

            // Polyline 좌표 지정
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.510363, 127.067945))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.512520, 127.067714))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514058, 127.067484))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.515384, 127.067241))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.515689, 127.069724))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514127, 127.069977))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.513347, 127.070130))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.511716, 127.070382))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.510701, 127.070417))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.510363, 127.067945))


            // Polyline 지도에 올리기
            mapView?.addPolyline(polyline)

            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
            val mapPointBounds = MapPointBounds(polyline.mapPoints)
            val padding = 100 // px

            mapView?.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
        }

    }


    // 코엑스 근처 두번째 산책로 경로 가져오기
    fun getWalkwayPath_2() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) {

            val polyline = MapPolyline()
            polyline.tag = 1000
            polyline.lineColor = Color.argb(128, 51, 51, 204) // Polyline 컬러 지정

            // Polyline 좌표 지정
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514238, 127.059948))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.513928, 127.058094))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514120, 127.058024))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.513938, 127.056857))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514110, 127.056831))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514485, 127.057259))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.514525, 127.057833))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.515006, 127.057763))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.515122, 127.057928))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.515361, 127.057740))


            // Polyline 지도에 올리기
            mapView?.addPolyline(polyline)

            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
            val mapPointBounds = MapPointBounds(polyline.mapPoints)
            val padding = 100 // px

            mapView?.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
        }

    }

    // 산책로 세번째 경로 가져오기
    fun getWalkwayPath_3() {

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
            && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_DENIED
        ) {

            val polyline = MapPolyline()
            polyline.tag = 1000
            polyline.lineColor = Color.argb(128, 51, 51, 204) // Polyline 컬러 지정

            // Polyline 좌표 지정

            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.509355, 127.058400))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.509133, 127.057692))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.509023, 127.057305))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.509755, 127.056919))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.510324, 127.056702))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.511086, 127.056408))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.511185, 127.056789))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.511362, 127.057346))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.510835, 127.057672))
            polyline.addPoint(MapPoint.mapPointWithGeoCoord(37.509355, 127.058400))


            // Polyline 지도에 올리기
            mapView?.addPolyline(polyline)

            // 지도뷰의 중심좌표와 줌레벨을 Polyline이 모두 나오도록 조정
            val mapPointBounds = MapPointBounds(polyline.mapPoints)
            val padding = 100 // px

            mapView?.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))

        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 200
            )
        }

    }


    // 산책 중단 버튼을 누르면 산책로 별점 alert 창이 뜬다.
    private fun showStarRangeAlert() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.stopwalkway_star_alert, null)
        val textView: TextView = view.findViewById(R.id.stopwalkway_star_alert)
        textView.text = "산책로 별점"

        val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this)
            .create()

        // 별점 alert 창에서 '확인' 버튼을 누르면 메인 액티비티로 이동한다.
        val registerStarRange = view.findViewById<Button>(R.id.registerStarRange)
        registerStarRange.setOnClickListener {
            // 백엔드에서 별점 등록하는 API 호출해야함

            alertDialog.dismiss()
            stopBtn.setVisibility(View.INVISIBLE);
            themeBtn.setVisibility(View.VISIBLE);
        }



        alertDialog.setView(view)
        alertDialog.show()
    }


    private fun initView() {

        hamburgerBtn.bringToFront()
        currentBtn.bringToFront()
        themeBtn.bringToFront()
        distanceBtn.bringToFront()

    }
}
