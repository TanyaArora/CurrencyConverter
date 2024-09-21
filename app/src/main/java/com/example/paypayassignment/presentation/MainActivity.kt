package com.example.paypayassignment.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.paypayassignment.presentation.ui.MainScreen
import com.example.paypayassignment.ui.theme.PayPayAssignmentTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var viewModel: CurrencyViewModel = hiltViewModel()
            PayPayAssignmentTheme {
                MainScreen(viewModel, Modifier.fillMaxSize()) {
                    viewModel.convertCurrency()
                }
            }
        }
    }
}