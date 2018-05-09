## 项目结构
```
roomserver
├── README.md                 //项目总体说明文档
├── pom.xml                   //maven 依赖配置文件
└── src/main
    ├── java/com/tencent/qcloud/roomservice
    │   ├── common
    │   │   ├── CORSFilter.java           //跨域设置类
    │   │   └── Config.java               //后台配置，需要修改这里
    │   ├── controller
    │   │   ├── DoubleRoom.java           //双人房间后台协议总入口
    │   │   ├── LiveRoom.java             //直播房间后台协议总入口
    │   │   ├── MultiRoom.java            //多人房间后台协议总入口
    │   │   └── Util.java                 //通用后台协议总入口
    │   ├── logic
    │   │   ├── IMMgr.java                //云通信逻辑实现
    │   │   ├── LiveUtil.java             //流管理逻辑实现
    │   │   └── RoomMgr.java              //房间管理实现
    │   ├── pojo
    │   │   ├── Audience.java             //观众结构定义
    │   │   ├── Pusher.java               //推流者结构定义
    │   │   ├── Request
    │   │   │   ├── AddAudienceReq.java          //增加观众请求定义
    │   │   │   ├── AddPusherReq.java            //增加推流者请求定义
    │   │   │   ├── CreateGroupReq.java          //建群请求定义，后台调用云通信rest api创建群组时候用到
    │   │   │   ├── CreateRoomReq.java           //建房请求定义
    │   │   │   ├── DelAudienceReq.java          //删除观众请求定义
    │   │   │   ├── DeletePusherReq.java         //删除推流者请求定义
    │   │   │   ├── DestroyGroupReq.java         //销毁群请求定义，后台调用云通信rest api销毁群组时候用到
    │   │   │   ├── DestroyRoomReq.java          //销毁房间请求定义
    │   │   │   ├── GetAudiencesReq.java         //获取观众列表请求定义
    │   │   │   ├── GetCustomInfoReq.java        //获取直播房间自定义信息请求定义
    │   │   │   ├── GetLoginInfoReq.java         //获取登录信息请求定义
    │   │   │   ├── GetPushersReq.java           //获取推流者列表请求定义
    │   │   │   ├── GetPushUrlReq.java           //获取推流地址请求定义
    │   │   │   ├── GetRoomListReq.java          //获取房间列表请求定义
    │   │   │   ├── NotifyPusherChangeReq.java   //通知推流者成员变化请求定义，后台调用云通信rest api发送群系统通知pusher变化的时候用到
    │   │   │   ├── PusherHeartbeatReq.java      //pusher心跳请求定义
    │   │   │   └── SetCustomInfoReq.java        //直播房间设置自定义信息请求定义
    │   │   ├── Response
    │   │   │   ├── BaseRsp.java。               //回包通用定义
    │   │   │   ├── CreateRoomRsp.java           //建房回包定义
    │   │   │   ├── GetAudiencesRsp.java         //获取观众列表回包定义
    │   │   │   ├── GetCustomInfoRsp.java        //获取直播房间设置自定义信息回包定义
    │   │   │   ├── GetLoginInfoRsp.java         //获取登录信息回包定义
    │   │   │   ├── GetPushUrlRsp.java           //获取推流地址回包定义
    │   │   │   ├── GetRoomListRsp.java          //获取房间列表回包定义
    │   │   │   ├── GetStreamStatusOutPut.java   //获取流状态回包定义
    │   │   │   ├── GetStreamStatusRsp.java      //获取流状态回包定义，包含GetStreamStatusOutPut
    │   │   │   ├── GetTestPushUrlRsp.java       //获取一组推流播放地址回包定义
    │   │   │   ├── GetTestRtmpAccUrlRsp.java    //获取加速拉流地址回包定义
    │   │   │   ├── LoginRsp.java                //登录回包定义
    │   │   │   └── MergeStreamRsp.java          //混流回包定义
    │   │   ├── Room.java                        //房间结构定义
    │   │   └── StreamIDS.java                   //流id结构定义
    │   ├── service
    │   │   ├── impl 
    │   │   │   ├── RoomServiceImpl.java         //房间管理服务service实现
    │   │   │   └── UtilServiceImpl.java         //通用service实现
    │   │   ├── RoomService.java                 //房间管理服务service接口
    │   │   └── UtilService.java                 //通用service接口
    │   ├── utils
    │   │   ├── RestTemplateConfig.java          //http请求配置
    │   │   └──  Utils.java                      //通用类
    ├── java/com/tls
    │   ├── base64_url
    │   │   └── base64_url.java                  //base64编解码类
    │   ├── tls_sigature   
    │   │   └── tls_sigature.java                //云通信签名生成和校验类
    ├── resources
    │   ├── applicationContext.xml               //spring配置文件
    │   └── logback.xml                          //log配置文件
    └── webapp/WEB-INF
        ├── dispatcher-servlet.xml               //spring配置文件
        ├── web.xml                              //后台配置文件
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