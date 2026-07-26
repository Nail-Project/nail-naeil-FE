package com.example.nailnail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nailnail.navigation.NailNailNavGraph
import com.example.nailnail.ui.theme.NailNailTheme

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
