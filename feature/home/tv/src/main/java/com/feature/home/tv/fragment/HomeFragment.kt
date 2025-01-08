package com.feature.home.tv.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.domain.model.Films
import com.domain.model.Films.Result
import com.feature.home.main.viewmodel.HomeViewmodel
import com.feature.home.tv.R
import com.feature.home.tv.databinding.FragmentHomeBinding
import com.feature.videoplayer.mobile.ui.activity.VideoPlayerActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewmodel by viewModels()
    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var listFragment: MoviesListFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            homeViewModel.uiState.collectLatest {
                if (it.isLoading) {
                    fragmentHomeBinding.progressBar.isVisible = true
                } else if (it.error != null) {
                    fragmentHomeBinding.progressBar.isVisible = false
                    fragmentHomeBinding.group.isVisible = true
                    Snackbar.make(view.rootView, it.error ?: "Error", Snackbar.LENGTH_LONG).show()
                } else {
                    fragmentHomeBinding.progressBar.isVisible = false
                    fragmentHomeBinding.group.isVisible = true
                    updateUI(it.data)
                }
            }
        }
    }

    private fun updateUI(data: Films) {
        listFragment = MoviesListFragment()
        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.list_fragment, listFragment)
        transaction.commit()

        listFragment.apply {
            bindData(data)
            setOnContentSelectedListener { updateBanner(it) }
            setOnItemClickListener {
                Toast.makeText(context, it.title, Toast.LENGTH_LONG).show()
                context?.startActivity(Intent(activity, VideoPlayerActivity::class.java).apply {
                    putExtra("videoUrl", it.videoUrl)
                    putExtra("isLiveUrl", it.isLiveUrl.toString())
                    putExtra("title", it.title)
                })
            }
        }

    }

    private fun updateBanner(result: Result) {
        fragmentHomeBinding.title.text = result.title
        fragmentHomeBinding.description.text = result.openingCrawl
        Glide.with(this).load(result.thumbUrl).into(fragmentHomeBinding.imgBanner)
    }

}