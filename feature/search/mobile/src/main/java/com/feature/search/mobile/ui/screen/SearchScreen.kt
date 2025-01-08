package com.feature.search.mobile.ui.screen

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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.feature.search.mobile.R
import com.feature.search.mobile.ui.viewmodel.SearchViewmodel
import java.net.URLEncoder

@Composable
fun SearchScreen(navController: NavHostController) {
    val viewmodel: SearchViewmodel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredResults = uiState.data.results.filter {
        it.title?.contains(searchQuery, ignoreCase = true) ?: false ||
                it.openingCrawl?.contains(searchQuery, ignoreCase = true) ?: false
    }

    val onVideoItemClick: (Films.Result) -> Unit = { films ->
        val encodedVideoUrl = URLEncoder.encode(films.videoUrl, "UTF-8")
        navController.navigate("${Screen.VideoPlayer.route}/${encodedVideoUrl}/${films.isLiveUrl}/${films.title}")
    }

    OTTSampleTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchBar(searchQuery) { query ->
                    searchQuery = query
                }
                if (uiState.isLoading) {
                    ProgressIndicator()
                } else if (uiState.error != null) {
                    ErrorUI(uiState.error)
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(filteredResults) { videoItem ->
                            VideoListItem(videoItem, onVideoItemClick)
                        }
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp)
            .shadow(20.dp)
            .background(White, shape = RoundedCornerShape(15.dp)),
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 16.sp,
                color = Color.Gray
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color.Black
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.Gray,
                contentDescription = "Search icon"
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Color.Gray
        )
    )
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
    SearchBar(query = "sdfsdf") {
        
    }
//    VideoListItem(
//        videoItem = Films.Result(
//            title = "werjkeret",
//            openingCrawl = "a sdif sif i sid isd fisd fis df dsj dsfj sdfjsjd"
//        )
//    ) {}
}