# 进度

## 完成部分

- 已部署到106.12.204.55:8000
- 查找用户
- 根据id查找好友
- 根据username查找好友
- 获取好友申请列表
- 发送好友申请
- 通过好友申请
- 拒绝好友申请
- 删除好友（双向删除）
- 获取好友列表
- 设置备注
- 屏蔽好友（不会删除好友）
- 撤销屏蔽好友
- 获取黑名单
- 查询用户是否是好友
- 查询用户是否被屏蔽
- 根据id查询用户是否是好友
- 根据id查询用户是否被屏蔽
- 获取好友id列表
- 根据id获取好友id列表

# 接口说明

- 前端传递access_token后经api gateway转换为id字段
- 向前端暴露的接口uri前会加/api

## 查找用户

### 接口描述

根据id关键字查找系统用户，返回匹配的用户

### 请求方法

GET

### URI

/api/friend/user

### 参数

| 字段         | 类型   | 描述           |
| :----------- | ------ | -------------- |
| access_token | String | 用户标识       |
| keyword      | String | 查找的id关键字 |

### 返回值

#### 成功

- status: 200

- data: user(Friend)


#### 不应出现的数据库错误

- status: 404
- data: null

## 根据id查找好友

### 接口描述

根据id查找当前用户的好友，返回好友

### 请求方法

GET

### URI

/api/friend/search/id

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId           | String | 好友     |

### 返回值

#### 成功

- status: 200
- data: friend(Friend)

#### 未找到好友

- status: 404
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 根据username查找好友

### 接口描述

根据关键字查找当前用户的好友，返回好友列表

### 请求方法

GET

### URI

/api/friend/search/username

### 参数

| 字段         | 类型   | 描述         |
| :----------- | ------ | ------------ |
| access_token | String | 用户标识     |
| username     | String | 查找的关键字 |

### 返回值

#### 成功

- status: 200

- data: users(Array)

  users：

  | 字段   | 类型   | 描述 |
  | :----- | ------ | ---- |
  | friend | Friend | 好友 |

#### 不应出现的数据库错误

- status: 404
- data: null

## 获取好友申请列表

### 接口描述

根据用户access_token获取好友列表

### 请求方法

GET

### URI

/api/friend/request

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |

### 返回值

#### 成功

- status: 200

- message: “获取成功”

- data: friends(Array)

  friends:

  | 字段          | 类型          | 描述     |
  | :------------ | ------------- | -------- |
  | friendRequest | friendRequest | 好友申请 |

#### 不应出现的数据库错误

- status: 404
- message: "不应出现的数据库错误"
- data: null

## 发送好友申请

### 接口描述

根据用户id发送好友申请

### 请求方法

POST

### URI

/api/friend/request

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |
| remark       | String | 申请备注 |

### 返回值

#### 成功

- status: 200
- data: null

#### 被对方屏蔽

- status: 403
- data: null

#### 未找到好友

- status: 404
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 通过好友申请

### 接口描述

通过指定id的好友申请

### 请求方法

PUT

### URI

/api/friend/request

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| requestId    | String | 申请的ID |

### 返回值

#### 成功

- status: 200
- data: null

#### 请求已被处理

- status: 409
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 拒绝好友申请

### 接口描述

拒绝指定id的所有好友申请

### 请求方法

DELETE

### URI

/api/friend/request

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| requestId    | String | 申请的ID |

### 返回值

#### 成功

- status: 200
- data: null

#### 请求已被处理

- status: 409
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 删除好友

### 接口描述

根据好友用户id删除好友,自己也将从对方的好友列表中移除

### 请求方法

DELETE

### URI

/api/friend

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |

### 返回值

#### 成功

- status: 200
- data: null

#### 未找到好友

- status: 404
- data: null

#### 好友已被删除

- status: 409
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 获取好友列表

### 接口描述

根据用户access_token获取好友列表

### 请求方法

GET

### URI

/api/friends

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |

### 返回值

#### 成功

- status: 200

- data: friends(Array)

  friends:

  | 字段   | 类型   | 描述 |
  | :----- | ------ | ---- |
  | friend | Friend | 用户 |

#### 不应出现的数据库错误

- status: 404
- data: null

## 设置好友备注

### 接口描述

根据好友用户id，设置好友备注

### 请求方法

PUT

### URI

/api/friend/nickname

### 参数

| 字段         | 类型   | 描述       |
| :----------- | ------ | ---------- |
| access_token | String | 用户标识   |
| friendId     | String | 好友的ID   |
| nickname     | String | 好友的备注 |

### 返回值

#### 成功

- status: 200
- data: null

#### 未找到好友

- status: 404
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 屏蔽好友

### 接口描述

根据好友用户id屏蔽好友

### 请求方法

PUT

### URI

/api/friend/block

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |

### 返回值

#### 成功

- status: 200
- data: null

#### 未找到好友

- status: 404
- data: null

#### 好友已被屏蔽

- status: 409
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 撤销屏蔽好友

### 接口描述

根据好友用户id撤销屏蔽

### 请求方法

DELETE

### URI

/api/friend/block

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |

### 返回值

#### 成功

- status: 200
- data: null

#### 未找到好友

- status: 404
- data: null

#### 屏蔽已被处理

- status: 404
- data: null

#### 不应出现的数据库错误

- status: 404
- data: null

## 获取黑名单

### 接口描述

根据用户access_token获取黑名单

### 请求方法

GET

### URI

/api/friend/blocks

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |

### 返回值

#### 成功

- status: 200

- data: friends(Array)

  friends:

  | 字段   | 类型   | 描述 |
  | :----- | ------ | ---- |
  | friend | Friend | 用户 |

#### 不应出现的数据库错误

- status: 404
- data: null

## 查询用户是否是好友

### 接口描述

根据id判断是否是用户的好友

### 请求方法

GET

### URI

/api/friend

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |

### 返回值

#### 成功

- status: 200

- data: true/false


#### 不应出现的数据库错误

- status: 404
- data: null

## 查询用户是否被屏蔽

### 接口描述

根据id判断用户是否被屏蔽

### 请求方法

GET

### URI

/api/friend/block

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |
| friendId     | String | 好友的ID |

### 返回值

#### 成功

- status: 200

- data: true/false

#### 不应出现的数据库错误

- status: 404
- data: null

## 根据id查询用户是否是好友

### 接口描述

根据id判断是否是用户的好友

### 请求方法

GET

### URI

/friend

### 参数

| 字段     | 类型   | 描述     |
| :------- | ------ | -------- |
| id       | String | 用户的id |
| friendId | String | 好友的id |

### 返回值

#### 成功

- status: 200

- data: true/false


#### 不应出现的数据库错误

- status: 404
- data: null

## 根据id查询用户是否被屏蔽

### 接口描述

根据id判断用户是否被屏蔽

### 请求方法

GET

### URI

/friend/block

### 参数

| 字段     | 类型   | 描述     |
| :------- | ------ | -------- |
| id       | String | 用户的id |
| friendId | String | 好友的id |

### 返回值

#### 成功

- status: 200

- data: true/false

#### 不应出现的数据库错误

- status: 404
- data: null

## 获取好友id列表

### 接口描述

根据用户access_token获取好友id列表

### 请求方法

GET

### URI

/api/friends/id

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |

### 返回值

#### 成功

- status: 200

- data: friends(Array)

  friends:

  | 字段 | 类型   | 描述   |
  | :--- | ------ | ------ |
  | id   | String | 用户id |

#### 不应出现的数据库错误

- status: 404
- data: null

## 根据id获取好友id列表

### 接口描述

根据用户id获取好友id列表

### 请求方法

GET

### URI

/friends/id

### 参数

| 字段         | 类型   | 描述     |
| :----------- | ------ | -------- |
| access_token | String | 用户标识 |

### 返回值

#### 成功

- status: 200

- data: friends(Array)

  friends:

  | 字段 | 类型   | 描述   |
  | :--- | ------ | ------ |
  | id   | String | 用户id |

#### 不应出现的数据库错误

- status: 404
- data: null
