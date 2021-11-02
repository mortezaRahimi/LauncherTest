package com.mortex.launchertest.ui.app_list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.R
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.login.ui.IS_PARENT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppListFragment : Fragment(), AppListener {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var binding: FragmentAppListBinding
    var appsList: ArrayList<AppInfo> = ArrayList()

    private var isForParent: Boolean = false
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

        mainViewModel.doGetUnblockedApps().observe(viewLifecycleOwner, Observer {
            mainViewModel.childAppList.value = it

            if (arguments != null && !arguments?.getBoolean(IS_PARENT)!!) {
                if (it != null) {
                    for (i in mainViewModel.childAppList.value!!) {
                        appsList.add(
                            AppInfo(
                                i.label,
                                i.packageName,
                                i.blocked,
                                i.icon
                            )
                        )
                    }
                }
            } else {
                binding.btnAddChild.visibility = View.VISIBLE
                isForParent = true
                appsList.clear()
                appsList.addAll(mainViewModel.parentAppList.value!!)
            }
            setupRecyclerView()
        })

        binding.btnAddChild.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_appListFragment_to_addChildFragment
                )
        }
    }

    private fun setupRecyclerView() {
        adapter = AppInfoAdapter(this@AppListFragment)
        binding.installedAppList.layoutManager = LinearLayoutManager(context)
        binding.installedAppList.adapter = adapter
        adapter.setItems(appsList)
    }

    override fun appTapped(app: AppInfo) {
        val launchIntent: Intent = requireActivity().packageManager
            .getLaunchIntentForPackage(app.packageName)!!
        requireActivity().startActivity(launchIntent)

    }


}