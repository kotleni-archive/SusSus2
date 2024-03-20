package com.maksimov.caria.di

import android.content.Context
import com.zazgorbatyi.aso_template_vitaliy_che.ui.viewmodels.MainViewModel
import com.maksimov.caria.controller.LocalMainStorageController
import com.maksimov.caria.controller.NetworkController
//import com.maksimov.caria.controller.RequestController
import com.maksimov.caria.ui.viewmodels.GameViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        MainViewModel(
            androidApplication(),
            localMainStorageController = get(),
            //requestController = get()
        )
    }

    viewModel {
        GameViewModel(androidApplication())
    }

    single {
        LocalMainStorageController(
            androidContext().getSharedPreferences(LocalMainStorageController.APP_STORAGE, Context.MODE_PRIVATE)
        )
    }

    single {
        NetworkController(androidApplication())
    }

    single {
        //RequestController(networkController = get())
    }
}