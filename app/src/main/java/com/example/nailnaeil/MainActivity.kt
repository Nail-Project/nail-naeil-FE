package com.example.nailnaeil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nailnaeil.navigation.NailNailNavGraph
import com.example.nailnaeil.ui.theme.NailNailTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NailNailTheme {
                NailNailNavGraph()
            }
        }
    }
}
