package com.muhammhassan.storyapp.utils.component

import android.content.Context
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.LayoutInputBaseBinding

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

        }
    }

    fun getEditText() = binding.editText

    fun getInputLayout() = binding.inputLayout
}