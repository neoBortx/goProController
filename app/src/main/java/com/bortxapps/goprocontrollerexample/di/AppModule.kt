package com.bortxapps.goprocontrollerexample.di

import com.bortxapps.goprocontrollerandroid.exposedapi.GoProApi
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.CameraCommandsViewModel
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.home.HomeCameraControlViewModel
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.media.CameraMediaViewModel
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.status.CameraStatusViewModel
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<GoProController> {
        GoProApi.initialize(androidContext())
        GoProApi.getInstance()
    }
    viewModel { CameraListViewModel(get()) }
    viewModel { CameraMediaViewModel(get()) }
    viewModel { CameraStatusViewModel(get()) }
    viewModel { HomeCameraControlViewModel(get(), get()) }
    viewModel { CameraCommandsViewModel(get()) }
}