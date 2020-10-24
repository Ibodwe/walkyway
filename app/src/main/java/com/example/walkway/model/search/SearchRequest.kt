package com.example.walkway.model.search

data class SearchRequest(
    var theme_entire: Boolean,
    var theme_night: Boolean,
    var theme_morning: Boolean,
    var theme_environment: Boolean,
    var theme_food: Boolean,
    var theme_animal: Boolean,
    var theme_date: Boolean
)