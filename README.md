# 这里是草稿区域

##
有一个很重要的问题，MyLibrary工程有它自己的依赖。例如，MyApplication和MyLibrary使用support libraries，这有可能会发生冲突。
我们可以在gradle中通过transitive配置依赖避免这个问题。
我们可以修改这行
compile project(':my-library')
为
compile(project(':my-library')){
    transitive=false;
}




#这里是答疑区域
- 为什么启动页面的bg图片放在不同的页面中会有导致占用的内存大小不一样


这个问题就可以避免。Done!
-------------------------------------------------------------------------------------------
# 为什么还要封装App 应用框架
  首先，我要反对过度封装；封装的目的是为了减少重复代码，精简清晰化项目结构。


# 应用中使用的lib
  其实大部分的App 最难以做好的模块是Http，http 模块封装的好，做起业务来很方便。
  UX 部分多考虑Material design和动画Material-Animations
  ## 建议必选
  - 数据库 GreenDao3(如果有需要使用数据库，建议使用，配置很简洁了)
  - 过度动画 Material-Animations
  - Http请求 retrofit2
  - 列表数据展示 springview
  - 图片展示 universalimageloader不再维护了，可选用
  - 权限管理
  - 二维码 com.journeyapps:zxing-android-embedded

  ## 可选
  - 视图绑定和监听 butterknife/databing
  - rxjava,rxAndroid
  -
