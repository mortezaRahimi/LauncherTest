package com.mortex.launchertest.ui.app_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.R
import com.mortex.launchertest.common.Utils.showToast
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.login.ui.ChildListener
import com.mortex.launchertest.ui.login.ui.IS_PARENT
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class AppListFragment : Fragment(), AppListener {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var binding: FragmentAppListBinding
    var appsList: ArrayList<AppInfoToShow> = ArrayList()

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

        if (arguments != null && !arguments?.getBoolean(IS_PARENT)!!) {
            binding.btnAddChild.visibility = View.GONE
            if (mainViewModel.childAppList.value != null) {
                for (i in mainViewModel.childAppList.value) {
                    appsList.add(
                        AppInfoToShow(
                            i.label,
                            i.packageName,
                            i.blocked,
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_launcher_background
                            )!!
                        )
                    )
                }
            }
        } else {
            isForParent = true
            appsList.clear()
            appsList.addAll(mainViewModel.parentAppList.value!!)
        }

        setupRecyclerView()

        binding.btnAddChild.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_appListFragment_to_addChildFragment
                )
        }
    }

    private fun setupRecyclerView() {
        adapter = AppInfoAdapter(this@AppListFragment, isForParent)
        binding.installedAppList.layoutManager = LinearLayoutManager(context)
        binding.installedAppList.adapter = adapter
        adapter.setItems(appsList)
    }


    override fun appCheckedForBlockList(app:AppInfoToShow) {

    }

    override fun removeFromBlockList(app: AppInfoToShow) {

    }


}