package com.sidratul.finalsubmissionbfaa.ui.theme

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.switchmaterial.SwitchMaterial
import com.sidratul.finalsubmissionbfaa.R
import com.sidratul.finalsubmissionbfaa.main.ViewModelFactory
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ThemeActivity : AppCompatActivity() {

    private lateinit var switchTheme: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_theme)

        switchTheme = findViewById(R.id.switch_theme)

        val pref = SettingPreferences.getInstance(dataStore)

        lifecycleScope.launch {
            pref.getThemeSetting().collect { isDarkModeActive ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    switchTheme.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    switchTheme.isChecked = false
                }
            }
        }

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
                ThemeViewModel::class.java
            )
            mainViewModel.saveThemeSetting(isChecked)
        }
    }
}