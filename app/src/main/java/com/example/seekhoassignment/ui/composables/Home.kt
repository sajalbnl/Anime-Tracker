package com.example.seekhoassignment.ui.composables


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavController
import com.example.seekhoassignment.R
import com.example.seekhoassignment.utils.publicsansBold
import com.example.seekhoassignment.utils.publicsansSemiBold

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController) {


    Column(modifier = Modifier.fillMaxSize().background(Color("#000000".toColorInt())),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Image(
            painter= painterResource(R.drawable.anime),
            modifier = Modifier.size(300.dp),
            contentDescription = "anime img"

        )
        Text(
            text = "Your Anime Hub",
            fontFamily = publicsansBold,
            fontSize = 38.sp,
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            color = Color("#ffffff".toColorInt())
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = "Check ratings and watch trailers of your favorite anime anytime.",
            fontFamily = publicsansBold,
            fontSize = 23.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp,top=2.dp),
            color = Color("#ffffff".toColorInt())
        )
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = Color("#000000".toColorInt()),
                    shape = RoundedCornerShape(60)
                )
                .border(
                    BorderStroke(1.dp, Color("#e8fa20".toColorInt())),
                    RoundedCornerShape(60)
                ).clickable(){
                    navController.navigate( route = "topAnime" )
                }
        ) {
            Text(
                text = "Lets Goo",
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 10.dp,
                    bottom = 10.dp
                ),
                fontFamily = publicsansSemiBold,
                color = Color("#e8fa20".toColorInt()),
                fontSize = 12.sp
            )
        }

    }
}


