package com.feature.videos.mobile.ui.screen

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.core.common.mobile.ui.common.ErrorUI
import com.core.common.mobile.ui.common.ProgressIndicator
import com.core.common.mobile.ui.theme.OTTSampleTheme
import com.domain.model.VideosList
import com.feature.videos.mobile.R
import com.feature.videos.mobile.ui.viewmodel.VideosListViewmodel
import kotlinx.coroutines.delay

@Composable
fun VideosScreen() {
    val viewmodel: VideosListViewmodel = hiltViewModel()
    val uiState by viewmodel.uiState.collectAsState()
    val lazyColumnListState = rememberLazyListState()
    val playingItemIndex by viewmodel.currentlyPlayingIndex.collectAsStateWithLifecycle()
    var isCurrentItemVisible by remember { mutableStateOf(false) }
    val shouldStartPaginate = remember {
        derivedStateOf {
            viewmodel.canPaginate && (lazyColumnListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: -9) >= (lazyColumnListState.layoutInfo.totalItemsCount - 2)
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = shouldStartPaginate.value) {
        if (shouldStartPaginate.value && viewmodel.uiState.value.listState == VideosListViewmodel.ListState.IDLE)
            viewmodel.getVideoList()
    }

    LaunchedEffect(Unit) {
        snapshotFlow {
            lazyColumnListState.visibleAreaContainsItem(playingItemIndex, uiState.data.hits)
        }.collect { isItemVisible ->
            isCurrentItemVisible = isItemVisible
        }
    }

    val context = LocalContext.current
    val player = remember(context) { ExoPlayer.Builder(context).build() }

    LaunchedEffect(playingItemIndex) {
        if (playingItemIndex == null) {
            player.pause()
        } else {
            val video = uiState.data.hits[playingItemIndex ?: 0]
            player.setMediaItem(
                MediaItem.fromUri(video.videos?.medium?.url ?: ""),
                video.lastPlayedPosition ?: 0
            )
            player.prepare()
            player.playWhenReady = true
        }
    }

    LaunchedEffect(isCurrentItemVisible) {
        if (!isCurrentItemVisible && playingItemIndex != null) {
            viewmodel.onPlayVideoClick(player.currentPosition, playingItemIndex!!)
        }
    }

    DisposableEffect(player) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (playingItemIndex == null) return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_START -> player.play()
                Lifecycle.Event.ON_STOP -> player.pause()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            player.release()
        }
    }

    OTTSampleTheme {
        Surface(color = White, modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                ProgressIndicator()
            } else if (uiState.error != null) {
                ErrorUI(uiState.error)
            } else {
                LazyColumn(state = lazyColumnListState) {
                    itemsIndexed(
                        items = uiState.data.hits,
                        key = { _, video -> video.id!! }) { index, video ->
                        VideoCard(video, index == playingItemIndex, player, onClick = {
                            viewmodel.onPlayVideoClick(player.currentPosition, index)
                        })
                    }

                    // Manage bottom loader when pagination is start
                    item(key = uiState.listState) {
                        if (uiState.listState == VideosListViewmodel.ListState.PAGINATING) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}


private fun LazyListState.visibleAreaContainsItem(
    currentlyPlayedIndex: Int?,
    videos: List<VideosList.Hit>
): Boolean {
    return when {
        currentlyPlayedIndex == null -> false
        videos.isEmpty() -> false
        else -> {
            layoutInfo.visibleItemsInfo.map { videos[it.index] }
                .contains(videos[currentlyPlayedIndex])
        }
    }
}


@OptIn(UnstableApi::class)
@Composable
fun VideoCard(
    hit: VideosList.Hit,
    isPlaying: Boolean,
    player: ExoPlayer,
    onClick: () -> Unit
) {
    val isPlayerUiVisible by remember { mutableStateOf(false) }
    val isPlayButtonVisible = if (isPlayerUiVisible) true else !isPlaying
    var controllerVisible by rememberSaveable { mutableStateOf(true) }

    Column(modifier = Modifier.padding(top = 15.dp)) {
        HeaderUserProfileView(hit)

        Spacer(modifier = Modifier.padding(top = 10.dp))

        Box(Modifier.fillMaxWidth().height(300.dp)) {
            if (isPlaying) {
                VideoPlayer(player) {
                    controllerVisible = !controllerVisible
                }
            } else {
                VideoThumbnail(hit)
            }

            if (isPlayButtonVisible) {
                Image(
                    painter = painterResource(R.drawable.ic_play),
                    contentDescription = "play / pause",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                        .clickable { onClick() })
            } else {
                if (controllerVisible) {
                    ShowController(player)
                }

                // Auto-hide the controller after 5 seconds
                LaunchedEffect(controllerVisible) {
                    if (controllerVisible) {
                        delay(5000L)
                        controllerVisible = false
                    }
                }
            }
        }

        BottomLikeCommentView(hit)
    }
}

@Composable
fun ShowController(player: ExoPlayer) {
    var currentPosition by rememberSaveable { mutableLongStateOf(player.currentPosition) }
    var duration by rememberSaveable { mutableLongStateOf(player.duration) }
    var isVideoEnded by rememberSaveable { mutableStateOf(false) }
    var isPlaying by rememberSaveable { mutableStateOf(player.isPlaying) }

    // Update state when player state changes
    LaunchedEffect(player) {
        snapshotFlow { player.playbackState }
            .collect { playbackState ->
                isVideoEnded = playbackState == Player.STATE_ENDED
                isPlaying = player.isPlaying
            }
    }

    // Update current position and duration
    LaunchedEffect(player) {
        while (true) {
            currentPosition = player.currentPosition
            duration = player.duration.coerceAtLeast(0L)  // Ensure duration is non-negative
            delay(500L)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Play/Pause/Refresh Button
        Image(
            painter = painterResource(
                when {
                    isVideoEnded -> R.drawable.ic_refresh
                    isPlaying -> R.drawable.ic_pause
                    else -> R.drawable.ic_play
                }
            ),
            contentDescription = "Play / Pause / Refresh",
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
                .clickable {
                    if (isVideoEnded) {
                        player.seekTo(0)
                        player.playWhenReady = true
                        isPlaying = true
                        isVideoEnded = false
                    } else {
                        player.playWhenReady = !isPlaying
                    }
                }
        )

        // Slider
        if (duration > 0) {
            Slider(
                value = currentPosition.toFloat(),
                valueRange = 0f..duration.toFloat(),
                onValueChange = { newValue ->
                    currentPosition = newValue.toLong()
                    player.seekTo(newValue.toLong())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter),
                colors = SliderDefaults.colors(
                    thumbColor = White,
                    activeTrackColor = Color.Red,
                    inactiveTrackColor = Color.Gray
                )
            )
        }
    }
}

@Composable
fun BottomLikeCommentView(hit: VideosList.Hit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "${hit.likes} likes",
            color = Color.Gray,
            fontSize = 15.sp,
        )
        Text(
            text = "${hit.comments} Comments",
            color = Color.Gray,
            fontSize = 15.sp
        )
        Text(
            modifier = Modifier.padding(start = 15.dp),
            text = "${hit.views} Views",
            color = Color.Gray,
            fontSize = 15.sp
        )
    }
}

@Composable
fun HeaderUserProfileView(hit: VideosList.Hit) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(horizontal = 10.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(
                    LocalContext.current
                ).data(hit.userImageURL)
                    .apply(block = fun ImageRequest.Builder.() {
                        placeholder(R.drawable.placeholder_image)
                        error(R.drawable.placeholder_image)
                    }).build()
            ),
            contentScale = ContentScale.Crop,
            contentDescription = "User profile",
            modifier = Modifier
                .size(50.dp, 50.dp)
                .padding(all = 0.dp)
                .clip(CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = hit.user ?: "",
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                maxLines = 1,
                text = hit.tags ?: "",
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun VideoPlayer(player: ExoPlayer?, onControllerVisibilityChanged: () -> Unit) {
    val playerView = createPlayerView(player)
    playerView.setOnClickListener {
        onControllerVisibilityChanged.invoke()
    }
    playerView.player = player

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.Black),
        factory = { playerView },
    )
}

@Composable
fun VideoThumbnail(hit: VideosList.Hit) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(
                LocalContext.current
            ).data(hit.videos?.large?.thumbnail ?: "")
                .apply(block = fun ImageRequest.Builder.() {
                    placeholder(R.drawable.placeholder_image)
                    error(R.drawable.placeholder_image)
                }).build()
        ),
        contentScale = ContentScale.Crop,
        contentDescription = "video thumb",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(top = 10.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Preview() {
    val context = LocalContext.current
    val player = remember(context) { ExoPlayer.Builder(context).build() }

    LazyColumn {
        items(5) {
            VideoCard(VideosList.Hit(), false, player) {}
        }
    }
}

@OptIn(androidx.media3.common.util.UnstableApi::class)
fun initPlayer(context: Context, videoUrl: String?): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val uri = Uri.parse(videoUrl)
        val mediaSource = ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))
        setMediaSource(mediaSource)
        playWhenReady = true
        prepare()
    }
}

@OptIn(UnstableApi::class)
@Composable
fun createPlayerView(player: ExoPlayer?): PlayerView {
    val context = LocalContext.current
    val playerView = remember {
        PlayerView(context).apply {
            this.player = player
            useController = false
        }
    }
    DisposableEffect(key1 = player) {
        playerView.player = player
        onDispose {
            playerView.player = null
        }
    }
    playerView.controllerAutoShow = true
    playerView.keepScreenOn = true
    playerView.setShowBuffering(SHOW_BUFFERING_WHEN_PLAYING)
    return playerView
}

