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

Kubectl 是 Kubernetes 命令行工具，可以让用户对 Kubernetes 集群运行命令

可以使用 kubectl 来部署应用、监测和管理集群资源以及查看日志

**注意**：

- Kubectl 版本和集群版本之间的差异必须在一个小版本号内



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



### 2、安装要求

**硬件配置**：

- 2GB 或更多 RAM，2 个 CPU 或更多 CPU，硬盘 30GB 或更多

**网络配置**：

- 集群中所有机器之间网络互通

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



### 2、初始化 Master

~~~bash
# 启动 Master 节点
# 
kubeadm init \
--apiserver-advertise-address=Master公网IP \
--kubernetes-version v1.26.0 \
--service-cidr=10.1.0.0/16 \
--pod-network-cidr=10.244.0.0/16
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
mkdir -p $HOME/.kube

cp -i /etc/kubernetes/admin.conf $HOME/.kube/config

chown $(id -u):$(id -g) $HOME/.kube/config

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
    token: xxxxx
    unsafeSkipCAVerification: true
  tlsBootstrapToken: xxxxx
  
# 加入集群
kubeadm join --config join-config.ymal
~~~

~~~bash
# 加入成功
This node has joined the cluster:
* Certificate signing request was sent to apiserver and a response was received.
* The Kubelet was informed of the new secure connection details.

Run 'kubectl get nodes' on the control-plane to see this node join the cluster.
~~~



# 3、kubernetes 核心技术



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

