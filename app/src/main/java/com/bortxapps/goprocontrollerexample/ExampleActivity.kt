package com.bortxapps.goprocontrollerexample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.bortxapps.goprocontrollerexample.navigation.GoProNavHost

class ExampleActivity : ComponentActivity() {
    @SuppressLint("MissingPermission", "CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoProNavHost()
        }
    }
}