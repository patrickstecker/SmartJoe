package com.patrickstecker.alarmnotificator.services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.PowerManager
import android.widget.Toast
import com.patrickstecker.alarmnotificator.activities.MainActivity
import com.patrickstecker.alarmnotificator.R
import java.util.*


class Alarm : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelId = "com.patrickstecker.alarmnotificator"
    val description = "My Notification"

    override fun onReceive(context: Context, intent: Intent) {
        val pm =
            context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "com.patrickstecker.alarmnotificator:ALLDAY")
        wl.acquire()
        if (LecturePlanAnalyzer().tomorrowIsALesson()) {
            throwNotification(context)
        }
        wl.release()
    }

    private fun setAlarm(context: Context, calendar: Calendar) {
        val am =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        am.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pi
        )
        Toast.makeText(context, "Alarm set for ${calendar.time.hours}:${calendar.time.minutes}", Toast.LENGTH_LONG).show()
    }

    private fun setRepeatingAlarm(context: Context, calendar: Calendar) {
        val am =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, Alarm::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60 * 60 * 24,
            pi
        )
        Toast.makeText(context, "Daily notifications activated", Toast.LENGTH_LONG).show()
    }

    fun cancelAlarm(context: Context) {
        val intent = Intent(context, Alarm::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
        Toast.makeText(context, "Deactivated Notifications", Toast.LENGTH_LONG).show()
    }

    fun throwNotification(context: Context) {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(context, channelId)
            .setContentTitle("Vorlesungs Wecker")
            .setContentText("Wecker für deine Morgige Vorlesung stellen")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setChannelId(channelId)

        notificationManager.notify(0, builder.build())
    }

    fun fireTestAlarm(context: Context) {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 1)
        setAlarm(context, calendar)
    }

    fun activateNotifications(context: Context) {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        setRepeatingAlarm(context, calendar)
    }
}