# 1、Go 安装

Windos 直接官网下载 msi 文件安装即可

Linux 

~~~bash
# 准备环境
yum install mercurial git gcc -y
~~~

~~~bash
# 下载 go
cd /usr/local/
wget https://go.googlecode.com/files/go1.13.linux-amd64.tar.gz
tar -zxvf go1.13.linux-amd64.tar.gz

# 创建 GoPath 用于存放 Go 项目
cd /home/
mkdir go
cd go/
mkdir bin
mkdir src
mkdir pkg

vi /etc/profile
export GOROOT=/usr/local/go     # Go 安装目录
export PATH=$GOROOT/bin:$PATH
export GOPATH=/home/go  		# Go 项目目录
source /etc/profile
~~~

~~~bash
# 查看 Go 是否安装成功
go version
~~~





# 2、Go 基础

## 1、简介

### 1、基本特征

Go 的函数、变量、常量、自定义类型、包(package) 的命名方式遵循以下规则

- 首字符可以是任意的 Unicode 字符或者下划线

- 剩余字符可以是 Unicode 字符、下划线、数字

- 字符长度不限

Go 只有 25 个关键字

```
break        default      func         interface    select
case         defer        go           map          struct
chan         else         goto         package      switch
const        fallthrough  if           range        type
continue     for          import       return       var
```

Go 还有 37 个保留字

```
Constants:    true  false  iota  nil

Types:    	int  int8  int16  int32  int64  
			uint  uint8  uint16  uint32  uint64  uintptr
			float32  float64  complex128  complex64
			bool  byte  rune  string  error

Functions:   make  len  cap  new  append  copy  close  delete
			complex  real  imag
			panic  recover
```

可见性：

- 声明在函数内部，是函数的本地值，类似 private
- 声明在函数外部，是对当前包内可见的全局值，类似 protect
- 声明在函数外部且首字母大写是所有包可见的全局值，类似 public

Go 有四种主要声明方式：

- var（声明变量）
- const（声明常量）
- type（声明类型）
- func（声明函数）

>Go 的程序是保存在多个. go 文件中
>
>文件的第一行就是 package XXX 声明，用来说明该文件属于哪个包(package)
>
>package 声明下来就是 import 声明
>
>再下来是类型，变量，常量，函数的声明

一个 Go 工程中主要包含以下三个目录：

- src	源代码文件
- pkg	包文件
- bin	相关bin文件



### 2、内置类型与函数

#### 1、值类型

```
bool
int(32 or 64), int8, int16, int32, int64
uint(32 or 64), uint8(byte), uint16, uint32, uint64
float32, float64
string
complex64, complex128
array    -- 固定长度的数组
```



#### 2、引用类型

(指针类型)

```
slice   -- 序列数组(最常用)
map     -- 映射
chan    -- 管道
```



#### 3、内置函数

Go 语言拥有一些不需要进行导入操作就可以使用的内置函数，可以针对不同的类型进行操作，例如：len、cap 和 append，或必须用于系统级的操作，例如：panic，因此它们需要直接获得编译器的支持

```
append          -- 用来追加元素到数组、slice中,返回修改后的数组、slice
close           -- 主要用来关闭channel
delete            -- 从map中删除key对应的value
panic            -- 停止常规的goroutine  （panic和recover：用来做错误处理）
recover         -- 允许程序定义goroutine的panic动作
real            -- 返回complex的实部   （complex、real imag：用于创建和操作复数）
imag            -- 返回complex的虚部
make            -- 用来分配内存，返回Type本身(只能应用于slice, map, channel)
new                -- 用来分配内存，主要用来分配值类型，比如int、struct。返回指向Type的指针
cap                -- capacity是容量的意思，用于返回某个类型的最大容量（只能用于切片和 map）
copy            -- 用于复制和连接slice，返回复制的数目
len                -- 来求长度，比如string、array、slice、map、channel ，返回长度
print、println     -- 底层打印函数，在部署环境中建议使用 fmt 包
```



#### 4、内置接口error

```
type error interface { // 只要实现了 Error() 函数，返回值为 String 的都实现了 err 接口
	Error()    String
}
```



### 3、Init 函数和 main 函数

#### 1、init 函数

go 语言中 init 函数用于包(package)的初始化，该函数是 go 语言的一个重要特性

有下面的特征：

- init 函数是用于程序执行前做包的初始化的函数，比如：初始化包里的变量等

- 每个包可以拥有多个 init 函数
- 包的每个源文件也可以拥有多个 init 函数

- 同一个包中多个 init 函数的执行顺序 go 语言没有明确的定义(说明)

- 不同包的 init 函数按照包导入的依赖关系决定该初始化函数的执行顺序

- init 函数不能被其他函数调用，而是在 main 函数执行之前，自动被调用



#### 2、main 函数

Go语言程序的默认入口函数(主函数)：func main() 函数体用｛｝一对括号包裹

```
func main(){
	// 函数体
}
```

异同点：

- 两个函数在定义时不能有任何的参数和返回值，且 Go 程序自动调用
- init 可以应用于任意包中，且可以重复定义多个
- main 函数只能用于 main 包中，且只能定义一个

两个函数的执行顺序：

- 对同一个 go 文件的 init() 调用顺序是从上到下的
- 对同一个 package 中不同文件是按文件名字符串比较从小到大顺序调用各文件中的 init() 函数。
- 对于不同的 package，如果不相互依赖的话，按照 main 包中先 import 的后调用的顺序调用其包中的 init()，如果 package 存在依赖，则先调用最早被依赖的 package 中的 init()，最后调用 main 函数
- 如果 init 函数中使用了 println() 或者 print() 会发现在执行过程中这两个不会按照想象中的顺序执行，这两个函数官方只推荐在测试环境中使用，对于正式环境不要使用



### 4、Go 命令

go env 用于打印 Go 语言的环境信息

go run 命令可以编译并运行命令源码文件

go get 可以根据要求和实际情况从互联网上下载或更新指定的代码包及其依赖包，并对它们进行编译和安装

go build 命令用于编译我们指定的源码文件或代码包以及它们的依赖包

go install 用于编译并安装指定的代码包及它们的依赖包

go clean 命令会删除掉执行其它命令时产生的一些文件和目录

go doc 命令可以打印附于 Go 语言程序实体上的文档，可以通过把程序实体的标识符作为该命令的参数来达到查看其文档的目的

go test 命令用于对 Go 语言编写的程序进行测试

go list 命令的作用是列出指定的代码包的信息

go fix 会把指定代码包的所有 Go 语言源码文件中的旧版本代码修正为新版本的代码

go vet 是一个用于检查 Go 语言源码中静态错误的简单工具

go tool pprof 命令来交互式的访问概要文件的内容



### 5、下划线

“_”是特殊标识符，用来忽略结果

import 下划线（如：import _ ".hello/imp"）的作用：当导入一个包时，该包下的文件里所有 init() 函数都会被执行，然而有些时候并不需要把整个包都导入进来，仅仅是是希望它执行 init() 函数而已，这个时候就可以使用 import 引用该包，即使用【import _ 包路径】只是引用该包，仅仅是为了调用 init() 函数，所以无法通过包名来调用包中的其他函数

下划线在代码中，意思是忽略这个变量

~~~go
import "database/sql"
import _ "github.com/go-sql-driver/mysql"

// 第二个 import 就是不直接使用 mysql 包，只是执行一下这个包的 init 函数
// 把 mysql 的驱动注册到 sql 包里，然后程序里就可以使用 sql 包来访问 mysql 数据库了
~~~



### 6、变量与常量

#### 1、变量

变量（Variable）的功能是存储数据，不同的变量保存的数据类型可能会不一样

Go 语言中的每一个变量都有自己的类型，且变量需要声明后才能使用，同一作用域内不支持重复声明

声明格式：

~~~go
var 变量名 变量类型

// 批量声明
var (
        a string
        b int
        c bool
        d float32
    )
~~~

Go语言在声明变量的时候，会自动对变量对应的内存区域进行初始化操作，每个变量会被初始化成其类型的默认值，例如：整型和浮点型变量的默认值为0，字符串变量的默认值为空字符串，布尔型变量默认为 false，切片、函数、指针变量的默认为 nil

可以在声明变量的时候为其指定初始值：

~~~go
var 变量名 类型 = 表达式

var name, sex = "pprof.cn", 1
~~~

类型推导，就是声明时将变量的类型省略，编译器会根据等号右边的值来推导变量的类型完成初始化

```go
var name = "pprof.cn"
var sex = 1
```

短变量声明，在函数内部，可以使用更简略的 := 方式声明并初始化变量

~~~go
func main() {
    n := 10
    m := 200 // 此处声明局部变量m
    fmt.Println(m, n)
}
~~~

匿名变量，在使用多重赋值时，如果想要忽略某个值，可以使用匿名变量（anonymous variable）匿名变量用一个下划线 _ 表示

- 匿名变量不占用命名空间，不会分配内存，所以匿名变量之间不存在重复声明

~~~go
func foo() (int, string) {
    return 10, "Q1mi"
}
func main() {
    x, _ := foo()
    _, y := foo()
    fmt.Println("x=", x)
    fmt.Println("y=", y)
}
~~~

**注意**：

- 函数外的每个语句都必须以关键字开始（var、const、func等）
- := 不能使用在函数外
- _ 多用于占位，表示忽略值



#### 2、常量

相对于变量，常量是恒定不变的值，多用于定义程序运行期间不会改变的那些值

常量的声明和变量声明非常类似，只是把 var 换成了 const，常量在定义的时候必须赋值

```go
// 声明了 pi 和 e 这两个常量之后，在整个程序运行期间它们的值都不能再发生变化了
const pi = 3.1415
const e = 2.7182
```

多个常量也可以一起声明：

```go
const (
    pi = 3.1415
    e = 2.7182
)
```

const 同时声明多个常量时，如果省略了值则表示和上面一行的值相同

```
// 常量n1、n2、n3的值都是100
const (
    n1 = 100
    n2
    n3
)
```



#### 3、iota

iota 是 go 语言的常量计数器，只能在常量的表达式中使用

iota 在 const 关键字出现时将被重置为 0，const 中每新增一行常量声明将使 iota 计数一次( iota 可理解为 const 语句块中的行索引) 使用iota 能简化定义，在定义枚举时很有用

举个例子：

```go
const (
    n1 = iota //0
    n2        //1
    n3        //2
    n4        //3
)
```

使用_跳过某些值

```go
const (
    n1 = iota //0
    n2        //1
    _
    n4        //3
)
```

iota 声明中间插队

```
const (
    n1 = iota // 0
    n2 = 100  // 100
    n3 = iota // 2
    n4        // 3
)
const n5 = iota // 0
```

定义数量级 （这里的 << 表示左移操作，1<<10 表示将 1 的二进制表示向左移 10 位，也就是由 1 变成了 10000000000，也就是十进制的1024，同理 2<<2 表示将 2 的二进制表示向左移 2 位，也就是由 10 变成了 1000，也就是十进制的8）

```go
const (
    _  = iota
    KB = 1 << (10 * iota)
    MB = 1 << (10 * iota)
    GB = 1 << (10 * iota)
    TB = 1 << (10 * iota)
    PB = 1 << (10 * iota)
)
```

多个iota定义在一行

```go
const (
    a, b = iota + 1, iota + 2 // 1,2
    c, d                      // 2,3
    e, f                      // 3,4
)
```



### 7、基本类型

#### 1、介绍

| 类型          | 长度(字节) | 默认值 | 说明                                         |
| ------------- | ---------- | ------ | -------------------------------------------- |
| bool          | 1          | false  |                                              |
| byte          | 1          | 0      | uint8                                        |
| rune          | 4          | 0      | Unicode Code Point, int32                    |
| int, uint     | 4 或 8     | 0      | 32 或 64 位                                  |
| int8, uint8   | 1          | 0      | -128 ~ 127, 0 ~ 255，byte 是 uint8 的别名    |
| int16, uint16 | 2          | 0      | -32768 ~ 32767, 0 ~ 65535                    |
| int32, uint32 | 4          | 0      | -21亿 ~ 21亿, 0 ~ 42亿，rune 是 int32 的别名 |
| int64, uint64 | 8          | 0      |                                              |
| float32       | 4          | 0.0    |                                              |
| float64       | 8          | 0.0    |                                              |
| complex64     | 8          |        |                                              |
| complex128    | 16         |        |                                              |
| uintptr       | 4 或 8     |        | 以存储指针的 uint32 或 uint64 整数           |
| array         |            |        | 值类型                                       |
| struct        |            |        | 值类型                                       |
| string        |            | ""     | UTF-8 字符串                                 |
| slice         |            | nil    | 引用类型                                     |
| map           |            | nil    | 引用类型                                     |
| channel       |            | nil    | 引用类型                                     |
| interface     |            | nil    | 接口                                         |
| function      |            | nil    | 函数                                         |

支持八进制、 六进制，以及科学记数法。标准库 math 定义了各数字类型取值范围。

```
a, b, c, d := 071, 0x1F, 1e9, math.MinInt16
```

空指针值 nil，而非C/C++ NULL

#### 2、整型

整型分为以下两个大类：

- 按长度分为：int8、int16、int32、int64 
- 对应的无符号整型：uint8、uint16、uint32、uint64

其中，uint8 就是熟知的 byte 型，int16 对应 C 语言中的 short 型，int64 对应 C 语言中的 long 型



#### 3、浮点型

Go 语言支持两种浮点型数：float32 和 float64

这两种浮点型数据格式遵循 IEEE 754 标准： 

- float32 的浮点数的最大范围约为3.4e38，可以使用常量定义：math.MaxFloat32
- float64 的浮点数的最大范围约为 1.8e308，可以使用一个常量定义：math.MaxFloat64



#### 4、复数

complex64 和 complex128

复数有实部和虚部，complex64 的实部和虚部为 32 位，complex128 的实部和虚部为 64 位



#### 5、布尔值

Go 语言中以 bool 类型进行声明布尔型数据，布尔型数据只有 true（真）和 false（假）两个值

**注意**：

- 布尔类型变量的默认值为false
- Go 语言中不允许将整型强制转换为布尔型
- 布尔型无法参与数值运算，也无法与其他类型进行转换



#### 6、字符串

##### 1、介绍

Go 语言中的字符串以原生数据类型出现，使用字符串就像使用其他原生数据类型（int、bool、float32、float64 等）一样

Go 语言里的字符串的内部实现使用 UTF-8 编码，字符串的值为双引号(")中的内容，可以在 Go 语言的源码中直接添加非 ASCII 码字符

例如：

```go
s1 := "hello"
s2 := "你好"
```



##### 1、字符串转义符

Go 语言的字符串常见转义符包含回车、换行、单双引号、制表符等，如下表所示

| 转义 | 含义                               |
| ---- | ---------------------------------- |
| \r   | 回车符（返回行首）                 |
| \n   | 换行符（直接跳到下一行的同列位置） |
| \t   | 制表符                             |
| \'   | 单引号                             |
| \"   | 双引号                             |
| \    | 反斜杠                             |

要打印一个Windows平台下的一个文件路径：

```go
package main
import (
    "fmt"
)
func main() {
    fmt.Println("str := \"c:\\pprof\\main.exe\"")
}
```



##### 3、多行字符串

Go 语言中要定义一个多行字符串时，就必须使用反引号字符：

```go
s1 := `第一行
第二行
第三行
`
fmt.Println(s1)
```

反引号间换行将被作为字符串中的换行，但是所有的转义字符均无效，文本将会原样输出



##### 4、字符串的常用操作

| 方法                                | 介绍           |
| ----------------------------------- | -------------- |
| len(str)                            | 求长度         |
| +或fmt.Sprintf                      | 拼接字符串     |
| strings.Split                       | 分割           |
| strings.Contains                    | 判断是否包含   |
| strings.HasPrefix,strings.HasSuffix | 前缀/后缀判断  |
| strings.Index(),strings.LastIndex() | 子串出现的位置 |
| strings.Join(a[]string, sep string) | join操作       |



#### 7、byte 和 rune 类型

##### 1、介绍

组成每个字符串的元素叫做“字符”，可以通过遍历或者单个获取字符串元素获得字符

字符用单引号（’）包裹起来，如：

```go
var a := '中'

var b := 'x'
```

Go 语言的字符有以下两种：

- uint8 类型，或者叫 byte 型，代表了 ASCII 码的一个字符
- rune 类型，代表一个 UTF-8 字符

当需要处理中文、日文或者其他复合字符时，则需要用到 rune 类型，rune 类型实际是一个 int32，Go 使用了特殊的 rune 类型来处理 Unicode，让基于 Unicode 的文本处理更为方便，也可以使用 byte 型进行默认字符串处理，性能和扩展性都有照顾

```go
// 遍历字符串
func traversalString() {
    s := "pprof.cn博客"
    for i := 0; i < len(s); i++ { //byte
        fmt.Printf("%v(%c) ", s[i], s[i])
    }
    fmt.Println()
    for _, r := range s { //rune
        fmt.Printf("%v(%c) ", r, r)
    }
    fmt.Println()
}
```

输出：

```
112(p) 112(p) 114(r) 111(o) 102(f) 46(.) 99(c) 110(n) 229(å) 141() 154() 229(å) 174(®) 162(¢)
112(p) 112(p) 114(r) 111(o) 102(f) 46(.) 99(c) 110(n) 21338(博) 23458(客)
```

因为 UTF-8 编码下一个中文汉字由 3~4 个字节组成，所以不能简单的按照字节去遍历一个包含中文的字符串，否则就会出现上面输出中第一行的结果

字符串底层是一个 byte 数组，所以可以和 []byte 类型相互转换

字符串是不能修改的，字符串是由 byte 字节组成，所以字符串的长度是 byte 字节的长度 

rune 类型用来表示 utf8 字符，一个 rune 字符由一个或多个 byte 组成



##### 2、修改字符串

要修改字符串，需要先将其转换成 []rune 或 []byte ，完成后再转换为 string

无论哪种转换，都会重新分配内存，并复制字节数组

```go
func changeString() {
    s1 := "hello"
    // 强制类型转换
    byteS1 := []byte(s1)
    byteS1[0] = 'H'
    fmt.Println(string(byteS1))

    s2 := "博客"
    runeS2 := []rune(s2)
    runeS2[0] = '狗'
    fmt.Println(string(runeS2))
}
```



#### 8、类型转换

Go 语言中只有强制类型转换，没有隐式类型转换

该语法只能在两个类型之间支持相互转换的时候使用

强制类型转换的基本语法如下：

```go
T(表达式)
```

其中，T表示要转换的类型，表达式包括变量、复杂算子、函数返回值等

```go
func sqrtDemo() {
    var a, b = 3, 4
    var c int
    // math.Sqrt()接收的参数是float64类型，需要强制转换
    c = int(math.Sqrt(float64(a*a + b*b)))
    fmt.Println(c)
}
```



### 8、数组

#### 1、概述

同一种数据类型的**固定长度**的序列

数组长度必须是常量，且是类型的组成部分，一旦定义，长度不能变

长度是数组类型的一部分，因此，var a[5] int 和 var a[10]int 是不同的类型

~~~go
// 数组定义
var a [len]int
~~~

数组可以通过下标进行访问，下标是从 0 开始，最后一个元素下标是：len-1

~~~go
for i := 0; i < len(a); i++ {
}
for index, v := range a {
}
~~~

访问越界，如果下标在数组合法范围之外，则触发访问越界，会 panic

**数组是值类型**，赋值和传参会复制整个数组，而不是指针，因此改变副本的值，不会改变本身的值

支持 "=="、"!=" 操作符

**内存总是被初始化过的**

指针数组 **[n]*T**，数组指针 **\*[n]T**

内置函数 len 和 cap 都返回数组长度 (元素数量)

~~~go
a := [2]int{}
println(len(a), cap(a)) 
~~~





#### 2、初始化

**一维数组**：

~~~go
// 全局
var arr0 [5]int = [5]int{1, 2, 3}
var arr1 = [5]int{1, 2, 3, 4, 5}
var arr2 = [...]int{1, 2, 3, 4, 5, 6}
var str = [5]string{3: "hello world", 4: "tom"}

// 局部
a := [3]int{1, 2}           // 未初始化元素值为对应类型零值
b := [...]int{1, 2, 3, 4}   // 通过初始化值确定数组长度
c := [5]int{2: 100, 4: 200} // 使用索引号初始化元素
d := [...]struct {
    name string
    age  uint8
}{
    {"user1", 10}, // 可省略元素类型
    {"user2", 20},
}
~~~



**多维数组**：

~~~go
// 全局
var arr0 [5][3]int
var arr1 [2][3]int = [...][3]int{{1, 2, 3}, {7, 8, 9}}

// 局部
a := [2][3]int{{1, 2, 3}, {4, 5, 6}}
b := [...][2]int{{1, 1}, {2, 2}, {3, 3}} // 第 2 纬度不能用 "..."
~~~

~~~go
// 遍历
var f [2][3]int = [...][3]int{{1, 2, 3}, {7, 8, 9}}

for k1, v1 := range f {
    for k2, v2 := range v1 {
        fmt.Printf("(%d,%d)=%d ", k1, k2, v2)
    }
    fmt.Println()
}
~~~



**注意**：

- 值拷贝行为会造成性能问题，通常会建议使用 slice，或数组指针



#### 3、拷贝与传参

~~~go
// 由于数组是值类型
// 必须使用数组指针作为参数传入，才能修改数组内容
func printArr(arr *[5]int) {
    arr[0] = 10
    for i, v := range arr {
        fmt.Println(i, v)
    }
}

func main() {
    var arr1 [5]int
    printArr(&arr1)
    fmt.Println(arr1)
    arr2 := [...]int{2, 4, 6, 8, 10}
    printArr(&arr2)
    fmt.Println(arr2)
}
~~~



### 9、切片

#### 1、概述

切片是数组的一个**引用**，因此**切片是引用类型**，但自身是结构体，值拷贝传递

~~~go
func testSlice(s []int) {
	s[0] = 5
}
func main() {
	var arr1 = []int{1, 2, 3}
	testSlice(arr1)
	fmt.Println(arr1)
    fmt.Println(reflect.TypeOf(arr1).Kind())
}
5 2 3
slice
~~~

切片的**长度可变**，其是一个可变的数组

切片遍历方式和数组一样，可以用 len() 求长度，表示可用元素数量，读写操作不能超过该限制

cap 可以求出 slice 最大扩张容量，不能超出数组限制，0 <= len(slice) <= len(array)，其中 array 是 slice 引用的数组

~~~go
// 切片定义
// var 变量名 []类型
var str []string
var arr []int
~~~



**注意**：

- slice 并不是数组或数组指针，其内部通过指针和相关属性引用数组片段，实现变长方案
- 如果 slice == nil，那么 len、cap 结果都等于 0
- 切片是引用类型，如果只声明，那么零值是 nil，还需要进一步的分配内存

<img src="images/image-20230210230257674.png" alt="image-20230210230257674" style="zoom:67%;" />



#### 2、初始化

~~~go
func main() {
    // 声明切片，为分配内存，为 nil
    var s1 []int
    if s1 == nil {
        fmt.Println("是空")
    } else {
        fmt.Println("不是空")
    }
    // :=
    s2 := []int{}
    s5 := []int{1, 2, 3}
    fmt.Println(s5)

    // make()
    // 第二个参数: len 长度
    // 第三个参数: cap 容量，可省略，默认 cap = len
    var s3 []int = make([]int, 0)
    fmt.Println(s1, s2, s3)
    var s4 []int = make([]int, 0, 0)
    fmt.Println(s4)

    // 从数组切片，前包后不包
    arr := [5]int{1, 2, 3, 4, 5}
    var s6 []int
    s6 = arr[1:4]
    fmt.Println(s6)
}
~~~

~~~go
// 全局
var arr = [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
var slice0 []int = arr[start:end] 
var slice1 []int = arr[:end]      
var slice2 []int = arr[start:]
var slice3 []int = arr[:] 		   // 两端都不写，默认 start 到 end
var slice4 = arr[:len(arr)-1]      // 去掉切片的最后一个元素

// 局部
arr2 := [...]int{9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
slice5 := arr[start:end]
slice6 := arr[:end]        
slice7 := arr[start:]     
slice8 := arr[:]  
slice9 := arr[:len(arr)-1] //去掉切片的最后一个元素
~~~



**注意**：

- 切片时，起止位默认是 0 到 len，可默认不写
- 使用 make 函数创建时，必须满足 cap >= len >= 0
- data[:6:8] 每个数字前都有个冒号，slice 内容为 data 从 0 到第 6 位，长度 len 为 6，最大扩充项 cap 设置为 8
  - a[x:y:z]，切片内容：[x:y]，切片长度：y-x，切片容量：z-x



~~~go
// 遍历
data := [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
slice := data[:]
for index, value := range slice {
    fmt.Printf("inde : %v , value : %v\n", index, value)
}
~~~



#### 3、追加与拷贝

使用 **append** 函数向 slice 尾部添加数据，并**返回新的 slice 对象**

```go
func main() {
    s1 := make([]int, 0, 5)
    fmt.Printf("%p\n", &s1)
    s2 := append(s1, 1)
    fmt.Printf("%p\n", &s2)
    fmt.Println(s1, s2)

}
0xc42000a060
0xc42000a080
[] [1]
```

超出原 slice.cap 限制，就会**重新分配底层数组**，即便原数组并未填满，**重新分配的底层数组与原数组无关**

~~~go
func main() {
    data := [...]int{0, 1, 2, 3, 4, 10: 0}
    s := data[:2]
    s = append(s, 100, 200) 		// 一次 append 两个值，超出 s.cap 限制
    fmt.Println(s, data)         	// 重新分配的底层数组，与原数组无关
    fmt.Println(&s[0], &data[0]) 	// 比对底层数组起始指针
}
~~~

通常以 2 倍容量重新分配底层数组，在大批量添加数据时，建议一次性分配足够大的空间，以减少内存分配和数据复制开销，或初始化足够长的 len 属性，改用索引号进行操作



**注意**：

- 及时释放不再使用的 slice 对象，避免持有过期数组，造成 GC 无法回收



使用 **copy** 函数拷贝切片，copy 函数在两个 slice 间复制数据，复制长度**以 len 小的为准**，两个 slice 可指向同一底层数组，允许元素区间重叠

~~~go
func main() {
    data := [...]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}
    fmt.Println("array data : ", data)
    s1 := data[8:]
    s2 := data[:5]

    fmt.Printf("slice s1 : %v\n", s1)
    fmt.Printf("slice s2 : %v\n", s2)
    copy(s2, s1)

    fmt.Printf("copied slice s1 : %v\n", s1)
    fmt.Printf("copied slice s2 : %v\n", s2)
    fmt.Println("last array data : ", data)
}

array data :  [0 1 2 3 4 5 6 7 8 9]
slice s1 : [8 9]
slice s2 : [0 1 2 3 4]
copied slice s1 : [8 9]
copied slice s2 : [8 9 2 3 4]
last array data :  [8 9 2 3 4 5 6 7 8 9]
~~~



#### 4、字符串切片

string 底层就是一个 byte 的数组，因此也可以进行切片操作

~~~go
func main() {
    str := "hello world"
    
    s1 := str[0:5]
    fmt.Println(s1)
    
    s2 := str[6:]
    fmt.Println(s2)
}
hello
world
~~~

string 本身是不可变的，因此要改变 string 中字符，需要先切片，再转为 string 类型

~~~go
func main() {
    str := "Hello world"
    s := []byte(str) // 中文字符用[]rune(str)
    s[6] = 'G'
    s = s[:8]
    s = append(s, '!')
    str = string(s)
    fmt.Println(str)
}
~~~









## func

### 1、基本概念

#### 1、函数格式

~~~go
func 函数名(参数列表) [返回值] {
    函数体
}
~~~



#### 2、函数返回值

函数返回值有三种情况：

- 函数无返回

  - ~~~go
    func 函数名(参数列表) {
        函数体
    }
    ~~~

- 函数返回单个值

  - ~~~go
    func 函数名(参数列表) 返回值类型 {
        函数体
        return 返回值
    }
    ~~~

- 函数返回多个值

  - ~~~go
    func 函数名(参数列表) (返回值1类型, 返回值2类型...) {
        函数体
        return 返回值1,返回值2
    }
    ~~~



#### 3、不定参函数

指参数数量不确定的函数

~~~go
func 函数名(切片变量...数据类型) [返回值] {
    函数体
}
~~~

参数传递时的**注意**事项：

- 调用不定参函数时，可以不传递参数给函数

  - ~~~go
    func myFunc(numbers...int) { }
     
    func main() {
    	myFunc()
    }
    ~~~

- 不定参参数列表之前还可以有其他参数值

  - ~~~go
    func myFunc(str string, numbers...int) { }
     
    func main() {
    	myFunc("HelloWorld")
    	myFunc("HelloWorld", 1)
    	myFunc("HelloWorld", 1, 2)
    }
    ~~~

- 如果除了不定参参数列表还有其他参数，不定参参数列表必须放在最后，不能放在其他参数前面

  - ~~~go
    func sumNumbers(str string, numbers...int) (string, int){ 
    	total := 0
    	for _,number := range numbers {
    		total += number
    	}
    	return str, total
    }
     
    func main() {
    	fmt.Println(sumNumbers("sum", 1, 2, 3, 4))
    }
    ~~~



#### 4、函数闭包

闭包是由函数和与其相关的引用环境组合而成的实体

- 闭包会把函数和被访问的变量打包到一起，不再关心这个变量原来的作用域，闭包本身可以看作是独立对象
- 闭包函数与普通函数的最大区别就是**参数不是值传递，而是引用传递**，所以闭包函数可以操作自己函数以外的变量
- 闭包函数对外部变量进行了操作使其不能被回收，跨过了作用域的限制

~~~go
func adder() func(int) int {
    // 闭包
    // 变量 sum 与 匿名函数绑定
    // 只要下方的变量 pos 与 neg 不死亡其内相关的 sum 变量都会存在
	sum := 0
	return func(x int) int {
		sum += x
		return sum
	}
}

func main() {
	pos, neg := adder(), adder()
	for i := 0; i < 100; i++ {
		fmt.Println(
			pos(i),
			neg(-2*i),
		)
	}
    
    for i := 0; i< 100; i++ {
        // 会有问题，此 go 程与外部 for 循环构成了闭包，go 程还没来及启动变量 i 已经变化，也就是顺序错乱了
        go func() {
            println(i)
        }()
        
        // 解决方法就是打破闭包
        go func(x int) {
            println(x) 
        }(i)
    }
}
~~~



### 2、函数类别

#### 1、具名函数

指函数能够在返回前将值赋值给具名变量，并且在 return 后面可以省略返回值

~~~go
func 函数名(参数列表) (具名变量1 具名变量数据类型,具名变量2 具名变量数据类型...) {
    函数体
    return
}
~~~

~~~go
func sayHi() (x, y string){
	x = "Hello"
	y = "World"
	return
}

func main() {
	fmt.Println(sayHi())
}
~~~



#### 2、匿名函数

匿名函数就是没有名字的普通函数

匿名函数的调用有两种：

- 定义时赋值给变量，然后使用变量调用
- 定义时直接调用

~~~go
func main() {
    // 定义后赋值给变量
    var nua = func(a int) int {
        return a * a
    }
    println(nua())
    
    // 定义后直接调用,并把返回值赋值给变量
    t := func(a3 string, a4 string) string {
		return a3 + a4
	}("a3", "a4")
	
	println(t)
}
~~~



## defer

### 1、基本概念

defer 语句会将其后面跟随的语句进行延迟处理

在 defer关键字所属的函数即将返回时或退出时，被延迟处理的语句将按 defer 注册的逆序进行执行

**注意**：

- 通常用于释放资源

简单的例子：

~~~go
func main() {
	s := 1
	
	defer func(s *int) {
		fmt.Println(*s)
	}(&s)

	s++
}

2
~~~

~~~go
func test()(x int)  {
	 x = 10
	 defer func() {
	 	x++
	 }()
	 return x
}

11
~~~

~~~go
func main() {
	s := 1

    // 第二个执行
	defer func(s *int) {
		fmt.Println(*s)
	}(&s)

    // 第一个执行
	defer func(s *int) {
		fmt.Println(*s)
		*s++
	}(&s)

	s++
}

2
3
~~~

~~~go
// 这里是具名函数，将返回值赋给一个变量，由于defer执行时间是在所属函数返回之前，那么x的值就会被其改变
func testA(a int) (x int) {
	fmt.Println("testA:", a)
	defer func() {
		x++
	}()
	return a
}

func main() {
	a := testA(10)
	fmt.Println("main:", a)
}

testA: 10
main: 11
~~~



### 2、执行时间

go 中的 return 语句并不是原子性操作，一般是分为两步:

1. 将返回值赋值给一个变量
2. 执行RET指令

defer 就执行在1之后，2之前



### 3、具名/匿名函数区别

- 如果 defer 语句调用的是一个**具名函数**，需要注意该具名函数的某个参数是不是引用了另外一个函数

~~~go
// 例如下面的例子，在 defer 注册的时候，会执行testA函数，但是testB函数的执行会延迟到 defer 所属函数退出或返回时

func testA() int {
	fmt.Println("A start")
	return 1
}

func testB(a int) int {
	fmt.Println("B start")
	return a
}

func main() {
	fmt.Println("main start")
	defer testB(testA())
	fmt.Println("main end")
}

main start
A start 
main end
B start 
~~~



- 如果 defer 调用的是一个匿名函数，那么统统在 defer 所属函数即将退出时才执行

~~~go
func testA() int {
	fmt.Println("A start")
	return 1
}

func testB(a int) int {
	fmt.Println("B start")
	return a
}

func main() {
	fmt.Println("main start")
	defer func() {
		fmt.Println("func start")
		testB(testA())
		fmt.Println("func end")
	}()
	fmt.Println("main end")
}

main start
main end  
func start
A start   
B start   
func end 
~~~







## struct

### 1、基本概念

- 结构体就是将一个或多个变量组合到一起，形成新的类型
- Java中类本质就是结构体
- 结构体是值类型

~~~go
type 结构体名称 struct{
    // 成员或属性
    名称 类型
    // ....
}
~~~

**注意**：

- 如果结构体定义在函数外面，结构体名称首字母是否大写影响到结构体是否能跨包访问
- 如果结构体能跨包访问，属性首字母是否大写影响到属性是否跨包访问
- 当要将结构体对象转换为 JSON 时，对象中的属性首字母必须是大写，才能正常转换



### 2、属性赋值

结构体是值类型，当声明的时候就会开辟内存空间，其内的属性为默认值

- 按照结构体中属性的顺序进行赋值，可以省略属性名称
- 明确指定给哪些属性赋值，可以都赋值，也可以只给其中一部分赋值
- 可以通过结构体变量名称获取到属性进行赋值或查看

在方法传递时希望传递结构体地址，可以使用时结构体指针完成

~~~go
type People struct {
    Name string
    Age  int
}

func main() {
    // peo := new(People)
    // peo := People{age: 18, Name: "aaa"}
    var peo People 
   
    peo = People{"aaa1", 17}
    fmt.Println(peo)

    peo = People{Age: 18, Name: "aaa2"}
    fmt.Println(peo)

    peo.Age = 10
    peo.Name = "aaa3"
    fmt.Println(peo)

}
~~~



### 3、结构体指针

可以定义指向结构体的指针类似于其他指针变量

**作用**：

- 如果想在函数里面改变结构体数据内容，需要传入指针，因为 Go 是值传递而不是引用传递

~~~go
func main() {
    // 声明结构体指针
    var peo *People
    // 给结构体指针赋值
    peo = &People{"ssss", 17}
    // 等同于
    // peo = &People
    // peo.Age = 17
    // peo.Name = "ssss"
    // 上面代码使用短变量方式如下
    // peo:= &People{"ssss", 17}
    fmt.Println(peo)
}
~~~





## interface

### 1、基本概念

接口把所有的具有共性的方法定义在一起，任何其他类型只要实现了这些方法就是实现了这个接口

接口又称为动态数据类型，在进行接口使用的时，会将接口的动态类型改为所指向的类型，一般会将动态值改成所指向类型的结构体



### 2、实现接口

有两种方式：值接收、指针接收

- 值接收：没有办法修改接受者本身的属性，类似于交换两个值时的值传递
- 指针接收：必须传地址，可以修改指向对象的属性

~~~go
// 创建结构体
type Stu struct{}

// 创建接口
type People interface {
    Show()
}

// 结构体实现接口函数
func (St *Stu) Show() {
    fmt.Println("aaaaa")
}

func doShow(peo People) {
    peo.Show()
}
  
func main() {
    // 传递结构体指针
    var p People = &Stu{}  
 	// 调用，实现多态
    doShow(p) // aaaaa 
}
~~~

~~~go
type MyHandler struct{}

func (h *MyHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "正在通过处理器处理你的请求")
}

func main() {
    myHandler := MyHandler{}
    //调用处理器
    http.Handle("/", &myHandler)
    http.ListenAndServe(":8080", nil)
}
~~~



### 3、空接口

没有任何方法的接口就是空接口，实际上每个类型都实现了空接口，因此以空接口为形参的函数可以接受任何类型的数据

~~~go
// 定义一个空接口
type phone interface{}

// 空接口作为参数，可以传进来任意类型参数判断其类型与打印其值
func showmpType(q interface{}) {
	fmt.Printf("type:%T,value:%v\n", q, q)
}
~~~



### 4、类型转换

- 方法一使用 接口.(类型) 判断，此方法有两个返回值：

  - 如果断言成功第一个返回值为该变量对应的数据，否则返回该类型的空值，第二个返回值是一个布尔值

  - 如果断言成功则返回的是第二个返回值是 true，否则返回 false

~~~go
func judgeType(q interface{}) {
	temp, ok := q.(string)
	if ok {
		fmt.Println("类型转换成功!", temp)
	} else {
		fmt.Println("类型转换失败!", temp)
	}
}
~~~

- 方法二使用 switch...case... 语句，如果断言成功则到指定分支

~~~go
func judgeType(q interface{}) {
	switch i := q.(type) {
	case string:
		fmt.Println("这是一个字符串!", i)
	case int:
		fmt.Println("这是一个整数!", i)
	case bool:
		fmt.Println("这是一个布尔类型!", i)
	default:
		fmt.Println("未知类型", i)
	}
}
~~~



### 5、接口嵌套

接口可以进行嵌套实现，通过大接口包含小接口

~~~go
type interface People {
    ActionOne
    ActionTwo
}

type interface ActionOne {
    eat()
}

type interface ActionTwo {
    say()
}
~~~





## channel

### 1、基本概念

channel是一个数据类型，主要用来解决协程的同步问题以及协程之间数据共享（数据传递）的问题

引⽤类型 channel可用于多个 goroutine 通讯，其内部实现了同步，确保并发安全

和其它的引用类型一样，channel的零值也是nil



>goroutine运行在相同的地址空间，因此访问共享内存必须做好同步
>
>goroutine奉行通过**通信来共享内存**，而不是共享内存来通信



### 2、基本使用

#### 1、创建

channel 是一个由 make 函数创建的底层数据结构的**引用**

定义一个 channel 时，也需要定义发送到 channel 的值的类型

- **chan **是创建 channel 所需使用的关键字
- **Type **代表指定 channel 收发数据的类型
- **capacity** 代表管道是否具有缓冲，以及缓冲大小
  - 当参数 capacity = 0 时，channel 是无缓冲阻塞读写的，此种一般不需要显示 lock
  - 当 capacity > 0 时，channel 有缓冲、是非阻塞的，直到写满 capacity 个元素才阻塞写入

~~~go
make(chan Type) // 等价于make(chan Type, 0)
make(chan Type, capacity)
~~~



#### 2、取值

使用 **<-** 操作符来接收和发送数据

~~~go
channel <- value   // 发送value到channel
<- channel         // 取出channel里的一个值并丢弃
x := <- channel     // 从channel中接收数据，并赋值给x
x, ok := <- channel // 功能同上，同时检查通道是否已关闭或者是否为空

// 使用前必须显示 close 关闭通道
for num := range ch {
    fmt.Println("num = ", num)
}
~~~



#### 3、关闭

当发送者知道没有更多消息需要发送时，为了让接收者及时知道，可以调用close函数关闭管道，关闭的管道仍旧可以取处数据

~~~go
close(chan)
~~~

**注意**：

- channel 不像文件一样需要经常去关闭，只有确实无任何数据发送了，或者想显式结束 range 循环之类的，才去关闭
- 关闭 channel 后，无法向 channel 再发送数据（引发 panic 错误后，立即接收 channel 类型的零值）
- 关闭 channel 后，可以继续从 channel 接收数据
- 对于 nil channel，无论收发都会被阻塞



### 3、无缓冲管道

指在接收前没有能力保存任何值的通道

**要求**：发送方和接收方同时准备好，才能完成发送和接收操作（否则会导致通道先执行发送或接收操作的阻塞等待）

**阻塞**：由于某种原因数据没有到达，当前协程持续处于等待状态，直到条件满足，才解除阻塞

~~~go
func main() {
	c := make(chan int)

	go func() {
		for i := 0; i < 3; i++ {
			c <- i
			fmt.Printf("子协程[0]正在运行: len(c) = %d, cap(c) = %d, 放入的值 = %d\n", len(c), cap(c), i)
			time.Sleep(2 * time.Second)
		}
	}()

	go func() {
		for {
			fmt.Println("子协程[1]正在运行", time.Now())
			i := <-c
			fmt.Printf("子协程[1]正在运行: len(c) = %d, cap(c) = %d, 获取到的值 = %d\n", len(c), cap(c), i)
		}
	}()

	time.Sleep(500 * time.Second)

}

子协程[1]正在运行 2022-07-17 17:31:21.2434537 +0800 CST m=+0.005775301
子协程[1]正在运行: len(c) = 0, cap(c) = 0, 获取到的值 = 0
子协程[1]正在运行 2022-07-17 17:31:21.2769445 +0800 CST m=+0.039266101
子协程[0]正在运行: len(c) = 0, cap(c) = 0, 放入的值 = 0
子协程[0]正在运行: len(c) = 0, cap(c) = 0, 放入的值 = 1
子协程[1]正在运行: len(c) = 0, cap(c) = 0, 获取到的值 = 1
子协程[1]正在运行 2022-07-17 17:31:23.2824331 +0800 CST m=+2.044754701
子协程[0]正在运行: len(c) = 0, cap(c) = 0, 放入的值 = 2
子协程[1]正在运行: len(c) = 0, cap(c) = 0, 获取到的值 = 2
子协程[1]正在运行 2022-07-17 17:31:25.2865296 +0800 CST m=+4.048851201
~~~



### 4、有缓冲管道

一种在被接收前能存储一个或者多个数据值的通道

不强制要求双方必须同时完成准备

通道的发送动作和接收动作的阻塞条件也不同

- 只有通道中没有可以接收的值时，接收动作才会阻塞
- 只有通道没有可用缓冲区容纳被发送的值时，发送动作才会阻塞

~~~go
go func() {
    for i := 0; i < 30; i++ {
        c <- i
        fmt.Printf("子协程[0]正在运行: len(c) = %d, cap(c) = %d, 放入的值 = %d\n", len(c), cap(c), i)
        // time.Sleep(1 * time.Second)
    }
}()

go func() {
    time.Sleep(5 * time.Second)
    for {
        fmt.Println("子协程[1]正在运行", time.Now())
        i := <-c
        fmt.Printf("子协程[1]正在运行: len(c) = %d, cap(c) = %d, 获取到的值 = %d\n", len(c), cap(c), i)
    }
}()

time.Sleep(500 * time.Second)
~~~



### 5、单向管道

将通道作为参数在多个任务函数间传递，在不同的任务函数中使用通道需要对其进行限制，比如限制通道在函数中只能发送或只能接收

- 参数 **chan<- int** 是一个只能发送的通道，可以发送但是不能接收
- 参数 **<-chan int** 是一个只能接收的通道，可以接收但是不能发送

**注意**：

- 在函数传参及任何赋值操作中将双向通道转换为单向通道是可以的，但反过来是不可以的

~~~go
package main

import (
	"fmt"
)

func counter(in chan<- int) {
	for i := 0; i < 100; i++ {
		in <- i
	}
	close(in)
}

func squarer(in chan<- int, out <-chan int) {
	for i := range out {
		in <- i * i
	}
	close(in)
}

func printer(in <-chan int) {
	for i := range in {
		fmt.Println(i)
	}
}

func main() {
	ch1 := make(chan int)
	ch2 := make(chan int)
	go counter(ch1)
	go squarer(ch2, ch1)
	printer(ch2)
}
~~~



## goroutine

### 1、基本概念

一个线程中可以有任意多个协程，但某一时刻只能有一个协程在运行，**多个协程分享该线程分配到的计算机资源**

Go标准库提供的所有系统调用操作（包括所有同步IO操作），都会出让CPU给其他goroutine，这让轻量级线程的切换管理不依赖于系统的线程和进程，也不需要依赖于CPU的核心数量

Go语言为并发编程而内置的上层API基于顺序通信进程模型CSP(communicating sequential processes)



### 2、基础使用

只需在函数调⽤语句前添加 **go** 关键字，就可创建并发执⾏单元，调度器会自动将其安排到合适的系统线程上执行

~~~go
func newTask() {
    i := 0
    for {
        i++
        fmt.Printf("new goroutine: i = %d\n", i)
        time.Sleep(time.Second) // 延时1秒
    }
}

func main() {

    // 创建一个goroutine，启动另外一个任务
    go newTask()

    // 循环打印
    for i := 0; i < 5; i++ {
        fmt.Printf("main goroutine: i = %d\n", i)
        time.Sleep(time.Second) // 延时1秒
        i++
    }
}
~~~



**注意**：

- 主goroutine退出后，其它的工作的子goroutine会自动退出

~~~go
func newTask() {
	i := 0
	for {
		i++
		fmt.Printf("new goroutine: i = %d\n", i)
		time.Sleep(time.Second) //延时1秒
	}
}

func main() {

	//创建一个goroutine，启动另外一个任务
	go newTask()
	time.Sleep(3 * time.Second)
	fmt.Println("main ok")

}

new goroutine: i = 1
new goroutine: i = 1
new goroutine: i = 2
new goroutine: i = 3
main ok

~~~





### 3、runtime包

#### 1、Gosched

用于让出CPU时间片，让出当前goroutine的执行权限，调度器安排其他等待的任务运行，并在下次再获得CPU时间片的时候，从让出CPU的时间位置恢复执行

~~~go
func main() {

	// 创建一个goroutine
	go func(s string) {
		for i := 0; i < 2; i++ {
			fmt.Println(s)
		}
	}("A go")

	for i := 0; i < 2; i++ {
		//runtime.Gosched()
		fmt.Println("main")
	}
	time.Sleep(200 * time.Second)
}

// 关闭注释
main
main
A go
A go

// 打开注释
A go
A go
main
main
~~~



#### 2、Goexit

将立即终止当前 goroutine 执⾏，调度器确保所有已注册 defer 延迟调用被执行

~~~go
func main() {
	go func() {
		defer fmt.Println("A.defer")
		func() {
			defer fmt.Println("B.defer")
            // 立即终止
			runtime.Goexit()
            // 不会执行
			fmt.Println("B") 
		}()
		fmt.Println("A") // 不会执行
	}()
	
	for {}
}
~~~



#### 3、GOMAXPROCS

用来设置可以并行计算的CPU核数的最大值，并返回之前的值，默认是跑满整个CPU

~~~go
func main() {

	// n := runtime.GOMAXPROCS(1)
	// 打印结果: 111111111111111111111111110000000000000000000000000....

	// n := runtime.GOMAXPROCS(2)
	// 打印结果: 1111111111111111111111110000000000000011111110000100000000111100001111
    
	fmt.Println(n)
	go func() {
		for {
			fmt.Print(0)
		}
	}()

	go func() {
		for {
			fmt.Print(1)
		}
	}()
    
    for{}
}
~~~



## sync

### 1、基本概念

提供了常见的并发编程同步原语，包括常见的互斥锁 Mutex 与读写互斥锁 RWMutex 以及 Once、WaitGroup



### 2、Mutex

它由两个字段 **state** 和 **sema** 组成，**state** 表示当前互斥锁的状态，而 **sema** 真正用于控制锁状态的信号量，这两个加起来只占8个字节空间的结构体就表示了 Go 语言中的互斥锁

```go
type Mutex struct {
    state int32
    sema  uint32
}
```







# 扩展



**注意，panic无法跨协程传递，主线程的recover无法恢复子协程的panic**



## 1、切片底层

首先 Go **数组是值类型**，赋值和函数传参操作都会复制整个数组数据

~~~go
func main() {
    // 初始化数组
    arrayA := [2]int{100, 200}
    var arrayB [2]int

    arrayB = arrayA

    fmt.Printf("arrayA : %p , %v\n", &arrayA, arrayA)
    fmt.Printf("arrayB : %p , %v\n", &arrayB, arrayB)

    testArray(arrayA)
}

func testArray(x [2]int) {
    fmt.Printf("func Array : %p , %v\n", &x, x)
}

arrayA : 0xc4200bebf0 , [100 200]
arrayB : 0xc4200bec00 , [100 200]
func Array : 0xc4200bec30 , [100 200]
~~~

三个内存地址都不同，这也就验证了 Go 中数组赋值和函数传参都是值复制的，这就带来一个问题，如果数组非常大，每次传参需要复制大量数据，内存占用很高，这就需要使用数组的指针进行传参，但是不是任何时候都适用，因为切片的底层数组有可能在堆上分配内存，同时小数组在栈上拷贝的消耗未必比 make 函数大

~~~go
func main() {
    arrayA := [2]int{100, 200}
    testArrayPoint(&arrayA)   // 1.传数组指针
}

func testArrayPoint(x *[2]int) {
    fmt.Printf("func Array : %p , %v\n", x, *x)
    (*x)[1] += 100
}

func Array : 0xc4200b0140 , [100 200]
arrayA : 0xc4200b0140 , [100 300]
~~~

切片是对数组一个连续片段的引用，所以切片是一个引用类型（更类似于 Python 中的 list 类型），这个片段可以是整个数组，或者是由起始和终止索引标识的一些项的子集，需要注意的是，终止索引标识的项不包括在切片内

~~~go
// 切片结构
type slice struct {
    array unsafe.Pointer
    len   int
    cap   int
}
// Pointer 是指向一个数组的指针
// len 代表当前切片的长度
// cap 是当前切片的容量，cap 总是大于等于 len 的
~~~

<img src="images/image-20230211114417279.png" alt="image-20230211114417279" style="zoom:67%;" />

从 slice 获取一块内存地址的指针（没啥用）

~~~go
s := make([]byte, 200)
ptr := unsafe.Pointer(&s[0])
~~~

从 Go 的内存地址中构造一个 slice（不太好用且没啥用）

~~~go
var ptr unsafe.Pointer
// 构造出一个 slice 结构体
var s1 = struct {
    addr uintptr
    len int
    cap int
}{ptr, length, length}
// 使用 unsafe.Pointer() 函数将 s1 转为 unsafe.Pointer 类型
// 再使用 (*[]byte) 对 unsafe.Pointer 类型进行转换
// 此时拿到的是一个 slice 指针，再前面加一个 * 获取 slice
// 到此就获取到了一个 nil 的 slice
s := *(*[]byte)(unsafe.Pointer(&s1))
~~~

```go
// 该函数可以将任意指针转换为 unsafe.Pointer 类型
unsafe.Pointer()
// 可以使用 (*T)ptr 再转换回去
a := 10
b := &a // b 为 *int
ub := unsafe.Pointer(b)
bak_b := (*int)ub // 转换回去
```

使用 Go 反射构造一个 slice（不太好用且没啥用）

~~~go
var o []byte
sliceHeader := (*reflect.SliceHeader)(unsafe.Pointer(&o))
sliceHeader.Cap = length
sliceHeader.Len = length
sliceHeader.Data = uintptr(ptr)
~~~



**注意**：

- 和数组不同的是，切片的长度可以在运行时修改













# 问题

## 1、环境变量问题

在VsCode中GOPATH和环境变量的设置不一致

首先我在用户变量中发现了Go预设的路径/users/xxx/go，将其删除，重启电脑，无效

再尝试，给VsCode的SettingJson文件设置GOPATH，重启，无效

再尝试，再VsCode中新建终端，临时修改GOPATH有效

总结，保留了环境变量中的GOPATH，删除用户变量中的GOPATH，因为直接用cmd输出GOPATH是正确的，而在VsCode中是错误的，然后在VsCode中新建终端，临时修改GOPATH



## 2、channel死锁问题

~~~bash
fatal error: all goroutines are asleep - deadlock!
~~~

**原因**：

- 期待协程接收数据（过度生产）且一直没有协程消费，触发 panic，形成死锁
  - 例如缓冲区100，过度生产200后才能消费，触发 panic

- 期待协程发送数据（过度消费）且一直没有协程生成，触发 panic，形成死锁
  - 例如缓冲区100，边生产200个边消费，但是总消费500，触发 panic

- 在未初始化的信道上发送或者接受数据

~~~go
func main() {
	ch := make(chan int)
	ch <- 1 // main 阻塞，因为没有协程准备接收
	fmt.Println("send")
	go func() {
		<-ch // 此协程永远不会被唤醒，因为执行不到这里
		fmt.Println("received")
	}()
    // ch <- 1 该语句放到这里就没问题了
	fmt.Println("over")
}
~~~



~~~go
	fmt.Println("main start")

	// 管道只有10空间，主线程存入100，过度生产报错
	// chanel_1 := make(chan int, 10)
	// 正确写法
	chanel_1 := make(chan int, 100)

	// 放到两个go程下面也可以避免死锁
	for i := 0; i < 100; i++ {
		chanel_1 <- i
	}

	go func() {
		fmt.Println("go1 start")

		// 只有100个数据，读取500次，过度消费产生报错
		//for i := 0; i < 500; i++ {
		//	fmt.Println("go1 get ", <-chanel_1)
		//}

		for i := 0; i < 5; i++ {
			fmt.Println("go1 get ", <-chanel_1)
		}
		fmt.Println("go1 end")
	}()

	go func() {
		fmt.Println("go2 start")
		for i := 0; i < 5; i++ {
			fmt.Println("go2 get ", <-chanel_1)
		}
		fmt.Println("go2 end")
	}()

	time.Sleep(10)

	fmt.Println("main end")
~~~

<img src="images/image-20220704211705519.png" alt="image-20220704211705519" style="zoom:67%;" />



## 3、Go 编译问题

Go 的编译使用命令 go build、go install 除非仅写一个 main 函数，否则还是准备好目录结构，GOPATH=工程根目录，其下应创建 src、pkg、bin 目录

- bin 目录中用于生成可执行文件
- pkg 目录中用于生成 .a 文件，Go 中的 import name，实际是到 GOPATH 中去寻找 name.a，使用时是该 name.a 的源码中声明的 package 名字

注意：

- 系统编译时 go install abc_name 时，系统会到 GOPATH 的 src 目录中寻找 abc_name 目录，然后编译其下的 go 文件

- 同一个目录中所有的 go 文件的 package 声明必须相同，所以 main 方法要单独放一个文件，否则在 eclipse 和 liteide 中都会报错

  - 编译报错如下：（假设 test 目录中有个 main.go 和 mymath.go 其中 main.go 声明 package 为 main，mymath.go 声明 packag 为 test);

  - >go install test
    >
    >can't load package: package test: found packages main (main.go) and test (mymath.go) in /home/wanjm/go/src/test
    >
    >报错说不能加载 package test（这是命令行的参数）因为发现了两个 package，分别时 main.go 和 mymath.go;

- 对于 main 方法，只能在 bin 目录下运行 go build path_tomain.go; 可以用 -o 参数指出输出文件名

- 可以添加参数 go build -gcflags "-N -l"，可以更好的便于 gdb 详细参见 http://golang.org/doc/gdb

- gdb 全局变量注意点，如有全局变量 a，则应写为 p 'main.a'；注意但引号不可少；  