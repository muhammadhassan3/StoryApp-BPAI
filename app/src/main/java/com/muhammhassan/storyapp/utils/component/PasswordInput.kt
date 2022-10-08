package com.muhammhassan.storyapp.utils.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.LayoutInputBaseBinding
import com.muhammhassan.storyapp.utils.Utils

class PasswordInput : ConstraintLayout {
    private val binding: LayoutInputBaseBinding =
        LayoutInputBaseBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        binding.apply {
            inputLayout.apply {
                hint = context.getString(R.string.password)
                endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            }
            editText.apply {
                inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                transformationMethod = PasswordTransformationMethod.getInstance()
            }

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (Utils.isPasswordValid(s.toString())) {
                        inputLayout.apply {
                            isHelperTextEnabled = false
                        }
                    } else inputLayout.apply {
                        isHelperTextEnabled = true
                        helperText = context.getString(R.string.password_less_than_six)
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    fun getEditText() = binding.editText

    fun getInputLayout() = binding.inputLayout
}