package com.patrickstecker.alarmnotificator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.patrickstecker.alarmnotificator.MainActivity
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.models.DasboardItemType
import com.patrickstecker.alarmnotificator.models.DashboardListItem

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
        val listView = view.findViewById<ListView>(R.id.dashboard_list_view)
        val listItems = arrayOf(
            DashboardListItem(R.mipmap.ic_launcher, R.string.card_welcome_title, R.string.card_welcome_text, DasboardItemType.WELCOME),
            DashboardListItem(R.drawable.dhbw, R.string.card_uni_title, R.string.card_uni_text, DasboardItemType.DHBW)
        )
        val adapter = activity?.let { DashboardAdapter(it, listItems) }
        listView.adapter = adapter
        return view
    }
}