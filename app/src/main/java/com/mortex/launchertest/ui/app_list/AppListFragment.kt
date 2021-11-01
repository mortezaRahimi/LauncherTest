package com.mortex.launchertest.ui.app_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.AppInfo
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class AppListFragment : Fragment() {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var binding: FragmentAppListBinding
    var appsList: List<AppInfo> = emptyList()
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAppListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)
//        appsList = mainViewModel.appList.value!!
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        adapter = AppInfoAdapter()
        binding.installedAppList.layoutManager = LinearLayoutManager(context)
        binding.installedAppList.adapter = adapter
        adapter.setItems(mainViewModel.appList.value!!)
    }

//    fun launchApp(packageName: String?) {
//        val intent = Intent()
//        intent.setPackage(packageName)
//        val pm = packageManager
//        val resolveInfos = pm.queryIntentActivities(intent, 0)
//        Collections.sort(resolveInfos, ResolveInfo.DisplayNameComparator(pm))
//        if (resolveInfos.size > 0) {
//            val launchable = resolveInfos[0]
//            val activity = launchable.activityInfo
//            val name = ComponentName(
//                activity.applicationInfo.packageName,
//                activity.name
//            )
//            val i = Intent(Intent.ACTION_MAIN)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
//                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
//            i.component = name
//            startActivity(i)
//        }
//    }
}