package com.example.workmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {

        Log.d("TAG", "doWork: Success function celled")
        val userData = inputData.getString("user_data")
        val userPerfect = inputData.getBoolean("user_perfect", false)

        showNotification(userData, userPerfect)

        val outputData = workDataOf(Pair("data", "Complected data from work manager"))

        return Result.success(outputData)
    }

    private fun showNotification(userData: String?, userPerfect: Boolean) {
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)


        val builderNotification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Google Developer")
            .setSmallIcon(R.drawable.icon)
            .setContentText("Time is running out. You need to move towards the goal")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Channel name"
            val channelDescription = "Channel description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, builderNotification.build())
        }


    }
}