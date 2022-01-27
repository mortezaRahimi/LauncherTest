package com.mortex.launchertest.ui.app_list

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mortex.launchertest.MyDeviceAdminReceiver
import com.mortex.launchertest.R
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.local.ULink
import com.mortex.launchertest.ui.MainActivity
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.login.ui.IS_PARENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants

@AndroidEntryPoint
class AppListFragment : Fragment(), AppListener, LinkListener {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var adapterForOthers: AppInfoAdapter
    private lateinit var linkAdapter: LinkAdapter

    private lateinit var binding: FragmentAppListBinding
    var appsList: ArrayList<AppInfo> = ArrayList()
    var appsListForOthers: ArrayList<AppInfo> = ArrayList()
    var ulinks: ArrayList<ULink> = ArrayList()

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

                mainViewModel.getAllLinks().observe(viewLifecycleOwner, Observer { links ->
                    linkAdapter = LinkAdapter(this)
                    binding.usefullLinksRv.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                    binding.usefullLinksRv.adapter = linkAdapter
                    linkAdapter.setItems(links)
                })


                requireActivity().startLockTask()
                if (it != null) {
                    for (i in mainViewModel.childAppList.value!!) {
                        if (!i.blocked && !i.forOthers)
                            appsList.add(
                                AppInfo(
                                    i.label,
                                    i.packageName,
                                    i.blocked,
                                    i.icon,
                                    forOthers = false
                                )
                            )

                        if (!i.blocked && i.forOthers)
                            appsListForOthers.add(
                                AppInfo(
                                    i.label,
                                    i.packageName,
                                    i.blocked,
                                    i.icon,
                                    forOthers = true
                                )
                            )


                    }
                }
            } else {
                requireActivity().stopLockTask()
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

        adapterForOthers = AppInfoAdapter(this@AppListFragment)
        binding.installedAppList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.othersRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.installedAppList.adapter = adapter

        binding.othersRv.adapter = adapterForOthers

        var listToShow = arrayListOf<AppInfoWithIcon>()
        var listOthersToShow = arrayListOf<AppInfoWithIcon>()

        for (i in list)
            for (j in mainViewModel.parentAppWithIconList.value!!) {
                if (i.label == j.label && !i.blocked && !i.forOthers) {
                    listToShow.add(
                        AppInfoWithIcon(
                            j.label, j.packageName, j.blocked, j.icon,
                            forOthers = false
                        )
                    )
                }


            }
        adapter.setItems(listToShow)

        for (i in appsListForOthers)
            for (j in mainViewModel.parentAppWithIconList.value!!) {
                if (i.label == j.label && !i.blocked && i.forOthers) {
                    listOthersToShow.add(
                        AppInfoWithIcon(
                            j.label, j.packageName, j.blocked, j.icon,
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
        requireActivity().stopLockTask()
        val launchIntent: Intent = requireActivity().packageManager
            .getLaunchIntentForPackage(app.packageName)!!
        requireActivity().startActivity(launchIntent)
    }

    override fun linkTapped(link: ULink) {
        requireActivity().stopLockTask()
        if (link.url.contains("http://")) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link.url))
            startActivity(browserIntent)
        }
    }

}