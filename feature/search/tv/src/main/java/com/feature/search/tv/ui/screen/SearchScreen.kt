package com.feature.search.tv.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.mobile.ui.common.ErrorUI
import com.core.common.mobile.ui.common.ProgressIndicator
import com.domain.model.Films
import com.feature.search.tv.R
import com.feature.search.tv.ui.viewmodel.SearchViewmodel

@Composable
fun SearchScreen(onVideoItemClick: (Films.Result) -> Unit) {
    val viewmodel: SearchViewmodel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    val filteredResults = uiState.data.results.filter {
        it.title?.contains(searchQuery, ignoreCase = true) ?: false ||
                it.openingCrawl?.contains(searchQuery, ignoreCase = true) ?: false
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF282828))
        ) {
            SearchBar(searchQuery) { query ->
                searchQuery = query
            }
            if (uiState.isLoading) {
                ProgressIndicator()
            } else if (uiState.error != null) {
                ErrorUI(uiState.error)
            } else {
                LazyVerticalGrid(
                    GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
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
            .background(Black, shape = RoundedCornerShape(15.dp)),
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 16.sp,
                color = Color.Gray
            )
        },
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = White
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
    StandardCardContainer(
        modifier = Modifier
            .fillMaxSize(),
        title = { },
        imageCard = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                onClick = {
                    onVideoItemClick.invoke(videoItem)
                },
                shape = ClickableSurfaceDefaults.shape(ShapeDefaults.ExtraSmall),
                colors = ClickableSurfaceDefaults.colors(Black),
                border = ClickableSurfaceDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 3.dp,
                            color = White
                        ),
                        shape = ShapeDefaults.ExtraSmall
                    ),
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
                content = {
                    Row {
                        Box(
                            modifier = Modifier
                                .width(120.dp)
                                .fillMaxHeight()
                                .padding(end = 8.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(
                                        LocalContext.current
                                    ).data(videoItem.thumbUrl)
                                        .apply(block = fun ImageRequest.Builder.() {
                                            placeholder(R.drawable.dummy_image)
                                            error(R.drawable.dummy_image)
                                        }).build()
                                ),
                                contentDescription = "image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )

                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.Transparent,
                                                Black.copy(alpha = 0.7f)
                                            )
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
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            TitleText(videoItem.title, White)
                            Spacer(modifier = Modifier.height(3.dp))
                            BodyText(videoItem.openingCrawl, White)
                        }
                    }
                }
            )
        },
    )
}

@Composable
fun BodyText(text: String?, color: Color = Black) {
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
fun TitleText(title: String?, color: Color = Black) {
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
    VideoListItem(
        videoItem = Films.Result(
            title = "werjkeret",
            openingCrawl = "a sdif sif i sid isd fisd fis df dsj dsfj sdfjsjd"
        )
    ) {}
}