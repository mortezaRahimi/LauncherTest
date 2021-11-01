package com.mortex.launchertest.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mortex.launchertest.databinding.ActivityMainBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.ui.app_list.AppInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var toSaveList: ArrayList<AppInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        toSaveList = ArrayList()
        viewModel.parentAppList.value = loadApps(packageManager).apply {
            for (i in this) {
                toSaveList.add(AppInfo(i.label.toString(), i.packageName.toString(), false))
            }

            viewModel.saveAllApps(toSaveList)
        }
    }


}


