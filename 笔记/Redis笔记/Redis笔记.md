# NoSQL数据库

## 概述

NoSQL(NoSQL = Not Only SQL )，意即“不仅仅是SQL”，泛指非关系型的数据库。 
NoSQL 不依赖业务逻辑方式存储，而以简单的key-value模式存储。因此大大的增加了数据库的扩展能力。

- 不遵循SQL标准。
- 不支持ACID。
- 远超于SQL的性能。

## 适用场景

- 对数据高并发的读写
- 海量数据的读写
- 对数据高可扩展性的

## 不适用场景

- 需要事务支持
- 基于sql的结构化查询存储，处理复杂的关系，需要即席查询。
- 用不着sql的和用了sql也不行的情况，请考虑用NoSql

# 数据库存储方式

## 行式存储

![image-20211129103516775](images/image-20211129103516775.png)

## 列式存储

![image-20211129103521368](images/image-20211129103521368.png)

## 图关系型数据库

![image-20211129103551695](images/image-20211129103551695.png)

# Redis概述

- Redis是一个开源的**key-value**存储系统。
- 和Memcached类似，它支持存储的value类型相对更多，包括**string**(字符串)、**list**(链表)、**set**(集合)、**zset**(sorted set --有序集合)和**hash**（哈希类型）。
- 这些数据类型都支持push/pop、add/remove及取交集并集和差集及更丰富的操作，而且这些操作都是原子性的。且支持各种不同方式的排序。
- 与memcached一样，为了保证效率，数据都是缓存在内存中。区别的是Redis会周期性的把更新的数据写入磁盘或者把修改操作写入追加的记录文件。并且在此基础上实现了master-slave(主从)同步。

# Redis安装

## 步骤

1. 下载安装最新版的gcc编译器

   ```shell
   yum install centos-release-scl scl-utils-build
   yum install -y devtoolset-8-toolchain
   scl enable devtoolset-8 bash
   测试 gcc版本 
   gcc --version
   ```

2. 下载redis-6.2.1.tar.gz放/opt目录

3. 解压命令：tar -zxvf redis-6.2.1.tar.gz

4. 解压完成后进入目录：cd redis-6.2.1

5. 在redis-6.2.1目录下再次执行make命令（只是编译好）

   1. 如果没有准备好C语言编译环境，make 会报错—Jemalloc/jemalloc.h：没有那个文件
   2. 解决方案：运行make distclean
   3. 在redis-6.2.1目录下再次执行make命令（只是编译好）

6. 跳过make test 继续执行: make install

7. 安装目录：/usr/local/bin

## 目录

查看默认安装目录：

- redis-benchmark：性能测试工具，可以在自己本子运行，看看自己本子性能如何
- redis-check-aof：修复有问题的AOF文件，rdb和aof后面讲
- redis-check-dump：修复有问题的dump.rdb文件
- redis-sentinel：Redis集群使用
- redis-server：Redis服务器启动命令
- redis-cli：客户端，操作入口

## 启动

后台启动：

1. 备份redis.conf

   1. 拷贝一份redis.conf到其他目录

      ```shell
      cp /opt/redis-3.2.5/redis.conf /myredis
      ```

2. 后台启动设置daemonize no改成yes

   1. 修改redis.conf(128行)文件将里面的daemonize no 改成 yes，让服务在后台启动

3. Redis启动

   1. ```shell
      redis-server/myredis/redis.conf
      ```

4. 用客户端访问：redis-cli

   1. 多个端口可以：redis-cli -p6379

5. Redis关闭

   1. 单实例关闭：redis-cli shutdown
   2. 也可以进入终端后再关闭，再控制台输入shutdown
   3. 多实例关闭，指定端口关闭：redis-cli -p 6379 shutdown

## 配置文件

### Units单位

配置大小单位,开头定义了一些基本的度量单位，只支持bytes，不支持bit
大小写不敏感

### INCLUDES包含

类似jsp中的include，多实例的情况可以把公用的配置文件提取出来

### 网络相关配置

#### bind

默认情况bind=127.0.0.1只能接受本机的访问请求
不写的情况下，无限制接受任何ip地址的访问
生产环境肯定要写你应用服务器的地址，服务器是需要远程访问的，所以需要将其注释掉。

如果开启了**protected-mode**，那么在没有设定bind ip且没有设密码的情况下，Redis只允许接受本机的响应

#### protected-mode

将本机访问保护模式设置no

#### Port

端口号，默认 6379

#### tcp-backlog

设置tcp的backlog，backlog其实是一个连接队列，backlog队列总和=未完成三次握手队列 + 已经完成三次握手队列。
在高并发环境下你需要一个高backlog值来避免慢客户端连接问题。
注意Linux内核会将这个值减小到**/proc/sys/net/core/somaxconn**的值（128），所以需要确认增大**/proc/sys/net/core/somaxconn和/proc/sys/net/ipv4/tcp_max_syn_backlog**（128）两个值来达到想要的效果

#### timeout

一个空闲的客户端维持多少秒会关闭，0表示关闭该功能。即永不关闭。

#### tcp-keepalive

对访问客户端的一种心跳检测，每个n秒检测一次。
单位为秒，如果设置为0，则不会进行Keepalive检测，建议设置成60。

### GENERAL通用

#### daemonize

是否为后台进程，设置为yes
守护进程，后台启动

#### pidfile

存放pid文件的位置，每个实例会产生一个不同的pid文件

#### loglevel

指定日志记录级别，Redis总共支持四个级别：debug、verbose、notice、warning，默认为notice
四个级别根据使用阶段来选择，生产环境选择notice 或者warning

#### logfile

日志文件名称

#### databases 16

设定库的数量 默认16，默认数据库为0，可以使用SELECT <dbid\>命令在连接上指定数据库id

### SECURITY安全

#### 设置密码

requirepass	xxxxxx

访问密码的查看、设置和取消

```shell
config get requirepass	
config set requirepass	"xxxxx"
config get requirepass	""
```

在命令中设置密码，只是临时的，重启redis服务器，密码就还原了。
永久设置，需要再配置文件中进行设置。

### LIMITS限制

#### maxclients

- 设置redis同时可以与多少个客户端进行连接。
- 默认情况下为10000个客户端。
- 如果达到了此限制，redis则会拒绝新的连接请求，并且向这些连接请求方发出“max number of clients reached”以作回应。

#### maxmemory

- 建议必须设置，否则，将内存占满，造成服务器宕机。
- 设置redis可以使用的内存量。一旦到达内存使用上限，redis将会试图移除内部数据，移除规则可以通过maxmemory-policy来指定。
- 如果redis无法根据移除规则来移除内存中的数据，或者设置了“不允许移除”，那么redis则会针对那些需要申请内存的指令返回错误信息，比如SET、LPUSH等。
- 但是对于无内存申请的指令，仍然会正常响应，比如GET等。如果你的redis是主redis（说明你的redis有从redis），那么在设置内存使用上限时，需要在系统中留出一些内存空间给同步队列缓存，只有在你设置的是“不移除”的情况下，才不用考虑这个因素。

#### maxmemory-policy

- **volatile-lru**：使用LRU算法移除key，只对设置了过期时间的键（最近最少使用）
- **allkeys-lru**：在所有集合key中，使用LRU算法移除key
- **volatile-random**：在过期集合中移除随机的key，只对设置了过期时间的键
- **allkeys-random**：在所有集合key中，移除随机的key
- **volatile-ttl**：移除那些TTL值最小的key，即那些最近要过期的key
- **noeviction**：不进行移除。针对写操作，只是返回错误信息

#### maxmemory-samples

- **设置样本数量**，LRU算法和最小TTL算法都并非是精确的算法，而是估算值，所以你可以设置样本的大小，redis默认会检查这么多个key并选择其中LRU的那个。
- 一般设置3到7的数字，数值越小样本越不准确，但性能消耗越小。



# Redis数据类型

## String字符串

### 简介

String是Redis最基本的类型，你可以理解成与Memcached一模一样的类型，**一个key对应一个value**。

String类型是**二进制安全的**，意味着Redis的string可以包含任何数据，比如jpg图片或者序列化的对象。

一个Redis中字符串value最多可以是**512M**。

### 常用命令

- set  <key\><value\> 添加键值对
  - 附加参数：
    - NX：当数据库中key不存在时，可以将key-value添加数据库
    - XX：当数据库中key存在时，可以将key-value添加数据库，与NX参数互斥
    - EX：key的超时秒数
    - PX：key的超时毫秒数，与EX互斥
- get  <key\> 查询对应键值
- append <key\><value\> 将给定的<value\>追加到原值的末尾
- strlen <key\> 获得值的长度
- setnx <key\><value\> 只有在 key 不存在时，设置 key 的值
- incr <key\> 将 key 中储存的数字值增1，只能对数字值操作，如果为空，新增值为1
- decr <key\> 将 key 中储存的数字值减1，只能对数字值操作，如果为空，新增值为-1
- incrby / decrby <key\><步长> 将 key 中储存的数字值增减，自定义步长。
- mset <key1\><value1\><key2\><value2\> .....  同时设置一个或多个 key-value对 
- mget <key1\><key2\><key3\> ..... 同时获取一个或多个 value 
- msetnx <key1\><value1\><key2\><value2\> .....  同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
- getrange <key\><起始位置><结束位置> 获得值的范围，类似java中的substring，**前包，后包**
- setrange <key\><起始位置><value\> 用 <value\> 覆写<key\>所储存的字符串值，从<起始位置>开始(**索引从0开始**)。
- setex <key\><过期时间><value\> 设置键值的同时，设置过期时间，单位秒。
- getset <key\><value\> 以新换旧，设置了新值同时获得旧值。

### 数据结构

String的数据结构为**简单动态字符串**(Simple Dynamic String,缩写SDS)。是可以修改的字符串，内部结构实现上类似于Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配。

![image-20211129110730073](images/image-20211129110730073.png)

内部为当前字符串实际分配的空间capacity，一般要高于实际字符串长度len。当字符串长度小于1M时，扩容都是加倍现有的空间，如果超过1M，扩容时一次只会多扩1M的空间。需要注意的是字符串最大长度为512M。

## List列表

### 简介

**单键多值**
Redis 列表是**简单的字符串列表**，按照插入顺序排序，可以添加一个元素到列表的头部（左边）或者尾部（右边）。
它的底层实际是个**双向链表**，对两端的操作性能很高，通过索引下标的操作中间的节点性能会较差。

### 常用命令

- lpush/rpush  <key\><value1\><value2\><value3\> ....  从左边/右边插入一个或多个值。
- lpop/rpop  <key\>从左边/右边吐出一个值。**值在键在，值光键亡**。
- rpoplpush  <key1\><key2\> 从<key1\>列表右边吐出一个值，插到<key2\>列表左边。
- lrange <key\><start\><stop\> 按照索引下标获得元素(从左到右)
- lrange <key\> 0 -1   0左边第一个，-1右边第一个，（0-1表示获取所有）
- lindex <key\><index\> 按照索引下标获得元素(从左到右)
- llen <key\> 获得列表长度 
- linsert <key\>  before <value\><newvalue\> 在<value\>的后面插入<newvalue\>插入值
- lrem <key\><n\><value\> 从左边删除n个value(从左到右)
- lset<key\><index\><value\> 将列表key下标为index的值替换成value

### 数据结构

List的数据结构为**快速链表quickList**。

首先在列表元素较少的情况下会使用一块连续的内存存储，这个结构是ziplist，也即是压缩列表，它将所有的元素紧挨着一起存储，分配的是一块连续的内存。

当数据量比较多的时候才会改成quicklist。因为普通的链表需要的附加指针空间太大，会比较浪费空间。

比如这个列表里存的只是int类型的数据，结构上还需要两个额外的指针prev和next。

​                               ![image-20211129111257856](images/image-20211129111257856.png)

Redis将链表和ziplist结合起来组成了quicklist。也就是将多个ziplist使用双向指针串起来使用。这样既满足了快速的插入删除性能，又不会出现太大的空间冗余。

## Set集合

### 简介

Redis Set对外提供的功能与list类似是一个列表的功能，特殊之处在于set是可以**自动排重**的，当你需要存储一个列表数据，又不希望出现重复数据时，set是一个很好的选择，并且set提供了判断某个成员是否在一个set集合内的重要接口，这个也是list所不能提供的。
Redis的Set是**string类型的无序集合**。它底层其实是一个**value为null**的**hash表**，所以添加，删除，查找的复杂度都是O(1)。

### 常用命令

sadd <key\><value1\><value2\> .....  将一个或多个 member 元素加入到集合 key 中，已经存在的 member 元素将被忽略
smembers <key\> 取出该集合的所有值。
sismember <key\><value\> 判断集合<key\>是否为含有该<value\>值，有1，没有0
scard<key\> 返回该集合的元素个数。
srem <key\><value1\><value2\> .... 删除集合中的某个元素。
spop <key\> 随机从该集合中吐出一个值。
srandmember <key\><n\> 随机从该集合中取出n个值。不会从集合中删除 。
smove <source\><destination\> value 把集合中一个值从一个集合移动到另一个集合
sinter <key1\><key2\> 返回两个集合的交集元素。
sunion <key1\><key2\> 返回两个集合的并集元素。
sdiff <key1\><key2\> 返回两个集合的差集元素(key1中的，不包含key2中的)

### 数据结构

Set数据结构是**dict字典**，字典是用**哈希表**实现的。
Java中HashSet的内部实现使用的是HashMap，只不过所有的value都指向同一个对象。Redis的set结构也是一样，它的内部也使用hash结构，所有的value都指向同一个内部值。

## Hash哈希

### 简介

Redis Hash 是一个**键值对集合**。
Redis Hash是一个string类型的**field和value的映射表**，hash特别适合用于存储对象。
类似Java里面的Map<String,Object>
用户ID为查找的key，存储的value用户对象包含姓名，年龄，生日等信息。

![image-20211205164400639](images/image-20211205164400639.png)

通过 **key**(用户ID) + **field**(属性标签) 就可以操作对应属性数据了，既不需要重复存储数据，也不会带来序列化和并发修改控制的问题

### 常用命令

hset <key\><field\><value\> 给<key\>集合中的 <field\>键赋值<value\>
hget <key1\><field\>从<key1\> 集合<field\>取出 value 
hmset <key1\><field1\><value1\><field2\><value2\>...  批量设置hash的值
hexists<key1\><field\> 查看哈希表 key 中，给定域 field 是否存在。 
hkeys <key\> 列出该hash集合的所有field
hvals <key\> 列出该hash集合的所有value
hincrby <key\><field\><increment\> 为哈希表 key 中的域 field 的值加上增量 +1   -1
hsetnx <key\><field\><value\> 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在 .

### 数据结构

Hash类型对应的数据结构是两种：**ziplist**（压缩列表），**hashtable**（哈希表）。当field-value长度较短且个数较少时，使用ziplist，否则使用hashtable。

## Zset有序集合

### 简介

Redis有序集合Zset与普通集合Set非常相似，是一个**没有重复元素**的字符串集合。
不同之处是有序集合的每个成员都关联了一个**评分**（score），这个评分（score）被用来按照从最低分到最高分的方式排序集合中的成员。集合的**成员是唯一**的，但是**评分可以重复**。
因为元素是有序的, 所以你也可以很快的根据评分（score）或者次序（position）来获取一个范围的元素。
访问有序集合的中间元素也是非常快的，因此你能够使用有序集合作为一个没有重复成员的智能列表。

### 常用命令

zadd  <key\><score1\><value1\><score2\><value2\>… 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
zrange <key\><start\><stop\>  [WITHSCORES]   返回有序集 key 中，下标在<start\><stop\>之间的元素，带WITHSCORES，可以让分数一起和值返回到结果集。
zrangebyscore <key\><min\><max\> [withscores] [limit offset count] 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。 
zrevrangebyscore <key\><min\><max\> [withscores] [limit offset count] 同上，改为从大到小排列。 
zincrby <key\><increment\><value\> 为元素的score加上增量
zrem  <key\><value\> 删除该集合下，指定值的元素 
zcount <key\><min\><max\> 统计该集合，分数区间内的元素个数 
zrank <key\><value\> 返回该值在集合中的排名，从0开始

### 数据结构

SortedSet(Zset)，一方面它等价于Java的数据结构**Map<String, Double>**，可以给每一个元素value赋予一个权重score，另一方面它又类似于**TreeSet**，内部的元素会按照权重score进行排序，可以得到每个元素的名次，还可以通过score的范围来获取元素的列表。
Zset底层使用了两个数据结构
（1）**hash**，hash的作用就是关联元素value和权重score，保障元素value的唯一性，可以通过元素value找到相应的score值。
（2）**跳跃表**，跳跃表的目的在于给元素value排序，根据score的范围获取元素列表。

## Bitmaps

### 简介

现代计算机用**二进制（位）** 作为信息的基础单位， **1个字节等于8位**， 例如“abc”字符串是由3个字节组成。

 实际在计算机存储时将其用二进制表示， “abc”分别对应的ASCII码分别是97、 98、 99， 对应的二进制分别是01100001、 01100010和01100011，如下图：

![image-20211206194316148](images/image-20211206194316148.png)

因此合理地使用操作位能够有效地提高内存使用率和开发效率。

Redis提供了Bitmaps这个“数据类型”可以实现**对位的操作**：

（1）  Bitmaps本身不是一种数据类型， **实际上它就是字符串**（key-value） ， 但是它可以对字符串的位进行操作。

（2）  Bitmaps单独提供了一套命令， 所以在Redis中使用Bitmaps和使用字符串的方法不太相同。 可以把Bitmaps想象成一个以位为单位的数组， 数组的每个单元只能存储0和1， 数组的下标在Bitmaps中叫做偏移量。

![image-20211206194433708](images/image-20211206194433708.png)



### 常用命令

setbit<key\><offset\><value\> 设置Bitmaps中某个偏移量的值（0或1），*offset：偏移量从0开始

例子：

每个独立用户是否访问过网站存放在Bitmaps中， 将访问的用户记做1， 没有访问的用户记做0， 用偏移量作为用户的id。

设置键的第offset个位的值（从0算起） ， 假设现在有20个用户，userid=1， 6， 11， 15， 19的用户对网站进行了访问， 那么当前Bitmaps初始化结果如图：

![image-20211206201052206](images/image-20211206201052206.png)

**注意**：很多应用的用户id以一个指定数字（例如10000） 开头， 直接将用户id和Bitmaps的偏移量对应势必会造成一定的浪费， 通常的做法是每次做setbit操作时将用户id减去这个指定数字。

在第一次初始化Bitmaps时， 假如偏移量非常大， 那么整个初始化过程执行会比较慢， 可能会造成Redis的阻塞。

getbit<key\><offset\> 获取Bitmaps中某个偏移量的值，获取键的第offset位的值（从0开始算）

**注意**：不存在，也是返回0

bitcount<key\>[start end]  统计字符串从start字节到end字节比特值为1的数量

**注意**：统计**字符串**被设置为1的bit数。一般情况下，给定的整个字符串都会被进行计数，通过指定额外的 start 或 end 参数，可以让计数只在特定的位上进行。start 和 end 参数的设置，**都可以使用负数值**：比如 -1 表示最后一个位，而 -2 表示倒数第二个位，start、end 是指bit组的字节的下标数，**二者皆包含**。

**注意**：redis的setbit设置或清除的是bit位置，而bitcount计算的是byte位置。

bitop and(or/not/xor) <destkey\> [key…]  bitop是一个复合操作， 它可以做多个Bitmaps的and（交集） 、 or（并集） 、 not（非） 、 xor（异或） 操作并将结果保存在destkey中。

![image-20211206201608266](images/image-20211206201608266.png)

### Bitmaps与Set对比

| set和Bitmaps存储一天活跃用户对比 |                    |                  |                        |
| -------------------------------- | ------------------ | ---------------- | ---------------------- |
| 数据  类型                       | 每个用户id占用空间 | 需要存储的用户量 | 全部内存量             |
| 集合  类型                       | 64位               | 50000000         | 64位*50000000 = 400MB  |
| Bitmaps                          | 1位                | 100000000        | 1位*100000000 = 12.5MB |

很明显， 这种情况下使用Bitmaps能节省很多的内存空间， 尤其是随着时间推移节省的内存还是非常可观的

| set和Bitmaps存储独立用户空间对比 |        |        |       |
| -------------------------------- | ------ | ------ | ----- |
| 数据类型                         | 一天   | 一个月 | 一年  |
| 集合类型                         | 400MB  | 12GB   | 144GB |
| Bitmaps                          | 12.5MB | 375MB  | 4.5GB |

但Bitmaps并不是万金油， 假如该网站每天的独立访问用户很少， 例如只有10万（大量的僵尸用户） ， 那么两者的对比如下表所示， 很显然， 这时候使用Bitmaps就不太合适了， 因为基本上大部分位都是0。

| set和Bitmaps存储一天活跃用户对比（独立用户比较少） |                    |                  |                        |
| -------------------------------------------------- | ------------------ | ---------------- | ---------------------- |
| 数据类型                                           | 每个userid占用空间 | 需要存储的用户量 | 全部内存量             |
| 集合类型                                           | 64位               | 100000           | 64位*100000 = 800KB    |
| Bitmaps                                            | 1位                | 100000000        | 1位*100000000 = 12.5MB |

## HyperLogLog

### 简介

求集合中不重复元素个数的问题称为基数问题。

解决基数问题有很多种方案：
（1）数据存储在MySQL表中，使用distinct count计算不重复个数
（2）使用Redis提供的hash、set、bitmaps等数据结构来处理
以上的方案结果精确，但随着数据不断增加，导致占用空间越来越大，对于非常大的数据集是不切实际的。

Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定的、并且是很小的。

在 Redis 里面，每个 HyperLogLog 键只需要花费 12 KB 内存，就可以计算接近 2^64 个不同元素的基数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比。

但是，因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。

### 常用命令

pfadd <key\>< element> [element ...]   添加指定元素到 HyperLogLog 中

将所有元素添加到指定HyperLogLog数据结构中。如果执行命令后HLL估计的近似基数发生变化，则返回1，否则返回0。

![image-20211206205106940](images/image-20211206205106940.png)

pfcount<key\> [key ...] 计算HLL的近似基数，可以计算多个HLL，比如用HLL存储每天的UV，计算一周的UV可以使用7天的UV合并计算即可。

![image-20211206205152332](images/image-20211206205152332.png)

pfmerge<destkey\><sourcekey\> [sourcekey ...] 将一个或多个HLL合并后的结果存储在另一个HLL中，比如每月活跃用户可以使用每天的活跃用户来合并计算可得。

![image-20211206211542120](images/image-20211206211542120.png)

## Geospatial

### 简介

Redis 3.2 中增加了对GEO类型的支持。

GEO，Geographic，地理信息的缩写。

该类型，就是元素的2维坐标，在地图上就是经纬度。

Redis基于该类型，提供了经纬度设置，查询，范围查询，距离查询，经纬度Hash等常见操作。

### 常用命令

geoadd<key\>< longitude><latitude\><member\> [longitude latitude member...]  添加地理位置（经度，纬度，名称）

![image-20211206211731963](images/image-20211206211731963.png)

**注意**：

两极无法直接添加，一般会下载城市数据，直接通过 Java 程序一次性导入。

有效的经度从 -180 度到 180 度。有效的纬度从 -85.05112878 度到 85.05112878 度。

当坐标位置超出指定范围时，该命令将会返回一个错误。

已经添加的数据，是无法再次往里面添加的。

geopos <key\><member\> [member...]  获得指定地区的坐标值

![image-20211206211820970](images/image-20211206211820970.png)

geodist<key\><member1\><member2\> [m|km|ft|mi ]  获取两个位置之间的直线距离

![image-20211206211843397](images/image-20211206211843397.png)

**注意**：

单位：

m 表示单位为米[默认值]。

km 表示单位为千米。

mi 表示单位为英里。

ft 表示单位为英尺。

如果用户没有显式地指定单位参数， 那么 GEODIST 默认使用米作为单位

georadius<key\>< longitude><latitude\>radius  m|km|ft|mi    以给定的经纬度为中心，找出某一半径内的元素

经度 纬度 距离 单位

![image-20211206211938762](images/image-20211206211938762.png)









# Redis发布与订阅

## 简介

Redis 发布订阅 (pub/sub) 是一种消息通信模式：发送者 (pub) 发送消息，订阅者 (sub) 接收消息。

Redis 客户端可以订阅任意数量的频道。

客户端可以订阅频道如下图：

![image-20211206193537187](images/image-20211206193537187.png)

当给这个频道发布消息后，消息就会发送给订阅的客户端：

![image-20211206193603009](images/image-20211206193603009.png)

## 命令行实现

1、 打开一个客户端订阅channel1

**SUBSCRIBE channel1**

![image-20211206193844485](images/image-20211206193844485.png)

2、打开另一个客户端，给channel1发布消息hello

**publish channel1 hello**

![image-20211206193849597](images/image-20211206193849597.png)

返回的1是订阅者数量

3、打开第一个客户端可以看到发送的消息

![image-20211206193854266](images/image-20211206193854266.png)

注：**发布的消息没有持久化**，正在订阅的客户端收不到hello，只能收到订阅后发布的消息。

# 相关知识

- 默认**16**个数据库，类似数组下标从0开始，初始**默认使用0号库 ** 
- 使用命令 select  <dbid\> 来切换数据库。如: select 8  
- 统一密码管理，所有库同样密码。

## 常用操作

- **dbsize** 查看当前数据库的key的数量
- **flushdb** 清空当前库
- **flushall** 通杀全部库
- **keys *** 查看当前库所有key  (匹配：keys *1)
- **exists key** 判断某个key是否存在
- **type key** 查看你的key是什么类型
- **del key**  删除指定的key数据
- **unlink key**  根据value选择非阻塞删除，仅将keys从keyspace元数据中删除，真正的删除会在后续异步操作。
- **expire key 10**  10秒钟：为给定的key设置过期时间
- **ttl key** 查看还有多少秒过期，-1表示永不过期，-2表示已过期

## 原子性

![image-20211129110217658](images/image-20211129110217658.png)

所谓原子操作是指不会被线程调度机制打断的操作。
这种操作一旦开始，就一直运行到结束，中间不会有任何 context switch （切换到另一个线程）。
（1）在单线程中， 能够在单条指令中完成的操作都可以认为是"原子操作"，因为中断只能发生于指令之间。
（2）在多线程中，不能被其它进程（线程）打断的操作就叫原子操作。
Redis单命令的原子性主要得益于Redis的单线程。

**原子性，有一个失败则都失败**



































