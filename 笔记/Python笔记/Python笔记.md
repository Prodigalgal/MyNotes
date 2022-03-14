# 1、Python基础

## 1.1、对象

Python是一门强类型的语言，对象一旦创建类型便不能修改。

类型转换不是改变对象本身的类型，而是根据当前对象的值创建一个新对象。

如果有其他变量也指向了该对象，则修改也会在其他的变量中体现，

~~~text
- 对象是内存中专门用来存储数据的一块区域。
- 对象中可以存放各种数据（比如：数字、布尔值、代码）
- 对象由三部分组成：
  1.对象的标识（id）
  2.对象的类型（type）
  3.对象的值（value）
~~~

![图3](images/图3.png)

### 1.1.1、类(class) 

#### 1.1.1.1、类的概念

类就是对象的图纸，也称对象是类的实例（instance）。

类就是一个用来创建对象的对象，类是type类型的对象，定义类实际上就是定义了一个type类型的对象

> 像 int() float() bool() str() list() dict() .... 这些都是类。

我们自定义的类都需要使用大写字母开头，使用大驼峰命名法（帕斯卡命名法）来对类命名。

#### 1.1.1.2、类的基本结构

~~~python
class 类名([父类]) :

    公共的属性... 

    # 对象的初始化方法
    def __init__(self,...):
        ...

    # 其他的方法    
    def method_1(self,...):
        ...

    def method_2(self,...):
        ...

    ...  
~~~

#### 1.1.1.3、类的定义

在类的代码块中，我们可以定义变量和函数：

- 变量会成为该类实例的公共属性，所有的该类实例都可以通过 对象**.**属性名 的形式访问，但是修改只能由类对象去修改。
- 函数会成为该类实例的公共方法，所有该类实例都可以通过 对象**.**方法名() 的形式调用方法。

注意：方法调用时，第一个参数由解析器自动传递，所以定义方法时，至少要定义一个形参！ 

~~~python
class Person :
    name = 'swk'
    
    def say_hello(self) :
        # 方法每次被调用时，解析器都会自动传递第一个实参
        # 第一个参数，就是调用方法的对象本身，
        # 在方法中不能直接访问类中的属性
        print('你好！我是 %s' %self.name)
~~~

![image-20220314154303513](images/image-20220314154303513.png)



1、实例为什么能访问到类中的属性和方法：

- 类中定义的属性和方法都是公共的，任何该类实例都可以访问

2、属性和方法查找的流程：

- 当我们调用一个对象的属性时，解析器会先在当前对象中寻找是否含有该属性，如果有，则直接返回当前对象的属性值
- 如果没有，则去当前对象的类中去寻找，如果有则返回类的属性值
- 如果类中依然没有，则报错

3、类对象和实例对象中都可以保存属性（方法）：

- 如果这个属性（方法）是所有的实例共享的，则应该将其保存到类中
- 如果这个属性（方法）是某个实例独有，则应该保存到实例对象中 
- 一般情况下，属性保存到实例对象中，而方法需要保存到类中 



#### 1.1.1.4、使用类创建对象流程

~~~python
class Person():
    pass


p1 = Person()的运行流程
    1.创建一个变量
    2.在内存中创建一个新对象
    3.__init__(self)方法执行
    4.将对象的id赋值给变量

# isinstance()用来检查一个对象是否是一个类的实例
result = isinstance(p1, Person)

# 通过Person这个类创建的对象都是一个空对象
# 也就是对象中实际上什么都没有
# 可以向对象中添加变量，对象中的变量称为属性
# 语法：对象.属性名 = 属性值
p1.name = '孙悟空'
~~~

![图1](images/图1.png)

#### 1.1.1.5、总结

~~~python
# 定义一个类
class A(object):

    # 类属性
    # 实例属性
    # 类方法
    # 实例方法
    # 静态方法

    # 类属性，直接在类中定义的属性是类属性
    # 类属性可以通过类或类的实例访问到
    # 但是类属性只能通过类对象来修改，无法通过实例对象修改
    count = 0

    def __init__(self):
        # 实例属性，通过实例对象添加的属性属于实例属性
        # 实例属性只能通过实例对象来访问和修改，类对象无法访问修改
        self.name = '孙悟空'

    # 实例方法
    #   在类中定义，以self为第一个参数的方法都是实例方法
    #   实例方法在调用时，Python会将调用对象作为self传入  
    #   实例方法可以通过实例和类去调用
    #       当通过实例调用时，会自动将当前调用对象作为self传入
    #       当通过类调用时，不会自动传递self，此时我们必须手动传递self
    def test(self):
        print('这是test方法~~~ ' , self)    

    # 类方法    
    # 在类内部使用 @classmethod 来修饰的方法属于类方法
    # 类方法的第一个参数是cls，也会被自动传递，cls就是当前的类对象
    #   类方法和实例方法的区别，实例方法的第一个参数是self，而类方法的第一个参数是cls
    #   类方法可以通过类去调用，也可以通过实例调用，没有区别
    @classmethod
    def test_2(cls):
        print('这是test_2方法，他是一个类方法~~~ ',cls)
        print(cls.count)

    # 静态方法
    # 在类中使用 @staticmethod 来修饰的方法属于静态方法  
    # 静态方法不需要指定任何的默认参数，静态方法可以通过类和实例去调用  
    # 静态方法，基本上是一个和当前类无关的方法，它只是一个保存到当前类中的函数
    # 静态方法一般都是一些工具方法，和当前类无关
    @staticmethod
    def test_3():
        print('test_3执行了~~~')
        
    # del是一个特殊方法，它会在对象被垃圾回收前调用
    def __del__(self):
        print('A()对象被删除了~~~',self)


a = A()
# 实例属性，通过实例对象添加的属性属于实例属性
# a.count = 10
# A.count = 100
# print('A ,',A.count) 
# print('a ,',a.count) 
# print('A ,',A.name) 
# print('a ,',a.name)  

a = A()
print(a.x, id(a.x))
a.x = 20
print(a.x, id(a.x))

10 1912166875664
20 1912166875984

# a.test() 等价于 A.test(a)

# A.test_2() 等价于 a.test_2()

A.test_3()
a.test_3()



~~~



### 1.1.2、对象初始化

在类中可以定义一些特殊方法（魔术方法），特殊方法都是以 **\__** 开头，**\_\_ **结尾的方法

**注意**：特殊方法不需要我们自己调用，也不要尝试去调用特殊方法。特殊方法将会在特殊的时刻自动调用。

~~~python
class Person :
    # init会在对象创建以后立刻执行
    # init可以用来向新创建的对象中初始化属性
    # 调用类创建对象时，类后边的所有参数都会依次传递到init()中
    def __init__(self,name):
        # 通过self向新建的对象中初始化属性
        self.name = name

    def say_hello(self):
        print('大家好，我是%s'%self.name)
        
p1 = Person('孙悟空')
~~~

### 1.1.3、封装

#### 1.1.3.1、封装基本概念

封装指的是隐藏对象中一些不希望被外部所访问到的属性或方法。

使用封装，会增加了类的定义的复杂程度，但是它也确保了数据的安全性

1、如何隐藏一个对象中的属性？

- 将对象的属性名，修改为一个外部不知道的名字

2、如何获取（修改）对象中的属性？

- 需要提供一个getter和setter方法使外部可以访问到属性
- getter 获取对象中的指定属性（get_属性名）
  - 可以在读取属性的同时做一些其他的处理
- setter 用来设置对象的指定属性（set_属性名）
  - 使用setter方法设置属性，可以增加数据的验证，确保数据的值是正确的

3、优点：

- 隐藏了属性名，使调用者无法随意的修改对象中的属性
- 增加了getter和setter方法，很好的控制的属性是否是只读的
  - 如果希望属性是只读的，则可以直接去掉setter方法
  - 如果希望属性不能被外部访问，则可以直接去掉getter方法

~~~python
class Dog:
    '''
        表示狗的类
    '''
    def __init__(self , name , age):
        self.hidden_name = name
        self.hidden_age = age

    def say_hello(self):
        print('大家好，我是 %s' %self.hidden_name) 

    def get_name(self):
        '''
            get_name()用来获取对象的name属性
        '''    
        return self.hidden_name

    def set_name(self , name):
        self.hidden_name = name

    def get_age(self):
        return self.hidden_age

    def set_age(self , age):
        if age > 0 :
            self.hidden_age = age    


d = Dog('旺财',8)
d.say_hello()

# 调用setter来修改name属性 
d.set_name('小黑')
d.set_age(-10)

d.say_hello()
print(d.get_age())
~~~

#### 1.1.3.2、隐藏属性

可以为对象的属性使用双下划线开头进行隐藏，__xxx

双下划线开头的属性，是对象的隐藏属性，隐藏属性只能在类的内部访问，无法通过对象访问。

其实隐藏属性只不过是Python自动为属性改了一个名字，实际上是将名字修改为了，_\_类名\_\_属性名 比如 \_\_name -> \_\_Person__name



但是，使用__开头的属性，实际上依然可以在外部访问，所以这种方式我们一般不用

一般情况下，使用_开头的属性都是私有属性，没有特殊需要不要修改私有属性

~~~python
 class Person:
    def __init__(self,name):
        self.__name = name

    def get_name(self):
        return self.__name

    def set_name(self , name):
        self.__name = name        

p = Person('孙悟空')

# print(p.__name) __开头的属性是隐藏属性，无法通过对象访问
# p.__name = '猪八戒' # 无效赋值
# print(p._Person__name)
# p._Person__name = '猪八戒' # 有效赋值

print(p.get_name())

class Person:
    def __init__(self,name):
        self._name = name

    def get_name(self):
        return self._name

    def set_name(self , name):
        self._name = name   

p = Person('孙悟空')

print(p._name)
~~~

#### 1.1.3.3、property装饰器

property装饰器，用来将一个get方法，转换为对象的属性

添加为property装饰器以后，我们就可以像调用属性一样使用get方法

使用property装饰的方法，必须和属性名是一样的

~~~python
class Person:
    def __init__(self,name,age):
        self._name = name
        self._age = age

    @property    
    def name(self):
        return self._name

    # setter方法的装饰器：@属性名.setter
    @name.setter    
    def name(self , name):
        self._name = name        

    @property
    def age(self):
        return self._age

    @age.setter    
    def age(self , age):
        self._age = age   

p = Person('猪八戒',18)

p.name = '孙悟空'
p.age = 28

print(p.name,p.age)
~~~

### 1.1.4、继承

#### 1.1.4.1、基本概念

子类从父类中来继承它的属性和方法。

在创建类时，如果省略了父类，则默认父类为object。

父类中的所有方法都会被子类继承，包括特殊方法，也可以重写特殊方法。



~~~python
class Animal:
    def __init__(self,name):
        self._name = name
        
    def run(self):
        print('动物会跑~~~')

    def sleep(self):
        print('动物睡觉~~~')
                   
class Dog(Animal):
    def __init__(self,name,age):
        # super() 可以用来获取当前类的父类，
        # 并且通过super()返回对象调用父类方法时，不需要传递self
        super().__init__(name)
        self._age = age
    
    def bark(self):
        print('汪汪汪~~~') 

    def run(self):
        print('狗跑~~~~')    

class Hashiqi(Dog):
    def fan_sha(self):
        print('我是一只傻傻的哈士奇')  
        
# issubclass() 检查一个类是否是另一个类的子类
# print(issubclass(Animal , Dog))
~~~

#### 1.1.4.2、重写

当我们调用一个对象的方法时，会优先去当前对象中寻找是否具有该方法，如果有则直接调用

如果没有，则去当前对象的父类中寻找，如果父类中有则直接调用父类中的方法

如果没有，则去父类的父类中寻找，以此类推，直到找到object，如果依然没有找到，则报错

~~~python
 class A(object):
    def test(self):
        print('AAA')

class B(A):
    def test(self):
        print('BBB')

class C(B):
    def test(self):
        print('CCC')   

c = C()
c.test()
~~~

#### 1.1.4.3、多重继承

在Python中是支持多重继承的，也就是我们可以为一个类同时指定多个父类。

可以在类名的()后边添加多个类，来实现多重继承，此会使子类同时拥有多个父类，并且会获取到所有父类中的方法。

1、对于父类中的同名方法：

- 如果多个父类中有同名的方法，则会现在第一个父类中寻找，然后找第二个，然后找第三个。。。
- 前边父类的方法会覆盖后边父类的方法



~~~python
class A(object):
    def test(self):
        print('AAA')

class B(object):
    def test(self):
        print('B中的test()方法~~')

    def test2(self):
        print('BBB') 
        

class C(A,B):
    pass

# 类名.__bases__ 这个属性可以用来获取当前类的所有父类    
# print(B.__bases__) (<class 'object'>,)
# print(C.__bases__) # (<class '__main__.A'>, <class '__main__.B'>)

c = C()
c.test()
~~~

#### 1.1.4.4、多态

~~~python
# 定义两个类
class A:
    def __init__(self,name):
        self._name = name

    @property
    def name(self):
        return self._name
        
    @name.setter
    def name(self,name):
        self._name = name   

class B:
    def __init__(self,name):
        self._name = name

    def __len__(self):
        return 10

    @property
    def name(self):
        return self._name
        
    @name.setter
    def name(self,name):
        self._name = name   

class C:
    pass


a = A('孙悟空')
b = B('猪八戒')
c = C()

# 定义一个函数
# 对于say_hello()这个函数来说，只要对象中含有name属性，它就可以作为参数传递
# 这个函数并不会考虑对象的类型，只要有name属性即可
def say_hello(obj):
    print('你好 %s'%obj.name)

# 在say_hello_2中做一个类型检查，也就是只有obj是A类型的对象时，才可以正常使用，
# 其他类型的对象都无法使用该函数，这个函数就违反了多态
# 违反了多态的函数，只适用于一种类型的对象，无法处理其他类型对象，这样导致函数的适应性非常的差
# 注意，向isinstance()这种函数，在开发中一般是不会使用的！
def say_hello_2(obj):
    # 做类型检查
    if isinstance(obj , A):
        print('你好 %s'%obj.name)    
        
say_hello(b)    
say_hello_2(b)

# len()
# 之所以一个对象能通过len()来获取长度，是因为对象中具有一个特殊方法__len__
# 换句话说，只要对象中具有__len__特殊方法，就可以通过len()来获取它的长度
print(len(b))
print(len(c))
~~~



### 1.1.5、特殊方法

特殊方法都是使用__开头和结尾的

~~~python
# 定义一个Person类
class Person(object):
    """人类"""
    def __init__(self, name , age):
        self.name = name
        self.age = age

    # __str__()这个特殊方法会在尝试将对象转换为字符串的时候调用
    # 它的作用可以用来指定对象转换为字符串的结果  （print函数）  
    def __str__(self):
        return 'Person [name=%s , age=%d]' %(self.name,self.age)        

    # __repr__()这个特殊方法会在对当前对象使用repr()函数时调用
    # 它的作用是指定对象在 ‘交互模式’中直接输出的效果    
    def __repr__(self):
        return 'Hello'        

    # object.__add__(self, other)
    # object.__sub__(self, other)
    # object.__mul__(self, other)
    # object.__matmul__(self, other)
    # object.__truediv__(self, other)
    # object.__floordiv__(self, other)
    # object.__mod__(self, other)
    # object.__divmod__(self, other)
    # object.__pow__(self, other[, modulo])
    # object.__lshift__(self, other)
    # object.__rshift__(self, other)
    # object.__and__(self, other)
    # object.__xor__(self, other)
    # object.__or__(self, other)

    # object.__lt__(self, other) 小于 <
    # object.__le__(self, other) 小于等于 <=
    # object.__eq__(self, other) 等于 ==
    # object.__ne__(self, other) 不等于 !=
    # object.__gt__(self, other) 大于 >
    # object.__ge__(self, other) 大于等于 >= 
    
    # __len__()获取对象的长度

    # object.__bool__(self)
    # 可以通过bool来指定对象转换为布尔值的情况
    def __bool__(self):
        return self.age > 17

    # __gt__会在对象做大于比较的时候调用，该方法的返回值将会作为比较的结果
    # 他需要两个参数，一个self表示当前对象，other表示和当前对象比较的对象
    # self > other
    def __gt__(self , other):
        return self.age > other.age


# 创建两个Person类的实例        
p1 = Person('孙悟空',18)
p2 = Person('猪八戒',28)

# 打印p1
# 当我们打印一个对象时，实际上打印的是对象的中特殊方法 __str__()的返回值
# print(p1) # <__main__.Person object at 0x04E95090>
# print(p2)

# print(repr(p1))

# print(p1 > p2)
# print(p2 > p1)

# print(bool(p1))

# if p1 :
#     print(p1.name,'已经成年了')
# else :
#     print(p1.name,'还未成年了')
~~~

### 1.1.6、模块（module）

#### 1.1.6.1、基本概念

模块化，模块化指将一个完整的程序分解为一个一个小的模块，通过将模块组合，来搭建出一个完整的程序。

- 不采用模块化，统一将所有的代码编写到一个文件中
- 采用模块化，将程序分别编写到多个文件中

在Python中一个py文件就是一个模块，要想创建模块，实际上就是创建一个python文件。

注意：模块名要符号标识符的规范

m.py

~~~python
# 可以在模块中定义变量，在模块中定义的变量，在引入模块后，就可以直接使用了
a = 10
b = 20

# 添加了_的变量，只能在模块内部访问，在通过import * 引入时，不会引入_开头的变量
_c = 30

# 可以在模块中定义函数，同样可以通过模块访问到
def test():
    print('test')

def test2():
    print('test2')

# 也可以定义类    
class Person:
    def __init__(self):
        self.name = '孙悟空'
        

# 编写测试代码，这部分代码，只要当当前文件作为主模块的时候才需要执行
# 而当模块被其他模块引入时，不需要执行时，此时我们就必须要检查当前模块是否是主模块  
if __name__ == '__main__':
    test()
    test2()
    p = Person()
    print(p.name)
~~~

引入模块

~~~python
import m

# 访问模块中的变量：模块名.变量名
print(m.a , m.b)
p = m.Person()
m.test2()
print(m.__name__)
print(__name__)
~~~

#### 1.1.6.2、引入外部模块

- import 模块名 （模块名，就是python文件的名字，注意不要py）
- import 模块名 as 模块别名

~~~python
from m as m1
~~~

- 也可以只引入模块中的部分内容

~~~python
from m import Person
from m import test
from m import Person,test
from m import * # 引入到模块中所有内容，一般不会使用
p1 = Person()
print(p1)
test()
test2()

~~~

- 也可以为引入的变量使用别名

~~~python
# 语法：from 模块名 import 变量 as 别名
from m import test2 as new_test2
~~~

**注意**：

- 可以引入同一个模块多次，但是模块的实例只会创建一个。
- import可以在程序的任意位置调用，但是一般情况下，import语句都会统一写在程序的开头
- 在每一个模块内部都有一个 \__name\_\_ 属性，通过这个属性可以获取到模块的名字
- \__name\_\_ 属性值为  \_\_main__ 的模块是主模块，一个程序中只会有一个主模块，主模块就是我们直接通过 python 执行的模块

#### 1.1.6.3、包（Package）

包也是一个模块，当模块中代码过多时，或者一个模块需要被分解为多个模块时，这时就需要使用到包。

普通的模块就是一个py文件，而包是一个**文件夹**

包中必须要一个一个 \__init__.py 这个文件，这个文件中**可以**包含有包中的主要内容

~~~python
from hello import a , b

print(a.c)
print(b.d)

# __pycache__ 是模块的缓存文件
# py代码在执行前，需要被解析器先转换为机器码，然后再执行
# 所以我们在使用模块（包）时，也需要将模块的代码先转换为机器码然后再交由计算机执行
# 而为了提高程序运行的性能，python会在编译过一次以后，将代码保存到一个缓存文件中
# 这样在下次加载这个模块（包）时，就可以不再重新编译而是直接加载缓存中编译好的代码即可
~~~

#### 1.1.6.4、标准库

为了实现开箱即用的思想，Python提供了一个模块的标准库，在这个标准库中，有很多很强大的模块可以直接使用，并且标准库会随Python的安装一同安装。

- **sys** 模块，它里面提供了一些变量和函数，可以获取到Python解析器的信息，或者通过函数来操作Python解析器。
- **pprint** 模块它给我们提供了一个方法 pprint() 该方法可以用来对打印的数据做简单的格式化
- **os** 模块让我们可以对操作系统进行访问

~~~python
import sys
import pprint

# 获取执行代码时，命令行中所包含的参数
# 该属性是一个列表，列表中保存了当前命令的所有参数
sys.argv
print(sys.argv)

# 获取当前程序中引入的所有模块
# modules是一个字典，字典的key是模块的名字，字典的value是模块对象
sys.modules
pprint.pprint(sys.modules)


# 他是一个列表，列表中保存的是模块的搜索路径
# ['C:\\Users\\lilichao\\Desktop\\resource\\course\\lesson_06\\code',
# 'C:\\dev\\python\\python36\\python36.zip',
# 'C:\\dev\\python\\python36\\DLLs',
# 'C:\\dev\\python\\python36\\lib',
# 'C:\\dev\\python\\python36',
# 'C:\\dev\\python\\python36\\lib\\site-packages']
sys.path
pprint.pprint(sys.path)


# 表示当前Python运行的平台
sys.platform
print(sys.platform)


# 函数用来退出程序
sys.exit()
sys.exit('程序出现异常，结束！')
print('hello')

import os


# 通过这个属性可以获取到系统的环境变量
os.environ
pprint.pprint(os.environ['path'])


# 可以用来执行操作系统的shell
os.system()
os.system('dir')
os.system('notepad')
~~~

### 比较对象

**== !=  is is not**

== != 比较的是对象的**值**是否相等 

is is not 比较的是对象的**id**是否相等（比较两个对象是否是同一个对象）

## 1.2、流程控制语句

### 1.2.1、if语句

if语句

```python
if 10 < num < 20:
    print('num比10大,num比20小！')
if False:
    print(123)
if True:
    print(123)
```

if-else语句

```python
if age > 17 :
    print('你已经成年了~~')
else :
    print('你还未成年~~')
```

### 1.2.2、循环语句

循环语句分成两种，while循环 和 for循环

while语句在执行时，会先对while后的条件表达式进行求值判断，如果判断结果为True，则执行循环体（代码块），循环体执行完毕，继续对条件表达式进行求值判断，以此类推，直到判断结果为False，则循环终止，如果循环有对应的else，则执行else后的代码块

```python
while 条件表达式 :
    代码块
else :
	代码块
```

### 1.2.3、中断语句

**break**
break可以用来立即退出循环语句（包括else）。

**continue**
continue可以用来跳过当次循环，break和continue都是只对离他最近的循环起作用。

**pass**
pass是用来在判断或循环语句中占位的。

```python
# i = 0
# while i < 5:
#     if i == 3:
#         break
#     print(i)
#     i += 1
# else :
#     print('循环结束')

# i = 0
# while i < 5:
#     i += 1
#     if i == 2:
#         continue
#     print(i)
# else :
#     print('循环结束')

i = 0
if i < 5:
    pass
```

## 1.3、序列

序列是Python中最基本的一种数据结构

序列用于保存一组**有序**的数据，所有的数据在序列当中都有一个唯一的位置（索引），并且序列中的数据会按照添加的顺序来分配索引。

可变序列（序列中的元素可以改变）：

- 列表（list）

不可变序列（序列中的元素不能改变）：

- 字符串（str）  
- 元组（tuple）

### 1.3.1、列表（list）

#### 1.3.1.1、基本概念

列表是Python中的一个**对象**。

对象（object）就是内存中专门用来存储数据的一块区域，列表中可以保存多个**有序**的数据，列表是用来存储对象的对象，列表中的对象都会按照插入的顺序存储到列表中。

列表的使用：
1.列表的创建
2.操作列表中的数据

```python
my_list = []  # 创建了一个空列表

my_list = [10] # 一个列表中可以存储多个元素，也可以在创建列表时，来指定列表中的元素

my_list = [10, 'hello', True, None, [1, 2, 3], print] # 列表中可以保存任意的对象

# 如果使用的索引超过了最大的范围，会抛出异常 print(my_list[5]) IndexError: list index out of range
# 获取列表的长度，列表中元素的个数，len()函数，通过该函数可以获取列表的长度，获取到的长度的值，是列表的最大索引 + 1
print(len(my_list))  # 5

# 列表的索引可以是负数
# 如果索引是负数，则从后向前获取元素，-1表示倒数第一个，-2表示倒数第二个 以此类推
my_list[-1]
```

#### 1.3.1.2、切片

切片指从现有列表中，获取一个子列表，通过切片来获取指定的元素，

**语法一**：列表[起始:结束] 

通过切片获取元素时，会**包括起始位置**的元素，**不会包括结束位置**的元素。

做切片操作时，总会返回一个新的列表，不会影响原来的列表。

- 起始和结束位置的索引都可以省略不写。
- 如果省略结束位置，则会一直截取到最后。
- 如果省略起始位置，则会从第一个元素开始截取。
- 如果起始位置和结束位置全部省略，则相当于创建了一个列表的副本。

```python
print(stus[1:])
print(stus[:3])
print(stus[:])
print(stus)
```

**语法二**：列表[起始:结束:步长] 

步长表示，每次获取元素的间隔，默认值是1

```python
print(stus[0:5:3])
```

步长不能是0，但是可以是负数。

```python
# print(stus[::0]) ValueError: slice step cannot be zero
```

如果是负数，则会从列表的后部向前边取元素

```python
print(stus[::-1])
```

通过切片来修改列表

在给切片进行赋值时，只能使用序列

```python
# 使用新的元素替换旧元素 0 1 被牛、红替代
stus[0:2] = ['牛魔王','红孩儿'] 

# 0 1 被 牛、红、二替代
stus[0:2] = ['牛魔王','红孩儿','二郎神'] 

#向索引为0的位置插入元素
stus[0:0] = ['牛魔王']

# 当设置了步长时，序列的长度必须比切片中元素的最大下标大
# 牛 --> 0 红 --> 2 二 --> 4
stus[::2] = ['牛魔王','红孩儿','二郎神']

#注意：以上操作，只适用于可变序列，但是可以通过 list() 函数将其他的序列转换为list
```

#### 1.3.1.3、方法

```python
append() 
# 向列表的最后添加一个元素
stus.append('唐僧')

insert()
# 向列表的指定位置插入一个元素
# 参数：
#   1.要插入的位置
#   2.要插入的元素
stus.insert(2,'唐僧')

extend()
# 使用新的序列来扩展当前序列
# 需要一个序列作为参数，它会将该序列中的元素添加到当前列表中
stus.extend(['唐僧','白骨精'])
stus += ['唐僧','白骨精']

clear()
# 清空序列
stus.clear()

pop()
# 根据索引删除并返回被删除的元素
stus.pop(0)
result = stus.pop(2) # 删除索引为2的元素
result = stus.pop() # 删除最后一个

remove()
# 删除指定值的元素，如果相同值的元素有多个，只会删除第一个
stus.remove('猪八戒')

reverse()
# 用来反转列表
stus.reverse()

sort()
# 用来对列表中的元素进行排序，默认是升序排列
# 如果需要降序排列，则需要传递一个reverse=True作为参数

# 通过while循环来遍历列表
i = 0
while i < len(stus):
   print(stus[i])
   i += 1

# 通过for循环来遍历列表
# 语法：
#   for 变量 in 序列 :
#       代码块
# for循环的代码块会执行多次，序列中有几个元素就会执行几次
# 每执行一次就会将序列中的一个元素赋值给变量，所以我们可以通过变量，来获取列表中的元素
for s in stus :
    print(s)
```



#### 1.3.1.4、通用操作

 **+** 和 *****

```python
# +可以将两个列表拼接为一个列表
my_list = [1,2,3] + [4,5,6]

# * 可以将列表重复指定的次数
my_list = [1,2,3] * 5
```
**in** 和 **not in**

```python
#in用来检查指定元素是否存在于列表中，如果存在，返回True，否则返回False
print('牛魔王' in stus)

# not in用来检查指定元素是否不在列表中，如果不在，返回True，否则返回False
print('牛魔王' not in stus)
```

```python
# len()获取列表中的元素的个数
# min() 获取列表中的最小值
# max() 获取列表中的最大值
```

**index()**

```python
#s.index() 获取指定元素在列表中的第一次出现时索引
print(stus.index('沙和尚'))

#index()的第二个参数，表示查找的起始位置 ， 第三个参数，表示查找的结束位置
print(stus.index('沙和尚',3,7))

#如果要获取列表中没有的元素，会抛出异常
# print(stus.index('牛魔王')) ValueError: '牛魔王' is not in list
```

**count()**

```python
#s.count() 统计指定元素在列表中出现的次数
print(stus.count('牛魔王'))
```

**del**

```python
# 通过del来删除元素
del stus[2]  # 删除索引为2的元素
```

### 1.3.2、元组（tuple）

元组是一个不可变的序列，它的操作的方式基本上和列表是一致的。

一般当我们希望数据不改变时，就使用元组，其余情况都使用列表。

```python
# 使用()来创建元组
my_tuple = ()  # 创建了一个空元组

# 元组是不可变对象，不能尝试为元组中的元素重新赋值
# my_tuple[3] = 10 TypeError: 'tuple' object does not support item assignment

# 当创建元组不是空元组时，括号可以省略
my_tuple = 10, 20, 30, 40
```

#### 1.3.2.1、解包

元组的解包（解构）

解包指就是将元组当中每一个元素都赋值给一个变量

```python
a, b, c, d = my_tuple
```

交互a 和 b的值，这时我们就可以利用元组的解包

```python
a, b = b, a
```

在对一个元组进行解包时，变量的数量必须和元组中的元素的数量一致

也可以在变量前边添加一个*****，这样变量将会获取元组中所有剩余的元素

```python
*a, b, c = my_tuple
a, b, *c = [1, 2, 3, 4, 5, 6, 7]
a, b, *c = 'hello world'

# 不能同时出现两个或以上的*变量
# *a , *b , c = my_tuple SyntaxError: two starred expressions in assignment
```

### 1.3.3、字典（dict）

#### 1.3.3.1、基本概念

字典属于一种新的数据结构，称为映射（mapping），字典的作用和列表类似，都是用来存储对象的容器。

列表存储数据的性能很好，但是查询数据的性能的很差。

在字典中每一个元素都有一个唯一的名字，通过这个唯一的名字可以快速的查找到指定的元素。

在查询元素时，字典的效率是非常快的。

在字典中可以保存多个对象，每个对象都会有一个唯一的名字，这个唯一的名字，我们称其为键（**key**），通过key可以快速的查询value
这个对象，我们称其为值（**value**），所以字典，我们也称为叫做键值对（key-value）结构，每个字典中都可以有多个键值对，而每一个键值对我们称其为一项（**item**）

- 字典的**值**可以是**任意对象**
- 字典的**键**可以是**任意的不可变对象**（int、str、bool、tuple ...），但是一般我们都会使用**str**
- 字典的**键是不能重复的**，如果出现重复的后出现的会替换到先出现的

#### 1.3.3.2、用法

```python
# 使用 {} 来创建字典
d = {}  # 创建了一个空字典
 
#创建一个保护有数据的字典
# 语法：
#   {key:value,key:value,key:value}
# d = {'name':'孙悟空' , 'age':18 , 'gender':'男' , 'name':'sunwukong'}

# 如果使用了字典中不存在的键，会报错
# print(d['hello']) KeyError: 'hello'
```

#### 1.3.3.3、方法

使用 **dict()**函数来创建字典

每一个参数都是一个键值对，参数名就是键，参数名就是值（这种方式创建的字典，key都是字符串）

```python
d = dict(name='孙悟空',age=18,gender='男')
```

也可以将一个包含有**双值子序列**的序列转换为字典，双值序列，序列中只有两个值，[1,2]、 ('a',3)、 'ab'

子序列，如果序列中的元素也是序列，那么我们就称这个元素为子序列 [(1,2),(3,5)]

```python
d = dict([('name','孙悟饭'),('age',18), ('wawa', '666')])
```

**len()** 获取字典中键值对的个数

```python
print(len(d))
```

**in** 检查字典中是否包含指定的键

**not in** 检查字典中是否不包含指定的键

```python
print('hello' in d)
```

获取字典中的值，根据键来获取值

```python
语法：d[key]
d['age']
```

**get([key, default])** 该方法用来根据键来获取字典中的值

如果获取的键在字典中不存在，会返回**None**

也可以指定一个默认值，来作为第二个参数，这样获取不到值时将会返回默认值

```python
print(d.get('hello','默认值'))
```

修改字典

```python
# 如果key存在则覆盖，不存在则添加
d[key] = value
```

**setdefault([key, default])** 可以用来向字典中添加key-value

如果key已经存在于字典中，则返回key的值，不会对字典做任何操作

如果key不存在，则向字典中添加这个key，并设置value

```python
d.setdefault('name','猪八戒')
```

**update([other])** 将其他的字典中的key-value添加到当前字典中

如果有重复的key，则后边的会替换到当前的

```python
d.update(d2)
```

删除，可以使用 **del** 来删除字典中的 key-value

````python
del d['a']
del d['z'] # z不存在，报错
````

**popitem() **随机删除字典中的一个键值对，一般都会删除**最后一个键值对**

删除之后，它会将删除的key-value作为返回值返回，返回的是一个元组，元组中有两个元素，第一个元素是删除的key，第二个是删除的value

当使用popitem()删除一个空字典时，会抛出异常 KeyError: 'popitem(): dictionary is empty'

```python
result = d.popitem()
```

**pop([key, default])** 根据key删除字典中的key-value，会将被删除的value返回，如果删除不存在的key，会抛出异常

如果指定了默认值，再删除不存在的key时，不会报错，而是直接返回默认值

```python
result = d.pop('z','这是默认值')
```

**clear()**用来清空字典

```python
d.clear()
```

**copy()** 该方法用于对字典进行浅复制

复制以后的对象，和原对象是独立，修改一个不会影响另一个

注意，浅复制会简单复制对象内部的值，如果值也是一个可变对象，这个可变对象不会被复制

```python
d2 = d.copy()
```

**keys()**来获取所有的键

```python
for k in d.keys() :
	print(k , d[k])
```

**values()**该方法会返回一个序列，序列中保存有字典的左右的值

```python
for v in d.values():
	print(v)
```

**items()** 该方法会返回字典中所有的项

它会返回一个序列，序列中包含有双值子序列

```python
for k, v in d.items():
    print(k, '=', v)
```



### 1.3.4、集合（set）

#### 1.3.4.1、基本概念

集合和列表非常相似

不同点：

1.集合中只能存储**不可变对象**。
2.集合中存储的对象是**无序**（不是按照元素的插入顺序保存）。
3.集合中**不能出现重复的元素**。

#### 1.3.4.2、用法

```python
# 使用 {} 来创建集合
s = {10, 3, 5, 1, 2, 1, 2, 3, 1, 1, 1, 1}

# 使用 set() 函数来创建集合
s = set()  # 空集合

# 可以通过set()来将序列和字典转换为集合
# 使用set()将字典转换为集合时，只会包含字典中的键
s = set([1, 2, 3, 4, 5, 1, 1, 2, 3, 4, 5])
s = set('hello')
s = set({'a': 1, 'b': 2, 'c': 3})


```

#### 1.3.4.3、方法

使用**in**和**not in**来检查集合中的元素

```python
print('c' in s)
```

使用**len()**来获取集合中元素的数量

```python
print(len(s))
```

**add()** 向集合中添加元素

```python
s.add(10)
```

**update()** 将一个**集合**中的元素添加到当前集合中

update()可以传递**序列**或**字典**作为参数，字典只会使用**键**

```python
s.update(s2)
s.update((10, 20, 30, 40, 50))
s.update({10: 'ab', 20: 'bc', 100: 'cd', 1000: 'ef'})
```

**pop()**随机删除并返回一个集合中的元素

```python
result = s.pop()
```

**remove()**删除集合中的指定元素

```python
s.remove(100)
```

**clear()**清空集合

```python
s.clear()
```

**copy()**对集合进行浅复制

```python
```

#### 1.3.4.4、运算

```python
s = {1,2,3,4,5}
s2 = {3,4,5,6,7}

# & 交集运算
result = s & s2 # {3, 4, 5}

# | 并集运算
result = s | s2 # {1,2,3,4,5,6,7}

# - 差集
result = s - s2 # {1, 2}

# ^ 异或集 获取只在一个集合中出现的元素
result = s ^ s2 # {1, 2, 6, 7}

# <= 检查一个集合是否是另一个集合的子集
# 如果a集合中的元素全部都在b集合中出现，那么a集合就是b集合的子集，b集合是a集合超集
a = {1,2,3}
b = {1,2,3,4,5}
result = a <= b # True
result = {1,2,3} <= {1,2,3} # True
result = {1,2,3,4,5} <= {1,2,3} # False

# < 检查一个集合是否是另一个集合的真子集
# 如果超集b中含有子集a中所有元素，并且b中还有a中没有的元素，则b就是a的真超集，a是b的真子集
result = {1,2,3} < {1,2,3} # False
result = {1,2,3} < {1,2,3,4,5} # True

# >= 检查一个集合是否是另一个的超集
# > 检查一个集合是否是另一个的真超集
print('result =',result)
```

## 1.4、函数

### 1.4.1、简介

**函数也是一个对象**，函数可以用来保存一些可执行的代码，并且可以在需要时，对这些语句进行多次的调用。

函数中保存的代码不会立即执行，需要调用函数代码才会执行。

定义函数一般都是要实现某种功能的。

在Python中，函数是**一等对象**，一等对象一般都会具有如下特点：
① 对象是在运行时创建的
② 能赋值给变量或作为数据结构中的元素
③ 能作为参数传递
④ 能作为返回值返回

高阶函数至少要符合以下两个特点中的一个
① 接收一个或多个函数作为参数
② 将函数作为返回值返回 

装饰器





创建函数：

~~~python
def 函数名(形参1,形参2,...形参n) :
    代码块
~~~

函数名必须要符号标识符的规范（可以包含字母、数字、下划线、但是不能以数字开头）

调用函数：

 ~~~python
 函数对象()
 ~~~

### 1.4.2、函数的参数

在定义函数时，可以在函数名后的()中定义数量不等的形参，多个形参之间使用 **, **隔开。

- 形参（形式参数），定义形参就相当于在函数内部声明了变量，但是并不赋值。

- 实参（实际参数），如果函数定义时，指定了形参，那么在调用函数时也必须传递实参，实参将会赋值给对应的形参，简单来说，有几个形参就得传几个实参。

解析器不会检查实参的类型，因此**实参可以传递任意类型的对象**。

**注意**：

- 在函数中对形参进行重新赋值，不会影响其他的变量。

```python
def fn4(a):
    a = 20
    print('a =', a, id(a))
    
c = 10
fn4(c)
print('c =', c, id(c))

a = 20 2854937822032
c = 10 2854937821712
```

- 如果形参指向的是一个对象，当通过形参去修改对象时，会影响到所有指向该对象的变量。

~~~python
def fn4(a):
    a[0] = 30
    print('a =', a, id(a))


c = [1, 2, 3]

fn4(c)
print('c =', c, id(c))

a = [30, 2, 3] 2251289769216
c = [30, 2, 3] 2251289769216

fn4(c.copy())
print('c =', c, id(c))

a = [30, 2, 3] 2251290065792
c = [30, 2, 3] 2251289769216

fn4(c[:])
print('c =', c, id(c))

a = [30, 2, 3] 2251290065792
c = [30, 2, 3] 2251289769216
~~~



#### 1.4.2.1、位置参数

位置参数就是将对应位置的实参复制给对应位置的形参。

fn(1 , 2 , 3)

#### 1.4.2.2、关键字参数

关键字参数，可以不按照形参定义的顺序去传递，而直接根据参数名去传递参数

fn(b=1 , c=2 , a=3)

混合使用关键字和位置参数时，必须将**位置参数写到前面**。

#### 1.4.2.3、不定长参数

在定义函数时，可以在形参前边加上一个 *****，这样这个形参将会获取到所有的实参。

它将会将所有的实参保存到一个元组中。

**注意**：

- 星号形参只能有一个
- 星号形参，可以和其他参数配合使用
- 星号形参只能接收位置参数，而不能接收关键字参数

~~~python
def fn2(a,b,*c):
~~~

- 可变参数**不是必须写在最后**，但是注意，带 * 的参数后的所有参数，**必须以关键字参数的形式传递**。

~~~python
def fn2(a,*b,c):
~~~

- 如果在形参的开头直接写一个 *****，则要求我们的所有的参数必须以**关键字参数**的形式传递

~~~python
def fn2(*,a,b,c):
    
fn2(a=3,b=4,c=5)
~~~

- ****** 形参可以接收其他的关键字参数，它会将这些参数统一保存到一个**字典**中

~~~python
# **形参只能有一个，并且必须写在所有参数的最后
# 字典的key就是参数的名字，字典的value就是参数的值
def fn3(b,c,**a):

fn3(b=1,d=2,c=3,e=10,f=20)
~~~

#### 1.4.2.4、参数解包

传递实参时，也可以在序列类型的参数前添加星号，这样他会自动将序列中的元素依次作为参数传递

这里要求序列中元素的个数必须和形参的个数的一致

***** 传递一个可变参数列表给函数形参

~~~python
def fn4(a,b,c):

t = (10,20,30)
fn4(*t)
~~~

****** 将一个可变的关键字参数的字典传给函数形参

~~~python
d = {'a':100,'b':200,'c':300}
fn4(**d)
~~~

~~~python
a = (10,20)
d = {'c':300}
fn4(*a, **d)
~~~

**注意**：

- 二者可以组合使用，但是 ***** 必须在 ****** 之前

- 在解包时，***** 与 ****** 所传参数不能出现重合，否者报错 got multiple values for argument



### 1.4.3、返回值

#### 1.4.3.1、简介

返回值，返回值就是函数执行以后返回的结果，可以通过 return 来指定函数的返回值，return 后边可以跟任意的对象，返回值甚至可以是一个函数。

可以直接使用函数的返回值，也可以通过一个变量来接收函数的返回值。

return 一旦执行函数自动结束。

~~~python
def fn():
    def fn2() :
        print('hello')
    return fn2 # 返回值也可以是一个函数

r = fn() # 这个函数的执行结果就是它的返回值
r()
~~~

如果仅仅写一个return 或者 不写return，则相当于return None 

~~~python
def fn2() :
    return
~~~

#### 1.4.3.1、文档字符串（doc str）

在定义函数时，可以在函数内部编写文档字符串，文档字符串就是函数的说明，当我们编写了文档字符串时，就可以通过help()函数来查看函数的说明。

文档字符串非常简单，其实直接在函数的第一行写一个字符串就是文档字符串

~~~python
def fn(a:int,b:bool,c:str='hello') -> int:
    '''
    这是一个文档字符串的示例

    函数的作用：。。。。。
    函数的参数：
        a，作用，类型，默认值。。。。
        b，作用，类型，默认值。。。。
        c，作用，类型，默认值。。。。
    '''
    return 10

help(fn)
~~~

help()是Python中的内置函数，通过help()函数可以查询python中的函数的用法

~~~python
语法：help(函数对象)
help(print) # 获取print()函数的使用说明
~~~

### 1.4.4、高阶函数

接收函数作为参数，或者将函数作为返回值的函数是高阶函数。

当使用一个函数作为参数时，实际上是将指定的代码传递进了目标函数。

~~~python
def fn(func , lst) :
~~~

#### 1.4.4.1、闭包

将函数作为返回值返回，通过闭包可以创建一些只有当前函数能访问的变量，可以将一些私有的数据藏到的闭包中

~~~python
def fn():
    a = 10
    # 函数内部再定义一个函数
    def inner():
        print('我是fn2' , a)
    # 将内部函数 inner作为返回值返回   
    return inner
# r是一个函数，是调用fn()后返回的函数
# 这个函数实在fn()内部定义，并不是全局函数，所以这个函数总是能访问到fn()函数内的变量
r = fn() 
r()
~~~

形成闭包的要件：

- 函数嵌套
- 将内部函数作为返回值返回
- 内部函数必须要使用到外部函数的变量

#### 1.4.4.2、装饰器

~~~python
def begin_end(old):
    '''
        用来对其他函数进行扩展，使其他函数可以在执行前打印开始执行，执行后打印执行结束
        参数：
            old 要扩展的函数对象
    '''
    
    # 创建一个新函数
    def new_function(*args , **kwargs):
        print('开始执行~~~~')
        # 调用被扩展的函数
        result = old(*args , **kwargs)
        print('执行结束~~~~')
        # 返回函数的执行结果
        return result

    # 返回新函数        
    return new_function


def test(first, *args, **kwargs):
    
test(1, 2, 3, 4, k1=5, k2=6)
1 ---》 first
2，3，4 ---》 args
k1，k2 ---》
~~~

begin_end()这种函数我们就称它为装饰器，通过装饰器，可以在不修改原来函数的情况下来对函数进行扩展。

在定义函数时，可以通过@装饰器，来使用指定的装饰器，来装饰当前的函数。

可以同时为一个函数指定多个装饰器，这样函数将会安装从内向外的顺序被装饰 。

~~~python
def fn3(old):
    '''
        用来对其他函数进行扩展，使其他函数可以在执行前打印开始执行，执行后打印执行结束

        参数：
            old 要扩展的函数对象
    '''
    
    # 创建一个新函数
    def new_function(*args , **kwargs):
        print('fn3装饰~开始执行~~~~')
        # 调用被扩展的函数
        result = old(*args , **kwargs)
        print('fn3装饰~执行结束~~~~')
        # 返回函数的执行结果
        return result

    # 返回新函数        
    return new_function

@fn3
@begin_end
def say_hello():
    print('大家好~~~')
~~~



### 常用函数

#### input()

input()调用后，程序会立即暂停，等待用户输入

用户输入完内容以后，点击回车程序才会继续向下执行

用户输入完成以后，其所输入的的内容会以返回值得形式返回

注意：input()的返回值是一个字符串

input()函数中可以设置一个字符串作为参数，这个字符串将会作为提示文字显示

#### range()

range()是一个函数，可以用来生成一个自然数的序列

```python
r = range(5) # 生成一个这样的序列[0,1,2,3,4]
```

该函数需要三个参数

1.起始位置（可以省略，默认是0）

2.结束位置

3.步长（可以省略，默认是1）

#### filter()

filter()可以从序列中过滤出符合条件的元素，保存到一个新的序列中

参数：

- 函数，根据该函数来过滤序列（可迭代的结构）
- 需要过滤的序列（可迭代的结构）

返回值：

- 过滤后的新序列（可迭代的结构）

~~~python
r = filter(lambda i : i > 5 , l)
~~~

#### map()

map()函数可以对可跌倒对象中的所有元素做指定的操作，然后将其添加到一个新的对象中返回

~~~python
r = map(lambda i : i ** 2 , l)
~~~

#### sort()

该方法用来对列表中的元素进行排序，sort()方法默认是直接比较列表中的元素的大小

在sort()可以接收一个关键字参数key，key需要一个函数作为参数，当设置了函数作为参数，每次都会以列表中的一个元素作为参数来调用函数，并且使用函数的返回值来比较元素的大小。

~~~python
l = [2,5,'1',3,'6','4']
l.sort(key=int)
~~~

#### sorted()

这个函数和sort()的用法基本一致，但是sorted()可以对任意的序列进行排序

并且使用sorted()排序不会影响原来的对象，而是返回一个新对象



## 1.5、作用域与命名空间

### 1.5.1、作用域

在Python中一共有两种作用域：

1. **全局作用域**

   - 全局作用域在程序执行时创建，在程序执行结束时销毁

   - 所有函数以外的区域都是全局作用域

   - 在全局作用域中定义的变量，都属于全局变量，全局变量可以在程序的任意位置被访问

2. **函数作用域**

   - 函数作用域在函数调用时创建，在调用结束时销毁

   - 函数每调用一次就会产生一个新的函数作用域

   - 在函数作用域中定义的变量，都是局部变量，它只能在函数内部被访问

**变量的查找**：当我们使用变量时，会优先在当前作用域中寻找该变量，如果有则使用。

如果没有则继续去上一级作用域中寻找，如果有则使用，如果没有，以此类推，最后找到全局作用域，依旧没有则报错NameError: name 'a' is not defined。

~~~python
def fn3():
    # 在函数中为变量赋值时，默认都是为局部变量赋值
    # a = 10 
    # 如果希望在函数内部修改全局变量，则需要使用global关键字，来声明变量
    # 声明在函数内部的使用a是全局变量，此时再去修改a时，就是在修改全局的a
    global a 
    a = 10 # 修改全局变量
    print('函数内部：','a =',a)
~~~

### 1.5.2、命名空间（namespace）

命名空间指的是变量存储的位置，每一个变量都需要存储到指定的命名空间当中。

每一个作用域都会有一个它对应的命名空间。

- 全局命名空间，用来保存全局变量。

- 函数命名空间用来保存函数中的变量

命名空间实际上就是一个字典，是一个专门用来存储变量的字典。

**locals()**用来获取当前作用域的命名空间

如果在全局作用域中调用locals()则获取全局命名空间，如果在函数作用域中调用locals()则获取函数命名空间。

~~~python
scope = locals() # 当前命名空间

# 向scope中添加一个key-value
# 向字典中添加key-value就相当于在全局中创建了一个变量（一般不建议这么做）
scope['c'] = 1000

def fn4():
    a = 10
    # 在函数内部调用locals()会获取到函数的命名空间
    # scope = locals() 
    # 可以通过scope来操作函数的命名空间，但是也是不建议这么做
    # scope['b'] = 20 

    # globals()函数可以用来在任意位置获取全局命名空间 
    global_scope = globals()
    # print(global_scope['a'])
    # 可以但不推荐
    global_scope['a'] = 30
    # print(scope)
~~~









