package com.bangkit23dwinovirhmwt.githubuser.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.bangkit23dwinovirhmwt.githubuser.R
import com.bangkit23dwinovirhmwt.githubuser.ui.mainActivity.MainActivity
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.SettingPreferences
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.ThemeViewModel
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.ViewModelFactory
import com.bangkit23dwinovirhmwt.githubuser.ui.themeActivity.dataStore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val pref = SettingPreferences.getInstance(application.dataStore)

        val settingViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val splashImageView = findViewById<ImageView>(R.id.logoGithub)

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                splashImageView.setImageResource(R.drawable.github_mark_white)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                splashImageView.setImageResource(R.drawable.github_mark_black)
            }
        }

        supportActionBar?.hide()

        val splashScreen = Thread {
            try {
                Thread.sleep(2000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        splashScreen.start()
    }
}