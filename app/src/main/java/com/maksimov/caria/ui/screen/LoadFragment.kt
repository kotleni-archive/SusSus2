package com.maksimov.caria.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.maksimov.caria.R
import com.maksimov.caria.databinding.FragmentLoadBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoadFragment : Fragment() {

    private var _binding: FragmentLoadBinding? = null
    private val binding: FragmentLoadBinding
        get() = _binding ?: throw NullPointerException("FragmentLoadBinding is null")

    private var isAnimatedLoading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoadBinding.inflate(inflater, container, false)
        start()
        return binding.root
    }

    private fun start() {
        animateLoadingText()
        updateStatus(MODE.LOADING)
    }

    private fun animateLoadingText() = with(binding){
        lifecycle.coroutineScope.launch {
            while (isAnimatedLoading) {
                delay(200)
                tvLoadIndicator.text = ""
                for (i in 0..3) {
                    delay(200)
                    tvLoadIndicator.text = tvLoadIndicator.text.toString().plus(".")
                }
                if(!isAnimatedLoading) tvLoadIndicator.text = ""
            }
        }
    }

    private fun updateStatus(status: MODE) {
        when (status) {
            MODE.LOADING ->
                setNewText(R.string.load)
        }
    }

    private fun setNewText(
        @StringRes newText: Int,
        isAnimatedLoading: Boolean? = true
    ) = with(binding) {
        lifecycle.coroutineScope.launch {
            delay(1000)
            tvLoad.setText(newText)
            this@LoadFragment.isAnimatedLoading = isAnimatedLoading ?: true
        }
    }

    override fun onResume() {
        super.onResume()
        isAnimatedLoading = true
    }

    override fun onStop() {
        super.onStop()
        isAnimatedLoading = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        isAnimatedLoading = false
    }

    companion object {

        @JvmStatic
        fun newInstance() = LoadFragment()
    }
}

enum class MODE {
    LOADING
}