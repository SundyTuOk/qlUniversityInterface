本工程开发配置注意事项：
1、maven中中央仓库不含有oracle jdbc的jar包（我将这个jar文件放在了classpath的lib文件夹下面），所以关于pom.xml中依赖ojdbc的块可能报错，
解决办法就是把依赖包安装到你本地的maven仓库，
详细可参考https://blog.csdn.net/sundy_tu/article/details/79472918

2、maven中央仓库没有kaptcha-2.3.2.jar（我将这个jar文件放在了classpath的lib文件夹下面），这个是首页关于验证码的依赖，所以关于pom.xml
中依赖kaptcha的块可能报错，解决办法就是把依赖包安装到你本地的maven仓库，
详细可参考https://blog.csdn.net/sundy_tu/article/details/79472918
kaptcha的使用方式可参考https://blog.csdn.net/educast/article/details/49535059