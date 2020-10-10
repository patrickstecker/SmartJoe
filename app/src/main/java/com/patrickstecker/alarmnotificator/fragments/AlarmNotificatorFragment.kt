package com.patrickstecker.alarmnotificator.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.patrickstecker.alarmnotificator.Alarm
import com.patrickstecker.alarmnotificator.LecturePlanAnalyzer
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.models.Lecture


class AlarmNotificatorFragment: Fragment() {
    companion object {
        fun newInstance(): AlarmNotificatorFragment {
            return AlarmNotificatorFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm_notificator, container, false)
        // Inflate the layout for this fragment
        val lecturePlanAnalyzer = LecturePlanAnalyzer()
        val textView: TextView = view.findViewById(R.id.textview)
        val btn1 = view.findViewById<Button>(R.id.btn1)
        val btn2 = view.findViewById<Button>(R.id.btn2)
        val deleteAlarmButton: Button = view.findViewById(R.id.deleteAlarms)
        val setAlarms: Button = view.findViewById(R.id.setAlarms)

        val alarm = Alarm()

        setAlarms.setOnClickListener {
            alarm.activateNotifications(view.context)
        }
        deleteAlarmButton.setOnClickListener {
            alarm.cancelAlarm(view.context)
        }
        textView.text = ""
        btn1.visibility = View.GONE
        btn2.visibility = View.GONE

        doAsync {
            val lesson: Lecture = lecturePlanAnalyzer.getFirstClassOfToday(1)
            if (
                lesson.times.beginHour == 0 &&
                lesson.times.beginMin == 0 &&
                lesson.times.endHour == 0 &&
                lesson.times.endMin == 0
            ){
                activity?.runOnUiThread {
                    textView.text = lesson.name
                    btn1.text = "--:--"
                    btn2.text = "--:--"
                    btn1.visibility = View.GONE
                    btn2.visibility = View.GONE
                }
            } else {
                activity?.runOnUiThread {
                    textView.text = ("Erste Vorlesung am " + lesson.date + ":" +
                            "\n\tName: " + lesson.name +
                            "\n\tVon: " + formatTime(lesson.times.beginHour, lesson.times.beginMin) +
                            "\n\tBis: " + formatTime(lesson.times.endHour, lesson.times.endMin))
                    btn1.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 60)
                    btn2.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 30)
                    btn1.visibility = View.VISIBLE
                    btn2.visibility = View.VISIBLE
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
        return view
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

    class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }
    }
}