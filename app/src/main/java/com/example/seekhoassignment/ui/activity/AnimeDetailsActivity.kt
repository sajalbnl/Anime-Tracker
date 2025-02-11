package com.example.seekhoassignment.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import com.example.seekhoassignment.R
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.toColorInt
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.seekhoassignment.ui.composables.AnimeCard
import com.example.seekhoassignment.ui.composables.ExcitingDealsShimmerLoaderView
import com.example.seekhoassignment.ui.theme.SeekhoAssignmentTheme
import com.example.seekhoassignment.ui.vm.AnimeViewModel
import com.example.seekhoassignment.utils.network.ApiState
import com.example.seekhoassignment.utils.publicsansBold
import com.example.seekhoassignment.utils.publicsansSemiBold
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnimeDetailsActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        // Ensure the system windows fit correctly
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = false
        }

        setContent {
            SeekhoAssignmentTheme {
                // A surface container using the 'background' color from the theme
                window.navigationBarColor = MaterialTheme.colorScheme.background.toArgb()

                val navController = rememberNavController()

                Surface(modifier = Modifier.fillMaxSize(), color = Color("#ffffff".toColorInt())) {
                    // State variables to manage location information and permission result text

                    AnimeDetailsScreenView(intent.getIntExtra("animeId",0))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun AnimeDetailsScreenView(animeId:Int){
        val context = (LocalContext.current as Activity)
        val animeViewModel = hiltViewModel<AnimeViewModel>()
        val animeDetails=animeViewModel.animeDetailsState.collectAsState()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color("#333333".toColorInt()).toArgb()
            window.navigationBarColor = Color("#333333".toColorInt()).toArgb()
        }

        LaunchedEffect(Unit) {
            animeViewModel.fetchAnimeDetails(animeId)
        }

        val lifecycle = LocalLifecycleOwner.current.lifecycle

        Column(modifier = Modifier.fillMaxSize().background(Color("#333333".toColorInt())).padding(10.dp)) {
        animeDetails.value.let {
            when(it){
                is ApiState.Loading -> {
                    AnimeDetailsShimmer()
                }
                is ApiState.Error -> {
                    Toast.makeText(context,"Error In Loading", Toast.LENGTH_SHORT).show()
                }
                is ApiState.Success -> {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 10.dp, top = 35.dp, bottom = 15.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.arrow_back),
                                    modifier = Modifier
                                        .clickable(indication = null,
                                            interactionSource = remember { MutableInteractionSource() }) {

                                            context.finish()
                                        }
                                        .size(20.dp),
                                    contentDescription = null,
                                    tint = Color("#ffffff".toColorInt())
                                )


                                Text(
                                    text = it.data?.data?.title.toString(),
                                    fontFamily = publicsansBold,
                                    fontSize = 25.sp,
                                    modifier = Modifier.padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
                                    color = Color("#ffffff".toColorInt())
                                )
                            }
                            if(it.data?.data?.trailer?.youtube_id==null){

                                val painter =
                                    rememberAsyncImagePainter(
                                        ImageRequest.Builder(
                                            LocalContext.current
                                        )
                                            .data(data = it.data?.data?.images?.jpg?.large_image_url)
                                            .apply(block = fun ImageRequest.Builder.() {
                                                crossfade(true)
                                            }).build()
                                    )

                                Image(
                                    painter,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth().clip(shape = RoundedCornerShape(16.dp))
                                        .padding(8.dp).height(220.dp)
                                )


                            }else {
                                AndroidView(
                                    factory = { context ->
                                        YouTubePlayerView(context).apply {
                                            lifecycle.addObserver(this)

                                            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                                    youTubePlayer.loadVideo(
                                                        it.data?.data?.trailer?.youtube_id.toString(),
                                                        0f
                                                    )
                                                }
                                            })
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(16.dp))
                                        .padding(8.dp).height(220.dp)
                                )
                            }

                            Row( verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier= Modifier.padding(top=10.dp, start = 10.dp)) {

                                Image(
                                    painter=painterResource(R.drawable.star),
                                    contentDescription = null,
                                    modifier = Modifier.padding(end=10.dp).size(20.dp)
                                )

                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = it.data?.data?.score.toString() + "/10",
                                    fontFamily = publicsansBold,
                                    maxLines = 1,
                                    color = Color("#ffffff".toColorInt())
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                Icon(
                                    painter=painterResource(R.drawable.episodes),
                                    contentDescription = null,
                                    tint = Color("#ffffff".toColorInt()),
                                    modifier = Modifier.padding(end=10.dp).size(16.dp)
                                )
                                Text(
                                    text = it.data?.data?.episodes.toString() + " Ep",
                                    fontFamily = publicsansBold,
                                    maxLines = 1,
                                    modifier = Modifier.padding(end=10.dp),
                                    color = Color("#ffffff".toColorInt())
                                )
                            }

                            val painter =
                                rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    )
                                        .data(data = it.data?.data?.images?.jpg?.image_url)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            crossfade(true)
                                        }).build()
                                )
                            Row(modifier=Modifier.fillMaxWidth().padding(top=10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,) {
                                Image(
                                    painter,
                                    contentDescription = "",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(0.3f).padding(start = 10.dp)
                                )
                                Text(
                                    text = it.data?.data?.synopsis.toString(),
                                    fontFamily = publicsansSemiBold,
                                    maxLines = 8,
                                    fontSize = 12.sp,
                                    modifier = Modifier.weight(0.7f).padding(start = 10.dp, end = 10.dp),
                                    color = Color("#ffffff".toColorInt())
                                )
                            }
                            Row(
                                modifier = Modifier.padding(top=10.dp)
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                it.data?.data?.genres?.forEachIndexed { index, title ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .padding(horizontal = 5.dp)
                                            .background(
                                                color = Color("#000000".toColorInt()),
                                                shape = RoundedCornerShape(60)
                                            )
                                            .border(
                                                BorderStroke(1.dp, Color("#333333".toColorInt())),
                                                RoundedCornerShape(60)
                                            )
                                    ) {
                                        Text(
                                            text = title.name,
                                            modifier = Modifier.padding(
                                                start = 12.dp,
                                                end = 12.dp,
                                                top = 10.dp,
                                                bottom = 10.dp
                                            ),
                                            fontFamily = publicsansSemiBold,
                                            color = Color("#ffffff".toColorInt()),
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun AnimeDetailsShimmer() {
    val items = (1..1).map { "Item $it" }
    Box(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
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
                            .fillMaxWidth()
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
                                    .height(300.dp)
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
