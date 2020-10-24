package com.example.walkway.model.search

data class SearchResponse(
    val body: Body,  // String으로 변경시 서버가동 성공 되는데, Body로 바꾸면 logcat엔 나오지만 값을 못 불러옴
    val statusCode: Int
)

data class Body(
    val message: List<Message>
)

data class Message(
    val account_profile: String,
    val duration: Int,
    val walkway_id: Int,
    val walkway_image: String,
    val walkway_name: String,
    val walkway_point1: WalkwayPoint1,
    val walkway_point2: WalkwayPoint2,
    val walkway_range: Int
)

data class WalkwayPoint1(
    val x: Double,
    val y: Double
)

data class WalkwayPoint2(
    val x: Double,
    val y: Double
)


//class message : ArrayList<ddItem>()
//
//data class ddItem(
//    val bnusNo: Int,
//    val drwNo: Int,
//    val drwNoDate: String,
//    val drwtNo1: Int,
//    val drwtNo2: Int,
//    val drwtNo3: Int,
//    val drwtNo4: Int,
//    val drwtNo5: Int,
//    val drwtNo6: Int,
//    val firstAccumamnt: Int,
//    val firstPrzwnerCo: Int,
//    val firstWinamnt: Int,
//    val returnValue: String,
//    val totSellamnt: Long
//)
////[{}]
////