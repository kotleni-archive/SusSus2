package com.maksimov.caria.ui.screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.maksimov.caria.data.User
import com.maksimov.caria.databinding.FragmentGameOnlineBinding
import com.maksimov.caria.ui.contracts.OnBackPressedDelegation
import com.maksimov.caria.ui.utils.OnBackPressedDelegationImpl
import com.zazgorbatyi.aso_template_vitaliy_che.ui.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class GameOnline: Fragment(), OnBackPressedDelegation by OnBackPressedDelegationImpl() {

    private var _binding: FragmentGameOnlineBinding? = null
    private val binding: FragmentGameOnlineBinding
        get() = _binding ?: throw NullPointerException("FragmentGameOnlineBinding is null")

    private lateinit var user: User

    private val mainViewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        user = arguments?.getSerializable(USER) as User
        _binding = FragmentGameOnlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpWebView()
        setUpProgressBar()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() = with(binding) {
        webMain.webViewClient = WebViewClient()
        with(webMain.settings) {
            blockNetworkImage = false
            blockNetworkLoads = false
            builtInZoomControls = true
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        if (user.isReg) {
            webMain.loadUrl(user.defaultLinks.home)
        } else {
            webMain.loadUrl(user.defaultLinks.track)
        }

        registerOnBackPressedDelegation(activity, this@GameOnline.lifecycle) {
            if (webMain.canGoBack()) {
                webMain.goBack()
            } else {
                activity?.moveTaskToBack(true)
            }
        }
    }

    private fun setUpProgressBar() = with(binding) {
        pbLoadPage.max = 100

        webMain.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    pbLoadPage.visibility = View.GONE
                } else {
                    pbLoadPage.visibility = View.VISIBLE
                    pbLoadPage.progress = newProgress
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) = with(binding) {
        super.onSaveInstanceState(outState)
        mainViewModel.saveUser(user, webMain.copyBackForwardList())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val USER = "user"

        @JvmStatic
        fun newInstance(user: User) = GameOnline().apply {
            arguments = Bundle().apply {
                putSerializable(USER, user)
            }
        }
    }
}