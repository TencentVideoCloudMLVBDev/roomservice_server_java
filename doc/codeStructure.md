## 项目结构
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