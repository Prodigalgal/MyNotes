# Docker简介

Docker的出现使得Docker得以打破过去「程序即应用」的观念

透过镜像(images)将作业系统核心除外，运作应用程式所需要的系统环境，由下而上打包，达到应用程式跨平台间的无缝接轨运作

解决了运行环境和配置问题的软件容器，方便做持续集成并有助于整体发布的容器虚拟化技术



# Docker基本组成

## 1、镜像（Image）

Docker 镜像（Image）就是一个只读的模板

镜像可以用来创建 Docker 容器，一个镜像可以创建很多容器，它也相当于是一个root文件系统

比如官方镜像 centos:7 就包含了完整的一套 centos:7 最小系统的 root 文件系统，相当于容器的“源代码”，docker镜像文件类似于Java的类模板，而docker容器实例类似于java中new出来的实例对象



## 2、容器(Container)

1、从面向对象角度

Docker 利用容器（Container）独立运行的一个或一组应用，应用程序或服务运行在容器里面，容器就类似于一个虚拟化的运行环境，容器是用镜像创建的运行实例

就像是Java中的类和实例对象一样，镜像是静态的定义，容器是镜像运行时的实体

容器为镜像提供了一个标准的和隔离的运行环境，它可以被启动、开始、停止、删除

每个容器都是相互隔离的、保证安全的平台

2、从镜像容器角度

可以把容器看做是一个简易版的 Linux 环境（包括root用户权限、进程空间、用户空间和网络空间等）和运行在其中的应用程序



##  3、仓库(Repository)


仓库（Repository）是集中存放镜像文件的场所。

类似于 Maven仓库，存放各种jar包的地方；github仓库，存放各种git项目的地方；Docker公司提供的官方registry被称为Docker Hub，存放各种镜像模板的地方

仓库分为公开仓库（Public）和私有仓库（Private）两种形式，最大的公开仓库是 Docker Hub(https://hub.docker.com/)

<img src="images/image-20220126165406986.png" alt="image-20220126165406986" style="zoom:80%;" /> 

# Docker架构

## 1、简单版

Docker是一个**Client-Server**结构的系统，Docker守护进程运行在主机上， 然后通过Socket连接从客户端访问，守护进程从客户端接受命令并管理运行在主机上的容器，容器，是一个运行时环境，就是我们前面说到的集装箱

![image-20220126165415848](images/image-20220126165415848.png) 



## 2、复杂版

Docker 是一个 C/S 模式的架构，后端是一个松耦合架构，众多模块各司其职

![image-20220126165628755](images/image-20220126165628755.png) 

![image-20220126165633562](images/image-20220126165633562.png) 



# Docker安装

**前提条件**

目前，CentOS 仅发行版本中的内核支持 Docker。Docker 运行在CentOS 7 (64-bit)上，
要求系统为64位、Linux系统内核版本为 3.8以上，这里选用Centos7.x

**查看自己的内核**

uname命令用于打印当前系统相关信息（内核版本号、硬件架构、主机名称和操作系统类型等）。

![image-20220126165005382](images/image-20220126165005382.png) 



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

~~~bash
# 由于RHEL8已从Docker转向PodMan所以需要先
dnf config-manager --add-repo=https://download.docker.com/linux/centos/docker-ce.repo

# 阿里云
yum config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
~~~

```shell
yum install -y docker-ce docker-ce-cli containerd.io
```

## 4、启动docker

```shell
systemctl start docker	   # 启动Docker守护进程

systemctl stop docker      # 停止Docker守护进程

systemctl restart docker   # 重启Docker守护进程

systemctl enable docker    #设置Docker开机自启动

systemctl status docker    #查看Docker的运行状态
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



~~~bash
docker run -itd \
-p 9099:8080 \
--name tomcat \
-v /data/docker/tomcat/webapps:/usr/local/tomcat/webapps \
--privileged=true \
tomcat
~~~





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
--net host 				使用宿主机的IP和端口，默认，rediscluster模式在docker中仅仅支持host模式
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



## 6、安装CentOS7

同样适用于OracleLinux7/8

~~~bash
docker pull centos:7
~~~

~~~bash
docker run --name cos -itd --privileged=true -p 9099:22 centos:7 /usr/sbin/init
~~~

~~~bash
yum install vim openssh-server openssh-clients -y
~~~

~~~bash
 vim /etc/ssh/sshd_config
 允许root登陆，允许账号密码登陆
 
 passwd
 修改root密码d
~~~

~~~bash
systemctl restart sshd.service
重启sshd
~~~

**建议**：主机仅打开需要映射到22端口的端口



## 7、安装Nginx

~~~bash
docker run -itd  --name nginx \
-p 80:80 \
-v /data/docker/nginx/html:/usr/share/nginx/html \
-v /data/docker/nginx/conf/nginx.conf:/etc/nginx/nginx.conf \
-v /data/docker/nginx/conf/conf.d:/etc/nginx/conf.d  \
-v /data/docker/nginx/logs:/var/log/nginx \
-v /data/docker/nginx/etc/html:/etc/nginx/html \
--privileged=true \
nginx
~~~

**问题**：

- 似乎静态文件应该放在 /etc/nginx/html 而非 /usr/share/nginx/html，从error.log文件中看出



## 8、安装Jupyter

方法一：选择DockerHub上官方包

方法二：自定义

~~~bash
# 启动一个oraclelinux容器，OL9

#1、安装python3
yum install python

#2、安装pip
yum install pip

#3、安装依赖，否则报错ERROR: Failed building wheel for psutil
搜索python3-dev
yum search python3
安装python3-dev
yum install python3-devel.aarch64
安装DevTools
yum groupinstall "Development Tools"

#4、安装jupyter
pip install jupyter
pip install jupyterlab

#5、将jupyter添加到环境变量中
find -name jupyter
vi /etc/profile

export JUPYTER_PATH=/usr/local/bin
export PATH=$PATH:$JUPYTER_PATH

重启环境变量
source /etc/profile

#6、生成jupyter密码，如果是notebook则将lab修改为notebook
jupyter-lab password
mnnuadmin
生成一个密码文件进入查看得到hash

#7、生成并修改jupyterlab或者notebook的配置文件
jupyter-lab--generate-config --allow-root
jupyter-notebook --generate-config --allow-root

#允许跨域
c.ServerApp.allow_origin = '*'
#不可随意更改密码
c.ServerApp.allow_password_change = False
#允许远程访问
c.ServerApp.allow_remote_access = True
#允许局域网本地访问
c.ServerApp.ip = '0.0.0.0'
#设置端口
c.ServerApp.port = 8888
#设置工作路径，确保文件夹存在
c.ServerApp.notebook_dir = '/usr/local/bin/work'
#开始服务时不启动浏览器
c.ServerApp.open_browser = False
#密码的hash
c.ServerApp.password = 'argon2:$argon2id$v=19$m=10240,t=10,p=8$DlJ5O6/O8+8DYu9WZXJqxg$Dd4rLTCldQGeEzQy/GXXsenPrQRDnR1SPGfnBcMwKrM'

#8、启动jupyter
前台启动
jupyter lab --allow-root
后台启动
nohup jupyter notebook --allow-root > /jupyter/jupyter.log 2>&1 &
~~~

附带安装R内核

~~~bash
#1、安装依赖
yum install make gcc gcc-c++ libcurl-devel libxml2-devel openssl-devel texlive-*

#2、前往该网站下载缺失的依赖
http://rpmfind.net/linux/rpm2html/search.php
下载依赖
wget [url] 
rpm添加强制安装参数
rpm -i *.rpm --force --nodeps

#3、在R控制台中安装devtools与内核
install.packages('devtools')
install.packages('IRkernel')

#4、在R控制台中激活内核
IRkernel::installspec()
~~~



## 9、安装Harbor

注意：证书部份存疑

完整安装 Docker 与 Docker-Compose

~~~bash
# 下载 离线版 Harbor
wget https://github.com/goharbor/harbor/releases/download/v2.5.5/harbor-offline-installer-v2.5.5.tgz
~~~

~~~bash
# 创建解压目录 Harbor
mkdir harbor
# 进入解压目录 Harbor
cd harbor
# 解压压缩包到当前目录下的 Harbor 目录中
tar -zxvf harbor-offline-installer-v2.5.5.tgz
~~~

~~~bash
# 各个主机添加 host 映射
vim /etc/hosts
192.168.100.142 fuckharbor.com
systemctl restart NetworkManager
~~~

~~~bash
# 生成自签名根证书的私钥
openssl genrsa -out ca.key 4096
# 使用私钥生成自签名根证书
openssl req -x509 -new -nodes -sha512 -days 3650 \
 -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=fuckharbor.com" \
 -key ca.key \
 -out ca.crt
~~~

~~~bash
# 生成服务端私钥
openssl genrsa -out fuckharbor.com.key 4096

# 使用服务端私钥生成服务端证书
openssl req -sha512 -new \
    -subj "/C=CN/ST=Beijing/L=Beijing/O=example/OU=Personal/CN=fuckharbor.com" \
    -key fuckharbor.com.key \
    -out fuckharbor.com.csr

# 生成 x509 v3 扩展文件
cat > v3.ext <<-EOF
authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
keyUsage = digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
extendedKeyUsage = serverAuth
subjectAltName = @alt_names

[alt_names]
DNS.1=fuckharbor.com
DNS.2=fuckharbor
EOF

# 使用根证书私钥对服务端证书签名
openssl x509 -req -sha512 -days 3650 \
    -extfile v3.ext \
    -CA ca.crt -CAkey ca.key -CAcreateserial \
    -in fuckharbor.com.csr \
    -out fuckharbor.com.crt

# 至此获取到 fuckharbor.com.crt 文件与 fuckharbor.com.key 文件
~~~

~~~bash
# 向 Harbor、Docker、hosts 提供证书
# 将 fuckharbor.com.crt 与 fuckharbor.com.key 移动到 harbor.yml SSL 部份所描述的 cert 证书位置
# 分发 ca.key、fuckharbor.com.key 到各个节点的 /usr/local/share/ca-certificates/ 文件夹下

# 向 Docker 提供文件
openssl x509 -inform PEM -in fuckharbor.com.crt -out fuckharbor.com.cert
# 将 ca.crt、fuckharbor.com.crt、fuckharbor.com.key 提供给 Docker 的 /etc/docker/certs.d/fuckharbor.com/
~~~

~~~bash
# 各个节点执行命令更新证书
update-ca-certificates
systemctl daemon-reload && systemctl restart containerd.service

# Docker 节点重启 Docker
systemctl restart docker
~~~

~~~bash
# 启动 Harbor
./prepare
./install.sh

# 登陆 Harbor 
docker login fuckharbor.com
# 填写 harbor.yml 设定的账号密码
# 进入 harbor Web，管理仓库，添加一个测试仓库 test
# 给一个镜像打上标签 fuckharbor.com/test/xxx:v1.0
# 推送镜像
docker push fuckharbor.com/test/xxx:v1.0
~~~

~~~bash
# 制作 K8s 密钥，从 Harbor 拉取镜像时需要用到
# 有两种方式：
# 从 /.docker/config.json 获取凭证，base64 编码写入 Secret 文件中
cat ~/.docker/config.json | base64

kubectl create secret generic regcred \
    --from-file=.dockerconfigjson=.docker/config.json \
    --type=kubernetes.io/dockerconfigjson

# 或者直接用命令行生成
kubectl create secret docker-registry fuckharbor\
  --docker-server=fuckharbor.com \
  --docker-username=admin \
  --docker-password=Harbor12345
~~~



## 10、安装GitLab

~~~bash
docker run -itd \
-p 8443:443 -p 8090:80 -p 8022:22 \
--restart always --name gitlab \
-v /usr/local/gitlab/etc:/etc/gitlab \
-v /usr/local/gitlab/log:/var/log/gitlab \
-v /usr/local/gitlab/data:/var/opt/gitlab \
--privileged=true gitlab/gitlab-ce
~~~



## 11、安装Go服务

建议 main.go 文件放置在根目录处，且 Dockerfile 与其同级

使用分层镜像可以大大减小容器大小

~~~dockerfile
from golang:alphine as build
ENV GO111MODULE=on \
	CGGO_ENABLED=on \
	GOOS=linux \
	GOARCH=amd64 \
	GOPROXY="https://goproxy.cn,direct"

WORKDIR /app
COPY . .
RUN go build -o main .

FROM scratch
WORKDIR /
COPY --from=build /app/main main
EXPOSE 8080
CMD["./main"]
~~~



# Docker容器数据卷

## 1、数据卷

类似Redis里面的rdb和aof文件，将docker容器内的数据保存进宿主机的磁盘中

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

Dockerfile定义了进程需要的一切东西，Dockerfile涉及的内容包括执行代码或者是文件、环境变量、依赖包、运行时环境、动态链接库、操作系统的发行版、服务进程和内核进程(当应用进程需要和系统服务和内核进程打交道，这时需要考虑如何设计namespace的权限控制)等等

<img src="images/image-20220226103930507.png" alt="image-20220226103930507" style="zoom:80%;" />

Dockerfile、Docker镜像与Docker容器分别代表软件的三个不同阶段：

*  Dockerfile是软件的原材料
*  Docker镜像是软件的交付品
*  Docker容器则可以认为是软件镜像的运行态，也即依照镜像运行的容器实例

**注意**：

- 每条指令都是独立运行的，并会创建一个新镜像，因此RUN cd /tmp不会对下一条指令产生任何影响。



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
| 解释       | ENTRYPOINT + CMD               | ENTRYPOINT + 外部传参                         |

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

### 5.2、Docker微服务实例

#### 5.2.1、微服务

新建一个微服务模块，并打包成jar

#### 5.2.2、发布到docker

通过dockerfile，发布到到docker

```dockerfile
# 基础镜像使用java
FROM openjdk:11
# 作者
MAINTAINER ppz
# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp
# 将jar包添加到容器中并更名为ppz.jar
ADD RedisCloud-0.0.1-SNAPSHOT.jar ppz.jar
# 运行jar包
RUN bash -c 'touch /ppz.jar'
ENTRYPOINT ["java","-jar","/ppz.jar"]
# 暴露8081端口作为微服务
EXPOSE 8081
```

#### 5.2.3、构建镜像

```bash
docker build -t ppz:1.6 .
```

运行容器测试即可

# Docker网络

## 1、概述

docker启动后，会产生一个名为docker0的虚拟网桥

![image-20220306192151042](images/image-20220306192151042.png)

docker网络自带的默认三种网络模式

![image-20220306192024957](images/image-20220306192024957.png)

容器间的互联和通信以及端口映射

容器IP变动时候可以通过服务名直接网络通信而不受到影响。

docker容器内部的ip是有可能会发生改变的

## 2、基本命令

![image-20220306192333960](images/image-20220306192333960.png)

| 命令                                | 功能           |
| ----------------------------------- | -------------- |
| docker network ls                   | 查看网络       |
| docker network inspect  XXX网络名字 | 查看网络源数据 |
| docker network rm XXX网络名字       | 删除网络       |

## 3、网络模式

| 模式          | 介绍                                                         |
| ------------- | ------------------------------------------------------------ |
| bridge模式    | 使用--network  bridge指定，为每一个容器分配IP，默认使用docker0，默认为该模式 |
| host模式      | 使用--network host指定，容器不会虚拟出自己的IP端口，而是使用宿主机的 |
| none模式      | 使用--network none指定，容器有自己独立的netword namespace，但并没有对其进行任何设置 |
| container模式 | 使用--network container:NAME或者容器ID指定，容器不会新建自己的网卡和IP，而是和一个指定的容器共用 |

### 3.1、bridge

Docker 服务默认会创建一个 docker0 网桥（其上有一个 docker0 内部接口），该桥接网络的名称为docker0，它在内核层连通了其他的物理或虚拟网卡，这就将所有容器和本地主机都放到同一个物理网络。Docker 默认指定了 docker0 接口 的 IP 地址和子网掩码，让主机和容器之间可以通过网桥相互通信。

```bash
# 查看 bridge 网络的详细信息，并通过 grep 获取名称项
docker network inspect bridge | grep name
```

![image-20220306215934836](images/image-20220306215934836.png)

1. Docker使用Linux桥接，在宿主机虚拟一个**Docker容器网桥**(docker0)，Docker启动一个容器时会根据Docker网桥的**网段**分配给容器一个IP地址，称为**Container-IP**，同时Docker网桥是每个容器的**默认网关**。因为在同一宿主机内的容器都接入同一个网桥，这样容器之间就能够通过容器的Container-IP直接通信。
2. docker run 的时候，没有指定network的话默认使用的网桥模式就是bridge，使用的就是docker0。在宿主机ifconfig，就可以看到docker0和自己create的network(后面讲)eth0，eth1，eth2……代表网卡一，网卡二，网卡三……，lo代表127.0.0.1，即localhost，inet addr用来表示网卡的IP地址
3. 网桥docker0创建**一对**对等虚拟设备接口一个叫**veth**，另一个叫**eth0**，成对匹配。
   1. 整个宿主机的网桥模式都是docker0，类似一个交换机有一堆接口，每个接口叫veth，在本地主机和容器内分别创建一个虚拟接口，并让他们彼此联通（这样一对接口叫veth pair）
   2. 每个容器实例内部也有一块网卡，每个接口叫eth0
   3. docker0上面的每个veth匹配某个容器实例内部的eth0，两两配对，一一匹配。veth <-----> eth0

通过上述，将宿主机上的所有容器都连接到这个内部网络上，两个容器在同一个网络下,会从这个网关下各自拿到分配的ip，此时两个容器的网络是互通的。

![image-20220306222811236](images/image-20220306222811236.png)

### 3.2、host

直接使用宿主机的 IP 地址与外界进行通信，不再需要额外进行NAT 转换，好处是外部主机与容器可以直接通信。

容器将不会获得一个独立的Network Namespace， 而是和宿主机共用一个Network Namespace。

容器将不会虚拟出自己的网卡而是使用宿主机的IP和端口。

![image-20220306224416803](images/image-20220306224416803.png)

docker启动时指定--network=host或-net=host，如果还指定了-p映射端口，那这个时候就会有此警告，并且通过-p设置的参数将不会起到任何作用，端口号会以主机端口号为主，重复时则递增。

### 3.3、none

在none模式下，并不为Docker容器进行任何网络配置。 这个Docker容器没有网卡、IP、路由等信息，只有一个lo，需要我们自己为Docker容器添加网卡、配置IP等。

### 3.4、contain

新建的容器和已经存在的一个容器共享一个网络ip配置而不是和宿主机共享。新创建的容器不会创建自己的网卡，配置自己的IP，而是和一个指定的容器共享IP、端口范围等。同样，两个容器除了网络方面，其他的如文件系统、进程列表等还是隔离的。

![image-20220307092937221](images/image-20220307092937221.png)

### 3.5、自定义网络

在之前，容器内ping另一容器的ip地址是可通的，但是ping容器名是不通的

自定义桥接网络,自定义网络默认使用的是桥接网络bridge，首先需要先自定义网络

```bash
docker network creat xxxx
```

之后创建容器时，使用自定义网络模式，容器之间不论ip还是容器名都可以ping通

## 4、总结

从其架构和运行流程来看，Docker 是一个 **C/S** 模式的架构，后端是一个松耦合架构，众多模块各司其职。 

Docker 运行的基本流程为：

1. 用户是使用 Docker **Client** 与 Docker **Daemon** 建立通信，并发送请求给后者。
2. Docker Daemon 作为 Docker 架构中的主体部分，首先提供 Docker **Server** 的功能使其可以接受 Docker Client 的请求。
3. Docker **Engine** 执行 Docker 内部的一系列工作，每一项工作都是以一个 **Job** 的形式的存在。
4. Job 的运行过程中，当需要容器镜像时，则从 Docker Registry 中下载镜像，并通过镜像管理驱动 **Graph driver** 将下载镜像以Graph的形式存储。
5. 当需要为 Docker 创建网络环境时，通过网络管理驱动 **Network driver** 创建并配置 Docker 容器网络环境。
6. 当需要限制 Docker 容器运行资源或执行用户指令等操作时，则通过 **Exec driver** 来完成。
7. **Libcontainer**是一项独立的容器管理包，Network driver以及Exec driver都是通过Libcontainer来实现具体对容器进行的操作。

![image-20220307095410542](images/image-20220307095410542.png)

# DockerCompose容器编排

## 1、简介

Compose 是 Docker 公司推出的一个工具软件，可以管理多个 Docker 容器组成一个应用，解决了容器与容器之间如何管理编排的问题。

Compose允许用户通过定义一个单独的 YAML 格式的配置文件docker-compose.yml，来定义一组相关联的应用容器为一个项目(project)，写好多个容器之间的调用关系。然后，只要一个命令，就能同时启动/关闭这些容器，实现对Docker容器集群的快速编排

可以很容易地用一个配置文件定义一个多容器的应用，然后使用一条指令安装这个应用的所有依赖，完成构建。

### 1.1、核心概念

**三大件**：yml文件，服务（service），工程（project）

**服务**：一个个应用容器实例，比如订单微服务、mysql容器。

**工程**：由一组关联的应用容器组成的一个完整业务单元，在 docker-compose.yml 文件中定义。

不用Compose的问题：启动一个工程需要多个run命令启动多个容器，容器启停会导致ip变化，映射规则可能出现问题

### 1.2、基本属性

```yml
version : '3'       #Compose文件版本支持特定的Docker版本
services:           #本工程的服务配置列表
 
  swapping:         #spring boot的服务名，服务名自定义
    container_name: swapping-compose   
                    #本spring boot服务之后启动的容器实例的名字，如果指定，按照这个命名容器，如果未指定，容器命名规则是
                    #【[compose文件所在目录]_[服务名]_1】，例如【swappingdockercompose_swapping_1】
　　　　　　　　　　　　#如果多启动，也就是docker-compose scale swapping=3 mysql=2的话，就不需要指定容器名称，
　　　　　　　　　　　　#如果多启动的同时，指定了container_name，会报容器名重复存在的错误
    build:          #基于Dockerfile文件构建镜像时使用的属性
      context: .    #代表当前目录，也可以指定绝对路径[/path/test/Dockerfile]或相对路径[../test/Dockerfile]
      				#尽量放在当前目录，便于管理
      dockerfile: Dockerfile-swapping    #指定Dockerfile文件名。如果context指定了文件名，这里就不用本属性了
    ports:                      #映射端口属性
      - "9666:9666"             #建议使用字符串格式，指定宿主机端口映射到本容器的端口
    dns:  						# 配置 dns 服务器，可以是一个值或列表
      - 8.8.8.8
      - 9.9.9.9
    dns_search:					#配置 DNS 搜索域，可以是一个值或列表
      - dc1.example.com
      - dc2.example.com
    environment:				#环境变量配置，可以用数组或字典两种方式
      - RACK_ENV=development
      - SHOW=ture
    env_file:  					#从文件中获取环境变量，可以指定一个文件路径或路径列表
    							#其优先级低于 environment 指定的环境变量
      - ./common.env
    expose:						#暴露端口，只将端口暴露给连接的服务，而不暴露给主机
      - "3000"
      - "8000"
    volumes:                    #挂载属性
      - /xxxx/xxxx:/vol/development      #挂载路径在compose配置文件中只能指定容器内的目录
      									 #可以使用:ro对容器内目录设置只读，来保护宿主机的文件系统
    depends_on:        			#本服务启动，需要依赖哪些别的服务  例如这里；mysql服务就会先于swapping服务启动。
      - mysql
    links:                      #与depends_on相对应，上面控制启动顺序，这个控制容器连接问题。
      - "mysql:m"           	#值可以是- mysql[- 服务名]，也可以是- "mysql:m"[- "服务名:别名"]
      							#避免ip导致的容器重启动态改变的无法连接情况
      							#进入swapping-compose容器后，可以ping m通过
    external_links:				#和links一样，只不过是连接到外部容器，外部容器和此容器需要在同一个docker网络中
      - redis-external
    restart: always             #是否随docker服务启动重启
    networks:                   #加入指定网络
      - my-network              #自定义的网络名
    network_mode: "bridge"		#设置网络模式，设置这个就不需要networks
	#network_mode: "host"
	#network_mode: "none"
	#network_mode: "service:[service name]"
	#network_mode: "container:[container name/id]"
    environment:                #environment 和 Dockerfile 中的 ENV 指令一样会把变量一直保存在镜像、容器中
    							#类似 docker run -e 的效果。设置容器的环境变量
      - TZ=Asia/Shanghai        #这里设置容器的时区为亚洲上海，也就解决了容器通过compose编排启动的时区问题！！！！
      							#解决了容器的时区问题！！！
 
  mysql:                            #服务名叫mysql，自定义
    container_name: mysql-compose   #容器名
    image: mysql:5.7                #虽然没有使用build，但使用了image，指定基于mysql:5.7镜像为基础镜像来构建镜像。
    								#【使用build基于Dockerfile文件构建，Dockerfile文件中也有FROM基于基础镜像】
    ports:
      - "33061:3306"
    command: [                        #使用 command 可以覆盖容器启动后默认执行的命令
            '--character-set-server=utf8mb4',           #设置数据库表的数据集
            '--collation-server=utf8mb4_unicode_ci',    #设置数据库表的数据集
            '--default-time-zone=+8:00'                 #设置mysql数据库的 时区问题！！！！ 
            											#而不是设置容器的时区问题！！！！
    ]
    environment:           
      MYSQL_DATABASE: wolong                             #设置初始的数据库名
      MYSQL_ROOT_PASSWORD: fuckharkadmin                 #设置root连接密码
      MYSQL_ROOT_HOST: '%'
    restart: always
    networks:
      - my-network
networks:                        #关于compose中的networks的详细使用
  my-network:                    #自定义的网络，会在第一次构建时候创建自定义网络，默认是bridge
```

## 2、安装Compose

为全部用户安装docker-compose

~~~bash
# 提前创建目录
mkdir -p /usr/local/lib/docker/cli-plugins
~~~

```bash
curl -SL https://github.com/docker/compose/releases/download/v2.16.0/docker-compose-linux-x86_64 -o /usr/local/lib/docker/cli-plugins/docker-compose
```

~~~bash
curl -SL https://github.com/docker/compose/releases/download/v2.5.0/docker-compose-linux-aarch64 -o /usr/local/lib/docker/cli-plugins/docker-compose
~~~

赋予权限给可执行的二进制文件

```bash
chmod +x /usr/local/lib/docker/cli-plugins/docker-compose
```

测试

```bash
docker compose version
```

卸载直接删除改文件夹即可

## 3、使用步骤

编写Dockerfile定义各个微服务应用并构建出对应的镜像文件。

- 微服务通过容器服务名进行连接访问，例如 jdbc:mysql://**mysql**:3306

使用 docker-compose.yml 定义一个完整业务单元，安排好整体应用中的各个容器服务。

最后，执行docker-compose up -f yml文件命令 来启动并运行整个应用程序，完成一键部署上线。

## 4、常用命令

Compose常用命令


| 命令                 | 功能                                 |
| --------------------| ------------------------------------ |
|docker compose exec yml里面的服务id  | 进入容器实例内部，docker-compose exec docker-compose.yml文件中写的服务id /bin/bash |
|docker compose ps               | 展示当前docker-compose编排过的运行的所有容器 |
|docker compose top               | 展示当前docker-compose编排过的容器进程 |
|docker compose logs  yml里面的服务id | 查看容器输出日志 |
|docker compose config   | 检查配置 |
|docker compose config -q | 检查配置，有问题才有输出 |
|docker compose restart  | 重启服务 |
|docker compose start    | 启动服务 |
|docker compose stop     | 停止服务 |
| docker compose -h   | 查看帮助                             |
| docker compose -f yml文件 up | 启动所有docker-compose服务           |
| docker compose  -f yml文件 up -d | 启动所有docker-compose服务并后台运行 |
| docker compose down | 停止并删除容器、网络、卷、镜像。     |

## 5、例子

### 5.1、部署RedisCluster

```yml
version: "3"
 
services:
  redis01:
    image: redis:latest
    container_name: redis01
    volumes:
      - /data/redis/redis01/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis01/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6381
    privileged: true
  
  redis02:
    image: redis:latest
    container_name: redis02
    volumes:
      - /data/redis/redis02/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis02/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6382
    privileged: true
    
  redis03:
    image: redis:latest
    container_name: redis03
    volumes:
      - /data/redis/redis03/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis03/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6383
    privileged: true
    
  redis04:
    image: redis:latest
    container_name: redis04
    volumes:
      - /data/redis/redis04/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis04/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6384
    privileged: true
    
  redis05:
    image: redis:latest
    container_name: redis05
    volumes:
      - /data/redis/redis05/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis05/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6385
    privileged: true
    
  redis06:
    image: redis:latest
    container_name: redis06
    volumes:
      - /data/redis/redis06/conf/redis.conf:/etc/redis/redis.conf
      - /data/redis/redis06/data:/data
    network_mode: "host"
    command: redis-server /etc/redis/redis.conf --cluster-enabled yes --appendonly yes --port 6386
    privileged: true
```

执行 docker compose -f yml文件 up -d 命令后，进入容器中进行主从分配



# Docker可视化工具

## 1、简介

Portainer 是一款轻量级的应用，它提供了图形化界面，用于方便地管理Docker环境，包括单机环境和集群环境。

## 2、安装

```bash
docker volume create portainer_data

docker run -d -p 8000:8000 -p 9000:9000 -p 9443:9443 --name portainer \
    --restart=always \
    -v /var/run/docker.sock:/var/run/docker.sock \
    -v portainer_data:/data \
    portainer/portainer-ce:latest
```

安装完登陆9000端口，第一次的登陆需要设置密码

# Docker监控

## 1、简介

docker stats统计结果只能是当前宿主机的全部容器，数据资料是实时的，没有地方存储、没有健康指标过线预警等功能

引入监控三剑客cAdvisor+influxDB+Grafana

![image-20220307202644191](images/image-20220307202644191.png)

![image-20220307202725620](images/image-20220307202725620.png)

![image-20220307202731529](images/image-20220307202731529.png)

![image-20220307202736856](images/image-20220307202736856.png)

## 2、构建

使用docker-compose.yml一次性构建完毕

```yml
version: '3.1'
 
volumes:
  grafana_data: {}
 
services:
 influxdb:
  image: tutum/influxdb:0.9
  restart: always
  environment:
    - PRE_CREATE_DB=cadvisor
  ports:
    - "8083:8083"
    - "8086:8086"
  volumes:
    - ./data/influxdb:/data
 
 cadvisor:
  image: google/cadvisor
  links:
    - influxdb:influxsrv
  command: -storage_driver=influxdb -storage_driver_db=cadvisor -storage_driver_host=influxsrv:8086
  restart: always
  ports:
    - "8080:8080"
  volumes:
    - /:/rootfs:ro
    - /var/run:/var/run:rw
    - /sys:/sys:ro
    - /var/lib/docker/:/var/lib/docker:ro
 
 grafana:
  user: "104"
  image: grafana/grafana
  user: "104"
  restart: always
  links:
    - influxdb:influxsrv
  ports:
    - "3000:3000"
  volumes:
    - grafana_data:/var/lib/grafana
  environment:
    - HTTP_USER=admin
    - HTTP_PASS=admin
    - INFLUXDB_HOST=influxsrv
    - INFLUXDB_PORT=8086
    - INFLUXDB_NAME=cadvisor
    - INFLUXDB_USER=root
    - INFLUXDB_PASS=root
```

## 3、测试

浏览cAdvisor**收集**服务
http://ip:8080/

浏览inFluxdb**存储**服务
http://ip:8083/

浏览Grafana**展现**服务
http://ip:3000

Grafanad的默认账号密码为admin/admin，第一次登陆需要修改密码

对Grafanad进行配置

1. 配置数据源，选择inFluxdb

   <img src="images/image-20220307203642402.png" alt="image-20220307203642402" style="zoom:50%;" /><img src="images/image-20220307203703782.png" alt="image-20220307203703782" style="zoom:50%;" />

   - 配置细节

   <img src="images/image-20220307203839491.png" alt="image-20220307203839491" style="zoom: 50%;" /><img src="images/image-20220307204059054.png" alt="image-20220307204059054" style="zoom: 50%;" />

2. 配置面板：从左至右，从上至下

<img src="images/image-20220307204335478.png" alt="image-20220307204335478" style="zoom:50%;" /><img src="images/image-20220307204353982.png" alt="image-20220307204353982" style="zoom:50%;" />

<img src="images/image-20220307204441861.png" alt="image-20220307204441861" style="zoom:50%;" /><img src="images/image-20220307204629677.png" alt="image-20220307204629677" style="zoom:50%;" />

<img src="images/image-20220307204757685.png" alt="image-20220307204757685" style="zoom:50%;" />

# 扩展

## 1、容器与虚拟机比较

**虚拟机**：

- 虚拟出一套完整的硬件，在其上运行一个完整的操作系统，在该系统上再运行所需应用进程


**缺点**：资源占用大，冗余多，启动慢

**优点**：安全性高

<img src="images/vm.jpeg" alt="vm" style="zoom:50%;" />

**容器**：

- Linux 发展出了另一种虚拟化技术：Linux 容器(Linux Containers，缩写为 LXC)

  - LXC 不是模拟一个完整的操作系统而是**对进程进行资源隔离**
  - 从一个镜像运行，由该镜像提供支持进程所需的全部文件，因此可以将软件运行所需的所有资源（例如依赖项）打包到一个隔离的容器中，从开发到测试再到生产的整个过程中，具备可移植性和一致性
- 在 Docker 的最初版本确实是支持 LXC 技术，但是在后来被其自研的 libcontainerd 替换掉，libcontainerd 直接与 Linux Kernel 提供的功能交互
  - 核心技术就是：Namespace（资源隔离）和Cgroup（资源管理），均由 Linux Kernel 提供

<img src="images/f69cc6b9582ef25f917c845334458f23.png" alt="f69cc6b9582ef25f917c845334458f23" style="zoom:50%;" />

**优点**：

- Docker 有着比虚拟机更少的抽象层：Docker 不需要 Hypervisor (虚拟机)实现硬件资源虚拟化，运行在 Docker 容器上的程序直接使用的都是实际物理机的硬件资源，因此在 CPU、内存利用率上 Docker 将会在效率上有明显优势

- Docker 利用的是宿主机的内核，而不需要加载操作系统OS内核：当新建一个容器时，Docker 不需要和虚拟机一样重新加载一个操作系统内核，避免了引寻、加载操作系统内核返回等比较费时费资源的过程，因此新建一个容器只需要几秒，而当新建一个虚拟机时，虚拟机软件需要加载 OS，返回新建过程是分钟级别的
- 容器不需要捆绑一整套操作系统，只需要软件工作所需的库资源和系统设置，系统因此而变得高效轻量并保证部署在任何环境中的软件都能始终如一地运行

**缺点**：

- 不安全

<img src="images/vm-16431867861881.jpeg" alt="vm" style="zoom:50%;" />

**相同点**：

- 虚拟机和容器都是宿主机上面的一个进程
- 虚拟机和容器都具备资源隔离、安全隔离、系统资源分配的功能

**不同点**：

- 虚拟机实现了 VM 系统之间强隔离，而容器只提供了进程之间的隔离
- 虚拟机是硬件级别的虚拟化，容器是操作系统级别的虚拟化

<img src="images/image-20220126170919455.png" alt="image-20220126170919455" style="zoom:60%;" />

<img src="images/vm-16431881956682.jpeg" alt="vm" style="zoom:67%;" />



## 2、Docker 虚悬镜像

仓库名、标签都是\<none>的镜像，俗称虚悬镜像 dangling image



##  3、前台模式与后台模式

后台模式：

首先：Docker 容器后台运行，就必须有一个前台进程。

如果在 docker run 后面追加 -d=true 或者 -d，那么容器将会运行在后台模式。此时所有 I/O 数据只能通过网络资源或者共享卷组来进行交互。因为容器不再监听你执行 docker run 的这个终端命令行窗口。但你可以通过执行 docker attach 来重新附着到该容器的会话中。需要注意的是，容器运行在后台模式下，是不能使用--rm选项的。

容器运行的命令如果不是那些一直挂起的命令（比如运行 top，tail），就是会自动退出的。例如  docker run -d centos 运行一个容器，再使用 docker ps -a 查看，发现容器已经下线，因为容器没有需要执行的前台进程，就直接下线。因此最佳的解决方案是，将你要运行的程序以前台进程的形式运行，常见就是命令行模式，表示我还有交互操作，别中断。

前台模式：

在前台模式下（不指定 -d 参数即可），Docker 会在容器中启动进程，同时将当前的命令行窗口附着到容器的标准输入、标准输出和标准错误中。也就是说容器中所有的输出都可以在当前窗口中看到。甚至它都可以虚拟出一个 TTY 窗口，来执行信号中断。这一切都是可以配置的：

>-a, --attach value                Attach to STDIN, STDOUT or STDERR (default [])
>
>-t, --tty                         Allocate a pseudo-TTY
>
>--sig-proxy                   Proxy received signals to the process (default true)
>
>-i, --interactive                 Keep STDIN open even if not attached

例如 docker run -it --name=interactive centos 前台启动一个 centos，启动后会直接进入到其内部，但是退出后容器下线。

如果在执行 run 命令时没有指定 -a 参数，那么 Docker 默认会挂载所有标准数据流，包括输入输出和错误，你可以单独指定挂载哪个标准流。

```shell
docker run -a stdin -a stdout -i -t ubuntu /bin/bash
```



## 4、Containerd 的发展

**历史**：

- 原本 Containerd 只是 Docker 运行的一个伴随应用，作为连接 Docker 和底层 runc 的中间件
- 后来 Containerd 从 Docker 项目中分离开源
- Containerd 设计了全新的容器与镜像管理接口
- CRI 作为独立于容器进程的插件被集成到了 Containerd，使 Containerd 成为符合 CRI 标准的容器运行时，也因此具备通用化能力，被 k8s 等项目使用
- Containerd  逐渐从一个容器的 supervisor（基本的容器检测与执行）发展为具有完整功能的 container runtime（全流程），也即 Containerd 彻底包含了 cri-plugin 与 runc



**注意**：

- Containerd 被设计为可以嵌入到其他系统中，因此并不由开发人员直接调用



<img src="images/image-20221230222932822.png" alt="image-20221230222932822" style="zoom:80%;" />

<p style="text-align:center">Docker 抽象图（旧）</p>

![190cfe8d4cd148a590dcde175e9fcdc2](images/190cfe8d4cd148a590dcde175e9fcdc2.png)

<p style="text-align:center">Containerd 架构图</p>



## 5、推送镜像到 Harbor

首先登陆 Harbor 创建项目

~~~bash
# 添加地址
vim /etc/docker/daemon.json

{
   "insecure-registries": ["http://211.131.241.221:8888"]
}
~~~

~~~bash
systemctl daemon-reload
systemctl restart docker

docker login xxx.xxx.xxx.xxx:xx
# 输入账号密码

# 给想要推送的镜像打标签
# docker tag 镜像名:TAG harbor地址/项目/镜像名:TAG
docker tag gos:v1.0 192.168.100.142:80/gostest/gos:v1.0

# 推送镜像
docker push 192.168.100.142:80/gostest/gos:v1.0
~~~







# 问题

## 1、端口映射出错

```shell
docker: Error response from daemon: driver failed programming external connectivity on endpoint mariadb (f21d032144a551af5486af37becd123b815d496ec64501aa77f639e2ca496ce6):  (iptables failed: iptables --wait -t nat -A DOCKER -p tcp -d 0/0 --dport 3306 -j DNAT --to-destination 172.17.0.2:3306 ! -i docker0: iptables: No chain/target/match by that name.
```

重启docker

systemctl restart docker

## 2、容器数据卷访问目录出错

Docker挂载主机目录访问如果出现**cannot open directory .: Permission denied**。

解决办法：在挂载目录后多加一个**--privileged=true**参数即可。

如果是CentOS7安全模块会比之前系统版本加强，不安全的会先禁止，而**目录挂载的情况被默认为不安全的行为**，所以在SELinux里面挂载目录被禁止掉了，如果要开启，我们一般使用--privileged=true命令，扩大容器的权限解决挂载目录没有权限的问题，也即使用该参数，container内的root拥有真正的root权限，否则，container内的root只是外部的一个普通用户权限。

## 1、MySQL无法连接

**问题描述：**

[08S01] Communications link failure

The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.


直译：错误代码[08S01] 连接失败

上次成功发送到服务器的数据包是0毫秒以前。驱动程序还没有收到来自服务器的任何数据包。

linux端可以登陆mysql，但是DataGrip无法连接，设置了root@%，但还是不行。

**问题解决：**

show global variables like 'port';

查看端口多少，发现为0

在进入etc/my.cnf修改

将\#skip-grant-tables 注释掉后端口号就不为零 了

\#skip-networking 也要注释掉

不放心顺手加上port=3306

本地去连接mysql时报错，错误 2058 Plugin caching_sha2_password could not be loaded

由于mysql8.0的加密方法变了。mysql8.0默认采用caching_sha2_password的加密方式。sqlyog不支持这种加密方式。

```
1、修改密码过期
ALTER USER'root'@'localhost' IDENTIFIED BY 'root' PASSWORD EXPIRE NEVER;
2、重新修改密码
ALTER USER'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';
3、刷新权限（不做可能无法生效）
FLUSH PRIVILEGES;
```

## 2、MySQL文件目录挂载出问题

**问题描述**：

使用命令如下，使用mysql:lastest版本

```shell
docker run -p 3306:3306 --name mysql01 \
-v /data/mysql/mysql01/log:/var/log/mysql \
-v /data/mysql/mysql01/data:/var/lib/mysql \
-v /data/mysql/mysql01/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=fuckharkadmin \
-itd mysql
```

![image-20220110164643005](images/image-20220110164643005.png)

**问题解决**：

当指定了外部配置文件与外部存储路径时，也需要指定 /var/lib/mysql-files的外部目录

所以添加一条挂载

```shell
-v /data/mysql/mysql01/mysql-files:/var/lib/mysql-files \
```

## 3、MySQL挂载四个目录后，无法登陆

**问题描述**：

在解决问题二的挂载问题后，启动登陆mysql报以下错误（有可能密码输入错误）

```shell
”Access denied for user ‘root’@’localhost’ (using password: YES)”
```

**问题解决**：

在映射的conf文件夹下配置mysql.cnf

```shell
[mysqld]
skip-grant-tables #跳过密码登陆
```

进入mysql后配置root密码以及**域**，或者新增一个用户即可

**ps**：有些机子不会出现这个问题

## 4、MySQL默认字符集隐患

**问题描述**：

![image-20220224213220476](images/image-20220224213220476.png)

**问题解决**：

创建一个my.cnf

```text
[client]
default_character_set=utf8
[mysqld]
collation_server = utf8_general_ci
character_set_server = utf8
```

进行映射后放入conf中

docker安装完MySQL并run容器后，建议先修改完字符集编码后重启容器，再新建mysql库-表-插数据

## 5、MySQL主从复制Connecting

**问题描述**：

如果一直显示这样

![image-20220225170526508](images/image-20220225170526508.png)

**问题解决**：

可以从几个方面考虑，偏移量错误、文件名错误、slave账号权限不足、网络不通

对于偏移量与文件名可以登陆slave账号使用show master status\G 查看

建议主库登陆root与slave账号都查看一下，我最后是以slave账号查看的为准

![image-20220225173557581](images/image-20220225173557581.png)

slave权限可以增加

```mysql
GRANT ALL PRIVILEGES ON *.* TO 'slave'@'%';
flush privileges;
```

## 6、Docker容器无法使用systemctl

rhel系可以尝试，在启动容器时，最后加上/usr/sbin/init

服务操作命令：

- 6的版本：service，对应就是 /usr/sbin/init 1号进程管理
- 7的版本：systemctl ，对应就是 /usr/lib/systemd/system 1号进程管理

![image-20220502004625935](images/image-20220502004625935.png)



