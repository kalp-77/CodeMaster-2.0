package com.example.codemaster.ui.cf_problems_screen

import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codemaster.MyApplication
import com.example.codemaster.WebViewActivity
import com.example.codemaster.components.ErrorDialog
import com.example.codemaster.components.Shimmer
import com.example.codemaster.components.WebViewPager
import com.example.codemaster.data.model.codeforces_offical.CodeforcesProblemset
import com.example.codemaster.data.model.codeforces_offical.Problem
import com.example.codemaster.ui.leetcode_screen.font
import com.example.codemaster.data.model.codeforces_offical.ProblemsetResult
import com.example.codemaster.ui.codeforces_screen.CodeforcesUiEvent
import com.example.codemaster.ui.codeforces_screen.CodeforcesViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CFProblemScreen(
    cfProblemsViewModel: CFProblemsViewModel = hiltViewModel()
){
    val state = cfProblemsViewModel.uistate.collectAsState().value
    val showDialog = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }
    val tagList = tags()


    Column (
        modifier = Modifier
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Problem Set",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 15.dp, bottom = 10.dp),
                    textAlign = TextAlign.Start,
                    fontFamily = font,
                    fontSize = 20.sp,
                    color = Color(0xFF2A265C)
                )
            }
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info, contentDescription = "null",
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(
                                onClick = {
                                    expanded.value = true
                                }
                            ),
                        tint = Color(0xFF2A265C),
                    )
                    Icon(
                        imageVector = Icons.Default.DateRange, contentDescription = "null",
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .align(Alignment.CenterVertically)
                            .clickable(
                                onClick = {
                                    showDialog.value = true
                                }
                            ),
                        tint = Color(0xFF2A265C),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        when(state){
            is CFProblemsUiState.Empty -> Column {
                repeat(7){
                    Shimmer()
                }
            }
            is CFProblemsUiState.Loading -> Column {
                repeat(7) {
                    Shimmer()
                }
            }
            is CFProblemsUiState.Failure -> ErrorDialog("dsncsdn")
            is CFProblemsUiState.Success -> Problems(
                data = state.data,
                list = state.list,
                rating = state.rating,
                showDialog,
//                showWebView,
                expanded,
                tagList
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Problems(
    data: List<Problem>,
    list: ArrayList<String>,
    rating : Int,
    showDialog : MutableState<Boolean>,
    expanded : MutableState<Boolean>,
    tagList: ArrayList<String>
) {
//    if(showWebView.value){
//        WebViewPager(
//            url = "https://codeforces.com/problemset/problem/${data[0].contestId}/${data[0].index}"
//        )
//    }
    if (showDialog.value) {
        TagDialog(
            tagList,
            list,
            showDialog,
            rating
        )
    }
    if (expanded.value) {
        Menuu(list,expanded)
    }
    if(data.isEmpty()){
        Nul("No result found!");
    }
    Column{
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
        ) {
            items(data) {
                ProblemCard(
                    contestId = it.contestId.toString(),
                    index = it.index,
                    contestName = it.name,
                    contestRating = it.rating.toString()
                )
            }
        }
    }
}

@Composable
fun Nul(msg : String){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = msg,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            fontFamily = font
        )
    }
}
@Composable
fun ProblemCard(
    contestId : String,
    index: String,
    contestName : String,
    contestRating : String,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE9ECEAF8))
    ) {
        Divider(
            modifier = Modifier.fillMaxSize(1f),
            color = Color(0xFFF3F3F3),
            thickness = 2.dp
        )
        Column(
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .padding(start =10.dp, top = 20.dp, bottom = 20.dp, end = 10.dp)
        ){
            val url = "https://codeforces.com/problemset/problem/${contestId}/${index}"
            Row (
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = {
                            val myIntent = Intent(MyApplication.instance, WebViewActivity::class.java)
                            myIntent.putExtra("key", url)
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            MyApplication.instance.startActivity(myIntent)
                        }
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Column(
                    modifier = Modifier
                        .width(70.dp)
                ) {
                    Text(
                        text = "${contestId}${index}",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier,
                        fontFamily = font,
                        color = Color(0xFF2A265C)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(200.dp)
                ) {
                    Text(
                        text = contestName,
                        modifier = Modifier,
                        textDecoration = TextDecoration.Underline,
                        fontFamily = font,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2A265C)
                    )
                }
                Column(
                    modifier = Modifier
                        .width(50.dp),
                    horizontalAlignment = Alignment.End
                ){
                    Text(
                        text= contestRating,
                        textAlign = TextAlign.End,
                        fontFamily = font,
                        color = Color(0xFF2A265C)
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Menuu(
    list: ArrayList<String>,
    expanded: MutableState<Boolean>,
    cfProblemsViewModel: CFProblemsViewModel = hiltViewModel(),
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false },
        offset = DpOffset(380.dp,30.dp)
    ) {
        for(rating in problemRatings){
            DropdownMenuItem(
                text = {
                    Text(rating.toString())
                },
                onClick = {
                    Log.d("kalp", rating.toString())
                    cfProblemsViewModel.fetchProblems(list,rating)
                    expanded.value = false  },
            )
        }
    }
}
@Composable
fun TagDialog(
    tagList: ArrayList<String>,
    list: ArrayList<String>,
    state: MutableState<Boolean>,
    rating : Int,
    viewModel: CFProblemsViewModel = hiltViewModel()
){
    AlertDialog(
        onDismissRequest = {state.value = false},
        modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 16.dp),
        text = {
            val checkStates = tagList.map { remember { mutableStateOf(false) } }
            Column{
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select tag",
                        fontSize = 24.sp,
                        modifier = Modifier.padding(0.dp,10.dp,0.dp,10.dp),
                        fontFamily = font
                    )
                    Divider(modifier = Modifier.padding(bottom = 10.dp))
                }
                Spacer(modifier = Modifier.height(5.dp))
                LazyColumn(
                    Modifier.weight(1f), contentPadding = PaddingValues(4.dp,4.dp)
                ) {
                    itemsIndexed(tagList) { index, text ->
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkStates[index].value,
                                onCheckedChange = {
                                    checkStates[index].value = !checkStates[index].value
                                    if(checkStates[index].value) {
                                        list.add(text)
                                        Log.d("kalp", "$list")
                                    }
                                    else if(!checkStates[index].value) {
                                        list.remove(text)
                                        Log.d("kalp", "$list")
                                    }
                                    Log.d("kalp", "$list")
                                },
                                colors = CheckboxDefaults.colors(checkedColor = Color.Black)
                            )
                            Text(
                                text = text,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.body1.merge(),
                                modifier = Modifier.padding(start = 8.dp),
                                fontFamily = font
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Column(){
                Button(
                    onClick = {
                        state.value = false
                        Log.d("kalp", "$list")
                        viewModel.fetchProblems(list,rating)
                        list.clear()
                        Log.d("kalp", "$list")
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                ) {
                    Text(
                        "OK",
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    )
}

val problemRatings = listOf(
    800,
    900,
    1000,
    1100,
    1200,
    1300,
    1400,
    1500,
    1600,
    1700,
    1800,
    1900,
    2000,
    2100,
    2200,
    2300,
    2400,
    2500,
    2600,
    2700,
    2800,
    2900,
    3000,
    3100,
    3200,
    3300,
    3400,
    3500
)

fun tags() : ArrayList<String> {
    val list = ArrayList<String>()
    list.add("binary search")
    list.add("geometry")
    list.add("sortings")
    list.add("data structures")
    list.add("hashing")
    list.add("probabilities")
    list.add("dp")
    list.add("interactive")
    list.add("bitmasks")
    list.add("constructive algorithms")
    list.add("ternary search")
    list.add("dfs and similar")
    list.add("greedy")
    list.add("trees")
    list.add("implementation")
    list.add("math")
    list.add("brute force")
    list.add("strings")
    list.add("number theory")
    list.add("flows")
    list.add("graphs")
    list.add("shortest paths")
    list.add("divide and conquer")
    list.add("two pointers")
    list.add("string suffix structures")
    list.add("combinatorics")
    list.add("games")
    list.add("dsu")
    list.add("graph matching")
    list.add("schedules")
    list.add("FFT")
    list.add("2-sat")
    list.add("matrices")
    list.add("meet in middle")
    list.add("chinese remainder theorem")
    list.add("*special")
    list.add("expression parsing")

    return list
}
