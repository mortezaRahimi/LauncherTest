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
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.app_list.AppInfo
import com.mortex.launchertest.ui.app_list.AppInfoAdapter
import com.mortex.launchertest.ui.app_list.AppInfoToShow
import com.mortex.launchertest.ui.app_list.AppListener
import kotlin.random.Random


class AddChildFragment : Fragment(), AppListener {

    private lateinit var binding: FragmentAddChildBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AppInfoAdapter
    var appsToBeBlocked: ArrayList<AppInfo> = ArrayList()

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
            for (i in appsToBeBlocked) {
                for (j in mainViewModel.childAppList.value) {
                    if (i.label == j.label) {
                        j.blocked = true
                    }
                }
            }

            mainViewModel.saveAllApps(mainViewModel.childAppList.value)
            findNavController().navigateUp()
            showToast("New Child Added")

        }
    }


    private fun setupRecyclerView() {
        adapter = AppInfoAdapter(this@AddChildFragment, true)
        binding.installedAppList.layoutManager = LinearLayoutManager(context)
        binding.installedAppList.adapter = adapter
        adapter.setItems(mainViewModel.parentAppList.value!!)
    }

    override fun appCheckedForBlockList(app: AppInfoToShow) {
        app.blocked = true
        appsToBeBlocked.add(AppInfo(app.label.toString(), app.packageName.toString(), app.blocked))
    }

    override fun removeFromBlockList(app: AppInfoToShow) {
        app.blocked = false
        appsToBeBlocked.remove(
            AppInfo(
                app.label.toString(),
                app.packageName.toString(),
                app.blocked
            )
        )
    }


}