# 腾讯云音视频多人会话解决方案服务端

## 1.项目简介
RTCRoom Server 是配合 live-room（用于直播连麦）和 rtc-room（用于视频通话）的后台组件，主要作用有：

- 生成推流、播放地址
- 生成IM签名，用于IM独立模式下的用户登录
- 管理IM聊天室，聊天室的创建和销毁、成员进出通知
- 双人/多人音视频管理视频位

**特别说明：**

- 房间管理采用 java对象直接在内存中进行管理。房间信息具有动态和实效性，因此没有采用数据库做持久存储，而是在内存中动态管理。

## 2.云服务开通

### 开通直播服务

#### 申请开通视频直播服务
进入 [直播管理控制台](https://console.cloud.tencent.com/live)，如果服务还没有开通，则会有如下提示:
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/live_open.png)
点击申请开通，之后会进入腾讯云人工审核阶段，审核通过后即可开通。

#### 配置直播码
直播服务开通后，进入【直播控制台】>【直播码接入】>【[接入配置](https://console.cloud.tencent.com/live/livecodemanage)】 完成相关配置，即可开启直播码服务：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/live_code.png)
点击【确定接入】按钮即可。

#### 获取直播服务配置信息
从直播控制台获取`APP_ID`、`APP_BIZID`、`API_KEY`，后面配置服务器会用到：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/live_config.png)

### 开通云通信服务
#### 申请开通云通讯服务
进入[云通讯管理控制台](https://console.cloud.tencent.com/avc)，如果还没有服务，直接点击**直接开通云通讯**按钮即可。新认证的腾讯云账号，云通讯的应用列表是空的，如下图：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/im_open.png)

点击**创建应用接入**按钮创建一个新的应用接入，即您要接入腾讯云IM通讯服务的App的名字，我们的测试应用名称叫做“RTMPRoom”，如下图所示：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/im_new.png)

点击确定按钮，之后就可以在应用列表中看到刚刚添加的项目了，如下图所示：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/im_list.png)

#### 配置独立模式
上图的列表中，右侧有一个**应用配置**按钮，点击这里进入下一步的配置工作，如下图所示。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/im_config.png)

#### 获取云通讯服务配置信息
从云通信控制台获取`IM_SDKAPPID`、`IM_ACCOUNTTYPE`、`ADMINISTRATOR`、`PRIVATEKEY`、`PUBLICKEY`，后面配置服务器会用到：
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/im_config_info.png)

从验证方式中下载公私钥，解压出来将private_key用文本编辑器打开，如：

```bash
-----BEGIN PRIVATE KEY-----
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-----END PRIVATE KEY-----
```

将其转换成字符串形式如下所示，后面在server配置文件中使用，<font color='red'>请注意每行后面要加入\r\n</font>：

```bash
"-----BEGIN PRIVATE KEY-----\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
"-----END PRIVATE KEY-----\r\n"
```
PUBLICKEY也采用同样的方式编辑，供后续使用。

## 3.修改配置信息
后台使用 spring 框架搭建，开发环境是 IntelliJ IDEA，java 需要使用 1.8 或以上。用 IntelliJ IDEA 导入工程源码，把`\src\main\java\com\tencent\qcloud\roomservice\common`目录下的 Config.java 中`APP_ID、APP_BIZID、PUSH_SECRET_KEY、APIKEY、IM_SDKAPPID、IM_ACCOUNTTYPE、ADMINISTRATOR、PRIVATEKEY、PUBLICKEY`等配置项替换成您的腾讯云账号信息。

```java
public class Config {

    /**
     * 需要开通云直播服务
     * 参考指引 @https://cloud.tencent.com/document/product/454/15187#.E4.BA.91.E6.9C.8D.E5.8A.A1.E5.BC.80.E9.80.9A
     * 有介绍APP_BIZID 和 PUSH_SECRET_KEY的获取方法。
     */
    public class Live {
        /**
         * 云直播 APP_ID =  和 APIKEY 主要用于腾讯云直播common cgi请求。appid 用于表示您是哪个客户，APIKey参与了请求签名sign的生成。
         * 后台用他们来校验common cgi调用的合法性
         */
        public final static int APP_ID = 0;

        /**
         * 云直播 APP_BIZID = 和pushSecretKey 主要用于推流地址的生成，填写错误，会导致推流地址不合法，推流请求被腾讯云直播服务器拒绝
         */
        public final static int APP_BIZID = 0;

        /**
         * 云直播 推流防盗链key = 和 APP_BIZID 主要用于推流地址的生成，填写错误，会导致推流地址不合法，推流请求被腾讯云直播服务器拒绝
         */
        public final static String PUSH_SECRET_KEY = "";

        /**
         * 云直播 API鉴权key = 和appID 主要用于common cgi请求。appid 用于表示您是哪个客户，APIKey参与了请求签名sign的生成。
         * 后台用他们来校验common cgi调用的合法性。
         */
        public final static String APIKEY = "";

        // 云直播 推流有效期单位秒 默认7天
        public final static int validTime = 3600 * 24 * 7;
    }

    /**
     * 需要开通云通信服务
     * 参考指引 @https://cloud.tencent.com/document/product/454/7953#3.-.E4.BA.91.E9.80.9A.E8.AE.AF.E6.9C.8D.E5.8A.A1.EF.BC.88im.EF.BC.89
     * 有介绍appid 和 accType的获取方法。以及私钥文件的下载方法。
     */
    public class IM {
        /**
         * 云通信 IM_SDKAPPID = IM_ACCOUNTTYPE 和 PRIVATEKEY 是云通信独立模式下，为您的独立账号 identifer，
         * 派发访问云通信服务的userSig票据的重要信息，填写错误会导致IM登录失败，IM功能不可用
         */
        public final static long IM_SDKAPPID = 0;

        /**
         * 云通信 账号集成类型 IM_ACCOUNTTYPE = IM_SDKAPPID 和 PRIVATEKEY 是云通信独立模式下，为您的独立账户identifer，
         * 派发访问云通信服务的userSig票据的重要信息，填写错误会导致IM登录失败，IM功能不可用
         */
        public final static String IM_ACCOUNTTYPE = "";

        // 云通信 管理员账号
        public final static String ADMINISTRATOR = "admin";

        /**
         * 云通信 派发usersig 采用非对称加密算法RSA，用私钥生成签名。PRIVATEKEY就是用于生成签名的私钥，私钥文件可以在互动直播控制台获取
         * 配置privateKey
         * 将private_key文件的内容按下面的方式填写到 PRIVATEKEY。
         */
        public final static String PRIVATEKEY = "-----BEGIN PRIVATE KEY-----\r\n"+
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
			"-----END PRIVATE KEY-----\r\n";
        /**
         * 云通信 验证usersig 所用的公钥
         */
        public final static String PUBLICKEY = "-----BEGIN PUBLIC KEY-----\n" +
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
			"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\r\n"+
			"-----END PUBLIC KEY-----\n";
    }


    /**
     * 多人音视频房间相关参数
     */
    public class MultiRoom {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;
    }

    /**
     * 双人音视频房间相关参数
     */
    public class DoubleRoom {
        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;
    }

    /**
     * 直播连麦房间相关参数
     */
    public class LiveRoom {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;

        // 最大观众列表长度
        public final static int maxAudiencesLen = 30;
    }
    
    /**
     * 创建者退出的时候是否需要删除房间
     * 默认false。表示房间所有成员是对等的，第一个进房的人退出并不会销毁房间，只有房间没人的时候才会销毁房间。
     * 此配置项只针对双人和多人实时音视频
     */
    public final static boolean isCreatorDestroyRoom = false;
}
```
## 4.准备发布包
配置修改好之后，选择 Build -> Build Artifacts 开始打包，打包完成后到输出路径拿到 roomservice.war 包。选择 File -> Project Structure 可以查看输出路径，如下图中的 E:\RoomService\rtcroom_server_java-master\target。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/build_war.png)

## 5.部署服务器
以部署到腾讯云服务器为例，描述部署过程。采用CentOS + nginx + Apache Tomcat + java 的 环境。

> 注意：
> jdk版本需要为1.8以上。
> 小程序和ios都要求服务器支持HTTPS请求。

### 申请云服务器
1、新建 CVM 主机。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/new_cvm.png)

2、从服务市场选取镜像。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/cvm_image.png)

3、配置硬盘、网络、云主机访问密码，并且妥善保管好密码，然后设置安全组。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/config_cvm.png)

4、付款后生成云主机。点击登录可以通过腾讯云的网页 shell 进行访问，也可以用 Filezilla 等工具登录到主机。
4、付款后生成云主机。点击登录可以通过腾讯云的网页 shell 进行访问。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/cvm_info.png)

5、查看/切换 JDK 版本。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/change_jdk.png)

修改完成后可以用 `sh version.sh` 命令查看是否修改成功。

使用 `sudo update-alternatives --display java` 命令可以查看当前已安装的 JDK 版本。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/check_jdk.png)

### 部署war包到服务器
将之前打包好的 roomservice.war 包上传到服务器 tomcat 的 webapps 目录下。和远程服务器通讯一般走 ssh 连接，可以通过 Filezilla 连接服务器。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/upload_war.png)

上传完成后，如果 tomcat 服务未启动，可以进入 tomcat/bin 目录，通过 ./startup.sh start 命令启动 tomcat。“Tomcat started.”表示服务启动成功。
![](https://github.com/TencentVideoCloudMLVBDev/roomservice_java/raw/master/image/start_tomcat.png)

### nginx 配置
如果您已经有**域名**以及域名对应的**SSL证书**存放在`/data/release/nginx/`目录下，请将下面配置内容中的
- [1] 替换成您自己的域名。
- [2-1] 替换成 SSL 证书的 crt 文件名。
- [2-2] 替换成 SSL 证书的 key 文件名。

```
upstream app_weapp {
    server localhost:5757;
    keepalive 8;
}

#http请求转为 https请求
server {
    listen      80;
    server_name [1]; 

    rewrite ^(.*)$ https://$server_name$1 permanent;
}

#https请求
server {
    listen      443;
    server_name [1];

    ssl on;

    ssl_certificate           /data/release/nginx/[2-1];
    ssl_certificate_key       /data/release/nginx/[2-2];
    ssl_session_timeout       5m;
    ssl_protocols             TLSv1 TLSv1.1 TLSv1.2;
    ssl_ciphers               ECDHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:ECDHE-RSA-AES256-SHA:ECDHE-RSA-AES128-SHA:DHE-RSA-AES256-SHA:DHE-RSA-AES128-SHA;
    ssl_session_cache         shared:SSL:50m;
    ssl_prefer_server_ciphers on;

    # tomcat默认端口是8080，转发给tomcat处理
    location / {
        proxy_pass   http://127.0.0.1:8080;
        proxy_redirect  off;
        proxy_set_header  X-Real-IP $remote_addr;
        proxy_set_header  X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

### 运行服务
输入命令，启动Nginx服务。
```
nginx -s reload
```

### 验证服务
通过浏览器地址栏访问接口，请求地址格式 https://您自己的域名/roomservice/weapp/utils/get_login_info

## 6.小程序和windows Demo部署
#### 1.小程序部署
下载 [小程序](https://github.com/TencentVideoCloudMLVBDev/RTCRoomDemo) 源码，将 wxlite/config.js 文件中的`serverUrl`和 `roomServiceUrl`修改成:
```
https://您自己的域名/roomservice/
```

#### 2.windows Demo部署
下载 [windows web demo](https://github.com/TencentVideoCloudMLVBDev/webexe_web) 源码，将 liveroom.html、double.html 文件中的`RoomServerDomain`修改成:
```
https://您自己的域名/roomservice/
```

## 7.开发者资源
* [项目结构](https://github.com/TencentVideoCloudMLVBDev/rtcroom_server_java/blob/master/doc/codeStructure.md) - 后台源码结构
* [协议文档](https://github.com/TencentVideoCloudMLVBDev/rtcroom_server_java/blob/master/doc/protocol.md) - 后台协议文档
