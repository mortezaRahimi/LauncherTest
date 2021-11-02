package com.mortex.launchertest.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.databinding.ActivityMainBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.ui.app_list.AppInfo
import com.mortex.launchertest.ui.login.ui.ChildAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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


        viewModel.getAllApps().observe(this, Observer {
            if (it.isEmpty()) {
                viewModel.parentAppList.value = loadApps(packageManager).apply {
                    for (i in this) {
                        toSaveList.add(
                            AppInfo(
                                i.label.toString(),
                                i.packageName.toString(),
                                false
                            )
                        )
                    }
                    viewModel.saveAllApps(toSaveList)
                    viewModel.parentAppList.value = this
                }
            } else {
                viewModel.parentAppList.value = it
            }
        })

    }



}


