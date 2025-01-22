package com.example.seekhoassignment

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int,
    val icon_focused: Int
) {

    object TopAnime: BottomBarScreen(
        route = "topAnime",
        title = "Top Anime",
        icon = R.drawable.top,
        icon_focused = R.drawable.top_active
    )

    object Home : BottomBarScreen(
        "home",
        title = "Home",
        icon = R.drawable.home,
        icon_focused = R.drawable.home_active
    )
}