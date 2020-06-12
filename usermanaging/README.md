# user-server文档

###  完成部分

- 已部署到106.13.134.241:8082
- 修改昵称
- 修改头像
- 修改密码
- 修改地区
- 修改签名

# user-server接口说明

## 修改昵称

### 请求方法

POST

### URI

/api/user/updateUsername

### 参数

| 字段     | 类型   | 描述     |
| :------- | ------ | -------- |
| access_token       | String | 用户标识 |
| username | String | 昵称     |

### 返回值

#### 成功

- code: 200

- message: “昵称修改成功”

- data: null


#### 用户不存在

- code: 400
- message: "用户不存在"
- data: null

## 修改头像

### 请求方法

POST

### URI

/api/user/updateAvatar

### 参数

| 字段 | 类型         | 描述     |
| :--- | ------------ | -------- |
| access_token   | String       | 用户标识 |
| file | MutipartFile | 头像图片 |

### 返回值

#### 成功

- code: 200
- message: “修改头像成功”
- data: 头像地址

#### 错误

- code: 400
- message: “头像上传失败”
- data: null

## 修改密码

### 请求方法

POST

### URI

/api/user/updatePassword

### 参数

| 字段        | 类型   | 描述     |
| :---------- | ------ | -------- |
| access_token          | String | 用户标识 |
| oldPassword | String | 旧密码   |
| newPassword | String | 新密码   |

### 返回值

#### 成功

- code: 200
- message: “密码修改成功”
- null

#### 验证码错误

- code: 400
- message: “密码错误”
- data:null

## 修改签名

### 请求方法

POST

### URI

/api/user/updateSign

### 参数

| 字段 | 类型   | 描述     |
| :--- | ------ | -------- |
| access_token   | String | 用户标识 |
| sign | String | 签名     |

### 返回值

#### 成功

- code: 200
- message: “修改签名成功”
- data:null

#### 错误

- code: 400
- message: “修改签名失败”
- data:null

## 修改地区

### 请求方法

POST

### URI

/api/user/updateArea

### 参数

| 字段 | 类型   | 描述     |
| :--- | ------ | -------- |
| access_token   | String | 用户标识 |
| area | String | 地区     |

### 返回值

#### 成功

- code: 200
- message: “修改地区成功”
- data: 头像地址

#### 错误

- code: 400
- message: “修改地区失败”
- data: null
