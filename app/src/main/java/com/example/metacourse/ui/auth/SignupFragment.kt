package com.example.metacourse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.metacourse.R
import com.example.metacourse.databinding.FragmentSignupBinding
import kotlinx.coroutines.flow.collect
import timber.log.Timber

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel

    private val logTag = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)

        viewModel = ViewModelProvider(this)[SignupViewModel::class.java]

        binding.authBtn.setOnClickListener {
            sendRegisterData()
        }

        binding.haveAccBtn.setOnClickListener {
            val action = SignupFragmentDirections.actionSignupFragmentToSigninFragment()
            if(findNavController().currentDestination?.id == R.id.signupFragment) {
                findNavController().navigate(action)
            }
        }

        this.lifecycleScope.launchWhenResumed {
            viewModel.signupEvents.collect {
                when(it) {
                    is SignupViewModel.SignupEvents.NavigateToSignin -> {
                        val action = SignupFragmentDirections.actionSignupFragmentToSigninFragment()
                        if(findNavController().currentDestination?.id == R.id.signupFragment) {
                            findNavController().navigate(action)
                        }
                    }
                }
            }
        }
    }

    private fun sendRegisterData() {
        val emailData = binding.loginInput.text.toString()
        val passwordData = binding.passwordInput.text.toString()

        Timber.tag(logTag).d("$emailData $passwordData")
        viewModel.sendRegisterData(emailData, passwordData)
    }
}