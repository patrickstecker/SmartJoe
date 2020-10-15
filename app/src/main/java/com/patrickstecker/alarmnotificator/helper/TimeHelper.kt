package com.patrickstecker.alarmnotificator.helper

import java.text.DateFormatSymbols
import java.time.LocalDate
import java.util.*

class TimeHelper {

    fun formatTime(hours: Int, mins: Int): String {
        var h: String = hours.toString()
        var m: String = mins.toString()
        if (h.length < 2) {
            h = "0$h"
        }
        if (m.length < 2) {
            m = "0$m"
        }
        return "$h:$m"
    }

    fun getTodayWeekDay(): String {
        val cal = Calendar.getInstance()
        val day = cal.get(Calendar.DAY_OF_WEEK)
        val days = DateFormatSymbols(Locale.getDefault()).weekdays
        return days[day]
    }

    fun getTodayDate(): String {
        val date = LocalDate.now()
        val day = date.dayOfMonth
        val month = date.month.value
        val year = date.year
        return "$day.$month.$year"
    }

    fun getTomorrowDate(): String {
        val date = LocalDate.now().plusDays(1)
        val day = date.dayOfMonth
        val month = date.month.value
        val year = date.year
        return "$day.$month.$year"
    }
}