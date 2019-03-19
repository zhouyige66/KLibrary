# KLibrary
集合了多种框架的基础库，内置多种工具、视图。

## 集成
* xUtils:3.3.36
* autolayout:1.4.5
* fastjson:1.1.70.android
* PhotoView:2.0.0
* log4j:1.2.17
* pinyin4j:2.5.0
* universal-image-loader:1.9.5

## 主要功能
### 工具封装（Util***系列）
* 截图工具UtilCapture。
* 加密工具UtilEncrypt。
* 文件工具UtilFile，检测SD卡是否可用，创建文件。
* 网络请求工具UtilHttp，主要提供get、post和文件上传功能。
* Intent工具UtilIntent，主要提供获取拍照、获取图片裁剪、获取调用相册的Intent。
* Logcat工具UtilLog。
* MD5加密工具UtilMD5与UtilMD5File。
* 网络状态检测工具UtilNetwork，网络是否可用检测，IP地址获取，MAC地址获取等。
* 获取Android设备Id工具UtilPhoneId，获取imei、imsi、AndroidId、SerialNumber，6.0以上需要手工授权（android.Manifest.permission.READ_PHONE_STATE）。
* 拼音工具UtilPinyin，提取汉子首字母。
* Properties操作工具UtilProperties。
* 尺寸转换工具类UtilSize，dp、sp、px装换。
* 文本对其工具UtilTextJustification。
* 日期格式装换工具UtilTime。
* 应用级别的Toast工具，依赖xUtils3。
### 视图
* 圆形ImageView-CircleImageView
* 数字时钟TextView-DigitalClockTextView
* ScrollView中嵌套GridView-GridViewInScrollView
* ScrollView中嵌套ListView-ListViewInScrollView
* 简单圆形ProgressBar-RoundProgressBar
* 简单水平柱形图-HorizontalProgressBarView
* 简单垂直柱形图-VerticalProgressBarView
* 简单圆形比例图-ScaleCircleView
* 简单实现的涂鸦板-PaintView
* 简单区域截图视图-ScreenShotView，使用了PorterDuffXfermode

### 其他
* 日志存储，使用log4j框架
* 应用崩溃重启CrashHandler，CrashTipActivity
* BaseActivity、BaseApplication、BaseFragment
* 权限检查与授权PermissionsChecker、PermissionsActivity
* 弱引用Handler-WeakHandler
* 通用Adapter-CommonAdapter
* 通用提示对话框-CommonTipDialog
* 通用加载对话框-CommonLoadingDialog

## 使用
### Gradle配置:
```javascript
repositories {
	...
	maven { url 'https://jitpack.io' }
}
dependencies {
	implementation 'com.github.zhouyige66:KLibrary:1.0.4'
}
```

### 代码中使用
* Application继承BaseApplication。
* Activity可选择继承BaseActivity。

----
## 关于作者
* Email： <751664206@qq.com>
* 有任何建议或者使用中遇到问题都可以给我发邮件。