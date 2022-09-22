package com.example.codemaster.ui.codeforces_screen

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.example.codemaster.components.ErrorDialog
import com.example.codemaster.data.model.Codeforces
//import com.jaikeerthick.composable_graphs.composables.LineGraph
//import com.jaikeerthick.composable_graphs.data.GraphData

@Composable
fun CodeforcesScreen (
    data : Codeforces
) {
    Box(
        modifier = Modifier
    ){
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.padding(10.dp)){
                Text(
                    text = "CODEFORCES",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif
                )
            }
            Row(modifier = Modifier.padding(10.dp) ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    backgroundColor = Color(0xffDEDEFA)
                ) {
                    Column(modifier = Modifier.padding(10.dp) ) {
                        Row() {
                            Column(modifier = Modifier.padding(10.dp)) {
                                val painter = rememberImagePainter(data = data.avatar)
                                Image(
                                    painter = painter,
                                    contentDescription = "Profile_picture",
                                    modifier = Modifier
                                        .height(20.dp)
                                        .width(50.dp)
                                )
                            }
                            Column(modifier = Modifier.padding(10.dp)) {
                                Box() {
                                    Text(text = "@${data.username}")
                                }
                                Box() {
                                    Text(text = data.rank)
                                }
                            }
                        }
                        Row(){
                            Column() {
                                Box() {
                                    Text(text = "Max Rating: ${data.rating}")
                                }
                                Box() {
                                    Text(text = "Rating: ${data.maxRating}")
                                }
                                Box() {
                                    Text(text = "Country Rank: ${data.rank}")
                                }
                                Box() {
                                    Text(text = "Global Rank: ${data.maxRank}")
                                }
                            }
                        }
//
                    }
                }
            }
            Row(modifier = Modifier.padding(10.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xffDEDEFA)
                ) {
                    Text(text = "GRAPH")
                }
            }
        }
    }
}
@Composable
fun MainCFSceen(
    codeforcesViewModel: CodeforcesViewModel = hiltViewModel()
) {
    val state = codeforcesViewModel.uiState.collectAsState().value
    when (state) {
        is CodeforcesUiState.Loading ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color(194, 169, 252, 255)
                )
            }
        is CodeforcesUiState.Failure -> ErrorDialog(state.message)
        is CodeforcesUiState.Success -> CodeforcesScreen(state.data)
    }
}