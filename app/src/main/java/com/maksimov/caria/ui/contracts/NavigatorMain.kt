package com.maksimov.caria.ui.contracts

import androidx.fragment.app.Fragment
import com.maksimov.caria.data.User

fun Fragment.nav(): NavigatorMain? {
    return activity as? NavigatorMain
}

interface NavigatorMain{

    fun goToPrivacyScreen()
    fun goToMainScreen(user: User)
    fun goToLoadScreen()
    fun goToStartScreen()
    fun goToGameScreen()
    fun goToResultScreen(isWin: Boolean)
}