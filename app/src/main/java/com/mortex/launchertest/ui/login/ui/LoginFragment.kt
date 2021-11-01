package com.mortex.launchertest.ui.login.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.mortex.launchertest.R
import com.mortex.launchertest.base.BaseFragment
import com.mortex.launchertest.common.Utils.showToast
import com.mortex.launchertest.databinding.FragmentLoginBinding
import com.mortex.launchertest.network.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()


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

        binding.btnLoginParent.setOnClickListener {
            if (binding.passEt.text!!.isNotEmpty() && binding.userNameEt.text!!.isNotEmpty()) {
                login(binding.passEt.text.toString(), binding.userNameEt.text.toString())
            }
        }
    }

    private fun login(pass: String, userName: String) {
        viewModel.login(userName, pass).observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.loading.alpha = 0.0f
                    showToast(it.message.toString())
                }

                Resource.Status.SUCCESS -> {
                    binding.loading.alpha = 0.0f
                    viewModel.setToken(it.data!!.token)
                    goToAppListView()
                }

                Resource.Status.LOADING -> {
                    binding.loading.alpha = 1.0f
                }
            }
        })
    }

    private fun goToAppListView() {
        findNavController()
            .navigate(R.id.action_loginFragment_to_appListFragment)
    }


}