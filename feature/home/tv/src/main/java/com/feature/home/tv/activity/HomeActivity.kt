package com.feature.home.tv.activity

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.feature.home.tv.R
import com.feature.home.tv.databinding.ActivityHomeBinding
import com.feature.account.tv.ui.screen.AccountFragment
import com.feature.home.tv.fragment.HomeFragment
import com.feature.search.tv.ui.screen.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : FragmentActivity(), View.OnKeyListener {
    private lateinit var activityHomeBinding: ActivityHomeBinding
    private var isSideMenuEnabled = false
    private var selectedMenu = R.id.textview_home
    private lateinit var lastSelectedMenu: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(activityHomeBinding.root)
        setUpOnClickListener()
        setUpInitialLayout()
    }

    private fun setUpOnClickListener() {
        activityHomeBinding.apply {
            textviewHome.apply {
                setOnClickListener(::onClick)
                setOnKeyListener(::onKey)
            }
            textviewSearch.apply {
                setOnClickListener(::onClick)
                setOnKeyListener(::onKey)
            }
            textviewAccount.apply {
                setOnClickListener(::onClick)
                setOnKeyListener(::onKey)
            }
        }
    }

    private fun setUpInitialLayout() {
        lastSelectedMenu = activityHomeBinding.textviewHome
        lastSelectedMenu.isActivated = true
        changeFragment(HomeFragment())
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
        closeMenu()
    }

    private fun onClick(view: View?) {
        lastSelectedMenu.isActivated = false
        view?.isActivated = true
        lastSelectedMenu = view!!

        when (view.id) {
            R.id.textview_home -> {
                selectedMenu = R.id.textview_home
                changeFragment(HomeFragment())
            }

            R.id.textview_search -> {
                selectedMenu = R.id.textview_search
                changeFragment(SearchFragment())
            }

            R.id.textview_account -> {
                selectedMenu = R.id.textview_account
                changeFragment(AccountFragment())
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                onClick(view)
            }

            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (!isSideMenuEnabled) {
                    switchToLastSelectedMenu()

                    openMenu()
                    isSideMenuEnabled = true
                }
            }
        }
        return false
    }

    private fun switchToLastSelectedMenu() {
        when (selectedMenu) {
            R.id.textview_home -> {
                activityHomeBinding.textviewHome.requestFocus()
            }

            R.id.textview_search -> {
                activityHomeBinding.textviewSearch.requestFocus()
            }

            R.id.textview_account -> {
                activityHomeBinding.textviewAccount.requestFocus()
            }
        }
    }

    private fun openMenu() {
        activityHomeBinding.browseFrameLayoutNavBar.apply {
            startAnimation(AnimationUtils.loadAnimation(this@HomeActivity, R.anim.slide_in_left))
            requestLayout()
            layoutParams.width = getWidthInPercent(this@HomeActivity, 16)
        }
    }

    private fun closeMenu() {
        activityHomeBinding.browseFrameLayoutNavBar.apply {
            requestLayout()
            getWidthInPercent(this@HomeActivity, 5)
        }
        activityHomeBinding.container.requestFocus()
        isSideMenuEnabled = false
    }

    private fun getWidthInPercent(context: Context, percent: Int): Int {
        val width = context.resources.displayMetrics.widthPixels
        return (width * percent) / 100
    }

    override fun onBackPressed() {
        if (isSideMenuEnabled) {
            isSideMenuEnabled = false
            closeMenu()
        } else {
            super.onBackPressed()
        }
    }

}
