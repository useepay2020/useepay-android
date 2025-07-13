# UseePay Payment SDK Integration and Usage Guide
## English | [中文](README_ZH_CN.md)
## Table of Contents
- [Prerequisites](#prerequisites)
- [Supported Versions](#supported-versions)
- [Running the Demo](#running-the-demo)
- [Integration Guide](#integration-guide)
    - [Import Dependencies](#import-dependencies)
    - [Initialize the SDK](#initialize-the-sdk)
    - [Create PaymentSheet](#create-paymentsheet)
    - [Present PaymentSheet](#present-paymentsheet)
    - [Handle Payment Result](#handle-payment-result)

## Prerequisites
Please make sure you have registered and applied for App payment access in the UseePay merchant dashboard. Below are the merchant dashboard URLs:
- [Sandbox](https://mc1.uat.useepay.com/#/login)
- [Production](https://mc.useepay.com/#/login)

## Supported Versions
- Minimum supported version is Android 7.0 and above (>= Android SDK API 24)

## Running the Demo
Edit the `UseePayApp.kt` file and fill in your merchant number and target environment:
```kotlin
// Configure the merchant no and env
UseePay.initialize("MERCHANT_NO", UseePayEnv.SANDBOX)
```

After editing, run the `app`. The screen will display two input fields:
- payment id
- client secret

These values should be obtained by the client from your server. After entering them, click the **Checkout** button to show the UseePay Payment Sheet.

## Integration Guide
> **Note**: All code examples below are in Kotlin.

### Import Dependencies
```gradle
implementation 'com.useepay.android:payment-core:0.0.9-alpha'
```

### Initialize the SDK
You can initialize in `Application#onCreate`. The SDK has no time-consuming operations internally:
```kotlin
UseePay.initialize("MERCHANT_NO", UseePayEnv.SANDBOX)
```

`UseePayEnv` has two enum values:
- SANDBOX – Sandbox environment
- PRODUCTION – Production environment

### Create PaymentSheet
```kotlin
// create paymentsheet
val paymentSheet = UseePay.createPaymentSheet(Context, PaymentResultCallback)
```

The `createPaymentSheet` method takes two parameters:
- `Context`
- `PaymentResultCallback` — the callback for handling the payment result (described below)

### Present PaymentSheet
You need to obtain the `payment id` and `client secret` from your server:
```kotlin
paymentSheet.launchWithPaymentIntent(paymentId: String, clientSecret: String, configuration: PaymentSheetConfiguration)
```

Here's how to build a `PaymentSheetConfiguration`:
```kotlin
PaymentSheetConfiguration.Builder(
    PaymentSheetTheme.FOLLOW_SYSTEM
)
    // optional: set background color for payment button in light and dark theme
    .setPrimaryButtonHexColors({hex button background color for light mode}, {hex button background color for dark mode})
    // optional: set text color for payment button in light and dark theme
    .setPrimaryButtonTextHexColors({hex text color for light mode}, {hex text color for dark mode})
    // optional: whether to show UseePay's built-in payment result UI, default value is true
    .setShowPaymentResult(true)
    .build()
```

`PaymentSheetTheme` enum values:
- FOLLOW_SYSTEM – Follow system theme
- ALWAYS_DARK – Always dark mode
- ALWAYS_LIGHT – Always light mode

### Handle Payment Result
As mentioned above, when calling `createPaymentSheet`, you need to pass in an instance of `PaymentResultCallback`. Here's how to handle it:
```kotlin
val paymentSheet = UseePay.createPaymentSheet(this) { result ->
    when (result) {
        is PaymentComplete -> {
            // Payment is complete, but not necessarily successful
        }

        is PaymentCancelled -> {
            // User cancelled the payment
        }

        is PaymentFailed -> {
            // Payment failed
        }
    }
}
```

**Note:** `PaymentComplete` does not guarantee a successful payment. It might be successful, pending confirmation from the bank, or even fail eventually. After receiving the payment callback, you should verify the result with your backend.
