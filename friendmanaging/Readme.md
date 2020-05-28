# 进度

## 完成部分

- 已部署到106.12.204.55:8000
- 查找好友
- 发送好友申请
- 通过好友申请
- 拒绝好友申请
- 删除好友（双向删除）
- 获取好友列表
- 设置备注
- 屏蔽好友（不会删除好友）
- 撤销屏蔽好友
- 获取黑名单

## 需要的其他模块接口

- 根据token得到当前用户id

# 接口说明

## 查找好友

### 接口描述

根据关键字查找当前用户的好友，返回好友列表

### 请求方法

GET

### URI

/api/friend

### 参数

| 字段     | 类型   | 描述         |
| :------- | ------ | ------------ |
| token    | Token  | 用户标识     |
| username | String | 查找的关键字 |

### 返回值

#### 成功

- code: 200

- message: “查找成功”

- data: users(Array)

  users：

  | 字段   | 类型 | 描述 |
  | :----- | ---- | ---- |
  | friend | User | 好友 |

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 发送好友申请

### 接口描述

根据用户id发送好友申请

### 请求方法

POST

### URI

/api/friend/request

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “添加成功”
- data: null

#### 被对方屏蔽

- code: 403
- message: "被对方屏蔽"
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 通过好友申请

### 接口描述

通过指定id的所有好友申请

### 请求方法

PUT

### URI

/api/friend/request

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “通过成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 被对方屏蔽

- code: 403
- message: "被对方屏蔽"
- data: null

#### 好友申请为空

- code: 404
- message: "好友申请为空"
- data: null

#### 已添加好友

- code: 409
- message: "已添加好友"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 拒绝好友申请

### 接口描述

拒绝指定id的所有好友申请

### 请求方法

DELETE

### URI

/api/friend/request

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “拒绝成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友申请为空

- code: 404
- message: "好友申请为空"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 删除好友

### 接口描述

根据好友用户id删除好友,自己也将从对方的好友列表中移除

### 请求方法

DELETE

### URI

/api/friend

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “删除成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友已被删除

- code: 409
- message: "好友已被删除"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 获取好友列表

### 接口描述

根据用户token获取好友列表

### 请求方法

GET

### URI

/api/friends

### 参数

| 字段  | 类型  | 描述     |
| :---- | ----- | -------- |
| token | Token | 用户标识 |

### 返回值

#### 成功

- code: 200

- message: “获取成功”

- data: friends(Array)

  friends:

  | 字段   | 类型 | 描述 |
  | :----- | ---- | ---- |
  | friend | User | 用户 |

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 设置好友备注

### 接口描述

根据好友用户id，设置好友备注

### 请求方法

PUT

### URI

/api/friend/nickname

### 参数

| 字段     | 类型   | 描述       |
| :------- | ------ | ---------- |
| token    | Token  | 用户标识   |
| id       | String | 好友的ID   |
| nickname | String | 好友的备注 |

### 返回值

#### 成功

- code: 200
- message: “设置成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 屏蔽好友

### 接口描述

根据好友用户id屏蔽好友

### 请求方法

PUT

### URI

/api/friend/block

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “屏蔽成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友已被屏蔽

- code: 409
- message: "好友已被屏蔽"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 撤销屏蔽好友

### 接口描述

根据好友用户id撤销屏蔽

### 请求方法

DELETE

### URI

/api/friend/block

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “屏蔽成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 屏蔽已被处理

- code: 404
- message: "屏蔽已被处理"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 获取黑名单

### 接口描述

根据用户token获取黑名单

### 请求方法

GET

### URI

/api/friend/blocks

### 参数

| 字段  | 类型  | 描述     |
| :---- | ----- | -------- |
| token | Token | 用户标识 |

### 返回值

#### 成功

- code: 200

- message: “获取成功”

- data: friends(Array)

  friends:

  | 字段   | 类型 | 描述 |
  | :----- | ---- | ---- |
  | friend | User | 用户 |

#### 其他错误

- code: 400
- message: "其他错误"
- data: null
