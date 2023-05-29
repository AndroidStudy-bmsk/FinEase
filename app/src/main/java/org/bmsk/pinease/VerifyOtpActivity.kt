package org.bmsk.pinease

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import org.bmsk.pinease.databinding.ActivityVerifyOtpBinding
import org.bmsk.pinease.util.ViewUtil.setOnEditorActionListener
import org.bmsk.pinease.util.ViewUtil.showKeyboardDelay

class VerifyOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyOtpBinding
    private var timer: CountDownTimer? = object : CountDownTimer((3 * 60 * 1000), 1000) {
        override fun onTick(p0: Long) {
            val min = (p0 / 1000) / 60
            val sec = (p0 / 1000) % 60
            binding.timerTextView.text = "$min:${String.format("%02d", sec)}"
        }

        override fun onFinish() {
            binding.timerTextView.text = ""
            Toast.makeText(
                this@VerifyOtpActivity,
                "입력 제한시간을 초과하였습니다.\n다시 시도해주세요.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.view = this
        initView()
    }

    override fun onResume() {
        super.onResume()
        binding.otpCodeEdit.showKeyboardDelay()
    }

    override fun onDestroy() {
        clearTimer()
        super.onDestroy()
    }

    private fun initView() {
        startTimer()
        with(binding) {
            otpCodeEdit.doAfterTextChanged {
                if (otpCodeEdit.length() >= 6) {
                    stopTimer()
                }
            }

            otpCodeEdit.setOnEditorActionListener(EditorInfo.IME_ACTION_DONE) {

            }
        }
    }

    private fun startTimer() {
        timer?.start()
    }

    private fun stopTimer() {
        timer?.cancel()
    }

    private fun clearTimer() {
        stopTimer()
        timer = null
    }

    companion object {
        fun verifyOtpIntent(context: Context) = Intent(context, VerifyOtpActivity::class.java)
    }
}