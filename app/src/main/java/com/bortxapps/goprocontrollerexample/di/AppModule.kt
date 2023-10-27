package com.bortxapps.goprocontrollerexample.di

import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListViewModel
import com.bortxapps.goprocontrollerexample.screens.media.CameraMediaViewModel
import com.bortxapps.goprocontrollerexample.screens.status.CameraStatusViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<GoProController> { GoProController(androidContext()) }
    viewModel { CameraListViewModel(get()) }
    viewModel { CameraMediaViewModel(get()) }
    viewModel { CameraStatusViewModel(get(), get()) }
}