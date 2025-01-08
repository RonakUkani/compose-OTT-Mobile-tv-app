package com.feature.search.tv.ui.screen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.feature.videoplayer.mobile.ui.activity.VideoPlayerActivity

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SearchScreen { films ->
                    context?.startActivity(Intent(activity, VideoPlayerActivity::class.java).apply {
                        putExtra("videoUrl", films.videoUrl)
                        putExtra("isLiveUrl", films.isLiveUrl.toString())
                        putExtra("title", films.title)
                    })
                }
            }
        }
    }
}
