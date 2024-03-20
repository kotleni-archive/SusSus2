package com.maksimov.caria.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.maksimov.caria.R
import com.maksimov.caria.ui.contracts.nav

class ResultScreen: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isWIn = arguments?.getBoolean(ISWIN) as Boolean
        return when(isWIn) {
            true -> {
                inflater.inflate(R.layout.fragment_result_win, container, false).apply {
                    findViewById<ImageButton>(R.id.btn_start_win).setOnClickListener {
                        nav()?.goToStartScreen()
                    }
                }
            }

            false -> {
                inflater.inflate(R.layout.fragment_result_lose, container, false).apply {
                    findViewById<ImageButton>(R.id.btn_again_lose).setOnClickListener {
                        nav()?.goToGameScreen()
                    }
                }
            }
        }
    }

    companion object {

        private const val ISWIN = "iswin"

        @JvmStatic
        fun newInstance(isWin: Boolean) = ResultScreen().apply {
            arguments = Bundle().apply {
                putBoolean(ISWIN, isWin)
            }
        }
    }
}