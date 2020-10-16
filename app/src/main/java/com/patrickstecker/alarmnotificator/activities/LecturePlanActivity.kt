package com.patrickstecker.alarmnotificator.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.Storage
import me.relex.circleindicator.CircleIndicator3

class LecturePlanActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_plan)
        val week = Storage.getLectures()
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager_2)

        viewPager2.adapter =
            ViewPagerAdapter(
                this,
                week
            )
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager2)
    }
}