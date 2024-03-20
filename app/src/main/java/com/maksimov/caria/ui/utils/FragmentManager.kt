package com.maksimov.caria.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.maksimov.caria.R

object FragmentManager {

    fun launchFragment(activity: AppCompatActivity, fragment: Fragment, isAddedHistory: Boolean = false) {
        activity.supportFragmentManager.beginTransaction().apply {
            if (isAddedHistory) {
                add(R.id.main_container, fragment, fragment::class.simpleName)
                addToBackStack(fragment::class.simpleName)
            } else {
                replace(R.id.main_container, fragment, fragment::class.simpleName)
            }
            commit()
        }
    }
}