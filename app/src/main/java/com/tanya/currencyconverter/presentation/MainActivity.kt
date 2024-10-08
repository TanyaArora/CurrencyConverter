package com.tanya.currencyconverter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.tanya.currencyconverter.presentation.ui.MainScreen
import com.tanya.currencyconverter.ui.theme.CurrencyConverterTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: CurrencyViewModel = hiltViewModel()
            CurrencyConverterTheme {
                MainScreen(viewModel, Modifier.fillMaxSize()) {
                    viewModel.convertCurrency()
                }
            }
        }
    }
}