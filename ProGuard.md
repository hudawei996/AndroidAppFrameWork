# 为什么要启动ProGuard(所有试验都在AS 环境)
代码压缩通过 ProGuard 提供，ProGuard 会检测和移除封装应用中未使用的类、字段、方法和属性，
包括自带代码库中的未使用项（这使其成为以变通方式解决 64k 引用限制的有用工具）。ProGuard
还可优化字节码，移除未使用的代码指令，以及用短名称混淆其余的类、字段和方法。混淆过的代码
可令您的 APK 难以被逆向工程，这在应用使用许可验证等安全敏感性功能时特别有用。

## 启用压缩代码步骤<一>
```
首先，你应该在项目的Gradle 中 做如下的定义
android {
    buildTypes {
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile(‘proguard-android.txt'),
                    'proguard-rules.pro'
        }
    }
    ...
}
```

>请注意，代码压缩会拖慢构建速度，因此您应该尽可能避免在调试构建中使用。不过，重要的是您一定要为
>用于测试的最终 APK 启用代码压缩，因为如果您不能充分地自定义要保留的代码，可能会引入错误
>注：Android Studio 会在使用 Instant Run 时停用 ProGuard。

## 启用压缩代码步骤<二>
在gradle 文件中定义好release 后我们会发现有这么一行代码
>proguardFiles getDefaultProguardFile(‘proguard-android.txt'),'proguard-rules.pro'

* proguard-rules.pro 是你项目中的混淆配置文件，你可以根据你的项目配置你的混淆规则，当然
  这是可以模板化的，配置实例请查看这里
* proguard-android.txt 是开发环境默认的配置，你可以查看里面的内容，你也可以使用
    proguard-android-optimize.txt；两者的区别 请点击这里

#简单的模板
  我建议gradle 中配置proguard-android-optimize.txt。风险？风险无处不在呀，来吧
  使用 proguard-android-optimize 后要添加以下配置，否知http请求的方法内部无法传参
  -keepclasseswithmembers class * {
      @retrofit2.http.* <methods>;
  }
  


#proguard-android-optimize.txt 与 proguard-android.txt的区别
  区别简单的说就是proguard-android-optimize.txt开启了优化（没有配置 -dontoptimize）并且
  配置了optimizations过滤算法：
 ```
------------------------proguard-android.txt---------------------------------
# 这个不需要翻译了吧，简单的就是配置了dontoptimize（不要开启优化）
# Optimization is turned off by default. Dex does not like code run
# through the ProGuard optimize and preverify steps (and performs some
# of these optimizations on its own).

# 不要开启优化 do not optimize
-dontoptimize
# 不要开启预校验，移动端都不需要开启，do not preverify
-dontpreverify


# Note that if you want to enable optimization, you cannot just
# include optimization flags in your own project configuration file;
# instead you will need to point to the
# "proguard-android-optimize.txt" file instead of this one from your
# project.properties file.


------------------------proguard-android-optimize.txt---------------------------

# Optimizations: If you don't want to optimize, use the
# proguard-android.txt configuration file instead of this one, which
# turns off the optimization flags.  Adding optimization introduces
# certain risks, since for example not all optimizations performed by
# ProGuard works on all versions of Dalvik.  The following flags turn
# off various optimizations known to have issues, but the list may not
# be complete or up to date. (The "arithmetic" optimization can be
# used if you are only targeting Android 2.0 or later.)  Make sure you
# test thoroughly if you go this route.

#翻译：如果你不需要优化，你就使用proguard-android.txt 配置吧（已经配置关闭了优化)
#警告：添加优化会带来一定的风险，并非所有ProGuard执行的优化都适用于所有版本的Dalvik。
#下面的配置经过一序列的实践可以显著降低风险，但是并非万能，所以请进行详细的测试吧
#不然使用的时候出问题了不要怪我。（呵呵我们适配的最小API 是15呢）

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
 ```
