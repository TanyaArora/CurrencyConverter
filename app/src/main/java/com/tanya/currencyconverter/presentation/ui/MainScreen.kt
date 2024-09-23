package com.tanya.currencyconverter.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tanya.currencyconverter.presentation.CurrencyViewModel
import com.tanya.currencyconverter.ui.theme.LightBackground
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: CurrencyViewModel, modifier: Modifier = Modifier, onConvertBtnClick: () -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Currency Converter",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start,
                        style = TextStyle(fontSize = 18.sp, color = Color.White)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }) { innerPadding ->

        if (uiState.isLoading) ShowLoader()

        uiState.errorMessage?.let {
            coroutineScope.launch {
                snackBarHostState.showSnackbar(it)
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(innerPadding)
        ) {

            item {
                OutlinedTextField(
                    value = uiState.baseAmount?.toString().orEmpty(),
                    onValueChange = { viewModel.updateBaseAmount(it) },
                    label = { Text("Enter amount") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                OutlinedTextField(value = uiState.baseCurrency.name,
                    onValueChange = {},
                    label = { Text("Select Option") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { viewModel.updateDropdownExpandState() }) {
                            Icon(
                                if (uiState.expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    })
                DropdownMenu(
                    expanded = uiState.expanded,
                    onDismissRequest = { viewModel.updateDropdownExpandState(false) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    uiState.convertedCurrencies.forEach { item ->
                        DropdownMenuItem(text = {
                            Text(
                                item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }, onClick = {
                            viewModel.apply {
                                onCurrencySelected(item)
                                updateDropdownExpandState(false)
                            }
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(
                                if (item == uiState.baseCurrency) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant
                                }
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Button(
                    onClick = {
                        onConvertBtnClick()
                    }, enabled = uiState.enableButton, modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Convert")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.showConvertedCurrencies) items(uiState.convertedCurrencies) {
                ConvertedCurrencyItem(
                    it,
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(LightBackground)
                        .padding(8.dp)
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    }

}

@Composable
private fun ShowLoader() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}