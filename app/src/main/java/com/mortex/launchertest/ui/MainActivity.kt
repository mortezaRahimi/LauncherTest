package com.mortex.launchertest.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mortex.launchertest.databinding.ActivityMainBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.local.AppInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var toSaveList = ArrayList<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadApps(packageManager).let {
            viewModel.parentAppWithIconList.value = it
            for (i in it) {
                toSaveList.add(
                    AppInfo(
                        i.label,
                        i.packageName,
                        false,
                        i.icon.toString(),
                        false,
                        forOthers = false
                    )
                )
            }
            viewModel.saveAllApps(toSaveList)
            viewModel.parentAppList.value = toSaveList
        }
    }

    override fun onResume() {
        super.onResume()
    }


}


