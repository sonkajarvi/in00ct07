package com.sonkajarvi.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.sonkajarvi.calculator.ui.theme.CalculatorTheme

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[EntryViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            CalculatorTheme {
                CalculatorApp(viewModel = viewModel)
            }
        }
    }
}
