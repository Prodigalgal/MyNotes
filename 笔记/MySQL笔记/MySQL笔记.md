# 1、MySQL概述

## 1.1、为什么要使用数据库

**持久化**(persistence)：把数据保存到可掉电式存储设备中以供之后使用。大多数情况下，特别是企业级应用，数据持久化意味着将内存中的数据保存到硬盘上加以”固化”，而持久化的实现过程大多通过各种关系数据库来完成。 

持久化的主要作用是将内存中的数据存储在关系型数据库中，当然也可以存储在磁盘文件、XML数据文件中。

## 1.2、基本概念

**DB**：数据库（Database） 即存储数据的“仓库”，其本质是一个文件系统。它保存了一系列有组织的数据。 

**DBMS**：数据库管理系统（Database Management System） 是一种操纵和管理数据库的大型软件，用于建立、使用和维护数据库，对数据库进行统一管理和控 制。用户通过数据库管理系统访问数据库中表内的数据。 

**SQL**：结构化查询语言（Structured Query Language） 专门用来与数据库通信的语言。

```text
“information_schema”是 MySQL 系统自带的数据库，主要保存 MySQL 数据库服务器的系统信息，
比如数据库的名称、数据表的名称、字段名称、存取权限、数据文件 所在的文件夹和系统使用的
文件夹，等等

“performance_schema”是 MySQL 系统自带的数据库，可以用来监控 MySQL 的各类性能指标。

“sys”数据库是 MySQL 系统自带的数据库，主要作用是以一种更容易被理解的方式展示 MySQL 数据
库服务器的各类性能指标，帮助系统管理员和开发人员监控 MySQL 的技术性能。

“mysql”数据库保存了 MySQL 数据库服务器运行时需要的系统信息，比如数据文件夹、当前使用的
字符集、约束检查信息，等等
```

## 1.3、SQL语言

### 1.3.1、SQL分类

SQL语言在功能上主要分为如下3大类：

- **DDL（Data Definition Languages、数据定义语言）**：这些语句定义了不同的数据库、表、视图、索引等数据库对象，还可以用来创建、删除、修改数据库和数据表的结构。 主要的语句关键字包括 CREATE 、 DROP 、 ALTER 等。 

- **DML（Data Manipulation Language、数据操作语言）**：用于添加、删除、更新和查询数据库记录，并检查数据完整性。 主要的语句关键字包括 INSERT 、 DELETE 、 UPDATE 、 SELECT 等。 SELECT是SQL语言的基础，最为重要。 

- **DCL（Data Control Language、数据控制语言）**：用于定义数据库、表、字段、用户的访问权限和安全级别。 主要的语句关键字包括 GRANT 、 REVOKE 、 COMMIT 、 ROLLBACK 、 SAVEPOINT 等。

因为查询语句使用的非常的频繁，所以很多人把查询语句单拎出来一类：DQL（数据查询语言）。 

还有单独将 COMMIT 、 ROLLBACK 取出来称为TCL （Transaction Control Language，事务控制语言）。

### 1.3.2、规则与规范

- SQL 可以写在一行或者多行。为了提高可读性，各子句分行写，必要时使用缩进
- 每条命令以 **;** 或 **\g** 或 **\G** 结束
- 关键字不能被**缩写**也不能**分行** 
- 关于标点符号必须保证所有的**()**、**单引号**、**双引号**是**成对结束**的
- 必须使用英文状态下的**半角**输入方式 
- 字符串型和日期时间类型的数据可以使用单引号**' '**
- 表示列的别名，尽量使用双引号**" "**，而且不建议省略**as** ，如果别名中间无空格可以省略**" "**
- MySQL 在 Windows 环境下是大小写不敏感的，在 Linux 环境下是大小写敏感的
  - 数据库名、表名、表的别名、变量名是严格区分大小写的 
  - 关键字、函数名、列名(或字段名)、列的别名(字段的别名) 是忽略大小写的。
- **推荐**采用统一的书写规范： 
  - 数据库名、表名、表别名、字段名、字段别名等都小写 
  - SQL 关键字、函数名、绑定变量等都大写

### 1.3.3、注释

```sql
单行注释：#注释文字(MySQL特有的方式)
单行注释：-- 注释文字(--后面必须包含一个空格。)
多行注释：/* 注释文字 */
```

### 1.3.4、命名规则

- 数据库、表名不得超过30个字符，变量名限制为29个 
- 必须只能包含 A–Z, a–z, 0–9, _共63个字符 
- 数据库名、表名、字段名等对象名中间不要包含空格 
- 同一个MySQL软件中，数据库不能同名，同一个库中，表不能重名，同一个表中，字段不能重名 
- 必须保证你的字段没有和保留字、数据库系统或常用方法冲突。如果坚持使用，请在SQL语句中使用 **`**（着重号）引起来
- 保持字段名和类型的一致性，在命名字段并为其指定数据类型的时候一定要保证一致性。假如数据类型在一个表里是整数，那在另一个表里可就别变成字符型了



## 1.4、表的关联关系

**四种关系**：一对一关联、一对多关联、多对多关联、自我引用

### 1.4.1、一对一关联（one-to-one）

**两种建表原则**： 

- 外键唯一：主表的主键和从表的外键（唯一），形成主外键关系，外键唯一。 
- 外键是主键：主表的主键和从表的主键，形成主外键关系。

<img src="images/image-20220312163511764.png" alt="image-20220312163511764" style="zoom:50%;" />

### 1.4.2、一对多关系（one-to-many）

**建表原则**：在从表(多方)创建一个字段，字段作为外键指向主表(一方)的主键

<img src="images/image-20220312163529411.png" alt="image-20220312163529411" style="zoom:50%;" />

### 1.4.3、多对多（many-to-many）

**建表原则**：要表示多对多关系，必须创建第三个表，该表通常称为 **联接表** ，它将多对多关系划分为两个一对多关系。将这两个表的主键都插入到第三个表中。

<img src="images/image-20220312163632964.png" alt="image-20220312163632964" style="zoom:50%;" />

### 1.4.4、自我引用(Self reference)

<img src="images/image-20220312163655231.png" alt="image-20220312163655231" style="zoom:50%;" />

## 1.5、卸载

先在任务管理器停止Mysql服务。

之后直接在控制面板卸载Mysql相关软件，或者通过Mysq安装包自带的卸载功能

清理残余文件，服务目录：mysql服务的安装目录，数据目录：默认在C:\ProgramData\MySQL

清理注册表，cmd打开输入regedit

```text
HKEY_LOCAL_MACHINE\SYSTEM\ControlSet001\Services\MySQL服务 目录删除
HKEY_LOCAL_MACHINE\SYSTEM\ControlSet002\Services\Eventlog\Application\MySQL服务 目录删除
HKEY_LOCAL_MACHINE\SYSTEM\ControlSet002\Services\MySQL服务 目录删除
HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Eventlog\Application\MySQL服务 目录删除
HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\MySQL服务 删除
```

删除环境变量配置，找到path环境变量，将其中关于mysql的环境变量删除，删除这一部分\MySQLServer8.0.26\bin; 

## 1.6、安装

下载mysql-installer-community-8.0.26.0.msi文件

双击打开选择安装

<img src="images/image-20220312164426601.png" alt="image-20220312164426601" style="zoom: 80%;" />

下一步选择要安装的产品

<img src="images/image-20220312164502292.png" alt="image-20220312164502292" style="zoom:80%;" />

路径一般是默认的，要选择可以点击产品，然后点击下方的Advanced

<img src="images/image-20220312164550500.png" alt="image-20220312164550500" style="zoom:80%;" />

ProgramData目录（这是一个隐藏目录）。如果自定义安装目录，请避免“**中文**”目录。

另外，建议服务目录和数据目录分开存放。

<img src="images/image-20220312164613842.png" alt="image-20220312164613842" style="zoom:80%;" />

点击下一步，Execute安装

<img src="images/image-20220312164658718.png" alt="image-20220312164658718" style="zoom:80%;" />

<img src="images/image-20220312164731138.png" alt="image-20220312164731138" style="zoom:80%;" />

之后配置Mysql8.0

<img src="images/image-20220312164751118.png" alt="image-20220312164751118" style="zoom:80%;" />

<img src="images/image-20220312164802505.png" alt="image-20220312164802505" style="zoom:80%;" />

Server Machine（服务器） ：该选项代表服务器，MySQL服务器可以同其他服务器应用程序一起 运行，例如Web服务器等。MySQL服务器配置成适当比例的系统资源。 

Dedicated Machine（专用服务器） ：该选项代表只运行MySQL服务的服务器。MySQL服务器配置成使用所有可用系统资源。

在然后选择授权方式

<img src="images/image-20220312164924902.png" alt="image-20220312164924902" style="zoom:80%;" />

之后next，设置root用户的密码，也可以点击add User添加用户，设置该用户的一系列权限等

<img src="images/image-20220312165022637.png" alt="image-20220312165022637" style="zoom:80%;" />

点击next，设置mysqlservic的服务名，还可以选择以什么系统用户运行，一般推荐标准系统账户

<img src="images/image-20220312165103292.png" alt="image-20220312165103292" style="zoom:80%;" />

之后一直点next即可。

cmd登陆Mysql

mysql -h 主机名 -P 端口号 -u 用户名 -p密码

## 1.7、基本操作

```sql
查看所有的数据库
show databases;

创建自己的数据库
create database 数据库名;

使用自己的数据库
use 数据库名;

查看某个库的所有表格
show tables from 数据库名;

使用 DESCRIBE 或 DESC 命令，表示表结构。
DESCRIBE employees;
DESC employees;

创建新的表格
create table 表名称(
    字段名 数据类型,
    字段名 数据类型
);

查看一个表的数据
select * from 数据库表名称;

添加一条记录
insert into 表名称 values(值列表);

查看表的创建信息
show create table 表名称\G

查看数据库的创建信息
show create database 数据库名\G

删除表格
drop table 表名称;

删除数据库
drop database 数据库名;

查看MySQL的编码设置
show variables like 'character_%';

在mysql里导入
source d:\mysqldb.sql
```

# 2、MySQL之基础查询

## 2.1、基本SELECT

### 2.1.1、无子语句

```sql
SELECT 1; #没有任何子句
SELECT 9/2; #没有任何子句
```

在生产环境下，不推荐你直接使用 SELECT ***** 进行查询。

### 2.1.2、选择特定表

```sql
SELECT 标识选择哪些列
FROM 标识从哪个表中选择
```

### 2.1.3、取别名

AS可以省略，别名如果有特殊字符必须用**" "**括起来

~~~sql
SELECT last_name AS "Name", salary*12 AS "Annual Salary"
FROM employees;
~~~

### 2.1.4、去除重复行

DISTINCT 是对后面所有列名的组合进行去重

~~~sql
SELECT DISTINCT department_id
FROM employees;
~~~

### 2.1.5、空值运算

所有运算符或列值遇到null值，运算的结果都为null

空值不等于空字符串。一个空字符串的长度是 0，而一个空值的长度是空。而且，在 MySQL 里面，空值是占用空间的。

~~~sql
SELECT employee_id,salary,commission_pct,12 * salary * (1 + commission_pct) "annual_sal"
FROM employees;
~~~

### 2.1.6、着重号

字段名或者表明与保留字冲突，需要用着重号**``**括起来

~~~sql
SELECT * FROM `ORDER`;
~~~

###  2.1.7、常数查询

在查询时增加一列常量字段

~~~sql
SELECT 'wawa' as corporation, last_name FROM employees;
~~~

<img src="images/image-20220313092727042.png" alt="image-20220313092727042" style="zoom:80%;" />



SELECT 字段1,字段2 FROM 表名 WHERE 过滤条件

## 2.2、运算符

![image-20220314100146097](images/image-20220314100146097.png)



![image-20220313093426803](images/image-20220313093426803.png)

### 2.2.1、加减法

~~~sql
SELECT 100, 100 + 0, 100 - 0, 100 + 50, 100 + 50 -30, 100 + 35.5, 100 - 35.5
FROM dual;
+-----+---------+---------+----------+--------------+------------+------------+
| 100 | 100 + 0 | 100 - 0 | 100 + 50 | 100 + 50 -30 | 100 + 35.5 | 100 - 35.5 |
+-----+---------+---------+----------+--------------+------------+------------+
| 100 | 	100 | 	  100 | 	 150 | 		 	120 | 	   135.5 | 		 64.5 |
+-----+---------+---------+----------+--------------+------------+------------+

~~~

- 整数类型对整数类型进行加减操作，结果还是一个整数类型；
- 整数类型对浮点数类型进行加减法操作，结果是一个浮点数类型；
- 加法和减法的优先级相同，进行先加后减操作与进行先减后加操作的结果是一样的；
- 在Java中，+的左右两边如果有字符串，那么表示字符串的拼接。但是在MySQL中+只表示数值相加。如果遇到非数值类型，先尝试转成数值，如果转失败，就按0计算。（补充：MySQL 中字符串拼接要使用字符串函数**CONCAT()**实现）

### 2.2.2、乘除法

~~~sql
SELECT 100, 100 * 1, 100 * 1.0, 100 / 1.0, 100 / 2,100 + 2 * 5 / 2,100 /3, 100
DIV 0 FROM dual;
+-----+---------+-----------+-----------+---------+-----------------+---------+-----------+
| 100 | 100 * 1 | 100 * 1.0 | 100 / 1.0 | 100 / 2 | 100 + 2 * 5 / 2 | 100 /3  | 100 DIV 0 |
+-----+---------+-----------+-----------+---------+-----------------+---------+-----------+
| 100 | 100 	| 100.0 	| 100.0000 	| 50.0000 | 105.0000 		| 33.3333 |NULL 	  |
+-----+---------+-----------+-----------+---------+-----------------+---------+-----------+
~~~

- 一个数乘以整数1和除以整数1后仍得原数；
- 一个数乘以浮点数1和除以浮点数1后变成浮点数，数值与原数相等；
- 一个数除以整数后，不管是否能除尽，结果都为一个浮点数；
- 一个数除以另一个数，除不尽时，结果为一个浮点数，并保留到小数点后4位；
- 乘法和除法的优先级相同，进行先乘后除操作与先除后乘操作，得出的结果相同。 
- 在数学运算中，0不能用作除数，在MySQL中，一个数除以0为NULL。

### 2.2.3、求模运算

~~~sql
SELECT 12 % 3, 12 MOD 5 FROM dual;
+--------+----------+
| 12 % 3 | 12 MOD 5 |
+--------+----------+
| 0 	 | 		  2 |
+--------+----------+
~~~

### 2.2.4、符号比较运算

比较运算符用来对表达式左边的操作数和右边的操作数进行比较，比较的结果为真则返回1，比较的结果为假则返回0，其他情况则返回NULL。 

比较运算符经常被用来作为SELECT查询语句的条件来使用，返回符合条件的结果记录。

![image-20220313105122326](images/image-20220313105122326.png)

#### 2.2.4.1、等号运算

~~~sql
SELECT 1 = 1, 1 = '1', 1 = 0, 'a' = 'a', (5 + 3) = (2 + 6), '' = NULL , NULL = NULL;
+-------+---------+-------+-----------+-------------------+-----------+-------------+
| 1 = 1 | 1 = '1' | 1 = 0 | 'a' = 'a' | (5 + 3) = (2 + 6) | '' = NULL | NULL = NULL |
+-------+---------+-------+-----------+-------------------+-----------+-------------+
| 1 	| 1 	  | 0 	  | 1 		  | 1 				  | NULL 	  | NULL 		|
+-------+---------+-------+-----------+-------------------+-----------+-------------+

SELECT 1 = 2, 0 = 'abc', 1 = 'abc' FROM dual;
+-------+-----------+-----------+
| 1 = 2 | 0 = 'abc' | 1 = 'abc' |
+-------+-----------+-----------+
| 0 	| 1 		| 0 		|
+-------+-----------+-----------+

~~~

等号运算符（=）判断等号两边的值、字符串或表达式是否相等，如果相等则返回1，不相等则返回 0。 

在使用等号运算符时，遵循如下规则： 

- 如果等号两边的值、字符串或表达式都为字符串，则MySQL会按照字符串进行比较，其比较的是每个字符串中字符的ANSI编码是否相等。 
- 如果等号两边的值都是整数，则MySQL会按照整数来比较两个值的大小。
- 如果等号两边的值一个是整数，另一个是字符串，则MySQL会将字符串转化为数字进行比较。 
- 如果等号两边的值、字符串或表达式中有一个为NULL，则比较结果为NULL。

#### 2.2.4.2、安全等于运算

安全等于运算符（<=>）与等于运算符（=）的作用是相似的， 唯一的区别是‘<=>’可 以用来对**NULL**进行判断。在两个操作数均为NULL时，其返回值为1，而不为NULL，当一个操作数为NULL 时，其返回值为0，而不为NULL。

~~~SQL
SELECT 1 <=> '1', 1 <=> 0, 'a' <=> 'a', (5 + 3) <=> (2 + 6), '' <=> NULL, NULL <=> NULL 
FROM dual;
+-----------+---------+-------------+---------------------+-------------+---------------+
| 1 <=> '1' | 1 <=> 0 | 'a' <=> 'a' | (5 + 3) <=> (2 + 6) | '' <=> NULL | NULL <=> NULL |
+-----------+---------+-------------+---------------------+-------------+---------------+
| 1 		| 0 	  | 1 			| 1 				  | 0 			|1 				|
+-----------+---------+-------------+---------------------+-------------+---------------+
~~~

#### 2.2.4.3、不等于运算符

不等于运算符（<>和!=）用于判断两边的数字、字符串或者表达式的值是否不相等， 如果不相等则返回1，相等则返回0。

不等于运算符不能判断NULL值。如果两边的值有任意一个为NULL， 或两边都为NULL，则结果为NULL。 

~~~sql
SELECT 1 <> 1, 1 != 2, 'a' != 'b', (3+4) <> (2+6), 'a' != NULL, NULL <> NULL;
+--------+--------+------------+----------------+-------------+--------------+
| 1 <> 1 | 1 != 2 | 'a' != 'b' | (3+4) <> (2+6) | 'a' != NULL | NULL <> NULL |
+--------+--------+------------+----------------+-------------+--------------+
| 0 	 | 1 	  | 1 		   | 1 			    | NULL 		  | NULL 		 |
+--------+--------+------------+----------------+-------------+--------------+
~~~

### 2.2.5、非符号比较运算

![image-20220313105721208](images/image-20220313105721208.png)

#### 2.2.5.1、空运算符

判断一个值是否为NULL，如果为NULL则返回1，否则返回 0

~~~sql
SELECT NULL IS NULL, ISNULL(NULL), ISNULL('a'), 1 IS NULL;
+--------------+--------------+-------------+-----------+
| NULL IS NULL | ISNULL(NULL) | ISNULL('a') | 1 IS NULL |
+--------------+--------------+-------------+-----------+
| 1 		   | 1 			  | 0 			| 0 		|
+--------------+--------------+-------------+-----------+
~~~



#### 2.2.5.2、空运算符

判断一个值是否不为NULL，如果不为NULL则返回1，否则返回0。

~~~sql
SELECT NULL IS NOT NULL, 'a' IS NOT NULL, 1 IS NOT NULL;
+------------------+-----------------+---------------+
| NULL IS NOT NULL | 'a' IS NOT NULL | 1 IS NOT NULL |
+------------------+-----------------+---------------+
| 0 			   | 1 				 | 1 			 |
+------------------+-----------------+---------------+
~~~



#### 2.2.5.3、最小值运算符

语法格式为：LEAST(值1，值2，...，值n)。其中，“值n”表示参数列表中有n个值。在有两个或多个参数的情况下，返回最小值。

~~~sql
SELECT LEAST (1,0,2), LEAST('b','a','c'), LEAST(1,NULL,2);
+---------------+--------------------+-----------------+
| LEAST (1,0,2) | LEAST('b','a','c') | LEAST(1,NULL,2) |
+---------------+--------------------+-----------------+
| 0 		    | a 				 | NULL 		   |
+---------------+--------------------+-----------------+
~~~

- 当参数是整数或者浮点数时，LEAST将返回其中最小的值。
- 当参数为字符串时，返回字母表中顺序最靠前的字符。
- 当比较值列表中有NULL时，不能判断大小，返回值为NULL。



#### 2.2.5.4、最大值运算符

语法格式为：GREATEST(值1，值2，...，值n)。其中，n表示参数列表中有n个值。当有两个或多个参数时，返回值为最大值。

假如任意一个自变量为NULL，则GREATEST()的返回值为NULL。

~~~sql
SELECT GREATEST(1,0,2), GREATEST('b','a','c'), GREATEST(1,NULL,2);
+-----------------+-----------------------+--------------------+
| GREATEST(1,0,2) | GREATEST('b','a','c') | GREATEST(1,NULL,2) |
+-----------------+-----------------------+--------------------+
| 2 			  | c 					  | NULL 			   |
+-----------------+-----------------------+--------------------+
~~~



#### 2.2.5.5、BETWEEN AND运算符

BETWEEN运算符使用的格式通常为SELECT D FROM TABLE WHERE C BETWEEN A AND B，此时，当C大于或等于A，并且C小于或等于B时，结果为1，否则结果为0。

~~~sql
SELECT 1 BETWEEN 0 AND 1, 10 BETWEEN 11 AND 12, 'b' BETWEEN 'a' AND 'c';
+-------------------+----------------------+-------------------------+
| 1 BETWEEN 0 AND 1 | 10 BETWEEN 11 AND 12 | 'b' BETWEEN 'a' AND 'c' |
+-------------------+----------------------+-------------------------+
| 1 			  	| 0 				   | 1 						 |
+-------------------+----------------------+-------------------------+
~~~



#### 2.2.5.6、IN运算符

 用于判断给定的值是否是IN列表中的一个值，如果是则返回1，否则返回0。

如果给定的值为NULL，或者IN列表中存在NULL，则结果为NULL。

~~~sql
SELECT 'a' IN ('a','b','c'), 1 IN (2,3), NULL IN ('a','b'), 'a' IN ('a', NULL);
+----------------------+------------+-------------------+--------------------+
| 'a' IN ('a','b','c') | 1 IN (2,3) | NULL IN ('a','b') | 'a' IN ('a', NULL) |
+----------------------+------------+-------------------+--------------------+
| 1 				   | 0 			| NULL 				| 1 				 |
+----------------------+------------+-------------------+--------------------+
~~~



#### 2.2.5.7、NOT IN运算符

用于判断给定的值是否不是IN列表中的一个值，如果不是IN列表中的一个值，则返回1，否则返回0。

~~~sql
SELECT 'a' NOT IN ('a','b','c'), 1 NOT IN (2,3);
+--------------------------+----------------+
| 'a' NOT IN ('a','b','c') | 1 NOT IN (2,3) |
+--------------------------+----------------+
| 0 					   | 1 				|
+--------------------------+----------------+
~~~



#### 2.2.5.8、LIKE运算符

主要用来匹配字符串，通常用于模糊匹配，如果满足条件则返回1，否则返回 0。

如果给定的值或者匹配条件为NULL，则返回结果为NULL。

~~~sql
SELECT NULL LIKE 'abc', 'abc' LIKE NULL;
+-----------------+-----------------+
| NULL LIKE 'abc' | 'abc' LIKE NULL |
+-----------------+-----------------+
| NULL 			  | NULL 			|
+-----------------+-----------------+
~~~

通配符：

~~~sql
“%”：匹配0个或多个字符。
“_”：只能匹配一个字符。
~~~

| LIKE运算符                | 描述                            |
| ------------------------- | ------------------------------- |
| WHERE name LIKE "a%"      | 查找以 "a"开头的任何值          |
| WHERE name LIKE "%a"      | 查找以 "a"结尾的任何值          |
| WHERE name LIKE "%a%"     | 在任何位置查找任何具有"a"的值   |
| WHERE name LIKE "_a%"     | 在第二个位置查找任何具有"a"的值 |
| WHERE name LIKE "o%a"     | 查找以 "o"开头以"a"结尾的任何值 |
| WHERE name LIKE "[!abc]%" | 查找不以a或b或c开头的值         |
| WHERE name LIKE "[abc]%"  | 查找以a或b或c开头的值           |



#### 2.2.5.9、REGEXP运算符

符用来匹配字符串，语法格式为： expr REGEXP 匹配条件 。如果expr满足匹配条件，返回 1；如果不满足，则返回0。

若expr或匹配条件任意一个为NULL，则结果为NULL。

REGEXP运算符在进行匹配时，常用的有下面几种通配符： 

（1）‘^’匹配以该字符后面的字符开头的字符串。 

（2）‘$’匹配以该字符前面的字符结尾的字符串。 

（3）‘.’匹配任何一个单字符。 

（4）“[...]”匹配在方括号内的任何字符。例如，“[abc]”匹配“a”或“b”或“c”。为了命名字符的范围，使用一 个‘-’。“[a-z]”匹配任何字母，而“[0-9]”匹配任何数字。 

（5）‘\*' 匹配零个或多个在它前面的字符。例如，“x\*”匹配任何数量的‘x’字符，“[0-9]\*”匹配任何数量的数字， 而“*”匹配任何数量的任何字符。

~~~sql
SELECT 'shkstart' REGEXP '^s', 'shkstart' REGEXP 't$', 'shkstart' REGEXP 'hk';
+------------------------+------------------------+-------------------------+
| 'shkstart' REGEXP '^s' | 'shkstart' REGEXP 't$' | 'shkstart' REGEXP 'hk'  |
+------------------------+------------------------+-------------------------+
| 1 					 | 1 					  | 1 						|
+------------------------+------------------------+-------------------------+
~~~



### 2.2.6、逻辑运算符

![image-20220314094715373](images/image-20220314094715373.png)

#### 2.2.6.1、非运算符

逻辑非（NOT或!）运算符表示当给定的值为0时返回1；

当给定的值为非0值时返回0； 

当给定的值为NULL时，返回NULL。

~~~sql
 SELECT NOT 1, NOT 0, NOT(1+1), NOT !1, NOT NULL;
+-------+-------+----------+--------+----------+
| NOT 1 | NOT 0 | NOT(1+1) | NOT !1 | NOT NULL |
+-------+-------+----------+--------+----------+
| 0 	| 1 	| 0 	   | 1 		| NULL 	   |
+-------+-------+----------+--------+----------+
~~~



#### 2.2.6.2、与运算符

 逻辑与（AND或&&）运算符是当给定的所有值均为非0值，并且都不为NULL时，返回 1；

当给定的一个值或者多个值为0时则返回0；否则返回NULL。

~~~sql
SELECT 1 AND -1, 0 AND 1, 0 AND NULL, 1 AND NULL;
+----------+---------+------------+------------+
| 1 AND -1 | 0 AND 1 | 0 AND NULL | 1 AND NULL |
+----------+---------+------------+------------+
| 1 	   | 0 		 | 0 		  | NULL 	   |
+----------+---------+------------+-------------
~~~



#### 2.2.6.3、或运算符

逻辑或（OR或||）运算符是当给定的值都不为NULL，并且任何一个值为非0值时，则返回1，否则返回0；

当一个值为NULL，并且另一个值为非0值时，返回1，否则返回NULL；

当两个值都为 NULL时，返回NULL。

~~~sql
 SELECT 1 OR -1, 1 OR 0, 1 OR NULL, 0 || NULL, NULL || NULL;
+---------+--------+-----------+-----------+--------------+
| 1 OR -1 | 1 OR 0 | 1 OR NULL | 0 || NULL | NULL || NULL |
+---------+--------+-----------+-----------+--------------+
| 1 	  | 1 	   | 1 		   | NULL 	   | NULL		  |
+---------+--------+-----------+-----------+--------------+
~~~

OR可以和AND一起使用，但是在使用时要注意两者的优先级，由于AND的优先级高于OR，因此先对AND两边的操作数进行操作，再与OR中的操作数结合。

#### 2.2.6.4、异或运算符

逻辑异或（XOR）运算符是当给定的值中任意一个值为NULL时，则返回NULL；

如果两个非NULL的值都是0或者都不等于0时，则返回0；

如果一个值为0，另一个值不为0时，则返回1。

~~~sql
SELECT 1 XOR -1, 1 XOR 0, 0 XOR 0, 1 XOR NULL, 1 XOR 1 XOR 1, 0 XOR 0 XOR 0;
+----------+---------+---------+------------+---------------+---------------+
| 1 XOR -1 | 1 XOR 0 | 0 XOR 0 | 1 XOR NULL | 1 XOR 1 XOR 1 | 0 XOR 0 XOR 0 |
+----------+---------+---------+------------+---------------+---------------+
| 0 	   | 1		 | 0	   | NULL	    | 1			    | 0			    |
+----------+---------+---------+------------+---------------+---------------+
~~~

## 2.3、排序

使用 ORDER BY 子句排序 

- ASC（ascend）: 升序 
- DESC（descend）:降序 

ORDER BY 子句在SELECT语句的结尾。

单列排序：

~~~sql
SELECT last_name, job_id, department_id, hire_date
FROM employees
ORDER BY hire_date ;

SELECT last_name, job_id, department_id, hire_date
FROM employees
ORDER BY hire_date DESC ;
~~~

多列排序：

~~~sql
SELECT last_name, department_id, salary
FROM employees
ORDER BY department_id, salary DESC;
~~~

在对多列进行排序的时候，首先排序的第一列必须有相同的列值，才会对第二列进行排序。如果第 一列数据中所有值都是唯一的，将不再对第二列进行排序。

## 2.4、分页

所谓分页显示，就是将数据库中的结果集，一段一段显示出来需要的条件。

MySQL中使用 LIMIT 实现分页。

LIMIT 子句必须放在整个SELECT语句的最后。

格式： LIMIT [位置偏移量,] 行数

- 第一个“位置偏移量”参数指示MySQL从哪一行开始显示，是一个可选参数，如果不指定“位置偏移量”，将会从表中的第一条记录开始（第一条记录的位置偏移量是0，第二条记录的位置偏移量是 1）；
- 第二个参数“行数”指示返回的记录条数。

~~~sql
--前10条记录：
SELECT * FROM 表名 LIMIT 0,10;
或者
SELECT * FROM 表名 LIMIT 10;

--第11至20条记录：
SELECT * FROM 表名 LIMIT 10,10;

--第21至30条记录：
SELECT * FROM 表名 LIMIT 20,10;
~~~

**tip**：MySQL 8.0中可以使用“LIMIT 3 OFFSET 4”，意思是获取从第5条记录开始后面的3条记录，和“LIMIT 4,3;”返回的结果相同。

分页公式：（当前页数-1）*每页条数，每页条数

~~~sql
SELECT * FROM table
LIMIT(PageNo - 1)*PageSize,PageSize;
~~~

**优点**：约束返回结果的数量可以减小网络传输的压力，还可以提升查询效率。例如只需要一条记录，使用LIMIT 1 查询到一条即可返回

# 3、MySQL之多表查询

## 3.1、多表查询前提

多表查询，也称为关联查询，指两个或更多个表一起完成查询操作。 

前提条件：

- 这些一起查询的表之间是有关系的（一对一、一对多），它们之间一定是有关联字段，这个关联字段可能建立了外键，也可能没有建立外键。

## 3.2、错误案例引入

<img src="images/image-20220314210832123.png" alt="image-20220314210832123" style="zoom:70%;" />

~~~sql
#案例：查询员工的姓名及其部门名称
SELECT last_name, department_name
FROM employees, departments;

2889 rows in set (0.01 sec)
~~~

分析错误情况：

~~~sql
SELECT COUNT(employee_id) FROM employees;
#输出107行
SELECT COUNT(department_id)FROM departments;
#输出27行

SELECT 107*27 FROM dual;
~~~

此种错误称为：笛卡尔积错误

笛卡尔乘积是一个数学运算。假设有两个集合 X 和 Y，那么 X 和 Y 的笛卡尔积就是 X 和 Y 的所有可能组合。

也就是第一个对象来自于 X，第二个对象来自于 Y 的所有可能。组合的个数即为两个集合中元素个数的乘积数。

SQL92中，笛卡尔积也称为 **交叉连接** ，英文是 **CROSS** **JOIN** 。在 SQL99 中也是使用 CROSS JOIN 表示交叉连接。它的作用就是可以把任意表进行连接，即使这两张表不相关。在MySQL中如下情况会出现笛卡尔积：

~~~sql
#查询员工姓名和所在部门名称
SELECT last_name,department_name FROM employees,departments;
SELECT last_name,department_name FROM employees CROSS JOIN departments;
SELECT last_name,department_name FROM employees INNER JOIN departments;
SELECT last_name,department_name FROM employees JOIN departments;
~~~

笛卡尔积的错误会在下面条件下产生： 

- 省略多个表的连接条件（或关联条件） 

- 连接条件（或关联条件）无效 

- 所有表中的所有行互相连接 

为了避免笛卡尔积， 可以在 WHERE 加入有效的连接条件。 加入连接条件后，查询语法：

在表中有相同列时，在列名之前加上表名前缀。

~~~sql
SELECT table1.column, table2.column
FROM table1, table2
WHERE table1.column1 = table2.column2; #连接条件
~~~

## 3.3、多表查询分类

### 3.3.1、等值连接

<img src="images/image-20220314211728065.png" alt="image-20220314211728065" style="zoom:60%;" />

~~~sql
SELECT e.employee_id, e.last_name,
	   e.department_id, e.department_id,
	   d.location_id
FROM employees e, departments d
WHERE e.department_id = d.department_id;
~~~

- 多个表中有相同列时，必须在列名之前加上表名前缀。
- 在不同表中具有相同列名的列可以用 表名 加以区分。
- 使用别名可以简化查询。 
- 列名前使用表名前缀可以提高查询效率。

> 需要注意的是，如果我们使用了表的别名，在查询字段中、过滤条件中就只能使用别名进行代替， 不能使用原有的表名，否则就会报错。

~~~sql
SELECT e.employee_id,e.last_name,d.department_name,l.city,e.department_id,l.location_id
FROM employees e,departments d,locations l
WHERE e.`department_id` = d.`department_id`
	AND d.`location_id` = l.`location_id`;
~~~

- 多个连接条件与 AND 操作符
- 连接 n个表,至少需要n-1个连接条件。比如，连接三个表，至少需要两个连接条件。

### 3.3.2、非等值连接

<img src="images/image-20220314213815654.png" alt="image-20220314213815654" style="zoom:70%;" />

~~~sql
SELECT e.last_name,e.salary,j.grade_level
FROM employees e,job_grades j
#where e.salary between j.lowest_sal and j.highest_sal;
WHERE e.salary >= j.lowest_sal AND e.salary <= j.highest_sal;
~~~

<img src="images/image-20220314213958479.png" alt="image-20220314213958479" style="zoom:70%;" />

### 3.3.3、自连接

<img src="images/image-20220314214750192.png" alt="image-20220314214750192" style="zoom:70%;" />

~~~sql
SELECT CONCAT(worker.last_name ,' works for ', manager.last_name)
FROM employees worker, employees manager
WHERE worker.manager_id = manager.employee_id 
~~~

- 当table1和table2本质上是同一张表，只是用取别名的方式虚拟成两张表以代表不同的意义。然后两个表再进行内连接，外连接等查询。

### 3.3.4、内连接 与 外连接

- 除了查询满足条件的记录以外，外连接还可以查询某一方不满足条件的记录。
- **内连接**：合并具有同一列的两个以上的表的行， 结果集中不包含一个表与另一个表不匹配的行。 
- **外连接**：两个表在连接过程中除了返回满足连接条件的行以外还返回左（或右）表中不满足条件的行 ，这种连接称为左（或右） 外连接。没有匹配的行时, 结果表中相应的列为空(NULL)。 
  - 如果是左外连接，则连接条件中左边的表也称为 主表 ，右边的表称为 从表 。 
  - 如果是右外连接，则连接条件中右边的表也称为 主表 ，左边的表称为 从表 。

#### 3.3.4.1、SQL92版

在 SQL92 中采用（**+**）**代表从表所在的位置**。即左或右外连接中，(+) 表示哪个是从表。

**注意**：

- Oracle 对 SQL92 支持较好，而 MySQL 则不支持 SQL92 的外连接。
- 而且在 SQL92 中，只有左外连接和右外连接，没有满（或全）外连接。

~~~sql
#左外连接
SELECT last_name,department_name
FROM employees ,departments
WHERE employees.department_id = departments.department_id(+);

#右外连接
SELECT last_name,department_name
FROM employees ,departments
WHERE employees.department_id(+) = departments.department_id;
~~~

#### 3.3.4.2、SQL99版

使用 **JOIN ON** 子句连接

~~~sql
SELECT table1.column, table2.column,table3.column
FROM table1
JOIN table2 ON (table1 和 table2 的连接条件)
JOIN table3 ON (table2 和 table3 的连接条件)

~~~

逻辑类似于for循环

~~~java
for t1 in table1:
	for t2 in table2:
		if condition1:
			for t3 in table3:
				if condition2:
					output t1 + t2 + t3
~~~

**注意**：

- 可以使用 ON 子句指定额外的连接条件，这个连接条件是与其它条件分开的
- 关键字 JOIN、INNER JOIN、CROSS JOIN 的含义是一样的，都表示内连接

#### 3.3.4.3、INNER JOIN

~~~sql
SELECT 字段列表
FROM A表 INNER JOIN B表
ON 关联条件
WHERE 等其他子句;
~~~

#### 3.3.4.4、OUTER JOIN

**左外连接(LEFT OUTER JOIN)** 

~~~sql
#实现查询结果是A为主
SELECT 字段列表
FROM A表 LEFT JOIN B表
ON 关联条件
WHERE 等其他子句;
~~~

**右外连接(RIGHT OUTER JOIN)**

~~~sql
#实现查询结果是B为主
SELECT 字段列表
FROM A表 RIGHT JOIN B表
ON 关联条件
WHERE 等其他子句;
~~~

**满外连接(FULL OUTER JOIN)**

- 满外连接的结果 = 左右表匹配的数据 + 左表没有匹配到的数据 + 右表没有匹配到的数据。 
- SQL99是支持满外连接的。使用FULL JOIN 或 FULL OUTER JOIN来实现。 
- 需要注意的是，MySQL不支持FULL JOIN，但是可以用 LEFT JOIN UNION RIGHT join代替。

### 3.3.5、UNION

**合并查询结果** 利用UNION关键字，可以给出多条SELECT语句，并将它们的结果组合成单个结果集。

合并时，两个表对应的列数和数据类型必须相同，并且相互对应。各个SELECT语句之间使用UNION或UNION ALL关键字分隔。

~~~sql
SELECT column,... FROM table1
UNION [ALL]
SELECT column,... FROM table2
~~~

- **UNION** 操作符返回两个查询的结果集的并集，去除重复记录。
- **UNION ALL** 操作符返回两个查询的结果集的并集。对于两个结果集的重复部分，不去重。

**注意**：执行UNION ALL语句时所需要的资源比UNION语句少。如果明确知道合并数据后的结果数据不存在重复数据，或者不需要去除重复的数据，则尽量使用UNION ALL语句，以提高数据查询的效率。

### 3.3.6、七种连接总结

![image-20220315095149755](images/image-20220315095149755.png)

~~~sql
#中图：内连接 A∩B
SELECT employee_id,last_name,department_name
FROM employees e JOIN departments d
ON e.`department_id` = d.`department_id`;

#左上图：左外连接
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`;

#右上图：右外连接
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`;

#左中图：A - A∩B
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
#中间重合部份大家都不是null，现在要从表null
WHERE d.`department_id` IS NULL

#右中图：B-A∩B
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE e.`department_id` IS NULL

#左下图：满外连接
# 左中图 + 右上图 A∪B
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE d.`department_id` IS NULL
UNION ALL #没有去重操作，效率高
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`;

#右下图
#左中图 + 右中图 A ∪B- A∩B 或者 (A - A∩B) ∪ （B - A∩B）
SELECT employee_id,last_name,department_name
FROM employees e LEFT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE d.`department_id` IS NULL
UNION ALL
SELECT employee_id,last_name,department_name
FROM employees e RIGHT JOIN departments d
ON e.`department_id` = d.`department_id`
WHERE e.`department_id` IS NULL
~~~

### 3.3.7、SQL99新特性

#### 3.3.7.1、NATURAL JOIN

在SQL92标准中：

~~~sql
SELECT employee_id,last_name,department_name
FROM employees e JOIN departments d
ON e.`department_id` = d.`department_id`
AND e.`manager_id` = d.`manager_id`;
~~~

在 SQL99 中你可以写成：

~~~sql
SELECT employee_id,last_name,department_name
FROM employees e NATURAL JOIN departments d;
~~~

NATURAL JOIN 用来表示自然连接，理解为 SQL92 中的等值连接。会自动查询两张连接表中所有相同的字段 ，然后进行 等值 连接 。

#### 3.3.7.2、USING

SQL99还支持使用 USING 指定数据表里的 **同名字段** 进行等值连接。但是只能配合JOIN一起使用。比如：

~~~sql
SELECT employee_id,last_name,department_name
FROM employees e JOIN departments d
USING (department_id);
~~~

与自然连接 NATURAL JOIN 不同的是，USING 指定了具体的相同的字段名称，你需要在 USING 的括号 () 中填入要指定的同名字段。

同时使用 JOIN...USING 可以简化 JOIN ON 的等值连接。

它与下面的 SQL 查询结果是相同的：

~~~sql
SELECT employee_id,last_name,department_name
FROM employees e ,departments d
WHERE e.department_id = d.department_id;
~~~

~~~sql
#把关联字段写在using()中，只能和JOIN一起使用
#而且两个表中的关联字段必须名称相同，而且只能表示=
#查询员工姓名与基本工资
SELECT last_name,job_title
FROM employees INNER JOIN jobs USING(job_id);
~~~

~~~sql
SELECT last_name,job_title,department_name 
FROM employees INNER JOIN departments INNER JOIN jobs 
ON employees.department_id = departments.department_id 
AND employees.job_id = jobs.job_id;
~~~

# 4、MySQL之单行函数

## 4.1、基本概念

- 操作数据对象 
- 接受参数返回一个结果 
- 只对一行进行变换 
- 每行返回一个结果 
- 可以嵌套 
- 参数可以是一列或一个值

## 4.2、数值函数

### 4.2.1、基本函数

|函数  	   	 	   	   | 用法     					|
| ---- | ---- |
|FLOOR(x)  		 		| 返回小于或等于某个值的最大整数	 |
|ABS(x)   			 	| 返回x的绝对值     			   |
|LEAST(e1,e2,e3…)		|返回列表中的最小值 |
|SIGN(X)   				| 返回X的符号。正数返回1，负数返回-1，0返回0     |
|GREATEST(e1,e2,e3…)  	|返回列表中的最大值  |
|PI()   				|  返回圆周率的值    |
|MOD(x,y)  				| 返回X除以Y后的余数 |
|CEIL(x)，CEILING(x)     |  返回大于或等于某个值的最小整数    |
|RAND()  				| 返回0~1的随机值 |
| RAND(x)   			|返回0~1的随机值，其中x的值用作种子值，相同的X值会产生相同的随机数|
|ROUND(x)  				| 返回一个对x的值进行四舍五入后，最接近于X的整数 |
|ROUND(x,y)    			 |  返回一个对x的值进行四舍五入后最接近X的值，并保留到小数点后面Y位|
|TRUNCATE(x,y)  		| 返回数字x截断为y位小数的结果 |
|SQRT(x)    			|  返回x的平方根。当X的值为负数时，返回NULL     |

~~~sql
SELECT
ABS(-123),ABS(32),SIGN(-23),SIGN(43),PI(),CEIL(32.32),CEILING(-43.23),FLOOR(32.32),
FLOOR(-43.23),MOD(12,5)
FROM DUAL;
~~~

![image-20220315105257798](images/image-20220315105257798.png)

~~~sql
SELECT RAND(),RAND(),RAND(10),RAND(10),RAND(-1),RAND(-1)
FROM DUAL;
~~~

![image-20220315105320409](images/image-20220315105320409.png)

~~~sql
SELECT
ROUND(12.33),ROUND(12.343,2),ROUND(12.324,-1),TRUNCATE(12.66,1),TRUNCATE(12.66,-1)
FROM DUAL;
~~~

![image-20220315105345243](images/image-20220315105345243.png)

### 4.2.2、角度弧度互换函数

| 函数       | 用法                                  |
| ---------- | ------------------------------------- |
| RADIANS(x) | 将角度转化为弧度，其中，参数x为角度值 |
| DEGREES(x) | 将弧度转化为角度，其中，参数x为弧度值 |

~~~sql
SELECT RADIANS(30),RADIANS(60),RADIANS(90),DEGREES(2*PI()),DEGREES(RADIANS(90))
FROM DUAL;
~~~

![image-20220315105500917](images/image-20220315105500917.png)

### 4.2.3、三角函数

| 函数 | 用法 |
| ---- | ---- |
|SIN(x)|返回x的正弦值，其中，参数x为弧度值|
|ASIN(x) |返回x的反正弦值，即获取正弦为x的值。如果x的值不在-1到1之间，则返回NULL|
|COS(x) |返回x的余弦值，其中，参数x为弧度值|
|ACOS(x) |返回x的反余弦值，即获取余弦为x的值。如果x的值不在-1到1之间，则返回NULL|
|TAN(x) |返回x的正切值，其中，参数x为弧度值|
|ATAN(x) |返回x的反正切值，即返回正切值为x的值|
|ATAN2(m,n) |返回两个参数的反正切值|
|COT(x) |返回x的余切值，其中，X为弧度值|

ATAN2(M,N)函数返回两个参数的反正切值。 与ATAN(X)函数相比，ATAN2(M,N)需要两个参数，例如有两个 点point(x1,y1)和point(x2,y2)，使用ATAN(X)函数计算反正切值为ATAN((y2-y1)/(x2-x1))，使用ATAN2(M,N)计 算反正切值则为ATAN2(y2-y1,x2-x1)。

由使用方式可以看出，当x2-x1等于0时，ATAN(X)函数会报错，而 ATAN2(M,N)函数则仍然可以计算。

~~~sql
SELECT
SIN(RADIANS(30)),DEGREES(ASIN(1)),TAN(RADIANS(45)),DEGREES(ATAN(1)),DEGREES(ATAN2(1,1))
FROM DUAL;
~~~

![image-20220315105917013](images/image-20220315105917013.png)

### 4.2.4、指数对数函数

| 函数 | 用法 |
| ---- | ---- |
|POW(x,y)，POWER(X,Y) |返回x的y次方|
|EXP(X) |返回e的X次方，其中e是一个常数，2.718281828459045|
|LN(X)，LOG(X) |返回以e为底的X的对数，当X <= 0 时，返回的结果为NULL|
|LOG10(X) |返回以10为底的X的对数，当X <= 0 时，返回的结果为NULL|
|LOG2(X) |返回以2为底的X的对数，当X <= 0 时，返回NULL|

~~~sql
SELECT POW(2,5),POWER(2,4),EXP(2),LN(10),LOG10(10),LOG2(4)
FROM DUAL;
~~~

![image-20220315110105804](images/image-20220315110105804.png)

### 4.2.5、进制转换函数

| 函数 | 用法 |
| ---- | ---- |
|BIN(x) |返回x的二进制编码|
|HEX(x) |返回x的十六进制编码|
|OCT(x) |返回x的八进制编码|
|CONV(x,f1,f2) |返回f1进制数变成f2进制数|

~~~sql
SELECT BIN(10),HEX(10),OCT(10),CONV(10,2,8)
FROM DUAL;
~~~

![image-20220315110247137](images/image-20220315110247137.png)

























# 扩展

![image-20220314100332774](images/image-20220314100332774.png)

![image-20220314100349871](images/image-20220314100349871.png)

![image-20220314103248300](images/image-20220314103248300.png)



# 问题

## 解决Mysql中文乱码

修改mysql的数据目录下的my.ini配置文，然后重启

从MySQL 8.0 开始，数据库的默认编码改为 utf8mb4

```text
default-character-set=utf8 #默认字符集
[mysqld] # 大概在76行左右，在其下添加
...
character-set-server=utf8
collation-server=utf8_general_ci
```

## 忘记root密码

通过任务管理器，关掉mysqld(服务进程) 

通过命令行+特殊参数开启

```cmd
mysqld mysqld -- defaults-file="D:\ProgramFiles\mysql\MySQLServer5.7Data\my.ini" --skip-grant-tables 
```

此时，mysqld服务进程已经打开。并且不需要权限检查 

mysql -uroot 无密码登陆服务器。

另启动一个客户端进行修改权限表 

（1） use mysql;

 （2）update user set authentication_string=password('新密码') where user='root' and Host='localhost'; 

（3）flush privileges; 

通过任务管理器，关掉mysqld服务进程。

再次通过服务管理，打开mysql服务。 

即可用修改后的新密码登陆。

## mysql命令报“不是内部或外部命令”

把mysql安装目录的bin目录配置到环境变量path中

<img src="images/image-20220312170352518.png" alt="image-20220312170352518" style="zoom:80%;" />

## 命令行客户端的字符集问题

```sql
mysql> INSERT INTO t_stu VALUES(1,'张三','男');
ERROR 1366 (HY000): Incorrect string value: '\xD5\xC5\xC8\xFD' for column 'sname' at
row 1
```

原因：服务器端认为你的客户端的字符集是utf-8，而实际上你的客户端的字符集是GBK。

<img src="images/image-20220312170520934.png" alt="image-20220312170520934" style="zoom:80%;" />

查看所有字符集：SHOW VARIABLES LIKE 'character_set_%';

设置当前连接的客户端字符集 “SET NAMES GBK;”

## 修改数据库和表的字符编码

使用 alter语句修改编码。

```sql
alter table 表名 charset utf8; 
alter table 表名 modify name varchar(20) charset utf8; 
alter database k charset utf8; 
```

