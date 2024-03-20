package com.zazgorbatyi.aso_template_vitaliy_che.ui.viewmodels

import android.app.Application
import android.webkit.WebBackForwardList
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.maksimov.caria.controller.LocalMainStorageController
//import com.maksimov.caria.controller.RequestController
import com.maksimov.caria.data.DefaultLinks
import com.maksimov.caria.data.User
import com.maksimov.caria.data.Failure

class MainViewModel(
    context: Application,
    private val localMainStorageController: LocalMainStorageController,
    //private val requestController: RequestController
) : AndroidViewModel(context) {

    val defaultLinksData = MutableLiveData<DefaultLinks?>()
    val failureData = MutableLiveData<Failure>()

    fun validateUser() {
        //requestController.make(defaultLinksData, failureData)
    }

    fun getUserFromStorage(): User? {
        return localMainStorageController.getSettings()
    }

    fun saveUser(user: User, webBackForwardList: WebBackForwardList? = null) {
        localMainStorageController.saveSettings(user, webBackForwardList)
    }

    fun savePrivacy() {
        localMainStorageController.privacyAccepted(true)
    }

    fun getPrivacy() = localMainStorageController.getPrivacy()
}