package ru.netology.nmedia.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.di.DependencyContainer
import ru.netology.nmedia.dto.PushMessage
import kotlin.random.Random

class FCMService : FirebaseMessagingService() {
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val pushMessage = gson.fromJson(message.data["content"], PushMessage::class.java)

        if (pushMessage.recipientId == null ||
            pushMessage.recipientId == DependencyContainer.getInstance().appAuth.authStateFlow.value.id
        ) {
            sendNotification(pushMessage.content)
        } else {
            DependencyContainer.getInstance().appAuth.sendPushToken()
        }
    }

    fun sendNotification(message: String) {
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        NotificationManagerCompat.from(this)
            .notify(Random.nextInt(100_000), notification)
    }

    override fun onNewToken(token: String) {
        DependencyContainer.getInstance().appAuth.sendPushToken(token)
    }
}