package com.mortex.launchertest.ui.app_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.common.Utils.showToast
import com.mortex.launchertest.databinding.FragmentAppListBinding
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.ui.MainViewModel
import com.mortex.launchertest.ui.login.ui.IS_PARENT
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class AppListFragment : Fragment() {

    private lateinit var adapter: AppInfoAdapter
    private lateinit var binding: FragmentAppListBinding
    var appsList: List<AppInfoToShow> = emptyList()
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
            if (mainViewModel.childAppList.value != null)
                appsList = mainViewModel.childAppList.value!!
        } else {
            appsList = mainViewModel.parentAppList.value!!
        }

        setupRecyclerView()

        binding.btnAddChild.setOnClickListener {
            mainViewModel.addUser(Child(Random.nextInt(), "Login By Child ${Random.nextInt()}"))
            showToast("New Child Added")
        }
    }

    private fun setupRecyclerView() {
        adapter = AppInfoAdapter()
        binding.installedAppList.layoutManager = LinearLayoutManager(context)
        binding.installedAppList.adapter = adapter
        adapter.setItems(appsList)
    }


}