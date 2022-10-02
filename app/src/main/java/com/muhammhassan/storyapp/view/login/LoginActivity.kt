package com.muhammhassan.storyapp.view.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.ActivityLoginBinding
import com.muhammhassan.storyapp.utils.Constant
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.utils.Extension.showToast
import com.muhammhassan.storyapp.utils.Extension.startActivityWithAnimation
import com.muhammhassan.storyapp.utils.Utils.isEmailValid
import com.muhammhassan.storyapp.utils.Utils.isPasswordValid
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
            edtEmail.getEditText().setText("muhammhassan@gmail.com")
            edtPassword.getEditText().setText("1234567")

            edtEmail.getEditText().addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (isEmailValid(s.toString())) {
                        binding.edtEmail.getInputLayout().apply {
                            isHelperTextEnabled = false
                        }
                    } else binding.edtEmail.getInputLayout().apply {
                        isHelperTextEnabled = true
                        helperText = getString(R.string.invalid_email)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
            edtPassword.getEditText().addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (isPasswordValid(s.toString())) {
                        binding.edtPassword.getInputLayout().apply {
                            isHelperTextEnabled = false
                        }
                    } else binding.edtPassword.getInputLayout().apply {
                        isHelperTextEnabled = true
                        helperText = getString(R.string.password_less_than_six)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })

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

    private fun redirectToListActivity(){
        val sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val token = sharedPref.getString(Constant.TOKEN_KEY, null)

        if(token != null){
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
                        viewModel.setToken(data.token, this@LoginActivity)
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