package com.patrickstecker.alarmnotificator.activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.patrickstecker.alarmnotificator.R
import com.patrickstecker.alarmnotificator.Storage
import me.relex.circleindicator.CircleIndicator3
import java.util.*

class LecturePlanActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture_plan)
        val week = Storage.getLectures()
        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager_2)
        val btnClose = findViewById<ImageButton>(R.id.close)

        viewPager2.adapter =
            ViewPagerAdapter(
                this,
                week
            )
        viewPager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager2)

        val cal = Calendar.getInstance()
        var start = (cal.get(Calendar.DAY_OF_WEEK) -2)
        if (start < 0)
            start += 7
        viewPager2.post {
            viewPager2.setCurrentItem(start, false)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }
}