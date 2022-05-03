package com.example.metacourse.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.metacourse.MainActivity
import com.example.metacourse.R
import com.example.metacourse.databinding.FragmentSigninBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SigninFragment : Fragment(R.layout.fragment_signin) {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var viewModel: SigninViewModel

    private val logTag = javaClass.simpleName

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSigninBinding.bind(view)

        viewModel = ViewModelProvider(this)[SigninViewModel::class.java]

        binding.nextBtn.setOnClickListener {
            sendAuthData()
        }

        this.lifecycleScope.launchWhenResumed {
            viewModel.signinEvents.collect {
                when(it) {
                    is SigninViewModel.SigninEvents.NavigateToMain -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun sendAuthData() {
        val emailData = binding.loginInput.text.toString()
        val passwordData = binding.passwordInput.text.toString()

        Timber.tag(logTag).d("$emailData $passwordData")
        viewModel.sendAuthData(emailData, passwordData)
    }
}