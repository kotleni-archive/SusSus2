package com.maksimov.caria.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.maksimov.caria.R
import com.maksimov.caria.ui.contracts.nav

class StartGameScreen: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start, container, false).apply {
            findViewById<ImageButton>(R.id.btn_start_game).setOnClickListener {
                nav()?.goToGameScreen()
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = StartGameScreen()
    }
}