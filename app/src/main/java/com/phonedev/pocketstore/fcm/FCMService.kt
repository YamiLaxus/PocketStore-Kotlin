package com.phonedev.pocketstore.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.phonedev.pocketstore.R
import com.phonedev.pocketstore.entities.Constants
import com.phonedev.pocketstore.pages.Phone_Activity

class FCMService : FirebaseMessagingService(){
    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        registerNewTokenLocal(newToken)
    }

    private fun registerNewTokenLocal(newToken: String){
        val preference = PreferenceManager.getDefaultSharedPreferences(this)

        preference.edit {
            putString(Constants.PROP_TOKEN, newToken)
                .apply()
        }
        Log.i("new token", newToken)
    }
    //f3xxkdw5SKSLhNYWhU2bjH:APA91bGyj9ChWcKg-s9i1Ox7sIKRDroDan16qmp7ZVz68XIt8h4FPEUenRxnWyMHgAi2Y3Ipz6QQ8hRWoMtH_Q3hakk-KYT1i0ZINgD3ZPlNs1yHEWaJElHopObgYOjFpNKVBLsYe4Fh

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        remoteMessage.notification?.let {
            sendNotification(it)
        }

    }

    private fun sendNotification(notification: RemoteMessage.Notification){
        val intent = Intent(this, Phone_Activity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.notification_channel_id_default)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }
}