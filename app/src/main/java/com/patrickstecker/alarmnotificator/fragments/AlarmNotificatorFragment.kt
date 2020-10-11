package com.patrickstecker.alarmnotificator.fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.patrickstecker.alarmnotificator.Alarm
import com.patrickstecker.alarmnotificator.LecturePlanAnalyzer
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.helper.doAsync
import com.patrickstecker.alarmnotificator.models.Lecture
import kotlinx.android.synthetic.main.dashboard_big_list_item.*
import org.w3c.dom.Text


class AlarmNotificatorFragment: Fragment() {

    private val timeHelper = TimeHelper()
    private val lecturePlanAnalyzer = LecturePlanAnalyzer()

    companion object {
        fun newInstance(): AlarmNotificatorFragment {
            return AlarmNotificatorFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm_notificator, container, false)
        // Inflate the layout for this fragment
        val btn1 = view.findViewById<Button>(R.id.btn1)
        val btn2 = view.findViewById<Button>(R.id.btn2)
        val deleteAlarmButton: Button = view.findViewById(R.id.deleteAlarms)
        val setAlarms: Button = view.findViewById(R.id.setAlarms)
        val classesView = view.findViewById<LinearLayout>(R.id.classes_view)

        classesView.visibility = View.INVISIBLE

        val alarm = Alarm()

        setAlarms.setOnClickListener {
            alarm.activateNotifications(view.context)
        }
        deleteAlarmButton.setOnClickListener {
            MaterialAlertDialogBuilder(view.context)
                .setTitle(R.string.deactivate_notification_dialog_title)
                .setMessage(R.string.deactivate_notification_dialog_text)
                .setNeutralButton(R.string.cancel) { _, _ ->

                }
                .setPositiveButton(R.string.deactivate) {_, _ ->
                    alarm.cancelAlarm(view.context)
                }
                .show()
        }
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
                    btn1.text = "--:--"
                    btn2.text = "--:--"
                    btn1.visibility = View.GONE
                    btn2.visibility = View.GONE
                }
            } else {
                activity?.runOnUiThread {
                    btn1.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 60)
                    btn2.text = minusMins(lesson.times.beginHour, lesson.times.beginMin, 30)
                    btn1.visibility = View.VISIBLE
                    btn2.visibility = View.VISIBLE
                }
            }
        }.execute()

        showClasses(view)

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

    private fun showClasses(view: View) {
        doAsync {
                val classes = lecturePlanAnalyzer.getClassesOfToday(1)

                val icon = view.findViewById<ImageView>(R.id.icon)
                val title = view.findViewById<TextView>(R.id.title)
                val text = view.findViewById<TextView>(R.id.text)
                val detailsSection = view.findViewById<LinearLayout>(R.id.details_section)
                val listView = view.findViewById<ListView>(R.id.card_list_view)
                val classesView = view.findViewById<LinearLayout>(R.id.classes_view)

                detailsSection.visibility = View.INVISIBLE
                listView.visibility = View.INVISIBLE

                activity?.runOnUiThread{
                icon.setImageResource(R.drawable.dhbw)
                title.text = getString(R.string.card_uni_title)
                text.text = getString(R.string.card_uni_text, classes.first().date)

                if (classes.first().isLecture) {
                    val adapter = activity?.let { LectureListAdapter (it, classes) }
                    listView.adapter = adapter
                    detailsSection.visibility = View.VISIBLE
                    listView.visibility = View.VISIBLE
                }

                classesView.visibility = View.VISIBLE
            }
        }.execute()
    }

    private fun minusMins(hours: Int, mins: Int, minus: Int): String {
        var mins = mins - minus
        var hours = hours
        while (mins < 0) {
            mins += 60
            hours -= 1
        }
        return timeHelper.formatTime(hours, mins)
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