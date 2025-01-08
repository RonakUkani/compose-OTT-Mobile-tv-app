package com.feature.videoplayer.mobile.ui.screen

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.net.Uri
import androidx.annotation.OptIn
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.smoothstreaming.SsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.SHOW_BUFFERING_WHEN_PLAYING
import com.feature.videoplayer.mobile.R
import com.feature.videoplayer.utils.formatTimeHHMMSS
import kotlinx.coroutines.delay

@Composable
@OptIn(UnstableApi::class)
fun VideoPlayerScreen(
    videoUrl: String?,
    isLiveUrl: Boolean,
    title: String,
    closeButtonClick: () -> Unit
) {
    val activity = LocalContext.current as Activity
    var player: Player? by remember { mutableStateOf(null) }
    var controllerVisible by rememberSaveable { mutableStateOf(false) }
    var userOrientation by rememberSaveable { mutableIntStateOf(SCREEN_ORIENTATION_USER_LANDSCAPE) }
    var isFullScreen by rememberSaveable { mutableStateOf(true) }
    var isVideoEnded by rememberSaveable { mutableStateOf(false) }

    val onFullScreenToggle: (Boolean) -> Unit = { it ->
        userOrientation = if (it) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
        activity.requestedOrientation = userOrientation
        isFullScreen = it
    }

    if (player == null) {
        player = initPlayer(activity, videoUrl)
    }
    player?.addListener(object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                Player.STATE_ENDED -> {
                    isVideoEnded = true
                }

                Player.STATE_READY -> {
                    isVideoEnded = false
                }

                else -> {}
            }
        }
    })

    val playerView = createPlayerView(player)
    activity.requestedOrientation = userOrientation

    ComposableLifecycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_START -> {
                player?.playWhenReady = true
                playerView.onResume()
            }

            Lifecycle.Event.ON_STOP -> {
                playerView.apply {
                    player?.release()
                    onPause()
                    player = null
                }
            }

            else -> {}
        }
    }

    val playPauseFocusRequester = remember { FocusRequester() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                controllerVisible = !controllerVisible
            }
    ) {
        AndroidView(
            modifier = Modifier
                .align(Alignment.Center),
            factory = { playerView },
        )
        if (controllerVisible) {
            CustomPlayerController(
                player = player,
                isLive = isLiveUrl,
                title = title,
                isFullScreen = isFullScreen,
                isVideoEnded = isVideoEnded,
                isPlay = player?.isPlaying ?: false,
                playPauseFocusRequester = playPauseFocusRequester,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
                closeButtonClick = {
                    closeButtonClick.invoke()
                },
                onFullScreenToggle = {
                    onFullScreenToggle.invoke(it)
                }
            )
        }
        // Auto-hide the controller after 5 seconds
        LaunchedEffect(controllerVisible) {
            if (controllerVisible) {
                playPauseFocusRequester.requestFocus()
                delay(5000L)
                controllerVisible = false
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlayerScreen() {
    val playPauseFocusRequester = remember { FocusRequester() }

    CustomPlayerController(
        player = null,
        isLive = false,
        modifier = Modifier
            .fillMaxWidth(),
        title = "title",
        closeButtonClick = {

        },
        onFullScreenToggle = {

        },
        isFullScreen = true,
        isVideoEnded = false,
        isPlay = true,
        playPauseFocusRequester = playPauseFocusRequester
    )
}

@Composable
fun CustomPlayerController(
    player: Player?,
    isLive: Boolean,
    modifier: Modifier,
    title: String,
    closeButtonClick: () -> Unit,
    onFullScreenToggle: (Boolean) -> Unit,
    isFullScreen: Boolean,
    isVideoEnded: Boolean,
    isPlay: Boolean,
    playPauseFocusRequester: FocusRequester
) {
    var currentPosition by rememberSaveable {
        mutableLongStateOf(
            player?.currentPosition ?: 0
        )
    }
    var latestPosition by rememberSaveable { mutableLongStateOf(0L) }
    var duration by rememberSaveable { mutableLongStateOf(0L) }
    var isPlaying by rememberSaveable { mutableStateOf(isPlay) }

    LaunchedEffect(player) {
        while (true) {
            currentPosition = player?.currentPosition ?: 0L
            latestPosition = player?.currentPosition ?: 0L
            duration = player?.duration ?: 0L
            delay(500L)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Black.copy(alpha = 0.7f))
            .padding(start = 16.dp, end = 16.dp, top = 15.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        ) {
            IconButton(onClick = {
                closeButtonClick()
            }) {
                Icon(
                    modifier = modifier
                        .size(50.dp)
                        .focusRequester(playPauseFocusRequester),
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 5.dp)
            ) {
                Text(
                    text = "Premier Movie",
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            IconButton(onClick = {
                onFullScreenToggle(!isFullScreen)
            }) {
                Icon(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(if (isFullScreen) R.drawable.ic_full_screen_exit else R.drawable.ic_full_screen),
                    contentDescription = "Full screen",
                    tint = Color.White
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    player?.seekBack()
                    player?.seekTo((player.currentPosition - 10000).coerceAtLeast(0))
                }, enabled = !isLive
            ) {
                Icon(
                    modifier = modifier
                        .size(60.dp)
                        .focusRequester(playPauseFocusRequester),
                    painter = painterResource(id = R.drawable.ic_replay),
                    contentDescription = "Rewind 10 seconds",
                    tint = if (isLive) Color.Gray else Color.White
                )
            }

            IconButton(onClick = {
                if (isVideoEnded) {
                    player?.seekTo(0)
                    player?.playWhenReady = true
                    isPlaying = true
                } else {
                    player?.playWhenReady = !(player?.playWhenReady ?: false)
                    isPlaying = player?.isPlaying == true
                }
                playPauseFocusRequester.requestFocus()
            }) {
                Icon(
                    modifier = modifier
                        .size(60.dp)
                        .focusRequester(playPauseFocusRequester),
                    painter = painterResource(
                        if (isVideoEnded) {
                            R.drawable.ic_refresh
                        } else if (isPlaying) {
                            R.drawable.ic_pause
                        } else {
                            R.drawable.ic_play
                        }
                    ),
                    contentDescription = "Play/Pause",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = {
                    player?.seekTo((player.currentPosition + 10000).coerceAtMost(player.duration))
                }, enabled = !isLive
            ) {
                Icon(
                    modifier = modifier
                        .size(60.dp)
                        .focusRequester(playPauseFocusRequester),
                    painter = painterResource(id = R.drawable.ic_forward),
                    contentDescription = "Forward 10 seconds",
                    tint = if (isLive) Color.Gray else Color.White
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val thumbColor =
                if (currentPosition >= if (isLive) latestPosition else duration) Color.Red else Color.White

            Slider(
                value = currentPosition.toFloat(),
                valueRange = 0f..(if (isLive) latestPosition else duration).toFloat(),
                onValueChange = {
                    currentPosition = it.toLong()
                    player?.seekTo(it.toLong())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(playPauseFocusRequester),
                colors = SliderDefaults.colors(
                    thumbColor = thumbColor,
                    activeTrackColor = Color.Red,
                    inactiveTrackColor = Color.Gray
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = currentPosition.formatTimeHHMMSS(),
                    color = Color.White,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isLive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .background(Color.Red)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                            .clickable { /* Scroll to live */ }
                    ) {
                        Text(
                            text = "LIVE",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        text = duration.formatTimeHHMMSS(),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit,
) {
    DisposableEffect(lifecycleOwner) {
        // 1. Create a LifecycleEventObserver to handle lifecycle events.
        val observer = LifecycleEventObserver { source, event ->
            // 2. Call the provided onEvent callback with the source and event.
            onEvent(source, event)
        }

        // 3. Add the observer to the lifecycle of the provided LifecycleOwner.
        lifecycleOwner.lifecycle.addObserver(observer)

        // 4. Remove the observer when the composable is disposed.
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun createPlayerView(player: Player?): PlayerView {
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

@OptIn(androidx.media3.common.util.UnstableApi::class)
fun initPlayer(context: Context, videoUrl: String?): Player {
    return ExoPlayer.Builder(context).build().apply {
        val defaultHttpDataSourceFactory = DefaultHttpDataSource.Factory()
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, defaultHttpDataSourceFactory, null)
        setMediaSource(mediaSource)
        playWhenReady = true
        prepare()
    }
}

@OptIn(UnstableApi::class)
fun buildMediaSource(
    uri: Uri,
    defaultHttpDataSourceFactory: DefaultHttpDataSource.Factory,
    overrideExtension: String?,
): MediaSource {
    return when (val type = Util.inferContentType(uri, overrideExtension)) {
        C.CONTENT_TYPE_DASH -> DashMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        C.CONTENT_TYPE_SS -> SsMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        C.CONTENT_TYPE_HLS -> HlsMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        C.CONTENT_TYPE_OTHER -> ProgressiveMediaSource.Factory(defaultHttpDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(uri))

        else -> {
            throw IllegalStateException("Unsupported type: $type")
        }
    }
}