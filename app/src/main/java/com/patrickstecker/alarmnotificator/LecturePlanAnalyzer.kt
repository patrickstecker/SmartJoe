package com.patrickstecker.alarmnotificator

import com.patrickstecker.alarmnotificator.models.Lecture
import com.patrickstecker.alarmnotificator.models.Times
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class LecturePlanAnalyzer {
    companion object {
        const val URL = "https://vorlesungsplan.dhbw-mannheim.de/index.php?action=view&uid=7431001"
    }

    private fun getPlan(): Document {
        return Jsoup.connect(URL + calculateTimeStamp(1)).get()
    }

    private fun calculateTimeStamp(dayOffset: Int): String {
        val dayMillis = 24 * 60 * 60
        val time = Date().time / 1000 + (dayOffset) * dayMillis
        return "&date=$time"
    }

    private fun convertElementToLectures(element: Element, date: String): ArrayList<Lecture> {
        val lectures = ArrayList<Lecture>()
        var first = true
        for (item in element.getElementsByTag("li")) {
            if (first) {
                first = false
            } else {
                lectures.add(convertElementToLecture(item, date))
            }
        }
        return lectures
    }

    private fun convertElementToLecture(element: Element, date: String): Lecture {
        var title = element.getElementsByClass("cal-title").text()
        var times = getLectureTimes(element.getElementsByClass("cal-time").text())
        val details = ArrayList<String>()
        for (item in element.getElementsByTag("div")) {
            if (item.className() == "cal-title")
                title = item.text()
            else if (item.className() == "cal-time")
                times = getLectureTimes(item.text())
            else
                details.add(item.text())
        }
        return Lecture(title!!, times, date, details, true)
    }

    fun getFirstClassOfToday(dayOffset: Long): Lecture {
        val lecturesOfToday = getClassesOfToday(dayOffset)
        if (lecturesOfToday.isNotEmpty()) {

            return lecturesOfToday[0]

        } else {

            val date = if (dayOffset > 0) LocalDate.now().plusDays(dayOffset) else LocalDate.now()

            val d = if (date.dayOfMonth < 10) "0" + date.dayOfMonth else "" + date.dayOfMonth //if (date.day < 10) "0" + date.day else "" + date.day
            val m = if (date.monthValue < 10) "0" + date.monthValue else "" + date.monthValue //if (date.month < 10) "0" + date.day else "" + date.day

            return Lecture("Am $d.$m. findet keine Vorlesung statt", Times(0, 0, 0, 0), "$d.$m.", ArrayList(), false)

        }
    }

    fun getClassesOfToday(dayOffset: Long): Array<Lecture> {
        val doc = getPlan()
        val date = if (dayOffset > 0) LocalDate.now().plusDays(dayOffset) else LocalDate.now()
        var lectures = ArrayList<Lecture>()

        val d = if (date.dayOfMonth < 10) "0" + date.dayOfMonth else "" + date.dayOfMonth //if (date.day < 10) "0" + date.day else "" + date.day
        val m = if (date.monthValue < 10) "0" + date.monthValue else "" + date.monthValue //if (date.month < 10) "0" + date.day else "" + date.day

        var next = false
        val week: Elements = doc.getElementsByAttributeValue("data-role", "listview")
        for (elem: Element in week) {
            if(next) {
                break
            }
            for (child: Element in elem.children()) {
                if (next) {
                    lectures = convertElementToLectures(elem, "$d.$m.")
                }
                if (elem.text().contains(", $d.$m", true)) {
                    next = true
                }
            }
        }
        return lectures.toTypedArray()
    }

    fun getLectureTimes(times: String): Times {
        val time = times.split("-")

        val endMin = time[1].split(":")[1].toInt()
        val endHour = time[1].split(":")[0].toInt()
        val beginMin = time[0].split(":")[1].toInt()
        val beginHour = time[0].split(":")[0].toInt()

        return  Times(endMin, endHour, beginMin, beginHour)
    }

    fun tomorrowIsALesson(): Boolean {
        return getFirstClassOfToday(1).isLecture
    }
}