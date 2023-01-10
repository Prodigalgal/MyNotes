# 1、Kubernetes 简介

## 1、基本介绍

Kubernetes  用于管理云平台中多个主机上的容器化应用，其目标是让部署容器化应用变得更简单且高效

Kubernetes  提供了应用部署、规划、更新、维护的一种机制

容器化与传统对比：

- 传统的应用部署方式是通过插件或脚本来安装，这样做的缺点是应用的运行、配置、管理、生存周期与当前操作系统绑定，不利于应用的升级更新、回滚等操作。虽然可以通过创建虚拟机的方式来实现某些功能，但是虚拟机非常重，并不利于可移植性
- 新的方式是通过将应用容器化进行部署，每个容器之间互相隔离，每个容器有自己的文件系统，容器之间进程不会相互影响，能区分计算资源。相对于虚拟机，容器能快速部署，并且由于容器与底层设施、机器文件系统解耦的，所以它能在不同的云、操作系统间进行迁移

容器的优点：

- 需要资源少、部署快
- 每个应用可以被打包成一个容器镜像，使得应用与容器之间是一对一的关系，不需要与其他应用堆栈进行组合
- 不依赖于生产环境基础结构，使得测试、生产能提供一致环境
- 便于监控与管理



## 2、功能与架构

### 1、功能

**自动装箱**：基于容器对应用运行环境的资源配置要求，自动部署应用容器

**自我修复**：当容器失败时，会对容器进行重启，当所部署的 Node 节点出错，会对容器进行重新部署和重新调度，当容器未通过监控检查时，会关闭此容器直到容器正常运行，才对外提供服务

**水平扩展**：通过简单的命令、用户 UI 或基于 CPU 等资源使用情况，对容器进行规模扩大或剪裁

**服务发现**：用户不需使用额外的服务发现机制，只需要基于 Kubernetes 自身能力实现服务发现和负载均衡

**滚动更新**：可以根据应用的变化，对运行的应用容器，进行一次性或批量式更新

**版本回退**：可以根据应用部署情况，对运行的应用容器，进行历史版本的即时回退

**密钥和配置管理**：在不需要重新构建镜像的情况下，可以部署和更新密钥和应用配置，类似热部署

**存储编排**：自动实现存储系统挂载及应用，特别对有状态应用实现数据持久化非常重要，存储系统可以来自于本地目录、网络存储(NFS、Gluster、Ceph 等)、公共云存储服

**批处理**：提供一次性任务，定时任务，满足批量数据处理和分析的场景



**应用部署架构分类**：

- **无中心**：GlusterFS
- **有中心**：HDFS、K8S



### 2、架构

**节点角色与功能**：

- **Master Node**：集群控制节点，对集群进行调度管理，接受集群外用户的集群操作请求，Master Node 由 API Server、Scheduler、ClusterState Store（ETCD 数据库）和 Controller MangerServer 所组成 
- **Worker Node**：集群工作节点，运行用户业务应用容器，Worker Node 包含 kubelet、kube proxy 和 ContainerRuntime

<img src="images/image-20221229202009990.png" alt="image-20221229202009990" style="zoom:67%;" />

<img src="images/image-20221229203054021.png" alt="image-20221229203054021" style="zoom:67%;" />

### 3、套件

#### 1、Kubeadm

Kubeadm 是官方社区推出的一个用于快速部署 Kubernetes 集群的工具

这个工具通过两条指令完成一个 Kubernetes 集群的部署： 

- 创建一个 Master 节点：kubeadm init 
- 将 Node 节点加入到当前集群中：kubeadm join 



#### 2、Kubectl

##### 1、基本介绍

Kubectl 是 Kubernetes 命令行工具，可以让用户对 Kubernetes 集群运行命令

可以使用 kubectl 来部署应用、监测和管理集群资源以及查看日志

~~~bash
# 命令语法
kubectl [command] [type] [name] [flags]
~~~

- command：指定要对资源执行的操作，例如：creat、get、describe、delete
- type：指定资源类型，大小写敏感，但是单数复数缩写不敏感，例如：pod、pods、po
- name：指定资源名称，大小写敏感，如果省略名称，会显示所有资源
- flags：指定可选参数，例如：-s、-server 指定 Kubernetes API server 的地址和端口



**注意**：

- Kubectl 版本和集群版本之间的差异必须在一个小版本号内
- 使用 kubectl --help 获取更多信息



##### 2、命令指南

**基础命令**：

| 命令    | 说明                                             |
| ------- | ------------------------------------------------ |
| creat   | 通过文件或标准输入创建资源                       |
| expose  | 将一个资源公开为一个新的 Service                 |
| run     | 在集群中运行一个特定的镜像                       |
| set     | 在对象上设定特定的功能                           |
| get     | 显示一个或多个的资源                             |
| explain | 显示文档参考资料                                 |
| edit    | 使用默认编辑器编辑一个资源                       |
| delete  | 通过文件、标准输入、资源名称、标签选择器删除资源 |





**管理命令**：

| 命令           | 说明                                                      |
| -------------- | --------------------------------------------------------- |
| rollout        | 管理资源的发布                                            |
| rollout-update | 对给定的复制控制器滚动更新                                |
| scale          | 扩容或缩容 Pod、Deployment、ReplicaSet、RC、Job           |
| certificate    | 修改证书资源                                              |
| cluster-info   | 显示集群信息                                              |
| top            | 显示资源（CPU、MEM、Storage）使用，需要使用 Headster 运行 |
| cordon         | 标记节点不可调度                                          |
| uncordon       | 标记节点可调度                                            |
| drain          | 驱逐节点上的资源，一般准备下线                            |
| taint          | 修改节点 taint 标记                                       |



**调试命令**：

| 命令         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| describe     | 显示资源或资源组的详细信息                                   |
| logs         | 在一个 Pod 中打印一个容器的日志，如果 Pod 只有一个容器，容器名称是可选的 |
| attach       | 附加到一个运行的容器里                                       |
| exec         | 执行命令到容器                                               |
| port-forward | 转发一个或多个本地端口到一个 Pod                             |
| proxy        | 运行一个 proxy 到 Kubernetes API Server                      |
| cp           | 拷贝文件或目录到容器中                                       |
| auth         | 检查授权                                                     |



**其他命令**：

| 命令         | 说明                                                 |
| ------------ | ---------------------------------------------------- |
| apply        | 通过文件或标准输入对资源进行配置                     |
| patch        | 使用补丁修改、更新资源的字段                         |
| repalce      | 通过文件或标准输入替换一个资源                       |
| conver       | 不同的 API 版本之间转换配置文件                      |
| label        | 更新资源上的标签                                     |
| annotate     | 更新资源上的注释                                     |
| completion   | 用于实现 kubectl 工具的自动补全                      |
| api-versions | 打印支持的 API 版本                                  |
| config       | 修改 kubeconfig 文件，用于访问 API，比如配置认证信息 |
| help         | 显示帮助信息                                         |
| plugin       | 运行一个命令行插件                                   |
| version      | 打印客户端和服务版本的信息                           |



**原生安装**：

根据目标系统修改，本次实验使用的是OracleLinux8，ARM64

如果要下载指定版本号，只需替换 $(curl -L -s https://dl.k8s.io/release/stable.txt) 这一部分

```bash
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/arm64/kubectl"
```

安装

~~~bash
install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
~~~

测试，下载的是最新版

~~~bash
kubectl version --client
~~~



#### 3、Kubelet

Kubelet 是在每个 Node 节点上运行的主要节点代理，用于在集群中的每个节点上启动 Pod 和容器等



#### 4、容器运行时

v1.24 之前的版本直接集成了 Docker Engine 的一个组件，名为 Dockershim，但是自 v1.24 版起，Dockershim 已从 Kubernetes 项目中移除，所以需要在集群内每个节点上安装一个容器运行时以使 Pod 可以运行在上面，v1.26 要求使用符合容器运行时接口（CRI）的运行时

容器运行时具有掌控容器运行的整个生命周期，包括镜像的构建和管理、容器的运行和管理等，其向上提供容器调用接口，包括容器生成与销毁的全生命周期管理的功能，向下提供调用接口，负责具体的容器操作事项

常见的容器运行时：

- containerd
- CRI-O
- Docker Engine
- Mirantis Container Runtime



# 2、Kubernetes 集群搭建

## 1、搭建需求

### 1、部署方式

目前生产部署 Kubernetes 集群主要有两种方式：

- **Kubeadm**：Kubeadm 是一个 K8s 部署工具，提供 kubeadm init 和 kubeadm join，用于快速部署 Kubernetes 集群
- **二进制包**（推荐）：从 Github 下载发行版的二进制包，手动部署每个组件，组成 Kubernetes 集群



**注意**：

- 如果从包管理器中安装 containerd，会发现 /etc/containerd/config.toml 默认禁止了 CRI 集成插件，需要移除



### 2、<a name='安装要求'>安装要求</a>

**硬件配置**：

- 2GB 或更多 RAM，2 个 CPU 或更多 CPU，硬盘 30GB 或更多

**网络配置**：

- 集群中所有机器之间网络互通

  - ~~~bash
    # 查看公网 IP 是否绑定在网卡上
    ip a | grep xx.xx.xx.xx
    
    # 如果没有,需要绑定
    # 临时,机器重启会失效
    ifconfig 网卡名:1 xx.xx.xx.xx
    
    # 永久
    # 在该 /etc/sysconfig/network-scripts 文件夹下创建 ifcfg-网卡名:1 的文件
    # 例如
    touch /etc/sysconfig/network-scripts/ifcfg-enp0s3:1
    
    # 添加如下内容
    DEVICE=enp0s3:1
    TYPE=Ethernet
    ONBOOT=yes
    NM_CONTROLLED=yes
    BOOTPROTO=static
    IPADDR=公网ip
    NETMASK=255.255.255.0
    ~~~

- 可以访问外网，需要拉取镜像

- 节点之中不可以有重复的主机名、MAC 地址或 product_uuid

  - ~~~bash
    # 获取网络接口的 MAC 地址
    ip link
    ifconfig -a
    
    # 对 product_uuid 校验
    cat /sys/class/dmi/id/product_uuid
    ~~~

- 桥接的 IPv4 流量传递到 iptables，因为 Kubernetes 的网络模型需要

  - ~~~bash
    cat <<EOF | sudo tee /etc/modules-load.d/k8s.conf
    overlay
    br_netfilter
    EOF
    
    modprobe overlay
    modprobe br_netfilter
    
    # 设置所需的 sysctl 参数，参数在重新启动后保持不变
    cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
    net.bridge.bridge-nf-call-iptables  = 1
    net.bridge.bridge-nf-call-ip6tables = 1
    net.ipv4.ip_forward                 = 1
    EOF
    
    # 应用 sysctl 参数而不重新启动
    sysctl --system
    ~~~

**禁止 swap 分区**：

- ~~~bash
  # 临时 
  swapoff -a 
  
  # 永久
  sed -ri 's/.*swap.*/#&/' /etc/fstab
  
  # 查看 swap 状态
  free -m
  ~~~

**关闭 selinux**：

- ~~~bash
  # 临时 
  setenforce 0
  
  # 永久 
  sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
  ~~~

**关闭防火墙**（不一定需要），但是一定要**开放所需的端口**：

- 控制面板：

  | 协议 | 方向 | 端口范围  | 目的                    | 使用者               |
  | ---- | ---- | --------- | ----------------------- | -------------------- |
  | TCP  | 入站 | 6443      | Kubernetes API server   | 所有                 |
  | TCP  | 入站 | 2379-2380 | etcd server client API  | kube-apiserver, etcd |
  | TCP  | 入站 | 10250     | Kubelet API             | 自身, 控制面         |
  | TCP  | 入站 | 10259     | kube-scheduler          | 自身                 |
  | TCP  | 入站 | 10257     | kube-controller-manager | 自身                 |

- 工作节点：

  | 协议 | 方向 | 端口范围    | 目的               | 使用者       |
  | ---- | ---- | ----------- | ------------------ | ------------ |
  | TCP  | 入站 | 10250       | Kubelet API        | 自身, 控制面 |
  | TCP  | 入站 | 30000-32767 | NodePort Services† | 所有         |
  
- 额外协议：

  ICMP、IPIP协议

**时间同步**：

- ~~~bash
  yum install -y ntpdate
  ntpdate time.windows.com
  ~~~



## 2、包管理器安装版

### 1、基准配置

#### 1、安装套件

访问 [Index of /yum/repos// (google.com)](https://packages.cloud.google.com/yum/repos/) 发现，Kubernetes 只适配到 el7，但是 el8 也可以使用，如果后续适配了可能需要修改

配置完包管理器的 repo，K8s 三件套可以一次安装了

以下为基于 Red Hat 发行的系统

~~~bash
# 配置仓库文件
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-\$basearch
enabled=1
gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF

# 安装
yum install -y kubectl kubelet kubeadm
~~~



#### 2、配置 CGroups

配置 containerd：

- ~~~bash
  # 修改
  vim /etc/containerd/config.toml
  
  # 粘贴
  [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc] 
    [plugins."io.containerd.grpc.v1.cri".containerd.runtimes.runc.options] 
       SystemdCgroup = true
  
  # 重启 containerd
  systemctl restart containerd
  systemctl status containerd
  ~~~

配置 Kubelet：

- ~~~bash
  # v1.22 后默认就是 systemd, 也即不需要这样配置了
  vim /usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf
  
  # 在 Environment="KUBELET_KUBECONFIG_ARGS 这行后面的双引号前添加如下内容，注意使用空格分隔
  --cgroup-driver=systemd
  
  # 重启 kubelet
  systemctl daemon-reload
  systemctl restart kubelet
  systemctl status kubelet
  ~~~

配置 Docker：

- ~~~bash
  # docker安装后默认没有daemon.json这个配置文件，需要进行手动创建
  vim /etc/docker/daemon.json
  "exec-opts": ["native.cgroupdriver=systemd"]
  
  # 如果没有则使用
  echo {\"exec-opts\": [\"native.cgroupdriver=systemd\"]} > /etc/docker/daemon.json
  
  # 重启 docker
  systemctl daemon-reload
  systemctl restart docker
  
  # 查看 Driver
  docker info|grep Driver
  ~~~



#### 3、配置文件初始化

~~~bash
# init.default初始化文件
kubeadm config print init-defaults >init.default.yaml
# 拉取相关镜像
kubeadm config images pull --config=init.default.yaml
~~~



#### 4、修改配置文件

~~~bash
vim /usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf

# 在末尾添加参数 --node-ip=公网IP
ExecStart=/usr/bin/kubelet $KUBELET_KUBECONFIG_ARGS $KUBELET_CONFIG_ARGS $KUBELET_KUBEADM_ARGS $KUBELET_EXTRA_ARGS --node-ip=<公网IP>
~~~



**重要注意**：开始前务必检查所有前置要求均满足

- 检查<a href='#安装要求'> 安装要求</a> 
- 检查容器运行时是否运行
- 检查公网 IP 是否绑定在网卡上



### 2、初始化 Master

~~~bash
# 启动 Master 节点
kubeadm init --apiserver-advertise-address=168.138.165.119 --kubernetes-version v1.26.0 --service-cidr=10.1.0.0/16 --pod-network-cidr=10.244.0.0/16
~~~

~~~bash
# 记录下 Token
You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 10.0.0.64:6443 --token 0sdiop.qs9m98sqqn9hzjjm \
	--discovery-token-ca-cert-hash sha256:39244ba104bd5d5f79c695852a18b329b86f75df1a4e7f8b583627a432e9f762
~~~

~~~bash
# 非 root 用户运行 kubectl
mkdir -p $HOME/.kube
cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
chown $(id -u):$(id -g) $HOME/.kube/config

# root 用户运行 kubectl
vim ~/.bashrc
# 在末尾添加
export KUBECONFIG=/etc/kubernetes/admin.conf
source ~/.bashrc
~~~

到此 Master 节点安装完毕



### 3、初始化 Node

~~~bash
# 加入Master，创建join-config.ymal文件
vim join-config.ymal

# apiServerEndpoint 为 Master 的地址
# token、 tlsBootstrapToken 为 Master 初始化生成的 Token
apiVersion: kubeadm.k8s.io/v1beta3
kind: JoinConfiguration
discovery:
  bootstrapToken:
    apiServerEndpoint: Master节点公网IP:6443
    token: t5a7cg.19va3edzrgbnldza
    unsafeSkipCAVerification: true
  tlsBootstrapToken: t5a7cg.19va3edzrgbnldza
  
# 加入集群
kubeadm join --config join-config.ymal

# 或者使用
kubeadm join MasterIP:6443 --token pw1mwc.e3cjvdsnhfc6nvmc --discovery-token-ca-cert-hash sha256:03d450c1455f3b72eccc1b7ddd3f2c0b6737e1f5492729de552dbd5c45a4781e
~~~

~~~bash
# 加入成功
This node has joined the cluster:
* Certificate signing request was sent to apiserver and a response was received.
* The Kubelet was informed of the new secure connection details.

Run 'kubectl get nodes' on the control-plane to see this node join the cluster.
~~~

自此 Node 已加入集群，但是没有容器网络的功能



### 4、初始化容器网络

#### 1、说明

必须部署一个基于 Pod 网络插件的容器网络接口 (CNI)，以便 Pod 可以相互通信

在安装网络插件之前，集群 DNS (CoreDNS) 将不会启动

Pod 网络不得与任何主机网络重叠

默认情况下，kubeadm 将集群设置为强制使用 RBAC，确认网络插件支持 RBAC

如果集群使用双栈协议，确认网络插件支持

每个集群只能安装一个 Pod 网络



**注意**：

- 以下插件二选一



#### 2、calico

~~~bash
# 下载配置文件
curl https://docs.projectcalico.org/manifests/calico.yaml -O
~~~

修改 name: CALICO_IPV4POOL_CIDR 的 value 为 Master 节点启动时的 --pod-network-cidr 参数值

在 CLUSTER_TYPE 参数的下方添加新配置，value 为 Master 节点的网卡名

~~~yml
- name: IP_AUTODETECTION_METHOD
	value: "interface=enp0s3"
~~~

~~~bash
# 安装 calico
kubectl apply -f calico.yaml
~~~

等待所有的 Pod 构建、运行完成

~~~bash
# 查看 Pod 状态
kubectl get pods -A

# 查看 节点状态
kubectl get nodes
~~~

卸载：

~~~bash
# Master 节点
kubectl delete -f calico.yaml

# 所有节点
modprobe -r ipip
# 停止所有生成的虚拟网卡
ifconfig xxx down
~~~



#### 3、flannel

~~~bash
# 下载 flannel 的 yaml 配置文件
wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
~~~

修改 net-conf.json 的 Network 为 Master 节点启动时的 --pod-network-cidr 参数值

~~~bash
# 安装
kubectl apply -f kube-flannel.yml
~~~



### 5、测试集群

~~~bash
# 启动一个 Nginx
kubectl create deployment nginx --image=nginx

# 暴露80端口
kubectl expose deployment nginx --port=80 --type=NodePort

# 查看 pod 与 svc 状态
kubectl get pod,svc

# 查看 pod 详细,顺便查看部署在哪个 Node 里了
kubectl describe pod podname
# 通过 Node IP:Port 访问，显示出 Nginx 界面即成功

# 进入容器,能显示出命令行即成功
kubectl exec nginx-748c667d99-94bsd -it --ns default /bin/bash
~~~



### 6、停止

任何节点都按此步骤

~~~bash
# 移除指定节点的 pod
kubectl drain nodename --delete-local-data --force --ignore-daemonsets
~~~

~~~bash
# 移除节点
kubectl delete nodes nodename
~~~

~~~bash
kubeadm reset -f
systemctl stop kubelet
systemctl stop docker
rm -rf /var/lib/cni/
rm -rf /var/lib/kubelet/*
rm -rf /etc/cni/
ifconfig cni0 down
ifconfig flannel.1 down
ifconfig docker0 down
ip link delete cni0
ip link delete flannel.1
systemctl start kubelet
systemctl start docker
~~~





# 3、kubernetes 资源清单

## 1、基本概念

Kubernetes 通过声明 yml 文件来解决资源管理和资源对象编排与部署，这种文件叫做资源清单文件，通过 kubectl 命令使用资源清单文件可以实现对大量的资源对象进行编排



## 2、常用字段

### 1、必须存在的属性

| 参数名                  | 字段类型 | 说明                                            |
| ----------------------- | -------- | ----------------------------------------------- |
| version                 | String   | K8S API 版本，使用 kubectl api-version 命令查询 |
| kind                    | String   | yml 文件定义的资源类型，比如：Pod               |
| metadata                | Object   | 元数据对象，固定值写 metadata                   |
| metadata.name           | String   | 元数据对象的名字，比如：Pod 的名字              |
| metadata.namespace      | String   | 元数据对象命名空间                              |
| Spec                    | Object   | 详细定义对象，固定值写 Spec                     |
| spec.containers[]       | list     | Spec 对象的容器列表定义                         |
| spec.containers[].name  | String   | 定义容器名字                                    |
| spec.containers[].image | String   | 定义用到的镜像名称                              |



### 2、Spec 对象

| 参数名                                      | 字段类型 | 说明                                                         |
| ------------------------------------------- | -------- | ------------------------------------------------------------ |
| spec.containers[].name                      | String   | 定义容器名字                                                 |
| spec.containers[].image                     | String   | 定义用到的镜像名称                                           |
| spec.containers[].imagePullPolicy           | String   | 定义镜像拉取策略，有 Always（默认）、Never、IfNotPresent 三个选项，Always：每次尝试重新拉去镜像，Never：仅使用本地镜像，IfNotPresent ：本地优先 |
| spec.containers[].command[]                 | List     | 指定容器启动命令，不指定则使用镜像打包时使用的启动命令       |
| spec.containers[].args[]                    | List     | 指定容器启动命令参数                                         |
| spec.containers[].workingDir                | String   | 指定容器工作目录                                             |
| spec.containers[].volumeMounts[]            | List     | 指定容器内部的存储卷配置                                     |
| spec.containers[]..volumeMounts[].name      | String   | 指定可以被容器挂载的存储卷的名称                             |
| spec.containers[]..volumeMounts[].mountPath | String   | 指定可以被容器挂载的存储卷的路径                             |
| spec.containers[].readOnly                  | String   | 设置存储卷的读写模式，true\false，默认为false                |
| spec.containers[].ports[]                   | List     | 指定容器需要用到的端口列表                                   |
| spec.containers[].ports[].name              | String   | 指定端口的名称                                               |
| spec.containers[].ports[].containerPort     | String   | 指定容器需要监听的端口号                                     |
| spec.containers[].ports[].hostPort          | String   | 指定容器所在主机需要监听的端口号，默认跟上面的 containerProt  相同，设置了 hostPort 同一台主机无法启动该容器的相同副本，因为主机端口号不能相同，会冲突 |
| spec.containers[].env[]                     | String   | 指定容器运行需要设置的环境变量列表                           |
| spec.containers[].env[].name                | String   | 指定环境变量名称                                             |
| spec.containers[].env[].value               | String   | 指定环境变量值                                               |
| spec.containers[].resources                 | Object   | 指定资源限制和资源请求的值，也即设置容器的资源上线           |
| spec.containers[].resources.limits          | Objcet   | 指定容器运行时资源的上限                                     |
| spec.containers[].resources.limits.cpu      | String   | 指定CPU限制，单位为 core 数，类似于 docker run --cpu-shares 参数 |
| spec.containers[].resources.limits.memory   | String   | 指定内存限制，单位为 MIB\GIB                                 |
| spec.containers[].resources.requests        | Object   | 指定容器启动和调度室的限制设置                               |
| spec.containers[].resources.requests.cpu    | String   | CPU 请求，单位为 core 数，容器启动时初始化可用数量           |
| spec.containers[].resources.requests.memory | String   | 内存请求，单位为 MIB\GIB 容器启动的初始化可用数量            |



### 3、额外参数

| 参数名                | 字段类型 | 说明                                                         |
| --------------------- | -------- | ------------------------------------------------------------ |
| spec.restartPolicy    | String   | 定义 Pod 重启策略，可选值为 Always、OnFailure、Never，默认值为 Always，Always：Pod 无论为终止，kubelet 都将重启它，OnFailure：容器非正常结束，kubelet 将重启它，Never Pod 终止后，kubelet 发出报告给 Master，并不重启它 |
| spec.nodSelector      | Object   | 定义 Node 的 Label 标签，以 key:value 格式指定               |
| spec.imagePullSecrets | Object   | 定义 pull 镜像时使用 secret 名称，以 name:secretkey 格式指定 |
| spec.hostNetWork      | Boolean  | 定义是否使用主机网络模式，默认为 false，设置 true 表示使用主机网络，不使用 docker 网桥，同时设置了 true 将无法再用一台主机上启动第二个副本 |



# 4、Kubernetes Pod

## 1、基本概念

Pod 是 Kubernetes 中可以创建和管理的最小单元，是资源对象模型中由用户创建或部署的最小资源对象模型，其他的资源对象都是用来支撑或者扩展 Pod 对象功能的，比如：控制器对象是用来管控 Pod 对象的、Service 或者 Ingress 资源对象是用来暴露 Pod 引用对象的，PersistentVolume 资源对象是用来为 Pod 提供存储等等

Kubernetes 只能直接处理 Pod，Pod 是由一个或多个 container 组成，每一个 Pod 都有一个特殊的被称为根容器的 Pause 容器，Pause 容器对应的镜像属于 Kubernetes 平台的一部分，除了 Pause 容器，每个 Pod 还包含一个或多个紧密相关的用户业务容器

<img src="images/image-20230107131244951.png" alt="image-20230107131244951" style="zoom: 33%;" />



## 2、特点

同一个 Pod 中的容器总会被调度到相同 Node 节点，不同节点间 Pod 的通信基于虚拟二层网络技术实现

Pod 又分为普通与静态



**资源共享**：

- 一个 Pod 里的多个容器可以共享存储和网络，可以看作一个逻辑的主机。共享的如 namespace、cgroups 或者其他的隔离资源
- 同一个 Pod 里的多个容器共享 Pod 的 IP 和 端口 namespace，所以一个 Pod 内的多个容器之间可以通过 localhost 来进行通信，所需要注意的是不同容器要注意不要有端口冲突即可
- 不同的 Pod 有不同的 IP，不同 Pod 内的容器之前通信，不可以使用 IPC（进程间通信，如果没有特殊指定的话），通常情况下使用 Pod 的 IP 进行通信
- 一个 Pod 里的多个容器可以共享存储卷，这个存储卷会被定义为 Pod 的一部分，并且可以挂载到该 Pod 里的所有容器的文件系统上
- 每个 Pod 中有一个 Pause 容器保存所有容器状态， 通过管理 Pause 容器，达到管理 Pod 中所有容器的效果



**生命周期短暂**：

- Pod 属于生命周期比较短暂的组件，比如：当 Pod 所在节点发生故障，那么该节点上的 Pod 会被调度到其他节点，但需要注意的是，被重新调度的 Pod 是一个全新的 Pod，跟之前的 Pod 毫无关系



**平坦的网络**：

- Kubernetes 集群中的所有 Pod 都在同一个共享网络地址空间中，也就是说每个 Pod 都可以通过其他 Pod 的 IP 地址来实现访问
- 每个 Pod 都是应用的一个实例，有专用的 IP



## 3、定义

配置文件（配置项尚未校对完毕）

~~~yaml
apiVersion: v1

kind: Pod

metadata: # 元数据
	name: string
	namespace: string
	labels:
		-name: string
		
annotations:
	-name: string
	
spec:
	containers: # Pod 中的容器列表，可以有多个容器
		- name: string # 容器的名称
		
		image: string # 容器中的镜像
		imagesPullPolicy: [Always|Never|IfNotPresent] # 获取镜像的策略，默认值为 Always，每次都尝试重新下载镜像
		
		command: [string] # 容器的启动命令列表（不配置的话使用镜像内部的命令） args: [string] 启动参数列表
		
		workingDir: string # 容器的工作目录 
		volumeMounts: # 挂载到到容器内部的存储卷设置
			- name: string
		mountPath: string # 存储卷在容器内部 Mount 的绝对路径 
		readOnly: boolean # 默认值为读写
		
		ports: # 容器需要暴露的端口号列表
			- name: string
		containerPort: int # 容器要暴露的端口
		hostPort: int # 容器所在主机监听的端口（容器暴露端口映射到宿主机的端口
		# 设置 hostPort 时同一台宿主机将不能再启动该容器的第 2 份副本）
		
		protocol: string # TCP 和 UDP，默认值为 TCP 
		env: # 容器运行前要设置的环境列表
			- name: string 
			  value: string
		
		resources:
			limits: # 资源限制，容器的最大可用资源数量 
				cpu: Srting
				memory: string
		requeste: # 资源限制，容器启动的初始可用资源数量 
			cpu: string
			memory: string
			
	livenessProbe: # pod 内容器健康检查的设置 
	exec:
		command: [string] # exec 方式需要指定的命令或脚本 
		
	httpGet: # 通过 httpget 检查健康
        path: string 
        port: number 
        host: string 
        scheme: Srtring 
        httpHeaders:
            - name: Stirng 
              value: string
	tcpSocket: # 通过 tcpSocket 检查健康
		port: number 
		initialDelaySeconds: 0 # 首次检查时间 
		timeoutSeconds: 0 # 检查超时时间
		periodSeconds: 0 # 检查间隔时间
		successThreshold: 0
		failureThreshold: 0 
		
	securityContext: # 安全配置
	privileged: falae
	restartPolicy: [Always|Never|OnFailure] # 重启策略，默认值为 Always
	nodeSelector: object # 节点选择，表示将该 Pod 调度到包含这些 label 的 Node 上，以 key:value 格式指定

	imagePullSecrets:
		-name: string
		
hostNetwork: false # 是否使用主机网络模式，弃用 Docker 网桥，默认否
volumes: # 在该 pod 上定义共享存储卷列表
	- name: string emptyDir: {} hostPath:
	  path: string 
secret:
	secretName: string 
	item:
		- key: string 
		  path: string
configMap: 
	name: string 
	items:
		- key: string
		  path: string
~~~



## 4、使用方法

在 Kubernetes 中对运行容器的要求为：容器的主程序需要一直在前台运行，而不是后台运行

因此应用需要改造成前台运行的方式，例如：创建的 Docker 镜像的启动命令是后台执行程序，则在 Kubelet 创建包含这个容器的 Pod 之 后运行完该命令，就认为 Pod 已经结束，将立刻销毁该 Pod，如果为该 Pod 定义了 RC，则会陷入一个无限循环创建、销毁的过程中



配置文件示例：

~~~yaml
# 一个容器组成的 Pod
apiVersion: v1 
kind: Pod 
metadata:
	name: mytomcat 
	labels:
		name: mytomcat 
spec:
	containers:
        - name: mytomcat 
        image: tomcat 
        ports:
        	- containerPort: 8000
~~~

~~~yaml
# 两个容器组成的 Pod
apiVersion: v1 
kind: Pod 
metadata:
	name: myweb 
	labels:
		name: tomcat-redis
spec:
	containers:
		- name: tomcat 
		image: tomcat 
		ports:
			- containerPort: 8080
			
		- name: redis 
		image: redis 
		ports:
			- containerPort: 6379
~~~

~~~bash
# 创建
kubectl creat -f xxx.yml
# 删除
kubectl delete -f pod xxx.yml
~~~



## 5、分类

Pod 有两种类型：普通和静态

**普通**：

- 普通 Pod 一旦被创建，就会被放入到 etcd 中存储，随后会被 Kubernetes Master 调度到某个具体的 Node 上并进行绑定，随后该 Pod 对应的 Node 上的 Kubelet 进程实例化成一组相关的容器并启动起来
- 在默认情况下，当 Pod 里某个容器停止时，Kubernetes 会自动检测到这个问题并且重新启动这个 Pod 里此类的所有容器， 如果 Pod 所在的 Node 宕机，则会将这个 Node 上的所有 Pod 重新调度到其它节点上

**静态**：

- 静态 Pod 是由 Kubelet 进行管理的仅存在于特定 Node 上的 Pod，它们不能通过 API Server 进行管理，无法与 ReplicationController、Deployment、DaemonSet 进行关联，并且 Kubelet 也无法对它们进行健康检查



## 6、生命周期

| 状态       | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| Pending    | API Server 已经创建了 Pod，但其中的容器镜像还未创建，包括镜像下载 |
| Running    | Pod 内所有容器已经创建，并且至少有一个容器处于运行中或启动中或重启中 |
| Compeleted | Pod 内所有容器均成功退出，并且不会重启                       |
| Failed     | Pod 内所有容器均退出，但至少有一个容器失败                   |
| Unknow     | 由于未知原因无法获取 Pod 状态，例如：网络通信不畅            |



## 7、重启策略

| 策略      | 说明                                                   |
| --------- | ------------------------------------------------------ |
| Always    | 当容器失效时，由 Kubelet 重启容器                      |
| OnFailure | 当容器非正确终止（退出码不为 0 ），由 Kubelet 重启容器 |
| Never     | 无论容器如何终止，都不重启                             |



## 8、状态转换

| 包含容器数 | 当前状态 | 发生事件        | 结果状态 |           | 结果状态结果状态 |
| ---------- | -------- | --------------- | -------- | --------- | ---------------- |
|            |          | **重启策略**    | Always   | OnFailure | Never            |
| 一个       | Running  | 容器成功终止    | Running  | Succeed   | Succeed          |
| 一个       | Running  | 容器失败终止    | Running  | Running   | Failed           |
| 多个       | Running  | 容器失败终止    | Running  | Running   | Running          |
| 多个       | Running  | 容器被 OOM 终止 | Running  | Running   | Failed           |



## 9、资源配置

可以对每个 Pod 能使用的计算资源限额

Kubernetes 中可以限额的计算资源有 CPU 与 Memory 两种

- CPU：资源单位为 CPU 数量，是一个绝对值而非相对值
- Memory：资源单位为内存字节数，配额也是一个绝对值

对一个计算资源进行限额需要设定以下两个参数： 

- Requests：该资源最小申请数量
- Limits：该资源最大允许使用的量

当容器试图使用超过 Limits 时，可能会被 Kubernetes Kill 并重启

~~~yaml
sepc:
	containers:
		- name: db
		image: mysql
		
		resources:
			requests: # 最小额度
				memory: "64Mi"
				cpu: "250m"
				
			limits: # 最大额度
				memory: "128Mi"
				cpu: "500m"
				
# MySQL 容器申请最少 0.25 个 CPU 以及 64MiB 内存
# 在运行过程中容器所能使用的资源配额为 0.5 个 CPU 以及 128MiB 内存
# m 表示千分位，250m = 0.25
~~~



# 5、Kubernetes Label

## 1、基本概念

一个 Label 是一个 **k : v** 键值对，key 和 value 均自定义

Label 可以附加到各种资源对象上，如 Node、Pod、 Service、RC，一个资源对象可以定义任意数量的 Label， 同一个 Label 也可以被添加到任意数量的资源对象上

Label 通常在资源对象定义时确定，也可以在对象创建后动态添加或删除

Label 的最常见的用法是使用 metadata.labels 字段，来为对象添加 Label，通过 spec.selector 来引用对象

Label 附加到 Kubernetes 集群中各种资源对象上，目的就是对这些资源对象进行分组管理， 而分组管理的核心就是 Label Selector

Label 与 Label Selector 都不能单独定义，必须附加在一些资源对象的定义文件上，一般附加在 RC 和 Service 的资源定义文件中

~~~yaml
apiVersion: v1
kind: ReplicationController 
metadata:
	name: nginx 
spec:
	replicas: 3 
	selector:
		app: nginx
		
	template:
        metadata:
            labels:
                app: nginx 
        spec:
            containers:
                - name: nginx 
                image: nginx 
                ports:
                    - containerPort: 80
-------------------------------------
apiVersion: v1 
kind: Service 
metadata: 
	name: nginx
spec:
	type: NodePort 
	ports:
		- port: 80
	nodePort: 3333 
	selector:
		app: nginx
~~~



# 6、Kubernetes Controller

## 1、Replication Controller

Replication Controller（RC）是 Kubernetes 系统中核心概念之一，当定义了一个 RC 并提交到 Kubernetes 集群中以后，Master 节点上的 Controller Manager 组件就得到通知，定期检查系统中存活的 Pod，并确保目标 Pod 实例的数量刚好等于 RC 的预期值，如果有过 多或过少的 Pod 运行，系统就会停掉或创建一些 Pod，此外可以通过修改 RC 的副本数量，来实现 Pod 的动态缩放功能

~~~bash
kubectl scale rc nginx --replicas=5
~~~

由于 Replication Controller 与 Kubernetes 代码中的模块 Replication Controller 同名， 所以在 Kubernetes v1.2 时， 它就升级成了另外一个新的概念 Replica Sets，官方解释为下一代的 RC，它与 RC 区别是：Replica Sets 支援基于集合的 Label selector，而 RC 只支持基于等式的 Label Selector

很少单独使用 Replica Set，它主要被 Deployment 这个更高层面的资源对象所使用，从而形成一整套 Pod 创建、删除、更新的编排机制

最好不要越过 RC 直接创建 Pod，因为 Replication Controller 会通过 RC 管理 Pod 副本，实现自动创建、补足、替换、删除 Pod 副本，这样就能提高应用的容灾能力，减少由于节点崩溃等意外状况造成的损失，即使应用程序只有一个 Pod 副本，也强烈建议使用 RC 来定 义 Pod



## 2、Replica Set 

Replica Set 跟 Replication Controller 没有本质的不同，只是名字不一样，并且 Replica Set 支持集合式的 Selector（Replication Controller 仅支持等式）

Kubernetes 官方强烈建议避免直接使用 Replica Set，而应该通过 Deployment 来创建 RS 和 Pod，由于 Replica Set 是 Replication Controller 的代替物，因此用法基本相同



## 3、Deployment

Deployment 是 Kubenetes v1.2 引入的新概念，引入的目的是为了更好的解决 Pod 的编排问题

Deployment 内部使用了 Replica Set 来实现，Deployment 的定义与 Replica Set 的 定义很类似，除了 API 声明与 Kind 类型有所区别

~~~yaml
apiVersion: extensions/v1beta1 
kind: Deployment
metadata:
	name: frontend 
spec:
	replicas: 1 
	selector:
		matchLabels:
			tier: frontend 
			matchExpressions:
				- {key: tier, operator: In, values: [frontend]} 
template:
    metadata:
        labels:
            app: app-demo 
            tier: frontend
    spec:
        containers:
            - name: tomcat-demo 
            image: tomcat 
            ports:
                - containerPort: 8080
~~~



## 4、Horizontal Pod Autoscaler

Horizontal Pod Autoscal（Pod 横向扩容简称 HPA）与 RC、Deployment 一样，也属于一种 Kubernetes 资源对象

通过追踪分析 RC 控制的所有目标 Pod 的负载变化情况，来确定是否需要针对性地调整目标 Pod 的副本数，这是 HPA 的实现原理

Kubernetes 对 Pod 扩容与缩容提供了手动和自动两种模式，手动模式通过 kubectl scale 命令对一个 Deployment/RC 进行 Pod 副本数量的设置，自动模式则需要用户根据某个性能指标或者自定义业务指标，并指定 Pod 副本数量的范围，系统将自动在这个范围内根据性 能指标的变化进行调整

**手动扩容和缩容**：

~~~bash
kubectl scale deployment frontend --replicas 1
~~~

**自动扩容和缩容**：

HPA 控制器基本 Master 的 kube-controller-manager 服务启动参数 --horizontal-podautoscaler-sync-period 定义的时长（默认值为 30s），周期性地监测 Pod 的 CPU 使用率， 并在满足条件时对 RC 或 Deployment 中的 Pod 副本数量进行调整，以符合用户定义的平均 Pod CPU 使用率

~~~yaml
apiVersion: extensions/v1beta1 
kind: Deployment
metadata:
	name: nginx-deployment 
spec:
	replicas: 1 
template:
	metadata: 
		name: nginx 
		labels:
			app: nginx 
	spec:
		containers:
			- name: nginx 
			image: nginx
		resources:
			requests:
				cpu: 50m 
		ports:
			- containerPort: 80
-------------------------------
apiVersion: v1 
kind: Service 
metadata:
	name: nginx-svc 
spec:
	ports:
		- port: 80 
	selector:
		app: nginx
-----------------------------------
apiVersion: autoscaling/v1 
kind: HorizontalPodAutoscaler 
metadata:
	name: nginx-hpa 
spec:
	scaleTargetRef:
		apiVersion: app/v1beta1 
		kind: Deployment
		name: nginx-deployment 
		minReplicas: 1
		maxReplicas: 10
		targetCPUUtilizationPercentage: 50
~~~



# 7、Kubernetes Volume

## 1、基本概念

Volume 是 Pod 中能够被多个容器访问的共享目录

Kubernetes 的 Volume 定义在 Pod 上， 它被一个 Pod 中的多个容器挂载到具体的文件目录下

Volume 与 Pod 的生命周期相同， 但与容器的生命周期不相关，当容器终止或重启时，Volume 中的数据也不会丢失

要使用 Volume，Pod 需要指定 Volume 的类型、内容（字段）、映射到容器的位置（字段）

Kubernetes 支持多种类型的 Volume，包括：emptyDir、hostPath、gcePersistentDisk、awsElasticBlockStore、nfs、iscsi、flocker、glusterfs、rbd、cephfs、gitRepo、secret、persistentVolumeClaim、downwardAPI、azureFileVolume、azureDisk、 vsphereVolume、Quobyte、PortworxVolume、ScaleIO



## 2、emptyDir 

emptyDir 类型的 Volume 创建于 Pod 被调度到某个宿主机上的时候，而同一个 Pod 内的容器都能读写 emptyDir 中的同一个文件，一旦这个 Pod 离开了这个宿主机，emptyDir 中的数据就会被永久删除，所以目前 emptyDir 类型的 Volume 主要用作临时空间，比如：Web 服务器写日志或者 tmp 文件需要的临时目录

~~~yaml
apiVersion: v1 
kind: Pod 
metadata:
	name: test-pd 
spec:
	containers:
		- image: docker.io/nazarpc/webserver
	name: test-container
	volumeMounts:
		- mountPath: /cache 
		name: cache-volume
	volumes:
		- name: cache-volume 
		emptyDir: {}
~~~





## 3、hostPath 

hostPath 属性的 Volume 使得对应的容器能够访问当前宿主机上的指定目录，例如：需要运行一个访问 Docker 系统目录的容器，那么就使用 /var/lib/docker 目录作为一个 hostDir 类型的 Volume，或者要在一个容器内部运行 CAdvisor，那么就使用 /dev/cgroups 目录作为一个 hostDir 类型的 Volume

一旦这个 Pod 离开了这个宿主机，hostDir 中的数据虽然不会被永久删除，但数据也不会随 Pod 迁移到其他宿主机上，因此需要注意的是， 由于各个宿主机上的文件系统结构和内容并不一定完全相同，所以相同 Pod 的 hostDir 可能会在不同的宿主机上表现出不同的行为

~~~yaml
apiVersion: v1 
kind: Pod 
metadata:
	name: test-pd 
spec:
	containers:
		- image: docker.io/nazarpc/webserver 
	name: test-container
	# 指定在容器中挂接路径
	volumeMounts:
		- mountPath: /test-pd 
		name: test-volume
	# 指定所提供的存储卷
	volumes:
		- name: test-volume 
		# 宿主机上的目录 
		hostPath:
			# directory location on host 
			path: /data
~~~



## 3、nfs 

nfs 类型的 Volume 允许一块现有的网络硬盘在同一个 Pod 内的容器间共享

~~~yaml
apiVersion: apps/v1  # for versions before 1.9.0 use apps/v1beta2 
kind: Deployment
metadata:
	name: redis 
spec:
	selector: 
		matchLabels:
			app: redis 
	revisionHistoryLimit: 2 
template:
	metadata:
		labels:
			app: redis 
	spec:
		containers:
			# 应用的镜像
			-image: redis 
			name: redis
			imagePullPolicy: IfNotPresent 
		ports: # 应用的内部端口
			- containerPort: 6379 
			name: redis6379
		env:
			- name: ALLOW_EMPTY_PASSWORD
			value: "yes"
			- name: REDIS_PASSWORD
			value: "redis"
		# 持久化挂接位置，在 docker 中
		volumeMounts:
			- name: redis-persistent-storage 
			mountPath: /data
		volumes:
			# 宿主机上的目录
			- name: redis-persistent-storage 
			nfs:
				path: /k8s-nfs/redis/data 
				server: 192.168.126.112
~~~



# 8、Kubernetes PVC\PV

## 1、基本概念

如何管理存储是管理计算的一个问题，而 PersistentVolume 子系统为用户和管理员提供了一个 API，用于抽象根据消费方式提供存储的详细信息

为此引入了两个新的 API 资源：PersistentVolume 和 PersistentVolumeClaim

- PersistentVolume（PV）：集群中由管理员配置的一段网络存储，是集群中的资源，就像节点是集群资源一样，PV 是容量插件，如 Volumes，但其生命周期独立于使用 PV 的任何单个 Pod，此 API 对象捕获存储实现的详细信息，包括 NFS，iSCSI 或特定于云提供程序的存储系统
- PersistentVolumeClaim（PVC）：由用户进行存储的请求，它类似于 Pod，Pod 消耗节点资源，PVC 消耗 PV 资源，Pod 可以请求特定级别的资源（CPU 和内存），PVC 可以请求特定的大小和访问模式（一次读写、多次只读）

虽然 PVC 允许用户使用抽象存储资源，但是 PV 对于不同的问题，用户通常需要具有不同属性（例如：性能）

集群管理员需要提供各种 PV，而不仅仅是大小和访问模式，并且不会让用户了解这些卷的实现方式，对于这些需求，需要使用 StorageClass 资源，StorageClass 为管理员提供了一种描述他们提供的存储类的方法

不同的类可能映射到服务质量级别，或备份策略，或者由群集管理员确定的任意策略，Kubernetes 本身对于什么类别代表是不言而喻的，这个概念有时在其他存储系统中称为配置文件



**注意**：

- PVC 和 PV 是一一对应的



## 2、生命周期

PV 是群集中的资源，PVC 是对这些资源的请求，并且还充当对资源的检查

PV 和 PVC 之间的相互作用遵循以下生命周期：

- **Provisioning** ——-> **Binding** ——–> **Using** ——> **Releasing** ——> **Recycling** 

  - Provisioning：供应准备，通过集群外的存储系统或者云平台来提供存储持久化支持

    - Static：静态提供，集群管理员创建多个 PV，它们携带可供集群用户使用的真实存储的详细信息，它们存在于 Kubernetes API 中，可用于消费

    - Dynamic：动态提供，当管理员创建的静态 PV 都不匹配用户的 PVC 时，集群可能会尝试为 PVC 动态配置卷，此配置基于  StorageClasses，PVC 必须请求一个类，并且管理员必须已创建并配置该类才能进行动态配置，要求该类的声明有效地为自己禁用动态配置

  - Binding：绑定，用户创建 PVC 并指定需要的资源和访问模式，在找到可用 PV 之前，PVC 会保持未绑定状态
  - Using：使用，用户可在 Pod 中像 Volume 一样使用 PVC
  - Releasing：释放，用户删除 PVC 来回收存储资源，PV 将变成 released 状态，由于还保留着之前的数据，这些数据需要根据不同的策略来处理，否则这些存储资源无法被其他 PVC 使用
  - Recycling：回收，PV 可以设置三种回收策略：保留（Retain）、回收（Recycle）、删除 （Delete）
    - 保留策略：允许人工处理保留的数据
    - 删除策略：将删除 PV 和外部关联的存储资源，需要插件支持
    - 回收策略：将执行清除操作，之后可以被新的 PVC 使用，需要插件支持

 

## 3、PV 类型

~~~tex
GCEPersistentDisk
AWSElasticBlockStore
AzureFile
AzureDisk
FC (Fibre Channel)
Flexvolume
Flocker
NFS
iSCSI
RBD (Ceph Block Device)
CephFS
Cinder (OpenStack block storage)
Glusterfs
VsphereVolume
Quobyte Volumes
HostPath (Single node testing only – local storage is not supported in any
way and WILL NOT WORK in a multi-node cluster)
Portworx Volumes
ScaleIO Volumes
StorageOS
~~~



## 4、PV 卷阶段状态

Available：资源尚未被 claim 使用 

Bound：卷已经被绑定到 claim 了 

Released：claim 被删除，卷处于释放状态，但未被集群回收

Failed：卷自动回收失败



## 5、例子

### 1、创建 PV

~~~yaml
# 创建 5 个 pv，存储大小各不相同，是否可读也不相同
apiVersion: v1
kind: PersistentVolume
metadata:
	name: pv001
	labels:
		name: pv001
spec:
	nfs:
		path: /data/volumes/v1
	server: nfs
	accessModes: ["ReadWriteMany","ReadWriteOnce"]
	capacity:
		storage: 2Gi
---
apiVersion: v1
kind: PersistentVolume
metadata:
	name: pv002
	labels:
		name: pv002
spec:
	nfs:
		path: /data/volumes/v2
	server: nfs
	accessModes: ["ReadWriteOnce"]
	capacity:
		storage: 5Gi
---
apiVersion: v1
kind: PersistentVolume
metadata:
	name: pv003
	labels:
		name: pv003
spec:
	nfs:
		path: /data/volumes/v3
	server: nfs
	accessModes: ["ReadWriteMany","ReadWriteOnce"]
	capacity:
		storage: 20Gi
---
apiVersion: v1
kind: PersistentVolume
metadata:
	name: pv004
	labels:
		name: pv004
spec:
	nfs:
		path: /data/volumes/v4
	server: nfs
	accessModes: ["ReadWriteMany","ReadWriteOnce"]
	capacity:
		storage: 10Gi
---
apiVersion: v1
kind: PersistentVolume
metadata:
	name: pv005
	labels:
		name: pv005
spec:
	nfs:
		path: /data/volumes/v5
	server: nfs
	accessModes: ["ReadWriteMany","ReadWriteOnce"]
	capacity:
    	storage: 15Gi
~~~

~~~bash
# 创建
kubectl apply -f pv-damo.yaml

# 查询
kubectl get pv
~~~



### 2、创建 PVC

~~~yaml
# 创建一个 pvc，需要 6G 存储；所以不会匹配 pv001、pv002、pv003
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
	name: mypvc
	namespace: default
spec:
	accessModes: ["ReadWriteMany"]
	resources:
		requests:
			storage: 6Gi
---
apiVersion: v1
kind: Pod
metadata:
	name: vol-pvc
	namespace: default
spec:
	volumes:
		- name: html
		persistentVolumeClaim:
			claimName: mypvc
	containers:
		- name: myapp
		image: ikubernetes/myapp:v1
        volumeMounts:
            - name: html
            mountPath: /usr/share/nginx/html/
~~~

~~~bash
# 创建
kubectl apply -f vol-pvc-demo.yaml

# 查询
kubectl get pvc, pv
~~~



# 9、Kubernetes Secret

## 1、基本概念

Secret 解决了密码、token、密钥等敏感数据的配置问题，不需要把这些敏感数据暴露到镜像或者 Pod Spec 中

Secret 以 Volume 或者环境变量的方式使用

Secret 有三种类型：

- **Service Account**：
  - 用来访问 Kubernetes API，由 Kubernetes 自动创建
  - 自动挂载到 Pod 的 /run/secrets/kubernetes.io/serviceaccount 目录
- **Opaque**：
  - base64 编码格式的 Secret
  - 用来存储密码、密钥等
- **kubernetes.io/dockerconfigjson**：
  - 用来存储私有 docker registry 的认证信息



## 2、Service Account

~~~bash
kubectl run nginx --image nginx
# deployment "nginx" created

kubectl get pods
# NAME READY STATUS RESTARTS AGE
# nginx-3137573019-md1u2 1/1 Running 0 13s

kubectl exec nginx-3137573019-md1u2 ls
# /run/secrets/kubernetes.io/serviceaccount
# ca.crt
# namespace
# token
~~~



## 3、Opaque Secret

创建说明：Opaque 类型的数据是一个 map 类型，要求 value 是 base64 编码格式

~~~bash
# 转码到 base64
echo -n "admin" | base64
YWRtaW4=

echo -n "1f2d1e2e67df" | base64
MWYyZDFlMmU2N2Rm:
~~~

~~~yaml
# 创建 secrets.yml
apiVersion: v1
kind: Secret
metadata:
	name: mysecret
	type: Opaque
	data:
		password: MWYyZDFlMmU2N2Rm
		username: YWRtaW4=
~~~

~~~yaml
# 使用方法
# 挂载到 volume
apiVersion: v1
kind: Pod
metadata:
	labels:
		name: seret-test
		name: seret-test
spec:
	volumes:
		- name: secrets
	secret:
		secretName: mysecret
	containers:
		-image: hub.atguigu.com/library/myapp:v1
		name: db
		volumeMounts:
			- name: secrets
			mountPath:"
		readOnly: true

# 导出到环境变量
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
	name: pod-deployment
spec:
	replicas: 2
	template:
		metadata:
			labels:
				app: pod-deployment
		spec:
			containers:
				- name: pod-1
				image: hub.atguigu.com/library/myapp:v1
				ports:
					-containerPort: 80
				env:
					-name: TEST_USER
				valueFrom:
					secretKeyRef:
						name: mysecret
						key: username
~~~



## 4、kubernetes.io/dockerconfigjson

使用 Kuberctl 创建 docker registry 认证的 secret

~~~bash
kubectl create secret \
docker-registry myregistrykey \
--docker-server=DOCKER_REGISTRY_SERVER \
--docker-username=DOCKER_USER \
--docker-password=DOCKER_PASSWORD \
--docker-email=DOCKER_EMAIL \
secret "myregistrykey" created .
~~~

在创建 Pod 的时候，通过 imagePullSecrets 来引用刚创建的 myregistrykey

~~~yaml
apiVersion: v1
kind: Pod
metadata:
	name: foo
spec:
    containers:
        - name: foo
        image: roc/awangyang:v1
        imagePullSecrets:
        	-name: myregistrykey
~~~



# 10、Kubernetes configMap

## 1、基本概念

configMap 功能在 v1.2 版本中引入

许多应用程序会从配置文件、命令行参数、环境变量中读取配置信息，configMap API 提供了向容器中注入配置信息的机制

configMap 可以被用来保存单个属性，也可以用来保存整个配置文件或者 JSON 二进制大对象



## 2、创建方式

### 1、使用目录创建

~~~bash
ls docs/user-guide/configmap/kubectl/
# game.properties
# ui.properties

cat docs/user-guide/configmap/kubectl/game.properties
# enemies=aliens
# lives=3
# enemies.cheat=true
# enemies.cheat.level=noGoodRotten
# secret.code.passphrase=UUDDLRLRBABAS
# secret.code.allowed=true
# secret.code.lives=30

cat docs/user-guide/configmap/kubectl/ui.properties
# color.good=purple
# color.bad=yellow
# allow.textmode=true
# how.nice.to.look=fairlyNice

kubectl create configmap game-config --from-file=docs/user-guide/configmap/kubectl
~~~

--from-file 指定目录下的所有文件，都会被读取然后在 configMap 里面创建一个键值对，键的名字就是文件名，值就是文件的内容



### 2、使用文件创建

只要 --from-file 指定一个文件，就可以从单个文件中创建 ConfigMap

~~~bash
kubectl create configmap game-config-2 --from-file=docs/user- guide/configmap/kubectl/game.properties

kubectl get configmaps game-config-2 -o yaml
~~~

--from-file 这个参数可以使用多次，使用两次分别指定上个创建方式中的两个配置文件，效果就跟指定整个目录是一样的



### 3、使用字面量创建

使用字面量创建，利用 -from-literal 参数传递配置信息，该参数可以使用多次

~~~bash
kubectl create configmap special-config \
--from-literal=special.how=very \
--from-literal=special.type=charm

kubectl get configmaps special-config -o yaml
~~~



## 3、使用

### 1、替换环境变量

~~~yaml
apiVersion: v1
kind: ConfigMap
metadata:
	name: special-config
	namespace: default
	data:
		special.how: very
		special.type: charm

apiVersion: v1
	kind: ConfigMap
	metadata:
		name: env-config
		namespace: default
		data:
			log_level: INFO

apiVersion: v1
	kind: Pod
	metadata:
		name: dapi-test-pod
	spec:
		containers:
			- name: test-container
			image: hub.xxx.com/library/myapp:v1
			command: [ "/bin/sh", "-c", "env"]
		env:
			- name: SPECIAL_LEVEL_KEY
			valueFrom:
				configMapKeyRef:
                	name: special-config
					key: special.how
			- name: SPECIAL_TYPE_KEY
			valueFrom:
				configMapKeyRef:
					name: special-config
					key: special.type
			envFrom:
				- configMapRef:
				name: env-config
restartPolicy: Never
~~~



### 2、设置命令行参数

~~~yaml
apiVersion: v1
kind: ConfigMap
metadata:
	name: special-config
	namespace: default
data:
    special.how: very
    special.type: charm
    
apiVersion: v1
kind: Pod
metadata:
	name: dapi-test-pod
spec:
	containers:
		- name: test-container
		image: hub.xxx.com/library/myapp:v1
		command: ['bin/sh', '-c', 'echo ${SPECIAL_LEVEL_KEY} ${SPECIAL_TYPE_KEY}']
		env:
			- name: SPECIAL_LEVEL_KEY
			valueFrom:
				configMapKeyRef:
					name: special-config
					key: special.how
            - name: SPECIAL_TYPE_KEY
			valueFrom:
				configMapKeyRef:
					name: special-config
					key: special.type
restartPolicy: Never
~~~



### 3、在数据卷中使用

~~~yaml
apiVersion: v1
kind: ConfigMap
metadata:
	name: special-config
	namespace: default
data:
    special.how: very
    special.type: charm
    
apiVersion: v1
kind: Pod
metadata:
	name: dapi-test-pod
spec:
	containers:
		- name: test-container
		image: hub.xxx.com/library/myapp:v1
		command: ['bin/sh', '-c', 'cat /etc/config/special.how']
		volumeMounts:
			- name: config-volume
			mountPath: /etc/config
		volumes:
			- name: config-volume
			configMap:
				name: special-config
~~~



## 4、热更新

~~~bash
# 使用自带的默认编辑器直接修改即可
kubectl edit configmap log-config
~~~

更新 configMap 目前并不会触发相关 Pod 的滚动更新，可以通过修改 pod annotations 的方式触发滚动更新，例如：给 spec.template.metadata.annotations 中添加 version/config 触发滚动更新



# 10、Kubernetes Namspace

## 1、基本概念





# 扩展

## 1、OCI

OCI 全称 Open Container Initiative，旨在为容器格式和运行时，构建开放的行业标准

OCI 规范了容器的配置、执行环境、生命周期管理，为此 OCI 在制定之初提出了以下5个理念：

- 操作标准化：
  - 对容器整个生命周期进行标准化，包括：创建、启动、停止、创建快照、暂停、恢复等操作，规范每个操作的具体含义，将容器的具体操作进行原子化规范
- 内容无关：
  - 不管具体容器内容是什么，容器标准操作执行后能产生同样的效果，如容器可以用同样的方式上传、启动，不管是PostgreSQL还是MySQL数据库服务
- 基础设施无关：
  - 容器可以运行在任何支持 OCI 的基础设施上运行
- 为自动化而生：
  - 由于容器的标准操作与基础设施无关，可以更好的进行自动化管理
- 工业级交付：
  - 容器标准化能够使软件应用的分发可以达到工业级的交付，因为标准容器使得构建自动化的软件交付流水线成为现实

OCI 标准目前包含两部分内容：（说白了 OCI 就是真正规定怎么干活的）

- 容器运行时规范： 定义了如何根据相应的配置构建容器运行时
- 容器镜像规范： 定义了容器运行时使用的镜像打包规范

由 Docker 开源出来的 runc 就是 OCI 的实现标准之一

<img src="images/image-20230107135634040.png" alt="image-20230107135634040" style="zoom:50%;" />





## 2、CRI

CRI 全称 Container Runtime Interface （容器运行时接口），Kubernetes 定义的接口，使得上层应用无需编译就可以支持多种容器运行时的接口，并且可以通过插件切换适配不同封装的容器

<img src="images/image-20230107133613400.png" alt="image-20230107133613400" style="zoom:70%;" />





# 问题

## 1、安装三件套报错 Nux.Ro RPMs 无法连接到

进入到 /etc/yum.repos.d/ 下，删除 nux-dextop.repo 即可



## 2、Kubeadm 初始化配置文件拉去所需镜像报错

~~~bash
failed to pull image "registry.k8s.io/kube-apiserver:v1.26.0": output: E0101 15:59:19.912124 1680941 remote_image.go:222] "PullImage from image service failed" err="rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.ImageService" image="registry.k8s.io/kube-apiserver:v1.26.0"
time="2023-01-01T15:59:19+08:00" level=fatal msg="pulling image: rpc error: code = Unimplemented desc = unknown service runtime.v1alpha2.ImageService"
, error: exit status 1
To see the stack trace of this error execute with --v=5 or higher
~~~

原因可能是 containerd 配置文件未将 cri 从 disabled_plugins 移除，并添加 SystemdCgroup，正确配置后重启即可





## 3、Kubelet 启动失败

~~~bash
kubelet.service - kubelet: The Kubernetes Node Agent
   Loaded: loaded (/usr/lib/systemd/system/kubelet.service; disabled; vendor preset: disabled)
  Drop-In: /usr/lib/systemd/system/kubelet.service.d
           └─10-kubeadm.conf
   Active: activating (auto-restart) (Result: exit-code) since Sun 2023-01-01 16:06:31 CST; 9s ago
     Docs: https://kubernetes.io/docs/
  Process: 2347095 ExecStart=/usr/bin/kubelet $KUBELET_KUBECONFIG_ARGS $KUBELET_CONFIG_ARGS $KUBELET_KUBEADM_ARGS $KUBELET_EXTRA_ARGS (code=exited, status=1/FAILURE)
 Main PID: 2347095 (code=exited, status=1/FAILURE)
~~~

有可能是 Docker 的 Driver 与 Kubelet  不匹配所致

~~~bash
# 查看 Docker Driver
docker info|grep Driver

# 查看 Kubelet Driver
systemctl show --property=Environment kubelet |cat
~~~

有可能是 swap 未关闭

~~~bash
swapoff -a
~~~



## 4、Node NotReady

网络插件已安装，节点已加入

查看节点 kubelet 报错信息

~~~bash
journalctl -xeu kubelet
~~~

~~~bash
Jan 02 16:14:43 instance-20220501-1117 kubelet[48772]: E0102 16:14:43.098605   48772 kubelet.go:2475] "Container runtime network not ready" networkReady="NetworkReady=false reason:NetworkPluginNotReady message:Network plugin returns error: cni plugin not initialized"
~~~

~~~bash
# 重启 containerd 即可
systemctl daemon-reload
systemctl restart containerd
~~~

