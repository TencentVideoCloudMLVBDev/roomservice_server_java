#腾讯云音视频多人会话解决方案服务端

## 1.项目简介
在构建直播业务，多人音视频业务等场景下，都需要后台配合完成诸如：
[x] 生成直播地址，包括推流和播放地址
[x] 生成IM签名，用于IM独立模式下的用户登录
[x] 管理IM聊天室，聊天室的创建和销毁还有成员进出通知
[x] 双人/多人音视频管理视频位。
以上这些都有一定的学习成本，为了**降低学习成本**，我们将后台封装了一套接口，来解决以上问题。再配合IOS，Android，小程序和Win PC端的后台调用封装。对应用开发者提供一套友好的接口，方便您实现多人实时音视频，直播，聊天等业务场景。

**特别说明：**
**[1] 后台没有对接口的调用做安全校验，这需要您结合您自己的账号和鉴权体系，诸如在请求接口上加一个Sig参数，内容是您账号鉴权体系派发的一个字符串，用于校验请求者的身份。**
**[2] 房间管理采用 java对象直接在内存中进行管理。房间信息动态和实效性，因此没有采用数据库做持久存储，而是在内存中动态管理。**


## 云服务开通

### 开通直播服务

#### 申请开通视频直播服务
进入 [直播管理控制台](https://console.cloud.tencent.com/live)，如果服务还没有开通，则会有如下提示:
![](https://mc.qcloudimg.com/static/img/c40ff3b85b3ad9c0cb03170948d93555/image.png)
点击申请开通，之后会进入腾讯云人工审核阶段，审核通过后即可开通。


#### 配置直播码
直播服务开通后，进入【直播控制台】>【直播码接入】>【[接入配置](https://console.cloud.tencent.com/live/livecodemanage)】 完成相关配置，即可开启直播码服务：
![](https://mc.qcloudimg.com/static/img/32158e398ab9543b5ac3acf5f04aa86e/image.png)
点击【确定接入】按钮即可。

#### 获取直播服务配置信息
从直播控制台获取`APP_ID`、`APP_BIZID`、`API_KEY`，后面配置服务器会用到：
![](https://main.qcloudimg.com/raw/b958c4d3ad29fd6114f92e0c8f7ca458.png)

### 开通云通信服务
#### 申请开通云通讯服务
进入[云通讯管理控制台](https://console.cloud.tencent.com/avc)，如果还没有服务，直接点击**直接开通云通讯**按钮即可。新认证的腾讯云账号，云通讯的应用列表是空的，如下图：
![](https://mc.qcloudimg.com/static/img/c033ddba671a514c7b160e1c99a08b55/image.png)

点击**创建应用接入**按钮创建一个新的应用接入，即您要接入腾讯云IM通讯服务的App的名字，我们的测试应用名称叫做“RTMPRoom演示”，如下图所示：
![](https://mc.qcloudimg.com/static/img/96131ecccb09ef06e50aa0ac591b802d/yuntongxing1.png)

点击确定按钮，之后就可以在应用列表中看到刚刚添加的项目了，如下图所示：
![](https://mc.qcloudimg.com/static/img/168928a60c0b4c07a2ee2c318e0b1a62/yuntongxing2.png)

#### 配置独立模式
上图的列表中，右侧有一个**应用配置**按钮，点击这里进入下一步的配置工作，如下图所示。
![](https://mc.qcloudimg.com/static/img/3e9cd34ca195036e21cb487014cc2c81/yuntongxing3.png)

#### 获取云通讯服务配置信息
从云通信控制台获取`IM_SDKAPPID`、`IM_ACCOUNTTYPE`、`ADMINISTRATOR`、`PRIVATEKEY`、`PUBLICKEY`，后面配置服务器会用到：
![](https://main.qcloudimg.com/raw/13ea29f1692106bafd9895e7624e167a.png)

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

## 修改配置信息
后台使用spring框架搭建，开发环境是IntelliJ IDEA，java需要使用1.8。用IntelliJ IDEA导入工程源码，修改Config.java 中`APP_ID、APP_BIZID、PUSH_SECRET_KEY、APIKEY、IM_SDKAPPID、IM_ACCOUNTTYPE、ADMINISTRATOR、PRIVATEKEY、PUBLICKEY`等配置项需要您替换成腾讯云账号下的值。
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
        public final static String PRIVATEKEY = "-----BEGIN PRIVATE KEY-----\n" +
                "\n" +
                "\n" +
                "\n" +
                "-----END PRIVATE KEY-----";
        /**
         * 云通信 验证usersig 所用的公钥
         */
        public final static String PUBLICKEY = "-----BEGIN PUBLIC KEY-----\n" +
                "\n" +
                "\n" +
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
}
```
配置修改好之后打成war包。

## 服务器部署
以CentOS 系统为例，描述部署过程。采用CentOS + nginx + Apache Tomcat + java 的 环境。小程序和IOS都要求服务器支持HTTPS请求。和远程服务器通讯一般走ssh连接，可以用工具Xshell，secureCRT连接服务器。对于小文件（小于100kB）可以用rz 命令从本机传送文件至服务器，以及sz命令从远程服务器下载文件。非常方便。

### 准备发布包
Config.java中的配置修改好之后打成war包。

### war包部署到服务器
将打包好的roomservice.war包通过rz命令上传到服务器 tomcat 的 webapps 目录下。通过 tomcat/bin 目录下的 startup.sh 脚本启动 tomcat。 

### nginx 配置
如果您已经有**域名**以及域名对应的**SSL证书**存放在`/data/release/nginx/`目录下，请将下面配置内容中的
[1] 替换成您自己的域名，
[2-1]替换成SSL证书的crt文件名，
[2-2]替换成SSL证书的key文件名。
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
通过Postman访问接口，就可以看到返回的json数据了。注意要按照接口定义正确填写body。
以双人房间建房为例，请求地址格式 https://您自己的域名/roomservice/weapp/double_room/create_room


## 小程序部署

下载[小程序](http://liteavsdk-1252463788.cosgz.myqcloud.com/xiaochengxu/RTCRoomRelease1.2.693.zip)源码，修改代码中的后台地址

小程序wxlite/config.js文件中的`serverUrl`、`roomServiceUrl`修改成 *https://您自己的域名/roomservice/*


## 附录

### 项目结构
```
roomserver
├── README.md
├── pom.xml
└── src/main
    ├── java/com/tencent/qcloud/roomservice
    │   ├── common
    │   │   └── Config.java
    │   ├── controller
    │   │   ├── DoubleRoom.java
    │   │   ├── LiveRoom.java
    │   │   ├── MultiRoom.java
    │   │   └── Util.java
    │   ├── logic
    │   │   ├── IMMgr.java
    │   │   ├── LiveUtil.java
    │   │   └── RoomMgr.java
    │   ├── pojo
    │   │   ├── Audience.java
    │   │   ├── Pusher.java
    │   │   ├── Request
    │   │   │   ├── AddAudienceReq.java
    │   │   │   ├── AddPusherReq.java
    │   │   │   ├── CreateGroupReq.java
    │   │   │   ├── CreateRoomReq.java
    │   │   │   ├── DelAudienceReq.java
    │   │   │   ├── DeletePusherReq.java
    │   │   │   ├── DestroyGroupReq.java
    │   │   │   ├── DestroyRoomReq.java
    │   │   │   ├── GetAudiencesReq.java
    │   │   │   ├── GetCustomInfoReq.java
    │   │   │   ├── GetLoginInfoReq.java
    │   │   │   ├── GetPushersReq.java
    │   │   │   ├── GetPushUrlReq.java
    │   │   │   ├── GetRoomListReq.java
    │   │   │   ├── NotifyPusherChangeReq.java
    │   │   │   ├── PusherHeartbeatReq.java
    │   │   │   └── SetCustomInfoReq.java
    │   │   ├── Response
    │   │   │   ├── BaseRsp.java
    │   │   │   ├── CreateRoomRsp.java
    │   │   │   ├── GetAudiencesRsp.java
    │   │   │   ├── GetCustomInfoRsp.java
    │   │   │   ├── GetLoginInfoRsp.java
    │   │   │   ├── GetPushUrlRsp.java
    │   │   │   ├── GetRoomListRsp.java
    │   │   │   ├── GetStreamStatusOutPut.java
    │   │   │   ├── GetStreamStatusRsp.java
    │   │   │   ├── GetTestPushUrlRsp.java
    │   │   │   ├── GetTestRtmpAccUrlRsp.java
    │   │   │   ├── LoginRsp.java
    │   │   │   └── MergeStreamRsp.java
    │   │   ├── Room.java
    │   │   └── StreamIDS.java
    │   ├── service
    │   │   ├── impl
    │   │   │   ├── RoomServiceImpl.java
    │   │   │   └── UtilServiceImpl.java
    │   │   ├── RoomService.java
    │   │   └── UtilService.java
    │   ├── utils
    │   │   ├── RestTemplateConfig.java
    │   │   └──  Utils.java
    ├── java/com/tls
    │   ├── base64_url
    │   │   └── base64_url.java
    │   ├── tls_sigature
    │   │   └── tls_sigature.java
    ├── resources
    │   ├── applicationContext.xml
    │   └── logback.xml
    └── webapp/WEB-INF
        ├── dispatcher-servlet.xml
        ├── web.xml
        └── lib
```
后台使用spring框架搭建，开发环境是IntelliJ IDEA，java需要使用1.8

`pom.xml` 是 maven 依赖配置文件。

`DoubleRoom.java` 是 服务器端 双人房间业务逻辑。

`LiveRoom.java` 是 服务器端 直播房间业务逻辑。

`MultiRoom.java` 是 服务器端 多人房间业务逻辑。

`Util.java` 是 服务器端 辅助业务逻辑。

`logic/IMMgr.java` 云通信相关的处理，主要功能有：
```
getLoginInfo,                // 获取云通信的登录信息，主要是计算云通信 账号登录IM所需要的userSig票据   
createGroup,                 // 创建IM聊天室，通过云通信提供的服务端对接用的RestFul API实现
destroyGroup,                // 销毁IM聊天室，通过云通信提供的服务端对接用的RestFul API实现 
notifyPushersChange          // IM聊天室成员进入和退出系统消息通知，通过云通信提供的服务端对接用的RestFul API实现
```

`logic/RoomMgr.java` 实时音视频房间管理模块，负责`双人`、`多人`、`直播`视频房间的创建，销毁，增加成员，删除成员，获取房间列表，获取房间成员列表等功能函数；另外也负责房间成员的心跳检查，对超时的成员进行删除处理。

`logic/LiveUtil.java` 云直播辅助函数，负责生成推流地址以及播放地址。外加一些用户ID分配和房间ID分配的功能函数。 

后台使用logback来生成日志，主要记录info、warn、error三大类日志。日志存储在服务器Apache-Tomcat的logs目录`{CATALINA_HOME}/logs/`下，根据级别日志文件名分别为`roomservice_info.log`、`roomservice_warn.log`、`roomservice_error.log`。以上默认配置可以通过修改`logback.xml`来调整。
日志配置：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoding>UTF-8</encoding>
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}][%p][%c{40}][%t] %m%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>DEBUG</level>
        </filter>
    </appender>

    <appender name="info" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${CATALINA_HOME}/logs/roomservice_info.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${CATALINA_HOME}/logs/roomservice_info.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
            <append>true</append>
            <maxHistory>10</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}][%p][%c{40}][%t] %m%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="warn" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${CATALINA_HOME}/logs/roomservice_warn.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${CATALINA_HOME}/logs/roomservice_warn.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
            <append>true</append>
            <maxHistory>10</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}][%p][%c{40}][%t] %m%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>


    <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <File>${CATALINA_HOME}/logs/roomservice_error.log</File>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${CATALINA_HOME}/logs/roomservice_error.log.%d{yyyy-MM-dd}.gz</fileNamePattern>
            <append>true</append>
            <maxHistory>10</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>[%d{HH:mm:ss.SSS}][%p][%c{40}][%t] %m%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <root level="INFO">
        <appender-ref ref="console"/>
        <appender-ref ref="info" />
        <appender-ref ref="warn" />
        <appender-ref ref="error"/>
    </root>

</configuration>
```


