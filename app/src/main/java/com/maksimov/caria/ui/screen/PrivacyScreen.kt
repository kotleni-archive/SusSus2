package com.maksimov.caria.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.maksimov.caria.R

class PrivacyScreen: Fragment() {

    private var acceptPrivacy: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_online, container, false).apply {
            loadPrivacy(this)

            findViewById<Button>(R.id.btn_accept_privacy).apply {
                visibility = View.VISIBLE
                setOnClickListener {
                    acceptPrivacy?.invoke()
                }
            }
        }
    }

    private fun loadPrivacy(view: View) {
        view.findViewById<WebView>(R.id.web_main).apply {
            webViewClient = WebViewClient()
            with(settings) {
                blockNetworkImage = false
                blockNetworkLoads = false
                builtInZoomControls = true
            }

            loadUrl("https://1292-maria-casino.s3.eu-central-1.amazonaws.com/privacy-policy.html")
        }
    }

    fun accentPrivacy(listener: () -> Unit) {
        acceptPrivacy = listener
    }

    companion object {

        @JvmStatic
        fun newInstance() = PrivacyScreen()
    }
}