package com.example.nailnail.ui.quote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.nailnail.ui.theme.NailNailTheme

class QuoteFlowActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NailNailTheme {
                QuoteFlow(onFinish = { finish() })
            }
        }
    }
}
