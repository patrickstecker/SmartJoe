package com.patrickstecker.alarmnotificator.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.patrickstecker.alarmnotificator.services.LecturePlanAnalyzer
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.Storage
import com.patrickstecker.alarmnotificator.activities.LecturePlanActivity
import com.patrickstecker.alarmnotificator.helper.TimeHelper
import com.patrickstecker.alarmnotificator.helper.doAsync
import com.patrickstecker.alarmnotificator.models.Lecture

class DashboardFragment: Fragment() {

    companion object {
        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        buildWelcomeCard(view)
        var lectures = Storage.getTodayLectures()
        if (lectures.isEmpty()) {
            doAsync{
                lectures = LecturePlanAnalyzer()
                    .getClassesOfToday(0)
                activity?.runOnUiThread {
                    buildDhbwCard(view, lectures)
                }
            }.execute()
        } else {
            buildDhbwCard(view, lectures)
        }

        return view
    }

    private fun buildWelcomeCard(view: View) {
        val card = view.findViewById<ConstraintLayout>(R.id.welcome_item)

        val imageView = card.findViewById(R.id.icon) as ImageView
        val title = card.findViewById(R.id.title) as TextView
        val text = card.findViewById(R.id.text) as TextView

        imageView.setImageResource(R.mipmap.ic_launcher)
        title.setText(R.string.card_welcome_title)
        text.text = getString(R.string.card_welcome_date_string, TimeHelper.getTodayWeekDay(), TimeHelper.getTodayDate())
    }

    private fun buildDhbwCard(view: View, lectures: Array<Lecture>) {
        val card = view.findViewById<ConstraintLayout>(R.id.dhbw_item)
        card.visibility = View.INVISIBLE

        val imageView = card.findViewById(R.id.icon) as ImageView
        val title = card.findViewById(R.id.title) as TextView
        val text = card.findViewById(R.id.text) as TextView
        val listView = card.findViewById(R.id.card_list_view) as ListView
        val detailsSection = card.findViewById(R.id.details_section) as LinearLayout

        imageView.setImageResource(R.drawable.dhbw)
        title.setText(R.string.card_uni_title)

        if (lectures.isNotEmpty()) {
            text.text = getString(R.string.card_uni_text, lectures[0].date)
            val adapter = activity?.let { LectureListAdapter(it, lectures) }
            listView.adapter = adapter
        } else {
            detailsSection.visibility = View.GONE
        }
        card.visibility = View.VISIBLE

        card.setOnClickListener {
            val intent = Intent(activity, LecturePlanActivity::class.java)
            startActivity(intent)
        }
    }
}