package com.example.codemaster.ui.contest_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codemaster.R
import com.example.codemaster.components.ErrorDialog
import com.example.codemaster.components.Shimmer
import com.example.codemaster.data.model.Contest
import com.example.codemaster.data.model.ContestItem
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import com.valentinilk.shimmer.unclippedBoundsInWindow
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun Contest(
    contestViewModel: ContestViewModel = hiltViewModel()
) {
    val state = contestViewModel.uiState.collectAsState().value
    Column {
        when (state) {
            is ContestUiState.Empty -> Column {
                repeat(7){
                    Shimmer()
                }
            }
            is ContestUiState.Loading -> Column {
                repeat(7){
                    Shimmer()
                }
            }
            is ContestUiState.Failure -> ErrorDialog(state.message)
            is ContestUiState.Success -> Contests(data = state.data)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Contests(
    data : Contest
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(data){
            ContestCard(data = it)
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContestCard(
    data : ContestItem
){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Divider(
            modifier = Modifier.fillMaxSize(1f),
            color = Color(0xFFE6E6F0),
            thickness = 2.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(){
            Column(modifier = Modifier.padding(15.dp)) {
                Row() {
                    val painter: Painter
                    if(data.site == "CodeChef")
                        painter = painterResource(id = R.drawable.icons_codechef)
                    else if(data.site == "CodeForces")
                        painter = painterResource(id = R.drawable.icons_codeforces)
                    else if(data.site == "LeetCode")
                        painter = painterResource(id = R.drawable.icons_leetcode)
                    else if(data.site == "HackerRank")
                        painter = painterResource(id = R.drawable.icons_hackerrank)
                    else
                        painter = painterResource(id = R.drawable.icons_contest)
                    Image(
                        painter = painter,
                        contentDescription = "logo",
                    )
                    Text(
                        text = data.site,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Text(
                    text = data.name,
                    fontWeight = Bold
                )

                //date
                if(data.site == "CodeChef") {
                    Text(
                        text =  data.start_time.toDate().formatTo("dd MMM, yyyy")
                    )
                }
                else {
                    val odt = OffsetDateTime.parse(data.start_time)
                    val dtf = DateTimeFormatter.ofPattern("dd MMM, uuuu", Locale.ENGLISH)
                    Text(
                        text = dtf.format(odt)
                    )
                }

                //no. of hours
                val x = (data.duration).toIntOrNull()
                val length = x?.div(3600)
                Text(
                    text = "${length.toString()} hrs"
                )
            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Image(
                    painter = painterResource(id = R.drawable.icons_alarm),
                    contentDescription = "Reminder",
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.End),
                )
            }
        }
    }
}

fun String.toDate(dateFormat: String = "yyyy-MM-dd HH:mm:ss", timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
    parser.timeZone = timeZone
    return parser.parse(this)
}
fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone
    return formatter.format(this)
}