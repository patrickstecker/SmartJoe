package com.patrickstecker.alarmnotificator

import com.patrickstecker.alarmnotificator.helper.doAsync
import com.patrickstecker.alarmnotificator.models.Lecture
import kotlin.collections.ArrayList

object Storage {
    private var lectures: Array<Array<Lecture>> = emptyArray()     // Lectures of current week and next week
    private var todayLectures: Array<Lecture> = emptyArray()
    private var tomorrowLectures: Array<Lecture> = emptyArray()

    fun getTodayLectures(): Array<Lecture>{
        return todayLectures
    }

    fun getTomorrowLectures(): Array<Lecture> {
        return tomorrowLectures
    }

    fun getLectures(): Array<Array<Lecture>> {
        return lectures
    }

    fun updateLectures() {
        doAsync{
            val analyzer = LecturePlanAnalyzer()
            todayLectures = analyzer.getClassesOfToday(0)
            tomorrowLectures = analyzer.getClassesOfToday(1)
        }.execute()
    }
}