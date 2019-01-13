# easy-nas

### 背景

云服务器(国内好像只有百度网盘了）保存文件不安全，速度受限，盗版电影、音乐失效....

手机内存太小...电脑硬盘太小...

5G时代来临，网络存储大有可为...

需要家用存储服务器，但是家庭服务器没有集群、组raid安全性不如（冷）备份, 通常家里的服务器可能是稳定性不高的(淘汰)电脑代替。

市面上常见的开软软件（如NextCloud)，不支持多块硬盘，如果硬盘损坏恢复机制不明确，依赖数据库，服务器奔溃可能会导致数据丢失。
太多额外不必要的功能（视频、团队合作、聊天室...)，最主要的是出了问题不好解决，并且扩展性差，不符合中国国情。

所以需要一款，定位家庭的服务器存储管理系统。

初期整体目标：

* 定位家庭
* 数据安全性、稳定性(系统奔溃了或者某个磁盘坏了，只要备份的地方数据还在就行)
* 易于扩展维护
* 支持硬盘横向扩展，（插入个硬盘，改一下配置就能用）
* 易于硬盘升级
* 支持常见的网盘功能
* 支持同步盘功能
* 符合中国国情（例如加密备份至百度网盘...）
* 迅雷离线下载
* 在线播放视频（电视和服务器在一个局域网内会超级快，相当于本地磁盘了）
* 系统足够的精简，对于普通用户只保留核心功能。
* ...

### 第一期目标
1. 支持多用户 (web, app)
    * 登录注册 
        * 给个提示，第一个注册的用户为管理员
    
1. 管理员 (web) (只有管理员才能访问的页面)
    * 设置通用信息保存路径
    * 设置通用信息备份路径 (备份可以不设置)
    * 设置文件存储路径，可以是多个地址
    * 设置文件备份路径，可以是多个地址（备份路径可以不设置）
    
1. 用户 (web, app)
    * 批量上传文件
    * 批量下载文件
    * 上传文件夹
    * 下载文件夹（可能是压缩传递）
    * 新建文件夹


### 第二期

1 版本控制 
    * 用户给当前网盘状态起个名字，可以随时回到这个节点
    * 切换到历史版本，只可读，不可写
1. (移动端)自动备份照片、联系人、短信等
1. 在线浏览浏览照片，视频
1. 支持网络地址备份
1. ....(还没想好)

## 文件存储路径

* 用户信息保存在：${generalInformationPath}/${username}/user-info.yml
* 用户数据保存在：${generalInformationPath}/${username}/data/*
* 真实文件数据保存在：${fileSavePath}/${sha256}

## 其他约定

* 注册的第一个用户为管理员