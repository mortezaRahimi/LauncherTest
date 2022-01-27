package com.mortex.launchertest.ui.app_list.add_child

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.mortex.launchertest.local.ULink
import com.mortex.launchertest.ui.app_list.*
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class AddChildFragment : Fragment(), AppListener, AppListenerOthers {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AppInfoAdapter
    private lateinit var adapterOthers: AppInfoOthersAdapter

    private var uLinks: ArrayList<ULink> = ArrayList()
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


        binding.btnAddLink.setOnClickListener {
            if (binding.linkUrlEt.text.toString().isNotEmpty()
                && binding.linkName.text.toString().isNotEmpty()
            ) {
                uLinks.add(
                    ULink(
                        name = binding.linkName.text.toString(),
                        url = binding.linkUrlEt.text.toString(),
                        id = Random.nextInt()
                    )
                )
                Toast.makeText(
                    context,
                    binding.linkName.text.toString() + "Added to links",
                    Toast.LENGTH_SHORT
                ).show()
                binding.linkName.text = null
                binding.linkUrlEt.text = null
            }
        }

    }

    private fun addUserAndSetApps() {
        if (binding.childNameValue.text != null && binding.childNameValue.text.toString()
                .isNotEmpty() && appsToBeUnblocked.size > 0
        ) {

            if (uLinks.size > 0) {
                mainViewModel.removeLinks()
                mainViewModel.saveALLLinks(uLinks)
            }

            mainViewModel.removeChild()
            mainViewModel.addUser(
                Child(
                    Random.nextInt(),
                    binding.childNameValue.text.toString()
                )
            )

            var listToSave: ArrayList<AppInfo> = ArrayList()
            for (i in loadApps(requireActivity().packageManager)) {
                listToSave.add(
                    AppInfo(
                        i.label,
                        i.packageName,
                        true,
                        i.icon.toString(),
                        forOthers = false
                    )
                )
            }
            mainViewModel.parentAppList.value!!.clear()
            mainViewModel.parentAppList.value = listToSave
            for (i in appsToBeUnblocked) {
                for (j in mainViewModel.parentAppList.value!!) {
                    if (i.label == j.label) {
                        j.blocked = false
                        j.forOthers = i.forOthers
                    }
                }
            }

            mainViewModel.saveAllApps(mainViewModel.parentAppList.value!!)


            findNavController()
                .navigate(
                    R.id.action_addChildFragment_to_loginFragment
                )
            showToast(getString(R.string.child_added))

        } else {
            Toast.makeText(context, "Please add apps to at least one section", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun setupRecyclerView() {
        adapter = AppInfoAdapter(this@AddChildFragment)
        adapterOthers = AppInfoOthersAdapter(this@AddChildFragment)

        var linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var linearLayoutManager2 =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var listToShow = arrayListOf<AppInfoWithIcon>()

        binding.installedAppList.layoutManager = linearLayoutManager
        binding.installedAppList.adapter = adapter


        binding.othersRv.layoutManager = linearLayoutManager2
        binding.othersRv.adapter = adapterOthers

        for (j in loadApps(packageManager = requireActivity().packageManager)) {
            listToShow.add(
                AppInfoWithIcon(
                    j.label, j.packageName, j.blocked, j.icon,
                    forOthers = false
                )
            )
        }

        adapter.setItems(listToShow)

        adapterOthers.setItems(listToShow)
    }

    override fun appTapped(app: AppInfoWithIcon) {
        app.blocked = false

//        optimiseList(appsToBeUnblocked, app.label)
        appsToBeUnblocked.add(
            AppInfo(
                app.label, app.packageName, app.blocked, "",
                forOthers = false
            )
        )
        showToast(app.label + getString(R.string.added_to_saved_list))


    }


    override fun appTappedForOthers(app: AppInfoWithIcon) {
        app.blocked = false
        app.forOthers = true

//        optimiseList(appsToBeUnblocked, app.label)
        appsToBeUnblocked.add(
            AppInfo(
                app.label, app.packageName, app.blocked, "",
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