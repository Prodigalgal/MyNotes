> 2025.02.11.ver.v2

# 1、Nginx简介

高性能的 HTTP 和反向代理服务器，占有内存少，并发能力强

Nginx 可以作为静态页面的 web 服务器，同时还支持 CGI 协议的动态语言



# 2、Nginx基本用途

## 1、代理概念

### 1、正向代理

局域网中的客户端要访问局域网外的Internet，需要通过代理服务器来访问，代理服务器位于客户服务器与目标服务器之间，客户服务器发送一个请求给代理服务器，其中指定了目标服务器的地址，之后代理服务器转发请求，并将结果返回给客户服务器，这种代理服务就称为正向代理

Nginx 可以做反向代理、实现负载均衡、还能用作正向代理来进行上网等功能

**注意**：

- 需要在客户端配置代理服务器的地址并进行指定网站访问
- 使用正向代理，客户服务器对目标服务器不可见

![image-20220109211300953](images/image-20220109211300953.png) 



### 2、反向代理

客户端对反向代理是无感知的，因为客户端不需要任何配置就可以访问，只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，在返回给客户端

此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器地址，隐藏了真实服务器 IP 地址

![image-20220109211359082](images/image-20220109211359082.png) 



## 2、负载均衡

将原先请求集中到单个服务器上的情况改为将请求分发到多个服务器上，将负载分发到不同的服务器，也就是我们所说的负载均衡

![image-20220109211542076](images/image-20220109211542076.png) 



## 3、动静分离

为了加快网站的解析速度，可以把动态页面和静态页面由不同的服务器来解析，加快解析速度。降低原来单个服务器的压力。

![image-20220109211616914](images/image-20220109211616914.png) 



# 3、Nginx安装与目录

## 1、安装

一键安装依赖

```shell
yum -y install make zlib zlib-devel gcc-c++ libtool openssl openssl-devel keepalived
```

下载Nginx，然后解压缩，进入根目录，执行 `./configure` 

再执行`make && make install` 

安装后的默认目录在 `/usr/local/nginx` 

查看开放的端口号`firewall-cmd --list -all` 

设置开放的端口号

`firewall-cmd --add -service =  http -permanent` 

`firewall-cmd --add -port = 80/tcp --permanent` 

重启防火墙`firewall-cmd -reload` 

## 2、目录结构

主要的文件夹

~~~txt
sbin：nginx的主程序
conf：用来存放配置文件相关
html：用来存放静态文件的默认目录 html、css等

logs 
uwsgi_temp

client_body_temp 
fastcgi_temp 
proxy_temp 
scgi_temp 
~~~

其中这几个文件夹在刚安装后是没有的，主要用来存放运行过程中的临时文件

~~~txt
client_body_temp 
fastcgi_temp 
proxy_temp 
scgi_temp
~~~

# 4、Nginx常用命令

## 1、启动命令

可以将Nginx命令软连接到 /usr/local/nginx/sbin 目录下的nginx

```shell
ln -s /usr/local/nginx/sbin/nginx /usr/bin/nginx
```

执行nginx即可启动

否则需要进入到该目录下执行 `./nginx`

## 2、关闭命令

```shell
./nginx -s stop
```

## 3、重载命令

```shell
./nginx -s reload
```

## 4、检查配置文件

```shell
./nginx -t
```

## 5、检查运行

通过检查Nginx程序的监听状态，或者在浏览器中访问此Web服务，默认页面将显示“Welcome to nginx!”

```shell
netstat -antp | grep nginx
# 结果
tcp        0      0 0.0.0.0:80              0.0.0.0:*               LISTEN      54386/nginx: master 
```



# 5、Nginx配置文件

## 1、概述

Nginx 配置文件通常位于 /etc/nginx/nginx.conf

分为以下层级：

- 全局块：配置全局参数（如工作进程数、用户权限等）

- events 块：配置网络连接相关参数

- http 块：定义 HTTP 服务器行为，包含多个 server 块

- server 块：定义虚拟主机（域名/IP）

- location 块：匹配 URI，定义请求处理规则

server 块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的

每个 http 块可以包括多个 server 块，而每个 server 块就相当于一个虚拟主机

而每个 server 块也分为全局 server 块，以及可以同时包含的多个 locaton 块

```nginx
worker_processes  1; 默认为1，表示开启一个业务进程

events {
    worker_connections  1024; 单个业务进程可接受连接数
}

http {
    include       mime.types; 引入http mime类型
    default_type  application/octet-stream; 如果mime类型没匹配上，默认使用二进制流的方式传输。

    sendfile        on; 使用linux的 sendfile(socket, file, len) 高效网络传输，也就是数据0拷贝。

    server {
        listen       80; 监听端口号
        server_name  localhost; 主机名

        location / { 匹配路径
            root   html;  文件根目录
            index  index.html index.htm;  默认页名称
        }

        error_page   500 502 503 504  /50x.html; 报错编码对应页面
        location = /50x.html {
            root   html;
        }

    }

}
```



## 2、全局块

### 1. user

**作用**：设置 Nginx 工作进程的用户和用户组

**语法**：user <user> [group];

**默认值**：nobody（在不同系统上可能不同

```nginx
user nginx nginx;
# 这表示 Nginx 工作进程将以 nginx 用户和 nginx 组的身份运行
```



### 2. worker_processes

**作用**：设置 Nginx 启动的工作进程数量，一般来说将其设置为与 CPU 核心数相同，或者根据系统负载来调整

**语法**：worker_processes <number>;

**默认值**：1

```nginx
# 表示 Nginx 启动 4 个工作进程
worker_processes 4;
```



### 3. worker_cpu_affinity

**作用**：设置工作进程绑定的 CPU 核心，以便实现负载均衡和性能优化

**语法**：worker_cpu_affinity <mask>;

**默认值**：无

```nginx
# 将工作进程绑定到指定的 CPU 核心上，具体的掩码可以根据硬件和需求来设置
worker_cpu_affinity 0101 1010;
```



### 4. error_log

**作用**：设置错误日志的路径和日志级别。

**语法**：`error_log <file> [level];`

**默认值**：`/var/log/nginx/error.log`，日志级别为 `error`

```nginx
# 表示设置错误日志文件的路径，并指定日志级别为 warn
error_log /var/log/nginx/error.log warn;
```



### 5. pid

**作用**：设置存储 Nginx 进程 ID 的文件路径。

**语法**：`pid <file>;`

**默认值**：/var/run/nginx.pid

```nginx
# 表示指定 Nginx 进程 ID 存储在 /var/run/nginx.pid  文件中
pid /var/run/nginx.pid;
```



### 6. worker_connections

**作用**：设置每个工作进程可以同时打开的最大连接数。这个值会影响 Nginx 能处理的并发请求数。

**语法**：`worker_connections <number>;`

**默认值**：`1024`

```nginx
# 表示每个工作进程最多可以同时处理 2048 个连接
worker_connections 2048;
```



### 7. multi_accept

**作用**：配置每个工作进程是否接受多个连接。开启后，工作进程将尽可能接受尽量多的连接。

**语法**：`multi_accept on | off;`

**默认值**：`off`

```nginx
# 表示开启多个连接的接受
multi_accept on;
```



### 8. worker_rlimit_nofile

**作用**：设置工作进程的最大文件描述符数量，如果需要处理大量并发连接时，可能需要调整此参数

**语法**：`worker_rlimit_nofile <number>;`

**默认值**：无

```nginx
# 表示每个工作进程最多可以打开 65535 个文件
worker_rlimit_nofile 65535;
```



### 9. worker_processes_max

**作用**：设置最大工作进程数目

**语法**：`worker_processes_max <number>;`

**默认值**：无

```nginx
# 表示最多 16 个工作进程
worker_processes_max 16;
```



### 10. log_format

**作用**：设置日志格式。该指令用于定义 Nginx 访问日志的格式。

**语法**：`log_format <name> <format>;`

**默认值**：`main`（默认格式）

```nginx
log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for"';

# 表示定义了一个名为 main 的日志格式，包含了客户端 IP、请求方式、返回状态等信息
```



### 11. access_log

**作用**：设置访问日志的路径和格式

**语法**：`access_log <file> [format];`

**默认值**：`/var/log/nginx/access.log`，格式为 `main`

```nginx
# 表示设置访问日志的路径，并使用 main 格式记录日志
access_log /var/log/nginx/access.log main;
```



### 12. **sendfile**

**作用**：启用或禁用高效文件传输模式，在开启时，Nginx 会通过 `sendfile()` 系统调用传输文件，这对大文件的传输性能有较大提升

**语法**：`sendfile on | off;`

**默认值**：`off`

```nginx
# 表示启用高效的文件传输模式
sendfile on;
```



### 13. **tcp_nopush**

**作用**：启用或禁用 TCP_NOPUSH 选项，优化传输效率

**语法**：`tcp_nopush on | off;`

**默认值**：`off`

```nginx
# 表示启用 TCP_NOPUSH
tcp_nopush on;
```



### 14. **tcp_nodelay**

**作用**：启用或禁用 TCP_NODELAY 选项，用于优化延迟。

**语法**：`tcp_nodelay on | off;`

**默认值**：`off`

```nginx
# 表示启用 TCP_NODELAY
tcp_nodelay on;
```



### 15. **open_file_cache**

**作用**：启用文件缓存，减少文件系统调用

**语法**：`open_file_cache <max> [inactive=10m] [min_uses=1] [valid=1m];`

**默认值**：无

```nginx
# 表示开启文件缓存，最多缓存 1000 个文件，文件在 20 秒内未使用则会从缓存中删除
open_file_cache max=1000 inactive=20s;
```



## 3、events 块

### 1. **worker_connections**

**作用**：设置每个工作进程可以同时处理的最大连接数

**语法**：`worker_connections <number>;`

**默认值**：`1024`

```nginx
# 表示每个工作进程最多可以同时处理 2048 个连接。这个值直接影响 Nginx 处理并发请求的能力
worker_connections 2048;
```



### 2. **use**

**作用**：设置事件驱动模型，指定 Nginx 使用哪个机制来处理连接。不同的操作系统支持不同的事件模型。

**语法**：`use <event-model>;`

**默认值**：`select`（在某些平台上可能是 `epoll` 或其他）

- 支持的值

  - `select`：基本的事件模型，适用于老旧操作系统
  - `poll`：更高效的事件模型，比 `select` 更好，但不如 `epoll` 高效
  - `epoll`：最佳的事件驱动模型，专为 Linux 系统优化，适合高并发场景
  - `kqueue`：针对 BSD 系统优化的事件模型
  - `rtsig`：用于实时信号的事件模型，通常在高性能实时系统中使用

  ```nginx
  use epoll;
  ```



### 3. **worker_aio_requests**

**作用**：设置每个工作进程允许的最大异步 I/O 请求数量。用于启用 `aio`（异步 I/O）模式时，控制可以同时处理的异步请求数量。

**语法**：`worker_aio_requests <number>;`

**默认值**：无

```nginx
# 表示每个工作进程最多可以处理 65535 个异步 I/O 请求
worker_aio_requests 65535;
```



### 4. **multi_accept**

**作用**：设置工作进程是否可以在一个循环中接受多个连接。启用后，工作进程会尽可能多地接受连接。

**语法**：`multi_accept on | off;`

**默认值**：`off`

```nginx
# 表示每个工作进程在一个循环中会接受尽可能多的连接，而不是一个接一个地接受
multi_accept on;
```



### 5. **accept_mutex**

**作用**：启用或禁用接收连接时的互斥锁。它用于确保多个工作进程不会同时接受连接，以避免竞争条件。

**语法**：`accept_mutex on | off;`

**默认值**：`on`

```nginx
# 表示禁用互斥锁，允许多个工作进程同时接受连接，通常用于多核系统以提高性能
accept_mutex off;
```



### 6. **accept_mutex_delay**

**作用**：设置工作进程在尝试获取连接时等待的延迟时间。这个设置与 `accept_mutex` 配合使用，指定等待时间的上限。

**语法**：`accept_mutex_delay <time>;`

**默认值**：`500ms`

```nginx
# 表示工作进程尝试获取连接时会等待最多 100 毫秒的时间，超过此时间就放弃等待
accept_mutex_delay 100ms;
```



### 7. **disable_accept_events**

**作用**：启用此选项将禁止使用某些特定的接收事件，通常用于调试或特定的性能需求。

**语法**：`disable_accept_events on | off;`

**默认值**：`off`



```nginx
# 表示禁用接收事件，一般只在某些特定的调试或优化场景中使用
disable_accept_events on;
```



### 8、完整示例

```nginx
events {
    worker_connections 2048;       # 设置每个工作进程的最大连接数
    use epoll;                     # 使用 epoll 事件模型（适用于 Linux 系统）
    multi_accept on;               # 启用每次循环接受多个连接
    accept_mutex on;               # 启用互斥锁，避免多个工作进程同时接受连接
    accept_mutex_delay 100ms;      # 设置工作进程获取连接时的最大等待时间
}
```



## 4、http 全局块

```text
http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;

    server {
        listen       80;
        server_name  localhost;

        location / {
            root   html;
            index  index.html index.htm;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

    }

}
```



### 1. **include**

**作用**：引入外部配置文件或目录中的文件

**语法**：`include <file | directory>;`

**默认值**：无

```nginx
include mime.types;
include /etc/nginx/conf.d/*.conf; 
# 这表示将 mime.types 文件和 /etc/nginx/conf.d/ 目录下所有的 .conf 文件引入到当前的配置中
```



### 2. **log_format**

**作用**：定义日志的格式，用于访问日志中每条请求的记录格式。

**语法**：`log_format <name> <format>;`

**默认值**：`main`（定义了默认的日志格式）

```nginx
log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                  '$status $body_bytes_sent "$http_referer" '
                  '"$http_user_agent" "$http_x_forwarded_for"';
# 这表示定义一个名为 main 的日志格式，记录了客户端的 IP 地址、请求方式、状态码等信息
```



### 3. **access_log**

**作用**：设置访问日志的路径及格式。

**语法**：`access_log <file> [format];`

**默认值**：`/var/log/nginx/access.log`，格式为 `main`

```nginx
# 表示设置访问日志文件的路径，并使用之前定义的 main 格式
access_log /var/log/nginx/access.log main;
```



### 4. **error_log**

**作用**：设置错误日志的路径及日志级别。

**语法**：`error_log <file> [level];`

**默认值**：`/var/log/nginx/error.log`，日志级别为 `error`

```nginx
# 表示设置错误日志文件路径，并使用 warn 级别记录错误信息
error_log /var/log/nginx/error.log warn;
```



### 5. **sendfile**

**作用**：启用或禁用高效文件传输模式。在启用时，Nginx 使用 `sendfile()` 系统调用进行文件传输。

**语法**：`sendfile on | off;`

**默认值**：`off`

```nginx
# 表示启用高效的文件传输
sendfile on;
```



### 6. **tcp_nopush**

- **作用**：启用或禁用 TCP_NOPUSH 选项，用于优化传输效率。它确保数据块的完整性，减少 TCP 包的数量。
- **语法**：`tcp_nopush on | off;`
- **默认值**：`off`

~~~nginx
# 表示启用 TCP_NOPUSH，以优化数据包的传输
tcp_nopush on;
~~~



### 7. **tcp_nodelay**

**作用**：启用或禁用 TCP_NODELAY 选项，用于控制延迟。禁用该选项时，数据包会尽可能立即发送。

**语法**：`tcp_nodelay on | off;`

**默认值**：`off`

```nginx
# 表示启用 TCP_NODELAY，以减少延迟
tcp_nodelay on;
```



### 8. **keepalive_timeout**

**作用**：设置连接的保持时间。在客户端和服务器之间保持连接，直到达到指定的时间。

**语法**：`keepalive_timeout <time>;`

**默认值**：`75s`

```nginx
# 表示将连接保持的最大超时时间设置为 65 秒
keepalive_timeout 65s;
```



### 9. **types**

**作用**：设置 MIME 类型（媒体类型）映射，用于响应 HTTP 请求时指定文件类型

**语法**：`types { <extension> <type>; ... }`

**默认值**：`types` 文件通常定义在 `mime.types` 文件中。

```nginx
# 表示为常见的文件扩展名设置 MIME 类型
types {
    text/html html htm;
    text/css css;
    application/javascript js;
    image/jpeg jpg jpeg;
    image/png png;
}
```



### 10. **client_max_body_size**

**作用**：设置客户端请求体的最大大小。用于限制请求内容的大小，例如上传文件的大小。

**语法**：`client_max_body_size <size>;`

**默认值**：`1m`

```nginx
# 表示将客户端请求体的最大大小设置为 10 MB
client_max_body_size 10m;
```



### 11. **server_tokens**

**作用**：控制是否在响应头中公开 Nginx 的版本号。

**语法**：`server_tokens on | off;`

**默认值**：`on`

```nginx
# 表示禁用在响应头中显示 Nginx 版本号，增强安全性
server_tokens off;
```



### 12. **gzip**

**作用**：启用或禁用对响应内容的压缩。启用后，Nginx 将使用 Gzip 压缩响应数据，以减小传输大小。

**语法**：`gzip on | off;`

**默认值**：`off`

```nginx
gzip on;
gzip_comp_level 5;
gzip_types text/plain application/javascript text/css application/json;

# 表示启用 Gzip 压缩，并为某些文件类型（如 text/plain text/css）设置压缩
```



### 13. **http2**

**作用**：启用 HTTP/2 协议。HTTP/2 提供了更高效的请求和响应处理方式，通常用于优化性能。

**语法**：`http2 on | off;`

**默认值**：`off`

```nginx
# 表示启用 HTTP/2 协议
listen 443 ssl http2;
```



### 14. **server_names_hash_bucket_size**

**作用**：设置 Nginx 内部哈希表的桶大小，用于存储虚拟主机名。适用于有很多不同主机名的虚拟主机配置。

**语法**：`server_names_hash_bucket_size <size>;`

**默认值**：`64`

```nginx
# 表示将哈希桶的大小设置为 128 字节
server_names_hash_bucket_size 128;
```



### 15. **resolver**

**作用**：配置 DNS 解析服务器，供 Nginx 用于解析域名。

**语法**：`resolver <ip-address> [valid=<time>] [ipv6=on];`

**默认值**：无

```nginx
# 表示使用 Google 的公共 DNS 服务器进行域名解析
resolver 8.8.8.8 8.8.4.4;
```



### 16、default_type

**作用**：`default_type` 指定了当 Nginx 无法根据文件扩展名确定文件类型时，默认使用的 MIME 类型，当服务器返回文件时，如果没有正确的 MIME 类型，客户端（浏览器）可能无法正确处理文件

**语法**：default_type <mime-type>;

**默认值**：application/octet-stream

~~~nginx
default_type application/octet-stream;
~~~





### 17、完整示例

```nginx
http {
    include       mime.types;      # 引入 mime.types 文件，设置文件类型
    default_type  application/octet-stream;   # 默认类型为二进制流
    log_format main '$remote_addr - $remote_user [$time_local] "$request" '
                     '$status $body_bytes_sent "$http_referer" '
                     '"$http_user_agent" "$http_x_forwarded_for"';  # 设置日志格式
    access_log /var/log/nginx/access.log main;   # 设置访问日志路径和格式
    error_log /var/log/nginx/error.log warn;     # 设置错误日志路径和级别

    sendfile on;                  # 启用高效文件传输
    tcp_nopush on;                # 启用 TCP_NOPUSH 优化
    tcp_nodelay on;               # 启用 TCP_NODELAY
    keepalive_timeout 65s;        # 设置连接保持时间
    client_max_body_size 10m;     # 限制最大请求体为 10MB
    gzip on;                      # 启用 Gzip 压缩
    gzip_comp_level 5;            # 设置 Gzip 压缩级别
    gzip_types text/plain application/javascript text/css application/json;  # 设置 Gzip 压缩类型

    server_names_hash_bucket_size 128;  # 设置虚拟主机名哈希桶大小

    resolver 8.8.8.8 8.8.4.4;  # 设置 DNS 解析服务器
}
```



## 5、server 全局块

### 1. **listen**

**作用**：定义 Nginx 监听的 IP 地址和端口

**语法**：`listen <address>:<port> [options];`

**默认值**：`listen 80;`（对于 HTTP 默认端口）

```nginx
listen 80;  # 监听 HTTP 的标准端口
listen 443 ssl;  # 监听 HTTPS 端口并启用 SSL
listen 127.0.0.1:8080;  # 监听指定 IP 和端口
```



### 2. **server_name**

**作用**：定义虚拟主机的域名，Nginx 根据请求的域名来匹配不同的 `server` 块，匹配分先后顺序，先匹配上就不会继续往下匹配

**语法**：`server_name <domain> [subdomains] ...;`

**默认值**：`localhost`（如果没有指定，则匹配 `localhost`）

~~~nginx
server_name vod.mmban.com www1.mmban.com; # 同一 servername 中匹配多个域名
server_name *.mmban.com # 通配符起始匹配
server_name vod.*; # 通配符结束匹配
server_name ~^[0-9]+\.mmban\.com$; # 正则匹配
~~~



### 3. **root**

**作用**：设置网站的根目录，也就是静态文件的位置

**语法**：`root <path>;`

**默认值**：无

```nginx
root /var/www/html;  # 网站的根目录
```



### 4. **index**

**作用**：设置默认的首页文件，客户端访问根目录时会默认请求该文件。

**语法**：`index <file> [file] ...;`

**默认值**：`index.html index.htm`

```nginx
index index.html index.htm;  # 默认首页文件
```



### 5. **error_page**

**作用**：自定义错误页面，用于当请求发生特定错误时，返回定制的页面

**语法**：`error_page <code> [code] ... <uri>;`

**默认值**：无

```nginx
error_page 404 /404.html;  # 设置 404 错误时的自定义页面
error_page 500 502 503 504 /50x.html;  # 设置服务器错误时的自定义页面
```



### 6. **return**

**作用**：设置服务器响应的返回值和 URL 重定向。

**语法**：`return <status_code> [<uri>];`

**默认值**：无

```nginx
return 301 http://www.example.com$request_uri;  # 重定向到 www 版本的 URL
return 404;  # 返回 404 错误
```



### 7. **rewrite**

**作用**：进行 URL 重写操作，可以通过正则表达式修改请求的 URL。

**语法**：`rewrite <regex> <replacement> [flag];`

**默认值**：无

```nginx
rewrite ^/oldpath/(.*)$ /newpath/$1 permanent;  # 将 /oldpath/ 重定向到 /newpath/
```



### 8. **ssl_certificate** 和 **ssl_certificate_key**

**作用**：配置 SSL 证书和私钥文件，用于启用 HTTPS

**语法**：

```nginx
ssl_certificate <path-to-cert>;
ssl_certificate_key <path-to-key>;
```

**默认值**：无

```nginx
ssl_certificate /etc/nginx/ssl/example.com.crt;  # SSL 证书
ssl_certificate_key /etc/nginx/ssl/example.com.key;  # SSL 私钥
```



### 9. **ssl_protocols** 和 **ssl_ciphers**

- **作用**：配置支持的 SSL/TLS 协议和加密算法

- **语法**：

  ```nginx
  ssl_protocols <protocols>;
  ssl_ciphers <ciphers>;
  ```

- **默认值**：无

  ```nginx
  ssl_protocols TLSv1.2 TLSv1.3;  # 启用 TLS 1.2 和 1.3 协议
  ssl_ciphers 'ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256';  # 设置安全的加密算法
  ```



### 10. **limit_req** 和 **limit_conn**

**作用**：限制请求的数量和连接的数量，防止 DoS 攻击或过载

**语法**：

```nginx
limit_req zone=<zone> [burst=<number>] [nodelay];
limit_conn zone=<zone> [max=<number>];
```

**默认值**：无

```nginx
limit_req zone=req_limit_per_ip burst=10 nodelay;  # 限制每秒的请求数量
limit_conn addr 10;  # 每个 IP 地址最大允许 10 个连接
```



### 11、**完整示例**

```nginx
server {
    listen 80;  # 监听 HTTP 端口 80
    server_name example.com www.example.com;  # 配置域名
    root /var/www/html;  # 设置网站根目录
    index index.html index.htm;  # 设置首页文件

    # 日志配置
    access_log /var/log/nginx/access.log;
    error_log /var/log/nginx/error.log warn;

    # 处理图片请求
    location /images/ {
        root /var/www/data;  # 图片存储目录
    }

    # 代理 API 请求
    location /api/ {
        proxy_pass http://backend-server;  # 转发请求到后端服务
    }

    # 自定义错误页面
    error_page 404 /404.html;  # 404 错误时显示 404.html
    error_page 500 502 503 504 /50x.html;  # 服务器错误时显示 50x.html

    # SSL 配置（如果启用 HTTPS）
    listen 443 ssl;
    ssl_certificate /etc/nginx/ssl/example.com.crt;
    ssl_certificate_key /etc/nginx/ssl/example.com.key;

    # URL 重写
    rewrite ^/oldpath/(.*)$ /newpath/$1 permanent;

    # 限制请求速率
    limit_req zone=req_limit_per_ip burst=10 nodelay;
}
```



## 6、location 块

### 1、基本配置

**作用**：配置 URL 路径的匹配规则，用于处理不同的请求路径，对特定的请求进行处理，地址定向、数据缓存和应答控制等功能，还有许多第三方模块的配置也在这里进行

**语法**：location [=|~|~*|^~] <path> { ... }

**默认值**：无

常用匹配方式：

- `location /`：匹配根路径
- `location ~`：使用正则表达式匹配路径（区分大小写）
- `location ~*`：使用正则表达式匹配路径（不区分大小写）
- `location ^~`：优先匹配该路径，避免使用正则表达式匹配

```nginx
location /images/ {
    root /var/www/data;  # 配置 images 目录的访问路径
}

location /api/ {
    proxy_pass http://backend;  # 将请求转发到后端服务
}
```



**注意**：

- 如果 uri 包含正则表达式，则必须要有 ~ 或者 ~* 标识



**匹配顺序**：

- 多个正则location直接按书写顺序匹配，成功后就不会继续往后面匹配
- 普通（非正则）location会一直往下，直到找到匹配度最高的（最大前缀匹配）
- 当普通location与正则location同时存在，如果正则匹配成功，则不会再执行普通匹配
- 所有类型location存在时，“=”匹配 > “^~”匹配 > 正则匹配 > 普通（最大前缀匹配）



### 2. **proxy_pass**

**作用**：将请求转发到其他服务器，通常用于反向代理

**语法**：`proxy_pass <url>;`

**默认值**：无

```nginx
location /api/ {
    proxy_pass http://backend-server;  # 将请求转发到后端服务
}
```

| 访问URL                        | location配置 | proxy_pass配置        | 后端接收的请求                 |
| :----------------------------- | :----------- | :-------------------- | :----------------------------- |
| http://test.com/user/test.html | /user/       | http://test.com/      | http://test.com/test.html      |
| http://test.com/user/test.html | /user/       | http://test.com       | http://test.com/user/test.html |
| http://test.com/user/test.html | /user        | http://test.com       | http://test.com/user/test.html |
| http://test.com/user/test.html | /user        | http://test.com/      | http://test.com//test.html     |
| http://test.com/user/test.html | /user/       | http://test.com/haha/ | http://test.com/haha/test.html |
| http://test.com/user/test.html | /user/       | http://test.com/haha  | http://test.com/hahatest.html  |



**注意**：

- nginx 官网中把proxy_path后的path分为两种
  - 不带uri，即 http://ip:port，不会把匹配的路径部分给代理走
  - 带uri，即 http://ip:port/ 或 http://ip:port/xxx，会把 location 中匹配的路径部分代理走，但是参数依然可以传递



**注意**：（以下情况最好不要携带uri）

- location 使用了正则
- location块内使用了 rewrite



### **3. root**

**作用**：设置指定路径作为根目录，通常用于静态文件的服务

**语法**：`root <path>;`

```nginx
location /images/ {
    root /var/www/data;
}
```



### **2. index**

**作用**：设置默认首页文件，当访问目录时，Nginx 会返回这个文件

**语法**：`index <file> [file] ...;`

**默认值**：`index.html index.htm`

```nginx
location / {
    index index.html index.htm;
}
```



### **3. try_files**

**作用**：尝试按照指定的顺序查找文件，如果文件不存在，按照指定的替代文件进行处理

**语法**：`try_files <file> [file] ... <fallback>;`

```nginx
location / {
    try_files $uri $uri/ /index.html;
}
```



### **4. rewrite**

**作用**：对请求 URL 进行重写。可以使用正则表达式修改 URL

**语法**：`rewrite <regex> <replacement> [flag];`

```nginx
location /oldpath/ {
    rewrite ^/oldpath/(.*)$ /newpath/$1 permanent;
}
```



### **5. return**

**作用**：设置响应的 HTTP 状态码及内容，或者进行 URL 重定向

**语法**：`return <status_code> [<uri>];`

```nginx
location /oldpath/ {
    return 301 /newpath/;
}
```



### **7. fastcgi_pass**

**作用**：将请求传递给 FastCGI 服务器，用于处理 PHP 或其他脚本语言

**语法**：`fastcgi_pass <address>;`

```nginx
location ~ \.php$ {
    fastcgi_pass 127.0.0.1:9000;
    fastcgi_param SCRIPT_FILENAME /var/www/html$document_root$fastcgi_script_name;
    include fastcgi_params;
}
```



### **8. add_header**

**作用**：在响应中添加 HTTP 头部

**语法**：`add_header <name> <value> [always];`

```nginx
location /images/ {
    add_header Cache-Control "public, max-age=86400";
}
```



### **9. set**

**作用**：设置一个变量的值，供后续指令使用

**语法**：`set $variable <value>;`

```nginx
location /images/ {
    set $image_path /var/www/data;
}
```



### **10. limit_req**

**作用**：限制请求频率，防止滥用或 DoS 攻击

**语法**：`limit_req zone=<zone> [burst=<number>] [nodelay];`

```nginx
location /api/ {
    limit_req zone=req_limit_per_ip burst=10 nodelay;
}
```



### **11. limit_conn**

**作用**：限制每个 IP 地址的连接数

**语法**：`limit_conn <zone> <max>;`

```nginx
location /api/ {
    limit_conn addr 10;
}
```



### **12. expires**

**作用**：设置文件的过期时间，常用于缓存策略

**语法**：`expires <time>;`

```nginx
location /static/ {
    expires 30d;
}
```



### **13. access_log**

**作用**：设置访问日志的位置和格式

**语法**：`access_log <file> [format];`

```nginx
location /images/ {
    access_log /var/log/nginx/images.log;
}
```



### **14. error_log**

**作用**：设置错误日志的位置和级别

**语法**：`error_log <file> [level];`

```nginx
location / {
    error_log /var/log/nginx/location_error.log warn;
}
```



### **15. deny**

**作用**：拒绝来自指定 IP 地址或网络的访问

**语法**：`deny <address>;`

```nginx
location /admin/ {
    deny 192.168.1.1;
}
```



### **16. allow**

**作用**：允许来自指定 IP 地址或网络的访问

**语法**：`allow <address>;`

```nginx
location /admin/ {
    allow 192.168.1.0/24;
    deny all;
}
```



### **17. if**

**作用**：在 `location` 块内基于条件判断执行某些操作。通常用于设置特殊的配置或重定向

**语法**：`if (<condition>) { ... }`

```nginx
location / {
    if ($http_user_agent ~* "MSIE") {
        return 403;
    }
}
```



### **18. fallback**

**作用**：配置请求失败时的备用处理

**语法**：`fallback <url>;`

```nginx
location /app/ {
    try_files $uri /fallback.html;
}
```



### **19. multi_accept**

**作用**：允许每个工作进程接受多个连接

**语法**：`multi_accept on | off;`

```nginx
location /api/ {
    multi_accept on;
}
```



### **20. cache**

**作用**：配置请求的缓存

**语法**：`cache <zone> [options];`

```nginx
location /media/ {
    cache media_cache;
}
```



# 6、Nginx单机配置实例

## 1、配置实例一

实现效果：使用 nginx 反向代理，访问 www.123.com 直接跳转到 127.0.0.1:8080

1. 首先在本机hosts文件中添加映射，将www.123.com 映射到127.0.0.1，这样即可通过www.123.com:8080 访问到Tomcat首页

   ~~~bash
   vim /etc/hosts
   ~~~
   
   
   
2. 在Nginx的配置文件中添加如下配置

   ```text
   server {
   	listen	80;
   	server_name	www.123.com;
   	
   	location / {
   		proxy_pass http://127.0.0.1:8080;
   		index index.html index.htm index.jsp
   	}
   }
   ```

3. 如上配置将会监听80端口，访问域名为 www.123.com ，不加端口号时默认为80端口。

## 2、配置实例二

实现效果：使用 nginx 反向代理，根据访问的路径跳转到不同端口的服务中。

nginx 监听端口为 9001

访问 http://127.0.0.1:9001/edu/ 直接跳转到 127.0.0.1:8081 

访问 http://127.0.0.1:9001/vod/ 直接跳转到 127.0.0.1:8082

1. 准备两个Tomcat，分别监听8081和8082端口

2. 修改Nginx配置文件

   ```text
   server {
   	listen 9001;
   	server_name localhost;
   	
   	location ~ /edu/ {
   		proxy_pass http://localhost:8081;
   	}
   	location ~ /vod/ {
   		proxy_pass http://localhost:8082;
   	}
   }
   ```

## 3、配置负载均衡

1. 准备两个Tomcat分别监听不同端口

2. 在Nginx中配置

   ```text
   http {
   	upstream myserver {
   		ip_hash;
   		server {192.168.100.1}:{8081} weight=1 down;
   		server {192.168.100.1}:{8083} weight=2;
   		server {192.168.100.1}:{8082} weight=1 backup;
   	}
   	server {
   		location / {
   			proxy_pass http://myserver;
   			proxy_connect_timeout 10;
   		}
   	}
   }
   ```

**负载均衡策略**：

1. 轮询
   - 每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器 down 掉，能自动剔除。
   - 默认情况下使用轮询方式，逐一转发，这种方式适用于无状态请求。
2. weight 权重
   - 指定轮询几率，weight 和访问比率成正比，用于后端服务器性能不均的情况。
   - 权重默认值为1，weight越大，负载的权重就越大。 
   - down：表示当前的server暂时不参与负载 
   - backup： 其它所有的非backup机器down或者忙的时候，请求backup机器。
3. ip_hash
   - 每个请求按访问 ip 的 hash 结果分配，这样每个访客固定访问一个后端服务器，可以解决 session 的问题。
4. fair(第三方)
   - 按后端服务器的响应时间来分配请求，响应时间短的优先分配。
5. least_conn
   - 最少连接访问
6. url_hash
   - 根据用户访问的url定向转发请求




## 4、配置动静分离

Nginx 动静分离简单来说就是把动态跟静态请求分开，不能理解成只是单纯的把动态页面和静态页面物理分离。严格意义上说应该是动态请求跟静态请求分开，可以理解成使用 Nginx  处理静态页面，Tomcat 处理动态页面。

动静分离从目前实现角度来讲大致分为两种

- 一种是纯粹把静态文件独立成单独的域名，放在独立的服务器上，也是目前主流推崇的方案
- 另外一种方法就是动态跟静态文件混合在一起发布，通过 nginx 来分开。

具体实现：通过 location 指定不同的后缀名实现不同的请求转发。通过 expires 参数设置，可以使 浏览器缓存过期时间，减少与服务器之前的请求和流量。

具体 Expires 定义：是给一个资源设定一个过期时间，也就是说无需去服务端验证，直接通过浏览器自身确认是否过期即可， 所以不会产生额外的流量。此种方法非常适合不经常变动的资源。（如果经常更新的文件， 不建议使用 Expires 来缓存），我这里设置 3d，表示在这 3 天之内访问这个 URL，发送一个请求，比对服务器该文件最后更新时间没有变化，则不会从服务器抓取，返回状态码 304，如果有修改，则直接从服务器重新下载，返回状态码 200

1. 准备静态页面与Tomcat

2. 配置Nginx文件

   ```text
   server {
   	listen 80;
   	server_name 192.168.100.1;
   	
       location / {
       	proxy_pass http://127.0.0.1:8080;
       	root html;
       	index index.html index.htm;
       }
   	
   	location /www/ {
   		root /data/;
   		index index.html index.htm;
   	}
   	location /image/ {
   		root /data/;
   		autoindex on;
   	}
   }
   ```



## 5、配置Sticky模块

[使用参考](http://nginx.org/en/docs/http/ngx_http_upstream_module.html#sticky) 

tengine中有session_sticky模块我们通过第三方的方式安装在开源版本中。

sticky是第三方模块，需要重新编译Nginx，他可以对Nginx这种静态文件服务器使用基于cookie的负载均衡。

1. 下载模块：

   - [项目官网](https://bitbucket.org/nginx-goodies/nginx-sticky-module-ng/src/master/)，[下载](https://bitbucket.org/nginx-goodies/nginx-sticky-module-ng/get/1.2.6.zip)

   - [另外一个版本](https://github.com/bymaximus/nginx-sticky-module-ng) 

2. 上传解压

3. 重新编译Nginx

   1. 依赖 openssl-devel

   2. ~~~bash
      ./configure --prefix=/usr/local/nginx --add-module=/root/nginx-goodies-nginx-sticky-module-ng-c78b7dd79d0d
      ~~~

4. 执行make

   1. 如遇报错修改源码

   2. 打开 `ngx_http_sticky_misc.c`文件

      - 在12行添加

      ```bash
      #include <openssl/sha.h>
      #include <openssl/md5.h>
      ```

      - 备份之前的程序

      - ~~~bash
        mv /usr/local/nginx/sbin/nginx /usr/local/nginx/sbin/nginx.old
        ~~~

5. 把编译好的Nginx程序替换到原来的目录里

   - ~~~bash
     cp objs/nginx /usr/local/nginx/sbin/
     ~~~

6. 升级检测

   - ~~~bash
     make upgrade
     ~~~

7. 检查程序中是否包含新模块

   - ~~~bash
     nginx -V
     ~~~

配置方法：

~~~bash
upstream httpget {

    sticky name=route expires=6h;

    server 192.168.44.102;
    server 192.168.44.103;
}
~~~



## 6、配置KeepAlive

在http协议的header中可以看到当前连接状态

#### 1、什么时候使用KeepAlive？

- 明显的预知用户会在当前连接上有下一步操作

- 复用连接，有效减少握手次数，尤其是https建立一次连接开销会更大

#### 2、什么时候不用KeepAlive？

- 访问内联资源一般用缓存，不需要keepalive

- 长时间的tcp连接容易导致系统资源无效占用





# 7、Nginx集群配置实例

## 1、集群原理

在Nginx集群中Nginx扮演的角色是：分发器。

任务：接受请求、分发请求、响应请求。

功能模块：

- ngx_http_upstream_module：基于应用层（七层）分发模块

- ngx_stream_core_module：基于传输层（四层）分发模块（1.9开始提供该功能）

<img src="images/image-20220125105224742.png" alt="image-20220125105224742" style="zoom:50%;" />

Nginx集群其实是：虚拟主机+反向代理+upstream分发模块组成的。

- 虚拟主机：负责接受和响应请求。

- 反向代理：带领用户去数据服务器拿数据。

- upstream：告诉nginx去哪个数据服务器拿数据。



## 2、keepalived/heartbeat/corosync

1、Heartbeat、Corosync、Keepalived这三个集群组件到底选哪个好呢？、

首先要说明的是，Heartbeat、Corosync是属于同一类型，Keepalived与Heartbeat、Corosync不是同一类型的。

- Keepalived使用的**vrrp**协议方式，虚拟路由冗余协议 (Virtual Router Redundancy Protocol，简称VRRP)。

- Heartbeat或Corosync是**基于主机或网络服务**的高可用方式。


简单的说就是，Keepalived的目的是模拟路由器的高可用，Heartbeat或Corosync的目的是实现Service的高可用。

所以一般Keepalived是实现前端高可用，常用的前端高可用的组合有，就是我们常见的LVS+Keepalived、Nginx+Keepalived、HAproxy+Keepalived。

而Heartbeat或Corosync是实现服务的高可用，常见的组合有Heartbeat v3(Corosync)+Pacemaker+NFS+Httpd 实现Web服务器的高可用、Heartbeat v3(Corosync)+Pacemaker+NFS+MySQL 实现MySQL服务器的高可用。

总结：

- Keepalived中实现轻量级的高可用，一般用于前端高可用，且不需要共享存储，一般常用于两个节点的高可用。
- Heartbeat(或Corosync)一般用于服务的高可用，且需要共享存储，一般用于多节点的高可用。

2、那heartbaet与corosync又应该选择哪个好？

一般用corosync，因为corosync的运行机制更优于heartbeat，就连从heartbeat分离出来的pacemaker都说在以后的开发当中更倾向于corosync，所以现在corosync+pacemaker是最佳组合。



## 3、双机高可用理论

双机高可用一般是通过虚拟IP（飘移IP）方法来实现的，基于Linux/Unix的IP别名技术。

双机高可用方法目前分为两种：

1. 双机**主从**模式：
   - 即前端使用两台服务器，一台主服务器和一台热备服务器，正常情况下，主服务器绑定一个公网虚拟IP，提供负载均衡服务，热备服务器处于空闲状态。
   - 当主服务器发生故障时，热备服务器接管主服务器的公网虚拟IP，提供负载均衡服务，但是热备服务器在主机器不出现故障的时候，永远处于浪费状态，对于服务器不多的网站，该方案不经济实惠。
2. 双机**主主**模式：
   - 前端使用两台负载均衡服务器，互为主备，且都处于活动状态，同时各自绑定一个公网虚拟IP，提供负载均衡服务。
   - 当其中一台发生故障时，另一台接管发生故障服务器的公网虚拟IP（这时由非故障机器一台负担所有的请求）这种方案，经济实惠，非常适合于当前架构环境。



## 4、配置主从集群

需要安装Keepalived

![image-20220110103528112](images/image-20220110103528112.png) 

1. 配置keepalived配置文件 /etc/keepalived/keepalived.conf

   ```text
   global_defs { 
    	notification_email { 
    		acassen@firewall.loc 
    		failover@firewall.loc 
    		sysadmin@firewall.loc 
   	} 
    	notification_email_from Alexandre.Cassen@firewall.loc 
    	smtp_server 192.168.17.129 
    	smtp_connect_timeout 30 
    	router_id LVS_DEVEL 
   } 
    
   vrrp_script chk_http_port { 
    	script "/usr/local/src/nginx_check.sh" 
    	interval 2 #（检测脚本执行的间隔） 
    	weight 2 
   }
   
   vrrp_instance VI_1 { 
    	state MASTER # 备份服务器上将 MASTER 改为 BACKUP xxxxxx主备不同处xxxxxx
   	interface ens33 # 网卡 
    	virtual_router_id 51 # 主、备机的 virtual_router_id 必须相同 
    	priority 100 # 主、备机取不同的优先级，主机值较大，备份机值较小 xxxxxx主备不同处xxxxxx
    	advert_int 1 
    	authentication { 
    		auth_type PASS 
    		auth_pass 1111 
    	} 
    	virtual_ipaddress { 
    		168.138.50.119 # VRRP H 虚拟地址 
    	} 
   }
   ```

   在 /usr/local/src 添加检测脚本：nginx_check.sh

   ```sh
   #!/bin/bash 
   A=`ps -C nginx –no-header |wc -l` 
   if [ $A -eq 0 ];then 
    /usr/local/nginx/sbin/nginx 
    sleep 2 
    if [ `ps -C nginx --no-header |wc -l` -eq 0 ];then 
    killall keepalived 
    fi 
   fi 
   ```

2. 在所有节点机上配置

   ```shell
   //关闭防火墙
   systemctl stop firewalld 
   //关闭 selinux，重启生效
   sed -i 's/^SELINUX=.*/SELINUX=disabled/' /etc/sysconfig/selinux 
   //时间同步
   ntpdate 0.centos.pool.ntp.org 
   ```

3. 配置Nginx配置文件

   ```text
   user nginx;
   worker_processes auto;
   error_log /var/log/nginx/error.log;
   pid /run/nginx.pid;
   include /usr/share/nginx/modules/*.conf;
   events {
    	worker_connections 1024;
   }
   http {
    	log_format main '$remote_addr - $remote_user [$time_local] "$request" '
    					'$status $body_bytes_sent "$http_referer" '
    					'"$http_user_agent" "$http_x_forwarded_for"';
    	access_log 			/var/log/nginx/access.log main;
    	sendfile 			on;
    	tcp_nopush 			on;
    	tcp_nodelay 		on;
    	keepalive_timeout 	65;
    	types_hash_max_size 2048;
    	include 			/etc/nginx/mime.types;
    	default_type 		application/octet-stream;
    	include 			/etc/nginx/conf.d/*.conf;
    	server {
    		listen 80;
    		server_name www.mtian.org;
    		location / {
   			root /usr/share/nginx/html;
    		}
    	access_log /var/log/nginx/access.log main;
    	}
   }
   ```
   
   
   
4. 配置LB节点

   修改Nginx配置文件

   ```text
   user nginx;
   worker_processes auto;
   error_log /var/log/nginx/error.log;
   pid /run/nginx.pid;
   include /usr/share/nginx/modules/*.conf;
   events {
   	worker_connections 1024;
   }
   http {
   	log_format main '$remote_addr - $remote_user [$time_local] "$request" '
   					'$status $body_bytes_sent "$http_referer" '
   					'"$http_user_agent" "$http_x_forwarded_for"';
   	access_log /var/log/nginx/access.log main;
   	sendfile on;
   	tcp_nopush on;
   	tcp_nodelay on;
   	keepalive_timeout 65;
   	types_hash_max_size 2048;
   	include /etc/nginx/mime.types;
   	default_type application/octet-stream;
   	include /etc/nginx/conf.d/*.conf;
   	upstream backend {
   		server 192.168.1.33:80 weight=1 max_fails=3 fail_timeout=20s;
   		server 192.168.1.34:80 weight=1 max_fails=3 fail_timeout=20s;
   	}
   	server {
   		listen 80;
   		server_name www.mtian.org;
   		location / {
   			proxy_pass http://backend;
   			proxy_set_header Host $host:$proxy_port;
   			proxy_set_header X-Forwarded-For $remote_addr;
   		}
   	}
   }
   ```

   

5. 在测试机上添加host解析

   ```text
   192.168.1.32 www.mtian.org
   192.168.1.31 www.mtian.org
   // 测试时候轮流关闭 lb1 和 lb2 节点，关闭后还是能够访问并看到轮循效果即表示 nginx lb 集群搭建成功。
   ```

   

6. 开始搭建 keepalived

   ```shell
   #两台LB节点上安装
   yum install keepalived -y
   ```

   

7. 配置 LB-01 节点

   ```text
   global_defs {
    notification_email {
    381347268@qq.com
    }
    smtp_server 192.168.200.1
    smtp_connect_timeout 30
    router_id LVS_DEVEL
   }
   vrrp_instance VI_1 {
    state MASTER
    interface ens33
    virtual_router_id 51
    priority 150
    advert_int 1
    authentication {
    auth_type PASS
    auth_pass 1111
    }
    virtual_ipaddress {
    192.168.1.110/24 dev ens33 label ens33:1
    }
   }
   ```

   ```shell
   systemctl start keepalived //启动 keepalived
   systemctl enable keepalived //加入开机自启动
   ```

8. ip a //查看 IP，会发现多出了 VIP 192.168.1.110

9. 配置 LB-02 节点

   ```text
   global_defs {
    notification_email {
    381347268@qq.com
    }
    smtp_server 192.168.200.1
    smtp_connect_timeout 30
    router_id LVS_DEVEL
   }
   vrrp_instance VI_1 {
    state BACKUP
    interface ens33
    virtual_router_id 51
    priority 100
    advert_int 1
    authentication {
    auth_type PASS
    auth_pass 1111
    }
    virtual_ipaddress {
    192.168.1.110/24 dev ens33 label ens33:1
    }
   }
   ```

   

10. ifconfig //查看 IP，此时备节点不会有 VIP（只有当主挂了的时候，VIP 才会飘到备 节点）



## 5、配置双主模式

![image-20220110105332995](images/image-20220110105332995.png) 

1. 在主从模式下修改，只是修改 LB 节点上面的 keepalived 服务的配置文件即可。此时 LB-01 节点即为 Keepalived 的主节点也为备节点，LB-02 节点同样即为 Keepalived 的主节点也为备节点。 LB-01 节点默认的主节点 VIP（192.168.1.110），LB-02 节点默认的主节点 VIP（192.168.1.210）

2. 修改keepalived配置文件，编辑配置文件，增加一段新的 vrrp_instance 规则

   ```text
   global_defs {
        notification_email {
        	381347268@qq.com
        }
        smtp_server 			192.168.200.1
        smtp_connect_timeout 	30
        router_id 				LVS_DEVEL
   }
   vrrp_instance VI_1 {
        state 				MASTER
        interface 			ens33
        virtual_router_id 	51
        priority 			150
        advert_int 		1
        authentication {
       	auth_type PASS
        	auth_pass 1111
        }
        virtual_ipaddress {
        	192.168.1.110/24 dev ens33 label ens33:1
        }
   }
   vrrp_instance VI_2 {
        state 				BACKUP
        interface 			ens33
        virtual_router_id 	52
        priority 			100
        advert_int 		1
        authentication {
        	auth_type PASS
        	auth_pass 2222
        }
        virtual_ipaddress {
        	192.168.1.210/24 dev ens33 label ens33:2
        }
   }
   
   ```

   

3. 配置 LB-02 节点，同样增加一个vrrp_instance规则

   ```text
   global_defs {
       notification_email {
        	381347268@qq.com
        }
        smtp_server 			192.168.200.1
        smtp_connect_timeout 	30
        router_id 				LVS_DEVEL
   }
   vrrp_instance VI_1 {
        state 				BACKUP
        interface 			ens33
        virtual_router_id 	51
        priority 			100
        advert_int 		1
        authentication {
        	auth_type PASS
        	auth_pass 1111
        }
        virtual_ipaddress {
        	192.168.1.110/24 dev ens33 label ens33:1
        }
   }
   vrrp_instance VI_2 {
        state 				MASTER
        interface 			ens33
        virtual_router_id 	52
        priority 			150
        advert_int 		1
        authentication {
        	auth_type PASS
        	auth_pass 2222
        }
        virtual_ipaddress {
        	192.168.1.210/24 dev ens33 label ens33:2
        } 
   }
   ```

   

# 8、Nginx原理与优化

## 1、基本原理

<img src="images/image-20220109215913777.png" alt="image-20220109215913777" style="zoom:80%;" /> 

![image-20220109215924538](images/image-20220109215924538.png) 

## 2、sendfile

<img src="images/image-20220427163820726.png" alt="image-20220427163820726" style="zoom:80%;" />

<img src="images/image-20220427164106217.png" alt="image-20220427164106217" style="zoom:80%;" />

<img src="images/image-20220427164415998.png" alt="image-20220427164415998" style="zoom:80%;" />





## 3、master-workers 机制

首先，对于每个 worker 进程来说，独立的进程，不需要加锁，所以省掉了锁带来的开销， 同时在编程以及问题查找时，也会方便很多。

其次，采用独立的进程，可以让进程互相之间不会影响，一个进程退出后，其它进程还在工作，服务不会中断，master 进程则很快启动新的 worker 进程。当然，worker 进程的异常退出，肯定是程序有 bug 了，异常退出，会导致当前 worker 上的所有请求失败，不过不会影响到所有请求，所以降低了风险。



## 4、worker 数量

Nginx 同 redis 类似都采用了 io 多路复用机制，每个 worker 都是一个独立的进程，但每个进程里只有一个主线程，通过异步非阻塞的方式来处理请求， 即使是千上万个请求也不在话下。每个 worker 的线程可以把一个 cpu 的性能发挥到极致。所以 worker 数和服务器的 cpu 数相等是最为适宜的。设少了会浪费 cpu，设多了会造成 cpu 频繁切换上下文带来的损耗。

```text
#设置 worker 数量。
worker_processes 4
#work 绑定 cpu(4 work 绑定 4cpu)。
worker_cpu_affinity 0001 0010 0100 1000
#work 绑定 cpu (4 work 绑定 8cpu 中的 4 个) 。
worker_cpu_affinity 0000001 00000010 00000100 00001000
```



## 5、worker_connection 连接数

这个值是表示每个 worker 进程所能建立连接的最大值，所以，一个 nginx 能建立的最大连接数，应该是 

worker_connections * worker_processes

当然，这里说的是最大连接数，对于 HTTP 请求本地资源来说 ， 能够支持的最大并发数量是 

worker_connections *  worker_processes

如果是支持 http1.1 的浏览器每次访问要占两个连接，所以普通的静态访 问最大并发数是： 

worker_connections * worker_processes / 2

而如果是 HTTP 作为反向代理来说，最大并发数量应该是 

worker_connections *  worker_processes/ 4

因为作为反向代理服务器，每个并发会建立与客户端的连接和与后端服务的连接，会占用两个连接

![image-20220109220400182](images/image-20220109220400182.png) 

# 扩展

## 1、Nginx+CertBot添加证书

### 1、安装EPEL

```shell
yum install https://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm
```

### 2、安装Snaped

```shell
yum install snapd
```

#### 2.1、配置Snaped

```bash
systemctl enable --now snapd.socket
```

```bash
ln -s /var/lib/snapd/snap /snap
```

```
snap install core
```

### 3、安装CertBot

删除任何其他方式安装的Cerbot

```shell
yum remove certbot
```

使用snap安装

```shell
snap install --classic certbot
```

```
ln -s /snap/bin/certbot /usr/bin/certbot
```

### 4、配置CertBot

#### 4.1、安装单域名证书

##### 4.1.1、自动配置Nginx

```shell
# 若nginx未安装在默认路径(/etc/nginx or /usr/local/etc/nginx)下需自己指定nginx路径，到conf目录
certbot --nginx --nginx-server-root=/usr/local/nginx/conf
```

#### 4.2、安装泛域名证书

##### 4.2.1、自动配置版Nginx

```shell
certbot --preferred-challenges dns --nginx -d *.xxx.com --server https://acme-v02.api.letsencrypt.org/directory
```

似乎设置了 --preferred-challenges dns DNS挑战，则需要4.2.2与4.2.3

##### 4.2.2、查看验证信息

##### 4.2.3、DNS添加验证解析

添加完成后，返回shell界面回车

### 5、自动配置

```shell
echo "0 0,12 * * * root python -c 'import random; import time; time.sleep(random.random() * 3600)' && certbot renew -q" | sudo tee -a /etc/crontab > /dev/null
```

```shell
SLEEPTIME=$(awk 'BEGIN{srand(); print int(rand()*(3600+1))}'); echo "0 0,12 * * * root sleep $SLEEPTIME && certbot renew -q" | sudo tee -a /etc/crontab > /dev/null
```

<img src="images/image-20220427171953938.png" alt="image-20220427171953938" style="zoom:80%;" />

## 2、root与alias

[root]
语法：root path
默认值：root html
配置段：http、server、location、if

```text
location ^~ /t/ {
     root /www/root/html;
}
```

如果一个请求的URI是 /t/a.html，web服务器将会返回服务器上的/www/root/html/t/a.html的文件。

root指定的目录是location匹配访问的path目录的上一级目录，这个path目录一定要是真实存在root指定目录下

**注意**：

- root会忽视路径最后面的 /



[alias]
语法：alias path
配置段：location

```text
location ^~ /t/ {
	alias /www/root/html/new_t/;
}
```

如果一个请求的URI是 /t/a.html，web服务器将会返回服务器上的/www/root/html/new_t/a.html的文件。

注意这里是new_t，因为alias指定的目录是准确的，即location匹配访问的path目录下的文件直接是在alias目录下查找的，也可以说alias会把location匹配的路径丢弃掉，把当前匹配到的目录指向到指定的目录。

1. 使用alias时，目录名最后面一定要加"/"。
2. alias在使用正则匹配时，必须捕捉要匹配的内容并在指定的内容处使用。
3. alias只能位于location块中（root可以不放在location中）。

**注意**：

- 使用alias标签的目录块中不能使用rewrite的break

**注意**：

- alias虚拟目录配置中，location匹配的path目录如果后面不带 /，那么访问的url地址中这个path目录后面加不加 / 不影响访问，访问时它会自动加上 / 
- 如果location匹配的path目录后面加上 / ，那么访问的url地址中这个path目录必须要加上 / ，访问时它不会自动加上 / ，如果不加上 / ，访问就会失败
- alias对于location以及path路径最后的 / 必须两个同时存在或同时不存在



## 3、UrlRewrite

rewrite是实现URL重写的关键指令，根据regex (正则表达式)部分内容，重定向到replacement，结尾是flag标记。

~~~TXT
关键字 	正则 		替代内容 	flag标记
rewrite <regex> <replacement> [flag];
~~~

- 关键字：其中关键字error_log不能改变
- 正则：perl兼容正则表达式语句进行规则匹配
- 替代内容：将正则匹配的内容替换成replacement
- flag标记：rewrite支持的flag标记

rewrite参数的标签段位置：server，location，if

flag标记说明：

~~~TXT
last # 本条规则匹配完成后，继续向下匹配新的location URI规则
break # 本条规则匹配完成即终止，不再匹配后面的任何规则
redirect # 返回302临时重定向，浏览器地址栏会显示跳转后的URL地址
permanent # 返回301永久重定向，浏览器地址栏会显示跳转后的URL地址
~~~

~~~txt
rewrite ^/([0-9]+).html$ /index.jsp?pageNum=$1 break;
~~~



## 4、防盗链配置

~~~txt
valid_referers none | blocked | server_names | strings ....;
~~~

- none：检测 Referer 头域不存在的情况。 
- blocked：检测 Referer 头域的值被防火墙或者代理服务器删除或伪装的情况，这种情况该头域的值不以 http:// 或 https:// 开头。 
- server_names：设置一个或多个 URL ，检测 Referer 头域的值是否是这些 URL 中的某一个。

在需要防盗链的location中配置

~~~txt
valid_referers 192.168.44.101;
	if ($invalid_referer) {
	return 403;
}
~~~



























