package com.example.paypayassignment.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.paypayassignment.presentation.state.CurrencyItemUiState
import com.example.paypayassignment.ui.theme.Purple40
import com.example.paypayassignment.ui.theme.Purple80
import java.util.Locale

@Composable
fun ConvertedCurrencyItem(currency: CurrencyItemUiState, modifier: Modifier = Modifier) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = currency.name, modifier = Modifier.weight(1f), color = Color.DarkGray)

        Spacer(modifier = Modifier.padding(16.dp))

        if (currency.usdConversionRate != null && currency.convertedAmount != null) {
            Text(
                text = String.format(
                    Locale.getDefault(), "%.2f %s", currency.convertedAmount, currency.code
                ), color = Color.Red, modifier = Modifier.weight(1f), textAlign = TextAlign.End
            )
        }
    }
}