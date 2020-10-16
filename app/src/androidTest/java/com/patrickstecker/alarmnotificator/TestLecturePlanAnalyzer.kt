package com.patrickstecker.alarmnotificator

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

import com.patrickstecker.alarmnotificator.services.LecturePlanAnalyzer
import com.patrickstecker.alarmnotificator.models.Lecture

@RunWith(AndroidJUnit4::class)
class TestLecturePlanAnalyzer {
    @Test
    fun testGetFirstClassOfTomorrow() {
        val lpa =
            LecturePlanAnalyzer()
        //val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val fcot: Lecture = lpa.getFirstClassOfToday(0)

        assert(fcot.name.isNotEmpty())
    }

    @Test
    fun testGetLectureTimes() {
        val lpa =
            LecturePlanAnalyzer()
        val timeString = "10:00-12:30"
        val erg = lpa.getLectureTimes(timeString)

        assertEquals(erg.beginHour, 10)
        assertEquals(erg.beginMin, 0)
        assertEquals(erg.endHour, 12)
        assertEquals(erg.endMin, 30)
    }
}