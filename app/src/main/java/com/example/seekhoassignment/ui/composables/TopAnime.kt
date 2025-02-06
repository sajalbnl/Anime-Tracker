package com.example.seekhoassignment.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavController


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.example.seekhoassignment.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.seekhoassignment.data.model.Data
import com.example.seekhoassignment.ui.activity.AnimeDetailsActivity
import com.example.seekhoassignment.ui.vm.AnimeViewModel
import com.example.seekhoassignment.utils.publicsansBold
import com.example.seekhoassignment.utils.publicsansSemiBold

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopAnime(navController: NavController) {
    val animeViewModel = hiltViewModel<AnimeViewModel>()
    val animeList=animeViewModel.animeListState.collectAsState()

    LaunchedEffect(Unit) {
        animeViewModel.fetchAnimeList()
    }

    Column (modifier = Modifier.fillMaxSize().background(Color("#000000".toColorInt()))){
        if (animeList.value?.data == null) {
            ExcitingDealsShimmerLoaderView()
        } else {
            LazyColumn {
                item {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp)
                    ) {


                        animeList.value?.data?.forEach{ anime->
                            AnimeCard(anime)
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AnimeCard(anime: Data?){
    val context=LocalContext.current
    val itemSize: Dp = (LocalConfiguration.current.screenWidthDp.dp / 2)
    val painter =
        rememberAsyncImagePainter(
            ImageRequest.Builder(
                LocalContext.current
            )
                .data(data = anime?.images?.jpg?.image_url)
                .apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
        )


    Card(
        modifier = Modifier.width(itemSize)
            .padding(8.dp).clickable(){
                val i = Intent(context, AnimeDetailsActivity::class.java)
                i.putExtra("animeId", anime?.mal_id)
                context.startActivity(i)
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Box() {
                Image(
                    painter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .width(200.dp).height(250.dp)
                )

                Row( verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier= Modifier.padding(top=10.dp).align(Alignment.TopEnd).background(color = Color("#333333".toColorInt()).copy(alpha = 0.8f))) {
                    Text(
                        text = anime?.score.toString(),
                        fontFamily = publicsansBold,
                        maxLines = 1,
                        modifier = Modifier.padding(start = 10.dp,top=5.dp, bottom = 5.dp),
                        color = Color("#ffffff".toColorInt())
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Image(
                        painter=painterResource(R.drawable.star),
                        contentDescription = null,

                        modifier = Modifier.padding(end=10.dp,top=5.dp, bottom = 5.dp).size(16.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(color = Color("#333333".toColorInt()).copy(alpha = 0.8f))){
                    Icon(
                        painter=painterResource(R.drawable.episodes),
                        contentDescription = null,
                        tint = Color("#ffffff".toColorInt()),
                        modifier = Modifier.padding(end=10.dp).size(16.dp)
                    )
                    Text(
                        text = anime?.episodes.toString() + " Ep",
                        fontFamily = publicsansBold,
                        maxLines = 1,
                        color = Color("#ffffff".toColorInt())
                    )

                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = anime?.title.toString(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier,
                color = Color("#333333".toColorInt()),
                fontFamily = publicsansSemiBold,
            )
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable

fun ExcitingDealsShimmerLoaderView() {
    val items = (1..15).map { "Item $it" }
    Box(modifier = Modifier.fillMaxSize().padding(top = 20.dp)) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),

                ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(24.dp))
                            .background(color = Transparent)
                            .width(180.dp)
                            .padding(start = 15.dp)
                            .height(200.dp)

                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                        ) {

                            Box(
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(24.dp))
                                    .background(color = Color.LightGray)
                                    .fillMaxWidth()
                                    .height(130.dp)
                                    .shimmerLoadingAnimationApi()
                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(color = Color.LightGray)
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .shimmerLoadingAnimationApi()

                            )
                            Box(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                                    .clip(shape = RoundedCornerShape(10.dp))
                                    .background(color = Color.LightGray)
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .shimmerLoadingAnimationApi()

                            )
                        }
                    }
                }
            }
        }
    }
}
fun Modifier.shimmerLoadingAnimationApi(
    widthOfShadowBrush: Int = 500,
    angleOfAxisY: Float = 270f,
    durationMillis: Int = 1000,
): Modifier {
    return composed {

        val shimmerColors = listOf(
            Color.White.copy(alpha = 0.1f),
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 1.0f),
            Color.White.copy(alpha = 0.3f),
            Color.White.copy(alpha = 0.1f),
        )

        val transition = rememberInfiniteTransition(label = "")

        val translateAnimation = transition.animateFloat(
            initialValue = 0f,
            targetValue = (durationMillis + widthOfShadowBrush).toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = durationMillis,
                    easing = LinearEasing,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "Shimmer loading animation",
        )

        this.background(
            brush = Brush.linearGradient(
                colors = shimmerColors,
                start = Offset(x = translateAnimation.value - widthOfShadowBrush, y = 0.0f),
                end = Offset(x = translateAnimation.value, y = angleOfAxisY),
            ),
        )
    }
}
