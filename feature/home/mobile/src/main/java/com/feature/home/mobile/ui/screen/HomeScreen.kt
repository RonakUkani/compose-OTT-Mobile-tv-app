package com.feature.home.mobile.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.core.common.mobile.ui.common.ErrorUI
import com.core.common.mobile.ui.common.ProgressIndicator
import com.core.common.mobile.ui.navigation.Screen
import com.core.common.mobile.ui.theme.OTTSampleTheme
import com.domain.model.Films
import com.feature.home.main.viewmodel.HomeViewmodel
import com.feature.home.mobile.R
import java.net.URLEncoder

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewmodel: HomeViewmodel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsState()

    val onVideoItemClick: (Films.Result) -> Unit = { films ->
        val encodedVideoUrl = URLEncoder.encode(films.videoUrl, "UTF-8")
        navController.navigate("${Screen.VideoPlayer.route}/${encodedVideoUrl}/${films.isLiveUrl}/${films.title}")
    }

    OTTSampleTheme {
        Surface(color = White, modifier = Modifier.fillMaxSize()) {

            if (uiState.isLoading) {
                ProgressIndicator()
            } else if (uiState.error != null) {
                ErrorUI(uiState.error)
            } else {
                LazyColumn {
                    item {
                        if (uiState.data.results.isNotEmpty()) {
                            HeaderSection(uiState.data.results[0], onVideoItemClick)
                        }
                    }
                    uiState.data.results.drop(0).let {
                        items(it) { videoItem ->
                            VideoListItem(videoItem, onVideoItemClick)
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderSection(result: Films.Result, onVideoItemClick: (Films.Result) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable { onVideoItemClick.invoke(result) }
    ) {
        Image(
            painter = rememberImagePainter(result.thumbUrl,
                builder = {
                    placeholder(R.drawable.dummy_image)
                    error(R.drawable.dummy_image)
                }),
            contentDescription = "Header Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .matchParentSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        Icon(
            imageVector = Icons.Filled.PlayArrow,
            contentDescription = "Play icon",
            tint = White,
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            TitleText(result.title, White)
            Spacer(modifier = Modifier.height(3.dp))
            BodyText(result.openingCrawl, White)
        }
    }
}

@Composable
fun VideoListItem(videoItem: Films.Result, onVideoItemClick: (Films.Result) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .clickable { onVideoItemClick(videoItem) },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = White,
        )
    ) {
        Row {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .padding(end = 8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(videoItem.thumbUrl,
                        builder = {
                            placeholder(R.drawable.dummy_image)
                            error(R.drawable.dummy_image)
                        }),
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                            )
                        )
                )

                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play icon",
                    tint = White,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )

            }


            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                TitleText(videoItem.title)
                Spacer(modifier = Modifier.height(3.dp))
                BodyText(videoItem.openingCrawl)
            }
        }
    }
}

@Composable
fun BodyText(text: String?, color: Color = Color.Black) {
    Text(
        text = text ?: "",
        maxLines = 2,
        modifier = Modifier.padding(horizontal = 8.dp),
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif,
        style = TextStyle(
            letterSpacing = 0.sp,
            lineHeight = 17.sp
        ),
        color = color
    )
}

@Composable
fun TitleText(title: String?, color: Color = Color.Black) {
    Text(
        text = title ?: "",
        modifier = Modifier.padding(horizontal = 8.dp),
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        fontFamily = FontFamily.SansSerif,
        color = color
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewItem() {
    VideoListItem(
        videoItem = Films.Result(
            title = "werjkeret",
            openingCrawl = "a sdif sif i sid isd fisd fis df dsj dsfj sdfjsjd"
        )
    ) {}

    HeaderSection(
        result = Films.Result(
            title = "werjkeret",
            openingCrawl = "a sdif sif i sid isd fisd fis df dsj dsfj sdfjsjd"
        ),
    ) {}
}