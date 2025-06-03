package com.useepay.android.useepaydemo

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.useepay.android.core.consts.PaymentSheetTheme
import com.useepay.android.core.paymentsheet.UseePay
import com.useepay.android.core.paymentsheet.cfg.PaymentSheetConfiguration
import com.useepay.android.core.paymentsheet.result.PaymentCancelled
import com.useepay.android.core.paymentsheet.result.PaymentComplete
import com.useepay.android.core.paymentsheet.result.PaymentFailed

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val paymentIdET = findViewById<EditText>(R.id.payment_id_et)
        val clientSecretET = findViewById<EditText>(R.id.client_secret_et)
        findViewById<Button>(R.id.checkout_btn).setOnClickListener {
            val pid = paymentIdET.text?.toString()
            if (TextUtils.isEmpty(pid)) {
                Toast.makeText(this@MainActivity, R.string.payment_id_required, Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            val clientSecret = clientSecretET.text?.toString()
            if (TextUtils.isEmpty(clientSecret)) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.client_secret_required,
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }
            // create paymentsheet
            val paymentSheet = UseePay.createPaymentSheet(this) { result ->

                val text = when (result) {
                    is PaymentComplete -> {
                        "PaymentComplete"
                    }

                    is PaymentCancelled -> {
                        "PaymentCancelled"

                    }

                    is PaymentFailed -> {
                        "PaymentFailed"
                    }

                    else -> {
                        "Unknown"
                    }
                }
                Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
            }
            // launch paymentsheet
            paymentSheet.launchWithPaymentIntent(
                pid,
                clientSecret,
                // paymentsheet configuration
                PaymentSheetConfiguration.Builder(
                    PaymentSheetTheme.FOLLOW_SYSTEM
                )
                    // configure the background color for payment button in light and dark theme
                    .setPrimaryButtonHexColors("#000000", "#ff00ff")
                    // configure the text color for payment button in light and dark theme
                    .setPrimaryButtonTextHexColors("#ffffff", "#ffffff")
                    // whether show UseePay's payment result ui
                    .setShowPaymentResult(true)
                    .build()
            )
        }
    }
}