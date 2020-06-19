# 聊天模块



## 聊天接口

### 建立连接

```javascript
var ws = new WebSocket("ws://server-address/ws?access_token=<token>")
```

### 连接成功回调函数

```javascript
ws.onopen = function() {
	console.log("WebSocket is open now.");
}
```

### 连接失败回调函数

```javascript
ws.onerror = function(event) {
	console.error("WebSocket error observed:", event);
}
```

### 发送消息

```javascript
ws.send(JSON.stringify({
}))
```

##### 消息格式

* type：unicast 表示私聊，multicast 表示群聊
* receiverId：接收用户的id，群聊不需要指定
* groupId：接收群组的 id，私聊不需要指定
* content：发送的消息内容

##### 注意事项

* 如果被对方屏蔽/不是对方好友，对方不会收到私聊消息。如果被对方屏蔽，对方也不会收到群聊消息。发送方不会有任何发送不成功的提醒。

### 接受消息回调函数

```
ws.onmessage = function(event) {
    data = JSON.parse(event.data)
    // do something
}
```

##### 消息格式

- type：unicast 表示私聊，multicast 表示群聊
- senderId：发送用户的 id
- receiverId：接收用户的 id，群聊无此字段
- groupId：群组的 id，私聊无此字段
- content：发送的消息内容
- timestamp：消息到达服务器的时间

### 关闭连接

```
ws.close()
```

### 关闭连接回调函数

```
ws.onclose = function(event) {
  console.log("WebSocket is closed now.");
};
```

### 获取离线私聊消息

* uri：/api/message

* 请求方法：GET

* 参数
  
  * access_token
  * sort：字符串，asc（默认） 表示按时间增序，desc 表示按时间降序
  * drop：布尔值，true（默认） 表示删除服务器上获取的消息，false 不删除
  * type: 字符串，all（默认）拉取所有信息，unicast 私聊，multicast 群聊
  
* 返回值

  数组，数组中每个成员的字段如下：

  * id：发送消息用户的 id
  * messages：该 id 用户发送给请求用户的所有消息，格式与 WebSocket 接收的私聊消息格式相同

* 状态码
  * 200：成功
  * 400：请求错误




## 群聊管理接口



**所有请求参数都要加入 access_token**



### 添加群聊

- uri：/api/group
- 请求方法：POST
- 参数
  
- name：群名
  
- 返回值

  新创建群聊的 id

- 状态码

  - 200：成功添加

### 修改群名

- uri：/api/group/{groupId}
- 请求方法：PUT
- 参数
  
- name：要修改的群名
  
- 返回值

  无

- 状态码

  - 200：成功修改
  - 403：不是群主，无权修改

### 邀请成员

- uri：/api/request
- 请求方法：POST
- 请求体
  - groupId：群组 Id
  - userId：邀请成员的 id

- 返回值

  无

- 状态码

  - 200：成功邀请
  - 403：不是群成员，无权邀请，或成员已在组内

### 获取群聊请求

- uri：/api/request
- 请求方法：GET

- 返回值

  数组，成员字段如下：

  * id：请求 id
  * groupName：群聊名称
  * groupId：群聊 id
  * userId：被邀请用户的 id
  * invitedBy：发起邀请的用户成员

- 状态码

  - 200：成功

### 删除群聊请求

- uri：/api/request/{requestId}
- 请求方法：DELETE

- 返回值

  无

- 状态码

  - 200：成功
  - 403：该邀请不是发送给此用户的

### 接受群聊请求

- uri：/api/request/{requestId}
- 请求方法：PUT

- 返回值

  无

- 状态码

  - 200：成功
  - 403：该邀请不是发送给此用户的

### 获取某个群聊信息

- uri：/api/group/{groupId}
- 请求方法：GET

- 返回值
  * id：群 id
  * name：群名
  * managerId：群主 id
  * usersId：字符串数组，内容是所有成员的 id
- 状态码
  - 200：成功
  - 403：不在此群聊中，没有权限获取

### 获取加入的所有群聊

- uri：/api/group
- 请求方法：GET
- 参数
  
- detailed：布尔值，默认为true，表示是否返回详细信息
  
- 返回值

  detailed 为 false 时，返回字符串数组，内容为群组id；detailed 为 true 时，返回对象数组，字段如下：

  - id：群 id
  - name：群名
  - managerId：群主 id
  - usersId：字符串数组，内容是所有成员的 id

- 状态码

  - 200：成功

### 退出群聊

- uri：/api/group/{groupId}
- 请求方法：DELETE

- 返回值

  无

- 状态码

  - 200：成功
  - 403：不在此群聊中，没有权限

##### 说明

* 群主退出相当于解散群聊，所有人都会退出

### 移除群成员

- uri：/api/group/{groupId}
- 请求方法：DELETE
- 参数
  
- userId：移除的成员 id
  
- 返回值

  无

- 状态码

  - 200：成功
  - 403：不是群主，没有权限

##### 说明

- 群主退出相当于解散群聊，所有人都会退出



## 消息推送接口

- uri：/notification

- 请求方法：POST

- 参数

  - from：用户id，标明消息来自哪个用户；type 为 unicast 和 multicast 时可以不需要设置，设置来自哪个用户仅用于屏蔽；type 为broadcast的时候需要指定
  - type：unicast（默认）发送给一个用户，multicast 发送给某群组的所有用户， broadcast 发送给指定用户的所有好友
  - to：unicast 时为推送对象的 id，multicast 时为推送群组的 id，broadcast 时不需要指定

- 请求体：

  - notificationType：字符串，通知的类型，前后端自行协调定义
  - content：通知的内容

- 返回值

  无

- 状态码

  - 200：成功
  - 403：请求有误
  
- WebSocket 收到的消息格式：

  - 与请求体相比多了一个 type，值为 NOTIFICATION，其他保持一致

##### 说明

- 消息接口不对前端开放，应该整合进后端的业务逻辑
- 指定消息来自哪个用户后，可以发送消息给还未成为好友的人，但屏蔽该用户的用户不会收到消息；不指定来自哪个用户时，所有目标用户都会收到消息
- 如果目标用户不在线，消息直接被丢弃
