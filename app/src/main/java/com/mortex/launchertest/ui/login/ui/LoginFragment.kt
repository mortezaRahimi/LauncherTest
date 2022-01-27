package com.mortex.launchertest.ui.login.ui

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mortex.launchertest.R
import com.mortex.launchertest.base.BaseFragment
import com.mortex.launchertest.common.Utils.showToast
import com.mortex.launchertest.databinding.FragmentLoginBinding
import com.mortex.launchertest.local.Child
import com.mortex.launchertest.network.Resource
import com.mortex.launchertest.ui.app_list.AppInfoAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

const val IS_PARENT = "IS_PARENT"

@AndroidEntryPoint
class LoginFragment : BaseFragment(), ChildListener {
    private var children: ArrayList<Child> = ArrayList()
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var adapter: ChildAdapter
    val show: Float = 1.0F
    val hide: Float = 0.0F
    private var isParent: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLoginParent.alpha = show

        binding.btnLoginParent.setOnClickListener {
            if (binding.passEt.text!!.isNotEmpty() && binding.passEt.text!!.toString() == "TOPtec@1400_8585#"
                && binding.userNameEt.text!!.isNotEmpty() && binding.userNameEt.text!!.toString() == "toptec"
            ) {
                binding.btnLoginParent.alpha = hide
//                login(binding.passEt.text.toString(), binding.userNameEt.text.toString())
                goToAppListView(true)
            } else
                showToast("Wrong user name or password")
        }

        getSndShowChildren()
    }

    private fun login(pass: String, userName: String) {
        viewModel.login(userName, pass).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.loading.alpha = hide
                    binding.btnLoginParent.alpha = show
                    showToast(it.message.toString())
                }

                Resource.Status.SUCCESS -> {
                    binding.loading.alpha = hide
                    viewModel.setToken(it.data!!.token)
                    isParent = true
                    goToAppListView(isParent)
                }

                Resource.Status.LOADING -> {
                    binding.loading.alpha = show
                }
            }
        })
    }

    private fun goToAppListView(isParent: Boolean) {
        findNavController()
            .navigate(
                R.id.action_loginFragment_to_appListFragment,
                bundleOf(IS_PARENT to isParent)
            )
    }

    private fun getSndShowChildren() {
        this.lifecycleScope.launch {
            viewModel.userDetails.collect {
                children.clear()
                children.addAll(it)
                adapter = ChildAdapter(this@LoginFragment)
                binding.childrenRv.layoutManager = LinearLayoutManager(context)
                binding.childrenRv.adapter = adapter
                adapter.setItems(children)
            }
        }
    }

    override fun childTapped() {
        goToAppListView(false)
    }


}