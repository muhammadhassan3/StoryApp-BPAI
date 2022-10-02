package com.muhammhassan.storyapp.view.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.ActivityRegisterBinding
import com.muhammhassan.storyapp.utils.Extension.gone
import com.muhammhassan.storyapp.utils.Extension.show
import com.muhammhassan.storyapp.utils.Extension.showToast
import com.muhammhassan.storyapp.utils.Utils.isEmailValid
import com.muhammhassan.storyapp.utils.Utils.isPasswordValid
import com.muhammhassan.storyapp.utils.api.Status
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViewModel()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.register)
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }



        binding.apply {

            edtEmail.getEditText().setText("muhammhassan@gmail.com")
            edtName.setText("Muhammad Hassan Thalib")
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
                    edtEmail.getInputLayout().apply {
                        if (isEmailValid(s.toString())) {
                            isHelperTextEnabled = false
                        } else {
                            isHelperTextEnabled = true
                            helperText = getString(R.string.invalid_email)
                        }
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
                    edtPassword.getInputLayout().apply {
                        if (isPasswordValid(s.toString())) {
                            isHelperTextEnabled = false
                        } else {
                            isHelperTextEnabled = true
                            helperText = getString(R.string.password_less_than_six)
                        }
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }
            })

            btnRegister.setOnClickListener {
                val name = edtName.text?.trim().toString()
                val email = edtEmail.getEditText().text?.trim().toString()
                val password = edtPassword.getEditText().text?.trim().toString()

                viewModel.register(name, email, password)
            }
        }
    }

    private fun initViewModel(){
        viewModel.response.observe(this){
            when(it.status){
                Status.LOADING -> showLoading(true)
                Status.SUCCESS -> {
                    showLoading(false)
                    showToast(getString(R.string.register_success))
                    proceedToLoginActivity()
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

    private fun showLoading(state: Boolean){
        binding.apply {
            if(state){
                content.gone()
                pbar.show()
            }else{
                content.show()
                pbar.gone()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun proceedToLoginActivity() {
        finish()
    }
}