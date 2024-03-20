package com.maksimov.caria.ui.contracts

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle

interface OnBackPressedDelegation {

    fun registerOnBackPressedDelegation(
        fragmentActivity: FragmentActivity?,
        lifecycle: Lifecycle,
        onBackPressed: () -> Unit
    )

}