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

#### 1、Master

**Master Node**：集群控制节点，对集群进行调度管理，接受集群外用户的集群操作请求，Master Node 由 API Server、Scheduler、ClusterState Store（ETCD 数据库）和 Controller MangerServer 所组成，拥有 Master 组件

**Master 组件**：是集群的控制平台（control plane）：负责集群中的全局决策（例如，调度），探测并响应集群事件（例如，当 Deployment 的实际 Pod 副本数未达到 replicas 字段的规定时，启动一个新的 Pod）

Master 组件可以运行于集群中的任何机器上，但是通常在同一台机器上运行所有的 Master 组件，且不在此机器上运行用户的容器

Master 组件：

- **kube-apiserver**：
  - 提供 Kubernetes API，Kubernetes控制平台的前端（front-end）可以水平扩展（通过部署更多的实例以达到性能要求）
  - kubectl / kubernetes dashboard 等 Kubernetes 管理工具就是通过 kubernetes API 实现对 Kubernetes 集群的管理
- **etcd**：

  - 支持一致性和高可用的名值对存储组件，Kubernetes集群的所有配置信息都存储在 etcd 中
- **kube-scheduler**：

  - 监控所有新创建尚未分配到节点上的 Pod，并且自动选择为 Pod 选择一个合适的节点去运行
  - 影响调度的因素有：
    - 单个或多个 Pod 的资源需求
    - 硬件、软件、策略的限制
    - 亲和与反亲和（affinity and anti-affinity）的约定
    - 数据本地化要求
    - 工作负载间的相互作用
- **kube-controller-manager**：

  - 运行了所有的控制器，逻辑上来说，每一个控制器是一个独立的进程，但是这些控制器都被合并运行在一个进程里
  - 包含的控制器有：
    - 节点控制器： 负责监听节点停机的事件并作出对应响应（下文 Node 篇有简要介绍）
    - 副本控制器： 负责为集群中每一个 副本控制器对象（Replication Controller Object）维护期望的 Pod 副本数
    - 端点（Endpoints）控制器：负责为端点对象（Endpoints Object，连接 Service 和 Pod）赋值
    - Service Account & Token控制器： 负责为新的名称空间创建 default Service Account 以及 API Access Token
- **cloud-controller-manager**：

  - 运行了与具体云基础设施供应商互动的控制器，这是 Kubernetes 1.6 版本中引入的特性

  - 只运行特定于云基础设施供应商的控制器，使得云供应商的代码和 Kubernetes 的代码可以各自独立的演化，在此之前的版本中，Kubernetes 的核心代码是依赖于云供应商的代码的，在后续的版本中，特定于云供应商的代码将由云供应商自行维护，并在运行 Kubernetes 时链接到 cloud-controller-manager

  - 包含的云供应商相关的依赖：

    - 节点控制器：当某一个节点停止响应时，调用云供应商的接口，以检查该节点的虚拟机是否已经被云供应商删除
      - 私有化部署 Kubernetes 时，由于不知道运行节点的系统是否被删除，所以在移除系统后，要自行通过 kubectl delete node 将节点对象从 Kubernetes 中删除

    - 路由控制器：在云供应商的基础设施中设定网络路由
      - 私有化部署 Kubernetes 时，需要自行规划 Kubernetes 的拓扑结构，并做好路由配置
    - 服务（Service）控制器：创建、更新、删除云供应商提供的负载均衡器
      - 私有化部署 Kubernetes 时，不支持 LoadBalancer 类型的 Service，如需要此特性，需要创建 NodePort 类型的 Service，并自行配置负载均衡器
    - 数据卷（Volume）控制器：创建、绑定、挂载数据卷，并协调云供应商编排数据卷
      - 私有化部署 Kubernetes 时，需要自行创建和管理存储资源，并通过 Kubernetes 的存储类、存储卷、数据卷等关联



#### 2、Node

##### 1、Node 组件

**Worker Node**：集群工作节点，运行用户业务应用容器，Worker Node 包含 Node 组件例如：kubelet、kube proxy 和 ContainerRuntime

**Node 组件**：Node 组件运行在每一个节点上（包括 Master 节点和 Worker 节点）负责维护运行中的 Pod 并提供 Kubernetes 运行时环境

- **kubelet**：
  - 运行在每一个集群节点上的代理程序，确保 Pod 中的容器处于运行状态
  - 其通过多种途径获得 PodSpec 定义，并确保 PodSpec 定义中所描述的容器处于运行和健康的状态
  - 注意：Kubelet 不管理不是通过 Kubernetes 创建的容器
- **kube-proxy**：
  - 网络代理程序，运行在集群中的每一个节点上，是实现 Kubernetes Service 概念的重要部分
  - 其在节点上维护网络规则，这些网络规则使得用户可以在集群内、集群外正确地与 Pod 进行网络通信
  - 如果操作系统中存在 packet filtering layer，kube-proxy 将使用这一特性（iptables代理模式），否则 kube-proxy 将自行转发网络请求（User space 代理模式）
- **容器运行时**：
  - 负责运行容器
  - Kubernetes 支持多种容器引擎：
    - Docker
    - containerd
    - cri-o
    - rktlet
    - 以及任何实现了 Kubernetes 容器引擎接口的容器运行时



##### 2、Node 信息

每个 Node 都有自身的状态，Node 状态包含如下信息：（这些信息由节点上的 kubelet 收集）

- **Addresses**：

  - 依据集群部署的方式（在哪个云供应商部署，或是在物理机上部署）Addesses 字段可能有所不同
    - HostName： 在节点执行 hostname 命令所获得的值，启动 kubelet 时，可以通过参数 --hostname-override 覆盖
    - ExternalIP：通常是节点的外部 IP，可以从集群外访问的内网 IP 地址，在集群搭建篇的例子中，为空值
    - InternalIP：通常是从节点内部可以访问的 IP 地址

- **Conditions**：

  - 描述了节点的状态，Node Condition 以一个 JSON 对象的形式存在

    | Node Condition    | 描述                                                         |
    | ----------------- | ------------------------------------------------------------ |
    | OutOfDisk         | 如果节点上的空白磁盘空间不够，不能够再添加新的节点时，该字段为 True，其他情况为 False |
    | Ready             | 如果节点是健康的且已经就绪可以接受新的 Pod，则节点Ready字段为 True，False 表明了该节点不健康，不能够接受新的 Pod |
    | MemoryPressure    | 如果节点内存紧张，则该字段为 True，否则为 False              |
    | PIDPressure       | 如果节点上进程过多，则该字段为 True，否则为 False            |
    | DiskPressure      | 如果节点磁盘空间紧张，则该字段为 True，否则为 False          |
    | NetworkUnvailable | 如果节点的网络配置有问题，则该字段为 True，否则为 False      |

    ~~~json
    // 例子
    "conditions": [
      {
        "type": "Ready",
        "status": "True",
        "reason": "KubeletReady",
        "message": "kubelet is posting ready status",
        "lastHeartbeatTime": "2019-06-05T18:38:35Z",
        "lastTransitionTime": "2019-06-05T11:41:27Z"
      }
    ]
    ~~~

    - 如果 Ready 类型的 Condition 的 status 字段持续为 Unkown 或者 False 超过 pod-eviction-timeout（kube-controller-manager (opens new window)的参数）所指定的时间，节点控制器（node controller）将对该节点上的所有 Pod 执行删除的调度动作，默认的 pod-eviction-timeout 时间是 5 分钟
    - 某些情况下（例如：节点网络故障）apiserver 不能够与节点上的 kubelet 通信，删除 Pod 的指令不能下达到该节点的 kubelet 上，直到 apiserver 与节点的通信重新建立，删除指令才下达到节点，这也就导致虽然对 Pod 执行了删除的调度指令，但是这些 Pod 仍然在失联的节点上运行，因此可能会发现失联节点上的 Pod 仍然在运行（在该节点上执行 docker ps 等命令可查看容器的运行状态），然而 apiserver 中，失联节点的 Pod 的状态已经变为 Terminating 或者 Unknown，如果 Kubernetes 不能通过 cloud-controller-manager 判断失联节点是否已经永久从集群中移除（例如：在虚拟机或物理机上自己部署 Kubernetes 的情况），则集群管理员需要手动删除 apiserver 中的节点对象，此时 Kubernetes 将删除该节点上的所有 Pod，在 Kubernetes v1.12 中，TaintNodesByCondition 特性进入 beta 阶段，此时 node lifecycle controller 将自动创建该 Condition 对应的污点，调度器在选择合适的节点时，不再关注节点的 Condition，而是检查节点的污点和 Pod 的容忍度

- **Capacity and Allocatable**：

  - Capacity 中的字段表示节点上的资源总数，Allocatable 中的字段表示该节点上可分配给普通 Pod 的资源总数
    - CPU
    - 内存
    - 该节点可调度的最大 Pod 数量

- **Info**：
  - 描述了节点的基本信息，例如：
    - Linux 内核版本
    - Kubernetes 版本（kubelet 和 kube-proxy 的版本）
    - Docker 版本
    - 操作系统名称

~~~bash
# 查看所有 Node
kubectl get nodes -o wide
# 查看节点详细信息
kubectl describe node <node-name>
~~~



##### 3、Node 管理

与 Pod 和 Service 不一样，节点并不是由 Kubernetes 创建的，节点由云供应商（例如：Google Compute Engine、阿里云等）创建，或者节点已经存在于本地物理机/虚拟机的资源池

Kubernetes 中创建节点时，仅仅是创建了一个描述该节点的 API 对象，节点 API 对象创建成功后，Kubernetes将检查该节点是否有效

假设创建如下节点信息：

~~~yaml
kind: Node
apiVersion: v1
metadata:
  name: "10.240.79.157"
  labels:
    name: "my-first-k8s-node"
~~~

Kubernetes 在 APIServer 上创建一个节点 API 对象（节点的描述），并且基于 metadata.name 字段对节点进行健康检查，如果节点有效（节点组件正在运行），则可以向该节点调度 Pod，否则该节点 API 对象将被忽略，并且 K8s 会一直检测节点的状态，直到节点变为有效状态或者由集群管理员手动删除节点



##### 4、Node Controller

节点控制器是一个负责管理节点的 Kubernetes master 组件，主要功能：

- 节点控制器在注册节点时为节点分配 CIDR 地址块

- 节点控制器通过云供应商接口检查节点列表中每一个节点对象对应的虚拟机是否可用，在云环境中，只要节点状态异常，节点控制器检查其虚拟机在云供应商的状态，如果虚拟机不可用，自动将节点对象从 APIServer 中删除

- 节点控制器监控节点的健康状况，当节点变得不可触达时（例如：由于节点已停机，节点控制器不再收到来自节点的心跳信号）节点控制器将节点API对象的 NodeStatus Condition 取值从 NodeReady 更新为 Unknown，然后在等待 pod-eviction-timeout 时间后，将节点上的所有 Pod 从节点驱逐

  - 默认40秒未收到心跳，修改 NodeStatus Condition 为 Unknown
  - 默认 pod-eviction-timeout 为 5分钟
  - 节点控制器每隔 --node-monitor-period 秒检查一次节点的状态

- 每个节点都有一个 kube-node-lease 名称空间下对应的 Lease 对象，节点控制器周期性地更新 Lease 对象，此时 NodeStatus 和 node-lease 都被用来记录节点的心跳信号

  - NodeStatus 的更新频率远高于 node-lease，原因是：

    - 每次节点向 Master 发出心跳信号，NodeStatus 都将被更新

    - 只有在 NodeStatus 发生改变，或者足够长的时间未接收到 NodeStatus 更新时，节点控制器才更新 node-lease（默认为1分钟，比节点失联的超时时间 40 秒要更长）
    - 由于 node-lease 比 NodeStatus 更轻量级，该特性显著提高了节点心跳机制的效率，并使 Kubernetes 性能和可伸缩性得到了提升

在 Kubernetes v1.4 中，优化了节点控制器的逻辑以便更好的处理大量节点不能触达 Master 的情况（例如：Master 出现网络故障），主要的优化点在于，节点控制器在决定是否执行 Pod 驱逐的动作时，会检查集群中所有节点的状态

大多数情况下，节点控制器限制了驱逐 Pod 的速率为 --node-eviction-rate （默认值是0.1）每秒，即节点控制器每 10 秒驱逐 1 个 Pod

当节点所在的高可用区出现故障时，节点控制器驱逐 Pod 的方式将不一样，节点控制器驱逐Pod前，将检查高可用区里故障节点的百分比（NodeReady Condition 的值为 Unknown 或 False）：

- 如果故障节点的比例不低于 --unhealthy-zone-threshold（默认为 0.55），则降低驱逐 Pod 的速率
- 如果集群规模较小（少于等于 --large-cluster-size-threshold 个节点，默认值为 50），则停止驱逐 Pod
- 如果集群规模大于 --large-cluster-size-threshold 个节点，则驱逐 Pod 的速率降低到 --secondary-node-eviction-rate （默认值为 0.01）每秒

针对每个高可用区使用这个策略的原因是，某一个高可用区可能与 Master 隔开了，而其他高可用区仍然保持连接，如果集群并未分布在云供应商的多个高可用区上，此时只有一个高可用区（即整个集群）

将集群的节点分布到多个高可用区最大的原因是，在某个高可用区出现整体故障时，可以将工作负载迁移到仍然健康的高可用区，因此如果某个高可用区的所有节点都出现故障时，节点控制器仍然使用正常的驱逐 Pod 的速率（--node-eviction-rate）

最极端的情况是，所有的高可用区都完全不可用（例如：集群中一个健康的节点都没有），此时节点控制器 Master 节点的网络连接出现故障，并停止所有的驱逐 Pod 的动作，直到某些连接得到恢复

自 Kubernetes v1.6 开始，节点控制器同时也负责为带有 NoExecute 污点的节点驱逐其上的 Pod，同时节点控制器还负责根据节点的状态（例如：节点不可用，节点未就绪等）为节点添加污点

自 Kubernetes v1.8 开始，节点控制器可以根据节点的 Condition 为节点添加污点



##### 5、节点自注册

如果 kubelet 的启动参数 --register-node为 true（默认为 true），kubelet 会尝试将自己注册到 API Server

kubelet自行时，将使用如下选项：

- --kubeconfig：向 apiserver 进行认证时所用身份信息的路径
- --cloud-provider：向云供应商读取节点自身元数据
- --register-node：自动向 API Server 注册节点
- --register-with-taints：注册节点时，为节点添加污点，逗号分隔，格式为 <key>=<value>:<effect>
- --node-ip：节点的 IP 地址
- --node-labels：注册节点时，为节点添加标签
- --node-status-update-frequency：向 Master 节点发送心跳信息的时间间隔

如果 Node authorization mode 和 NodeRestriction admission plugin 被启用，kubelet 只拥有创建/修改其自身所对应的节点 API 对象的权限



##### 6、手动管理节点

如果管理员想要手动创建节点 API 对象，可以将 kubelet 的启动参数 --register-node 设置为 false

管理员可以修改节点API对象（不管是否设置了 --register-node 参数）

可以修改的内容有：

- 增加/减少标签
- 标记节点为不可调度（unschedulable）

节点的标签与 Pod 上的节点选择器（node selector）配合，可以控制调度方式，例如：限定 Pod 只能在某一组节点上运行

执行如下命令可将节点标记为不可调度（unschedulable）阻止新的 Pod 被调度到该节点上，但是不影响任何已经在该节点上运行的 Pod，这在准备重启节点之前非常有用

```sh
kubectl cordon $NODENAME
```

z**注意**：

- DaemonSet Controller 创建的 Pod 将绕过 Kubernetes 调度器，并且忽略节点的 unschedulable 属性
  - Daemons 守护进程属于节点，尽管该节点在准备重启前，已经排空了上面所有的应用程序



##### 7、节点容量

节点 API 对象中描述了节点的容量（Capacity），例如：CPU数量、内存大小等信息

通常节点在向 APIServer 注册的同时，在节点 API 对象里汇报了其容量（Capacity），如果手动管理节点，需要在添加节点时自己设置节点的容量

Kubernetes 调度器在调度 Pod 到节点上时，将确保节点上有足够的资源，也即调度器检查节点上所有容器的资源请求之和不大于节点的容量，并且只能检查由 kubelet 启动的容器，不包括直接由容器引擎启动的容器，更不包括不在容器里运行的进程

可以显示的为 Pod 以外的进程预留资源，参考 reserve resources for system daemons



#### 3、Addons

Addons 使用 Kubernetes 资源（DaemonSet、Deployment等）实现集群的功能特性

由于其提供集群级别的功能特性，addons 使用到的 Kubernetes 资源都放置在 kube-system 名称空间下

下面描述了一些经常用到的 addons，参考 [Addons](https://kubernetes.io/docs/concepts/cluster-administration/addons/)查看更多列表

- **DNS**：

  - 除了 DNS Addon 以外，其他的 addon 都不是必须的，所有 Kubernetes 集群都应该有 Cluster DNS

  - Cluster DNS 是一个 DNS 服务器，是对已有环境中其他 DNS 服务器的一个补充，存放了 Kubernetes Service 的 DNS 记录

  - Kubernetes 启动容器时，自动将该 DNS 服务器加入到容器的 DNS 搜索列表中
  - 目前默认安装 Core DNS

- **Web UI**：
  - [Dashboard](https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/) 是一个 Kubernetes 集群的 Web 管理界面，让用户可以通过该界面管理集群

- **ContainerResource Monitoring**
  - [Container Resource Monitoring](https://kubernetes.io/docs/tasks/debug-application-cluster/resource-usage-monitoring/) 将容器的度量指标（metrics）记录在时间序列数据库中，并提供了 UI 界面查看这些数据

- **Cluster-level Logging**
  - [Cluster-level logging](https://kubernetes.io/docs/concepts/cluster-administration/logging/) 负责将容器的日志存储到一个统一存储中，并提供搜索浏览的界面

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
| apply        | 通过文件或标准输入对资源进行更改                     |
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



**开启命令提示**：

~~~bash
yum install -y bash-completion
source /usr/share/bash-completion/bash_completion
source <(kubectl completion bash)
echo "source <(kubectl completion bash)" >> ~/.bashrc
~~~



#### 3、Kubelet

Kubelet 是在每个 Node 节点上运行的主要节点代理，用于在集群中的每个节点上启动 Pod 和容器等



#### 4、容器运行时

##### 1、概述

v1.24 之前的版本直接集成了 Docker Engine 的一个组件，名为 Dockershim，但是自 v1.24 版起，Dockershim 已从 Kubernetes 项目中移除，所以需要在集群内每个节点上安装一个容器运行时以使 Pod 可以运行在上面，v1.26 要求使用符合容器运行时接口（CRI）的运行时

容器运行时具有掌控容器运行的整个生命周期，包括镜像的构建和管理、容器的运行和管理等，其向上提供容器调用接口，包括容器生成与销毁的全生命周期管理的功能，向下提供调用接口，负责具体的容器操作事项

常见的容器运行时：

- containerd（推荐）
- CRI-O
- Docker Engine
- Mirantis Container Runtime



Kubernetes为容器提供了一系列重要的资源：

- 由镜像、一个或多个数据卷合并组成的文件系统
- 容器自身的信息
- 集群中其他重要对象的信息

在容器中执行 hostname 命令或者在libc 中执行 gethostname 函调用，获得的是容器所在 Pod 的名字

Pod 的名字，以及 Pod 所在名称空间可以通过 downward API 注入到容器的环境变量里。

用户也可以使用 configMap 为容器自定义环境变量

在容器创建时，集群中所有的 Service 的连接信息将以环境变量的形式注入到容器中

~~~bash
FOO_SERVICE_HOST=<Service的ClusterIP>
FOO_SERVICE_PORT=<Service的端口>
~~~



##### 2、RunTime Class

使用 RuntimeClass 这一特性可以为容器选择运行时的容器引擎，以在性能和安全之间取得平衡

如果要使用，请确保 RuntimeClass 的 feature gate 在 apiserver 和 kubelet 上都是是激活状态，其依赖于 Container Runtime Interface（CRI）的具体实现

RuntimeClass 默认要求集群中所有节点上的容器引擎的配置都是相同的，Kubernetes v1.16 中才开始引入对节点上容器引起不同的情况下的支持



**containerd**：

修改配置文件 /etc/containerd/config.toml 配置其 Runtime handler，请注意该文档的如下内容

```toml
# 旧版为 cri
# 新版查看安装过程中配置
[plugins.cri.containerd.runtimes.${HANDLER_NAME}]
```

此配置会有一个 handler 名称，用来唯一地标识该 CRI 的配置，此时需要为每一个 handler 创建一个对应的 RuntimeClass api 对象

RuntimeClass 目前只有两个主要的字段：

- RuntimeClass name（metadata.name）
- handler (handler)

~~~yaml
apiVersion: node.k8s.io/v1beta1
kind: RuntimeClass
metadata:
  name: myclass # RuntimeClass 没有名称空间
handler: myHandler  # 对应 CRI 配置的 handler 名称
~~~

为集群完成 RuntimeClass 的配置后，在 Pod 的定义中指定 runtimeClassName 即可

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: mypod
spec:
  runtimeClassName: myclass
~~~

kubelet 将依据这个字段使用指定的 RuntimeClass 来运行该 Pod，如果指定的 RuntimeClass 不存在，或者 CRI 不能运行对应的 handler 配置，则 Pod 将进入 Failed 这个终止阶段，需要查看 Pod 的日志排查，如果 Pod 中未指定 runtimeClassName，kubelet 将使用默认的 RuntimeHandler 运行 Pod



##### 3、容器钩子

Kubernetes中为容器提供了两个 hook（钩子函数）：

- **PostStart**：
  - 此钩子函数在容器创建后将立刻执行，并不能保证该钩子函数在容器的 ENTRYPOINT 之前执行
  - 该钩子函数没有输入参数
- **PreStop**：
  - 此钩子函数在容器被 terminate（终止）之前执行，例如：
    - 通过接口调用删除容器所在 Pod
    - 某些管理事件的发生：健康检查失败、资源紧缺等
  - 如果容器已经被关闭或者进入了 completed 状态，preStop 钩子函数的调用将失败
  - 该函数的执行是同步的，即 Kubernetes 将在该函数完成执行之后才删除容器
  - 该钩子函数没有输入参数

容器只要实现并注册 hook handler 便可以使用钩子函数，容器可以实现两种类型的 hook handler：

- **Exec**：在容器的名称空进和 cgroups 中执行一个指定的命令，例如：pre-stop.sh
  - 该命令所消耗的 CPU、内存等资源，将计入容器可以使用的资源限制
- **HTTP**：向容器的指定端口发送一个 HTTP 请求

当容器的生命周期事件发生时，Kubernetes 在容器中执行该钩子函数注册的 handler，对于 Pod 而言，hook handler 的调用是同步的，即如果是 PostStart hook，容器的 ENTRYPOINT 和 hook 是同时出发的，然而如果 hook 执行的时间过长或者挂起了，容器将不能进入到 Running 状态，PreStop hook 的行为与此相似，如果 hook 在执行过程中挂起了，Pod phase 将停留在 Terminating 的状态，并且在 terminationGracePeriodSeconds 超时之后，Pod被删除，如果 PostStart 或者 PreStop hook 执行失败，则 Kubernetes 将 kill 该容器

用户应该使其 hook handler 越轻量级越好，例如：对于长时间运行的任务，在停止容器前，调用 PreStop 钩子函数，以保存当时的计算状态和数据

Hook 将至少被触发一次，即，当指定事件 PostStart 或 PreStop 发生时，hook 有可能被多次触发，因此 hook handler 的实现需要保证即使多次触发，执行也不会出错

Hook handler 的日志并没有在 Pod 的 events 中发布，如果 handler 因为某些原因失败了，kubernetes 将广播一个事件 PostStart hook 发送 FailedPreStopHook 事件，可以执行命令 kubectl describe pod $(pod_name) 以查看这些事件

~~~bash
# 创建一个 Pod 并在其内定义 hook
apiVersion: v1
kind: Pod
metadata:
  name: lifecycle-demo
spec:
  containers:
  - name: lifecycle-demo-container
    image: nginx
    lifecycle:
      postStart:
        exec:
          command: ["/bin/sh", "-c", "echo Hello from the postStart handler > /usr/share/message"]
      preStop:
        exec:
          command: ["/bin/sh","-c","nginx -s quit; while killall -0 nginx; do sleep 1; done"]
~~~



**注意**：

- Kubernetes 只在 Pod Teminated 状态时才发送 preStop 事件，处于其他状态不会发送



### 4、集群通信

**Node To Master**：

所有从集群访问 Master 节点的通信，都是通过 APIServer （没有任何其他 Master 组件发布远程调用接口）

通常安装 Kubernetes 时，APIServer 监听 HTTPS 端口（443），并且配置了一种或多种客户端认证方式，至少需要配置一种形式的授权方式，尤其是匿名访问或 Service Account Tokens 被启用的情况下

节点上必须配置集群（APIServer）的公钥根证书（public root certificate），在提供有效的客户端身份认证的情况下，节点可以安全地访问 APIServer，例如：在 Google Kubernetes Engine 的默认 K8s 安装里，通过客户端证书为 kubelet 提供客户端身份认证

对于需要调用 APIServer 接口的 Pod，应该为其关联 Service Account，此时 Kubernetes 将在创建 Pod 时自动为其注入公钥根证书（public root certificate）以及一个有效的 bearer token（放在 HTTP 请求头 Authorization 字段）

所有名称空间中，都默认配置了名为 Kubernetes 的 Service，该 Service 对应一个虚拟 IP（默认为 10.96.0.1）将发送到该地址的请求将由 kube-proxy 转发到 APIServer 的 HTTPS 端口上

得益于这些措施，默认情况下从集群（节点以及节点上运行的 Pod）访问 Master 的连接是安全的，可以通过不受信的网络或公网连接 Kubernetes 集群



**Master To Node**：

从 Master（APIServer）到 Node 存在着两条主要的通信路径：

- apiserver 访问集群中每个节点上的 kubelet 进程
- 使用 apiserver 的 proxy 功能，从 apiserver 访问集群中的任意节点、Pod、Service



**apiserver to kubelet**：

apiserver 在如下情况下访问 kubelet：

- 抓取 Pod 的日志
- 通过 kubectl exec -it 指令（或 kuboard 的终端界面）获得容器的命令行终端
- 提供 kubectl port-forward 功能

这些连接的访问端点是 kubelet 的 HTTPS 端口，默认情况下，apiserver 不校验 kubelet 的 HTTPS 证书，这种情况下，连接可能会收到 man-in-the-middle 攻击，因此该连接如果在不受信网络或者公网上运行时，是不安全的

如果要校验 kubelet 的 HTTPS 证书，可以通过 --kubelet-certificate-authority 参数为 apiserver 提供校验 kubelet 证书的根证书，否则建议开启 Kubelet authentication/authorization 以保护 kubelet API



**apiserver to nodes, pods, services**：

从 apiserver 到 节点、Pod、Service 的连接使用的是 HTTP 连接，没有进行身份认证，也没有进行加密传输，也可以通过增加 https 作为 节点、Pod、Service 请求 URL 的前缀，但是 HTTPS 证书并不会被校验，也无需客户端身份认证，因此该连接是无法保证一致性的

目前，此类连接如果运行在非受信网络或公网上时，是不安全的



**SSH**：

Kubernetes 支持 SSH隧道（tunnel）来保护 Master --> Node 访问路径，APIServer 将向集群中的每一个节点建立一个 SSH 隧道（连接到端口 22 的 ssh 服务）并通过隧道传递所有发向 kubelet、node、pod、service 的请求

SSH 隧道当前已被不推荐使用，Kubernetes 正在设计新的替代通信方式



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
    # 公网 VPS IP 一般不会直接绑定在网卡上
    # 检查公网 IP 是否绑定在网卡上
    ip a | grep xx.xx.xx.xx
    
    # 临时
    # 如果没有,需要绑定
    # 机器重启会失效
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
    IPADDR=公网IP
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
    
    cat <<EOF | sudo tee /etc/sysctl.d/k8s.conf
    net.bridge.bridge-nf-call-iptables  = 1
    net.bridge.bridge-nf-call-ip6tables = 1
    net.ipv4.ip_forward                 = 1
    EOF
    
    # 应用 sysctl 参数而不重新启动
    # 设置所需的 sysctl 参数，参数在重新启动后保持不变
    sysctl --system
    
    # 检查非常重要
    lsmod | grep br_netfilter
    lsmod | grep overlay
    sysctl net.bridge.bridge-nf-call-iptables net.bridge.bridge-nf-call-ip6tables net.ipv4.ip_forward
    ~~~
  
- 防火墙

  - ~~~bash
    # 为了省事直接关闭
    # 否则需要开启对应端口与协议
    systemctl disable firewalld
    systemctl stop firewalld
    ~~~


**禁止 swap 分区**：

- ~~~bash
  # 临时 
  swapoff -a 
  
  # 永久
  sed -ri 's/.*swap.*/#&/' /etc/fstab
  
  # 检查 swap 状态
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
  
- 开放额外协议：

  ICMP、IPIP协议

**时间同步**：（部份 OS 不需要）

- ~~~bash
  yum install -y ntpdate
  ntpdate time.windows.com
  ~~~



## 2、包管理器安装版

**注意**：内网外网搭建的区别



### 1、基准配置

#### 1、安装套件

访问 [Index of /yum/repos// (google.com)](https://packages.cloud.google.com/yum/repos/) 发现，Kubernetes 只适配到 el7，但是 el8 也可以使用，如果后续适配了可能需要修改

配置完包管理器的 repo，K8s 三件套可以一次安装了

以下为基于 Red Hat 发行的系统

~~~bash
# 环境准备
yum install -y yum-utils device-mapper-persistent-data lvm2 gcc gcc-c++ tc
~~~



~~~bash
# k8s 国外直接使用 google 配置仓库文件，省事
cat <<EOF | sudo tee /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-\$basearch
enabled=1
gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
EOF

# k8s 国内 Aliyun
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF

# containerd 国内 Aliyun
yum config-manager --add-repo https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

# 安装四件套
yum install -y kubectl kubelet kubeadm containerd

# 安装完 containerd 先启动一次生成配置文件
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
  systemctl daemon-reload
  systemctl restart containerd
  systemctl status containerd
  
  # 国内 Aliyun 镜像下载的 sandbox_image 的 tag 与 原版有出入，需要在配置中新增对应 sanbox_image tag
  # 查看镜像 crictl image list
  # 修改完同样需要重启
  [plugins."io.containerd.grpc.v1.cri"]
    sandbox_image = "xxxxx"
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

配置 Docker：（直接使用 containerd 就不需要再配置 Docker）

- ~~~bash
  # docker安装后默认没有daemon.json这个配置文件，需要进行手动创建
  vim /etc/docker/daemon.json
  "exec-opts": ["native.cgroupdriver=systemd"]
  
  # 如果没有则使用
  echo {\"exec-opts\": [\"native.cgroupdriver=systemd\"]} > /etc/docker/daemon.json
  
  # 重启 docker
  systemctl daemon-reload
  systemctl restart docker
  systemctl status docker
  
  # 查看 Driver
  docker info|grep Driver
  ~~~



#### 3、配置文件初始化

~~~bash
# init.default初始化文件
kubeadm config print init-defaults >init.default.yaml

# 国内需要修改 init.default.yaml 的镜像地址 registry.aliyuncs.com/google_containers
# 国外默认
# 或者不使用 --config 启动，改为 --image-repository= 加上镜像地址
# 使用配置文件，拉取相关镜像
kubeadm config images pull --config=init.default.yaml
# 或者使用命令行
kubeadm config images pull --image-repository=registry.aliyuncs.com/google_containers
~~~

~~~bash
# 国内 Aliyun 下载的 sandbox_image 为 3.9
# 在修改 containerd 的 sandbox_image 失效的情况下，直接修改 tag 即可
# 具体修改为多少的 tag，可查看任意容器日志得到
ctr -n k8s.io i tag registry.aliyuncs.com/google_containers/pause:3.9 registry.k8s.io/pause:3.6
~~~



#### 4、修改配置文件

~~~bash
# 未验证
vim /usr/lib/systemd/system/kubelet.service.d/10-kubeadm.conf

# 在末尾添加参数 --node-ip=主机IP
ExecStart=/usr/bin/kubelet $KUBELET_KUBECONFIG_ARGS $KUBELET_CONFIG_ARGS $KUBELET_KUBEADM_ARGS $KUBELET_EXTRA_ARGS --node-ip=<主机IP>
~~~



**重要注意**：开始前务必检查所有前置要求均满足

- 检查<a href='#安装要求'> 安装要求</a> 
- 检查容器运行时是否运行
- 检查公网 IP 是否绑定在网卡上



### 2、初始化 Master

~~~bash
# 启动 Master 节点，国内需要添加 --image-repository
kubeadm init --apiserver-advertise-address=masterIP --kubernetes-version v1.26.0 --service-cidr=10.1.0.0/16 --pod-network-cidr=10.244.0.0/16 --image-repository=registry.aliyuncs.com/google_containers
~~~

~~~bash
# 记录下 Token
You should now deploy a pod network to the cluster.
Run "kubectl apply -f [podnetwork].yaml" with one of the options listed at:
  https://kubernetes.io/docs/concepts/cluster-administration/addons/

Then you can join any number of worker nodes by running the following on each as root:

kubeadm join 10.0.0.64:6443 --token 0sdiop.qs9m98sqqn9hzjjm \
	--discovery-token-ca-cert-hash sha256:39244ba104bd5d5f79c695852a18b329b86f75df1a4e7f8b583627a432e9f762

# 后续忘记可以使用改命令获取，不过需要检查是否过期
kubeadm token list

# 过期了，需要重新生成
kubeadm token create --print-join-command
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
# 使用配置文件加入，麻烦不建议
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

# 或者使用命令行加入
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

#### 1、部署一个应用

在 k8s 上进行部署前，首先需要了解一个基本概念 **Deployment**，在k8s中，通过发布 Deployment，可以创建镜像 (image) 的实例 (container)，这个实例会被包含在称为 **Pod** 的概念中，**Pod** 是 k8s 中最小可管理单元

在发布 Deployment 后，Deployment 将指示 k8s 如何创建和更新应用程序的实例，Master 节点将实例调度到集群中的具体节点

创建实例后，Kubernetes Deployment Controller 会持续监控这些实例，如果运行实例的 Worker 节点关机或被删除，则 Kubernetes Deployment Controller 将在集群选择资源最优的 Worker 节点上重新创建新实例，这是一种通过自我修复机制来解决故障或维护问题

> 在容器编排之前的时代，各种安装脚本通常用于启动应用程序，但是不能够使应用程序从机器故障中恢复，通过创建应用程序实例并确保它们在集群节点中的运行实例个数，Kubernetes Deployment 提供了一种完全不同的方式来管理应用程序

<img src="images/image-20230216223932817.png" alt="image-20230216223932817" style="zoom:30%;" />

通过在 Master 节点发布 Deployment，Master 节点会选择合适的 Worker 节点创建 Container，Container 会包含在 Pod 里

~~~yaml
apiVersion: apps/v1	# 与 k8s 集群版本有关，使用 kubectl api-versions 即可查看当前集群支持的版本
kind: Deployment	# 配置的类型，使用的是 Deployment
metadata:	        # 元数据，即 Deployment 的一些基本属性和信息
  name: nginx-deployment	# Deployment 的名称
  labels:	    # 标签，可以灵活定位一个或多个资源，其中 key:value 均自定义，可以定义多组
    app: nginx	# 为该 Deployment 设置标签
spec:	        # 关于该 Deployment 的预期状态
  replicas: 1	# 使用该 Deployment 创建一个实例
  selector:	    # 标签选择器，与上面的标签共同作用
    matchLabels: # 选择包含标签 app:nginx 的资源
      app: nginx
  template:	    # 这是选择或创建的 Pod 的模板
    metadata:	# Pod 元数据
      labels:	# Pod 标签，上面的 selector 即选择包含标签 app:nginx 的 Pod
        app: nginx
    spec:	    # Pod 预期状态
      containers:	# 生成 container，与 docker 中的 container 是同一种
      - name: nginx	# container 名称
        image: nginx:1.7.9	# 使用镜像 nginx:1.7.9 创建 container，该 container 默认 80 端口可访问
~~~

~~~bash
# 部署
kubectl apply -f nginx-deployment.yaml
~~~

~~~bash
# 查看 Deployment
kubectl get deployments
# 查看 Pod
kubectl get pods
~~~



#### 2、状态查看

~~~bash
# kubectl get 资源类型
# 获取类型为Deployment的资源列表
kubectl get deployments

# 获取类型为Pod的资源列表
kubectl get pods

# 获取类型为Node的资源列表
kubectl get nodes
~~~

在命令后增加 -A 或 --all-namespaces 可查看所有 namespace 中的对象，使用参数 -n 可查看指定 namespace 的对象

~~~bash
# 查看所有名称空间的 Deployment
kubectl get deployments -A
kubectl get deployments --all-namespaces

# 查看 kube-system 名称空间的 Deployment
kubectl get deployments -n kube-system
~~~



#### 3、详情查看

显示 Pod 的详细信息

~~~~bash
# kubectl describe 资源类型 资源名称

# 查看名称为 nginx-XXXXXX 的 Pod 的信息
kubectl describe pod nginx-XXXXXX	

# 查看名称为 nginx 的 Deployment 的信息
kubectl describe deployment nginx	
~~~~

查看容器日志

~~~bash
# kubectl logs Pod 名称

# 查看名称为 nginx-pod-XXXXXXX 的 Pod 内的容器打印的日志
# 上一步的 nginx-pod 没有输出日志，所以结果为空
kubectl logs -f nginx-pod-XXXXXXX
~~~



#### 4、进入容器

~~~bash
# kubectl exec Pod名称 操作命令

# 在名称为 nginx-pod-xxxxxx 的 Pod 中运行 bash
kubectl exec -it nginx-pod-xxxxxx /bin/bash
~~~



#### 5、暴露实例

Kubernetes 中的 **Service（服务）** 提供了一种抽象层，它选择具备某些特征的 Pod 并定义一个访问方式

Service 使 Pod 之间的相互依赖解耦，原本从一个 Pod 中访问另外一个 Pod，需要知道对方的 IP 地址

Service 通过 Labels、LabelSelector 选定 Pod

在创建Service的时候，通过设置配置文件中的 spec.type 字段的值，可以以不同方式向外部暴露应用程序：

- **ClusterIP**（默认）：在群集中的内部IP上公布服务，这种方式的 Service（服务）只在集群内部可以访问到
- **NodePort**：使用 NAT 在集群中所有节点的同一端口上公布服务，可以通过访问集群中任意 节点IP + 端口号 的方式访问服务，此时 ClusterIP 的访问方式仍然可用
- **LoadBalancer**：在云环境中，创建一个集群外部的负载均衡器，并为使用该负载均衡器的 IP 地址作为服务的访问地址，此时 ClusterIP 和 NodePort 的访问方式仍然可用



<img src="images/image-20230217211134357.png" alt="image-20230217211134357" style="zoom:50%;" />

~~~yaml
apiVersion: v1
kind: Service
metadata:
  name: nginx-service	# Service 名称
  labels:     	# Service 自己的标签
    app: nginx	# 为该 Service 设置 key 为 app，value 为 nginx 的标签
spec:	    # Service 预期状态
  selector:	    # 标签选择器
    app: nginx	# 选择包含标签 app:nginx 的 Pod
  ports:
  - name: nginx-port	# 端口的名字
    protocol: TCP	    # 协议类型 TCP/UDP
    port: 80	        # 集群内的其他容器组可通过 80 端口访问 Service
    nodePort: 32600   # 通过任意节点的 32600 端口访问 Service
    targetPort: 80	# 将请求转发到匹配 Pod 的 80 端口
  type: NodePort	# Serive 负载类型：ClusterIP/NodePort/LoaderBalancer
~~~

~~~bash
# 部署
kubectl apply -f nginx-service.yaml
# 查看
kubectl get svc -o wide
~~~

~~~bash
# 访问
curl <任意节点 IP>:32600
~~~



#### 6、伸缩容器

**伸缩容器**通过更改 nginx-deployment.yaml 文件中部署的 replicas（副本数）来完成

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
spec:
# 修改副本数量为 4
  replicas: 4
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
~~~

~~~bash
# 使用 apply 应用
kubectl apply -f nginx-deployment.yaml
# 查看结果
watch kubectl get pods -o wide
~~~



#### 7、滚动更新

**Rolling Update 滚动更新**通过使用新版本 Pod 逐步替代旧版本 Pod 来实现 Deployment 的更新，从而实现零停机，新版本 Pod 将在具有可用资源的 Node 上进行调度，这个过程中，Service 会持续监视 Pod 的状态，将流量始终转发到可用的 Pod 上

在Kubernetes 中，更新是版本化的，任何部署更新都可以恢复为以前的版本

滚动更新允许以下操作：

- 将应用程序从准上线环境升级到生产环境（通过更新容器镜像）
- 回滚到以前的版本
- 持续集成和持续交付应用程序，无需停机

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
spec:
  replicas: 4
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.8   # 使用镜像 nginx:1.8 替换原来的 nginx:1.7.9
        ports:
        - containerPort: 80
~~~

~~~bash
# 应用
kubectl apply -f nginx-deployment.yaml
# 查看
watch kubectl get pods -l app=nginx
~~~

<img src="images/image-20230217212230637.png" alt="image-20230217212230637" style="zoom:50%;" />









### 6、停止

~~~bash
# 移除指定节点的 pod
kubectl drain nodename --delete-local-data --force --ignore-daemonsets
~~~

~~~bash
# 移除节点
kubectl delete nodes nodename
~~~

如果安装的网络插件为 flannel 且 容器运行时为 Docker 则直接运行下列所有命令

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





# 3、Kubernetes 资源清单

## 1、基本概念

Kubernetes 通过声明 yml 文件来解决资源管理和资源对象编排与部署，这种文件叫做资源清单文件，通过 kubectl 命令使用资源清单文件可以实现对大量的资源对象进行编排



## 2、资源对象

### 1、概述

资源对象指的是 Kubernetes 系统的持久化实体，也可以叫 K8s 对象，并通过 APIServer存储在 etcd 中，这些数据/对象描述了：

- 集群中运行了哪些容器化应用程序（以及在哪个节点上运行）
- 集群中对应用程序可用的资源
- 应用程序相关的策略定义，例如，重启策略、升级策略、容错策略
- 其他 Kubernetes 管理应用程序时所需要的信息

一个 Kubernetes 对象代表着用户的一个意图（a record of intent），一旦创建了一个 Kubernetes 对象，Kubernetes 将持续工作，以尽量实现此用户的意图，Kubernetes 对象就是告诉 Kubernetes，需要的集群中的工作负载是什么（集群的**目标状态**）

所有 K8s 对象都包含两个状态：

- **spec**：必须由您来提供，描述了您对该对象所期望的预期状态
- **status**：只能由 Kubernetes 系统来修改，描述了该对象在 Kubernetes 系统中的实际状态

同一个 Kubernetes 对象应该只使用一种方式管理，否则可能会出现不可预期的结果



### 2、管理

| 管理方式         | 操作对象                     | 推荐的环境 |
| ---------------- | ---------------------------- | ---------- |
| 指令性的命令行   | Kubernetes 对象              | 开发环境   |
| 指令性的对象配置 | 单个 yaml 文件               | 生产环境   |
| 声明式的对象配置 | 包含多个 yaml 文件的多个目录 | 生产环境   |

~~~bash
# 指令性命令行
kubectl create deployment nginx --image nginx

# 指令性 yaml
kubectl create -f nginx.yaml

# 声明式 yaml
kubectl diff -f configs/
kubectl apply -f configs/
~~~







## 3、常用字段

### 1、必须存在的属性

| 参数名                | 字段类型 | 说明                                            |
| --------------------- | -------- | ----------------------------------------------- |
| version               | String   | K8S API 版本，使用 kubectl api-version 命令查询 |
| kind                  | String   | yml 文件定义的资源类型，比如：Pod               |
| metadata              | Object   | 元数据对象，固定值写 metadata                   |
| metadata.name         | String   | 元数据对象的名字，比如：Pod 的名字              |
| metadata.namespace    | String   | 元数据对象命名空间                              |
| metadata.labels       | List     | 自定义标签属性列表                              |
| metadata.annotation[] | List     | 自定义注解属性列表                              |
| spec                  | Object   | 详细定义对象，固定值写 Spec                     |

**注解**：

- 可以存入任意的信息，Kubernetes 的客户端或者自动化工具可以存取这些信息以实现其自定义的逻辑

- ~~~yaml
  metadata:
    annotations:
      key1: value1
      key2: value2
  ~~~

- 注解的 key 有两个部分：可选的前缀和标签名，通过 / 分隔。

  - 注解名：
    - 标签名部分是必须的
    - 不能多于 63 个字符
    - 必须由字母、数字开始和结尾
    - 可以包含字母、数字、减号-、下划线_、小数点.
  - 注解前缀：
    - 注解前缀部分是可选的
    - 如果指定，必须是一个DNS的子域名，同 Label 前缀
    - 不能多于 253 个字符
    - 使用 / 和标签名分隔

- 类似于下面的信息可以记录在注解中：

  - 声明式配置层用到的状态信息。
  - Build、release、image，例如：timestamp、release ID、git branch、PR number、image hash、registry address
  - 日志、监控、分析、审计系统的参数
  - 第三方工具所需要的信息，例如：name、version、build information、URL
  - 轻量级的发布工具用到的信息，例如：config、checkpoint
  - 负责人的联系方式，例如：电话号码、网址、电子信箱
  - 用户用来记录备忘信息的说明，例如：对标准镜像做了什么样的修改、维护过程中有什么特殊信息需要记住





### 2、Spec 属性

Spec 存在于多个 Kubernetes 组件的 yaml 描述当中，主要用于表达该组件要达到什么预期状态

每个 K8s 对象都有 Spec 属性，只是其中的参数有所不同

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
| spec.restartPolicy                          | String   | 定义 Pod 重启策略，可选值为 Always、OnFailure、Never，默认值为 Always，Always：Pod 无论为终止，kubelet 都将重启它，OnFailure：容器非正常结束，kubelet 将重启它，Never Pod 终止后，kubelet 发出报告给 Master，并不重启它 |
| spec.nodSelector                            | Object   | 定义 Node 的 Label 标签，以 key:value 格式指定               |
| spec.imagePullSecrets                       | Object   | 定义 pull 镜像时使用 secret 名称，以 name:secretkey 格式指定 |
| spec.hostNetWork                            | Boolean  | 定义是否使用主机网络模式，默认为 false，设置 true 表示使用主机网络，不使用 docker 网桥，同时设置了 true 将无法再用一台主机上启动第二个副本 |



# 4、Kubernetes Pod

## 1、基本概念

Pod 是 Kubernetes 中可以创建和管理的最小单元，是资源对象模型中由用户创建或部署的最小资源对象模型，其他的资源对象都是用来支撑或者扩展 Pod 对象功能的，比如：控制器对象是用来管控 Pod 对象的、Service 或者 Ingress 资源对象是用来暴露 Pod 引用对象的，PersistentVolume 资源对象是用来为 Pod 提供存储等等

Kubernetes 只能直接处理 Pod，Pod 是由一个或多个 container 组成，其中的每个容器都称为副本（replication），由对应的 Controller 管理，每一个 Pod 都有一个特殊的被称为根容器的 Pause 容器，Pause 容器对应的镜像属于 Kubernetes 平台的一部分，除了 Pause 容器，每个 Pod 还包含一个或多个紧密相关的用户业务容器，以及这些容器包含的资源，这些资源包括：

- 共享存储：称为卷 Volumes
- 网络：每个 Pod 在集群中有个唯一的 IP，Pod 中的 container 共享该 IP 地址
- container 的基本信息，例如：容器的镜像版本，对外暴露的端口等

<img src="images/image-20230107131244951.png" alt="image-20230107131244951" style="zoom: 33%;" />

某些 Pod 除了使用 app container （工作容器）以外，还会使用 init container （初始化容器），初始化容器运行并结束后，工作容器才开始启动



**建议**：

- 如果多个容器紧密耦合并且需要共享磁盘等资源，则应该被部署在同一个 Pod
- 应该尽量避免在 Kubernetes 中直接创建单个 Pod，推荐的做法是使用 Controller 来管理 Pod

<img src="images/image-20230216234334535.png" alt="image-20230216234334535" style="zoom:50%;" />

**注意**：

- Pod 由控制器依据 Pod Template 创建以后，此时再修改 Pod Template 的内容，已经创建的 Pod 不会被修改



## 2、特点

同一个 Pod 中的容器总会被调度到相同 Node 节点，并在相同 Node 节点的共享上下文中运行

不同节点间 Pod 的通信基于**虚拟二层网络技术**实现

Pod 本身并不能自愈（self-healing）

Pod 又分为普通与静态



**资源共享**：

- 一个 Pod 里的多个容器可以共享存储和网络，可以看作一个逻辑的主机
  - 资源：namespace、cgroups 或者其他的隔离资源

- 同一个 Pod 里的多个容器共享 Pod 的 IP、端口、namespace
  - 一个 Pod 内的多个容器之间可以通过 localhost 来进行通信，但需要注意的是容器之间的端口冲突

- 不同的 Pod 有不同的 IP，不同 Pod 内的容器之前通信，不可以使用 IPC（进程间通信，如果没有特殊指定的话），通常情况下使用 Pod 的 IP 进行通信
- 一个 Pod 里的多个容器可以共享存储卷，这个存储卷会被定义为 Pod 的一部分，并且可以挂载到该 Pod 里的所有容器的文件系统上
- 每个 Pod 中有一个 Pause 容器保存所有容器状态， 通过管理 Pause 容器，达到管理 Pod 中所有容器的效果



**生命周期短暂**：

- Pod 属于生命周期比较短暂的组件，比如：当 Pod 所在节点发生故障，那么该节点上的 Pod 会被调度到其他节点，但需要注意的是，被重新调度的 Pod 是一个全新的 Pod，跟之前的 Pod 毫无关系，除了配置相同外，其余属性均不相同，例如：IP、Name



**平坦的网络**：

- Kubernetes 集群中的所有 Pod 都在同一个共享网络地址空间中，也就是说每个 Pod 都可以通过其他 Pod 的 IP 地址来实现访问
- 每个 Pod 都是应用的一个实例，有专用的 IP



## 3、定义

Pod Template 是关于 Pod 的定义，其包含在其他的 Kubernetes 对象中（例如：Deployment、StatefulSet、DaemonSet 等控制器），控制器通过 Pod Template 信息来创建 Pod

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



**注意**：

- imagePullPolicy 字段和 image tag的可能取值将影响到 kubelet 如何抓取镜像：
  - Policy=IfNotPresent 仅在节点上没有该镜像时，从镜像仓库抓取
  - Policy=Always 每次启动 Pod 时，从镜像仓库抓取
  - Policy未填写，tag=latest 或者未填写，则同 Always 每次启动 Pod 时，从镜像仓库抓取
  - Policy未填写，tag != latest，则同 IfNotPresent 仅在节点上没有该镜像时，从镜像仓库抓取
  - Policy=Never，Kubernetes 假设本地存在该镜像，并且不会尝试从镜像仓库抓取镜像





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

Pod phase 代表其所处生命周期的阶段，phase 并不是用来代表其容器的状态，也不是一个严格的状态机

| 状态       | 说明                                                         |
| ---------- | ------------------------------------------------------------ |
| Pending    | API Server 已经创建了 Pod，但其中的容器镜像还未创建，包括镜像下载 |
| Running    | Pod 内所有容器已经创建，并且至少有一个容器处于运行中或启动中或重启中 |
| Compeleted | Pod 内所有容器均成功退出，并且不会重启                       |
| Failed     | Pod 内所有容器均退出，但至少有一个容器失败                   |
| Unknow     | 由于未知原因无法获取 Pod 状态，例如：网络通信不畅            |



Pod 内有一个状态数组描述其是否达到某些指定的条件：

| 字段名             | 描述                                                         |
| ------------------ | ------------------------------------------------------------ |
| type               | type 是最重要的字段，可能的取值有：<br />**PodScheduled：** Pod 已被调度到一个节点<br />**Ready：** Pod 已经可以接受服务请求，应该被添加到所匹配 Service 的负载均衡的资源池<br />**Initialized：**Pod 中所有初始化容器已成功执行<br />**Unschedulable：**不能调度该 Pod（缺少资源或者其他限制）<br />**ContainersReady：**Pod 中所有容器都已就绪 |
| status             | 可能的取值：True、False、Unknown                             |
| reason             | Condition 发生变化的原因，使用一个符合驼峰规则的英文单词描述 |
| message            | Condition 发生变化的原因的详细描述，human-readable           |
| lastTransitionTime | Condition 发生变化的时间戳                                   |
| lastProbeTime      | 上一次针对 Pod 做健康检查/就绪检查的时间戳                   |

当Pod 被创建后，Pod 将一直保留在该节点上，直到 Pod 以下情况发生：

- Pod 中的容器全部结束运行
- Pod 被删除
- 由于节点资源不够，Pod 被驱逐
- 节点出现故障（例如死机）



Pod 代表了运行在集群节点上的进程，而进程的终止有两种方式：

- gracefully terminate （优雅地终止）
- 直接 kill，此时进程没有机会执行清理动作

当用户发起删除 Pod 的指令时，Kubernetes 需要：

- 让用户知道 Pod 何时被删除
- 确保删除 Pod 的指令最终能够完成

Kubernetes 收到用户删除 Pod 的指令后：

1. 记录强制终止前的等待时长（grace period）
2. 向 Pod 中所有容器的主进程发送 TERM 信号
3. 一旦等待超时，向超时的容器主进程发送 KILL 信号
4. 删除 Pod 在 API Server 中的记录

如果要手动强制删除 Pod，必须为 kubectl delete 命令同时指定两个选项 --grace-period=0 和 --force，通常如果没有人或者控制器删除 Pod，Pod 不会自己消失，除非 Pod 处于 Scucceeded 或 Failed 的 phase，并超过了垃圾回收的时长（在 Kubernetes Master 中通过 terminated-pod-gc-threshold 参数指定），kubelet 自动将其删除



## 7、重启策略

| 策略      | 说明                                                   |
| --------- | ------------------------------------------------------ |
| Always    | 当容器失效时，由 Kubelet 重启容器                      |
| OnFailure | 当容器非正确终止（退出码不为 0 ），由 Kubelet 重启容器 |
| Never     | 无论容器如何终止，都不重启                             |

kubelet 将在五分钟内，按照递延的时间间隔（10s, 20s, 40s ......）尝试重启已退出的容器，并在十分钟后再次启动这个循环，直到容器成功启动，或者 Pod 被删除

控制器 Deployment、StatefulSet、DaemonSet，只支持 Always 这一个选项，不支持 OnFailure 和 Never 选项



Pod 重启时，所有的初始化容器都会重新执行，Pod 重启的原因可能有：

- 用户更新了 Pod 的定义，并改变了初始化容器的镜像
  - 改变任何一个初始化容器的镜像，将导致整个 Pod 重启
  - 改变工作容器的镜像，将只重启该工作容器，而不重启 Pod
- Pod 容器所在节点的基础设施被重启（例如：docker engine），通常只有 Node 节点的 root 用户才可以执行此操作
- Pod 中所有容器都已经结束，restartPolicy 是 Always，且初始化容器执行的记录已经被垃圾回收，此时将重启整个 Pod



## 8、状态转换

在 K8s 里面这个状态机制之间这个状态转换会产生相应的事件，而这个事件又通过类似像 normal 或者是 warning 的方式进行暴露，可以通过上层 Condition Status 相应的一系列字段来判断当前应用的具体状态并进行诊断

| 包含容器数 | 当前状态 | 发生事件        | 结果状态 |           | 结果状态结果状态 |
| ---------- | -------- | --------------- | -------- | --------- | ---------------- |
|            |          | **重启策略**    | Always   | OnFailure | Never            |
| 一个       | Running  | 容器成功终止    | Running  | Succeed   | Succeed          |
| 一个       | Running  | 容器失败终止    | Running  | Running   | Failed           |
| 多个       | Running  | 容器失败终止    | Running  | Running   | Running          |
| 多个       | Running  | 容器被 OOM 终止 | Running  | Running   | Failed           |

<img src="images/fd5234589b3e19eed61ce07381ef27e5de8f1123.451480e1.png" alt="fd5234589b3e19eed61ce07381ef27e5de8f1123.451480e1" style="zoom:50%;" />



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



## 10、初始化容器

### 1、概述

Pod 可以包含多个工作容器，也可以包含一个或多个初始化容器，初始化容器在工作容器启动之前执行，初始化容器与工作容器完全相同，除了如下几点：

- 初始化容器总是运行并自动结束
- kubelet 按顺序执行 Pod 中的初始化容器，前一个初始化容器成功结束后，下一个初始化容器才开始运行。所有的初始化容器成功执行后，才开始启动工作容器
- 如果 Pod 的任意一个初始化容器执行失败，kubernetes 将反复重启该 Pod，直到初始化容器全部成功（除非 Pod 的 restartPolicy 被设定为 Never）
- 初始化容器的 Resource request / limits 处理不同
- 初始化容器不支持就绪检查，因为初始化容器必须在 Pod ready 之前运行并结束



### 2、行为

Pod 的启动时，首先初始化网络和数据卷，然后按顺序执行每一个初始化容器

- 任何一个初始化容器都必须成功退出，才能开始下一个初始化容器
- 如果某一个容器启动失败或者执行失败，kubelet 将根据 Pod 的 restartPolicy 决定是否重新启动 Pod

只有所有的初始化容器全都执行成功，Pod 才能进入 ready 状态

- 初始化容器的端口是不能够通过 kubernetes Service 访问的
- Pod 在初始化过程中处于 Pending 状态，并且同时有一个 type 为 initializing status 为 True 的 Condition

如果 Pod 重启，所有的初始化容器也将被重新执行

可以重启、重试、重新执行初始化容器，因此初始化容器中的代码必须是幂等的

可以组合使用就绪检查和 activeDeadlineSeconds，以防止初始化容器始终失败

Pod 中不能包含两个同名的容器（初始化容器和工作容器也不能同名）



### 3、配置

~~~yaml
apiVersion: v1
kind: Pod
metadata:
  name: init-demo
spec:
  containers:
  - name: nginx
    image: nginx
    ports:
    - containerPort: 80
    volumeMounts:
    - name: workdir
      mountPath: /usr/share/nginx/html
  # 初始化容器
  initContainers:
  - name: install
    image: busybox
    command:
    - wget
    - "-O"
    - "/work-dir/index.html"
    - https://kuboard.cn
    volumeMounts:
    - name: workdir
      mountPath: "/work-dir"
  dnsPolicy: Default
  volumes:
  - name: workdir
    emptyDir: {}
~~~

Pod 中初始化容器和应用程序共享了同一个数据卷，初始化容器将该共享数据卷挂载到 /work-dir 路径，应用程序容器将共享数据卷挂载到 /usr/share/nginx/html 路径，初始化容器执行完命令后，就退出执行



### 4、Debug

~~~bash
kubectl logs <pod-name> -c <init-container-1>
~~~

如果 Pod 的状态以 **Init:** 开头，表示该 Pod 正在执行初始化容器

| 状态                       | 描述                                                       |
| -------------------------- | ---------------------------------------------------------- |
| Init:N/M                   | Pod 中包含 M 个初始化容器，其中 N 个初始化容器已经成功执行 |
| Init:Error                 | Pod 初始化容器执行失败                                     |
| Init:CrashLoopBackOff      | Pod 初始化容器反复执行失败                                 |
| Pending                    | Pod 还未开始执行初始化容器                                 |
| PodInitializing or Running | Pod 已经完成初始化容器的执行                               |



## 11、Disruptions

### 1、概述

除非人为销毁 Pod，或者出现不可避免的硬件/软件故障，否则 Pod 不会凭空消失，此类不可避免的情况，称之为非自愿的干扰

例如：

- 节点所在物理机的硬件故障
- 集群管理员误删了虚拟机
- 云供应商或管理程序故障导致虚拟机被删
- Linux 内核故障
- 集群所在的网络发生分片，导致节点不可用
- 节点资源耗尽，导致 Pod 被驱逐

还有一类毁坏，称之为自愿的干扰，主要包括由应用管理员或集群管理员主动执行的操作

应用管理员可能执行的操作有：

- 删除 Deployment 或其他用于管理 Pod 的控制器
- 修改 Deployment 中 Pod 模板的定义，导致 Pod 重启
- 直接删除一个 Pod

集群管理员可能执行的操作有：

- 排空节点，以便维修、升级、集群缩容
- 从节点上删除某个 Pod，以使得其他的 Pod 能够调度到该节点上



**注意**：

- 并非所有自愿的干扰都受 Pod Disruption Budgets 限制，例如：删除 Deployment 或 Pod



### 2、处理干扰

弥补非自愿干扰可以采取的方法有：

- 确保的 Pod 申请合适的计算资源
- 如果需要高可用，运行多个副本
- 如果需要更高的高可用性，将应用程序副本分布到多个机架上或分不到多个地区

自愿干扰，发生频率不定，在一个基础的 Kubernetes 集群中，可能不会发生自愿干扰，当集群管理员或者托管供应商运行某些额外的服务是可能导致自愿干扰发生，例如：

- 更新节点上的软件
- 自定义实现的自动伸缩程序

Kubernetes 提供了 Disruption Budget 这一特性，以在高频次自愿干扰情况下，仍然运行高可用的应用程序

应用程序管理员可以为每一个应用程序创建 PodDisruptionBudget 对象（PDB），PDB 限制了多副本应用程序在自愿干扰情况发生时，最多有多少个副本可以同时停止，例如：一个 web 前端的程序需要确保可用的副本数不低于总副本数的一定比例

集群管理员以及托管供应商在进行系统维护时，应该使用兼容 PodDisruptionBudget 的工具（例如：kubectl drain，此类工具调用 Eviction API，而不是直接删除 Pod 或者 Deployment，kubectl drain 命令会尝试将节点上所有的 Pod 驱逐掉，驱逐请求可能会临时被拒绝，kubectl drain 将周期性地重试失败的请求，直到节点上所有的 Pod 都以终止，或者直到超过了预先配置的超时时间

PDB 指定了应用程序最少期望的副本数（相对于总副本数）例如：某个 Deployment 的 .spec.replicas 为 5，如果其对应的 PDB 允许最低 4个副本数，则 Eviction API（kubectl drain）在同一时刻最多会允许1个自愿干扰，而不是2个或更多

- PDB 通过 Pod 的 .metadata.ownerReferences 查找到其对应的控制器（Deployment、StatefulSet）
- PDB 通过控制器的 .spec.replicas 字段来确定期望的副本数
- PDB 通过控制器的 label selector 来确定哪些 Pod 属于同一个应用程序
- PDB 不能阻止非自愿干扰发生，但是当这类毁坏发生时，将被计入到当前毁坏数里
- 通过 kubectl drain 驱逐 Pod 时，Pod 将被优雅地终止



**注意**：

- 在滚动更新过程中被删除的 Pod 也将计入到 PDB 的当前干扰数，但是控制器在执行滚动更新时，并不受 PDB 的约束，滚动更新过程中，同时可删除 Pod 数量在控制器对象的定义中规定



Kubernetes 中如下因素决定了干扰发生的频率：（也即干扰时删除 Pod 的速度）

- 应用程序所需要的副本数
- 对一个 Pod 执行优雅终止（gracefully shutdown）所需要的时间
- 新的 Pod 实例启动所需要的时间
- 控制器的类型
- 集群资源的容量



### 3、PodDisruptionBudget

使用 PodDisruptionBudget 保护应用程序：

1. 首先要确定哪个应用程序需要使用 PodDisruptionBudget 保护
2. 思考应用程序如何处理干扰
3. 创建 PDB yaml 文件
4. 从 yaml 文件创建 PDB 对象

通常如下几种 Kubernetes 控制器创建的实例可以使用 PDB：

- Deployment

- ReplicationController
- ReplicaSet
- StatefulSet



PodDisruptionBudget 包含三个字段：

- .spec.selector：用于指定 PDB 适用的 Pod，此字段为必填
- .spec.minAvailable：当完成驱逐时，最少要保留多少个 Pod 可用，该字段可以是一个整数，也可以是一个百分比
- .spec.maxUnavailable： 当完成驱逐时，最多有多少个 Pod 被终止，该字段可以是一个整数，也可以是一个百分比



**注意**：

- 在一个 PodDisruptionBudget 中，只能指定 maxUnavailable 和 minAvailable 中的一个

- maxUnavailable 只能应用到那些有控制器的 Pod 上
- 通常一个 PDB 对应一个控制器创建的 Pod，例如：Deployment、ReplicaSet、StatefulSet
- PodDisruptionBudget 只能保护应用避免受到自愿干扰的影响，而不是所有原因的毁坏
- PDB 中 .spec.selector 字段的内容必须与控制器中 .spec.selector 字段的内容相同
- maxUnavailable 为 0%（或0）或者 minAvailable 为 100%（或与控制器的 .spec.replicas 相等）将阻止节点排空任务
- 当名称空间中有多个 PDB 时，必须小心，PDB 的标签选择器之间不能重叠



>自 Kubernetes v 1.15 开始，PDB 支持激活了 scale subresource 的 custom controller，也可以为那些不是通过上述控制器创建的 Pod 设置 PDB，但存在一些限制条件
>
>- 只能使用 .spec.minAvailable，不能使用 .spec.maxUnavailable
>- .spec.minAvailable 字段中只能使用整型数字，不能使用百分比



~~~yaml
apiVersion: policy/v1beta1
kind: PodDisruptionBudget
metadata:
  name: zk-pdb
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: zookeeper
~~~

~~~bash
apiVersion: policy/v1beta1
kind: PodDisruptionBudget
metadata:
  name: zk-pdb
spec:
  maxUnavailable: 1
  selector:
    matchLabels:
      app: zookeeper
~~~

~~~bash
# 创建 pdb 对象
kubectl apply -f mypdb.yaml
~~~

~~~bash
# 查看 pdb 详细信息
kubectl get poddisruptionbudgets zk-pdb -o yaml
~~~



## 12、调试

通过 kubectl exec 进入容器命令行终端进行问题诊断

~~~bash
# Pod 中只有一个容器时
kubectl exec -it pod-name /bin/bash

# Pod 中有多个容器时
kubectl exec -it pod-name -c container-name /bin/bash
~~~

使用开源工具 **kubectl-debug**



# 5、Kubernetes Label

## 1、基本概念

一个 Label 是一个 **k : v** 键值对，key 和 value 均自定义，但是有一定的限制，见下文

Label 可以附加到各种资源对象上，如 Node、Pod、 Service、RC，一个资源对象可以定义任意数量的 Label， 同一个 Label 也可以被添加到任意数量的资源对象上

Label 通常在资源对象定义时确定，也可以在对象创建后动态添加或删除

Label 的最常见的用法是使用 metadata.labels 字段，来为对象添加 Label，通过 spec.selector 来引用对象

Label 附加到 Kubernetes 集群中各种资源对象上，目的就是对这些资源对象进行分组管理， 而分组管理的核心就是 Label Selector

Label 与 Label Selector 都不能单独定义，必须附加在一些资源对象的定义文件上，一般附加在 RC 和 Service 的资源定义文件中



标签名：

- 标签名部分是必须的
- 不能多于 63 个字符
- 必须由字母、数字开始和结尾
- 可以包含字母、数字、减号-、下划线_、小数点.

标签前缀：

- 标签前缀部分是可选的
- 如果指定，必须是一个DNS的子域名
  - 如果省略标签前缀，则标签的 key 将被认为是专属于用户的
  - Kubernetes的系统组件向用户的Kubernetes对象添加标签时，必须指定一个前缀
  - kubernetes.io/ 和 k8s.io/ 这两个前缀是 Kubernetes 核心组件预留
- 不能多于 253 个字符
- 使用 / 和标签名分隔

标签的值：

- 不能多于 63 个字符
- 可以为空字符串
- 如果不为空，则
  - 必须由字母、数字开始和结尾
  - 可以包含字母、数字、减号-、下划线_、小数点.



## 2、标签选择器

与 name 和 UID 不同，标签不一定是唯一的，通常 K8s对象与标签是 M：N 的关系

通过使用标签选择器（Label Selector），可以选择一组对象

标签选择器是 Kubernetes 中最主要的分类和筛选手段

Kubernetes api server支持两种形式的标签选择器，**equality-based** 基于等式的和 **set-based** 基于集合的

- 均支持 ==、=、!=
  - == 等同于 =

- 基于集合：
  - 使用小括号表示集合
  - 额外支持 not in、int、exists 条件

标签选择器可以包含多个条件，并使用逗号分隔，逗号等同于 and，此时只有满足所有条件的 Kubernetes 对象才会被选中，如果使用空的标签选择器或者不指定选择器，其含义由具体的 API 接口决定

~~~bash
environment = production
tier != frontend
environment=production,tier!=frontend
~~~

~~~bash
environment in (production, qa)
tier notin (frontend, backend)
# 具有 partition key
partition
!partition
~~~



API 查询：两种类型都能支持，但要符合 URL 编码

- 基于等式的选择方式： ?labelSelector=environment%3Dproduction,tier%3Dfrontend
- 基于集合的选择方式： ?labelSelector=environment+in+%28production%2Cqa%29%2Ctier+in+%28frontend%29



Job、Deployment、ReplicaSet、DaemonSet 同时支持基于等式的选择方式和基于集合的选择方式

~~~yaml
selector:
  matchLabels:
    component: redis
  matchExpressions:
    - {key: tier, operator: In, values: [cache]}
    - {key: environment, operator: NotIn, values: [dev]}
~~~

matchLabels 是一个 {key,value} 组成的 map，map 中的一个 {key,value} 条目相当于 matchExpressions 中的一个元素，其 key 为 map 的 key，operator 为 In， values 数组则只包含 value 一个元素

matchExpression 等价于基于集合的选择方式，支持的 operator 有 In、NotIn、Exists 和 DoesNotExist，当 operator 为 In 或 NotIn 时，values 数组不能为空，所有的选择条件都以 AND 的形式合并计算，即所有的条件都满足才可以算是匹配



## 3、使用

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



## 4、字段选择器

字段选择器（Field Selector）可以用来基于的一个或多个字段的取值来选取一组 Kubernetes 对象，类似于 Label

字段选择器本质上是一个 filter，默认情况下，没有添加 selector/filter 时，代表着指定资源类型的所有对象都被选中

~~~bash
# 选择了所有字段 status.phase 的取值为 Running 的 Pod
kubectl get pods --field-selector status.phase=Running
~~~

不同的 Kubernetes 对象类型，可以用来查询的字段不一样。所有的对象类型都支持的两个字段是 metadata.name 和 metadata.namespace。在字段选择器中使用不支持的字段，将报错

字段选择器中可以使用的操作符有 =、==、!= （= 和 == 含义相同），可以指定多个字段，用逗号 , 分隔

~~~bash
kubectl get pods --field-selector=status.phase!=Running,spec.restartPolicy=Always
~~~

字段选择器可以跨资源类型使用

~~~bash
kubectl get statefulsets,services --all-namespaces --field-selector metadata.namespace!=default
~~~



# 6、Kubernetes Controller

Kubernetes 通过引入 Controller（控制器）的概念来管理 Pod 实例，在 Kubernetes 中，应该始终通过创建 Controller 来创建 Pod，而不是直接创建 Pod（非常重要）

在 Kubernetes 中，每个控制器至少追踪一种类型的资源，这些资源对象中有一个 spec 字段代表了预期状态，资源对象对应的控制器负责不断地将当前状态调整到预期状态

理论上，控制器可以直接执行调整动作，然而在 Kubernetes 普遍的做法是，控制器发送消息到 APIServer，间接去调整

作为一个底层设计原则，Kubernetes 使用了大量的控制器，每个控制器都用来管理集群状态的某一个方面，因此可能存在多种控制器可以创建或更新相同类型的 API 对象，为了避免混淆，Kubernetes 控制器在创建新的 API 对象时，会将该对象与对应的控制器关联，例如：Deployment 和 Job，这两类控制器都创建 Pod，但是 Job Controller 不会删除 Deployment Controller 创建的 Pod，因为控制器可以通过标签信息区分哪些 Pod 是它创建的

**在 Kubernetes 支持的控制器有如下几种：**

- Deployment 
- StatefulSet
- DaemonSet
- CronJob
- Jobs - Run to Completion
- ReplicaSet
- ReplicationController
- Garbage Collection
- TTL Controller for Finished Resources



## 1、Replication Controller

### 1、概述

Replication Controller（RC）是 Kubernetes 系统中核心概念之一，当定义了一个 RC 并提交到 Kubernetes 集群中以后，Master 节点上的 Controller Manager 组件就得到通知，定期检查系统中存活的 Pod，并确保目标 Pod 实例的数量刚好等于 RC 的预期值，如果有过 多或过少的 Pod 运行，系统就会停掉或创建一些 Pod，此外可以通过修改 RC 的副本数量，来实现 Pod 的动态缩放功能

~~~bash
kubectl scale rc nginx --replicas=5
~~~

由于 Replication Controller 与 Kubernetes 代码中的模块 Replication Controller 同名， 所以在 Kubernetes v1.2 时， 它就升级成了另外一个新的概念 ReplicaSet，官方解释为下一代的 RC，RS 与 RC 区别是：ReplicaSet 支援基于集合的 Label selector，而 RC 只支持基于等式的 Label Selector

很少单独使用 ReplicaSet 与 Replication Controller，它们主要被 Deployment 这个更高层面的资源对象所使用，从而形成一整套 Pod 创建、删除、更新的编排机制

最好不要越过 RC 直接创建 Pod，因为 Replication Controller 会通过 RC 管理 Pod 副本，实现自动创建、补足、替换、删除 Pod 副本，这样就能提高应用的容灾能力，减少由于节点崩溃等意外状况造成的损失，即使应用程序只有一个 Pod 副本，也强烈建议使用 RC 来定 义 Pod



**注意**：

- Replication Controller 不支持基于集合的选择器，推荐使用 ReplicaSet 而不是 Replication Controller



### 2、ReplicaSet 

#### 1、概述

Replica Set 跟 Replication Controller 没有本质的不同，只是名字不一样，并且 Replica Set 支持集合式的 Selector（Replication Controller 仅支持等式）

Kubernetes 官方强烈建议避免直接使用 ReplicaSet，而应该通过 Deployment 来创建 RS 和 Pod，由于 ReplicaSet 是 Replication Controller 的代替物，因此用法基本相同

ReplicaSet 创建的 Pod 中，有一个字段 metadata.**ownerReferences** 用于标识该 Pod 从属于哪一个 ReplicaSet，如果 Pod 没有 ownerReference 字段，或者 ownerReference 字段指向的对象不是一个控制器，但是该 Pod 匹配了 ReplicaSet 的 selector，则该 Pod 的 ownerReference 将被修改为该 ReplicaSet 的引用

ReplicaSet的定义中，包含：

- **apiVersion**：apps/v1
- **kind**：始终为 ReplicaSet
- **metadata**：一些元数据
- **spec**： ReplicaSet 的详细定义

- **selector**： 用于指定哪些 Pod 属于该 ReplicaSet 的管辖范围
- **replicas**： 副本数，用于指定该 ReplicaSet 应该维持多少个 Pod 副本
- **template**： Pod模板，在 ReplicaSet 使用 Pod 模板的定义创建新的 Pod
  - 其中必须定义 .spec.template.metadata.labels 字段
  - .spec.template.spec.restartPolicy 的默认值为 Always



#### 2、使用

~~~yaml
# 创建 ReplicaSet
apiVersion: apps/v1
kind: ReplicaSet
metadata:
  name: frontend
  labels:
    app: guestbook
    tier: frontend
spec:
  # 创建三副本
  replicas: 3
  selector:
    matchLabels:
      tier: frontend
  template:
    metadata:
      labels:
        tier: frontend
    spec:
      containers:
      - name: nginx
        image: nginx
~~~

~~~bash
# 查看刚才创建的 rs
kubectl get rs

# 查看详情
kubectl describe rs/frontend

# 清理 RS 与 Pod
# Garbage Collector 将自动删除该 ReplicaSet 所有从属的 Pod
kubectl delete -f rs/frontend

# 只删除 RS，保留从属 Pod
kubectl delete --cascade=false
~~~



**注意**：

- 如果通过直接创建 Pod，并且标签被 RS 匹配到，则该 Pod 将会立刻被管控，如果此时 Pod 副本数量超过预期值，新 Pod 立刻终止
- 其中 Pod Template 内的标签最好不要与其他 RS 重叠，避免被其余 RS 管控
- 当旧 RS 被删除，只要新 RS 的 .spec.selector 字段与旧 RS 的 .spec.selector 字段相同，则新的 RS 将接管旧 RS 的从属 Pod
  - 新的 RS 中定义的 .spec.template 对遗留下来的 Pod 不会产生任何影响

- ReplicaSet 不直接支持滚动更新



#### 3、伸缩

RS 可以轻易的 scale up 或者 scale down，只需要修改 .spec.replicas 字段即可

RS 控制器将确保与其标签选择器 .spec.selector 匹配的 Pod 数量与 replicas 指定的数量相等

可以使用 Horizontal Pod Autoscalers(HPA) 对 RS 执行自动的水平伸缩，或者使用命令 kubectl autoscale rs frontend --max=10

~~~bash
apiVersion: autoscaling/v1
kind: HorizontalPodAutoscaler
metadata:
  name: frontend-scaler
spec:
  scaleTargetRef:
    kind: ReplicaSet
    name: frontend
  minReplicas: 3
  maxReplicas: 10
  targetCPUUtilizationPercentage: 50
~~~



## 2、Deployment

### 1、概述

Deployment 是 Kubenetes v1.2 引入的新概念，引入的目的是为了更好的解决 Pod 的编排问题

Deployment 内部使用了 ReplicaSet 来实现，Deployment 的定义与 ReplicaSet 的 定义很类似，除了 API 声明与 Kind 类型有所区别

Deployment 是一个更高级别的概念，是最常用的用于部署无状态服务的方式，并可以声明式的更新与管理 ReplicaSet、Pod，以及其他的许多有用的特性



### 2、使用

#### 1、创建

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  labels:
    app: nginx
spec:
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.7.9
        ports:
        - containerPort: 80
~~~

~~~~bash
# 部署 Deploy
kubectl apply -f xxx.yaml
~~~~

可以为该命令增加 --record 选项，此举 kubectl 会将刚运行的命令写入 Deployment 的 annotation 的 kubernetes.io/change-cause，以便后续维护查看

~~~bash
# 查看 Deployment 的发布状态（rollout status），
kubectl rollout status deployment.v1.apps/deploy-name

# 查看 Deploy 创建的 RS
kubectl get rs

# 查看 Pod Label
kubectl get pods --show-labels
~~~



**注意**：

- 控制器之间的 .spec.**selector** 和 .template.metadata.**labels**，尽量确保不同，否则可能发生冲突，并产生不可预见的行为
- pod-template-hash 标签是 Deployment 创建 RS 时添加到 RS 上的，RS 将此标签添加到 Pod 上，这个标签用于区分 Deployment 中哪个 RS 创建了哪些 Pod，切勿修改



#### 2、更新

当且仅当 Deployment 的 Pod template（.spec.**template**）字段中的内容发生变更时（例如：标签、容器的镜像被改变），Deployment 的发布更新（rollout）将被触发

Deployment 中其他字段的变化（例如：修改 .spec.replicas）将不会触发 Deployment 的发布更新（rollout）



**rollout**：

- 每创建一个 Deployment，Deployment Controller 都为其创建一个 RS，并设定其副本数为期望的 Pod 数，如果 Deployment 被更新，旧的 RS 将被 Scale down，新建的 RS 将被 Scale up，直到最后新旧两个 RS，一个副本数为 .spec.replias，另一个副本数为 0



~~~bash
# 修改镜像 tag
kubectl --record deployment.apps/nginx-deployment set image deployment.v1.apps/nginx-deployment nginx=nginx:1.9.1

# 查看 RS 情况
kubectl get rs
~~~

Deployment 的更新是通过创建一个新的 RS 并同时将旧的 RS 的副本数缩容到 0 个副本来达成的

- Deployment 将确保更新过程中，任意时刻只有一定数量的 Pod 被关闭，默认情况下，Deployment 确保至少 .spec.replicas 的 75% 的 Pod 保持可用（25% max unavailable）
- Deployment 将确保更新过程中，任意时刻只有一定数量的 Pod 被创建。默认情况下，Deployment 确保最多 .spec.replicas 的 25% 的 Pod 被创建（25% max surge）



**覆盖更新**：

当 Deployment 的 rollout 正在进行中的时候，如果再次更新 Deployment 的信息，此时 Deployment 将再创建一个新的 RS 并开始 scale up，将先前正在 scale up 的新 RS 也转为旧的 RS，并立刻 scale down



## 3、Horizontal Pod Autoscaler

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



### 3、安装 nfs

创建目录，开放权限

~~~bash
# 所有节点都要创建该目录
mkdir -p /home/data
# 主节点开放
echo "/home/data *(insecure,rw,sync,no_root_squash)" > /etc/exports
# 主节点重启服务
systemctl restart nfs-server
~~~

~~~bash
# 从节点挂载
mount 196.198.168.168:/home/data /home/data
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



# 11、Kubernetes Namspace

## 1、Name 概念

Kubernetes REST API 中，所有的对象都是通过 name 和 UID 唯一性确定，例如确定一个 RESTFUL 对象：

```
/api/v1/namespaces/{namespace}/pods/{name}
```

同一个名称空间下，同一个类型的对象，可以通过 name 唯一性确定

~~~yaml
# 例子
apiVersion: v1
kind: Pod
metadata:
	# Pod Name
  name: nginx-demo
spec:
  containers:
  # Contain Name
  - name: nginx
    image: nginx:1.14.2
    ports:
    - containerPort: 80
~~~



## 2、基本概念

Namespace 在很多情况下用于实现多用户的资源隔离，通过将集群内部的资源对象分配到不同的 Namespace 中形成逻辑上的分组，便于不同的分组在共享使用整个集群的资源同时还能被分别管理

Kubernetes 安装成功后，默认有初始化了三个名称空间：

- **default**：默认名称空间，如果 Kubernetes 对象中不定义 metadata.namespace 字段，该对象将放在此名称空间下
- **kube-system**：Kubernetes 系统创建的对象放在此名称空间下
- **kube-public**：此名称空间自动在安装集群是自动创建，并且所有用户都是可以读取的（即使是那些未登录的用户），主要是为集群预留的，例如：某些情况下，某些 Kubernetes 对象应该被所有集群用户看到

当您创建一个 Service 时，Kubernetes 为其创建一个对应的 DNS 条目，格式为 <service-name>.<namespace-name>.svc.cluster.local，在容器中只使用 <service-name>，其DNS将解析到同名称空间下的 Service

某些更底层的对象，是不在任何名称空间中的，例如 nodes、persistentVolumes、storageClass 等



**注意**：

- 名称空间内部的同类型对象不能重名，但是跨名称空间可以有同名同类型对象
- 名称空间不可以嵌套，任何一个 Kubernetes 对象只能在一个名称空间中



~~~bash
# 查看名称空间
kubectl get namespaces

# 在名称空间里
kubectl api-resources --namespaced=true
# 不在名称空间里
kubectl api-resources --namespaced=false
~~~



## 3、使用

### 1、创建

~~~yaml
apiVersion: v1 
kind: Namespace 
metadata:
	name: development

---------------------

apiVersion: v1 
kind: Pod 
metadata:
	name: busybox 
	namespace: development
spec:
containers:
    - image: busybox 
    command:
    	- sleep
    	- -"3600"
    name: busybox
~~~

~~~bash
kubectl get pods --namespace=development
~~~

~~~bash
# 执行命令时指定名称空间
kubectl run nginx --image=nginx --namespace=<您的名称空间>
kubectl get pods --namespace=<您的名称空间>
~~~



### 2、设置名称空间偏好

以通过 set-context 命令改变当前 kubectl 上下文 的名称空间，后续所有命令都默认在此名称空间下执行

~~~bash
kubectl config set-context --current --namespace=<您的名称空间>
# 验证结果
kubectl config view --minify | grep namespace:
~~~











# 12、Kubernetes Service

## 1、基本概念

Service 是 Kubernetes 最核心概念，通过创建 Service，可以为一组具有相同功能的容器应用提供一个统一的入口地址，并且将请求负载分发到后端的各个容器应用上



## 2、定义

~~~yaml
apiVersion: v1 
kind: Service 
matadata:
	name: string 
	namespace: string 
	labels:
        - name: string 
    annotations:
        - name: string 
spec:
	selector: [] 
	type: string 
	clusterIP: string
	sessionAffinity: string 
	ports:
		- name: string 
		protocol: string 
		port: int 
		targetPort: int 
		nodePort: int
	status: 
		loadBalancer:
			ingress:
				ip: string 
				hostname: string
~~~

| 属性                                 | 类型   | 说明                                                         |
| ------------------------------------ | ------ | ------------------------------------------------------------ |
| spec.type                            | String | Service 的类型，指定 Service 的访问类型，默认值为 ClusterIP，可选值为ClusterIP、NodePort、LoadBalancer， ClusterIP： 用于 Kubernetes 集群内部的 Pod 访问，通过 Node 的 kube-proxy 设置的 iptables 规则进行转发，NodePort：使用宿主机的端口，通过 NodeIP 与端口访问，LoadBalancer：使用外接的负载均衡器完成负载分发，需要在 spec.status.loadBalancer 指定外部负载均衡器的IP，并同时定义 NodePort、ClusterIP，通常用于公网环境 |
| spec.clusterIP                       | String | 当 type = clusterIP 时，如果不指定，则系统自动分配，如果 type = loadBalancer，需要手动指定 |
| spec.sessionAffinity                 | String | 是否支持 session，可选值为 ClientIP，表示将同一个 IP 的请求转发到 同一个 Pod 中，默认值为空 |
| spec.ports[]                         | List   | Service 服务需要暴露的端口                                   |
| spec.ports[].name                    | String | 端口名称                                                     |
| spec.ports[].protocol                | String | 端口协议，支持 TCP UDP，默认 TCP                             |
| spec.ports[].port                    | int    | 服务监听的端口号                                             |
| spec.ports[].targetPort              | int    | 需要转发到 Pod 的端口号                                      |
| spec.ports[].nodePort                | int    | 当 type 为 nodePort 时，指定映射到主机的端口号               |
| status                               | Object | 当 type 为 loadBalancer 时，设置外部负载均衡器 IP 地址       |
| status.loadBalancer                  | Object | 外部负载均衡器                                               |
| status.loadBalancer.ingress          | Object | 外部负载均衡器                                               |
| status.loadBalancer.ingress.ip       | String | 外部负载均衡器 IP                                            |
| status.loadBalancer.ingress.hostname | String | 外部负载均衡器主机名                                         |

ClusterIP：虚拟服务IP，在公网环境搭建即为 MasterIP



## 2、使用

对外提供服务的应用程序需要通过某种机制来实现，对于容器应用最简便的方式就是通过 TCP/IP 机制及监听 IP 和端口号来实现

~~~yaml
# 首先创建具备一个基本功能的服务
apiVersion: v1
kind: ReplicationController 
metadata:
	name: mywebapp 
spec:
	replicas: 2 
template:
	metadata:
		name: mywebapp 
		labels:
			app: mywebapp 
	spec:
		containers:
			- name: mywebapp 
			image: tomcat 
			ports:
				- containerPort: 8080
~~~

可以通过 

~~~bash
kubectl get pods -l app=mywebapp -o yaml | grep podIP
~~~

来获取 Pod 的 IP 地址和端口号来访问 Tomcat 服务，但是直接通过 Pod 的 IP 地址和端口访问应用服务是不可靠的，因为当 Pod 所在的 Node 发生故障时， Pod 将被 Kkubernetes 重新调度到另一台 Node，Pod 的地址会发生改变

可以通过配置文件来定义 Service，通过 kubectl create 来创建，就可以通过 Service 地址来访问后端的 Pod

~~~yaml
apiVersion: v1 
kind: Service 
metadata:
	name: mywebAppService 
spec:
	ports:
		- port: 8081
		targetPort: 8080 
	selector:
		app: mywebapp
~~~

有时一个容器应用可能需要提供多个端口的服务，那么在 Service 的定义中也可以相应地将多个端口对应到多个应用服务

~~~yaml
# 多端口 Service
apiVersion: v1 
kind: Service 
metadata:
	name: mywebAppService
spec:
	ports:
		- port: 8080
		targetPort: 8080 
		name: web
		- port: 8005
		targetPort: 8005 
		name: management
    selector:
        app: mywebapp
~~~

假如应用系统需要将一个外部数据库作为后端服务进行连接，或将另一个集群或 Namespace 中的服务作为服务的后端，这时可以通过创建一个无 Label Selector 的 Service 来实现

~~~yaml
# 外部服务 Service
apiVersion: v1 
kind: Service 
metadata:
	name: my-service 
spec:
	ports:
		- protocol: TCP 
		port: 80
		targetPort: 80
--------------------------
apiVersion: v1
kind: Endpoints 
metadata:
	name: my-service 
subsets:
	- addresses:
	- IP: 10.254.74.3
ports:
	- port: 8080
~~~



# 13、Kubernetes Probe

## 1、基本概念

Kubernetes 存在两种类型的探针：liveness probe、readiness probe

- Liveness 适用场景是支持那些可以重新拉起的应用
- Readiness 主要应对的是启动之后无法立即对外提供服务的这些应用

每类探针都支持三种探测方法：

- **exec**：通过执行命令来检查服务是否正常，针对复杂检测或无 HTTP 接口的服务，命令返回值为 0 则表示容器健康
- **httpGet**：通过发送 http 请求检查服务是否正常，返回 200-399 状态码则表明容器健康
- **tcpSocket**：通过容器的 IP 和 Port 执行 TCP 检查，如果能够建立 TCP 连接，则表明容器健康

探针探测结果:

- **Success**：Container 通过了检查
- **Failure**：Container 未通过检查
- **Unknown**：未能执行检查，不采取任何措施



![35933da46310f1becb0fe2c6335968a8aecce931.a73f1073](images/35933da46310f1becb0fe2c6335968a8aecce931.a73f1073.png)



## 2、Liveness Probe

存活探针用于判断容器是否存活，即 Pod 是否为 running 状态

有时应用程序可能因为某些原因（后端服务故障等）导致暂时无法对外提供服务，但应用软件没有终止，导致 Kubernetes 无法隔离有故障的 Pod，调用者可能会访问到有故障的 Pod，导致业务不稳定，Kubernetes 提供存活探针来检测应用程序是否正常运行，并且对相应状况进行相应的补救措施

- 如果存活探针探测到容器不健康，则 Kubelet 将 kill 掉容器，并执行容器的重启策略
- 如果一个容器不包含存活探针，则 Kubelet 认为容器的存活探针的返回值为健康



## 3、Readiness Probe

就绪探针用于判断容器是否启动完成，即容器的 Ready 状态是否为 True，如果就绪探针探测失败，则容器的 Ready 状态将为 False，控制器将此 Pod 的 Endpoint 从对应的 Service 的 Endpoint 列表中移除，且不将任何请求调度到此 Pod，直到下次探测成功

通过使用就绪探针，Kubernetes 能够等待应用程序完全启动，才允许服务将流量发送到新副本

比如：使用 Tomcat 的应用程序来说，并不是简单地说 Tomcat 启动成功就可以对外提供服务的，还需要等待 Spring 容器初始化，数据库连接等等。对于 Spring Boot 应用，默认的 actuator 带有 /health 接口，可以用来进行启动成功的判断



## 4、示例

~~~yaml
apiVersion: v1
kind: Pod
metadata:
	name: goproxy
	labels:
		app: goproxy
spec:
	containers:
		- name: goproxy
		image: k8s.gcr.io/goproxy:0.1
	ports:
		- containerPort: 8080
	readinessProbe:
		tcpSocket:
			port: 8080
		initialDelaySeconds: 5	# 容器启动后第一次执行探测是需要等待多少秒
		periodSeconds: 10	# 执行探测的频率。默认是 10 秒，最小 1 秒
	livenessProbe:
		tcpSocket:
			port: 8080
		initialDelaySeconds: 15
		periodSeconds: 20
~~~

其余参数：

- timeoutSeconds：探测超时时间，默认 1 秒，最小 1 秒
- successThreshold：探测失败后，最少连续探测成功多少次才被认定为成功，默认是 1，对于 liveness 必须是 1，最小值是 1 
- failureThreshold：探测成功后，最少连续探测失败多少次才被认定为失败，默认是 3，最小值是 1



# 14、Kubernetes Scheduler

## 1、基本概念

一个容器平台的主要功能就是为容器分配运行时所需要的计算、存储、网络资源

容器调度系统负责选择在最合适的主机上启动容器，并且将它们关联起来

容器调度系统必须能自动的处理容器故障并且在更多的主机上自动启动更多的容器来应对更多的应用访问

目前三大主流的容器平台 Swarm、Mesos、Kubernetes 具有不同的容器调度系统

- Swarm 特点是直接调度 Docker 容器，并且提供和标准 Docker API 一致的 API
- Mesos 针对不同的运行框架采用相对独立的调度系统，其中 Marathon 框架提供了 Docker 容器的原生支持
- Kubernetes 则采用 Pod 和 Label 这样的概念把容器组合成互相依赖的逻辑单元，相关容器被组合成 Pod 后被共同部署和调度，形成服务（Service），这个是 Kubernetes 和 Swarm、Mesos 的主要区别，相对来说，Kubernetes 采用这样的方式简化了集群范围内相关容器被共同调度管理的复杂性，同时这也能够相对容易的支持更强大，复杂的容器调度算法



kube-scheduler 是 Kubernetes 系统的核心组件之一，主要负责整个集群资源的调度功能，根据特定的调度算法和策略，将 Pod 调度到最优的工作节点上面去，从而更加合理、充分的利用集群资源

kube-scheduler 是一个独立的二进制程序，启动之后会一直监听 API Server，获取到 PodSpec.NodeName 为空的 Pod，对每个 Pod 都会创建一个 binding

默认情况下，kube-scheduler 提供的默认调度器能够满足绝大多数的要求，可以保证 Pod 可以被分配到资源充足的节点上运行，但是在实际的线上项目中，可能需要调度器能够可控

Kubernetes 的资源分为两种属性：（未来 Kubernetes 会加入更多资源，如网络带宽，存储 IOPS 的支持）

- 可压缩资源：
  - 例如：CPU、Disk I/O，都是可以被限制和被回收的，对于一个 Pod 来说可以降低这些资源的使用量而不去杀掉 Pod
- 不可压缩资源：
  - 例如内存、硬盘空间，一般来说不杀掉 Pod 就没法回收



<img src="images/image-20230112153405208.png" alt="image-20230112153405208" style="zoom:80%;" />

## 2、调度流程

调度主要分为以下几个部分：（简略版）

1. 预选过程：过滤掉不满足条件的节点，这个过程称为 Predicates
   - 首先遍历全部节点，过滤掉不满足条件的节点，属于强制性规则
   - 这一阶段所有满足要求的 Node 将被记录并作为第二阶段的输入
   - 如果所有的节点都不满足条件，那么 Pod 将会一直处于 Pending 状态，直到有节点满足条件，在这期间调度器会不断的重试，所以在部署应用的时候，如果发现有 Pod 一直处于 Pending 状态，那么就是没有满足调度条件的节点，这个时候可以去检查下节点资源是否可用
2. 优选过程：对通过的节点按照优先级排序，称之为 Priorities 
   - 再次对节点进行筛选，如果有多个节点都满足条件的话，那么系统会按照节点的优先级（priorites）大小对节点进行排序
3. 最后选择优先级最高的节点

注意：

- 如果中间任何一步骤有错误，就直接返回错误



调度主要分为以下几个部分：（详细版）

1. Client 通过 API Server 的 REST API 或者 Kubectl 工具创建 Pod 资源
2. API Server 收到请求后，存储相关数据到 etcd 数据库
3. 调度器监听 API Server 查看待调度（bind）的 Pod 列表，循环遍历地为每个 Pod 尝试分配节点
   1. 预选阶段：Predicates
      - 过滤节点，调度器用一组规则过滤掉不符合要求的 Node 节点，比如：Pod 设置了资源的 request，那么可用资源比 Pod 需要的资源少的主机显然就会被过滤掉
   2. 优选阶段：Priorities
      - 为节点的优先级打分，将上一阶段过滤出来的 Node 列表进行打分，调度器会考虑一些整体的优化策略，比如：把  Deployment 控制的多个 Pod 副本分布到不同的主机上，使用最低负载的主机等等
   3. 经过上面的阶段过滤后，选择打分最高的 Node 节点与 Pod 进行 binding 操作，然后将结果存储到 etcd 中
   4. 最后交给被选择出来的 Node 节点上运行的 Kubelet 去执行创建 Pod 的相关操作



Predicates 有一系列的过滤算法使用：

- PodFitsResources：节点上剩余的资源是否大于 Pod 请求的资源 
- PodFitsHost：如果 Pod 指定了 NodeName，检查节点名称是否和 NodeName 匹配 
- PodFitsHostPorts：节点上已经使用的 port 是否和 Pod 申请的 port 冲突 
- PodSelectorMatches：过滤掉和 Pod 指定的 Label 不匹配的节点 
- NoDiskConflict：已经 mount 的 volume 和 Pod 指定的 volume 不冲突，除非它们都是只读的 
- CheckNodeDiskPressure：检查节点磁盘空间是否符合要求 
- CheckNodeMemoryPressure：检查节点内存是否够用



Priorities 优先级是由一系列键值对组成的，键是该优先级的名称，值是它的权重值：

- LeastRequestedPriority：通过计算 CPU 和内存的使用率来决定权重，使用率越低权重越高，当然正常肯定也是资源是使用率越低权重越高，能给别的 Pod 运行的可能性就越大 
- SelectorSpreadPriority：为了更好的高可用，对同属于一个 Deployment 或者 RC 下面的多个 Pod 副本，尽量调度到多个不同的节点上，当一个 Pod 被调度的时候，会先去查找该 Pod 对应的 Controller，然后查看该 Controller 下面的已存在的 Pod，运行 Pod 越少的节点权重越高 
- ImageLocalityPriority：就是如果在某个节点上已经有要使用的镜像节点了，镜像总大小值越大，权重就越高 
- NodeAffinityPriority：这个就是根据节点的亲和性来计算一个权重值



## 3、Node 调度亲和性

### 1、概述

节点亲和性规则：

- 硬亲和性 required 
- 软亲和性 preferred

硬亲和性规则不满足时，Pod 会置于 Pending 状态，软亲和性规则不满足时，会选择一个不匹配的节点，当节点标签改变而不再符合此节点亲和性规则时，不会将 Pod 从该节点移出，仅对新建的 Pod 对象生效



### 2、硬亲和性

requiredDuringSchedulingIgnoredDuringExecution

方式一：Pod 使用 spec.nodeSelector (基于等值关系) 

方式二：Pod 使用 spec.affinity 支持 matchExpressions 属性 (复杂标签选择机制)

~~~bash
# 调度至 foo 域的节点
kubectllabelnodeskube-node1zone=foo
~~~

~~~yaml
apiVersion: v1
kind: Pod
metadata:
	name: with-required-nodeaffinity
spec:
	affinity:
		nodeAffinity:
			requiredDuringSchedulingIgnoredDuringExecution: # 定义硬亲和性
				nodeSelectorTerms:
					- matchExpressions: # 集合选择器
					-{key:zone,operator: In, values: ["foo"]}
	containers:
		- name: myapp
		image: ikubernetes/myapp:v1
~~~



### 3、软亲和性

preferredDuringSchedulingIgnoredDuringExecution

柔性控制逻辑，当条件不满足时，能接受被编排于其他不符合条件的节点，权重 weight 定义优先级，1-100 值越大优先级越高

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
	name: myapp-deploy-with-node-affinity
spec:
	replicas: 2
	selector:
		matchLabels:
			app: myapp
	template:
		metadata:
			name: myapp-pod
			labels:
				app: myapp
		spec:
            affinity:
            	nodeAffinity:
            		preferredDuringSchedulingIgnoredDuringExecution: # 节点软亲和性
            		- weight:60
            			preference:
            				matchExpressions:
            					- {key:zone,operator:In, values:["foo"]}
            		- weight:30
            			preference:
            				matchExpressions:
            					- {key:ssd,operator:Exists,values:[]}
            containers:
            	- name: myapp
            	image: ikubernetes/myapp:v1
~~~









# 部署常用软件

## 1、MySQL

**环境准备**：

~~~bash
yum install -y nfs-utils rpcbind

# Master
systemctl start nfs-server
systemctl start rpcbind
systemctl enable nfs-server
systemctl enable rpcbind
~~~



### 1、创建 Namespace

把 MySQL 部署在单独的名称空间中

~~~bash
kubectl create namespace dev
~~~



### 2、创建持久卷 PV

存储 MySQL 数据文件

定义一个容量大小为 1 GB 的 PV，挂载到 /home/data/mysql 目录，需手动创建该目录

~~~bash
# 主节点
# 创建目录
mkdir /home/data/mysql
# 授权
echo "/home/data/ *(insecure,rw,sync,no_root_squash)" > /etc/exports
systemctl restart nfs-server

# 子节点
mkdir -p /home/data/mysql
mount masterIP:/home/data/mysql /home/data/mysql
~~~

编写 mysql-pv.yaml 文件内容，要创建的 pv 对象名称：mysql-pv-1g

~~~yaml
# 定义持久卷信息
apiVersion: v1
kind: PersistentVolume
metadata:
  # pv 是没有 namespace 属性的，它是一种跨 namespace 的共享资源
  name: mysql-pv-1g
spec:
  capacity:
    storage: 1Gi
  accessModes:
    - ReadWriteMany
  # 存储类，具有相同存储类名称的 pv 和 pvc 才能进行绑定
  storageClassName: nfs
  nfs:
    path: /home/data/mysql
    server: MasterIP
    
~~~

~~~bash
kubectl create -f mysql-pv-1g.yaml
kubectl get pv
~~~



### 3、创建持久卷声明 PVC

声明存储大小为 1 Gb 的 PVC 资源，k8s 会根据 storageClassName 存储类名称找到匹配的 PV 对象进行绑定

编写 mysql-pvc.yaml 文件内容，要创建的 pvc 对象名称是：mysql-pvc

~~~bash
# 定义mysql的持久卷声明信息
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  namespace: dev
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
  # 存储类，具有相同存储类名称的 pv 和 pvc才 能进行绑定
  storageClassName: nfs
~~~

~~~bash
kubectl create -f mysql-pvc.yaml
kubectl get pvc -n dev
~~~



### 4、创建 Secret 对象

用来保存 MySQL 的 root 用户密码

设置密码为 fuckharkadmin，执行创建命令

```bash
kubectl create secret generic mysql-root-password --from-literal=password=fuckharkadmin -n dev
```

~~~bash
kubectl get secret -n dev
~~~



### 5、创建 Deployment 和 Service

编辑 mysql-svc.yaml 文件内容，service 使用 NodePort 类型，指定暴露的 nodePort 端口为 31306，在宿主机使用任意数据库客户端对 MySQL 进行访问

~~~yaml
# 定义 MySQL 的 Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: mysql
  name: mysql
  namespace: dev
spec:
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: mysql:latest
        name: mysql
        env:
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-root-password
              key: password
          # 如果不想使用 secret 对象保存 MySQL 登录密码，可以直接使用下面的方式指定   
          # value: "123456"
        ports:
        - containerPort: 3306
        volumeMounts:
        - name: mysqlvolume
          mountPath: /var/lib/mysql
      volumes:
      - name: mysqlvolume
        # 使用 pvc
        persistentVolumeClaim:
          claimName: mysql-pvc
---
# 定义 MySQL 的 Service
apiVersion: v1
kind: Service
metadata:
  labels:
    app: svc-mysql
  name: svc-mysql
  namespace: dev
spec:
  selector:
    app: mysql
  type: NodePort
  ports:
  - port: 3306
    protocol: TCP
    targetPort: 3306
    nodePort: 31306
~~~

~~~bash
kubectl create -f mysql-svc.yaml
~~~



移除

~~~bash
kubectl delete deployment mysql -n dev
kubectl delete service svc-mysql -n dev
~~~



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





## 3、常用命令

~~~bash
# 查看 Pod 都运行在哪些节点上
kubectl get pod -A -o yaml |grep '^    n'|grep -v nodeSelector|awk 'NR%3==1{print ++n"\n"$0;next}1'

1
    name: nginx-ingress-controller-688987f6c9-tndbc
    namespace: ingress-nginx
    nodeName: node2
2
    name: jenkins-0
    namespace: jenkins
    nodeName: node1
~~~



## 4、从 Harbor 拉取镜像

首先在一台安装有 Docker 的节点上访问一次 Harbor

~~~bash
# 在改节点上解析文件保存结果
cat /root/.docker/config.json | base64 -w 0

ewoJImF1dGhzIjogewoJCSIxOTIuMTY4LjEwMC4xNDI6ODAiOiB7CgkJCSJhdXRoIjogIllXUnRhVzQ2TVRJek5EVTIiCgkJfQoJfQp9
~~~

失败了。。。



## 5、从 DockerHub 拉取镜像

~~~bash
docker tag 镜像名:版本 用户名/镜像名:版本
docker push 用户名/镜像名:版本
~~~

~~~yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: gos
  name: gos
spec:
  selector:
    matchLabels:
      app: gos
  template:
    metadata:
      labels:
        app: gos
    spec:
      imagePullSecrets:
        - name: harbor-secret
      containers:
      # 刚才推送的镜像 Tag
        - image: proglagla/gos:v1.0
          name: gos
          ports:
            - containerPort: 3306
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: svc-gos
  name: svc-gos
spec:
  selector:
    app: gos
  type: NodePort
  ports:
    - port: 8080
      protocol: TCP
      targetPort: 8080
      nodePort: 30800
~~~



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



## 5、crictl 报错 WARN[0000] runtime connect using default endpoints

```bash
crictl config runtime-endpoint unix:///var/run/containerd/containerd.sock
```



## 6、Pod 停留在 Pending

第一个就是 pending 状态，pending 表示调度器没有进行介入

此时可以通过 kubectl describe pod 来查看相应的事件，如果由于资源或者说端口占用，或者是由于 node selector 造成 pod 无法调度的时候，可以在相应的事件里面看到相应的结果，这个结果里面会表示说有多少个不满足的 node，有多少是因为 CPU 不满足，有多少是由于 node 不满足，有多少是由于 tag 打标造成的不满足



## 7、Pod 停留在 waiting

那第二个状态就是 pod 可能会停留在 waiting 的状态，pod 的 states 处在 waiting 的时候，通常表示说这个 pod 的镜像没有正常拉取，

原因可能是由于这个镜像是私有镜像，但是没有配置 Pod secret

那第二种是说可能由于这个镜像地址是不存在的，造成这个镜像拉取不下来

还有一个是说这个镜像可能是一个公网的镜像，造成镜像的拉取失败



## 8、Pod 不断被拉取并且可以看到 crashing

第三种是 pod 不断被拉起，而且可以看到类似像 backoff 

这个通常表示说 pod 已经被调度完成了，但是启动失败，那这个时候通常要关注的应该是这个应用自身的一个状态，并不是说配置是否正确、权限是否正确，此时需要查看的应该是 pod 的具体日志



## 9、Pod 处在 Runing 但是没有正常工作

第四种 pod 处在 running 状态，但是没有正常对外服务

那此时比较常见的一个点就可能是由于一些非常细碎的配置，类似像有一些字段可能拼写错误，造成了 yaml 下发下去了，但是有一段没有正常地生效，从而使得这个 pod 处在 running 的状态没有对外服务

那此时可以通过 apply-validate-f pod.yaml 的方式来进行判断当前 yaml 是否是正常的，如果 yaml 没有问题，那么接下来可能要诊断配置的端口是否是正常的，以及 Liveness 或 Readiness 是否已经配置正确



## 10、Service 无法正常的工作

最后一种就是 service 无法正常工作的时候

那比较常见的 service 出现问题的时候，是自己的使用上面出现了问题，因为 service 和底层的 pod 之间的关联关系是通过 selector 的方式来匹配的，也就是说 pod 上面配置了一些 label，然后 service 通过 match label 的方式和这个 pod 进行相互关联

如果这个 label 配置的有问题，可能会造成这个 service 无法找到后面的 endpoint，从而造成相应的 service 没有办法对外提供服务，那如果 service 出现异常的时候，第一个要看的是这个 service 后面是不是有一个真正的 endpoint，其次来看这个 endpoint 是否可以对外提供正常的服务
