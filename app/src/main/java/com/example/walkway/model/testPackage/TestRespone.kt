package com.example.walkway.model.testPackage


data class TestRespone(
    val body: Body,
    val statusCode: Int
)

data class Body(
    val message: List<Message>
)

data class Message(
    val account_profile: Any,
    val duration: Int,
    val walkway_id: Int,
    val walkway_image: String,
    val walkway_name: String,
    val walkway_point1: Any,
    val walkway_point2: Any,
    val walkway_range: Int
)
