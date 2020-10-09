package com.patrickstecker.alarmnotificator

import com.patrickstecker.alarmnotificator.models.Lecture
import com.patrickstecker.alarmnotificator.models.Times
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.LocalDate
import java.util.*

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

    private fun convertElementToLecture(element: Element, date: String): Lecture {
        val title = element.getElementsByClass("cal-title")?.first()?.text()
        val times = getLectureTimes(element.getElementsByClass("cal-time").first().text())
        return Lecture(title!!, times, date, true)
    }

    fun getFirstClassOfToday(dayOffset: Long): Lecture {
        val doc: Document = getPlan()
        val date = if (dayOffset > 0) LocalDate.now().plusDays(dayOffset) else LocalDate.now()

        val d = if (date.dayOfMonth < 10) "0" + date.dayOfMonth else "" + date.dayOfMonth //if (date.day < 10) "0" + date.day else "" + date.day
        val m = if (date.monthValue < 10) "0" + date.monthValue else "" + date.monthValue //if (date.month < 10) "0" + date.day else "" + date.day

        val week: Elements = doc.getElementsByAttributeValue("data-role", "listview")
        var next = false
        for (elem: Element in week) {
            for (child: Element in elem.children()) {
                if (next) {
                    return convertElementToLecture(elem, "$d.$m.")
                }
                if (elem.text().contains(", $d.$m", true)) {
                    next = true
                }
            }
        }
        return Lecture("Am $d.$m. findet keine Vorlesung statt", Times(0, 0, 0, 0), "$d.$m.", false)
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