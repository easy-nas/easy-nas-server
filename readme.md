# easy-nas-sever

这个项目是 easy-nas的服务端

### 启动方式：

1. docker 启动（推荐）
    * 已经上传到docker hub
    * docker-compose.yml 供参考
    ```yml
    version: '2.0'
      
    services:
      easy-nas-server:
        restart: unless-stopped
        image: llysrv/easy-nas-server:0.0.4
        ports:
          - 8888:8888
          - 2222:2222
        volumes:
          - ./admin-config.yml:/usr/share/easy-nas/admin-config.yml
          - ./logs:/usr/share/easy-nas/logs
          - /disks/sda1:/disks/sda1
    ```
    * 运行
    ```shell
    sudo docker-compose up -d
    ```
    
    
2. maven 启动


### 配置文件详解：
* 如果使用docker启动，这个配置文件 建议从docker映射出来。
* 除了第一次启动之前可以修改它，其他时候均不可修改。
* 程序中有修改的接口，需要修改去页面修改否则会出现数据不可访问的问题。
* admin-config.yml
```yml
!!com.easynas.server.model.AdminConfig
fileSavePaths:
  master: [/disks/sda1/easy-nas-data/file] # 文件保存路径
  backup: null                             # 文件备份路径 
generalInformationPath: {
  master: /disks/sda1/easy-nas-data/general-information/, # 通用信息保存路径
  backup: null                                            # 通用信息备份路径
}
```

其他文档在[easy-nas-doc-and-plan](https://github.com/easy-nas/easy-nas-doc-and-plan/wiki/easy-nas)维护
