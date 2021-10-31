package com.mortex.launchertest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val appsList: MutableList<AppInfo> = ArrayList()
    private lateinit var adapter: InterestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()
//        AsyncTask.execute { loadApps(packageManager) }
//
//
//        for (i in appsList) {
//            Log.d("APP_NAME", i.packageName.toString())
//        }
    }

//    fun loadApps(packageManager: PackageManager): List<AppInfo> {
//        val loadList = mutableListOf<AppInfo>()
//
//        val i = Intent(Intent.ACTION_MAIN, null)
//        i.addCategory(Intent.CATEGORY_LAUNCHER)
//        val allApps = packageManager.queryIntentActivities(i, 0)
//        for (ri in allApps) {
//            val app = AppInfo()
//            app.label = ri.loadLabel(packageManager)
//            app.packageName = ri.activityInfo.packageName
//            app.icon = ri.activityInfo.loadIcon(packageManager)
//            loadList.add(app)
//        }
//        loadList.sortBy { it.label.toString() }
//
//        appsList.clear()
//        appsList.addAll(loadList)
//        return appsList
//    }

    private fun setupRecyclerView() {
        adapter = InterestsAdapter(this)
        binding.installedAppList.layoutManager = LinearLayoutManager(this)
        binding.installedAppList.adapter = adapter
//        loadApps(packageManager)
        adapter.setItems(loadApps(packageManager))
    }

    fun launchApp(packageName: String?) {
        val intent = Intent()
        intent.setPackage(packageName)
        val pm = packageManager
        val resolveInfos = pm.queryIntentActivities(intent, 0)
        Collections.sort(resolveInfos, ResolveInfo.DisplayNameComparator(pm))
        if (resolveInfos.size > 0) {
            val launchable = resolveInfos[0]
            val activity = launchable.activityInfo
            val name = ComponentName(
                activity.applicationInfo.packageName,
                activity.name
            )
            val i = Intent(Intent.ACTION_MAIN)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            i.component = name
            startActivity(i)
        }
    }


}


