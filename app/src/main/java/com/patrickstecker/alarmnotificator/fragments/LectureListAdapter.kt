package com.patrickstecker.alarmnotificator.fragments

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.models.Lecture

class LectureListAdapter(
    private val context: Activity,
    private val objects: Array<Lecture>
) : ArrayAdapter<Lecture>(context, R.layout.lecture_list_item, objects){

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val item = objects[position]
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.lecture_list_item, null, true)

        val title = view.findViewById<TextView>(R.id.title)
        val time = view.findViewById<TextView>(R.id.time)
        val details = view.findViewById<TextView>(R.id.details)

        title.text = item.name
        time.text =
            (TimeHelper().formatTime(item.times.beginHour, item.times.beginMin) +
                " - " +
                TimeHelper().formatTime(item.times.endHour, item.times.endMin)
            )
        if (item.details.isNotEmpty()) {
            var details = ""
            for (i in item.details) {
                details += (i + "\n")
            }
        } else {
            details.setText(R.string.no_details)
        }
        return view
    }
}