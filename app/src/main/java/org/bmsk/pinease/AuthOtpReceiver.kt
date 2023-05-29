package org.bmsk.pinease

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * 1. 문자 내용이 140바이트를 초과하면 안 된다.
 * 2. 문자 맨 앞에 <#> 가 포함되어야 한다.
 * 3. 맨 마지막에 앱을 식별하는 11글자 해시코드가 존재해야 한다.
 */

/* 양식:
<#> [Sample] 본인확인 인증번호 [123456] 입니다. 0QaSrUKkEGq
 */

class AuthOtpReceiver : BroadcastReceiver() {

    private var listener: OtpReceiverListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            intent.extras?.let { bundle ->
                val status = bundle.get(SmsRetriever.EXTRA_STATUS) as Status
                when (status.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val otpSms = bundle.getString(SmsRetriever.EXTRA_SMS_MESSAGE, "")
                        if (listener != null && otpSms.isNotEmpty()) {
                            val otp = PATTERN.toRegex().find(otpSms)?.destructured?.component1()
                            if (!otp.isNullOrBlank()) {
                                listener?.run { onOtpReceived(otp) }
                            }
                        }
                    }
                }
            }
        }
    }

    fun setOtpListener(receiveListener: OtpReceiverListener) {
        this.listener = receiveListener
    }

    fun doFilter() = IntentFilter().apply {
        addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
    }

    interface OtpReceiverListener {
        fun onOtpReceived(otp: String)
    }

    companion object {
        private const val PATTERN = "^<#>.*\\[Sample\\].+\\[(\\d{6})\\].+\$"
    }
}