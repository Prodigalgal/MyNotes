# Go基础

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



### 2、实现接口

~~~go
type Stu struct{}

type People interface {
    Show()
}

func (St *Stu) Show() {
    fmt.Println("aaaaa")
}
  
func main() {
    var p People = &Stu{}  // 传递结构体指针
    p.Show()  // aaaaa
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

channel是一个由make函数创建的底层数据结构的**引用**

定义一个channel时，也需要定义发送到channel的值的类型

- **chan**是创建channel所需使用的关键字
- **Type**代表指定channel收发数据的类型
- **capacity**代表管道是否具有缓冲，以及缓冲大小
  - 当参数 capacity = 0 时，channel 是无缓冲阻塞读写的，此种一般不需要显示lock
  - 当capacity > 0 时，channel 有缓冲、是非阻塞的，直到写满 capacity个元素才阻塞写入

~~~go
make(chan Type) //等价于make(chan Type, 0)
make(chan Type, capacity)
~~~



#### 2、取值

使用 **<-** 操作符来接收和发送数据

~~~go
channel <- value   // 发送value到channel
<- channel         // 取出channel里的一个值并丢弃
x := <-channel     // 从channel中接收数据，并赋值给x
x, ok := <-channel // 功能同上，同时检查通道是否已关闭或者是否为空

// 使用前必须close的关闭通道
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

- channel不像文件一样需要经常去关闭，只有确实无任何数据发送了，或者想显式结束range循环之类的，才去关闭
- 关闭channel后，无法向channel再发送数据(引发 panic 错误后，立即接收channel类型的零值)
- 关闭channel后，可以继续从channel接收数据
- 对于nil channel，无论收发都会被阻塞



### 3、无缓冲管道

指在接收前没有能力保存任何值的通道

**要求**：发送方和接收方同时准备好，才能完成发送和接收操作（否则通道会导致先执行发送或接收操作的阻塞等待）

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
        //time.Sleep(1 * time.Second)
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

- 参数 **chan<- int** 是一个只能发送的通道，可以发送但是不能接收。
- 参数 **<-chan int** 是一个只能接收的通道，可以接收但是不能发送。

**注意**：

- 在函数传参及任何赋值操作中将双向通道转换为单向通道是可以的，但反过来是不可以的。

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

- 期待协程接收数据（过度生产），触发panic，形成死锁
- 期待协程发送数据（过度消费），触发panic，形成死锁
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