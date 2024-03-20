package com.maksimov.caria.ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maksimov.caria.R
import com.maksimov.caria.controller.NetworkController
import com.maksimov.caria.data.DefaultLinks
import com.maksimov.caria.data.Failure
import com.maksimov.caria.data.User
import com.maksimov.caria.ui.contracts.NavigatorMain
import com.maksimov.caria.ui.screen.GameFragment
import com.maksimov.caria.ui.screen.GameOnline
import com.maksimov.caria.ui.screen.LoadFragment
import com.maksimov.caria.ui.screen.NoInternetFragment
import com.maksimov.caria.ui.screen.PrivacyScreen
import com.maksimov.caria.ui.screen.ResultScreen
import com.maksimov.caria.ui.screen.StartGameScreen
import com.maksimov.caria.ui.utils.FragmentManager
import com.maksimov.caria.ui.utils.NetworkStateListener
import com.zazgorbatyi.aso_template_vitaliy_che.ui.viewmodels.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), NavigatorMain {

    private val viewModel: MainViewModel by viewModel()
    private val netWorkController by inject<NetworkController>()
    private var activityIsVisible = false
    private var oldNoInternetFragment: NoInternetFragment? = null

    override fun onResume() {
        super.onResume()
        activityIsVisible = true
    }

    override fun onPause() {
        super.onPause()
        activityIsVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val isAccepted = viewModel.getPrivacy()
//        val noInternetFragment = connectNetworkListener(isAccepted)
        goToGameScreen()
//        val databaseReference =  FirebaseDatabase.getInstance().reference
//        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.value != null) {
//                    if (snapshot.child("privacy").value == true) {
//                        startValidating(isAccepted)
//                    } else {
//                        goToStartScreen()
//                    }
//
//                } else {
//                    startValidating(isAccepted)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                startValidating(isAccepted)
//            }
//        })
//
//        viewModel.defaultLinksData.observe(this) {
//            it?.let {
//                when (it.status) {
//                    true -> {
//                        registeredUser(it)
//                    }
//                    false -> {
//                        goToStartScreen()
//                    }
//                }
//            }
//        }
//
//        viewModel.failureData.observe(this) {
//            when (it) {
//                Failure.NetworkConnectionError -> {
//                    showNoInternetDialog(noInternetFragment)
//
//                }
//                Failure.UnknownError -> {
//                    goToStartScreen()
//                }
//            }
//        }
    }

    private fun startValidating(isAccepted: Boolean) {
        if (!netWorkController.isConnected) {
            viewModel.failureData.postValue(Failure.NetworkConnectionError)
        }
        if (!isAccepted) {
            goToPrivacyScreen()
        } else {
            goToLoadScreen()
            initUser()
        }
    }

    private fun initUser() {
        viewModel.getUserFromStorage().apply {
            if (this == null) {
                viewModel.validateUser()

            } else {
                if (this.defaultLinks.status) {
                    goToMainScreen(this)

                } else {
                    goToStartScreen()
                }
            }
        }
    }

    private fun connectNetworkListener(isAccepted: Boolean): NoInternetFragment {
        NetworkStateListener.registerListener(this)
        return NoInternetFragment().also {
            it.isCancelable = false
        }.also { noInternetDialog ->
            NetworkStateListener.networkState = { isNetworkWork ->
                if (isNetworkWork) {
                    if (noInternetDialog.isVisible && noInternetDialog.isAdded && activityIsVisible) {
                        if (!isAccepted) goToPrivacyScreen()
                        noInternetDialog.dismiss()
                    }
                    if (isAccepted) initUser()
                } else {
                    showNoInternetDialog(noInternetDialog)
                }
            }
        }
    }

    private fun showNoInternetDialog(noInternetDialog: NoInternetFragment) {
        if (!noInternetDialog.isVisible && this.activityIsVisible) {
            if (oldNoInternetFragment != null) {
                supportFragmentManager.beginTransaction().remove(noInternetDialog).commit()
            }
            if (!noInternetDialog.isVisible) {
                oldNoInternetFragment = noInternetDialog
                noInternetDialog.show(
                    supportFragmentManager,
                    NoInternetFragment::class.simpleName
                )
            }
        }
    }


    private fun registeredUser(it: DefaultLinks) {
        if (it.track.isBlank() or it.home.isBlank()) {
            viewModel.validateUser()
            return
        }

        val user = User(
            defaultLinks = DefaultLinks(
                home = it.home,
                status = true,
                track = it.track
            )
        )

        viewModel.saveUser(user)

        goToMainScreen(user)
    }

    override fun goToPrivacyScreen() {
        val privacy = PrivacyScreen.newInstance()
        FragmentManager.launchFragment(this, privacy)

        privacy.accentPrivacy {
            if (netWorkController.isConnected) {
                viewModel.savePrivacy()
                initUser()
                goToLoadScreen()
            } else {
                viewModel.failureData.postValue(Failure.NetworkConnectionError)
            }
        }
    }

    override fun goToMainScreen(user: User) {
        FragmentManager.launchFragment(this, GameOnline.newInstance(user))
    }

    override fun goToLoadScreen() {
        FragmentManager.launchFragment(this, LoadFragment.newInstance())
    }

    override fun goToStartScreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        NetworkStateListener.isListenNetwork = false
        FragmentManager.launchFragment(this, StartGameScreen.newInstance())
    }

    override fun goToGameScreen() {
        FragmentManager.launchFragment(this, GameFragment.newInstance())
    }

    override fun goToResultScreen(isWin: Boolean) {
        FragmentManager.launchFragment(this, ResultScreen.newInstance(isWin))
    }
}