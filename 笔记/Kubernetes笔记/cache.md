# 9、Kubernetes Config

## 1、Secret

### 1、概述

Secret 用于安全存储密码、令牌、密钥等敏感数据，避免直接暴露在镜像或 Pod 定义中，支持两种使用方式：

- Volume 挂载
- 环境变量注入

**安全特性**：

- 命名空间隔离（无法跨命名空间访问）
- 建议启用 etcd 静态加密
- 应定期轮换敏感凭证



### 2、类型说明

#### 1、核心类型

##### 1、Opaque

**作用**：存储任意用户自定义数据

**特点**：

- 数据需 base64 编码存储
- 适用于密码、API 密钥等通用敏感信息
- 创建命令：kubectl create secret generic

~~~yaml
# 推荐使用 kubectl 创建（自动处理 base64 编码）
kubectl create secret generic mysecret \
  --from-literal=username=admin \
  --from-literal=password=1f2d1e2e67df
~~~



##### 2、kubernetes.io/service-account-token

**作用**：服务账号访问 Kubernetes API 的凭证

**特性**：

- 自动创建并与 ServiceAccount 绑定

- 包含三个关键文件：

  - ~~~bash
    ca.crt        # 集群 CA 证书
    namespace     # 当前命名空间
    token         # JWT 令牌
    ~~~

~~~yaml
# 创建 Deployment 示例
kubectl create deployment nginx --image=nginx

# 查看自动挂载的凭证文件
kubectl exec $(kubectl get pod -l app=nginx -o jsonpath='{.items[0].metadata.name}') \
  -- ls /var/run/secrets/kubernetes.io/serviceaccount
# ca.crt
# namespace
# token
~~~



##### 3、kubernetes.io/dockerconfigjson

**作用**：存储私有容器镜像仓库认证信息

**使用场景**：Pod 拉取私有镜像时通过 imagePullSecrets 引用

~~~bash
kubectl create secret docker-registry <name> \
  --docker-server=<server> \
  --docker-username=<user> \
  --docker-password=<password>
~~~

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: private-image-pod
spec:
  containers:
    - name: app
      image: registry.example.com/team/private-app:v1
  imagePullSecrets:
    - name: myregistrykey
~~~



##### 4、kubernetes.io/tls

**作用**：存储 TLS 证书和私钥

**典型应用**：Ingress TLS 终止、mTLS 认证

**强制字段**：

~~~yaml
data:
  tls.crt: <base64 编码证书>
  tls.key: <base64 编码私钥>
~~~



#### 2、专用认证类型

##### 1、kubernetes.io/basic-auth

**作用**：存储基本身份认证凭据

**必需字段**：

~~~yaml
data:
  username: <base64 用户名>
  password: <base64 密码>
~~~



##### 2、kubernetes.io/ssh-auth

**作用**：存储 SSH 连接凭据

**强制要求**：

~~~yaml
data:
  ssh-privatekey: <base64 编码私钥>
~~~



#### 3、系统级类型

##### 1、bootstrap.kubernetes.io/token

**作用**：节点加入集群的启动引导令牌

**核心字段**：

~~~yaml
data:
  token-id: 6 字符标识符
  token-secret: 16 字符密钥
  expiration: RFC3339 格式过期时间
~~~



#### 4、自定义类型

##### 1、用户自定义类型

**格式要求**：`<domain>/<type-name>`（如：acme.com/vault-token）

**优势**：

- 实现特定业务场景的敏感数据管理
- 可集成自定义控制器实现高级功能





### 3、创建方式

#### 1、kubectl CLI 创建

| 类型        | 命令格式                                     | 示例                                                         |
| ----------- | -------------------------------------------- | ------------------------------------------------------------ |
| Opaque      | kubectl create secret generic <name>         | kubectl create secret generic db-creds \ --from-literal=username=admin \ --from-literal=password=1f2d1e2e67df |
| Docker 认证 | kubectl create secret docker-registry <name> | kubectl create secret docker-registry myregistry \ --docker-server=registry.example.com \ --docker-username=admin \ --docker-password=\*\* |
| TLS 证书    | kubectl create secret tls <name>             | kubectl create secret tls nginx-cert \ --cert=path/to/cert.crt \ --key=path/to/cert.key |



#### 2、文件/目录导入

~~~bash
# 从单个文件创建（自动取文件名作为键名）
kubectl create secret generic ssh-key --from-file=id_rsa=~/.ssh/id_rsa

# 从目录批量创建（每个文件生成一个键值对）
kubectl create secret generic configs --from-file=path/to/config_dir/

# 组合文件和文字值
kubectl create secret generic combo-secret \
  --from-file=ssl.crt=server.crt \
  --from-literal=timeout=30
~~~



#### 3、环境变量文件创建

~~~bash
# 从 .env 文件创建（每行 KEY=VALUE）
kubectl create secret generic env-secret --from-env-file=config.env
~~~

~~~bash
API_KEY=supersecret
DB_PASSWORD=topsecret
~~~



#### 4、YAML 清单创建

~~~yaml
apiVersion: v1
kind: Secret
metadata:
  name: manual-secret
type: Opaque
data:
  username: YWRtaW4=  # base64 编码值
stringData:          # 自动编码字段（推荐方式）
  password: "plaintext-password" 
~~~

优先使用 stringData 字段：避免手动 base64 编码错误





### 4、使用方式

#### 1、Volume 挂载

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: secret-volume-pod
spec:
  containers:
  - name: app
    image: nginx
    volumeMounts:
    - name: secret-vol
      mountPath: "/etc/secrets"
      readOnly: true
  volumes:
  - name: secret-vol
    secret:
      secretName: mysecret
      # 可选：指定特定键值对
      items:
      - key: username
        path: credentials/user
      - key: password
        path: credentials/pass
~~~



#### 2、环境变量注入

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: secret-env-deploy
spec:
  template:
    spec:
      containers:
      - name: app
        image: nginx
        env:
        - name: DB_USERNAME
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
~~~



**注意**：

- 环境变量不会自动更新（需重启 Pod）
- 不推荐用于大体积数据（超过 1MB 的 Secret 无法通过此方式使用）
- 可能通过日志意外暴露（需配置日志脱敏）



#### 4、私有镜像拉取

~~~yaml
# Pod 级别引用
apiVersion: v1
kind: Pod
metadata:
  name: private-image-pod
spec:
  containers:
  - name: app
    image: registry.example.com/private/app:v1
  imagePullSecrets:
  - name: regcred

# ServiceAccount 级别引用（集群全局生效）
apiVersion: v1
kind: ServiceAccount
metadata:
  name: ci-serviceaccount
imagePullSecrets:
- name: regcred
~~~



### 5、高级配置

#### 1、最佳实践

| 使用场景                 | 推荐方式                   | 原因                                   |
| ------------------------ | -------------------------- | -------------------------------------- |
| 配置文件/证书            | Volume 挂载                | 支持动态更新，避免重启容器             |
| 短期凭证（如 API 密钥）  | 环境变量                   | 快速生效，适合频繁变更场景             |
| 私有镜像仓库认证         | ServiceAccount 级引用      | 集群全局生效，避免每个 Pod 重复配置    |
| 高敏感数据（如 CA 证书） | 投射卷（Projected Volume） | 可组合多个 Secret 并设置精细的文件权限 |



## 2、ConfigMap

### 1、概述

ConfigMap 是 Kubernetes 中用于存储非敏感配置数据的 API 对象，便于将配置与应用程序镜像分离，实现配置动态管理

**主要特点包括**：

- **版本支持**：自 v1.2 引入
- **数据结构**：支持存储键值对、完整配置文件或 JSON 二进制大对象
- **作用范围**：命名空间级别资源，不可跨命名空间共享
- **典型用法**：通过环境变量、命令行参数或文件形式注入容器



**注意**：

- Pod 引用的 ConfigMap 必须与 Pod 处于同一命名空间，且需在 Pod 创建前已存在



### 2、创建方式

#### 1、目录导入

使用 --from-file 指定目录路径，目录内每个文件生成一个键值对（键=文件名，值=文件内容）

~~~bash
# 示例目录结构
ls configs/
game.properties  ui.properties

kubectl create configmap game-config --from-file=configs/
~~~



#### 2、文件导入

通过多次使用 --from-file 指定多个文件

~~~bash
kubectl create configmap game-config-2 \
  --from-file=configs/game.properties \
  --from-file=configs/ui.properties
~~~



#### 3、字面量创建

通过 --from-literal 直接指定键值对

~~~bash
kubectl create configmap special-config \
  --from-literal=special.how=very \
  --from-literal=special.type=charm
~~~



#### 4、YAML 清单

声明式资源配置文件

~~~yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: special-config
data:
  special.how: very
  special.type: charm
~~~



### 3、使用方式

#### 1、注入环境变量

通过 valueFrom 引用 ConfigMap 键值

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: test-pod
spec:
  containers:
    - name: test-container
      image: busybox
      command: ["/bin/sh", "-c", "env"]
      env:
        - name: SPECIAL_LEVEL_KEY
          valueFrom:
            configMapKeyRef:
              name: special-config
              key: special.how
~~~

使用 envFrom 自动注入所有键

~~~yaml
envFrom:
  - configMapRef:
      name: env-config  # 自动注入 env-config 的所有键
~~~



#### 2、Volume 挂载

先创建一个 CM

~~~yaml
# 定义包含多个键值对的 ConfigMap
apiVersion: v1
kind: ConfigMap
metadata:
  name: special-config
data:
  special.how: "very"          # 键值对 1
  special.type: "charm"        # 键值对 2
  app.properties: |            # 键值对 3（多行内容）
    cache.enable=true
    cache.size=1024
~~~



##### 1、常规挂载（覆盖目录）

将 ConfigMap 所有键值对挂载到目录，清空该目录原有内容，每个键生成一个文件

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: full-mount-pod
spec:
  containers:
    - name: app
      image: busybox:latest
      command: ["/bin/sh", "-c", "ls -l /etc/config && cat /etc/config/special.how"]
      volumeMounts:
        - name: config-volume
          mountPath: /etc/config  # 全量挂载点
  volumes:
    - name: config-volume
      configMap:
        name: special-config      # 引用已创建的 ConfigMap
  restartPolicy: Never
~~~



##### 2、指定路径挂载（选择性挂载）

使用 items 选择性挂载键，并通过 path 定义文件结构

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: selective-mount-pod
spec:
  containers:
    - name: app
      image: busybox:latest
      command: ["/bin/sh", "-c", "ls -l /etc/config/keys && cat /etc/config/keys/special.level"]
      volumeMounts:
        - name: config-volume
          mountPath: /etc/config  # 挂载到该目录下的子路径
  volumes:
    - name: config-volume
      configMap:
        name: special-config
        items:
          - key: special.how       # 选择键
            path: keys/special.level  # 生成文件路径：/etc/config/keys/special.level
          - key: special.type
            path: keys/special.type   # 生成文件路径：/etc/config/keys/special.type
  restartPolicy: Never
~~~



##### 3、多目录挂载

将同一 ConfigMap 挂载到多个目录

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: multi-mount-pod
spec:
  containers:
    - name: app
      image: busybox:latest
      command: ["/bin/sh", "-c", "ls -l /etc/config /etc/config2"]
      volumeMounts:
        - name: config-volume
          mountPath: /etc/config   # 第一个挂载点
        - name: config-volume2
          mountPath: /etc/config2  # 第二个挂载点
  volumes:
    - name: config-volume
      configMap:
        name: special-config
        items:
          - key: special.how
            path: special.level    # 生成文件：/etc/config/special.level
    - name: config-volume2
      configMap:
        name: special-config
        items:
          - key: special.type
            path: special.type     # 生成文件：/etc/config2/special.type
  restartPolicy: Never
~~~



##### 4、SubPath （精确挂载）

不覆盖目录，使用 subPath 挂载单个文件，保留目录原有内容

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: subpath-file-pod
spec:
  containers:
    - name: nginx
      image: nginx:latest
      volumeMounts:
        - name: config-volume
          mountPath: /etc/nginx/conf.d/special.how  # 最终文件路径
          subPath: special.how  # 匹配 ConfigMap 中的键名
  volumes:
    - name: config-volume
      configMap:
        name: special-config
        items:
          - key: special.how
            path: special.how  # 显式定义路径（优先级高于 key）
  restartPolicy: Never
~~~



### 4、高级配置

#### 1、可选引用配置

标记为 optional: true 后，即使 ConfigMap 不存在也不会阻断 Pod 启动

~~~yaml
env:
  - name: OPTIONAL_ENV
    valueFrom:
      configMapKeyRef:
        name: missing-config
        key: key
        optional: true  # 环境变量可选
volumes:
  - name: optional-volume
    configMap:
      name: missing-config
      optional: true    # 卷挂载可选
~~~



#### 2、不可变 ConfigMap

通过设置 immutable: true 防止意外修改

~~~yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: immutable-config
immutable: true
data:
  key: value
~~~

**优点**：

- 禁止修改数据
- 提升集群性能（减少 API 监听）



#### 3、热更新与滚动发布

通过 kubectl edit 或替换文件进行更新

~~~yaml
kubectl edit configmap log-config
~~~

触发 Pod 滚动更新，通过修改 Pod 模板中的注解强制触发更新

~~~yaml
spec:
  template:
    metadata:
      annotations:
        version/config: "20231001"  # 修改此值触发更新
~~~
