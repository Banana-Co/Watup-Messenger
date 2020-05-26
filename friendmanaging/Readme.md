# 进度

## 完成部分

- 已部署到106.12.204.55:8000
- 查找好友
- 添加好友

## 需要的其他模块接口

- 根据token得到当前用户id
- 根据id找到对应用户

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

## 添加好友

### 接口描述

根据用户id添加好友

### 请求方法

PUT

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
- message: “添加成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友已存在

- code: 409
- message: "好友已存在"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 删除好友

### 接口描述

根据好友用户id删除好友

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

## 拉黑好友

### 接口描述

根据好友用户id拉黑好友

### 请求方法

PUT

### URI

/api/block

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “拉黑成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友已被拉黑

- code: 409
- message: "好友已被拉黑"
- data: null

#### 其他错误

- code: 400
- message: "其他错误"
- data: null

## 撤销拉黑好友

### 接口描述

根据好友用户id撤销拉黑

### 请求方法

DELETE

### URI

/api/block

### 参数

| 字段  | 类型   | 描述     |
| :---- | ------ | -------- |
| token | Token  | 用户标识 |
| id    | String | 好友的ID |

### 返回值

#### 成功

- code: 200
- message: “撤销成功”
- data: null

#### 未找到好友

- code: 404
- message: "未找到好友"
- data: null

#### 好友已被拉黑

- code: 409
- message: "拉黑已被撤销"
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

/api/blocks

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

/api/friend

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