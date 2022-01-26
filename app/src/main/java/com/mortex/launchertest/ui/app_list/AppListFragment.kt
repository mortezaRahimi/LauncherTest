package com.mortex.launchertest.ui.app_list

import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.R
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.login.ui.IS_PARENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants

@AndroidEntryPoint
class AppListFragment : Fragment(), AppListener {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var adapterForLinks: AppInfoAdapter
    private lateinit var adapterForOthers: AppInfoAdapter
    private lateinit var binding: FragmentAppListBinding
    var appsList: ArrayList<AppInfo> = ArrayList()
    var appsListForLinks: ArrayList<AppInfo> = ArrayList()
    var appsListForOthers: ArrayList<AppInfo> = ArrayList()

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

        mainViewModel.doGetUnblockedApps(blocked = false).observe(viewLifecycleOwner, Observer {
            mainViewModel.childAppList.value = it

            if (arguments != null && !arguments?.getBoolean(IS_PARENT)!!) {
                if (it != null) {
                    for (i in mainViewModel.childAppList.value!!) {
                        if (!i.blocked && !i.forLinks && !i.forOthers)
                            appsList.add(
                                AppInfo(
                                    i.label,
                                    i.packageName,
                                    i.blocked,
                                    i.icon,
                                    false,
                                    forOthers = false
                                )
                            )

                        if (!i.blocked && i.forOthers && !i.forLinks)
                            appsListForOthers.add(
                                AppInfo(
                                    i.label,
                                    i.packageName,
                                    i.blocked,
                                    i.icon,
                                    false,
                                    forOthers = true
                                )
                            )

                        if (!i.blocked && i.forLinks && !i.forOthers)
                            appsListForLinks.add(
                                AppInfo(
                                    i.label,
                                    i.packageName,
                                    i.blocked,
                                    i.icon,
                                    true,
                                    forOthers = false
                                )
                            )
                    }
                }
            } else {
                binding.btnAddChild.visibility = View.VISIBLE
                isForParent = true
                appsList.clear()

                for (i in loadApps(requireActivity().packageManager)) {
                    appsList.add(
                        AppInfo(
                            i.label,
                            i.packageName,
                            i.blocked,
                            i.icon.toString(),
                            forLinks = false,
                            forOthers = false
                        )
                    )
                }
                binding.title2.visibility = View.GONE
                binding.title3.visibility = View.GONE
                binding.title.text = getString(R.string.list_of_all_apps)
            }
            setupRecyclerView(appsList)
        })

        binding.btnAddChild.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_appListFragment_to_addChildFragment
                )
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupRecyclerView(list: List<AppInfo>) {
        adapter = AppInfoAdapter(this@AppListFragment)
        adapterForLinks = AppInfoAdapter(this@AppListFragment)
        adapterForOthers = AppInfoAdapter(this@AppListFragment)
        binding.installedAppList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.usefullLinksRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.othersRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.installedAppList.adapter = adapter
        binding.usefullLinksRv.adapter = adapterForLinks
        binding.othersRv.adapter = adapterForOthers

        var listToShow = arrayListOf<AppInfoWithIcon>()
        var listLinksToShow = arrayListOf<AppInfoWithIcon>()
        var listOthersToShow = arrayListOf<AppInfoWithIcon>()

        for (i in list)
            for (j in mainViewModel.parentAppWithIconList.value!!) {
                if (i.label == j.label && !i.blocked && !i.forLinks && !i.forOthers) {
                    listToShow.add(
                        AppInfoWithIcon(
                            j.label, j.packageName, j.blocked, j.icon, false,
                            forOthers = false
                        )
                    )
                }


            }
        adapter.setItems(listToShow)

        for (i in appsListForLinks)
            for (j in mainViewModel.parentAppWithIconList.value!!) {
                if (i.label == j.label && !i.blocked && i.forLinks && !i.forOthers) {
                    listLinksToShow.add(
                        AppInfoWithIcon(
                            j.label, j.packageName, j.blocked, j.icon, true,
                            forOthers = false
                        )
                    )
                }
            }
        adapterForLinks.setItems(listLinksToShow)

        for (i in appsListForOthers)
            for (j in mainViewModel.parentAppWithIconList.value!!) {
                if (i.label == j.label && !i.blocked && !i.forLinks && i.forOthers) {
                    listOthersToShow.add(
                        AppInfoWithIcon(
                            j.label, j.packageName, j.blocked, j.icon, false,
                            forOthers = true
                        )
                    )
                }
            }
        adapterForOthers.setItems(listOthersToShow)

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun appTapped(app: AppInfoWithIcon) {
        val launchIntent: Intent = requireActivity().packageManager
            .getLaunchIntentForPackage(app.packageName)!!
        requireActivity().startActivity(launchIntent)
    }


}