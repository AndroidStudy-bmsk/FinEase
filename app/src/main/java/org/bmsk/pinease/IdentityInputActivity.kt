package org.bmsk.pinease

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import org.bmsk.pinease.databinding.ActivityIdentityInputBinding
import org.bmsk.pinease.util.ViewUtil.hideKeyboard
import org.bmsk.pinease.util.ViewUtil.setOnEditorActionListener
import org.bmsk.pinease.util.ViewUtil.showKeyboard
import org.bmsk.pinease.util.ViewUtil.showKeyboardDelay

class IdentityInputActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIdentityInputBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIdentityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.view = this
        initView()
        binding.nameEdit.showKeyboardDelay()
    }

    private fun initView() {
        with(binding) {
            nameEdit.setOnEditorActionListener(EditorInfo.IME_ACTION_NEXT) {
                birthLayout.isVisible = true
                birthEdit.showKeyboard()
            }

            birthEdit.doAfterTextChanged {
                if (birthEdit.length() > 7) {
                    genderLayout.isVisible = true
                    birthEdit.hideKeyboard()
                }
            }

            genderChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                if (!telecomLayout.isVisible) {
                    telecomLayout.isVisible = true
                }
            }

            telecomChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                if (phoneLayout.isVisible.not()) {
                    phoneLayout.isVisible = true
                    phoneEdit.showKeyboard()
                }
            }

            phoneEdit.doAfterTextChanged {
                if (phoneEdit.length() > 10) {
                    confirmButton.isVisible = true
                    phoneEdit.hideKeyboard()
                }
            }

            phoneEdit.setOnEditorActionListener(EditorInfo.IME_ACTION_DONE) {
                if (phoneEdit.length() > 9) {
                    confirmButton.isVisible = true
                    phoneEdit.hideKeyboard()
                }
            }
        }
    }

    companion object {
        fun identityIntent(context: Context) = Intent(context, IdentityInputActivity::class.java)
    }
}