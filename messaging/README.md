# 聊天模块

## 建立连接

```javascript
var ws = new WebSocket("ws://server-address/ws/?token=<token>)
```

## 连接成功回调函数

```
ws.onopen = function() {
	console.log("WebSocket is open now.");
}
```

## 连接失败回调函数

```
ws.onerror = function(event) {
	console.error("WebSocket error observed:", event);
}
```

## 发送消息

```
ws.send(JSON.stringify({
	id: "user01",
	message: "message",
}))
```

## 接受消息回调函数

```
ws.onmessage = function(event) {
    data = JSON.parse(event.data)
    var id = data.id;
    var message = data.message;
    var timestamp = data.timestamp;
}
```

## 关闭连接

```
ws.close()
```

## 关闭连接回调函数

```
ws.onclose = function(event) {
  console.log("WebSocket is closed now.");
};
```

