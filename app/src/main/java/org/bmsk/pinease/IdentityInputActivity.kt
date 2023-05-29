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
                if (validName()) {
                    nameLayout.error = null
                    if (phoneLayout.isVisible) {
                        confirmButton.isVisible = true
                    } else {
                        birthLayout.isVisible = true
                        birthEdit.showKeyboard()
                    }
                } else {
                    confirmButton.isVisible = false
                    nameLayout.error = getString(R.string.guide_input_text_kor_length_min2)
                }
            }

            birthEdit.doAfterTextChanged {
                if (birthEdit.length() > 7) {
                    if (validBirthday()) {
                        birthLayout.error = null
                        if (phoneLayout.isVisible) {
                            confirmButton.isVisible = true
                        } else {
                            genderLayout.isVisible = true
                            birthEdit.hideKeyboard()
                        }
                    }
                } else {
                    genderLayout.isVisible = false
                    birthLayout.error = getString(R.string.guide_invalid_birth_format)
                }
            }

            birthEdit.setOnEditorActionListener(EditorInfo.IME_ACTION_DONE) {
                val isValid = validBirthday() && birthEdit.length() > 7
                if (isValid) {
                    confirmButton.isVisible = phoneLayout.isVisible
                    birthLayout.error = null
                } else {
                    birthLayout.error = getString(R.string.guide_invalid_birth_format)
                }
                birthEdit.hideKeyboard()
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
                    if (validPhone()) {
                        phoneLayout.error = null
                        confirmButton.isVisible = true
                        phoneEdit.hideKeyboard()
                    }
                } else {
                    confirmButton.isVisible = false
                    phoneLayout.error = getString(R.string.guide_invalid_phone_number_format)
                }
            }

            phoneEdit.setOnEditorActionListener(EditorInfo.IME_ACTION_DONE) {
                confirmButton.isVisible = phoneEdit.length() > 10 && validPhone()
                phoneEdit.hideKeyboard()
            }
        }
    }

    fun onClickDone() {
        if (!validName()) {
            binding.nameLayout.error = getString(R.string.guide_input_text_kor_length_min2)
            return
        }

        if (!validBirthday()) {
            binding.birthLayout.error = getString(R.string.guide_invalid_birth_format)
            return
        }

        if (!validPhone()) {
            binding.phoneLayout.error = getString(R.string.guide_invalid_phone_number_format)
            return
        }

        startActivity(VerifyOtpActivity.verifyOtpIntent(this))
    }

    private fun validName() = !binding.nameEdit.text.isNullOrBlank()
            && REGEX_NAME.toRegex().matches(binding.nameEdit.text!!)

    private fun validBirthday() = !binding.birthEdit.text.isNullOrBlank()
            && REGEX_BIRTHDAY.toRegex().matches(binding.birthEdit.text!!)

    private fun validPhone() = !binding.phoneEdit.text.isNullOrBlank()
            && REGEX_PHONE.toRegex().matches(binding.phoneEdit.text!!)

    companion object {
        private const val REGEX_NAME = "^[가-힣]{2,}\$"
        private const val REGEX_BIRTHDAY =
            "^(19|20)[0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1])"
        private const val REGEX_PHONE = "^01([016789])([0-9]{3,4})([0-9]{4})\$"
        fun identityIntent(context: Context) = Intent(context, IdentityInputActivity::class.java)
    }
}