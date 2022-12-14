package com.example.codemaster.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codemaster.components.CustomTextField
import com.example.codemaster.utils.UiEvent


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: InputListViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Navigate -> onNavigate(event)
                else -> Unit
            }

        }
    }
    val state by viewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(235, 248, 255, 255))
            .padding(30.dp)
    ){
        Row(
            modifier = Modifier.padding(bottom = 30.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp),
                text = "Select Platform Details",
                textAlign = TextAlign.Center
            )
        }
        Text(text = "Codechef", modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp))
        CustomTextField(value = viewModel.CCusername, platform = "codechef")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Codeforces", modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp))
        CustomTextField(value = viewModel.CFusername, platform = "codeforces")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Leetcode", modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp))
        CustomTextField(value = viewModel.LCusername, platform = "leetcode")
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.onEvent(InputListEvent.OnClick)
            }
        ){
            Text(text = "Save")
        }
    }
}
