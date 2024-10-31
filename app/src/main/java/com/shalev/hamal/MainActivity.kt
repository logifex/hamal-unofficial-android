package com.shalev.hamal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shalev.hamal.ui.HamalApp
import com.shalev.hamal.ui.theme.HamalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HamalTheme {
                HamalApp()
            }
        }
    }
}