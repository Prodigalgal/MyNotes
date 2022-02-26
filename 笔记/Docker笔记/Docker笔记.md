# Docker简介

Docker的出现使得Docker得以打破过去「程序即应用」的观念。透过镜像(images)将作业系统核心除外，运作应用程式所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作。

解决了运行环境和配置问题的软件容器，方便做持续集成并有助于整体发布的容器虚拟化技术。

# Docker安装

**前提条件**

目前，CentOS 仅发行版本中的内核支持 Docker。Docker 运行在CentOS 7 (64-bit)上，
要求系统为64位、Linux系统内核版本为 3.8以上，这里选用Centos7.x

**查看自己的内核**

uname命令用于打印当前系统相关信息（内核版本号、硬件架构、主机名称和操作系统类型等）。

![image-20220126165005382](images/image-20220126165005382.png) 



# Docker基本组成

## 1、镜像（Image）

Docker 镜像（Image）就是一个只读的模板。镜像可以用来创建 Docker 容器，一个镜像可以创建很多容器。它也相当于是一个root文件系统。比如官方镜像 centos:7 就包含了完整的一套 centos:7 最小系统的 root 文件系统。相当于容器的“源代码”，docker镜像文件类似于Java的类模板，而docker容器实例类似于java中new出来的实例对象。

## 2、容器(Container)

1、从面向对象角度

Docker 利用容器（Container）独立运行的一个或一组应用，应用程序或服务运行在容器里面，容器就类似于一个虚拟化的运行环境，容器是用镜像创建的运行实例。就像是Java中的类和实例对象一样，镜像是静态的定义，容器是镜像运行时的实体。容器为镜像提供了一个标准的和隔离的运行环境，它可以被启动、开始、停止、删除。每个容器都是相互隔离的、保证安全的平台。

2、从镜像容器角度

可以把容器看做是一个简易版的 Linux 环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序。

##  3、仓库(Repository)


仓库（Repository）是集中存放镜像文件的场所。

类似于 Maven仓库，存放各种jar包的地方；github仓库，存放各种git项目的地方；Docker公司提供的官方registry被称为Docker Hub，存放各种镜像模板的地方。

仓库分为公开仓库（Public）和私有仓库（Private）两种形式。最大的公开仓库是 Docker Hub(https://hub.docker.com/)

<img src="images/image-20220126165406986.png" alt="image-20220126165406986" style="zoom:80%;" /> 

# Docker架构

## 1、简单版

Docker是一个**Client-Server**结构的系统，Docker守护进程运行在主机上， 然后通过Socket连接从客户端访问，守护进程从客户端接受命令并管理运行在主机上的容器。 容器，是一个运行时环境，就是我们前面说到的集装箱。

![image-20220126165415848](images/image-20220126165415848.png) 

## 2、复杂版

Docker 是一个 C/S 模式的架构，后端是一个松耦合架构，众多模块各司其职。 

![image-20220126165628755](images/image-20220126165628755.png) 

![image-20220126165633562](images/image-20220126165633562.png) 

# Docker安装

## 1、卸载旧版本

```shell
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
```

## 2、安装依赖

更新yum软件包索引

```shell
yum makecache fast
```

下载依赖软件包

```shell
yum install -y yum-utils device-mapper-persistent-data lvm2 gcc gcc-c++
```

## 3、安装docker-ce

```shell
yum install docker-ce docker-ce-cli containerd.io
```

## 4、启动docker

```shell
systemctl start docker
```

## 测试

```shell
docker version
docker run hello-world
```

![image-20220126170548825](images/image-20220126170548825.png)

![image-20220126170527731](images/image-20220126170527731.png) 

![image-20220126170737307](images/image-20220126170737307.png) 

## 卸载

```shell
systemctl stop docker 
yum remove docker-ce docker-ce-cli containerd.io
rm -rf /var/lib/docker
rm -rf /var/lib/containerd
```

# Docker常用命令

| 指令                                                         | 作用                                                         |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| systemctl start docker                                       | 启动docker                                                   |
| systemctl stop docker                                        | 停止docker                                                   |
| systemctl restart docker                                     | 重启docker                                                   |
| systemctl status docke                                       | 查看docker状态                                               |
| systemctl enable docker                                      | 开机自启doker                                                |
| docker info                                                  | 查看docker信息                                               |
| docker --help                                                | 查看docker总体帮助文档                                       |
| docker 具体命令 --help                                       | 查看docker具体命令帮助文档                                   |
| docker search xxxx                                           | 在docker仓库中查找 --limit : 只列出N个镜像，默认25个         |
| docker pull imagesname：tag                                  | ：tag可选，下载镜像版本，默认latest                          |
| docker images                                                | 查看本地镜像 -a 列出本地所有的镜像（含历史映像层） -q 只显示镜像ID。 |
| docker system df                                             | 查看镜像/容器/数据卷所占的空间                               |
| docker rm image-id                                           | 删除指定本地镜像                                             |
| docker run --name container-name -d image-name               | -name 自定义容器名，-d 表示后台运行， image-name 指定容器模板 |
| docker ps -a                                                 | 查看运行中的容器，添加-a查看所有                             |
| docker stop container-name/id                                | 停止当前运行的容器                                           |
| docker start container-name/id                               | 启动容器                                                     |
| docker rm container-id                                       | 删除指定容器                                                 |
| docker logs container-name/id                                | 容器日志                                                     |
| docker start $(docker ps -a \| awk '{ print $1}' \| tail -n +2) | 启动所有容器                                                 |
| docker kill 容器名/id                                        | 强制停止容器                                                 |
| docker rm -f $(docker ps -a -q)                              | 删除所有容器                                                 |
| docker top 容器ID                                            | 查看容器内运行的进程                                         |
| docker inspect 容器ID                                        | 查看容器内部细节                                             |
| docker commit -m="ms" -a="auth" 容器ID 自定义镜像名:[标签名] | 创建一个镜像，创建后repository写的是自定义镜像名，tag是标签名。 |

## images命令显示参数说明

![image-20220126172501622](images/image-20220126172501622.png) 

> REPOSITORY：表示镜像的仓库源
>
> TAG：镜像的标签版本号
>
> IMAGE ID：镜像ID
>
> CREATED：镜像创建时间
>
> SIZE：镜像大小
>
> 同一仓库源可以有多个 TAG版本，代表这个仓库源的不同个版本，我们使用 REPOSITORY:TAG 来定义不同的镜像。
> 如果你不指定一个镜像的版本标签，例如你只使用 ubuntu，docker 将默认使用 ubuntu:latest 镜像

## run命令OPTIONS说明（常用）

有些是一个减号，有些是两个减号

> --name="容器新名字"       为容器指定一个名称；
>
> -d: 后台运行容器并返回容器ID，也即启动守护式容器(后台运行)；
>
> -i：以交互模式运行容器，通常与 -t 同时使用；
>
> -t：为容器重新分配一个伪输入终端，通常与 -i 同时使用；也即启动交互式容器(前台有伪终端，等待交互)；
>
> -P: 随机端口映射，大写P
>
> -p: 指定端口映射，小写p



![image-20220126173229444](images/image-20220126173229444.png) 

例子：使用镜像centos:latest以交互模式启动一个容器,在容器内执行/bin/bash命令

```shell
docker run -it centos /bin/bash 

参数说明：
-i: 交互式操作。
-t: 终端。
centos : centos 镜像。
/bin/bash：放在镜像名后的是命令，使用交互式 Shell，因此用的是 /bin/bash。
```

## ps命令OPTIONS说明（常用）

> -a :列出当前所有正在运行的容器+历史上运行过的
>
> -l :显示最近创建的容器。
>
> -n：显示最近n个创建的容器。
>
> -q :静默模式，只显示容器编号。

## 退出容器

run进去容器，exit退出，容器停止

run进去容器，ctrl+p+q退出，容器不停止

## 进入容器

### 1、exec命令（推荐）

```shell
docker exec -it 容器ID bashShell
```

**注意**：exec 是在容器中打开新的终端，并且可以启动新的进程，用exit退出，不会导致容器的停止。

![image-20220126184013546](images/image-20220126184013546.png) 

### 2、attach命令

```shell
docker attach 容器ID
```

**注意**：attach 命令直接进入容器启动命令的终端，不会启动新的进程，用exit退出，会导致容器的停止。

![image-20220126183951523](images/image-20220126183951523.png) 

## 复制文件容器→主机

```shell
docker cp  容器ID:容器内路径 目的主机路径
```

![image-20220126185513557](images/image-20220126185513557.png) 

## 导出与导入容器

### 1、export命令

**export** 命令导出容器的内容留作为一个tar归档文件[对应import命令]

```shell
docker export 容器ID > 文件名.tar
```

![image-20220126185731525](images/image-20220126185731525.png) 

### 2、import命令

**import** 命令从tar包中的内容创建一个新的文件系统再导入为镜像[对应export]

```shell
docker import - 镜像用户/镜像名:镜像版本号
```

![image-20220126185908567](images/image-20220126185908567.png) 

![image-20220126192227886](images/image-20220126192227886.png) 

# Docker镜像

## 1、简述

镜像是一种轻量级、可执行的独立软件包，它包含运行某个软件所需的所有内容，我们把应用程序和配置依赖打包好形成一个可交付的运行环境(包括代码、运行时需要的库、环境变量和配置文件等)，这个打包好的运行环境就是image镜像文件。只有通过这个镜像文件才能生成Docker容器实例(类似Java中new出来一个对象)。

## 2、分层镜像

以下载Tomcat为例，下载过程像是一层一层下载

<img src="images/image-20220126204358136.png" alt="image-20220126204358136" style="zoom:50%;" />

Docker镜像层都是**只读**的，容器层是**可写**的。当容器启动时，一个新的可写层被加载到镜像的顶部，这一层通常被称作“容器层”，“容器层”之下的都叫“镜像层”。

### 2.1、UnionFS（联合文件系统）

Union文件系统（UnionFS）是一种分层、轻量级并且高性能的文件系统，它支持文件系统的修改作为一次提交来一层层的叠加，同时可以**将不同目录挂载到同一个虚拟文件系统下**(unite several directories into a single virtual filesystem)。

Union 文件系统是 Docker 镜像的基础。

镜像可以通过**分层**来进行**继承**，基于基础镜像（没有父镜像），可以制作各种具体的应用镜像。

特性：一次同时加载多个文件系统，但从外面看起来，只能看到一个文件系统，联合加载会把各层文件系统叠加起来，这样最终的文件系统会包含所有底层的文件和目录

### 2.2、镜像加载原理

docker的镜像实际上由一层一层的文件系统组成-----》文件系统UnionFS。

**bootfs**(boot file system)主要包含**bootloader**和**kernel**。**bootloader**主要是引导加载kernel，Linux刚启动时会加载bootfs文件系统，在Docker镜像的最底层是引导文件系统bootfs。这一层与我们典型的Linux/Unix系统是一样的，包含boot加载器和内核。当boot加载完成之后整个内核就都在内存中了，此时内存的使用权已由bootfs转交给内核，此时系统也会卸载bootfs。

**rootfs** (root file system) ，在bootfs之上。包含的就是典型 Linux 系统中的 /dev, /proc, /bin, /etc 等标准目录和文件。rootfs就是各种不同的操作系统发行版，比如Ubuntu，Centos等等。 

![image-20220126211042024](images/image-20220126211042024.png) 

对于一个精简的OS，rootfs可以很小，只需要包括最基本的命令、工具和程序库就可以了，因为底层直接用Host的kernel，自己只需要提供 rootfs 就行了。由此可见对于不同的linux发行版，bootfs基本是一致的，rootfs会有差别，因此不同的发行版可以公用bootfs。

### 2.3、优点

镜像分层最大的一个好处就是共享资源，方便复制迁移，就是为了复用。

比如说有多个镜像都从相同的 base 镜像构建而来，那么 Docker Host 只需在磁盘上保存一份 base 镜像。同时内存中也只需加载一份 base 镜像，就可以为所有容器服务了。而且镜像的每一层都可以被共享。

## 3、镜像发布

### 3.1、发布到私有仓库

#### 3.1.1、下载镜像Docker Registry

```shell
docker pull registry
```

#### 3.1.2、运行私有库Registry

相当于本地有个私有Docker hub

```shell
docker run -d -p 5000:5000 -v /data/registry:/temp/registry --privileged=true registry
```

检查下私有仓库镜像

```shell
curl -XGET http://host:5000/v2/_catalog
```

#### 3.1.3、修改镜像tag

符合私有仓库的规范，如果符合规范就不修改

```shell
docker tag 镜像:Tag Host:Port/镜像:Tag
#docker tag zzyyubuntu:1.2 192.168.111.162:5000/zzyyubuntu:1.2
```

![image-20220126214518858](images/image-20220126214518858.png) 

#### 3.1.4、修改docker配置文件

修改配置文件使之支持http。

```shell
vim /etc/docker/daemon.json
```

```json
{
    //镜像加速一定要
  //"registry-mirrors": ["https://aa25jngu.mirror.aliyuncs.com"],
  "insecure-registries": ["host:port"]
}
```

docker默认不允许http方式推送镜像，通过配置选项来取消这个限制。====> 修改完后如果不生效，建议重启docker。

### 3.1.3、推送镜像到私有仓库

```shell
docker push 符合规范的镜像名
```

## 4、虚悬镜像

### 4.1、简介

仓库名、标签都是<none\>的镜像，俗称dangling image

### 4.2、创建虚悬镜像

```dockerfile
from ubuntu
CMD echo 'action is success'
```

```bash
docker build .
```

<img src="images/image-20220226112009728.png" alt="image-20220226112009728" style="zoom:80%;" />

### 4.3、查看

```bash
docker image ls -f dangling=true
```

<img src="images/image-20220226112049595.png" alt="image-20220226112049595" style="zoom:80%;" />

### 4.4、删除

虚悬镜像已经失去存在价值，可以删除

```bash
docker image prune
```

# Docker部署常用软件

## 1、安装Tomcat

[下载Tomcat9](https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.56/bin/apache-tomcat-9.0.56.tar.gz)

解压

```shell
tar -xzvf ........
```

启动

```shell
/tomcat/bin/startup.sh
```

配置service命令

进入到 /etc/init.d 文件夹下

```shell
vim tomcat
```

复制如下

```shell
 
        #!/bin/bash  
        # This is the init script for starting up the  
        #  Jakarta Tomcat server  
        #  
        # chkconfig: 345 91 10  
        # description: Starts and stops the Tomcat daemon.  
        #  
 
        # Source function library.  
        . /etc/rc.d/init.d/functions  
 
        # Get config.  
        . /etc/sysconfig/network  
 
        # Check that networking is up.  
        [ "${NETWORKING}" = "no" ] && exit 0  
 
        export JAVA_HOME=/root/jdk-15 #自己的jdk安装目录
        tomcat_home=/root/tomcat/apache-tomcat-9.0.56  #自己的tomcat安装目录
        startup=$tomcat_home/bin/startup.sh  
        shutdown=$tomcat_home/bin/shutdown.sh  
 
        start(){  
           echo -n "Starting Tomcat service:"  
           cd $tomcat_home  
           $startup  
           echo "tomcat is succeessfully started up"  
        }  
 
        stop(){  
           echo -n "Shutting down tomcat: "  
           cd $tomcat_home  
           $shutdown  
           echo "tomcat is succeessfully shut down."  
        }  
 
        status(){  
            numproc=`ps -ef | grep catalina | grep -v "grep catalina" | wc -l`  
            if [ $numproc -gt 0 ]; then  
               echo "Tomcat is running..."  
            else  
               echo "Tomcat is stopped..."  
            fi  
        }  
 
        restart(){  
           stop  
           start  
        }    
        # See how we were called.  
        case "$1" in  
        start)  
           start  
           ;;  
        stop)  
           stop  
           ;;  
        status)  
           status  
           ;;  
        restart)  
           restart  
           ;;  
        *)  
           echo $"Usage: $0 {start|stop|status|restart}"  
           exit 1  
        esac
```

添加权限

```shell
chmod 755 /etc/rc.d/init.d/tomcat
```

加入服务中

```shell
chkconfig --add tomcat
```

检查是否在服务中

```shell
chkconfig --list
```

设置开机在自启

```shell
chkconfig tomcat on
```

## 2、安装MySQL

### 2.1、单机版MySQL

#### 2.1.1、拉取最新的MySQL

```shell
docker pull mysql:latest
```

#### 2.1.2、查看是否已经安装了镜像

```shell
docker images
```

#### 2.1.3、运行容器

**tip**：

--name 容器名字

-p 端口映射

-e 设置属性

-itd 如下

| Options | Mean                                               |
| ------- | -------------------------------------------------- |
| -i      | 以交互模式运行容器，通常与 -t 同时使用；           |
| -t      | 为容器重新分配一个伪输入终端，通常与 -i 同时使用； |
| -d      | 后台运行容器，并返回容器ID；                       |

最后添加镜像名，例如hello world

![image-20211014113035886](images\image-20211014113035886.png)

设置root密码，允许远程登陆

```
docker run -itd --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=fuckharkadmin -e MYSQL_ROOT_HOST=% mysql #镜像名
```

#### 2.1.4、查看容器运行情况

```shell
docker ps
```

#### 2.1.5、进入容器

```shell
docker exec -it 容器名/id bash
```

#### 2.1.6、登陆mysql

```
ALTER USER 'root'@'localhost' IDENTIFIED BY 'admin';
```

#### 2.1.7、添加一个支持远程登陆的账户

```mysql
CREATE USER 'wolong'@'%' IDENTIFIED WITH mysql_native_password BY 'mnnuwolong';
GRANT ALL PRIVILEGES ON *.* TO 'wolong'@'%';
flush privileges;
```

#### 完善的启动命令

```shell
docker run -p 3306:3306 --name mysql01 \
-v /data/mysql/mysql01/log:/var/log/mysql \
-v /data/mysql/mysql01/data:/var/lib/mysql \
-v /data/mysql/mysql01/conf:/etc/mysql \
-v /data/mysql/mysql01/mysql-files:/var/lib/mysql-files \
-e MYSQL_ROOT_PASSWORD=fuckharkadmin \
-e MYSQL_ROOT_HOST=% \
-itd mysql
```

### 2.2、主从复制版

#### 2.2.1、新建Master与Slave

```bash
docker run -p 3307:3306 --name mysql-master \
-v /data/mysql/mysql-master/log:/var/log/mysql \
-v /data/mysql/mysql-master/data:/var/lib/mysql \
-v /data/mysql/mysql-master/conf:/etc/mysql \
-v /data/mysql/mysql-master/mysql-files:/var/lib/mysql-files \
-e MYSQL_ROOT_PASSWORD=fuckharkadmin \
-e MYSQL_ROOT_HOST=% \
-itd mysql

docker run -p 3308:3306 --name mysql-slave \
-v /data/mysql/mysql-slave/log:/var/log/mysql \
-v /data/mysql/mysql-slave/data:/var/lib/mysql \
-v /data/mysql/mysql-slave/conf:/etc/mysql \
-v /data/mysql/mysql-slave/mysql-files:/var/lib/mysql-files \
-e MYSQL_ROOT_PASSWORD=fuckharkadmin \
-e MYSQL_ROOT_HOST=% \
-itd mysql
```

#### 2.2.2、修改master与slave的cnf文件

进入/data/mysql/mysql-master/conf目录下新建my.cnf

```text
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=101 
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能
log-bin=mall-mysql-bin  
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062
```

修改完后重启master

进入/data/mysql/mysql-slave/conf目录下新建my.cnf

```text
[mysqld]
## 设置server_id，同一局域网中需要唯一
server_id=102
## 指定不需要同步的数据库名称
binlog-ignore-db=mysql  
## 开启二进制日志功能，以备Slave作为其它数据库实例的Master时使用
log-bin=mall-mysql-slave1-bin
## 设置二进制日志使用内存大小（事务）
binlog_cache_size=1M  
## 设置使用的二进制日志格式（mixed,statement,row）
binlog_format=mixed  
## 二进制日志过期清理时间。默认值为0，表示不自动清理。
expire_logs_days=7  
## 跳过主从复制中遇到的所有错误或指定类型的错误，避免slave端复制中断。
## 如：1062错误是指一些主键重复，1032错误是因为主从数据库数据不一致
slave_skip_errors=1062  
## relay_log配置中继日志
relay_log=mall-mysql-relay-bin  
## log_slave_updates表示slave将复制事件写进自己的二进制日志
log_slave_updates=1  
## slave设置为只读（具有super权限的用户除外）
read_only=1
```

修改完后重启slave

#### 2.2.3、进入master容器

master容器实例内创建数据同步用户

```mysql
CREATE USER 'slave'@'%' IDENTIFIED BY 'fuckharkadmin';
```

```mysql
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'slave'@'%';
```

在主数据库中查看主从同步状态

获取需要的数据

```mysql
show master status;

binlog.000002
711
```

#### 2.2.4、slave中配置主从复制

```mysql
change master to master_host='宿主机ip', master_user='给从库用的账号', master_password='该账号的密码', master_port=主机的端口, master_log_file='mall-mysql-bin.000001', master_log_pos=617, master_connect_retry=30;
```

```text
change master to master_host='138.2.46.254', master_user='slave', master_password='fuckharkadmin', master_port=3307, master_log_file='mall-mysql-bin.000001', master_log_pos=1049, master_connect_retry=30;
```

指令说明：

```text
master_host：主数据库的IP地址；
master_port：主数据库的运行端口；
master_user：在主数据库创建的用于同步数据的用户账号；
master_password：在主数据库创建的用于同步数据的用户密码；
master_connect_retry：连接失败重试的时间间隔，单位为秒。
=========需要从主库获取的============
master_log_file：指定从数据库要复制数据的日志文件，通过查看主数据的状态，获取File参数；
master_log_pos：指定从数据库从哪个位置开始复制数据，通过查看主数据的状态，获取Position参数；
```

在从数据库中查看主从同步状态

```mysql
show slave status\G
```

<img src="images/image-20220225164904566.png" alt="image-20220225164904566" style="zoom:80%;" />

#### 2.2.5、slave中开启主从同步

```mysql
# 开始主从复制
start slave;
# 停止主从复制
stop slave;
# 重置主从复制
reset slave;
```

```mysql
show slave status\G
```

![image-20220225170526508](images/image-20220225170526508.png)

如果Slave_IO_Running = yes、Slave_SQL_Running = yes则成功

#### 2.2.6、测试

主库插入数据，从库读取到

## 3、安装Redis

### 3.1、单机版

#### 3.1、首先拉取redis镜像

```shell
docker pull redis
```

#### 3.2、修改redis.conf配置文件

```text
bind 127.0.0.1 			#注释掉这部分，使redis可以外部访问
daemonize no 			#用守护线程的方式启动，改为yes会使配置文件方式启动redis失败，改为yes意为以守护进程方式启动，可后台运行，除非kill进程（可选）
requirepass admin 		#给redis设置密码,原本这行是注释掉的，关闭注释即可
appendonly yes 			#redis持久化，默认是no，（可选）
tcp-keepalive 300 		#防止出现远程主机强迫关闭了一个现有的连接的错误 默认是300
```

#### 3.3、启动redis

不挂载文件

```shell
docker run --name redis -p 6379:6379 -d --restart=always redis redis-server --appendonly yes --requirepass "admin"
```

挂载文件

```shell
docker run --name redis -p 6379:6379 -v /root/redis/config/redis.conf:/etc/redis/redis.conf -v /root/redis/data:/data -d redis redis-server /etc/redis/redis.conf --appendonly yes
```

命令解释说明：

- -p 6379:6379：端口映射：前表示主机部分，：后表示容器部分。
- --name redis：指定该容器名称，查看和进行操作都比较方便。
- -v 挂载目录：规则与端口映射相同。/root/redis/config/redis.conf表示自己的配置文件，/etc/redis/redis.conf表示容器的配置文件
- -d redis：表示后台启动redis
- redis-server /etc/redis/redis.conf：以配置文件启动redis，加载容器内的conf文件，最终找到的是挂载的目录/usr/local/docker/redis.conf
- --appendonly yes：开启redis 持久化

```properties
# REDIS配置
 
# Redis数据库索引（默认为0）
spring.redis.database=0
 
# Redis服务器IP地址
spring.redis.host=127.0.0.1
 
# Redis服务器连接端口
spring.redis.port=6379
 
# Redis服务器连接密码（默认为空，可不配置此项）
# spring.redis.password=xxx
 
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.lettuce.pool.max-active=500
 
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.lettuce.pool.max-wait=-1
 
# 连接池中的最大空闲连接
spring.redis.lettuce.pool.max-idle=8
 
# 连接池中的最小空闲连接
spring.redis.lettuce.pool.min-idle=0
 
# 连接超时时间（毫秒）
spring.redis.timeout=0
```

如果出现以下报错：

docker: Error response from daemon: driver failed programming external connectivity on endpoint redis

重启Docker

```shell
systemctl restart docker
```

#### 3.4、进入Redis

```shell
docker exec -it 容器ID bash
redis-cli	#进入客户端
auth admin #验证密码
```

```shell
docker exec -it 容器ID redis-cli
```

#### 完善的启动命令

```shell
docker run -p 6379:6379 --name redis01 \
-v /data/redis/redis01/data:/data \
-v /data/redis/redis01/conf/redis.conf:/etc/redis/redis.conf \
-itd --privileged=true redis redis-server /etc/redis/redis.conf/redis.conf
```

redis指定配置文件启动时一定要指定到具体文件，不能是父级文件夹，否者虽然不会加载失败，但是配置文件会失效

### 3.2、集群版

#### 3.2.1、启动六台Redis容器

三主三从，启动六个redis容器

```bash
docker run -d --name redis-node-1 --net host --privileged=true \
-v /data/redis/share/redis-node-1:/data redis \
--cluster-enabled yes --appendonly yes --port 6381

docker run -d --name redis-node-2 --net host --privileged=true \
-v /data/redis/share/redis-node-2:/data redis \
--cluster-enabled yes --appendonly yes --port 6382
 
docker run -d --name redis-node-3 --net host --privileged=true \
-v /data/redis/share/redis-node-3:/data redis \
--cluster-enabled yes --appendonly yes --port 6383
 
docker run -d --name redis-node-4 --net host --privileged=true \
-v /data/redis/share/redis-node-4:/data redis \
--cluster-enabled yes --appendonly yes --port 6384
 
docker run -d --name redis-node-5 --net host --privileged=true \
-v /data/redis/share/redis-node-5:/data redis \
--cluster-enabled yes --appendonly yes --port 6385
 
docker run -d --name redis-node-6 --net host --privileged=true \
-v /data/redis/share/redis-node-6:/data redis \
--cluster-enabled yes --appendonly yes --port 6386
```

```text
--net host 				使用宿主机的IP和端口，默认
--cluster-enabled yes 	开启redis集群
--appendonly yes		开启持久化
--port 6386				redis端口号
```

#### 3.2.2、为6台机器构建集群关系

进入redis-node-1

```bash
docker exec -it redis-node-1 /bin/bash
```

```bash
redis-cli --cluster create node1的ip:port node2的ip:port node3的ip:port node4的ip:port node5的ip:port node6的ip:port --cluster-replicas 1
```

```bash
redis-cli --cluster create 138.2.46.254:6381 138.2.46.254:6382 138.2.46.254:6383 138.2.46.254:6384 138.2.46.254:6385 138.2.46.254:6386 --cluster-replicas 1
```

<img src="images/image-20220225230121206.png" alt="image-20220225230121206" style="zoom:80%;" />

<img src="images/image-20220225230142254.png" alt="image-20220225230142254" style="zoom:80%;" />

搞定

#### 3.2.3、进入node1查看集群状态

```bash
redis-cli -p 6381
```

```bash
cluster info
```

<img src="images/image-20220225230341974.png" alt="image-20220225230341974" style="zoom:80%;" />

```bash
cluster nodes
```

<img src="images/image-20220225230447293.png" alt="image-20220225230447293" style="zoom:80%;" />

#### 3.2.4、数据读写存储

对6381新增两个key

![image-20220225230651410](images/image-20220225230651410.png)

出现错误，因为当前hash算法计算出来k1应该存在node3，可是当前登陆的是在node1，超出了槽范围，12706即为槽号

连接6381时添加 -c 参数优化路由

```bash
redis-cli -p 6381 # 单机模式连接
```

```bash
redis-cli -p 6381 -c # 集群模式连接
```

![image-20220225231451958](images/image-20220225231451958.png)

置入key，重定向到hash算法计算出来的槽号所对应的master节点

查看集群信息

```bash
redis-cli --cluster check 138.2.46.254:6381
```

<img src="images/image-20220225231701984.png" alt="image-20220225231701984" style="zoom:80%;" />

#### 3.2.5、容错迁移

##### 3.2.5.1、测试master宕机

先停止node1，进入node2查看集群信息

可以看到node1已被node6顶替

<img src="images/image-20220225232100571.png" alt="image-20220225232100571" style="zoom:80%;" />

##### 3.2.5.2、还原之前的3主3从

启动node1，查看集群状态

node1上线后作为slave节点

![image-20220225232326954](images/image-20220225232326954.png)

重启node6，进入node1查看集群状态

ndoe1夺回master，node6变回slave

<img src="images/image-20220225232728002.png" alt="image-20220225232728002" style="zoom:80%;" />

查看集群状态

<img src="images/image-20220225233113999.png" alt="image-20220225233113999" style="zoom:80%;" />

#### 3.2.6、扩容

##### 3.2.6.1、新增俩个节点

```bash
docker run -d --name redis-node-7 --net host --privileged=true \
-v /data/redis/share/redis-node-7:/data redis \
--cluster-enabled yes --appendonly yes --port 6387

docker run -d --name redis-node-8 --net host --privileged=true \
-v /data/redis/share/redis-node-8:/data redis \
--cluster-enabled yes --appendonly yes --port 6388
```

##### 3.2.6.2、新增一个master节点

进入node7容器，将新增的node7节点(空槽号)作为master节点加入原集群

```bash
redis-cli --cluster add-node 自己实际IP地址:6387 自己实际IP地址:6381
```

node7就是将要作为master新增节点，node1就是原来集群节点里面的领路人

```bash
redis-cli --cluster add-node 138.2.46.254:6387 138.2.46.254:6381
```

<img src="images/image-20220225234410149.png" alt="image-20220225234410149" style="zoom:80%;" />

检查集群状态，此时node7空槽

<img src="images/image-20220225234813749.png" alt="image-20220225234813749" style="zoom:80%;" />

##### 3.2.6.3、重新分配槽号

给node7分配槽号

```bash
redis-cli --cluster reshard IP地址:端口号
redis-cli --cluster reshard 138.2.46.254:6381
```

<img src="images/image-20220225235326668.png" alt="image-20220225235326668" style="zoom:80%;" />

为什么6387是3个新的区间，以前的还是连续？

重新分配成本太高，所以前3家各自匀出来一部分，从node1/node2/node3三个旧节点分别匀出1364个坑位给新节点node7

<img src="images/image-20220225235436194.png" alt="image-20220225235436194" style="zoom:80%;" />

##### 3.2.6.4、添加node8作为slave

```bash
redis-cli --cluster add-node ip:新slave端口 ip:master端口 --cluster-slave --cluster-master-id master节点ID
```

```bash
redis-cli --cluster add-node 138.2.46.254:6388 138.2.46.254:6387 --cluster-slave --cluster-master-id 545c6964b11f682de2afa928950e6761dad23a6b
```

<img src="images/image-20220225235849453.png" alt="image-20220225235849453" style="zoom:80%;" />

查看集群状态

<img src="images/image-20220225235740740.png" alt="image-20220225235740740" style="zoom:80%;" />

#### 3.2.7、缩容

目的：node7和node8下线

##### 3.2.7.1、删除slave节点

先查看集群状态，获取node8的ID

<img src="images/image-20220226000129298.png" alt="image-20220226000129298" style="zoom:80%;" />

删除node8

```bash
redis-cli --cluster del-node ip:从机node8端口 从机node8节点ID
```

```bash
redis-cli --cluster del-node 138.2.46.254:6388 7a3163b3c5c527c21c89b40013c55fa6636a5ade
```

<img src="images/image-20220226000313749.png" alt="image-20220226000313749" style="zoom:80%;" />

查看集群状态

<img src="images/image-20220226000345089.png" alt="image-20220226000345089" style="zoom:80%;" />

##### 3.2.7.2、清空master槽号

将node7的槽号清空，重新分配，本例将清出来的槽号都给node1

```bash
redis-cli --cluster reshard 138.2.46.254:6381
```

<img src="images/image-20220226000610472.png" alt="image-20220226000610472" style="zoom:80%;" />

由node1接收node7的槽

![image-20220226000707192](images/image-20220226000707192.png)

填入要清空的节点的槽，输入done结束

![image-20220226000837319](images/image-20220226000837319.png)

检查集群状态，4096个槽位都指给node1，它变成了8192个槽位，相当于全部都给node1了

<img src="images/image-20220226001057668.png" alt="image-20220226001057668" style="zoom:80%;" />

删除node7

```bash
redis-cli --cluster del-node ip:端口 6387节点ID
```

## 4、安装MariaDB

```shell
docker pull mariadb
```

```shell
docker run -itd -p 3306:3306 --name mariadb --env MARIADB_USER=wolong --env MARIADB_PASSWORD=m --env MARIADB_ROOT_PASSWORD=admin mariadb:latest
```

## 5、安装RabbitMQ

```shell
docker pull rabbitmq:management
```

```shell
docker run -itd -p 5672:5672 \
-p 15672:15672 \
--hostname rabbit01 \
--name some-rabbit \
-e RABBITMQ_DEFAULT_USER=wolong \
-e RABBITMQ_DEFAULT_PASS=mnnuwolong \
rabbitmq:management
```

# Docker容器数据卷

## 1、数据卷

类似Redis里面的rdb和aof文件，将docker容器内的数据保存进宿主机的磁盘中。

示例：运行一个带有容器卷存储功能的容器实例

```bash
docker run -it --privileged=true -v 宿主机绝对路径目录:容器内目录 镜像名
```

## 2、作用

### 2.1、需求

将应用与运行的环境打包镜像，run后形成容器实例运行 ，但是希望数据是持久化的。

Docker容器产生的数据，如果不备份，那么当容器实例删除后，容器内的数据自然也就没有了。

为了能保存数据在docker中我们使用卷。

### 2.2、特点

1. 数据卷可在**容器之间**共享或重用数据
2. **容器卷**中的更改可以直接**实时生效** 
3. 数据卷中的更改不会包含在镜像的更新中
4. 数据卷的生命周期**一直持续到没有容器使用它为止** 

## 3、示例

### 3.1、宿主与容器之间

直接命令添加

```bash
docker run -it --privileged=true -v 宿主机绝对路径目录:容器内目录  镜像名
```

查看是否添加成功

```bash
docker inspect 容器ID
```

![image-20220224210533714](images/image-20220224210533714.png) 

docker容器stop，主机修改，docker容器重启看数据同步。

## 4、读写规则映射添加

### 4.1、读写(默认)

```bash
docker run -it --privileged=true -v 宿主机绝对路径目录:容器内目录:rw 镜像名
```

默认就是rw

### 4.2、只读

容器实例内部被限制，只能读取不能写。

```bash
docker run -it --privileged=true -v 宿主机绝对路径目录:容器内目录:ro 镜像名
```

容器实例内部被限制，只能读取不能写。

ro = read only

此时如果宿主机写入内容，可以同步给容器内，容器可以读取到。

## 5、卷的继承和共享

首先创建容器A1与宿主机进行映射

```bash
docker run -it  --privileged=true -v 宿主机文件绝对路径:容器文件绝对路径 --name A1 镜像名
```

容器B1继承容器A1的卷规则

```bash
docker run -it  --privileged=true --volumes-from A1  --name B1 镜像名
```

# DockerFile

## 1、简介

Dockerfile是用来构建Docker镜像的文本文件，是由一条条构建镜像所需的指令和参数构成的脚本。

Dockerfile定义了进程需要的一切东西，Dockerfile涉及的内容包括执行代码或者是文件、环境变量、依赖包、运行时环境、动态链接库、操作系统的发行版、服务进程和内核进程(当应用进程需要和系统服务和内核进程打交道，这时需要考虑如何设计namespace的权限控制)等等。

<img src="images/image-20220226103930507.png" alt="image-20220226103930507" style="zoom:80%;" />

Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段：

*  Dockerfile是软件的原材料
*  Docker镜像是软件的交付品
*  Docker容器则可以认为是软件镜像的运行态，也即依照镜像运行的容器实例

## 2、基本规则

1. 每条**保留字指令**都必须为**大写字母**且后面要跟随**至少一个参数**。
2. 指令按照从上到下，顺序执行。
3. #表示注释。
4. 每条指令都会创建一个**新的镜像层**并对镜像进行提交。

## 3、执行流程

1. docker从基础镜像运行一个容器
2. 执行一条指令并对容器作出修改
3. 执行类似docker commit的操作提交一个新的镜像层
4. docker再基于刚提交的镜像运行一个新容器
5. 执行dockerfile中的下一条指令直到所有指令都执行完成

## 4、保留字指令

| 保留字指令 | 作用                                                         |
| ---------- | ------------------------------------------------------------ |
| FROM       | 基础镜像，当前新镜像是基于哪个镜像的，指定一个已经存在的镜像作为模板，第一条必须是FROM |
| MAINTAINER | 镜像维护者的姓名和邮箱地址                                   |
| RUN        | 容器构建时需要运行的命令，RUN是在docker **build**时运行 <br/>RUN指令有两种格式：<br/>shell格式：RUN yum -y install vim <br/>![image-20220226104802939](images/image-20220226104802939.png)<br/>exec格式：<br/>![image-20220226104825362](images/image-20220226104825362.png) |
| EXPOSE     | 当前容器对外暴露出的端口                                     |
| WORKDIR    | 指定在创建容器后，终端默认登陆的进来工作目录，一个落脚点     |
| USER       | 指定该镜像以什么样的用户去执行，如果都不指定，默认是root     |
| ENV        | 用来在构建镜像过程中设置环境变量                             |
| ADD        | 将宿主机目录下的文件拷贝进镜像且会自动处理URL和解压tar压缩包 |
| COPY       | 类似ADD，拷贝文件和目录到镜像中。<br/>将从构建上下文目录中 <源路径> 的文件/目录复制到新的一层的镜像内的 <目标路径> 位置。<br/>COPY ["src", "dest"]  ===》COPY src dest <br/><src源路径>：源文件或者源目录 <br/><dest目标路径>：容器内的指定路径，该路径不用事先建好，路径不存在的话，会自动创建。 |
| VOLUME     | 容器数据卷，用于数据保存和持久化工作                         |
| CMD        | 指定容器启动后的要干的事情 <br/><img src="images/image-20220226105506821.png" alt="image-20220226105506821" style="zoom:80%;" /> <br/>Dockerfile 中可以有多个 CMD 指令，但只有**最后一个生效**，**CMD 会被 docker run 之后的参数替换** <br/>CMD是在docker run时运行，RUN是在docker build时运行 |
| ENTRYPOINT | 也是用来指定一个容器启动时要运行的命令<br/>类似于 CMD 指令，但是**ENTRYPOINT不会被docker run后面的命令覆盖**，而且这些命令行参数会被当作参数送给 ENTRYPOINT 指令指定的程序 <br/>格式：<br/>![image-20220226105922096](images/image-20220226105922096.png)<br/>ENTRYPOINT可以和CMD一起用，一般是**变参**才会使用 CMD ，当指定了ENTRYPOINT后，CMD的含义就发生了变化，不再是直接运行其命令而是将CMD的内容作为参数传递给ENTRYPOINT指令，他两个组合会变成 CMD 给 ENTRYPOINT 传参。<img src="images/image-20220226110157277.png" alt="image-20220226110157277" style="zoom:50%;" /><br/>优点：在执行docker run的时候可以指定 ENTRYPOINT 运行所需的参数。<br/>如果 Dockerfile 中如果存在多个 ENTRYPOINT 指令，**仅最后一个生效**。 |

ENTRYPOINT例子：

假设已通过 Dockerfile 构建了 nginx:test 镜像：

<img src="images/image-20220226110327030.png" alt="image-20220226110327030" style="zoom:80%;" />

| 是否传参   | 按照dockerfile编写执行         | 传参运行                                      |
| ---------- | ------------------------------ | --------------------------------------------- |
| Docker命令 | docker run  nginx:test         | docker run  nginx:test -c /etc/nginx/new.conf |
| 实际命令   | nginx -c /etc/nginx/nginx.conf | nginx -c /etc/nginx/new.conf                  |

## 5、实例

### 5.1、自定义Centos2JDK

#### 5.1.2、编写DockerFile

ADD 是**相对路径**，把.gz文件添加到容器中，安装包必须要和Dockerfile文件在同一位置。

```dockerfile
FROM centos
MAINTAINER xxx<xxx@xxx.com>
 
ENV MYPATH /usr/local
WORKDIR $MYPATH
 
#安装vim编辑器
RUN yum -y install vim
#安装ifconfig命令查看网络IP
RUN yum -y install net-tools
#安装java8及lib库
RUN yum -y install glibc.i686
RUN mkdir /usr/local/java
#ADD 是相对路径jar,把jdk-8u171-linux-x64.tar.gz添加到容器中,安装包必须要和Dockerfile文件在同一位置
ADD jdk-8u171-linux-x64.tar.gz /usr/local/java/
#配置java环境变量
ENV JAVA_HOME /usr/local/java/jdk1.8.0_171
ENV JRE_HOME $JAVA_HOME/jre
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar:$JRE_HOME/lib:$CLASSPATH
ENV PATH $JAVA_HOME/bin:$PATH
 
EXPOSE 80
 
CMD echo $MYPATH
CMD echo "success--------------ok"
CMD /bin/bash
```

#### 5.1.3、构建镜像

build指令最后有一个“ . ”切记

```bash
docker build -t 新镜像名字:TAG .
```





# 扩展

## 1、容器与虚拟机比较

**虚拟机**：

缺点：资源占用大，冗余多，启动慢

![vm](images/vm.jpeg) 

**容器**：

Linux发展出了另一种虚拟化技术：Linux容器(Linux Containers，缩写为 LXC)

Linux容器是与系统其他部分隔离开的一系列进程，从另一个镜像运行，并由该镜像提供支持进程所需的全部文件。容器提供的镜像包含了应用的所有依赖项，因而在从开发到测试再到生产的整个过程中，它都具有可移植性和一致性。

Linux 容器不是模拟一个完整的操作系统而是**对进程进行隔离**。有了容器，就可以将软件运行所需的所有资源打包到一个隔离的容器中。容器与虚拟机不同，不需要捆绑一整套操作系统，只需要软件工作所需的库资源和设置。系统因此而变得高效轻量并保证部署在任何环境中的软件都能始终如一地运行。

![vm](images/vm-16431867861881.jpeg) 

1、docker有着比虚拟机更少的抽象层

由于docker不需要Hypervisor(虚拟机)实现硬件资源虚拟化，运行在docker容器上的程序直接使用的都是实际物理机的硬件资源。因此在CPU、内存利用率上docker将会在效率上有明显优势。

2、docker利用的是宿主机的内核,而不需要加载操作系统OS内核

当新建一个容器时，docker不需要和虚拟机一样重新加载一个操作系统内核。进而避免引寻、加载操作系统内核返回等比较费时费资源的过程，当新建一个虚拟机时，虚拟机软件需要加载OS，返回新建过程是分钟级别的。而docker由于直接利用宿主机的操作系统，则省略了返回过程，因此新建一个docker容器只需要几秒钟。

![image-20220126170919455](images/image-20220126170919455.png) 

![vm](images/vm-16431881956682.jpeg) 

## 2、docker虚悬镜像

仓库名、标签都是\<none>的镜像，俗称虚悬镜像dangling image

##  3、前台模式与后台模式

后台模式：

首先：Docker容器后台运行，就必须有一个前台进程。

如果在docker run后面追加-d=true或者-d，那么容器将会运行在后台模式。此时所有I/O数据只能通过网络资源或者共享卷组来进行交互。因为容器不再监听你执行docker run的这个终端命令行窗口。但你可以通过执行docker attach来重新附着到该容器的会话中。需要注意的是，容器运行在后台模式下，是不能使用--rm选项的。

容器运行的命令如果不是那些一直挂起的命令（比如运行top，tail），就是会自动退出的。例如 docker run -d centos运行一个容器，再使用docker ps -a查看，发现容器已经下线，因为容器没有需要执行的前台进程，就直接下线。因此最佳的解决方案是，将你要运行的程序以前台进程的形式运行，常见就是命令行模式，表示我还有交互操作，别中断。

前台模式：

在前台模式下（不指定-d参数即可），Docker会在容器中启动进程，同时将当前的命令行窗口附着到容器的标准输入、标准输出和标准错误中。也就是说容器中所有的输出都可以在当前窗口中看到。甚至它都可以虚拟出一个TTY窗口，来执行信号中断。这一切都是可以配置的：

>-a, --attach value                Attach to STDIN, STDOUT or STDERR (default [])
>
>-t, --tty                         Allocate a pseudo-TTY
>
>--sig-proxy                   Proxy received signals to the process (default true)
>
>-i, --interactive                 Keep STDIN open even if not attached

例如 docker run -it --name=interactive centos 前台启动一个centos，启动后会直接进入到其内部，但是退出后容器下线。

如果在执行run命令时没有指定-a参数，那么Docker默认会挂载所有标准数据流，包括输入输出和错误，你可以单独指定挂载哪个标准流。

```shell
docker run -a stdin -a stdout -i -t ubuntu /bin/bash
```







