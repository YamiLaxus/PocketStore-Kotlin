package com.phonedev.pocketstore.fcm

import android.util.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.phonedev.pocketstore.entities.Constants

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
}