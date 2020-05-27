# 聊天模块

## TODO
* 对方不在线时存储到数据库
* 从数据库拉取离线消息
* 群聊功能
* @功能

## 可以在此模块实现的功能
* 消息（上线、好友申请、群邀请）消息推送

## 已实现的接口

### 建立连接

```javascript
var ws = new WebSocket("ws://server-address/ws/?token=<token>)
```

### 连接成功回调函数

```
ws.onopen = function() {
	console.log("WebSocket is open now.");
}
```

### 连接失败回调函数

```
ws.onerror = function(event) {
	console.error("WebSocket error observed:", event);
}
```

### 发送消息

```
ws.send(JSON.stringify({
	id: "user01",
	message: "message",
}))
```

### 接受消息回调函数

```
ws.onmessage = function(event) {
    data = JSON.parse(event.data)
    var id = data.id;
    var message = data.message;
    var timestamp = data.timestamp;
}
```

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

