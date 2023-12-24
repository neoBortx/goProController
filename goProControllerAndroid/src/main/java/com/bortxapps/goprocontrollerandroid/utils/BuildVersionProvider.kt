package com.bortxapps.goprocontrollerandroid.utils

import android.os.Build

class BuildVersionProvider {

    fun getSdkVersion() = Build.VERSION.SDK_INT

}