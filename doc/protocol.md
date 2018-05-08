# 后台协议设计

* 接口命名采用c风格，举例 get_push_url
* 接口参数命名采用驼峰风格，举例 roomID
* 大部分请求方式是POST，请求的body是Json格式。有少数get请求接口，会在接口说明里指出。

## 通用`utils`接口说明

### get_login_info 获取sdkAppID、accType、userID、userSig信息

注意：get请求接口，参数是拼在URL后面的

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | 用户id，可为空，为空则随机分配 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| sdkAppID | String | 云通信的sdkAppID |
| accType | String | 云通信的accountType |
| userID | String | 用户id |
| userSig | String | 用户签名 |

### get_test_pushurl 获取一个随机的推流地址

注意：get请求接口

#### 参数说明

无入参

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| url_push | String        | 推流地址 |
| url_play_flv | String | flv播放地址 |
| url_play_rtmp | String | rtmp播放地址 |
| url_play_hls | String | hls播放地址 |
| url_play_acc | String | 低延时播放地址 |

### get_test_rtmpaccurl 获取体验低延时播放的地址

#### 参数说明

无入参

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| url_rtmpacc | String | 体验低延时播放的地址 |

## 直播房间`live_room`接口说明

### login 登录接口，校验im签名合法性并派发token，后续请求需要校验token。

#### 参数说明

注意：参数拼接在URL后面

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| sdkAppID    | String | 是    | 云通信的sdkAppID |
| accountType    | String | 是    | 云通信的accountType |
| userID    | String | 是    | userID |
| userSig    | String | 是    | 云通信派发的userSig |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| userID | String | 用户id |
| token | String | 登录成功后产生的token |

### logout 退出登录接口，清理后台保存的token信息

#### 参数说明

注意：userID和token参数拼接在URL后面。后续所有接口都需要带上userID和token，且是拼接在URL后面的，后面不再赘述。

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### get_push_url 获取推流地址接口

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| pushURL | String | 推流地址 |

### get_room_list 获取直播房间列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| cnt    | int | 是    | 房间数量 |
| index    | int | 是    | 起始下标 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| rooms | List<Room> | 直播房间列表 |

#### Room 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### get_pushers 获取指定房间的推流者列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### create_room 创建直播房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomInfo    | String | 是    | 房间名称 |
| roomID    | String | 否    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |

### destroy_room 销毁直播房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### add_pusher 增加一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| roomInfo    | String | 否    | 房间名称 |
| userName    | String | 是    | 用户名称 |
| userAvatar    | String | 是    | 用户头像 |
| pushURL    | String | 是    | 推流地址 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### delete_pusher 删除一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### pusher_heartbeat 推流者心跳

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### merge_stream 连麦混流接口

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| mergeParams | String | 是    | 混流参数json格式字符串 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| result | Result | 混流结果 |

#### Result 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int   | 后台混流返回码 |
| message | String | 后台混流返回信息描述 |
| timestamp | int | 时间戳 |

### get_custom_info 获取房间自定义信息，可以用来实现统计房间观众数等功能。

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| customInfo | String | 自定义信息json字符串 |

### set_custom_field 设置房间自定义字段，可以用来实现统计房间观众数等功能。

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| fieldName    | String | 是    | 属性名称 |
| operation    | String | 是    | 操作类型 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| customInfo | String | 自定义信息json字符串 |

### add_audience 增加一个观众

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| userInfo    | String | 是    | 观众信息 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### delete_audience 删除一个观众

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### get_audiences 获取观众列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| audiences | List<Audience> | 观众列表 |

#### Audience 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | userID |
| userInfo | String | 用户信息 |

## 双人房间`double_room`接口说明

### login 登录接口，校验im签名合法性并派发token，后续请求需要校验token。

#### 参数说明

注意：参数拼接在URL后面

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| sdkAppID    | String | 是    | 云通信的sdkAppID |
| accountType    | String | 是    | 云通信的accountType |
| userID    | String | 是    | userID |
| userSig    | String | 是    | 云通信派发的userSig |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| userID | String | 用户id |
| token | String | 登录成功后产生的token |

### logout 退出登录接口，清理后台保存的token信息

#### 参数说明

注意：userID和token参数拼接在URL后面。后续所有接口都需要带上userID和token，且是拼接在URL后面的，后面不再赘述。

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### get_push_url 获取推流地址接口

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| pushURL | String | 推流地址 |

### get_room_list 获取双人房间列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| cnt    | int | 是    | 房间数量 |
| index    | int | 是    | 起始下标 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| rooms | List<Room> | 双人房间列表 |

#### Room 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### get_pushers 获取推流者列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### create_room 创建双人房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomInfo    | String | 是    | 房间名称 |
| roomID    | String | 否    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |

### destroy_room 销毁双人房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### add_pusher 增加一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| roomInfo    | String | 否    | 房间名称 |
| userName    | String | 是    | 用户名称 |
| userAvatar    | String | 是    | 用户头像 |
| pushURL    | String | 是    | 推流地址 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### delete_pusher 删除一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### pusher_heartbeat 推流者心跳

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

## 多人房间`multi_room`接口说明

### login 登录接口，校验im签名合法性并派发token，后续请求需要校验token。

#### 参数说明

注意：参数拼接在URL后面

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| sdkAppID    | String | 是    | 云通信的sdkAppID |
| accountType    | String | 是    | 云通信的accountType |
| userID    | String | 是    | userID |
| userSig    | String | 是    | 云通信派发的userSig |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| userID | String | 用户id |
| token | String | 登录成功后产生的token |

### logout 退出登录接口，清理后台保存的token信息

#### 参数说明

注意：userID和token参数拼接在URL后面。后续所有接口都需要带上userID和token，且是拼接在URL后面的，后面不再赘述。

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### get_push_url 获取推流地址接口

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| pushURL | String | 推流地址 |

### get_room_list 获取多人房间列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| cnt    | int | 是    | 房间数量 |
| index    | int | 是    | 起始下标 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| rooms | List<Room> | 多人房间列表 |

#### Room 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### get_pushers 获取推流者列表

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |
| roomInfo | String | 房间名称 |
| roomCreator | String | 房间创建者userID |
| mixedPlayURL | String | 混流播放地址 |
| pushers | List<Pusher> | 推流者列表 |

#### Pusher 属性说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| userID | String   | 用户id |
| userName | String | 用户昵称 |
| userAvatar | String | 用户头像 |
| accelerateURL | String | 低延时播放地址 |

### create_room 创建多人房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomInfo    | String | 是    | 房间名称 |
| roomID    | String | 否    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |
| roomID | String   | 房间ID |

### destroy_room 销毁多人房间

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### add_pusher 增加一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |
| roomInfo    | String | 否    | 房间名称 |
| userName    | String | 是    | 用户名称 |
| userAvatar    | String | 是    | 用户头像 |
| pushURL    | String | 是    | 推流地址 |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### delete_pusher 删除一个推流者

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |

### pusher_heartbeat 推流者心跳

#### 参数说明

| 参数名称   | 类型         | 是否必填 | 说明                 |
| ------ | ---------- | ---- | ------------------ |
| userID    | String | 是    | userID |
| token    | String | 是    | 登录成功后返回的token |
| roomID    | String | 是    | 房间ID |

#### 返回结果说明

| 属性名称    | 类型         | 说明     |
| ------- | ---------- | ------ |
| code | int        | 返回码，0表示成功 |
| message | String | 描述信息 |