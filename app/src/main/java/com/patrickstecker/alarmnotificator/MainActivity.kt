package com.patrickstecker.alarmnotificator

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.provider.AlarmClock
import android.webkit.WebView
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.patrickstecker.alarmnotificator.models.Lecture
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lecturePlanAnalyzer = LecturePlanAnalyzer()
        val show = findViewById<Button>(R.id.notify_btn)
        val textView: TextView = findViewById(R.id.textview)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)
        val deleteAlarmButton: Button = findViewById(R.id.deleteAlarms)
        val setAlarms: Button = findViewById(R.id.setAlarms)

        val alarm = Alarm()

        setAlarms.setOnClickListener {
            alarm.activateNotifications(this)
        }
        deleteAlarmButton.setOnClickListener {
            alarm.cancelAlarm(this)
        }
        show.setOnClickListener {
            alarm.fireTestAlarm(this)
        }

        doAsync {
            val lesson: Lecture = lecturePlanAnalyzer.getFirstClassOfToday(1)
            if (
                lesson.times.beginHour == 0 &&
                lesson.times.beginMin == 0 &&
                lesson.times.endHour == 0 &&
                lesson.times.endMin == 0
            ){
                runOnUiThread {
                    textView.text = lesson.name
                    btn1.text = "--:--"
                    btn2.text = "--:--"
                    btn1.isClickable = false
                    btn2.isClickable = false
                }
            } else {
                runOnUiThread {
                    textView.text = ("Erste Vorlesung am " + lesson.date + ":" +
                                    "\n\tName: " + lesson.name +
                                    "\n\tVon: " + formatTime(lesson.times.beginHour, lesson.times.beginMin) +
                                    "\n\tBis: " + formatTime(lesson.times.endHour, lesson.times.endMin))
                    btn1.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 60)
                    btn2.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 30)
                }
            }
        }.execute()

        btn1.setOnClickListener {
            val text = btn1.text.toString()
            planAlarm(getHourOfTime(text),getMinutesOfTime(text))
        }

        btn2.setOnClickListener {
            val text = btn2.text.toString()
            planAlarm(getHourOfTime(text),getMinutesOfTime(text))
        }
    }

    private fun minusMins(hours: Int, mins: Int, minus: Int): String {
        var mins = mins - minus
        var hours = hours
        while (mins < 0) {
            mins += 60
            hours -= 1
        }
        return formatTime(hours, mins)
    }

    private fun formatTime(hours: Int, mins: Int): String {
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

    private fun getHourOfTime(time: String): Int {
        return time.split(":")[0].toInt()
    }

    private fun getMinutesOfTime(time: String): Int {
        return time.split(":")[1].toInt()
    }

    fun planAlarm(hours: Int, mins: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Wake Up")
        intent.putExtra(AlarmClock.EXTRA_HOUR, hours)
        intent.putExtra(AlarmClock.EXTRA_MINUTES, mins)
        startActivity(intent)
    }
}

class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
    override fun doInBackground(vararg params: Void?): Void? {
        handler()
        return null
    }
}