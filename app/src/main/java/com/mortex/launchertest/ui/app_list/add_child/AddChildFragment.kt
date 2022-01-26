package com.mortex.launchertest.ui.app_list.add_child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.R
import com.mortex.launchertest.common.Utils.showToast
import com.mortex.launchertest.databinding.FragmentAddChildBinding
import com.mortex.launchertest.loadApps
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.local.AppInfo
import com.mortex.launchertest.local.AppInfoWithIcon
import com.mortex.launchertest.ui.app_list.*
import kotlin.random.Random


class AddChildFragment : Fragment(), AppListener, AppListenerLinks, AppListenerOthers {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AppInfoAdapter
    private lateinit var adapterLinks: AppInfoLinksAdapter
    private lateinit var adapterOthers: AppInfoOthersAdapter
    var appsToBeUnblocked: ArrayList<AppInfo> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddChildBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        binding.btnAddChild.setOnClickListener {
            addUserAndSetApps()
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()

    }

    private fun addUserAndSetApps() {
        if (binding.childNameValue.text != null && binding.childNameValue.text.toString()
                .isNotEmpty()
        ) {
            mainViewModel.addUser(
                Child(
                    Random.nextInt(),
                    binding.childNameValue.text.toString()
                )
            )

            for (item in mainViewModel.parentAppList.value!!) {
                item.blocked = true
            }
            for (i in appsToBeUnblocked) {
                for (j in mainViewModel.parentAppList.value!!) {
                    if (i.label == j.label) {
                        j.blocked = false
                        j.forLinks = i.forLinks
                        j.forOthers = i.forOthers
                    }
                }
            }

            mainViewModel.saveAllApps(mainViewModel.parentAppList.value!!)

            //save links and others

            findNavController()
                .navigate(
                    R.id.action_addChildFragment_to_loginFragment
                )
            showToast(getString(R.string.child_added))

        }
    }

    private fun setupRecyclerView() {
        adapter = AppInfoAdapter(this@AddChildFragment)
        adapterLinks = AppInfoLinksAdapter(this@AddChildFragment)
        adapterOthers = AppInfoOthersAdapter(this@AddChildFragment)

        var linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        var linearLayoutManager1 =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        var linearLayoutManager2 =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var listToShow = arrayListOf<AppInfoWithIcon>()

        binding.installedAppList.layoutManager = linearLayoutManager
        binding.installedAppList.adapter = adapter

        binding.usefullLinksRv.layoutManager = linearLayoutManager1
        binding.usefullLinksRv.adapter = adapterLinks

        binding.othersRv.layoutManager = linearLayoutManager2
        binding.othersRv.adapter = adapterOthers

        for (j in loadApps(packageManager = requireActivity().packageManager)) {
            listToShow.add(
                AppInfoWithIcon(
                    j.label, j.packageName, j.blocked, j.icon, false,
                    forOthers = false
                )
            )
        }

        adapter.setItems(listToShow)
        adapterLinks.setItems(listToShow)
        adapterOthers.setItems(listToShow)
    }

    override fun appTapped(app: AppInfoWithIcon) {
        app.blocked = false

        optimiseList(appsToBeUnblocked, app.label)
        appsToBeUnblocked.add(
            AppInfo(
                app.label, app.packageName, app.blocked, "",
                forLinks = false,
                forOthers = false
            )
        )
        showToast(app.label + getString(R.string.added_to_saved_list))


    }

    override fun appTappedForLinks(app: AppInfoWithIcon) {
        app.blocked = false
        app.forLinks = true
        app.forOthers = false

        optimiseList(appsToBeUnblocked, app.label)
        appsToBeUnblocked.add(
            AppInfo(
                app.label, app.packageName, app.blocked, "",
                app.forLinks,
                app.forOthers
            )
        )

        showToast(app.label + getString(R.string.added_for_links))
    }

    override fun appTappedForOthers(app: AppInfoWithIcon) {
        app.blocked = false
        app.forOthers = true
        app.forLinks = false

        optimiseList(appsToBeUnblocked, app.label)
        appsToBeUnblocked.add(
            AppInfo(
                app.label, app.packageName, app.blocked, "",
                app.forLinks,
                app.forOthers
            )
        )

        showToast(app.label + getString(R.string.added_for_others))
    }

    private fun optimiseList(list: ArrayList<AppInfo>, label: String) {
        for (i in list) {
            if (i.label == label)
                list.remove(i)
        }
    }


}