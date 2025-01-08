package com.core.common.mobile.ui.util

var dummyImages : List<String> = listOf(
    "https://fastly.picsum.photos/id/335/500/300.jpg?hmac=A3_o2C8bz2mMZ_s5PI7befV2EQ5dzBN4kpHHbQlDs78",
    "https://fastly.picsum.photos/id/264/500/300.jpg?hmac=GMlNLTIvhUi5KrgWTbkXoE2LjDZKyfzpCcPpVWAicjI",
    "https://fastly.picsum.photos/id/566/500/300.jpg?hmac=26G7V2McR3vR9Un2PlxDBjsAXfI7E7vEwy26nPsgiYc",
    "https://fastly.picsum.photos/id/1051/500/300.jpg?hmac=GhHWrCZ4HwcHnRat_UEREQgBZ6_Oskhcz4UPdSMYjzw",
    "https://fastly.picsum.photos/id/400/500/300.jpg?hmac=0xa2sTYOlDCeKJbUcleKYTKebYgIdOKfn6sEDQwwDeE",
    "https://fastly.picsum.photos/id/819/500/300.jpg?hmac=WOSaTvFhKa7FPlUPQgkKYBAwv2PX0N39RT0fdf3b3uI",
    "https://picsum.photos/500/300"
)

const val video_Url: String =
    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
const val hls_video_live: String =
    "https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8"
const val dash_normal =
    "https://dash.akamaized.net/dash264/TestCases/1b/qualcomm/1/MultiRatePatched.mpd"
const val dash_live = "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd"

var dummyVideoUrl = listOf(
    dash_normal,
    hls_video_live,
    video_Url,
    dash_live,
)