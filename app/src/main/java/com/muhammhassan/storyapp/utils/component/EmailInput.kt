package com.muhammhassan.storyapp.utils.component

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputLayout
import com.muhammhassan.storyapp.R
import com.muhammhassan.storyapp.databinding.LayoutInputBaseBinding

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
        }
    }

    fun getEditText() = binding.editText

    fun getInputLayout() = binding.inputLayout
}