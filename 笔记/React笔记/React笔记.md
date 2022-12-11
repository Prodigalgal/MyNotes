# 1、React简介

- 用于动态构建用户界面的 JavaScript 库（**只关注于视图**）
- 声明式编码
- 组件化编码
- React Native 编写原生应用
- 高效（优秀的Diffing算法）

**相关类库**：

~~~react
<!-- 引入React核心库 -->
<script type="text/javascript" src="../JS/React.development.JS"></script>

<!-- 引入React-DOM，用于支持React操作DOM -->
<script type="text/javascript" src="../JS/React-DOM.development.JS"></script>

<!-- 引入babel，用于将JSx转为JS -->
<script type="text/javascript" src="../JS/babel.min.JS"></script>
~~~



# 2、虚拟DOM

## 1、两种方式创建

虚拟 DOM 对象最终都会被 React 转换为真实的 DOM

编码时基本只需要操作 React  的虚拟 DOM 相关数据，React 会自动渲染为真实 DOM 变化而更新视界

~~~react
<!-- 准备好一个“容器” -->
<div id="test"></div>

<!-- JSX方式 -->
<script type="text/babel" > /* 此处一定要写babel */
	// 1.创建虚拟DOM
	// 为了显示出多级结构使用（）括起来
	const VDOM = (  /* 此处一定不要写引号，因为不是字符串 */
                    <h1 id="title">
                        <span>Hello,React</span>
                    </h1>
                  )
	// 2.渲染虚拟DOM到页面
	ReactDOM.render(VDOM,document.getElementById('test'))
</script>

<!-- 纯JS方式 -->
<script type="text/javascript" >
    // 1.创建虚拟DOM
    const VDOM = React.createElement('h1',{id:'title'}, React.createElement('span',{},'Hello,React'))
    // 2.渲染虚拟DOM到页面
    ReactDOM.render(VDOM,document.getElementById('test'))
</script>
~~~



## 2、渲染虚拟DOM

语法：ReactDOM.render(virtualDOM, containerDOM)
- 参数一：纯 JS 或 JSX 创建的虚拟 DOM 对象
- 参数二：用来包含虚拟 DOM 元素的真实 DOM 元素对象(一般是一个 div)
- 

作用：

- 将虚拟 DOM 元素渲染到页面中的真实容器 DOM 中显示，也即使用自定义的 JSX 来替换 HTML 中的一个 DOM 节点，便于将 React 集成到应用中，该方法可以被多次调用，加载单个或者多个组件，但是一般在 React 应用中只会调用一次来加载整个组件树



## 3、虚拟VS真实

虚拟 DOM 本质是 Object 类型的对象（一般对象）

虚拟 DOM 比较轻，真实 DOM 比较重，因为虚拟 DOM 是 React 内部在用，无需真实 DOM 上那么多的属性

~~~js
<script type="text/babel" > /* 此处一定要写babel */
		// 1.创建虚拟DOM
		const VDOM = (  /* 此处一定不要写引号，因为不是字符串 */
			<h1 id="title">
				<span>Hello,React</span>
    		</h1>
		)
		// 2.渲染虚拟DOM到页面
		ReactDOM.render(VDOM,document.getElementById('test'))
		// 3.获取真实DOM
		const TDOM = document.getElementById('demo')
		
		console.log('虚拟DOM',VDOM);
		console.log('真实DOM',TDOM);
		console.log(typeof VDOM);
		console.log(VDOM instanceof Object);
</script>
~~~



# 3、React JSX

全称：JavaScript XML

简介：React 定义的一种类似于 XML 的 JS 扩展语法，本质是 React.createElement(component, props, ...children) 方法的语法糖

作用：用来简化创建虚拟 DOM 

语法：

- 只有一个根标签
- 每个标签必须闭合
- 遇到 **<** 开头的代码，以标签的语法解析，HTML 同名标签转换为 HTML 同名元素，其它标签需要特别解析
  - 由标签首字母判断：
    - 若小写字母开头，则将该标签转为 HTML中同名元素，若 HTML中无该标签对应的同名元素，则报错
    - 若大写字母开头，React 就去渲染对应的组件，若组件没有定义，则报错
- 样式类名不要用 class，要用 className
- 内联样式，要用 style={{key:value}} 的形式去写

- 定义虚拟 DOM 时，不要写引号
- 遇到以 **{** 开头的代码，以 JS 语法解析，标签中的 JS 表达式必须用 **{ }** 包含

~~~react
const myId = 'aTest'
const myData = 'HeLlo, React'

const VDOM = (<h2 className="title" id={myId.toLowerCase()}>
                  <span style={{color:'white',fontSize:'29px'}}>{myData.toLowerCase()}</span>
              </h2>)
~~~

babel.js 的作用：

- 浏览器不能直接解析 JSX 代码，需要 babel 转译为纯 JS 的代码才能运行
- 只要用了 JSX，都要加上 type="text/babel"，声明需要 babel 来处理



**注意**：

- 它不是字符串，也不是 HTML/XML 标签
- 它最终产生的就是一个 JS 对象
- 标签名任意: HTML 标签或其它标签
- 标签属性任意: HTML 标签属性或其它



~~~react
<script type="text/babel" >
    const myId = 'aTest'
    const myData = 'HeLlo,React'

    // 1.创建虚拟DOM
    const VDOM = (
    <div>
        <h2 className="title" id={myId.toLowerCase()}>
            <span style={{color:'white',fontSize:'29px'}}>{myData.toLowerCase()}</span>
        </h2>
        <h2 className="title" id={myId.toUpperCase()}>
            <span style={{color:'white',fontSize:'29px'}}>{myData.toLowerCase()}</span>
        </h2>
        <input type="text"/>
    </div>
    )
    // 2.渲染虚拟DOM到页面
    ReactDOM.render(VDOM,document.getElementById('test'))
</script>
~~~



**注意**：

- 一定注意区分：JS 语句(代码) 与 JS 表达式
  - 表达式：一个表达式会产生一个值，可以放在任何一个需要值的地方
    - 这些都是表达式：a、a+b、demo(1)、arr.map()、function test () {}
  - 语句(代码)：
    - 这些都是语句(代码)：if(){}、for(){}、switch(){case:xxxx}



~~~react
<script type="text/babel" >
    // 模拟一些数据
    const data = ['Angular','React','Vue']
    // 1.创建虚拟DOM
    const VDOM = (
    <div>
        <h1>前端JS框架列表</h1>
        <ul>{
                data.map( (item,index) => {return <li key={index} > {item} </li>} )
            }</ul>
    </div>
    )
    // 2.渲染虚拟DOM到页面
    ReactDOM.render(VDOM,document.getElementById('test'))
</script>
~~~



# 4、React组件

## 1、组件概念

作用：用来实现局部功能效果的代码和资源的集合(html/css/JS/image等等)，复用编码，简化项目编码，提高运行效率

流程：
1. React 内部会创建组件实例对象
2. 调用 render() 得到虚拟 DOM，并解析为真实 DOM
3. 插入到指定的页面元素内部



**注意**：

- 组件名必须首字母大写
- 虚拟 DOM 元素只能有一个根元素
- 虚拟 DOM 元素必须有结束标签



~~~react
import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';

// 声明了一个 App 组件，采用的 ES6 的语法
// 此 App 组件可以任意地方创建实例
// 其内的 render 方法包含了所返回的元素
class App extends Component {
    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo" />
                    <h1 className="App-title">Welcome to React</h1>
                </header>
                <p className="App-intro">
                    To get started, edit <code>src/App.js</code> and save to reload.
                </p>
            </div>
        );
    }
}

export default App;
~~~



**注意**：

- 组件、实例、元素的区别
  - 实例由组件实例化
  - 元素是组件的构成



## 2、组件分类

### 1、类式组件

使用 ES6 类组件可以在构造函数中初始化组件的状态，并且需要强制地调用 super(); 方法

extends 所继承的 Component 会注册所有生命周期方法，所有的 Component  API 都可以使用，包括 state 等

~~~react
// 1.创建类式组件
class MyComponent extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        // render 方法 ——> 放在 MyComponent 的原型对象上，供实例使用
        // render 中的 this ——> MyComponent 的实例对象 <=> MyComponent 组件实例对象
        console.log('render中的this:',this);
        return <h2>我是用类定义的组件(适用于【复杂组件】的定义)</h2>
    }
}
// 2.渲染组件到页面
// 2.1.React 解析组件标签，找到了 MyComponent 组件
// 2.2.发现组件是使用类定义的，随后 new 出来该类的实例，并通过该实例调用到原型上的 render 方法
// 2.3.将 render 返回的虚拟 DOM 转为真实 DOM，随后呈现在页面中
ReactDOM.render(<MyComponent/>,document.getElementById('test'))
~~~



### 2、函数式组件

组件就是函数，接收一个输入并返回一个输出

- 输入是 props
- 输出就是一个普通的 JSX 组件实例

函数式组件比较特殊，因为本身是函数所以无状态，也没有 this，也没有生命周期

```react
// 1.创建函数式组件
function MyComponent() {
    // 此处的this是undefined，因为 babel 编译后开启了严格模式
    console.log(this); 
    return <h2>我是用函数定义的组件(适用于【简单组件】的定义)</h2>
}

// 2.渲染组件到页面
// 2.1.React 解析组件标签，找到了 MyComponent 组件
// 2.2.发现组件是使用函数定义的，随后调用该函数，将返回的虚拟 DOM 转为真实 DOM，随后呈现在页面中
ReactDOM.render(<MyComponent/>, document.getElementById('test'))

// 箭头函数、解构赋值
const Search = ({ value, onChange, children }) => {
    // do something
    return (
        <form>
            {children} <input type="text" value={value} onChange={onChange} />
        </form>
    );
}
```



**注意**：

- 类式组件中 render 方法里的 this 为组件实例对象
- 函数式组件没有本地状态，不能通过 this.state 访问，不能通过 this.setState() 修改



**技巧**：

- React 学习之道里提到，如果不需要控制组件的 state、props、生命周期建议使用函数式组件，否则使用类式组件



## 4、三大属性

### 1、state

state 是组件对象最重要的属性，值是对象，可以包含多个 **key：value** 的组合

组件又被称为状态机，通过更新组件的 state 来重新渲染组件

state 通过使用 this 绑定在类上，因此可以在整个组件访问到 state



**注意**：

- 状态数据，不能直接修改，调用实例的 setState() 方法进行更新
- 状态的更新是一种合并，不是替换
- 类式组件自定义的方法中的 this 为 undefined
  - 也可以通过箭头函数进行绑定
  - 通过 bind 方法强制绑定 this：this.xxx.bind()



~~~react
// 第二个代码块为简写方式
// 1.创建组件
class Weather extends React.Component {
    // 构造器调用几次？ ———— 1次
    constructor(props) {
        console.log('constructor');
        super(props)
        // 初始化状态
        this.state = {isHot:false, wind:'微风'}
        // 解决changeWeather中this指向问题
        this.changeWeather = this.changeWeather.bind(this)
    }

    // render 调用次数 ———— 1+n次 1是初始化的那次 n是状态更新的次数
    render() {
        console.log('render');
        // 读取状态
        const {isHot, wind} = this.state;
        return <h1 onClick={this.changeWeather}>今天天气很{isHot ? '炎热' : '凉爽'}，{wind}</h1>
    }

    // changeWeather 调用次数 ———— 点几次调几次
    changeWeather() {
        // changeWeather()方法位置 ———— Weather 的原型对象上，供实例使用
        // 由于 changeWeather 是作为 onClick 的回调，所以不是通过实例调用的，是直接调用
        // 类中的方法默认开启了局部的严格模式，所以 changeWeather 中的 this 为 undefined
        // constructor 方法中的代码解决
        console.log('changeWeather');
        // 获取原来的isHot值
        const isHot = this.state.isHot
        this.setState({isHot:!isHot})
        console.log(this);
        
		// 这是错误的写法
        // this.state.isHot = !isHot 
    }
}

// 2.渲染组件到页面
ReactDOM.render(<Weather/>, document.getElementById('test'))
~~~

~~~react
// 1.创建组件
class Weather extends React.Component{
    // 初始化状态
    state = {isHot:false, wind:'微风'}

    render() {
        const {isHot,wind} = this.state
        return <h1 onClick={this.changeWeather}>今天天气很{isHot ? '炎热' : '凉爽'}，{wind}</h1>
    }

    // 自定义方法————要用赋值语句的形式+箭头函数
    // 如在箭头函数内部使用了 this，会找其外部函数的 this
    changeWeather = () => {
        const isHot = this.state.isHot
        this.setState({isHot:!isHot})
    }
}

// 2.渲染组件到页面
ReactDOM.render(<Weather/>,document.getElementById('test'))
~~~



<img src="images/image-20221122114632951.png" alt="image-20221122114632951" style="zoom:50%;" />





### 2、props

每个组件对象都会有 props（properties） 属性，并且组件对象的所有属性都保存在 props 中

props 可以设定初始值，还可以限制类型

作用：通过 props 从组件外向组件内传递变化的数据



属性：

- children

  - 将元素从上层传递到当前组件中，这些元素对当前组件来说是未知的

  - 不仅可以传递字符串，还可以传递元素、元素树、组件

  - ~~~react
    <Search value={searchTerm} onChange={this.onSearchChange} >
        Search
    </Search>
    
    class Search extends Component {
        render() {
            const { value, onChange, children } = this.props;
            return (
                <form>
                    // Search
                    {children} <input type="text" value={value} onChange={onChange} />
                </form>
            );
        }
    }
    ~~~



**注意**：

- 组件内部不要修改props数据
- props 只读



~~~react
// 1、创建组件
class Person extends React.Component{
    render(){
        // console.log(this);
        // this.props.name = 'jack' // 此行代码会报错，因为props是只读的
        
        const {name,age,sex} = this.props
        return (
            <ul>
                <li>姓名：{name}</li>
                <li>性别：{sex}</li>
                <li>年龄：{age+1}</li>
            </ul>
        )
    }
}
const p = {name:'老刘', age:18, sex:'女'}

// 2、渲染组件到页面
ReactDOM.render(<Person name="jerry" age={19}  sex="男" speak={speak}/>, document.getElementById('test1'))
ReactDOM.render(<Person name="tom" age={18} sex="女"/>, document.getElementById('test2'))
// ES6 中可以通过 ... 展开一个对象
ReactDOM.render(<Person {...p}/>,document.getElementById('test3'))


// 3、对标签属性进行类型、必要性的限制
Person.propTypes = {
    name: PropTypes.string.isRequired, // 限制name必传，且为字符串
    sex: PropTypes.string, // 限制sex为字符串
    age: PropTypes.number, // 限制age为数值
    speak: PropTypes.func, // 限制speak为函数
}

// 4、指定默认标签属性值
Person.defaultProps = {
    sex: '男',// sex默认值为男
    age: 18 // age默认值为18
}

function speak() {
    console.log('我说话了');
}
~~~

~~~react
// 1、创建组件
class Person extends React.Component {
    constructor(props) {
        // 构造器是否接收props，是否传递给super，取决于：是否希望在构造器中通过this访问props
        // console.log(props);
        super(props)
        console.log('constructor',this.props);
    }
    
    // 2、对标签属性进行类型、必要性的限制
    static propTypes = {
        name:PropTypes.string.isRequired, // 限制name必传，且为字符串
        sex:PropTypes.string,// 限制sex为字符串
        age:PropTypes.number,// 限制age为数值
    }

    // 3、指定默认标签属性值
    static defaultProps = {
        sex:'男', // sex默认值为男
        age:18 // age默认值为18
    }

    render() {
        const {name,age,sex} = this.props
        return (
            <ul>
                <li>姓名：{name}</li>
                <li>性别：{sex}</li>
                <li>年龄：{age+1}</li>
            </ul>
        )
    }
}

// 4、渲染组件到页面
ReactDOM.render(<Person name="jerry"/>,document.getElementById('test1'))
~~~



### 3、refs

组件内的标签可以定义 ref 属性来标识自己

默认情况下，不能在函数组件上使用 ref 属性，因为它们没有实例

高级技巧：ref 转发



**注意**：

- 回调函数形式的 refs，调用次数为 2，第一次传入参数 null，然后第二次会传入参数 DOM 元素



~~~react
// 第一个代码块字符串形式的 refs（不推荐）
// 第二个代码块回调形式的 refs
// 第三个代码块 creatRefs 方法

// 1、创建组件
class Demo extends React.Component {
    // 展示左侧输入框的数据
    showData = () => {
        // 解构赋值，对象中需要存在该属性才能赋值
        // 这里的 this 是 Demo 组件实例
        const {input1} = this.refs
        // 获取到的是真实 DOM
        alert(input1.value)
    }
    // 展示右侧输入框的数据
    showData2 = () => {
        const {input2} = this.refs
        console.log(this)
        alert(this.value)
    }

    render() {
        return (
            <div>
                <input ref="input1" type="text" placeholder="点击按钮提示数据"/>&nbsp;
                <button onClick={this.showData}>点我提示左侧的数据</button>
                &nbsp;
                <input ref="input2" onBlur={this.showData2} type="text" placeholder="失去焦点提示数据"/>
            </div>
        )
    }
}

// 2、渲染组件到页面
ReactDOM.render(<Demo a="1" b="2"/>, document.getElementById('test'))
~~~

<img src="images/image-20220607090644018.png" alt="image-20220607090644018" style="zoom:80%;" />

~~~react
// 1、创建组件
class Demo extends React.Component {
    // 展示左侧输入框的数据
    showData = () => {
        const {input1} = this
        alert(input1.value)
    }
    // 展示右侧输入框的数据
    showData2 = () => {
        const {input2} = this
        alert(input2.value)
    }

    render() {
        return (
            <div>
                <!-- currentNode 的缩写 c，传入的参数其实是该节点 -->
                <input ref={(c) => {this.input1 = c}} type="text" placeholder="点击按钮提示数据" />
                &nbsp;
                <button onClick={this.showData}>点我提示左侧的数据</button>
                &nbsp;
                <input ref={(c) => {this.input2 = c}} type="text" placeholder="失去焦点提示数据" 
                    onBlur={this.showData2} />
                &nbsp;
            </div>
        )
    }
}

// 2、渲染组件到页面
ReactDOM.render(<Demo a="1" b="2"/>, document.getElementById('test'))
~~~

<img src="images/image-20220607090729967.png" alt="image-20220607090729967" style="zoom:80%;" />

~~~react
// 1、创建组件
class Demo extends React.Component {
    /* React.createRef 调用后可以返回一个容器，该容器可以存储被 ref 所标识的节点，该容器是“专人专用”的 */
    myRef = React.createRef()
    myRef2 = React.createRef()
    // 展示左侧输入框的数据
    showData = () => {
        alert(this.myRef.current.value);
    }
    // 展示右侧输入框的数据
    showData2 = () => {
        alert(this.myRef2.current.value);
    }

    render() {
        return (
            <div>
                <!-- 当把ref传给render中的元素时，可以通过ref中的current属性访问到该节点 -->
                <input ref={this.myRef} type="text" placeholder="点击按钮提示数据"/>
                &nbsp;
                <button onClick={this.showData}>点我提示左侧的数据</button>
                &nbsp;
                <input onBlur={this.showData2} ref={this.myRef2} 
                    type="text" placeholder="失去焦点提示数据"/>
                &nbsp;
            </div>
        )
    }
}

// 2、渲染组件到页面
ReactDOM.render(<Demo a="1" b="2"/>, document.getElementById('test'))
~~~

<img src="images/image-20220607091821915.png" alt="image-20220607091821915" style="zoom:80%;" />



## 5、事件处理

通过 onXxx 属性指定事件处理函数（注意大小写）
- React 使用的是自定义合成事件，而不是使用的原生 DOM 事件 ——> 为了更好的兼容性
- React 中的事件是通过事件委托方式处理的（委托给组件最外层的元素） ——> 为了高效

在元素中使用监听时，可以在回调函数的参数中使用 event 访问到 React 的合成事件

- 通过 event.target 得到发生事件的 DOM 元素对象 ——> 不要过度使用 ref

- 发生事件的元素正好是要操作的元素，可以省写 ref 去操作



**注意**：

- 当必须要传递一个参数时，需要把类方法封装到一个箭头函数中，此举称为高阶函数
  - 因为传给元素事件处理器的内容必须是函数

~~~react
// 1、创建组件
class Demo extends React.Component {
    // 创建ref容器
    myRef = React.createRef()
    myRef2 = React.createRef()

    // 展示左侧输入框的数据
    showData = (event) => {
        console.log(event.target);
        alert(this.myRef.current.value);
    }

    // 展示右侧输入框的数据
    showData2 = (event) => {
        alert(event.target.value);
    }
    
    onDismiss = (oid) => {
        console.log(oid);
    }

    render() {
        return (
            <div>
                <input ref={this.myRef} type="text" placeholder="点击按钮提示数据"/>&nbsp;
                <button onClick={this.showData}>点我提示左侧的数据</button>
                &nbsp;
                <input onBlur={this.showData2} type="text" placeholder="失去焦点提示数据"/>&nbsp;

                <button
                    onClick={() => this.onDismiss(item.objectID)}
                    // onClick={this.onDismiss} 无法传递参数
                    // onClick={this.onDismiss(item.objectID)} 组件加载完后会直接调用
                    type="button"
                />
            </div>
        )
    }
}

// 2、渲染组件到页面
ReactDOM.render(<Demo a="1" b="2"/>, document.getElementById('test'))
~~~



~~~react

~~~









## 6、受控VS非受控

**注意**：

- 区别在于表单数据有没有被 React 控制
  - 现用现取，叫做非受控组件
  - 存入到 status 等，叫做受控组件



~~~react
 // 创建组件
class Login extends React.Component {
    
    handleSubmit = (event) => {
        event.preventDefault() // 阻止表单提交
        const {username, password} = this
        alert(`你输入的用户名是：${username.value},你输入的密码是：${password.value}`)
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                用户名：<input ref={c => this.username = c} type="text" name="username"/>
                密码：<input ref={c => this.password = c} type="password" name="password"/>
                <button>登录</button>
            </form>
        )
    }
}

// 渲染组件
ReactDOM.render(<Login/>, document.getElementById('test'))
~~~

~~~react
// 创建组件
class Login extends React.Component {

    // 初始化状态
    // 所有输入框都由 state 维护，建议使用
    state = {
        username: '', // 用户名
        password: '' // 密码
    }

    // 保存用户名到状态中
    saveUsername = (event) => {
        console.log(this)
        this.setState({username: event.target.value})
    }

    // 保存密码到状态中
    savePassword = (event) => {
        this.setState({password: event.target.value})
    }

    // 表单提交的回调
    handleSubmit = (event) => {
        event.preventDefault() // 阻止表单提交
        const {username, password} = this.state
        alert(`你输入的用户名是：${username},你输入的密码是：${password}`)
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                用户名：<input onChange={this.saveUsername} type="text" name="username"/>
                密码：<input onChange={this.savePassword} type="password" name="password"/>
                <button>登录</button>
            </form>
        )
    }
}

// 渲染组件
ReactDOM.render(<Login/>, document.getElementById('test'))
~~~



## 7、生命周期

### 1、概述

组件从创建到死亡它会经历一些特定的阶段

React 组件中包含一系列**勾子函数**（生命周期回调函数），会在特定的时刻调用

在定义组件时，会在特定的**生命周期回调函数**中做特定的工作

挂载：组件实例化的过程

生命周期回调函数 <==> 生命周期钩子函数 <==> 生命周期函数 <==> 生命周期钩子

~~~react
// 1、创建组件
class Life extends React.Component {

    state = {opacity: 1}

    death = () => {
        // 2、卸载组件函数
        ReactDOM.unmountComponentAtNode(document.getElementById('test'))
    }

    // 3、组件挂完毕
    componentDidMount() {
        console.log('componentDidMount');
        this.timer = setInterval(() => {
            // 获取原状态
            let {opacity} = this.state
            // 减小0.1
            opacity -= 0.1
            if (opacity <= 0) opacity = 1
            // 设置新的透明度
            this.setState({opacity})
        }, 200);
    }

    // 4、组件将要卸载
    componentWillUnmount() {
        // 清除定时器
        clearInterval(this.timer)
    }

    // 初始化渲染、状态更新之后
    render() {
        console.log('render');
        return (
            <div>
                <h2 style={{opacity: this.state.opacity}}>React学不会怎么办？</h2>
                <button onClick={this.death}>不活了</button>
            </div>
        )
    }
}

// 5、渲染组件
ReactDOM.render(<Life/>, document.getElementById('test'))
~~~



### 2、流程

<img src="images/3_React生命周期(新).png" alt="3_React生命周期(新)" style="zoom: 60%;" />

1. 初始化阶段：由 ReactDOM.render() 触发 ---> 初次渲染
   1. constructor() 
      - 构造函数
      - 用于初始化操作，一般很少使用，只有在组件实例化并插入到 DOM 中的时候才会被调用
      - 唯一直接修改 state 的地方，其他地方均通过 this.setState() 方法修改
   2. getDerivedStateFromProps()
      - 当 state 需要从 props 获取数据做初始化时使用
      - 尽量不使用，维护二者状态需要消耗额外资源，增加复杂度
      - 每次渲染前都会调用
      - 替代了 componentWillReceiveProps()
      - 典型场景：表单获取默认值
   3. render() 
      - 渲染函数
      - 当 state 或者 props 修改时都会调用
   4. componentDidMount() 
      - 挂载函数，只执行一次，常用
      - UI 渲染完成后调用，一般在此钩子中做初始化
      - 典型场景：获取外部资源
      - 例如：开启定时器、发送网络请求、订阅消息
2. 更新阶段：由组件内部 this.setSate()、修改 props 或者父组件重新 render 触发
   1. getDerivedStateFromProps()
   2. shouldComponentUpdate()
      - 典型场景：性能优化
   3. render()
   4. getSnapshotBeforeUpdate() 
      - 在 render 之后但还未渲染时调用，state 已更新
      - 典型场景：获取 render 之前的 DOM 状态
   5. componentDidUpdate()
      - 每次 UI 更新被调用
      - 典型场景：页面通过 props 重新获取数据
3. 卸载组件：由 ReactDOM.unmountComponentAtNode() 触发
   1. componentWillUnmount()
      - 常用，一般在这个钩子中做收尾的事
      - 例如：关闭定时器、取消订阅消息



**重要的钩子函数**：

1.	render()：初始化渲染或更新渲染调用
2.	componentDidMount()：开启监听，发送 ajax 请求
3.	componentWillUnmount()：做一些收尾工作，如：清理定时器

~~~react
//创建组件
class Count extends React.Component{
    // 构造器
    constructor(props){
        console.log('Count---constructor');
        super(props)
        //初始化状态
        this.state = {count:0}
    }

    // 加1按钮的回调
    add = ()=>{
        //获取原状态
        const {count} = this.state
        //更新状态
        this.setState({count:count+1})
    }

    // 卸载组件按钮的回调
    death = ()=>{
        ReactDOM.unmountComponentAtNode(document.getElementById('test'))
    }

    // 强制更新按钮的回调
    force = ()=>{
        this.forceUpdate()
    }

    // 若state的值在任何时候都取决于props，那么可以使用getDerivedStateFromProps
    static getDerivedStateFromProps(props,state){
        console.log('getDerivedStateFromProps', props, state);
        return null
    }

    // 在更新之前获取快照
    getSnapshotBeforeUpdate(){
        console.log('getSnapshotBeforeUpdate');
        return 'atTest'
    }

    // 组件挂载完毕的钩子
    componentDidMount(){
        console.log('Count---componentDidMount');
    }

    // 组件将要卸载的钩子
    componentWillUnmount(){
        console.log('Count---componentWillUnmount');
    }

    // 控制组件更新的“阀门”
    shouldComponentUpdate(){
        console.log('Count---shouldComponentUpdate');
        return true
    }

    // 组件更新完毕的钩子
    componentDidUpdate(preProps,preState,snapshotValue){
        console.log('Count---componentDidUpdate',preProps,preState,snapshotValue);
    }

    render(){
        console.log('Count---render');
        const {count} = this.state
        return(
            <div>
                <h2>当前求和为：{count}</h2>
                <button onClick={this.add}>点我+1</button>
                <button onClick={this.death}>卸载组件</button>
                <button onClick={this.force}>不更改任何状态中的数据，强制更新一下</button>
            </div>
        )
    }
}

// 渲染组件
ReactDOM.render(<Count count={199}/>,document.getElementById('test'))
~~~



## 8、条件渲染

条件渲染用于需要决定渲染哪个元素时，有些时候也可以是渲染一个元素或者什么都不渲染

- 最简单的条件渲染，只需要用 JSX 中的 if-else 就可以实现
- 在 JSX 中加上一个三元运算符也可以达到这样的目的
- 运用 && 逻辑运算符

~~~react
render() {
    const { searchTerm, result } = this.state;
    return (
        <div className="page">
            <div className="interactions">
                <Search
                    value={searchTerm}
                    onChange={this.onSearchChange}
                    >
                    Search
                </Search>
            </div>
            { result
                ? <Table
                      list={result.hits}
                      pattern={searchTerm}
                      onDismiss={this.onDismiss}
                      />
                : null
            }
        </div>
    );
}

{ result &&
    <Table
        list={result.hits}
        pattern={searchTerm}
        onDismiss={this.onDismiss}
     />
}

~~~





# 5、高阶函数

**高阶函数**：如果一个函数符合下面 2 个规范中的任何一个，那该函数就是高阶函数

- 若 A 函数，接收的参数是一个函数，那么 A 就可以称之为高阶函数
- 若 A 函数，调用后的返回值依然是一个函数，那么 A 就可以称之为高阶函数
- 常见的高阶函数有：Promise、setTimeout、arr.map() 等等



**函数柯里化**：通过函数调用继续返回函数的方式，实现多次接收参数最后统一处理的函数编码形式



~~~react
// 函数柯里化
function sum(a) {
    return(b) => {
        return (c) => {
            return a+b+c
        }
    }
}
~~~

~~~react
// 使用高阶函数
// 创建组件
class Login extends React.Component {
    // 初始化状态
    state = {
        username: '', // 用户名
        password: '' // 密码
    }

    // 保存表单数据到状态中
    saveFormData = (dataType) => {
        // 将this.saveFormData('username') 的返回值作为回调，而这个回调是个函数
        return (event) => {
            this.setState({[dataType]: event.target.value})
        }
    }

    // 表单提交的回调
    handleSubmit = (event) => {
        event.preventDefault() // 阻止表单提交
        const {username, password} = this.state
        alert(`你输入的用户名是：${username},你输入的密码是：${password}`)
    }

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                {/* 必须拿一个函数作为回调 */}
                用户名：<input onChange={this.saveFormData('username')} type="text" name="username"/>
                密码：<input onChange={this.saveFormData('password')} type="password" name="password"/>
                <button>登录</button>
            </form>
        )
    }
}

//渲染组件
ReactDOM.render(<Login/>, document.getElementById('test'))
~~~

~~~react
// 不使用高阶函数
// 创建组件
class Login extends React.Component{
    // 初始化状态
    state = {
        username:'', // 用户名
        password:'' // 密码
    }

    // 保存表单数据到状态中
    saveFormData = (dataType, event)=>{
        this.setState({[dataType]:event.target.value})
    }

    // 表单提交的回调
    handleSubmit = (event)=>{
        event.preventDefault() // 阻止表单提交
        const {username,password} = this.state
        alert(`你输入的用户名是：${username},你输入的密码是：${password}`)
    }
    render(){
        return(
            <form onSubmit={this.handleSubmit}>
                用户名：<input onChange={event => this.saveFormData('username', event) } 
                        type="text" name="username"/>
                密码：<input onChange={event => this.saveFormData('password',event) } 
                       type="password" name="password"/>
                <button>登录</button>
            </form>
        )
    }
}
// 渲染组件
ReactDOM.render(<Login/>,document.getElementById('test'))
~~~



# 6、开发技巧

## 1、客户端缓存

为了实现在客户端对结果的缓存，必须在内部组件的状态中存储多个结果 （results）而不是一个结果（result）这些结果对象将会与关键字映射成一个键值对，而每一个从 API 得到的结果会以关键字为键（key）保存下来

首先用 state 保存关键字，然后将后续请求合并到旧的结果中

~~~js
result: {
	hits: [ ... ],
	page: 2,
}

results: {
	redux: {
		hits: [ ... ],
		page: 2,
	},
	react: {
		hits: [ ... ],
		page: 1,
	},
	...
}
~~~



## 2、错误处理

处理错误的基础知识，就是本地状态和条件渲染

本质上来讲，错误只是 React 的另一种状态，当一个错误发生时，先将它存在本地状态中，然后利用条件渲染在组件中显示错误信息

首先，要在本地状态中引入 error 这个状态，它初始化为 null，当错误发生时它会被置成一个 error 对象

~~~react
this.state = {
    error: null,
};

catchErrorFunc() {
    ....
    .catch(e => this.setState({ error: e }));
}


render() {
    const { error } = this.state;
    ...
    if (error) {
        return <p>Something went wrong.</p>;
    }
    ...
}

~~~



## 3、模块划分

将组件封装到各自文件夹中，并且组件入口文件使用 index.js 命名，样式使用 index.css 命名

文件名中的 index 名称表示是这个文件夹的入口文件，这仅仅是一个命名共识

~~~text
src/
	index.js
	index.css
	constants/
		index.js
	components/
        App/
            index.js
            test.js
            index.css
        Button/
            index.js
            test.js
            index.css
~~~

**注意**：

- 当使用 index.js 这个命名共识的时候，可以在导入时省略相对路径中的文件名

  - 这个约定是在 node.js 世界里面被引入的，index 文件是一个模块的入口

    - ~~~react
      import { x } from '../constants';
      ~~~

  - 描述了一个模块的公共 API，外部模块只允许通过 index.js 文件导入模块中的共享代码

  - 例子：

    - ~~~tex
      src/
      	index.js
      	App/
      		index.js
      	Buttons/
      		index.js
      		SubmitButton.js
      		SaveButton.js
      		CancelButton.js
      ~~~

    - ~~~react
      import SubmitButton from './SubmitButton';
      import SaveButton from './SaveButton';
      import CancelButton from './CancelButton';
      
      export {
      	SubmitButton,
          SaveButton,
          CancelButton,
      };
      
      import {
          SubmitButton,
          SaveButton,
          CancelButton
      } from '../Buttons';
      ~~~



## 4、引用 DOM 元素

有时需要在 React 中与 DOM 节点进行交互，使用 ref 属性可以访问元素中的一个节点，但通常访问 DOM 节点是 React 中的一种反模式，因为 React 是声明式编程和单向数据流

但是在某些情况下，仍然需要访问 DOM 节点，官方文档提到了三种情况：

- 使用 DOM API（focus 事件，媒体播放等）
- 调用命令式 DOM 节点动画
- 与需要 DOM 节点的第三方库集成（例如 D3.js）



## 5、加载

请求是异步的，此时应该向用户展示某些事情即将发生的某种反馈

首先顶一个 Loading 组件用于向用户展示正在加载中

再使用 state 保存一个是否加载的属性，在根据请求流程设置该属性

根据是否加载的属性，判断 Loading 组件是否渲染



## 6、setState() 使用函数参数

setState() 方法不仅可以接收对象，在它的第二种形式中，还可以传入一个函数来更新状态信息

使用函数作为参数而不是对象，有一个非常重要的应用场景，就是当更新状态需要取决于之前的状态或者属性的时候，如果不使用函数参数的形式， 组件的内部状态管理可能会引起 bug

因为 React 的 setState() 方法是异步的，React 会依次执行 setState() 方法，如果 setState() 方法依赖于之前的状态或者属性的话，有可能在按批次执行的期间，状态或者属性的值就已经被改变了

~~~js
// fooCount 和 barCount 这样的状态或属性
// 在调用 setState() 方法的时候在其他地方被异步地改变了
// 就会导致错误
const { fooCount } = this.state;
const { barCount } = this.props;
this.setState({ count: fooCount + barCount });
~~~

使用函数参数形式的话，传入 setState() 方法的参数是一个回调，该回调会在被执行时传入状态和属性，尽管 setState() 方法是异步的，但是通过回调函数，其使用的是执行那一刻的状态和属性

~~~js
// 上一次的值会传给prevState
this.setState((prevState, props) => {
    const { fooCount } = prevState;
    const { barCount } = props;
    return { count: fooCount + barCount };
});
~~~

setState() 中函数参数形式相比于对象参数来说，在预防潜在 bug 的同时，还可以 提高代码的可读性和可维护性





# 7、高级组件

## 1、高阶组件

高阶组件（HOC）是 React 中的一个高级概念

HOC 与高阶函数是等价的，其接受任何输入，而输入值多数时候是一个组件，也可以是可选参数，并返回一个组件作为输出，返回的组件是输入组件的增强版本，并且可以在 JSX 中使用

HOC 可用于不同的情况，比如：准备属性，管理状态、更改组件的表示形式，例如：用于帮助实现条件渲染

~~~js
// 例子
// 在这个例子中，没有做任何改变，输入组件将和输出组件一样
// 渲染与输入组件相同的实例，并将所有的属性 (props) 传递给输出组件，因此这个 HOC 没意义
function withFoo(Component) {
    return function(props) {
        return <Component { ...props } />;
    }
}

// ES6 简化写法
const withFoo = (Component) => (props) => <Component { ...props } />

// 增强输出组件功能：当加载状态 (isLoading) 为 true 时，组件显示 Loading 组件，否则显示输入的组件
// 条件渲染是 HOC 的一种绝佳用例
const withLoading = (Component) => (props) => props.isLoading ? <Loading /> : <Component { ...props } />
// 但有点不好，就是组件可能不关心 isLoading 属性，因此可以用 解构赋值将 isLoading 属性单独取出
const withLoading = 
      (Component) => ({isLoading, ...others}) => isLoading ? <Loading /> : <Component { ...others } />

~~~



# 8、状态管理

## 1、基本概念

将子状态（substate）从一个组件移动到其他组件中的重构过程被称为状态提取

例如：存在于父组件但是父组件用不到，并且子组件中需要的状态，重构到子组件中，状态从父组件到子组件向下移动

状态提取的过程也可以反过来：从子组件到父组件，这种情形被称为状态提升

例如：需要在子组件的兄弟组件上显示该组件的状态，此时需要将状态提升到父组件中，并在父组件中处理并传播







# 扩展

## 1、模块热替换

模块热替换（HMR）是一个帮助开发者在浏览器中重新加载应用的工具，并且无需再让浏览器刷新页面，使用 HMR 最大的好处是你可以保持应用的状态，例如保持在一个流程的第三步，即使修改了第三步的代码，也无需重复第一二步

开启：在 React 的入口文件 src/index.js 中，添加一些配置代码

**注意**：应用本身会被重新加载，而不是页面被重新加载

~~~react
if (module.hot) {
    module.hot.accept();
}
~~~



## 2、ES6

### 1、箭头函数

JavaScript ES6 引入了箭头函数，箭头函数表达式比普通的函数表达式更加简洁

需要注意箭头函数的一些功能性：

- this 对象：

  - 普通函数表达式总会定义它自己的 this 对象

  - 箭头函数表达式仍然会使用包含它的语境下的 this 对象

- 参数括号：

  - 如果函数只有一个参数，可以移除掉参数的括号
  - 但是如果有多个参数，就必须保留这个括号

- 可以用简洁函数体来替换块状函数体：

  - 简洁函数体的返回不用显示声明，以此移除 return 表达式

~~~jsx
// function expression
function () { ... }

// arrow function expression
() => { ... }

{list.map(item => {
    	return (
            <div key={item.objectID}></div>
        )
	}
)}

{list.map(item =>
          <div key={item.objectID}></div>
)}

~~~



### 2、类

JavaScript ES6 引入了类的概念

类都有一个用来实例化自己的构造函数，这个构造函数可以用来传入参数来赋给类的实例

此外类可以定义函数，因为这个函数被关联给了类，所以它通常被称为类的方法

~~~react
class Developer {
    constructor(firstname, lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    getName() {
        return this.firstname + ' ' + this.lastname;
    }
}
const robin = new Developer('Robin', 'Wieruch');
console.log(robin.getName());
~~~



### 3、对象初始化

简写属性简洁地初始化对象

简写方法名简洁地初始化对象的方法

使用计算属性名，为一个对象动态地根据 key 分配值

~~~js
const name = 'Robin';
const user = {
    name,
    // 等同于
    // name: name,
};

const userService = {
    getUserName(user) {
        return user.firstname + ' ' + user.lastname;
    },
    // 等同于
    //getUserName: function (user) {
    //    return user.firstname + ' ' + user.lastname;
    //},
};

// ES6
const key = 'name';
const user = {
    [key]: 'Robin',
    // 等同于
    // name: 'Robin',
};
~~~



### 4、对象方法

类方法不会自动绑定 this 到实例上

有三种方法：

- 在构造函数中使用 bind 强制绑定
- 使用箭头函数作为类方法声明自动绑定（推荐）
- 在构造函数中使用箭头函数绑定并且初始化（不推荐）
- 在 render 函数中使用 bind 强制绑定（不推荐）

~~~react
// 控制台显示 undifine
class ExplainBindingsComponent extends Component {
    onClickMe() {
        console.log(this);

        // this.onClickMe = this.onClickMe.bind(this);
        // this.onClickMe = () => { console.log(this); }

    }

    // onClickMe = () => { console.log(this); }

    render() {
        return (
            <button
                // onClick={this.onClickMe.bind(this)}
                onClick={this.onClickMe}
                type="button"
                >
                Click Me
            </button>
        );
    }
}
~~~



### 5、解构

在 JavaScript ES6 中有一种更方便的方法来访问对象和数组的属性，叫做解构

数组、字典均可以使用解构

~~~js
const user = {
    firstname: 'Robin',
    lastname: 'Wieruch',
};

const users = ['Robin', 'Andrew', 'Dan'];

// ES5
var firstname = user.firstname;
var lastname = user.lastname;
console.log(firstname + ' ' + lastname);
// output: Robin Wieruch

// ES6
const { firstname, lastname } = user;
console.log(firstname + ' ' + lastname);
// output: Robin Wieruch
~~~



### 6、Import\Export

在 JavaScript ES6 可以从模块中导入和导出某些功能，这些功能可以是函数、类、组件、常量等等

基本上可以将所有东西都赋值到一个变量上，模块可以是单个文件，或者一个带有入口文件的文件夹

import 和 export 语句的出现使得可以在多个不同的文件间共享代码，它能帮助开发者思考代码封装，不是所有的功能都需要从一个文件导出，其中一些功能应该只在定义它的文件中使用，一个文件导出的功能是这个文件公共 API，只有导出的功能才能被其他地方重用，这遵循了封装的最佳实践

~~~js
const firstname = 'robin';
const lastname = 'wieruch';
// 在一个文件导出
export { firstname, lastname };

// 在另一个文件中用相对路径导入
import { firstname, lastname } from './file1.js';
console.log(firstname);

// 也可以用面向对象的方式导入
// 导入可以有一个别名，防止冲突
import * as person from './file1.js';
console.log(person.firstname);
~~~

default 语句主要是为了兼容 ES5 只能导出一个对象，导入 default 语句可以省略花括号

~~~js
const firstname = 'robin';
const lastname = 'wieruch';

const person = {
    firstname,
    lastname,
};

export {
	firstname,
    lastname,
};

export {firstname, lastname}
export default robin;

import developer, { firstname, lastname } from './file1.js';
console.log(developer);
console.log(firstname, lastname);
~~~











# 问题

## 1、React中的key有什么作用

key的内部原理是什么

为什么遍历列表时，key最好不要用index

作用：

- 简单的说：key是虚拟DOM对象的标识，在更新显示时key起着极其重要的作用。
- 详细的说：当状态中的数据发生变化时，React会根据【新数据】生成【新的虚拟DOM】，随后React进行【新虚拟DOM】与【旧虚拟DOM】的diff比较，比较规则如下：
  - 旧虚拟DOM中找到了与新虚拟DOM相同的key：
    - 若虚拟DOM中内容没变, 直接使用之前的真实DOM
    - 若虚拟DOM中内容变了, 则生成新的真实DOM，随后替换掉页面中之前的真实DOM
  - 旧虚拟DOM中未找到与新虚拟DOM相同的key：
    - 根据数据创建新的真实DOM，随后渲染到到页面

用index作为key可能会引发的问题：

- 若对数据进行：逆序添加、逆序删除等破坏顺序操作：
  - 会产生没有必要的真实DOM更新 ==> 界面效果没问题, 但效率低。
- 如果结构中还包含输入类的DOM：
  - 会产生错误DOM更新 ==> 界面有问题。
- 注意：如果不存在对数据的逆序添加、逆序删除等破坏顺序操作，仅用于渲染列表用于展示，使用index作为key是没有问题的。

<img src="images/image-20220607162537827.png" alt="image-20220607162537827" style="zoom:80%;" />

~~~react
class Person extends React.Component{

    state = {
        persons:[
            {id:1,name:'小张',age:18},
            {id:2,name:'小李',age:19},
        ]
    }

    add = () => {
        const {persons} = this.state
        const p = {id:persons.length+1,name:'小王',age:20}
        this.setState({persons:[p,...persons]})
    }

    render() {
        return (
            <div>
                <h2>展示人员信息</h2>
                <button onClick={this.add}>添加一个小王</button>
                <h3>使用index（索引值）作为key</h3>
                <ul>
                    {this.state.persons.map((personObj,index) => {
                          return <li key={index}>{personObj.name}---{personObj.age}
                            <input type="text"/></li>})
                    }
                </ul>
                <hr/>
                <hr/>
                <h3>使用id（数据的唯一标识）作为key</h3>
                <ul>
                    {this.state.persons.map((personObj)=>{
                            return <li key={personObj.id}>{personObj.name}---{personObj.age}
                                <input type="text"/></li>})
                    }
                </ul>
            </div>
        )
    }
}

ReactDOM.render(<Person/>,document.getElementById('test'))
~~~







































































