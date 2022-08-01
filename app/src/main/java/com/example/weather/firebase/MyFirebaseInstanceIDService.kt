package com.example.weather.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.weather.MainActivity
import com.example.weather.MyApplication
import com.example.weather.R
import com.example.weather.utils.*
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("@@@", token)
        pushNotification(token, token)
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val title = data[NOTIFICATION_KEY_TITLE]
        val body = data[NOTIFICATION_KEY_BODY]
        if (!title.isNullOrEmpty() && !body.isNullOrEmpty()) {
            pushNotification(title, body)
        }
        super.onMessageReceived(message)
    }

    private fun pushNotification(title: String, body: String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //этот интент запускает новое активити и если программа в этот момент работает то запускается еще 1 программа
        //не придумал как это исправить в этом классе, в активити нашел выход, а тут без понятия
        val intent = Intent(MyApplication.getMyApp(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, HIGH_CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(body)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelHigh = NotificationChannel(
                HIGH_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
            channelHigh.description = "Понял зачем это ненужное - нужно"
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}