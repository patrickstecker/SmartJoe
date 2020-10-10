package com.patrickstecker.alarmnotificator.fragments

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.models.DasboardItemType
import com.patrickstecker.alarmnotificator.models.DashboardListItem

class DashboardAdapter(
    private val context: Activity,
    private val objects: Array<DashboardListItem>
) : ArrayAdapter<DashboardListItem>(context, R.layout.dashboard_list_item, objects) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val item = objects[position]
        if(item.type == DasboardItemType.WELCOME) {
            return getWelcomeView(item)
        } else if (item.type == DasboardItemType.DHBW) {
            return getDhbwView(item)
        }
        throw error("ERROR: Wrong DashboardItemType")
    }

    fun getWelcomeView(item: DashboardListItem): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.dashboard_list_item, null, true)

        val imageView = view.findViewById(R.id.icon) as ImageView
        val title = view.findViewById(R.id.title) as TextView
        val text = view.findViewById(R.id.text) as TextView

        imageView.setImageResource(item.imgId)
        title.setText(item.titleId)
        text.setText(item.textId)

        return view
    }

    fun getDhbwView(item: DashboardListItem): View {
        val inflater = context.layoutInflater
        val view = inflater.inflate(R.layout.dashboard_list_item, null, true)

        val imageView = view.findViewById(R.id.icon) as ImageView
        val title = view.findViewById(R.id.title) as TextView
        val text = view.findViewById(R.id.text) as TextView

        imageView.setImageResource(item.imgId)
        title.setText(item.titleId)
        text.setText(item.textId)

        return view
    }
}