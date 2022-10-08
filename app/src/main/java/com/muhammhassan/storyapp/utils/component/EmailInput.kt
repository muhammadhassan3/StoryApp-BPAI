package com.muhammhassan.storyapp.utils.component

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.LayoutInputBaseBinding
import com.muhammhassan.storyapp.utils.Utils

class EmailInput : ConstraintLayout {
    private val binding = LayoutInputBaseBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        binding.apply {
            inputLayout.apply {
                hint = context.getString(R.string.email)
                endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
            }
            editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (Utils.isEmailValid(s.toString())) {
                        inputLayout.isHelperTextEnabled = false
                    } else inputLayout.apply {
                        isHelperTextEnabled = true
                        helperText = context.getString(R.string.invalid_email)
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