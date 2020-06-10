# auth-server文档

###  完成部分

- 已部署到106.13.134.241:8080
- 发送邮箱验证码
- 验证用户Id
- 用户注册（包含邮箱认证）
- 用户登录
- 获取(刷新)token
- 验证token

### 未完成
 - 注销token 

# auth-server接口说明

## 发送邮箱验证码

### 接口描述

根据邮箱发送邮箱验证码，设置时间戳并存储

### 请求方法

POS

### URI

/oauth/sendCode

### 参数

| 字段 | 类型   | 描述         |
| :--- | ------ | ------------ |
| email | String | 用户绑定邮箱 |

### 返回值

#### 成功

- code: 200

- message: “验证码发送成功”

- data: 验证码内容


#### 验证码依然有效

- code: 400
- message: "验证码依然有效"
- data: null

#### 邮箱已被绑定

- code: 400
- message: "该邮箱已被绑定"
- data: null

## 验证用户id(前端应在用户输入id完成时发送请求)

### 接口描述

验证用户id是否重复

### 请求方法

POST

### URI

/oauth/checkUserId

### 参数

| 字段 | 类型   | 描述     |
| :--- | ------ | -------- |
| id   | String | 用户标识 |

### 返回值

#### 成功

- code: 200
- message: “用户id合法”
- null

#### 错误

- code: 400
- message: “用户id被占用”
- data: null

## 用户注册（用户点击注册时请求）

### 接口描述

正常的用户注册流程

### 请求方法

POST

### URI

/oauth/register

### 参数

| 字段     | 类型   | 描述       |
| :------- | ------ | ---------- |
| id       | String | 用户标识   |
| username | String | 用户“名”   |
| password | String | 密码       |
| email    | String | 邮箱       |
| code     | String | 邮箱验证码 |

### 返回值

#### 成功

- code: 200
- message: “注册成功”
- null

#### 验证码错误

- code: 400
- message: “验证码错误”
- data: null

#### 验证码过期

- code: 400
- message: “验证码过期”
- data: null

#### 验证码过期

- code: 400
- message: “验证码过期”
- data: null

#### 用户已存在（暂时不要使用，最后checkId用户会很不爽，开始就用之前的checkUserId）

- code: 400
- message: “用户已存在”
- data: null

## 用户登录

### 接口描述

login

### 请求方法

POST

### URI

/oauth/login

### 参数

| 字段     | 类型   | 描述     |
| :------- | ------ | -------- |
| id       | String | 用户标识 |
| password | String | 用户密码 |

### 返回值

#### 成功

- code: 200
- message: “成功”
- data  {
          "access_token": "357c8300-b994-48a5-8dd0-f8f34fc9b4d5",
          "token_type": "bearer",
          "refresh_token": "892d9782-83ad-4622-98e7-8e2f542928bb",
          "expires_in": 3598,
          "scope": "all",
          "id": "leon",
          "email": "leondastur@163.com",
          "username": "dastur"
      }

#### 错误

- code: 400
- message: “用户名或密码错误”
- data: null



## 获取token

### 接口描述

请求token

### 请求方法

POST

### URI

/oauth/token

### 参数

还可以将以下信息写入Authorization

| 字段          | 类型                           | 描述                                         |
| :------------ | ------------------------------ | -------------------------------------------- |
| grant_type    | String==password/refresh_token | 认证模式（密码/刷新）                        |
| username      | String                         | **注意发送id，只不过参数名称必须是username** |
| password      | String                         | 用户密码                                     |
| client_id     | String==watup（值就是watup）   | 服务客户端的id                               |
| client_secret | Stirng==watup (值就是watup)    | 服务客户端密码                               |

### 返回值

#### 成功

{

  "access_token": "357c8300-b994-48a5-8dd0-f8f34fc9b4d5",

  "token_type": "bearer",

  "refresh_token": "892d9782-83ad-4622-98e7-8e2f542928bb",

  "expires_in": 959,

  "scope": "all",

  "id": "leon",

  "email": "leondastur@163.com",

  "username": "dastur"

}

#### 错误

{

  "error": "invalid_grant",

  "error_description": "用户名或密码错误"

}

## 验证token

### 接口描述

验证token

### 请求方法

POST

### URI

/oauth/check_token

### 参数

| 字段  | 类型   | 描述      |
| :---- | ------ | --------- |
| token | String | 就是token |

### 返回值

#### 成功

{

  "user_name": "dastur",

  "scope": [

​    "all"

  ],

  "active": true,

  "id": "leon",

  "exp": 1591780347,

  "email": "leondastur@163.com",

  "client_id": "watup",

  "username": "dastur"

}

#### 错误

{

  "error": "invalid_token",

  "error_description": "Token was not recognised"

}
