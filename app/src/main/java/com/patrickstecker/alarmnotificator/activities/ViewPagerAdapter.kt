package com.patrickstecker.alarmnotificator.activities

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.fragments.LectureListAdapter
import com.patrickstecker.alarmnotificator.helper.TimeHelper
import com.patrickstecker.alarmnotificator.models.Lecture
import java.text.DateFormatSymbols
import java.util.*

class ViewPagerAdapter(private val activity: Activity?, private var week: Array<Array<Lecture>>): RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {
    inner class Pager2ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val dayOfWeek = itemView.findViewById<TextView>(R.id.day_of_week)
        val card = itemView.findViewById<ConstraintLayout>(R.id.item)
        val icon = card.findViewById<AppCompatImageView>(R.id.icon)
        val title = card.findViewById<TextView>(R.id.title)
        val text = card.findViewById<TextView>(R.id.text)
        val detailsSection = card.findViewById<LinearLayout>(R.id.details_section)
        val listView = card.findViewById<ListView>(R.id.card_list_view)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Pager2ViewHolder {
        return Pager2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.lecture_plan_swipe_item, parent, false))
    }

    override fun getItemCount(): Int {
        return week.size
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        val item = week[position]
        holder.dayOfWeek.text = TimeHelper.getWeekDay(position)
        holder.icon.setImageResource(R.drawable.dhbw)
        holder.title.setText(R.string.card_uni_title)
        if (item.isNotEmpty() && item.first().isLecture) {
            holder.text.text = holder.text.context.getString(R.string.card_uni_text, item.first().date)
            val adapter = activity?.let {
                LectureListAdapter(
                    it,
                    item
                )
            }
            holder.listView.adapter = adapter
        } else {
            holder.text.text = holder.text.context.getString(R.string.no_class_on, item.first().date)
            holder.detailsSection.visibility = View.GONE
        }
    }

}