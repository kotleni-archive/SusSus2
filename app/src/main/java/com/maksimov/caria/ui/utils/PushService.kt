//package com.maksimov.caria.ui.utils
//
//import com.maksimov.caria.controller.LocalMainStorageController
//import com.google.firebase.messaging.FirebaseMessagingService
//import com.google.firebase.messaging.RemoteMessage
//import org.koin.android.ext.android.inject
//
//
//class PushService: FirebaseMessagingService() {
//
//    private val localMainStorageController by inject<LocalMainStorageController>()
//
//    override fun onNewToken(token: String) {
//        super.onNewToken(token)
//    }
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        val event = remoteMessage.data["event"].toString()
//        if (event.isNotEmpty()) {
//            if(event == REG_KEY) {
//                //save status
//                localMainStorageController.getSettings().apply {
//                    this?.let {
//                        localMainStorageController.saveSettings(it.copy(isReg = true), null)
//                    }
//                }
//            }
//        }
//
//        if (remoteMessage.notification != null) {
//            super.onMessageReceived(remoteMessage)
//        }
//    }
//    companion object {
//        const val REG_KEY = "registration"
//    }
//}