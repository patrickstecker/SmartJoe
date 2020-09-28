package com.patrickstecker.alarmnotificator

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    val channelId = "com.patrickstecker.alarmnotificator"
    val description = "My Notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val show = findViewById<Button>(R.id.notify_btn)
        val tv = findViewById<TextView>(R.id.textview)
        val btn1 = findViewById<Button>(R.id.btn1)
        val btn2 = findViewById<Button>(R.id.btn2)

        show.setOnClickListener {
            thrwoNotification()
        }
        btn1.setOnClickListener {
            planAlarm(8,0)
        }

        btn2.setOnClickListener {
            planAlarm(8,30)
        }
    }

    fun thrwoNotification() {
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(this, channelId)
            .setContentTitle("Vorlesungs Wecker")
            .setContentText("Wecker für deine Morgige Vorlesung stellen")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setChannelId(channelId)

        notificationManager.notify(0, builder.build())
    }

    fun planAlarm(hours: Int, mins: Int) {
        val intent = Intent(AlarmClock.ACTION_SET_ALARM)
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Wake Up")
        intent.putExtra(AlarmClock.EXTRA_HOUR, hours)
        intent.putExtra(AlarmClock.EXTRA_MINUTES, mins)
        startActivity(intent)
//        val pendingIntent = PendingIntent.getBroadcast(baseContext, 1, intent,0)
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
////        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.timeInMillis, pendingIntent)
//        val alarmInfo = AlarmManager.AlarmClockInfo(targetCal.timeInMillis, pendingIntent)
 //       alarmManager.setAlarmClock(alarmInfo, pendingIntent)
    }
}