package com.mortex.launchertest.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.mortex.launchertest.databinding.ActivityMainBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.local.AppInfo
import dagger.hilt.android.AndroidEntryPoint
import android.widget.Toast

import android.app.ActivityManager.RunningTaskInfo

import android.app.ActivityManager
import android.content.Context
import com.mortex.launchertest.MyDeviceAdminReceiver

import android.content.ComponentName

import android.app.admin.DevicePolicyManager


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

        viewModel.doGetUnblockedApps(false).observe(this, Observer { list ->
            if (list.isEmpty()) {
                loadApps(packageManager).let {
                    viewModel.parentAppWithIconList.value = it

                    for (i in it) {
                        toSaveList.add(
                            AppInfo(
                                i.label,
                                i.packageName,
                                false,
                                i.icon.toString(),
                                forOthers = false
                            )
                        )
                    }
//                    viewModel.saveAllApps(toSaveList)
                    viewModel.parentAppList.value = toSaveList
                }
            } else {
                loadApps(packageManager).let {
                    viewModel.parentAppWithIconList.value = it
                }
                viewModel.parentAppList.value = list as ArrayList<AppInfo>?
            }
        })

        requestTaskLock()

    }

    override fun onResume() {
        super.onResume()
    }

     fun requestTaskLock() {
        val dpm = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val deviceAdminReceiver = ComponentName(this, MyDeviceAdminReceiver::class.java)
        if (dpm.isDeviceOwnerApp(this.packageName)) {
            val packages = arrayOf(this.packageName)
            dpm.setLockTaskPackages(deviceAdminReceiver, packages)
            if (dpm.isLockTaskPermitted(this.packageName)) {
                startLockTask()
            } else {
                Toast.makeText(this, "Lock screen is not permitted", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "App is not a device administrator", Toast.LENGTH_SHORT).show()
        }
    }

}


