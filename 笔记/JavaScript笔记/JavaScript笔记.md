> ver.2025.04.14.3

# 1、JavaScript 概述

JavaScript 最初由 Brendan Eich 于 1993 年在 Netscape 公司创建，其初衷是为静态网页增加交互性

随着互联网的飞速发展，JavaScript 已经从最初的浏览器脚本语言演变为一种功能强大的多范式动态语言，被广泛应用于网站、移动应用、桌面应用、服务器甚至操作系统等多个领域

如今，JavaScript 被认为是全球最流行的编程语言之一，其标准化版本被称为 ECMAScript，并且是所有现代 Web 浏览器的默认语言

JavaScript 在 Web 开发中扮演着至关重要的角色，它不仅能够增强用户界面的交互性，还能用于构建复杂的单页应用程序。不仅如此，JavaScript 的触角已经延伸到后端开发（Node.js）、移动应用开发（React Native、Ionic）、桌面应用开发（Electron）等多个领域，这种广泛的适用性使得掌握 JavaScript 成为开发人员的一项核心技能



# 2、开发环境

首先需要搭建一个基本的开发环境，对于 Web 开发而言，最简单的方式是将 JavaScript 代码嵌入到 HTML 文档中，通过 <script> 标签实现，也可以直接在 <script> 标签内部编写 JavaScript 代码，也可以使用 src 属性链接到外部的.js文件

此外，利用浏览器内置的开发者控制台（通常通过按 F12 键打开）可以方便地测试和调试 JavaScript 代码，开发者控制台提供了一个交互式的环境，可以实时执行 JavaScript 代码并查看结果，这对于初学者理解语言特性和调试程序非常有帮助，选择一款合适的代码编辑器也能显著提升开发效率，例如Visual Studio Code、Sublime Text等

学习任何编程语言的第一个程序通常是“Hello, World!”，在 JavaScript 中，可以使用console.log()函数将文本输出到浏览器的控制台，一个简单的 “Hello, World!” 程序如下所示：

```JavaScript
console.log("Hello, World!");
```

这段代码展示了 JavaScript 的基本结构：使用内置函数执行特定操作，并以分号结束语句（尽管分号在某些情况下是可选的）



# 3、基本语法

## 1、变量

### 1、变量声名

变量是程序中用于存储数据的命名容器，在 JavaScript 中，可以使用 var、let 和 const 这三个关键字来声明变量

- var 是早期版本 JavaScript 中声明变量的方式，它具有函数作用域或全局作用域（如果在任何函数之外声明），这意味着使用 var 声明的变量在其所在的整个函数内都是可访问的，即使声明语句位于代码块（如if语句或循环）内部
- 相比之下，let 和 const 关键字在 ECMAScript 6（ES6）中引入，它们声明的变量具有块级作用域，这意味着这些变量只在其声明所在的代码块内部可见

现代 JavaScript 开发通常推荐使用 let 和 const 来替代 var



### 2、变量提升

使用 var 声明的变量会被提升到其作用域的顶部，这意味着可以在声明之前使用该变量，但其值在声明之前是 undefined，而 let 和 const 声明也会被提升，但它们不会被初始化，如果在声明之前访问它们，会导致 ReferenceError，这被称为暂时性死区（Temporal Dead Zone，TDZ）



### 3、变量修改

使用 var 和 let 声明的变量可以被重新赋值，但是，使用 const 声明的变量在初始化之后不能再被重新赋值，尝试重新赋值会导致TypeError，此外，const 声明的变量在声明时必须进行初始化，否则会抛出 SyntaxError

关于变量的重新声明，在非严格模式下，可以使用 var 在同一作用域内多次声明同一个变量而不会报错（尽管不推荐这样做），但是，使用 let 和 const 在同一作用域内重新声明同一个变量会导致 SyntaxError

现代 JavaScript 开发中，推荐优先使用 const，只有当变量的值确实需要改变时才使用 let，并尽量避免使用 var

```JavaScript
// var 示例
function exampleVar() {
  var x = 10;
  if (true) {
    var x = 20; // 这里的 x 会覆盖函数作用域的 x
    console.log(x); // 输出 20
  }
  console.log(x); // 输出 20
}
exampleVar();

// let 示例
function exampleLet() {
  let y = 10;
  if (true) {
    let y = 20; // 这里的 y 是块级作用域的，不会覆盖外部的 y
    console.log(y); // 输出 20
  }
  console.log(y); // 输出 10
}
exampleLet();

// const 示例
function exampleConst() {
  const z = 10;
  // z = 20; // 错误: Assignment to constant variable.
  console.log(z); // 输出 10
}
exampleConst();
```



# 3、数据类型

## 1、原始数据类型

JavaScript 定义了七种原始数据类型：Number、BigInt、String、Boolean、Symbol、Undefined 和 Null

- Number 类型用于表示整数和浮点数
- BigInt 类型用于表示任意精度的整数
- String 类型用于存储文本数据，可以使用单引号或双引号括起来
- Boolean 类型只有两个值：true 和 false，常用于条件判断
- Symbol 类型用于创建唯一的标识符
- Undefined 类型表示变量已声明但尚未赋值
- Null 类型表示一个有意为之的空值

可以使用 **typeof** 运算符来检测一个值的类型，需要注意的是，typeof null 会返回"object"

除了 Null 和 Undefined，每种原始类型都有其对应的包装对象（Boolean、Number、String、BigInt、Symbol），原始类型的值是不可变的，这意味着一旦创建，就不能被改变，对原始值的操作总是返回一个新的原始值

```JavaScript
let age = 30; // Number
let bigIntValue = 9007199254740992n; // BigInt
let name = "Alice"; // String
let isTrue = true; // Boolean
let uniqueId = Symbol("id"); // Symbol
let notAssigned; // Undefined
let emptyValue = null; // Null

console.log(typeof age); // 输出 "number"
console.log(typeof bigIntValue); // 输出 "bigint"
console.log(typeof name); // 输出 "string"
console.log(typeof isTrue); // 输出 "boolean"
console.log(typeof uniqueId); // 输出 "symbol"
console.log(typeof notAssigned); // 输出 "undefined"
console.log(typeof emptyValue); // 输出 "object"
```



## 2、引用类型 

引用数据类型主要包括对象（Object）、数组（Array）和函数（Function），从技术上讲，数组和函数也是对象

与原始类型不同，引用类型是可变的，这意味着它们的值可以在创建后被修改，当将一个引用类型的值赋给一个变量时，变量实际上存储的是对内存中对象的引用（或指针），而不是对象本身，因此，当多个变量引用同一个对象时，通过其中一个变量修改对象会影响到所有引用该对象的变量

```JavaScript
let person = { name: "Bob", age: 25 }; // 对象
let numbers = [16, 3, 5]; // 数组
function greet() { // 函数
  console.log("Hello!");
}

let anotherPerson = person;
anotherPerson.age = 26;
console.log(person.age); // 输出 26，因为 person 和 anotherPerson 引用同一个对象

let anotherNumbers = numbers;
anotherNumbers.push(4);
console.log(numbers); // 输出 [16, 3, 5, 17]，因为 numbers 和 anotherNumbers 引用同一个数组
```



## 3、值传递与引用传递

原始类型和引用类型在内存中的存储方式以及赋值和传递方式上存在关键区别：

- 原始类型的值直接存储在变量所访问的内存位置中，当将一个原始值赋给另一个变量时，会创建一个该值的副本并存储在新变量的内存位置中，这被称为“按值传递”（pass by value），因此，修改其中一个变量的值不会影响到另一个变量
- 引用类型的值（即对象）存储在堆内存中，变量存储的是对该对象在堆内存中位置的引用，当将一个引用类型的值赋给另一个变量时，实际上是复制了这个引用，两个变量指向内存中的同一个对象，这被称为“按引用传递”（pass by reference） ，因此，通过任何一个变量修改对象，所有引用该对象的变量都会反映出这些变化

```JavaScript
// 原始类型按值传递
let num1 = 10;
let num2 = num1;
num2 = 20;
console.log(num1); // 输出 10
console.log(num2); // 输出 20

// 引用类型按引用传递
let obj1 = { value: 10 };
let obj2 = obj1;
obj2.value = 20;
console.log(obj1.value); // 输出 20
console.log(obj2.value); // 输出 20
```



# 4、运算符

运算符是用于执行各种操作的符号，JavaScript 提供了多种类型的运算符，包括算术运算符、赋值运算符、比较运算符、逻辑运算符、位运算符、字符串运算符和特殊运算符

算术运算符用于执行基本的数学运算，如加法（`+`）、减法（`-`）、乘法（`*`）、除法（`/`）、取余（`%`）和求幂（`**`），自增（`++`）和自减（`--`）运算符用于快速增加或减少变量的值

赋值运算符用于将值赋给变量，最基本的赋值运算符是等号（`=`），还有复合赋值运算符，如`+=`、`-=`等

比较运算符用于比较两个值，并返回一个布尔值，包括等于（`==`）、严格等于（`===`）、不等于（`!=`）、严格不等于（`!==`）、大于（`>`）、小于（`<`）、大于等于（`>=`）和小于等于（`<=`）

逻辑运算符用于执行布尔逻辑运算，包括逻辑与（`&&`）、逻辑或（`||`）和逻辑非（`!`），逻辑与和逻辑或运算符还具有短路求值的特性

逻辑赋值运算符包括`&&=`、`||=`和`??=`

位运算符用于对数字的二进制位进行操作，包括按位与（`&`）、按位或（`|`）、按位异或（`^`）、按位取反（`~`）、左移（`<<`）、右移（`>>`）和无符号右移（`>>>`）

字符串运算符主要包括用于字符串连接的加号（`+`）和加等于号（`+=`）

殊运算符包括`typeof`（用于检测数据类型）、`instanceof`（用于检查对象是否为特定构造函数的实例）、`in`（用于检查属性是否存在于对象中）、`delete`（用于删除对象的属性）、`void`、`new`、`this`、`super`、`...`（展开运算符）、`?.`（可选链）、`??`（空值合并运算符）和`?:`（三元运算符）

```JavaScript
// 算术运算符
let a = 10;
let b = 5;
console.log(a + b); // 输出 15
console.log(a - b); // 输出 5
console.log(a * b); // 输出 50
console.log(a / b); // 输出 2
console.log(a % b); // 输出 0
console.log(a ** b); // 输出 100000

// 赋值运算符
let c = 10;
c += 5; // 等同于 c = c + 5
console.log(c); // 输出 15

// 比较运算符
let d = 5;
let e = "5";
console.log(d == e); // 输出 true (松散相等，会进行类型转换)
console.log(d === e); // 输出 false (严格相等，不会进行类型转换)

// 逻辑运算符
let f = true;
let g = false;
console.log(f && g); // 输出 false (逻辑与)
console.log(f || g); // 输出 true (逻辑或)
console.log(!f); // 输出 false (逻辑非)
```



# 5、表达式

表达式是 JavaScript 中能够产生一个值的代码单元，表达式可以是简单的字面量（如数字、字符串、布尔值）、变量、运算符与操作数的组合，也可以是函数调用等

JavaScript 中有多种类型的表达式，包括算术表达式、赋值表达式、逻辑表达式、比较表达式、字符串表达式、对象表达式、数组表达式、函数表达式等

运算符优先级决定了表达式中不同运算符的计算顺序，可以使用圆括号()来显式地控制表达式的计算顺序

```javascript
// 字面量表达式
10; // 数字字面量
"hello"; // 字符串字面量
true; // 布尔字面量
{}; // 对象字面量
; // 数组字面量
 function() {}; // 函数表达式

 // 变量表达式
 let x = 5;
 x; // 变量表达式

 // 运算符与操作数组合的表达式
 2 + 3; // 算术表达式
 x = 10; // 赋值表达式
 x > 5; // 比较表达式
 true && false; // 逻辑表达式

 // 函数调用表达式
 console.log("hello"); // 函数调用表达式

 // 使用括号控制优先级
 (2 + 3) * 4; // 先计算括号内的加法
```



# 6、语句

语句是 JavaScript 中的基本执行单元，它指示计算机执行一个特定的操作，JavaScript 程序是由一系列语句组成的

语句可以分为多种类型，包括表达式语句（如函数调用、赋值）、声明语句（用于声明变量、函数或类，如 var、let、const、function、class）、控制流语句（用于控制代码的执行顺序，如 if、else、switch、for、while）和跳转语句（用于改变代码的执行流程，如 break、continue、return、throw）

```javascript
// 声明语句
let message = "Hello";
function greet(name) {
  console.log("Hello, " + name + "!");
}

// 表达式语句
greet("World"); // 函数调用表达式
message = "Hi"; // 赋值表达式
console.log(message); // 函数调用表达式

// 控制流语句
if (message === "Hi") {
  console.log("The message is Hi.");
} else {
  console.log("The message is not Hi.");
}

// 跳转语句
for (let i = 0; i < 5; i++) {
  if (i === 3) {
    break; // 终止循环
  }
  console.log(i);
}
```



# 7、注释与空白符

注释是代码中用于解释和说明的文本，不会被 JavaScript 解释器执行

JavaScript 支持单行注释（使用`//`）和多行注释（使用`/*... */`）

空白符（如空格、制表符、换行符）在 JavaScript 中通常被忽略，但它们可以提高代码的可读性，良好的注释习惯和适当的空白符使用是编写清晰易懂代码的重要组成部分

```javascript
// 这是一个单行注释

/*
这是一个
多行注释
*/

let counter = 0; // 初始化计数器 (行尾注释)

function incrementCounter() {
  counter++;
  console.log(counter);
}

incrementCounter();
```



# 8、分号

在 JavaScript 中，语句通常以分号`;`结束，但 JavaScript 具有自动分号插入（Automatic Semicolon Insertion，ASI）机制，这意味着在某些情况下，即使省略了分号，JavaScript 引擎也会自动将其插入到代码中，然而，ASI 并非总是可靠，在某些特定情况下可能会导致意想不到的行为，因此，为了代码的清晰性和避免潜在的错误，推荐始终显式地使用分号来结束语句



# 9、控制流

## 1、条件语句

条件语句用于根据不同的条件执行不同的代码块

- JavaScript 提供了 if 语句、else if 语句、else 语句和 switch 语句来实现条件控制
- if 语句用于判断一个条件是否为真，如果为真则执行相应的代码块
- else if 语句允许在第一个if条件不满足时检查另一个条件
- else 语句提供了一个在所有 if 和 else if 条件都不满足时执行的代码块
- switch 语句用于将一个表达式的值与多个 case 子句的值进行比较，如果匹配则执行相应的代码块，在 switch 语句中，通常需要使用break 语句来防止代码执行“穿透”到下一个 case 子句
- 三元运算符（? :）提供了一种简洁的语法来表示简单的 if-else 条件判断



```JavaScript
// if 语句
let temperature = 20;
if (temperature > 25) {
  console.log("It's hot!");
} else {
  console.log("It's not too hot.");
}

// else if 语句
let score = 75;
if (score >= 90) {
  console.log("Grade A");
} else if (score >= 80) {
  console.log("Grade B");
} else if (score >= 70) {
  console.log("Grade C");
} else {
  console.log("Grade D");
}

// switch 语句
let day = "Monday";
switch (day) {
  case "Monday":
    console.log("Start of the week");
    break;
  case "Friday":
    console.log("Almost weekend");
    break;
  default:
    console.log("Mid-week");
}

// 三元运算符
let isMember = true;
let discount = isMember? 0.1 : 0;
console.log("Discount: " + discount);
```



## 2、循环语句

循环语句用于重复执行一段代码，直到满足特定的条件为止

JavaScript 提供了 for 循环、while 循环、do...while 循环、for...in 循环和 for...of 循环

- for 循环通常用于在已知循环次数的情况下执行代码，它包含初始化、条件判断和迭代更新三个部分
- while 循环在指定的条件为真时重复执行代码块，条件判断在每次循环开始之前进行
- do...while 循环与 while 循环类似，但它首先执行一次代码块，然后在每次循环结束后检查条件，这意味着 do...while 循环至少会执行一次
- for...in 循环用于遍历对象的可枚举属性名（键），需要注意的是，它遍历的属性包括对象自身的属性以及从原型链继承的属性，并且属性遍历的顺序可能不是确定的
- for...of 循环用于遍历可迭代对象的值，例如数组、字符串、Map 和 Set 等

```JavaScript
// for 循环
for (let i = 0; i < 5; i++) {
  console.log("For loop iteration: " + i);
}

// while 循环
let count = 0;
while (count < 5) {
  console.log("While loop count: " + count);
  count++;
}

// do...while 循环
let j = 0;
do {
  console.log("Do...while loop: " + j);
  j++;
} while (j < 5);

// for...in 循环 (遍历对象属性)
const person = { name: "Charlie", age: 30 };
for (let key in person) {
  console.log(key + ": " + person[key]);
}

// for...of 循环 (遍历数组元素)
const colors = ["red", "green", "blue"];
for (let color of colors) {
  console.log(color);
}
```



## 3、跳转语句

跳转语句用于改变代码的正常执行流程

JavaScript 提供了 break 语句、continue 语句和 return 语句

- break 语句用于立即终止当前循环（for、while、do...while、for...of、for...in）或 switch 语句的执行，并将控制权转移到紧随被终止语句之后的语句
- continue 语句用于终止当前循环迭代的执行，并将控制权转移到下一次循环迭代的开始
- return 语句用于从函数中返回值，并终止函数的执行

```JavaScript
// break 语句
for (let i = 0; i < 10; i++) {
  if (i === 5) {
    break; // 当 i 等于 5 时，终止循环
  }
  console.log(i); // 输出 0, 1, 2, 3, 4
}

// continue 语句
for (let i = 0; i < 5; i++) {
  if (i === 3) {
    continue; // 当 i 等于 3 时，跳过本次循环
  }
  console.log(i); // 输出 0, 1, 2, 4
}

// return 语句
function sum(a, b) {
  return a + b; // 返回 a 和 b 的和，并终止函数执行
}
let result = sum(5, 3);
console.log(result); // 输出 8
```



# 10、函数

## 1、函数定义

JavaScript 提供了多种定义函数的方式，函数声明使用 function 关键字后跟函数名和参数列表，以及包含函数体的花括号

- 函数声明会被提升到其所在作用域的顶部，因此可以在声明之前调用
- 函数表达式是将一个匿名或命名的函数赋值给一个变量
- 箭头函数是ES6中引入的一种更简洁的函数定义语法，它没有自己的 this 绑定，而是继承自外层作用域的 this

```JavaScript
// 函数声明
function add(a, b) {
  return a + b;
}

// 函数表达式（匿名函数）
const multiply = function(a, b) {
  return a * b;
};

// 函数表达式（命名函数）
const divide = function divideNumbers(a, b) {
  return a / b;
};

// 箭头函数
const subtract = (a, b) => a - b;

console.log(add(5, 2)); // 输出 7
console.log(multiply(5, 2)); // 输出 10
console.log(divide(10, 2)); // 输出 5
console.log(subtract(5, 2)); // 输出 3
```



## 2、函数调用

要调用一个函数，需要在函数名后加上一对圆括号()，如果函数定义了参数，可以在调用时将实际的值（称为参数）传递给函数

```JavaScript
function greet(name) {
  console.log("Hello, " + name + "!");
}

greet("Alice"); // 调用 greet 函数，并传递参数 "Alice"
```



## 3、函数参数

函数可以定义参数，这些参数是在函数定义时声明的占位符，用于接收函数被调用时传递进来的实际值（参数）

ES6 引入了默认参数，允许在函数定义时为参数指定默认值，当调用函数时没有传递相应的参数或者传递的值是 undefined 时，会使用默认值

ES6 还引入了剩余参数，允许将不定数量的参数表示为一个数组

```JavaScript
// 带有默认参数的函数
function power(base, exponent = 2) {
  return Math.pow(base, exponent);
}

console.log(power(3)); // 输出 9 (exponent 使用默认值 2)
console.log(power(3, 3)); // 输出 27 (exponent 使用传递的值 3)

// 带有剩余参数的函数
function sumAll(...numbers) {
  let total = 0;
  for (let number of numbers) {
    total += number;
  }
  return total;
}

console.log(sumAll(1, 2, 3)); // 输出 6
console.log(sumAll(10, 20, 30, 40)); // 输出 100
```



## 4、返回值

函数可以使用 return 语句返回一个值，当执行到 return 语句时，函数会停止执行，并将指定的值返回给调用者，如果函数没有显式地使用 return 语句返回值，或者 return 语句后面没有跟任何值，那么函数将隐式地返回 undefined

返回值使得函数能够产生结果并在程序的其他地方使用

```JavaScript
function multiply(a, b) {
  return a * b;
}

let result = multiply(4, 5);
console.log(result); // 输出 20

function noReturnValue() {
  console.log("This function does not return anything explicitly.");
}

let undefinedResult = noReturnValue();
console.log(undefinedResult); // 输出 undefined
```



## 5、作用域与闭包

函数拥有自己的作用域，在函数内部声明的变量只能在该函数内部访问，这称为函数作用域，JavaScript 还具有词法作用域，这意味着函数可以访问其定义时所在的外部（包含）作用域中的变量

闭包是一种特殊的现象，指的是内部函数可以访问并记住其外部（封闭）函数作用域中的变量，即使在外部函数执行完毕后，内部函数仍然可以访问这些变量

```JavaScript
// 函数作用域
function outerFunction() {
  let outerVar = "I am from outer function";
  function innerFunction() {
    let innerVar = "I am from inner function";
    console.log(outerVar); // innerFunction 可以访问 outerVar
  }
  innerFunction();
  // console.log(innerVar); // 错误: innerVar 在 outerFunction 中不可访问
}
outerFunction();

// 闭包
function createCounter() {
  let count = 0;
  return function() {
    count++;
    console.log(count);
  };
}

const counter1 = createCounter();
counter1(); // 输出 1
counter1(); // 输出 2

const counter2 = createCounter();
counter2(); // 输出 1 (count 变量是相互独立的)
```



## 6、this 关键字

 this 关键字在 JavaScript 函数中具有特殊的含义，this 的值取决于函数被调用的方式

- 在全局作用域中，this 通常指向全局对象（在浏览器中是 window 对象）
- 在普通函数中，this 的值取决于函数的调用方式
  - 作为独立函数调用时，this 通常指向全局对象
  - 作为对象的方法调用时，this 指向该对象
- 箭头函数没有自己的 this 绑定，它们会捕获其所在词法作用域的 this 值

可以使用 call()、apply() 和 bind() 方法来显式地设置函数调用时 this 的值

```JavaScript
// 全局作用域中的 this
console.log(this === window); // 在浏览器中输出 true

// 普通函数中的 this
function showThis() {
  console.log(this);
}
showThis(); // 在浏览器中输出 Window 对象 (非严格模式)

const myObject = {
  value: 10,
  getValue: function() {
    console.log(this.value); // this 指向 myObject
  }
};
myObject.getValue(); // 输出 10

// 箭头函数中的 this
const anotherObject = {
  value: 20,
  getValueArrow: () => {
    console.log(this.value); // this 继承自全局作用域，在浏览器中通常是 undefined (严格模式) 或 Window 对象 (非严格模式)
  }
};
anotherObject.getValueArrow();

// 使用 call() 方法改变 this 的指向
function greetPerson(greeting) {
  console.log(greeting + ", " + this.name);
}
const person1 = { name: "David" };
greetPerson.call(person1, "Hello"); // 输出 Hello, David
```



# 11、面向对象

## 1、对象

对象是 JavaScript 中一种基本的数据结构，它是一个无序的属性集合，每个属性由一个键（通常是字符串或 Symbol）和一个值组成

对象用于表示现实世界中的实体，可以包含描述该实体的属性和可以对该实体执行的操作（方法）

```JavaScript
// 使用对象字面量创建一个对象
const car = {
  brand: "Toyota",
  model: "Camry",
  year: 2023,
  start: function() {
    console.log("Car started.");
  }
};

console.log(car.brand); // 输出 "Toyota"
car.start(); // 输出 "Car started."
```



## 2、创建对象

JavaScript 提供了多种创建对象的方式，最常见的方式是使用对象字面量，即使用一对花括号 {} 来定义一个对象，并在其中指定属性的键值对，另一种方式是使用构造函数，首先定义一个函数作为对象的“蓝图”，然后使用 new 关键字来创建该函数的实例（对象）

ES5 引入了 Object.create() 方法，它允许创建一个新对象，并指定其原型对象

```JavaScript
// 对象字面量
const person1 = { name: "Alice", age: 30 };

// 构造函数
function Person(name, age) {
  this.name = name;
  this.age = age;
  this.greet = function() {
    console.log("Hello, my name is " + this.name + " and I am " + this.age + " years old.");
  };
}
const person2 = new Person("Bob", 25);
person2.greet(); // 输出 "Hello, my name is Bob and I am 25 years old."

// Object.create()
const proto = {
  greet: function() {
    console.log("Hello from prototype!");
  }
};
const person3 = Object.create(proto);
person3.greet(); // 输出 "Hello from prototype!"
```



## 3、访问和修改属性

可以使用点号.或方括号来访问对象的属性，当属性名是有效的 JavaScript 标识符且不是保留字时，可以使用点号表示法，当属性名是字符串（包含空格或特殊字符）或者需要使用变量来访问属性时，需要使用方括号表示法

可以通过给对象添加新的键值对来添加新的属性，也可以通过为已有的属性赋值来修改属性的值

可以使用 delete 运算符来删除对象的属性

```JavaScript
const myBook = {
  title: "The Great Gatsby",
  author: "F. Scott Fitzgerald",
  "publication year": 1925
};

// 使用点号访问属性
console.log(myBook.title); // 输出 "The Great Gatsby"

// 使用方括号访问属性 (当属性名包含空格或使用变量时)
console.log(myBook["publication year"]); // 输出 1925
const yearKey = "publication year";
console.log(myBook[yearKey]); // 输出 1925

// 添加新属性
myBook.genre = "Novel";
console.log(myBook.genre); // 输出 "Novel"

// 修改属性值
myBook.year = 1926;
console.log(myBook.year); // 输出 1926

// 删除属性
delete myBook.author;
console.log(myBook.author); // 输出 undefined
```



## 4、原型继承

在 JavaScript 中，每个对象都有一个原型（prototype），原型是另一个对象，当前对象会从其原型继承属性和方法

原型本身也可以有自己的原型，这样就形成了一个原型链，当试图访问一个对象的属性时，如果该对象自身没有这个属性，JavaScript 引擎会沿着原型链向上查找，直到找到该属性或者到达原型链的末端（通常是 Object.prototype）

Object.prototype 是所有 JavaScript 对象的最终原型，可以通过修改对象的原型来添加或覆盖继承的属性，原型继承是JavaScript实现对象之间共享属性和方法的主要机制

```JavaScript
function Animal(name) {
  this.name = name;
}

Animal.prototype.sayHello = function() {
  console.log(this.name + " makes a sound.");
};

function Dog(name, breed) {
  Animal.call(this, name); // 调用父构造函数
  this.breed = breed;
}

// 设置 Dog 的原型为 Animal 的实例，实现继承
Dog.prototype = Object.create(Animal.prototype);
Dog.prototype.constructor = Dog; // 修复构造函数指向

Dog.prototype.bark = function() {
  console.log("Woof!");
};

const myDog = new Dog("Buddy", "Golden Retriever");
myDog.sayHello(); // 输出 "Buddy makes a sound." (继承自 Animal)
myDog.bark(); // 输出 "Woof!" (Dog 自身的方法)

console.log(myDog instanceof Animal); // 输出 true
console.log(myDog instanceof Dog); // 输出 true
```



## 5、类

ES6 引入了 class 关键字，提供了一种更接近传统面向对象语言的语法来定义类，然而，需要理解的是，JavaScript 中的类本质上仍然是基于原型继承的语法糖

可以使用 class 关键字定义一个类，类中可以包含构造函数（constructor）、方法和属性，可以使用 new 关键字来创建类的实例（对象）

```JavaScript
class Rectangle {
  constructor(width, height) {
    this.width = width;
    this.height = height;
  }

  area() {
    return this.width * this.height;
  }
}

const rect = new Rectangle(10, 5);
console.log(rect.area()); // 输出 50
```



## 6、继承与多态

可以使用 extends 关键字来实现类之间的继承，从而创建一个新的类（子类），该子类继承了另一个类（父类或超类）的属性和方法

在子类的构造函数中，可以使用 super 关键字来调用父类的构造函数，子类可以重写（override）父类的方法，实现多态性，即相同的操作在不同的对象上可以产生不同的行为

```JavaScript
class Shape {
  constructor(color) {
    this.color = color;
  }

  draw() {
    console.log("Drawing a shape with color " + this.color);
  }
}

class Circle extends Shape {
  constructor(color, radius) {
    super(color); // 调用父类的构造函数
    this.radius = radius;
  }

  draw() {
    super.draw(); // 调用父类的 draw 方法
    console.log("Drawing a circle with radius " + this.radius);
  }

  area() {
    return Math.PI * this.radius * this.radius;
  }
}

const myCircle = new Circle("blue", 5);
myCircle.draw();
console.log("Area: " + myCircle.area());
```



## 7、Getter 和 Setter

Getter 方法使用 get 关键字定义，用于获取对象属性的值

Setter 方法使用 set 关键字定义，用于设置对象属性的值

Getter 和 Setter 提供了一种控制对象属性访问和修改的方式，可以用来实现封装，即隐藏对象的内部状态，并通过定义好的接口进行交互

```JavaScript
class Person {
  constructor(firstName, lastName) {
    this._firstName = firstName; // 使用下划线表示这是一个“私有”属性 (约定)
    this.lastName = lastName;
  }

  get fullName() {
    return this._firstName + " " + this.lastName;
  }

  set firstName(newFirstName) {
    this._firstName = newFirstName;
  }
}

const person = new Person("John", "Doe");
console.log(person.fullName); // 输出 "John Doe" (调用 getter)

person.firstName = "Jane"; // 调用 setter
console.log(person.fullName); // 输出 "Jane Doe"
```



# 12、处理数组和集合

## 1、数组创建

在 JavaScript 中，可以使用数组字面量（用方括号括起来，元素之间用逗号分隔）或者使用 Array 构造函数来创建数组，数组是一种特殊的对象，用于存储有序的数据集合

```JavaScript
// 使用数组字面量创建数组
const numbers = [16, 3, 5, 17, 29];
const fruits = ["apple", "banana", "orange"];

// 使用 Array 构造函数创建数组
const emptyArray = new Array();
const anotherNumbers = new Array(10, 20, 30);
```



## 2、数组访问和修改

可以使用索引（从 0 开始）通过方括号来访问数组中的元素，也可以通过索引来修改数组中的元素，即为指定索引位置的元素赋新的值

数组的 length 属性可以获取或设置数组中元素的个数

```JavaScript
const colors = ["red", "green", "blue"];

console.log(colors); // 输出 "red" (访问索引为 0 的元素)

colors[16] = "yellow"; // 修改索引为 1 的元素
console.log(colors); // 输出 ["red", "yellow", "blue"]

console.log(colors.length); // 输出 3 (获取数组长度)

colors.length = 5; // 设置数组长度 (会添加空位)
console.log(colors); // 输出 ["red", "yellow", "blue", empty × 2]
```



## 3、常用数组方法

JavaScript 提供了许多常用的数组方法来操作数组

- push() 方法用于在数组末尾添加一个或多个元素
- pop() 方法用于移除并返回数组的最后一个元素
- shift() 方法用于移除并返回数组的第一个元素
- unshift()方法用于在数组开头添加一个或多个元素
- slice() 方法用于提取数组的一部分并返回一个新数组
- splice() 方法用于通过删除或替换现有元素来修改数组
- map() 方法创建一个新数组，其结果是该数组中每个元素都调用一个提供的函数后返回的结果
- filter() 方法创建一个新数组，其中包含通过所提供函数实现的测试的所有元素
- reduce() 方法对数组中的每个元素执行一个由您提供的 reducer 函数(升序执行)，将其结果汇总为单个返回值
- forEach() 方法对数组的每个元素执行一次给定的函数
- concat() 用于连接两个或多个数组
- join() 用于将数组的所有元素连接成一个字符串
- indexOf() 和 lastIndexOf() 用于查找指定元素在数组中首次或最后一次出现的索引
- includes() 用于判断数组是否包含一个指定的值
- find() 和 findIndex() 用于查找满足条件的第一个元素或其索引
- sort() 用于对数组的元素进行排序
- reverse() 用于颠倒数组中元素的顺序

```JavaScript
const numbers = [16, 3, 5];

numbers.push(4);
console.log(numbers); // 输出 [16, 3, 5, 17]

numbers.pop();
console.log(numbers); // 输出 [16, 3, 5]

numbers.unshift(0);
console.log(numbers); // 输出 

numbers.shift();
console.log(numbers); // 输出 [16, 3, 5]

const sliced = numbers.slice(1, 3);
console.log(sliced); // 输出 [3, 5]

numbers.splice(1, 1, 5); // 从索引 1 开始删除 1 个元素，并插入 5
console.log(numbers); // 输出 [16, 29, 5]

const doubled = numbers.map(num => num * 2);
console.log(doubled); // 输出 [3, 22, 2]

const even = numbers.filter(num => num % 2 === 0);
console.log(even); // 输出 (因为 numbers 现在是 [16, 29, 5])

const sum = numbers.reduce((acc, curr) => acc + curr, 0);
console.log(sum); // 输出 9

numbers.forEach(num => console.log(num)); // 输出 1, 5, 3

const moreNumbers = [2, 6];
const combined = numbers.concat(moreNumbers);
console.log(combined); // 输出 [16, 29, 5, 2, 6]

console.log(combined.join("-")); // 输出 "1-5-3-6-7"

console.log(combined.indexOf(5)); // 输出 1

console.log(combined.includes(7)); // 输出 true

const found = combined.find(num => num > 5);
console.log(found); // 输出 6

const foundIndex = combined.findIndex(num => num > 5);
console.log(foundIndex); // 输出 3

const sorted = combined.sort((a, b) => a - b);
console.log(sorted); // 输出 [16, 5, 29, 2, 6]

const reversed = sorted.reverse();
console.log(reversed); // 输出 [6, 2, 29, 5, 16]
```



## 4、Set 集合

ES6 引入了两种新的集合类型：Set 和 Map

Set 是一种存储唯一值的集合，可以使用 new Set() 创建 Set 对象，由于Set只存储唯一值，因此常用于去除数组中的重复元素

- add() 方法用于向 Set 中添加元素
- has() 方法用于检查 Set 中是否存在某个元素
- delete() 方法用于从 Set 中移除某个元素
- size 属性返回 Set 中元素的个数
- forEach() 方法或 for...of 循环来迭代 Set 中的元素

```JavaScript
const mySet = new Set();

mySet.add(1);
mySet.add(2);
mySet.add(2); // 重复添加，不会生效
mySet.add("hello");

console.log(mySet.size); // 输出 3

console.log(mySet.has(2)); // 输出 true

mySet.delete(1);
console.log(mySet.has(1)); // 输出 false

mySet.forEach(value => console.log(value)); // 输出 2, "hello"

for (let item of mySet) {
  console.log(item); // 输出 2, "hello"
}

// 使用 Set 去除数组中的重复元素
const numbersWithDuplicates = [1, 2, 2, 3, 4, 4, 5];
const uniqueNumbers =;
console.log(uniqueNumbers); // 输出 [16, 3, 5, 17, 29]
```



## 5、Map集合

Map 是一种存储键值对的集合，与对象不同的是，Map 的键可以是任何数据类型，包括对象和原始值，可以使用 new Map() 创建 Map 对象

- set() 方法用于向 Map 中添加键值对
- get() 方法用于通过键获取值
- has() 方法用于检查 Map 中是否存在某个键
- delete() 方法用于移除指定键的键值对
- clear() 方法用于清空 Map
- size 属性返回 Map 中键值对的个数
- forEach() 方法、for...of 循环以及 keys()、values() 和 entries() 方法来迭代 Map 中的键、值或键值对

```JavaScript
const myMap = new Map();

const key1 = "string key";
const key2 = {};
const key3 = function() {};

myMap.set(key1, "value associated with string key");
myMap.set(key2, "value associated with object key");
myMap.set(key3, "value associated with function key");

console.log(myMap.size); // 输出 3

console.log(myMap.get(key1)); // 输出 "value associated with string key"
console.log(myMap.has(key2)); // 输出 true

myMap.delete(key1);
console.log(myMap.has(key1)); // 输出 false

myMap.forEach((value, key) => console.log(key, value));
// 输出:
// {} "value associated with object key"
// function () {} "value associated with function key"

for (let [key, value] of myMap) {
  console.log(key, value);
}

for (let key of myMap.keys()) {
  console.log(key);
}

for (let value of myMap.values()) {
  console.log(value);
}

for (let entry of myMap.entries()) {
  console.log(entry, entry[16]);
}
```



## 6、WeakSet 和 WeakMap

WeakSet 和 WeakMap 是 ES6 中新增的另外两种集合类型，它们与 Set 和 Map 的主要区别在于它们存储的是对象的“弱引用”，这意味着如果存储在 WeakSet 或 WeakMap 中的对象只被这些集合引用，那么当垃圾回收机制运行时，这些对象仍然会被回收，而不会阻止垃圾回收的进行

- WeakSet 只存储唯一的对象，add()、has() 和 delete() 是其主要方法
- WeakMap 存储键值对，但键必须是对象，set()、get()、has()  和delete() 是其主要方法

由于弱引用的特性，WeakSet 和 WeakMap 没有 size 属性，也不能被迭代，它们常用于存储与对象相关的附加信息，而又不希望这些信息阻止对象的垃圾回收

```JavaScript
// WeakSet 示例
const ws = new WeakSet();
let obj1 = {};
let obj2 = {};

ws.add(obj1);
ws.add(obj2);

console.log(ws.has(obj1)); // 输出 true

obj1 = null; // 解除对 obj1 的引用
// 此时 obj1 可能会被垃圾回收，WeakSet 中的引用也会失效

// WeakMap 示例
const wm = new WeakMap();
let key1 = {};
let key2 = {};

wm.set(key1, "value1");
wm.set(key2, "value2");

console.log(wm.get(key1)); // 输出 "value1"

key1 = null; // 解除对 key1 的引用
// 此时 key1 可能会被垃圾回收，WeakMap 中对应的键值对也会被移除
```



# 13、DOM和事件

## 1、DOM 简介

文档对象模型（Document Object Model，DOM）是 Web 页面的编程接口，它将 HTML 或 XML 文档表示为一棵由节点组成的树形结构，每个节点代表文档中的一个元素、属性或文本

通过 DOM，JavaScript 可以访问和操作 Web 页面的内容、结构和样式

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>DOM Example</title>
</head>
<body>
  <div id="container">
    <h1 class="main-title">Welcome</h1>
    <p>This is a paragraph.</p>
  </div>
  <script>
    // JavaScript 代码将在这里操作 DOM
  </script>
</body>
</html>
```



## 2、选择 DOM 元素

JavaScript 提供了多种方法来选择 DOM 中的元素，getElementById() 方法通过元素的唯一 ID 来选取元素

- getElementsByTagName() 方法返回具有指定标签名的所有元素的集合
- getElementsByClassName() 方法返回具有指定类名的所有元素的集合
- querySelector() 方法返回匹配指定 CSS 选择器的第一个元素
- querySelectorAll() 方法返回匹配指定 CSS 选择器的所有元素的集合

```JavaScript
// 获取 ID 为 "container" 的元素
const container = document.getElementById("container");

// 获取所有标签名为 "p" 的元素
const paragraphs = document.getElementsByTagName("p");

// 获取所有类名为 "main-title" 的元素
const titles = document.getElementsByClassName("main-title");

// 使用 CSS 选择器获取第一个匹配的元素
const firstTitle = document.querySelector("#container.main-title");

// 使用 CSS 选择器获取所有匹配的元素
const allParagraphs = document.querySelectorAll("#container p");

console.log(container);
console.log(paragraphs);
console.log(titles);
console.log(firstTitle);
console.log(allParagraphs);
```



## 3、操作 DOM 元素

一旦选中了 DOM 元素，就可以使用 JavaScript 来操作它们

可以使用 getAttribute() 和 setAttribute() 方法来访问和修改 HTML 元素的属性

可以直接访问和修改 DOM 元素的属性，例如 innerHTML 用于获取或设置元素内部的 HTML 内容，textContent 用于获取或设置元素的文本内容，className 用于获取或设置元素的类名。使用元素的 style 属性来直接修改元素的 CSS 样式

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>DOM Manipulation</title>
</head>
<body>
  <img id="myImage" src="placeholder.jpg" alt="Placeholder">
  <div id="content">Some text here.</div>
  <script>
    const image = document.getElementById("myImage");
    const contentDiv = document.getElementById("content");

    // 获取和设置属性
    console.log(image.getAttribute("src")); // 输出 "placeholder.jpg"
    image.setAttribute("src", "new-image.jpg");
    image.setAttribute("alt", "New Image");

    // 修改 innerHTML 和 textContent
    contentDiv.innerHTML = "<h1>New Heading</h1><p>Updated content.</p>";
    // contentDiv.textContent = "Only text content."; // 如果只想设置文本内容

    // 修改类名
    contentDiv.className = "updated-content";

    // 修改样式
    contentDiv.style.color = "blue";
    contentDiv.style.backgroundColor = "#f0f0f0";
  </script>
</body>
</html>
```



## 4、动态创建和移除元素

JavaScript 允许动态地创建和移除 DOM 元素

- document.createElement() 方法创建一个新的 HTML 元素
- appendChild() 方法将一个元素添加到另一个元素的子元素列表的末尾
- insertBefore() 方法将一个元素插入到另一个元素的指定子元素之前
- removeChild() 方法从 DOM 中移除一个子元素

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>Dynamic DOM</title>
</head>
<body>
  <div id="parent"></div>
  <script>
    const parentElement = document.getElementById("parent");

    // 创建一个新的 <p> 元素
    const newParagraph = document.createElement("p");
    newParagraph.textContent = "This is a dynamically created paragraph.";

    // 将新元素添加到父元素的末尾
    parentElement.appendChild(newParagraph);

    // 创建另一个新的 <span> 元素
    const newSpan = document.createElement("span");
    newSpan.textContent = "This is a span.";

    // 将 span 元素插入到第一个子元素之前 (如果存在)
    if (parentElement.firstChild) {
      parentElement.insertBefore(newSpan, parentElement.firstChild);
    } else {
      parentElement.appendChild(newSpan);
    }

    // 移除第一个子元素
    if (parentElement.firstChild) {
      parentElement.removeChild(parentElement.firstChild);
    }
  </script>
</body>
</html>
```



## 5、事件处理

事件处理是使 Web 页面具有交互性的关键，可以使用事件监听器来响应用户的操作或其他事件

- addEventListener() 方法用于向 DOM 元素添加事件监听器，需要指定要监听的事件类型和一个回调函数，当指定的事件发生时，回调函数会被执行
- removeEventListener() 方法来移除已添加的事件监听器

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>Event Handling</title>
</head>
<body>
  <button id="myButton">Click Me</button>
  <script>
    const button = document.getElementById("myButton");

    function handleClick() {
      console.log("Button clicked!");
    }

    // 添加事件监听器
    button.addEventListener("click", handleClick);

    // 移除事件监听器 (如果需要)
    // button.removeEventListener("click", handleClick);
  </script>
</body>
</html>
```



## 6、常见 DOM 事件

常见的 DOM 事件包括

- 鼠标事件（如 click、mouseover、mouseout、mousemove 等）
- 键盘事件（如keydown、keyup、keypress）
- 表单事件（如submit、change、input、focus、blur）
- 文档/窗口事件（如load、DOMContentLoaded、resize、scroll）

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>Common Events</title>
</head>
<body>
  <div id="hoverMe" style="width: 100px; height: 100px; background-color: lightblue;">Hover Me</div>
  <input type="text" id="myInput" placeholder="Type something">
  <script>
    const hoverDiv = document.getElementById("hoverMe");
    const inputField = document.getElementById("myInput");

    hoverDiv.addEventListener("mouseover", () => {
      console.log("Mouse over!");
      hoverDiv.style.backgroundColor = "lightblue";
    });

    hoverDiv.addEventListener("mouseout", () => {
      console.log("Mouse out!");
      hoverDiv.style.backgroundColor = "lightgreen";
    });

    inputField.addEventListener("input", (event) => {
      console.log("Input changed:", event.target.value);
    });
  </script>
</body>
</html>
```



## 7、事件流

DOM 事件流分为三个阶段：捕获阶段、目标阶段和冒泡阶段

- 在捕获阶段，事件从文档根节点向下传播到目标元素
- 在目标阶段，事件到达目标元素
- 在冒泡阶段，事件从目标元素向上冒泡到文档根节点

大多数事件默认在冒泡阶段被处理

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>Event Flow</title>
  <style>
    #outer {
      background-color: yellow;
      padding: 20px;
    }
    #inner {
      background-color: lightgreen;
      padding: 20px;
    }
  </style>
</head>
<body>
  <div id="outer">
    Outer
    <div id="inner">
      Inner
      <button id="myButton">Click Me</button>
    </div>
  </div>
  <script>
    const outerDiv = document.getElementById("outer");
    const innerDiv = document.getElementById("inner");
    const button = document.getElementById("myButton");

    // 捕获阶段的监听器
    outerDiv.addEventListener("click", () => console.log("Outer div clicked (capturing)"), true);
    innerDiv.addEventListener("click", () => console.log("Inner div clicked (capturing)"), true);
    button.addEventListener("click", () => console.log("Button clicked (capturing)"), true);

    // 冒泡阶段的监听器 (默认)
    outerDiv.addEventListener("click", () => console.log("Outer div clicked (bubbling)"));
    innerDiv.addEventListener("click", () => console.log("Inner div clicked (bubbling)"));
    button.addEventListener("click", () => console.log("Button clicked (bubbling)"));
  </script>
</body>
</html>
```



## 8、事件委托

事件委托是一种利用事件冒泡的特性来处理多个相似元素的事件的技术，通过将一个事件监听器添加到它们的父元素上，可以统一处理所有子元素的相应事件，这种方法可以提高性能，尤其是在处理大量动态添加的元素时

```HTML
<!DOCTYPE html>
<html>
<head>
  <title>Event Delegation</title>
</head>
<body>
  <ul id="item-list">
    <li data-item="1">Item 1</li>
    <li data-item="2">Item 2</li>
    <li data-item="3">Item 3</li>
  </ul>
  <script>
    const itemList = document.getElementById("item-list");

    itemList.addEventListener("click", (event) => {
      if (event.target.tagName === "LI") {
        const itemNumber = event.target.dataset.item;
        console.log("Clicked on item:", itemNumber);
      }
    });

    // 假设稍后动态添加了新的列表项
    const newItem = document.createElement("li");
    newItem.dataset.item = "4";
    newItem.textContent = "Item 4";
    itemList.appendChild(newItem); // 这个新的列表项的点击事件也会被上面的监听器处理
  </script>
</body>
</html>
```



# 14、异步和  Promise

## 1、异步操作的概念

异步操作是指不会立即完成的操作，它们通常需要一些时间才能返回结果，例如网络请求、文件读取、定时器等，与同步操作不同，异步操作不会阻塞程序的执行，允许程序在等待异步操作完成的同时继续执行其他任务

```JavaScript
// 同步操作
console.log("Start");
let result = performSynchronousTask(); // 假设这是一个耗时的同步函数
console.log("Result:", result);
console.log("End");

// 异步操作 (使用 setTimeout 模拟)
console.log("Start of async");
setTimeout(() => {
  let asyncResult = performAsyncTask(); // 假设这是一个耗时的异步函数
  console.log("Async Result:", asyncResult);
}, 2000); // 延迟 2 秒执行
console.log("End of async");

function performSynchronousTask() {
  // 模拟耗时操作
  for (let i = 0; i < 1000000000; i++) {}
  return "Synchronous task done";
}

function performAsyncTask() {
  return "Asynchronous task done";
}
```



## 2、回调函数

回调函数是处理异步操作结果的一种常见方式，当异步操作完成时，会调用预先定义好的回调函数来处理结果，然而，当存在多个依赖彼此的异步操作时，回调函数可能会嵌套得很深，形成所谓的“回调地狱”（Callback Hell），这使得代码难以阅读和维护

```JavaScript
function fetchData(url, callback) {
  // 模拟异步请求
  setTimeout(() => {
    const data = `Data from ${url}`;
    callback(null, data); // 成功时调用回调
  }, 1000);
}

fetchData("api/data1", (error, result1) => {
  if (error) {
    console.error("Error fetching data 1:", error);
    return;
  }
  console.log("Data 1:", result1);
  fetchData("api/data2", (error, result2) => {
    if (error) {
      console.error("Error fetching data 2:", error);
      return;
    }
    console.log("Data 2:", result2);
    fetchData("api/data3", (error, result3) => {
      if (error) {
        console.error("Error fetching data 3:", error);
        return;
      }
      console.log("Data 3:", result3);
      //... 更多嵌套的回调
    });
  });
});
```



## 3、Promise

Promise 是一种代表异步操作最终完成（或失败）及其结果值的对象

Promise 有三种状态：pending（等待中）、fulfilled（已完成，也称为resolved）和rejected（已拒绝）

可以使用 Promise 构造函数创建一个 Promise 对象，Promise 提供了一种更结构化和可管理的方式来处理异步操作

```JavaScript
function fetchDataPromise(url) {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const data = `Data from ${url}`;
      // 模拟成功或失败
      if (url.includes("success")) {
        resolve(data);
      } else {
        reject("Failed to fetch data from " + url);
      }
    }, 1000);
  });
}

fetchDataPromise("api/success/data1")
.then(result => console.log("Data 1:", result))
.catch(error => console.error("Error:", error));

fetchDataPromise("api/fail/data2")
.then(result => console.log("Data 2:", result))
.catch(error => console.error("Error:", error));
```



## 4、Promise 链

Promise 支持链式调用，可以将多个异步操作按顺序连接起来，使得异步代码的逻辑更加清晰

- .then() 方法来处理 Promise 成功完成后的结果
- .catch() 方法用于处理 Promise 被拒绝（发生错误）的情况
- .finally() 方法用于指定在 Promise 完成（无论是成功还是失败）后始终执行的回调

```JavaScript
fetchDataPromise("api/success/data1")
.then(result1 => {
    console.log("Data 1:", result1);
    return fetchDataPromise("api/success/data2"); // 返回一个新的 Promise
  })
.then(result2 => {
    console.log("Data 2:", result2);
    return fetchDataPromise("api/fail/data3"); // 返回一个会失败的 Promise
  })
.then(result3 => {
    console.log("Data 3:", result3); // 这部分不会执行，因为上一个 Promise 失败了
  })
.catch(error => {
    console.error("Error in the chain:", error); // 捕获链中任何地方发生的错误
  })
.finally(() => {
    console.log("Promise chain completed (success or failure)."); // 无论成功与否都会执行
  });
```



## 5、Promise API

Promise API 提供了一些实用的方法来处理多个Promise

- Promise.all() 接收一个 Promise 数组，当所有 Promise 都成功完成时，返回一个新的 Promise，该 Promise 的结果是包含所有 Promise 结果的数组，如果任何一个 Promise 被拒绝，则返回的 Promise 也会立即被拒绝
- Promise.race() 也接收一个 Promise 数组，一旦数组中的某个 Promise 完成（无论是成功还是失败），就返回一个新的 Promise，该Promise 的结果与第一个完成的Promise的结果相同
- Promise.allSettled() 接收一个 Promise 数组，当所有 Promise 都完成（无论是成功还是失败）时，返回一个新的 Promise，该 Promise的结果是一个包含每个 Promise 结果状态的数组
- Promise.any() 接收一个 Promise 数组，只要其中一个 Promise 成功完成，就返回一个新的 Promise，该 Promise 的结果是第一个成功完成的 Promise 的结果，如果所有 Promise 都被拒绝，则返回的 Promise 会被拒绝，并抛出一个 AggregateError

```JavaScript
const promise1 = Promise.resolve("Promise 1 resolved");
const promise2 = new Promise((resolve) => setTimeout(() => resolve("Promise 2 resolved"), 1500));
const promise3 = Promise.reject("Promise 3 rejected");

Promise.all([promise1, promise2])
.then(results => console.log("All resolved:", results))
.catch(error => console.error("All error:", error)); // 如果有任何一个 Promise 被拒绝，这里会被调用

Promise.race([promise1, promise2, promise3])
.then(result => console.log("Race winner:", result)) // promise1 最先完成
.catch(error => console.error("Race error:", error));

Promise.allSettled([promise1, promise2, promise3])
.then(results => console.log("All settled:", results));
// 输出:
// [
//   { status: 'fulfilled', value: 'Promise 1 resolved' },
//   { status: 'fulfilled', value: 'Promise 2 resolved' },
//   { status: 'rejected', reason: 'Promise 3 rejected' }
// ]

Promise.any([promise3, promise1, promise2])
.then(result => console.log("Any winner:", result)) // promise1 最先成功
.catch(error => console.error("Any error:", error));
```



## 6、async/await

async 和 await 是 ES2017 引入的用于简化异步代码的关键字

- async 关键字用于声明一个异步函数，异步函数总是返回一个 Promise
- await 关键字只能在 async 函数内部使用，它会暂停 async 函数的执行，直到一个 Promise 被解决（fulfilled）或被拒绝，这使得异步代码看起来更像同步代码，更容易理解和编写

可以使用 try...catch 语句来处理 async 函数中可能发生的错误

```JavaScript
async function fetchDataAsync() {
  try {
    const result1 = await fetchDataPromise("api/success/data1");
    console.log("Async Data 1:", result1);
    const result2 = await fetchDataPromise("api/success/data2");
    console.log("Async Data 2:", result2);
    const result3 = await fetchDataPromise("api/fail/data3"); // 这会抛出一个错误
    console.log("Async Data 3:", result3); // 这行不会执行
  } catch (error) {
    console.error("Async Error:", error);
  } finally {
    console.log("Async operation completed.");
  }
}

fetchDataAsync();
```



## 7、事件循环

JavaScript 的事件循环是一种机制，用于处理异步任务和事件，而不会阻塞主线程的执行，JavaScript 是单线程的，这意味着一次只能执行一个任务

事件循环通过维护一个调用栈和一个事件队列来实现并发，当遇到异步任务时，它们会被添加到事件队列中，当调用栈为空时，事件循环会将事件队列中的任务移动到调用栈中执行

微任务（microtask）和宏任务（macrotask）是事件队列中不同类型的任务，它们的执行优先级不同



# 15、模块化

## 1、模块化概念

代码模块化是将代码分割成独立、可重用的模块的过程，模块化有助于代码的组织、重用、维护，并可以避免命名冲突，在 JavaScript 的发展历程中，出现了多种模块化系统



## 2、CommonJS

 CommonJS 是 Node.js 环境中广泛使用的模块化系统，在 CommonJS 中，可以使用 require() 函数来导入其他模块，使用 module.exports 对象来导出当前模块的内容

```JavaScript
// moduleA.js (CommonJS 模块)
const message = "Hello from module A";

function greet(name) {
  return `${message}, ${name}!`;
}

module.exports = {
  greet: greet
};

// main.js
const moduleA = require('./moduleA');
console.log(moduleA.greet("User")); // 输出 "Hello from module A, User!"
```



## 3、AMD

异步模块定义（Asynchronous Module Definition，AMD）是一种主要用于浏览器环境的模块化规范，AMD 通过 define() 函数来定义模块，并支持异步加载模块，这对于在网络环境下加载模块非常有用，RequireJS 是 AMD 规范的一个流行实现

```JavaScript
// moduleB.js (AMD 模块)
define(function() {
  const message = "Hello from module B";
  return {
    greet: function(name) {
      return `${message}, ${name}!`;
    }
  };
});

// main.js (使用 RequireJS)
require(, function(moduleB) {
  console.log(moduleB.greet("Guest")); // 输出 "Hello from module B, Guest!"
});
```



## 4、ES模块

ES 模块是 ECMAScript 标准中定义的官方模块化系统，在 ES6 中引入，ES 模块使用 import 关键字来导入其他模块导出的绑定（变量、函数、类），使用 export 关键字来导出当前模块的绑定

ES 模块支持命名导出（export named）和默认导出（export default），还可以使用 import() 表达式进行动态模块加载

ES 模块被现代浏览器和 Node.js（需要进行一些配置）所支持，是现代 JavaScript 开发的首选模块化方案

```JavaScript
// moduleC.js (ES 模块 - 命名导出)
export const message = "Hello from module C";

export function farewell(name) {
  return `Goodbye, ${name}!`;
}

// moduleD.js (ES 模块 - 默认导出)
const greeting = function(name) {
  return `Greetings, ${name}!`;
};

export default greeting;

// main.js (ES 模块)
import { message, farewell } from './moduleC.js';
import defaultGreeting from './moduleD.js';

console.log(message); // 输出 "Hello from module C"
console.log(farewell("Friend")); // 输出 "Goodbye, Friend!"
console.log(defaultGreeting("Developer")); // 输出 "Greetings, Developer!"

// 动态导入
async function loadModule() {
  const moduleE = await import('./moduleE.js');
  console.log(moduleE.default("Dynamic User"));
}
loadModule();

// moduleE.js (ES 模块 - 默认导出)
export default function(name) {
  return `Dynamically imported greeting: Hello, ${name}!`;
}
```



## 5、模块化实践与工具

使用模块化的方式组织和管理 JavaScript 代码有助于构建可扩展和可维护的应用程序，良好的模块化实践包括将相关功能的代码放在同一个模块中，明确模块的依赖关系，以及暴露清晰的模块接口

代码分割（code splitting）是一种优化技术，可以将应用程序的代码分割成更小的块，按需加载，从而提高应用程序的初始加载速度 

模块打包工具（如 Webpack、Parcel）在现代 Web 开发中扮演着重要的角色，它们可以处理模块之间的依赖关系，并将各种资源（包括 JavaScript、CSS、图片等）打包成最终可以在浏览器中运行的 bundle