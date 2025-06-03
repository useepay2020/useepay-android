# UseePay Payment SDK 集成与使用指南

## 目录
- [前置条件](#前置条件)
- [支持版本](#支持版本)
- [运行Demo](#运行demo)
- [集成指南](#集成指南)
    - [导入依赖](#导入依赖)
    - [初始化SDK](#初始化sdk)
    - [创建PaymentSheet](#创建paymentsheet)
    - [唤起PaymentSheet](#唤起paymentsheet)
    - [处理支付结果](#处理支付结果)
## 前置条件
请确保你已经在UseePay的商户后台注册并申请了相关App的支付, 下面是UseePay商户后台地址:
- [沙盒](https://mc1.uat.useepay.com/#/login)
- [正式](https://mc.useepay.com/#/login)

## 支持版本
- 最低支持 Android 7.0版本及以上( >= Android SDK API 24)

## 运行Demo
修改`UseePayApp.kt`文件, 填入你的商户号以及想要运行的环境
``` kotlin
// Configure the merchant no and env
UseePay.initialize("MERCHANT_NO", UseePayEnv.SANDBOX)
```
修改后运行`app`, 此时页面上会有两个输入框:
- payment id
- client secret

这两个输入框的值均为客户端从你的服务端获取, 填入后点击**Checkout**按钮即可展示UseePay Payment Sheet支付弹层

## 集成指南
> **注意**: 以下代码块均示例均为Kotlin语言
### 导入依赖
``` gradle
implementation 'com.useepay.android:payment-core:0.0.6-alpha'
```
### 初始化SDK
可以在`Application#onCreate`中进行初始化, SDK内部无耗时操作
```
UseePay.initialize("MERCHANT_NO", UseePayEnv.SANDBOX)
```
`UseePayEnv`有两个枚举值:
- SANDBOX 沙盒环境
- PRODUCTION 线上环境

### 创建PaymentSheet
``` kotlin
// create paymentsheet
val paymentSheet = UseePay.createPaymentSheet(Context, PaymentResultCallback)
```
`createPaymentSheet`方法接收两个参数:
- Context
- PaymentResultCallback 支付结果回调, 后面将介绍支付结果回调处理

### 唤起PaymentSheet
你需要从你的服务端获取本次订单用于支付的`payment id`与`client secret`
``` kotlin
paymentSheet.launchWithPaymentIntent(paymentId:String, clientSecret:String, configuration:PaymentSheetConfiguration)
```
PaymentSheetConfiguration的构建如下:
``` kotlin
PaymentSheetConfiguration.Builder(
    PaymentSheetTheme.FOLLOW_SYSTEM
)
    // optional, configure the background color for payment button in light and dark theme
    .setPrimaryButtonHexColors({亮色模式的按钮背景色16进制色值}, {暗色模式的按钮背景色16进制值})
    // optional, configure the text color for payment button in light and dark theme
    .setPrimaryButtonTextHexColors({亮色模式的按钮文本色16进制色值}, {暗色模式的按钮文本色16进制值})
    // whether show UseePay's payment result ui
    .setShowPaymentResult(true)
    .build()
```
`PaymentSheetTheme`枚举值如下:
- FOLLOW_SYSTEM 跟随系统
- ALWAYS_DARK 强制暗色模式
- ALWAYS_LIGHT 强制亮色模式

### 处理支付结果
前面提到了`createPaymentSheet`时需要传递`PaymentResultCallback`的实例作为参数, PaymentResultCallback的处理如下:
``` kotlin
val paymentSheet = UseePay.createPaymentSheet(this) { result ->
    when (result) {
        is PaymentComplete -> {
            // 支付完成，不代表支付成功
        }

        is PaymentCancelled -> {
            // 用户取消支付

        }

        is PaymentFailed -> {
            // 支付失败
        }
    }
}
```
**注意:**`PaymentComplete`不表示支付成功了，可能是成功，也可能是待银行确认，最终可能会失败也可能会成功，获取到支付回调后应当以你的服务端查询结果为准