package com.bortxapps.goprocontrollerandroid.urils

import android.os.Build

class BuildVersionProvider {

    fun getSdkVersion() = Build.VERSION.SDK_INT

}