package com.muhammhassan.storyapp.view.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.ActivityLoginBinding
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.utils.Extension.showToast
import com.muhammhassan.storyapp.utils.Extension.startActivityWithAnimation
import com.muhammhassan.storyapp.utils.api.Status
import com.muhammhassan.storyapp.view.list.ListStoryActivity
import com.muhammhassan.storyapp.view.register.RegisterActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        binding.apply {
            btnRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivityWithAnimation(intent)
            }

            btnLogin.setOnClickListener {
                val email = edtEmail.getEditText().text?.trim().toString()
                val password = edtPassword.getEditText().text?.trim().toString()

                viewModel.login(email, password)
            }
        }
        redirectToListActivity()
    }

    private fun redirectToListActivity() {
        val token = viewModel.getToken()
        if (token != null) {
            val intent = Intent(this, ListStoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initViewModel() {
        viewModel.response.observe(this) {
            when (it.status) {
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    it.data?.let { data ->
                        viewModel.setToken(data.token)
                    }
                    val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.NO_DATA -> {
                    showLoading(false)
                    showToast(getString(R.string.data_not_found))
                }
                Status.ERROR -> {
                    showLoading(false)
                    showToast(it.message.toString())
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.apply {
            if (state) {
                content.gone()
                pbar.show()
            } else {
                content.show()
                pbar.gone()
            }
        }
    }
}