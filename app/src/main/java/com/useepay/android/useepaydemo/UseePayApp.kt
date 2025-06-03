package com.useepay.android.useepaydemo

import android.app.Application
import com.useepay.android.core.consts.UseePayEnv
import com.useepay.android.core.paymentsheet.UseePay

class UseePayApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Configure the merchant no and env
        UseePay.initialize("MERCHANT_NO", UseePayEnv.SANDBOX)
    }
}