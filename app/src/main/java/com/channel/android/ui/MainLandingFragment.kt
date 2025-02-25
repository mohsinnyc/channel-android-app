package com.channel.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.channel.android.R
import com.channel.android.databinding.FragmentComposeLayoutBinding
import com.channel.android.ui.viewmodel.MainLandingViewModel
import com.channel.data.session.AuthState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainLandingFragment : Fragment() {

    private var _binding: FragmentComposeLayoutBinding? = null
    private val binding get() = _binding!!
    private val mainLandingViewModel: MainLandingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComposeLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenStarted {
            mainLandingViewModel.authState.collectLatest { authState ->
                when (authState) {
                    AuthState.AUTHENTICATED -> switchToAuthGraph()
                    AuthState.LOGGED_OUT -> findNavController().navigate(R.id.action_mainLandingFragment_to_loginFragment)
                    AuthState.REFRESHING_TOKEN -> {

                    }
                }
            }
        }
    }

    private fun switchToAuthGraph() {
        val navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav_authenticated)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
