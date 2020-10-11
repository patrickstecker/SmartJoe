package com.patrickstecker.alarmnotificator.models

class Lecture (
    val name: String,
    val times: Times,
    val date: String,
    val details: ArrayList<String> = ArrayList(),
    val isLecture: Boolean
)

class Times (
    val endMin: Int,
    val endHour: Int,
    val beginMin: Int,
    val beginHour: Int
)