# 后台协议设计

## 1.说明
* 接口命名采用c风格，举例 get_push_url
* 接口参数命名采用驼峰风格，举例 roomID
* 统一用Https请求，请求方式POST，请求的body是Json格式。
## 2.参数一览
接下来接口的请求应答协议中会用到这些参数。
|参数名|类型|意义|
| :--------------------- | :--------------------| :------- |
|roomID|          String | 视频位房间ID|
|userID|          String | 用户ID（pusher/普通观众）|
|pushURL|         String | 推流地址|
|accelerateURL|   String | 加速拉流地址|
|mixedPlayURL|    String | 混流地址|
|roomCreator|     String | 视频位房间创建者|
|roomInfo|        String | 房间名称|
|userName|        Stirng | 用户昵称|
|userAvatar|      String | 用户头像URL|
|pusherCnt|       Int    | 视频位房间当前人数|
|userSig|         String | IM登录凭证|
|sdkAppID|        Int    | IM应用ID|
|accType|         String | IM账号集成类型|
|code|            Int    | 返回码|
|message|         String | 返回码说明|
|cnt|             Int    | 请求的房间个数|
|index|           Int    | 请求的房间索引开始位置|
## 3.房间管理接口请求应答
请求应答举例说明：
* `req`  = **cnt + index** 表示客户端向服务器的请求，翻译一下就是请求的body为Json串`{"cnt":0,"index":0}`，参数名和类型要求和参数一览里面的完全对应。
* `rsp` = **code + message + rooms(array)** 表示服务器的响应结果，翻译一下就是返回错误码code，错误信息message，以及房间列表是一个数组用`（array）`表示，房间列表的每一个元素的格式用单数形式的 **room** 来指代 `room` = roomID + roomName + roomCreator + mixedPlayURL ,这个响应的结果大概是这样的一个Json串`{"code":0,"message":"ok","rooms":[{"roomID":"room_1","roomName":"a string","roomCreator":"user_1", "mixedPlayURL":"a url"},{"roomID":"room_2","roomName":"a string","roomCreator":"user_2", "mixedPlayURL":"a url"}]}`
### 3.1.获取房间列表 - get_room_list
`req` = cnt + index
`rsp` = code + message + rooms(array)
room = roomID + roomInfo + roomCreator + mixedPlayURL + pushers(array)
pusher = userID + userName + userAvatar + accelerateURL

### 3.2.获取推流地址 - get_push_url
`req` = roomID + userID
`rsp` = code + message + pushURL

### 3.3.获取指定房间的成员信息 - get_pushers
`req` = roomID
`rsp` = code + message + roomID + roomName + roomCreator + mixedPlayURL + pushers(array)
pusher = userID + userName + userAvatar + accelerateURL

### 3.4.创建房间 - create_room
`req` = userID + roomInfo + roomID
`rsp` = code + message + roomID

### 3.5.销毁房间 - destroy_room
`req` = roomID + userID
`rsp` = code + message

### 3.6.房间新增一个成员 - add_pusher
`req` = roomID + roomInfo + userID + userName + userAvatar + pushURL
`req` = code + message

### 3.7.房间移除一个成员 - delete_pusher
`req` = roomID + userID
`rsp` = code + message

### 3.8.房间成员心跳 - pusher_heartbeat
`req` = roomID + userID
`rsp` = code + message

### 3.9.获取IM登录相关的信息 - get_im_login_info
云通信移动端IMSDK，通过这四个参数（userID，userSig，sdkAppID， accType）来登录云通信IM后台。
`req` = userID
`rsp` = code + message + userID + userSig + sdkAppID + accType
