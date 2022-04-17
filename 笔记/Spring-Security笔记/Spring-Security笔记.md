# 扩展

## 动态权限

原本的权限授予是在SpringSecurity的配置中写死，不能动态的根据需求修改权限

```java
.antMatchers("/", "/index", "/user/register", "/user/login", "/user/authentication/register").permitAll()
.antMatchers("/msgg").hasRole("p2")
.antMatchers("/eat").hasAnyRole("p1")
.antMatchers("/happytime").hasAnyRole("p2")
.antMatchers("/admin/**").hasAnyRole("root")
```

而动态权限就是将url与角色写入数据库中，使用自定义的**FilterInvocationSecurityMetadataSource**与**AccessDecisionVoter**实现，不自定义投票器也可以通过自定义AccessDecisionManager实现，不过我认为AccessDecisionManager不应该改变其原本的唱票者身份，所以选择自定义AccessDecisionVoter。

当用户登录后，获取用户访问路径并对其进行解析，查看数据库中访问该路径所需要的用户角色，并对比当前用户所拥有的角色，如果相匹配则可以访问；还有一些路径，只需要用户登录即可访问，无关用户角色，则可以在解析路径时返回默认标识或者空值以表示该路径无需用户角色；

首先是自定义的FilterInvocationSecurityMetadataSource：

```java
@Component
public class CustomizeSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    // 用于获取url以及所需的角色
    @Autowired
    MenuService menuService;
	// 匹配请求的url与数据库中的url
    AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        /*根据请求地址 分析请求该地址需要什么角色*/

        // 获取请求地址
        String url = ((FilterInvocation) object).getRequestUrl();
        // 获取所有地址
        List<Menu> menuList = menuService.getAllMenus();
        // 对请求地址进行匹配
        for (Menu m: menuList) {
            String pattern = m.getUrl();
            if(pathMatcher.match(pattern, url)) {
                // 获取该地址所需的角色
                MenuRoleVo rolesByMenuUrl = menuService.getRolesByMenuUrl(pattern);
                List<String> roleList = rolesByMenuUrl.getRoleList()
                    								  .stream()
                    								  .map(Role::getRoleName)
                    								  .collect(Collectors.toList());
                if (roleList.size() != 0) {
                    String[] roles = roleList.toArray(new String[0]);
                    // 返回所需的角色
                    return SecurityConfig.createList(roles);
                }
            }
        }
        // 如果该地址无需授权，返回null
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 为了支持所有的类型，直接返回true
        return true;
    }
}
```

接着自定义AccessDecisionVoter：

```java
@Component
public class DynamicAccessDecisionVoter implements AccessDecisionVoter<Object> {
    @Override
    public boolean supports(ConfigAttribute attribute) {
        // 同样为了支持所有类型，直接返回true
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 同样为了支持所有类型，直接返回true
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
        // 默认弃权票
        int result = ACCESS_ABSTAIN;
        Object principal = authentication.getPrincipal();
        
        if ("anonymousUser".equals(principal)) {
            // 当前用户未登录，如果不要求权限->允许访问，否则拒绝访问
            // 如果当前地址在之前是返回null，则判断
            return CollectionUtils.isEmpty(attributes) ? ACCESS_GRANTED : ACCESS_DENIED;
        } else {
            // 用户所具有的权限
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // 对比用户权限与所需权限
            // 只要具有一个角色即可通过
            for (ConfigAttribute attribute : attributes) {
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        result = ACCESS_GRANTED;
                    }
                }
            }
            return result;
        }
    }
}
```

最后注册到配置中

```java
// 与之前的s
.authorizeRequests()
// .antMatchers("/", "/index", "/user/register", "/user/login", "/user/authentication/register").permitAll()
// .antMatchers("/msgg").hasRole("p2")
// .antMatchers("/eat").hasAnyRole("p1")
// .antMatchers("/happytime").hasAnyRole("p2")
// .antMatchers("/admin/**").hasAnyRole("root")
// 任何请求,都需要身份验证
.anyRequest().authenticated()
.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
    @Override
    public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
        fsi.setSecurityMetadataSource(customizeSecurityMetadataSource);
        fsi.setAccessDecisionManager(new AffirmativeBased(getDecisionVoters()));
        return fsi;
    }
})
```

## SimpleUrlAuthenticationFailureHandler

### AuthenticationFailureHandler

**AuthenticationFailureHandler** 接口定义了Spring Security Web在遇到认证错误时所使用的处理策略。

典型做法一般是将用户**重定向到认证页面**(比如认证机制是用户名表单认证的情况)让用户再次认证。

当然具体实现类可以根据需求实现更复杂的逻辑，比如根据异常做不同的处理等等。

举个例子，如果遇到**CredentialsExpiredException**异常(**AuthenticationException**异常的一种，表示密码过期失效)，可以将用户重定向到修改密码页面而不是登录认证页面。

```
public interface AuthenticationFailureHandler {

	/**
	 * 认证失败时会调用此方法
	 * @param request 出现认证失败时所处于的请求.
	 * @param response 对应上面请求的响应对象.
	 * @param exception 携带认证失败原因的认证失败异常对象
	 * request.
	 */
	void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException;
}
```

在Spring Security Web框架内部，默认使用的认证错误处理策略是**AuthenticationFailureHandler**的实现类**SimpleUrlAuthenticationFailureHandler**。
它由**配置**指定一个**defaultFailureUrl**，表示认证失败时缺省使用的重定向地址。一旦认证失败，它的方法**onAuthenticationFailure**被调用时，它就会将用户重定向到该地址。如果该属性没有设置，它会向客户端返回一个`401`状态码。

另外SimpleUrlAuthenticationFailureHandler还有一个属性**useForward**,如果该属性设置为`true`,页面跳转将不再是重定向(`redirect`)机制，取而代之的是转发(`forward`)机制。

可以通过继承，重写onAuthenticationFailure方法实现，同一url路径不同错误提示

```java
@Override
public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    // 设置错误转发路径
    setDefaultFailureUrl(determineFailureUrl(exception));

    ......
}

    
private String determineFailureUrl(AuthenticationException exception) {
    // 默认设置登录错误页面为/login_fail
    defaultFailureUrl = StringUtils.hasLength(defaultFailureUrl) ? defaultFailureUrl : DEFAULT_FAILURE_URL;

    Integer failureType = determineFailureType(exception).getType();

    if (failureType != null) {
        defaultFailureUrl += defaultFailureUrl.lastIndexOf("?") > 0 ? "&" : "?" + "error=" + failureType;
    }

    return defaultFailureUrl;
}

// 自定义错误提示
private LoginError determineFailureType(AuthenticationException exception) {
    if (exception instanceof BadCredentialsException) {
        return LoginError.BADCREDENTIALS;
    } else if (exception instanceof LockedException) {
        return LoginError.LOCKED;
    } else if (exception instanceof AccountExpiredException) {
        return LoginError.ACCOUNTEXPIRED;
    } else if (exception instanceof UsernameNotFoundException) {
        return LoginError.USERNAMENOTFOUND;
    }

    return LoginError.FAILURE;
}
```

登录失败controller，根据登录失败类型，组装登录失败原因。

```java
@RequestMapping("/login_fail")
public String loginFail(HttpServletRequest request, Model model) {
    LoginError loginError = determineErrorType(request);

    model.addAttribute("errorMessage", loginError != null ? loginError.getMessage() : null);

    return "login_fail";
}

private LoginError determineErrorType(HttpServletRequest request) {
    String typeStr = request.getParameter("error");

    return typeStr == null ? null : LoginError.resolve(Integer.valueOf(typeStr));
}
```

SimpleUrlAuthenticationFailureHandler源码

```java
public class SimpleUrlAuthenticationFailureHandler implements
		AuthenticationFailureHandler {
	protected final Log logger = LogFactory.getLog(getClass());

	// 认证失败时缺省使用的重定向地址
	private String defaultFailureUrl;
	// 是否使用 forward, 缺省为 false, 表示使用 redirect
	private boolean forwardToDestination = false;
	// 是否在需要session的时候允许创建session
	private boolean allowSessionCreation = true;
	// 页面重定向策略
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	public SimpleUrlAuthenticationFailureHandler() {
	}

	public SimpleUrlAuthenticationFailureHandler(String defaultFailureUrl) {
		setDefaultFailureUrl(defaultFailureUrl);
	}

	/**
	 * Performs the redirect or forward to the defaultFailureUrl if set, otherwise
	 * returns a 401 error code.
	 * 
	 * If redirecting or forwarding, saveException will be called to cache the
	 * exception for use in the target view.
	 */
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		if (defaultFailureUrl == null) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");

			// 如果 defaultFailureUrl 没有设置，向客户端返回 401 错误 ： Unauthorized
			response.sendError(HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase());
		}
		else {
			saveException(request, exception);

			if (forwardToDestination) {
				// 指定了使用 forward 的情况
				logger.debug("Forwarding to " + defaultFailureUrl);

				request.getRequestDispatcher(defaultFailureUrl)
						.forward(request, response);
			}
			else {
				// 指定了使用  redirect 的情况 , 缺省情况
				logger.debug("Redirecting to " + defaultFailureUrl);
				redirectStrategy.sendRedirect(request, response, defaultFailureUrl);
			}
		}
	}

	/**
	 * Caches the AuthenticationException for use in view rendering.
	 * 
	 * If forwardToDestination is set to true, request scope will be used,
	 * otherwise it will attempt to store the exception in the session. If there is no
	 * session and allowSessionCreation is true a session will be created.
	 * Otherwise the exception will not be stored.
	 */
	protected final void saveException(HttpServletRequest request,
			AuthenticationException exception) {
		if (forwardToDestination) {
		// forward 的情况，保存异常到 request 属性 : SPRING_SECURITY_LAST_EXCEPTION
			request.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, exception);
		}
		else {
		// redirect 的情况 , 保存异常到 session : SPRING_SECURITY_LAST_EXCEPTION
			HttpSession session = request.getSession(false);

			if (session != null || allowSessionCreation) {
				request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION,
						exception);
			}
		}
	}

	/**
	 * The URL which will be used as the failure destination.
	 *
	 * @param defaultFailureUrl the failure URL, for example "/loginFailed.jsp".
	 */
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		Assert.isTrue(UrlUtils.isValidRedirectUrl(defaultFailureUrl),
				() -> "'" + defaultFailureUrl + "' is not a valid redirect URL");
		this.defaultFailureUrl = defaultFailureUrl;
	}

	protected boolean isUseForward() {
		return forwardToDestination;
	}

	/**
	 * If set to true, performs a forward to the failure destination URL instead
	 * of a redirect. Defaults to false.
	 */
	public void setUseForward(boolean forwardToDestination) {
		this.forwardToDestination = forwardToDestination;
	}

	/**
	 * Allows overriding of the behaviour when redirecting to a target URL.
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	protected boolean isAllowSessionCreation() {
		return allowSessionCreation;
	}

	public void setAllowSessionCreation(boolean allowSessionCreation) {
		this.allowSessionCreation = allowSessionCreation;
	}
}
```







## SavedRequestAwareAuthenticationSuccessHandler 

身份验证成功策略，可以利用身份验证成功策略，该策略**DefaultSavedRequest**可能已由会话存储在会话中**ExceptionTranslationFilter**。当此类请求被拦截并需要进行身份验证时，将存储请求数据以记录身份验证过程开始之前的原始目的地，并允许在重定向到相同URL时重构请求。如果合适，此类负责执行重定向到原始URL的操作。

成功进行身份验证后，它将根据以下情况决定重定向目标：

- 如果该**alwaysUseDefaultTargetUrl属性**设置为true，**defaultTargetUrl** 则将用于目标。任何DefaultSavedRequest存储在会话将被删除。
- 如果**targetUrlParameter**已在请求中设置，则该值将用作目的地。任何DefaultSavedRequest都将再次被删除。
- 如果在**SavedRequest**中找到了**RequestCache**（由设置为在**ExceptionTranslationFilter**身份验证过程开始之前记录原始目标），则将重定向到该原始目标的Url。SavedRequest收到重定向的请求后，该对象将保持缓存并被拾取（请参阅参考资料SavedRequestAwareWrapper）。
- 如果**SavedRequest**找不到，它将委派给基类。

![image-20211103163455136](images/image-20211103163455136.png)

当在**ExceptionTranslationFilter**中拦截时，会调用**HttpSessionRequestCache**保存原始的请求信息。

在**UsernamePasswordAuthenticationFilter**过滤器登录成功后，会调用**SavedRequestAwareAuthenticationSuccessHandler**。

## JWT

### **Jwts.builder()** 

返回了一个 **DefaultJwtBuilder()**

DefaultJwtBuilder包含了一些Header和Payload的一些常用设置方法

DefaultJwtBuilder属性

```java
private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
private Header header; //头部
private Claims claims; //声明
private String payload; //载荷
private SignatureAlgorithm algorithm; //签名算法
private Key key; //签名key
private byte[] keyBytes; //签名key的字节数组
private CompressionCodec compressionCodec; //压缩算法
```

### **setHeader()** 

有两种参数形式

- 一种是Header接口的实现。
- 一种是Map。

其中Header接口也继承自Map。如果以第二种形式（即Map）作为参数，在setHeader()的时候会生成默认的Header接口实现**DefaultHeader**对象。两种参数形式调用setHeader()，都会令Header重新赋值。即：

```java
this.header = header;
或者
this.header = new DefaultHeader(header);
```

### **setHeaderParam()** 和 **setHeaderParams()** 

向Header追加参数

 两个方法都使用**ensureHeader()** 方法（返回当前header 如果不存在则创建DefaultHeader）

在生成jwt的时候，如果不设置签名，那么header中的alg应该为none。jjwt中compact()方法实现如下：

```java
if (key != null) {
    jwsHeader.setAlgorithm(algorithm.getValue());
} else {
    //no signature - plaintext JWT:
    jwsHeader.setAlgorithm(SignatureAlgorithm.NONE.getValue());
}
```

```json
{"alg":"none"}
```

### 载荷

载荷部分存在两个属性：**payload**和**claims**。两个属性均可作为载荷，jjwt中二者只能设置其一，如果同时设置，在终端方法**compact()** 中将抛出异常。

#### setPayload() 

- 设置payload，直接赋值

#### setClaims() 

- 设置claims，以参数创建一个新Claims对象，直接赋值

#### claim() 

- 如果builder中Claims属性为空，则创建DefaultClaims对象，并把键值放入；如果Claims属性不为空，获取之后判断键值，存在则更新，不存在则直接放入。

### JWT7个保留声明，可选内容

- setIssuer()
- setSubject()
- setAudience()
- setExpiration()
- setNotBefore()
- setIssuedAt()
- setId()

```java
iss: 签发者
sub: 面向用户
aud: 接收者
iat(issued at): 签发时间
exp(expires): 过期时间
nbf(not before)：不能被接收处理时间，在此之前不能被接收处理
jti：JWT ID为web token提供唯一标识
```

可以在Payload中添加一些自定义的属性claims键值对

### compressWith()

- 压缩方法。当载荷过长时可对其进行压缩。可采用jjwt实现的两种压缩方法**CompressionCodecs.GZIP**和**CompressionCodecs.DEFLATE**

### signWith()

- 签名方法。两个参数分别是签名算法和自定义的签名Key（盐）。签名key可以byte[] 、String及Key的形式传入。前两种形式均存入builder的keyBytes属性，后一种形式存入builder的key属性。如果是第二种（及String类型）的key，则将其进行base64解码获得byte[] 。

### compact()

- 生成JWT

## defaultSuccessUrl和SuccessForwardUrl

**defaultSuccessUrl** 有一个重载的方法，如果我们在 defaultSuccessUrl 中指定登录成功的跳转页面为 ”/index”，此时分两种情况

- 如果你是直接在浏览器中输入的登录地址，登录成功后，就直接跳转到 /index
- 如果你是在浏览器中输入了其他地址，例如 http://localhost:8080/xxxx，结果因为没有登录，又重定向到登录页面，此时登录成功后，就不会来到 “/index“ ，而是来到 ”/xxxx“ 页面。

总结：defaultSuccessUrl 就是说，它会**默认**跳转到 **Referer** 来源页面，如果 Referer 为空，没有来源页，则跳转到默认设置的页面。

```java
public final T defaultSuccessUrl(String defaultSuccessUrl) {
	return defaultSuccessUrl(defaultSuccessUrl, false);
}

public final T defaultSuccessUrl(String defaultSuccessUrl, boolean alwaysUse) {
	SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
	handler.setDefaultTargetUrl(defaultSuccessUrl);
	handler.setAlwaysUseDefaultTargetUrl(alwaysUse);
	this.defaultSuccessHandler = handler;
	return successHandler(handler);
}
```

**successForwardUrl** 表示不管你是从哪里来的，登录后一律跳转到 successForwardUrl 指定的地址。例如 successForwardUrl 指定的地址为 ”/index“ ，你在浏览器地址栏输入 http://localhost:8080/codedq，如果你还没有登录，将会重定向到登录页面，当你登录成功之后，就会服务端跳转到 /index 页面。或者你直接就在浏览器输入了登录页面地址，登录成功后也是来到 /index。

**注**：defaultSuccessUrl 另外一个重载方法，第二个参数如果输入为 true，则效果和 successForwardUrl 一致。

## SuccessForwardUrl的405报错

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http
    		....
            // 登录成功跳转
            .successForwardUrl("/main")
           ....
}

```

```java
// 错误方法 
@RequestMapping("/main")
 public String toMain(Authentication authentication){ 
     String name = SecurityContextHolder.getContext().getAuthentication().getName();  // 正确方式
	 System.out.println("登录用户：" + authentication.getName()); 
     return "main.html";
 }
```

**两个错误的地方**：

- 此时括号中的**authentication**无法获取，要用第一行的方式获取用户的认证信息
- 登录成功跳转出现**异常**:There was an unexpected error (type=Method Not Allowed, status=**405**).

登录的请求是**POST**方式，但是我们看输出toMain()方法是执行了的，**注意springmvc不支持POST请求直接返回页面**，successForwardUrl是**转发**过来的，所以还是POST请求，所以报错了。如果要是用successForwardUrl，那么controller中最后在重定向一下就可以解决问题了。

## @EnableWebSecurity注解

**@EnableWebSecurity**是Spring Security用于**启用Web安全的注解**。

**典型的用法**：该注解用在某个Web安全配置类上(实现了**接口WebSecurityConfigurer**或者**继承**自**WebSecurityConfigurerAdapter**)。

首先**@EnableWebSecurity注解**是个组合注解，他的注解中，又使用了**@EnableGlobalAuthentication**注解。

```java
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Documented
// 导入 WebSecurityConfiguration Web安全配置,Spring Web Mvc 有关安全的配置，OAuth2 有关安全的配置
@Import({ WebSecurityConfiguration.class,
		SpringWebMvcImportSelector.class,
		OAuth2ImportSelector.class })
// 启用全局安全认证机制	
@EnableGlobalAuthentication
@Configuration
public @interface EnableWebSecurity {

	/**
	 * Controls debugging support for Spring Security. Default is false.
	 * @return if true, enables debug support with Spring Security
	 */
	boolean debug() default false;
}
```

该注解其实起到了如下效果 :

1. 控制Spring Security是否使用调试模式(通过注解属性debug指定)，缺省为false，表示缺省不使用调试模式

2. 导入 **WebSecurityConfiguration**，用于配置Web安全过滤器**FilterChainProxy**。其注入了一个非常重要的Bean，Bean的name为**springSecurityFilterChain**，这是Spring Secuity的核心过滤器，就是请求的认证入口。

   - 若干个WebSecurityConfigurerAdapter作用于一个WebSecurity**生成一个最终使用的**web安全过滤器FilterChainProxy
     - 也就是配置覆盖

3. 如果是Servlet 环境，导入**WebMvcSecurityConfiguration。**

4. 如果是OAuth2环境，导入**OAuth2ClientConfiguration**。

5. 使用注解@EnableGlobalAuthentication启用**全局认证机制**。

   - Spring Security依赖于全局认证机制，所以这里启用全局认证机制。
     注解@EnableGlobalAuthentication又导入了**AuthenticationConfiguration**用于全局认证机制配置。
     AuthenticationConfiguration主要目的用于配置**认证管理器组件** **AuthenticationManager**。
     AuthenticationManager会在运行时用于认证请求者身份。

   - ```java
     @Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
     @Target(value = { java.lang.annotation.ElementType.TYPE })
     @Documented
     @Import(AuthenticationConfiguration.class)
     @Configuration
     public @interface EnableGlobalAuthentication {}
     ```

     - 激活了**AuthenticationConfiguration**配置类，此类是来配置认证相关的核心类，用于向Spring容器中注入**AuthenticationManagerBuilder**。
     - AuthenticationManagerBuilder使用了建造者模式，该类能建造**AuthenticationManager**，AuthenticationManager是身份认证的入口。

**注意**：

在非Springboot的Spring Web MVC应用中，该注解@EnableWebSecurity需要自己引入以启用Web安全。

而在基于Springboot的Spring Web MVC应用中，没有必要再次引用该注解，Springboot的自动配置机制**WebSecurityEnablerConfiguration**已经引入了该注解，如下所示：

```java
package org.springframework.boot.autoconfigure.security.servlet;
import ........
    
@Configuration
// 仅在存在 WebSecurityConfigurerAdapter bean 时该注解才有可能生效
// (最终生效与否要结合其他条件综合考虑)
@ConditionalOnBean(WebSecurityConfigurerAdapter.class)
// 仅在不存在 springSecurityFilterChain 时该注解才有可能生效
// (最终生效与否要结合其他条件综合考虑)
@ConditionalOnMissingBean(name = BeanIds.SPRING_SECURITY_FILTER_CHAIN)
// 仅在 Servlet 环境下该注解才有可能生效
// (最终生效与否要结合其他条件综合考虑)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
// 这里启用了 Web 安全
@EnableWebSecurity 
public class WebSecurityEnablerConfiguration {}
```

## SpringSecurity的三个configure方法

```java
//用于通过允许AuthenticationProvider容易地添加来建立认证机制。
//也就是说用来记录账号，密码，角色信息。
void configure(AuthenticationManagerBuilder auth) throws Exception
//允许基于选择匹配在资源级配置基于网络的安全性。
//也就是对角色的权限——所能访问的路径做出限制
void configure(HttpSecurity http) throws Exception
//用于影响全局安全性(配置资源，设置调试模式，通过实现自定义防火墙定义拒绝请求)的配置设置。
//一般用于配置全局的某些通用事物，例如静态资源等
void configure(WebSecurity web) throws Exception
```

1、configure(AuthenticationManagerBuilder auth)实例

**注意**：此代码不从数据库读取，直接手动赋予。记录在内存中

```java
AuthenticationManagerBuilder allows 
    public void configure(AuthenticationManagerBuilder auth) {
        auth
            .inMemoryAuthentication()
            .withUser("user")
            .password("password")
            .roles("USER")
        .and()
            .withUser("admin")
            .password("password")
            .roles("ADMIN","USER");
}
```

2、configure(HttpSecurity)实例

以下示例将/admin/ 开头的网址限制为，只有具有ADMIN角色的用户才能访问，并声明任何其他网址需要成功验证。

```java
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeUrls()
        .antMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest()
        .authenticated()
}
```

3、configure(WebSecurity web)实例

```java
public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .antMatchers("/resources/**");
}
```

## HttpSecurity方法列表

### 1、formLogin()

- **说明**：指定支持 **基于表单** 的身份验证。如果没有指定{@link FormLoginConfigurer#**loginPage()**} ，将生成一个默认的登录页面。
- **示例** ：

```java
//第一个例子
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and()
    .formLogin()
    .usernameParameter("username")
    .passwordParameter("password")
    .loginPage("/authentication/login")
    .failureUrl("/authentication/login?failed") //和failureForwardUrl()f
    .loginProcessingUrl("/authentication/login/process");

//第二个例子
http.formLogin() //表单登录
    .loginPage("/index") //配置哪个url是登录页
    .loginProcessingUrl("/login") //配置哪个是登陆url
    .successForwardUrl("/succeed") //登陆成功跳转的url
    .failureForwardUrl("/fail") //登陆失败跳转的url
 
```

### 2、openidLogin()

- **说明**：配置基于 `OpenID` 的认证
- **示例**：启用 `OpenID` 认证

```java
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and()
    .openidLogin() //启用 OpenID 认证
    .permitAll();
```

### 3、headers()

- **说明**：向响应添加 **请求安全头** 。当使用 {@link WebSecurityConfigurerAdapter} 的默认构造函数时，它会被默认激活。
- **示例**：

```java
// 只调用 {@link HttpSecurity#headers()} ，其实相当于调用了以下的所有方法
http.headers()
    .contentTypeOptions()
    .and().xssProtection()
    .and().cacheControl()
    .and().httpStrictTransportSecurity()
    .and().frameOptions()
    .and()
    //...
    ;
 
//可以禁用 headers()
http.headers().disable();
 
//使用部分请求头，前提你需要调用 {@link HeadersConfigurer#defaultsDisabled()} 先关闭所有，然后打开你想要的请求头
http.headers().defaultsDisabled().cacheControl()
    .and().frameOptions()
    .and()
    //...
    ;
 
//可以选择默认值，而关闭某些特定的请求头
http.headers()
    .frameOptions().disable()
    .and()
    //...
    ;
 
```

### 4、cors()

- **说明**：添加要使用的 `{@link CorsFilter}` 。如果提供了一个名为 `corsFilter` 的bean，则使用 `{@link corsFilter}` 添加该Filter。

### 5、sessionManagement()

- 说明：允许配置 `Session` 会话管理

```java
http.authorizeRequests()
    .anyRequest().hasRole("USER")
    .and().formLogin().permitAll()
    .and().sessionManagement().maximumSessions(1).expiredUrl("/login?expired");
///强制一次只对用户的单个实例进行身份验证。如果用户使用用户名 user 进行身份验证而没有注销，并且尝试再次使用 user 进行身份验证，第一个会话将被强制终止并发送到 /login?expired URL。
//当使用 {@link SessionManagementConfigurer#maximumSessions()} 时，不要忘记为应用程序配置 {@link HttpSessionEventPublisher} ，以确保过期的会话被清除。
```

### 6、portMapper()

- **说明**：允许配置 `{@link HttpSecurity#getSharedObject()}` 中可用的 `{@link PortMapper}` 端口映射。
- 提供的 {@link SecurityConfigurer} 对象在从HTTP重定向到HTTPS，或从HTTPS重定向到HTTP时，使用这个配置作为默认的 {@link PortMapper}。默认情况下，Spring Security使用 {@link PortMapperImpl} 将HTTP端口8080映射到HTTPS端口8443，将HTTP端口80映射到HTTPS端口443。

```java
http.portMapper().http(9090).mapsTo(9443).http(80).mapsTo(443);
```

### 7、jee()

- **说明**：配置 `基于容器` 的预认证。在本例中，身份验证由Servlet容器管理。
- **示例**：这个示例将使用 `{@link HttpServletRequest}` 上找到的用户，如果用户是角色 `ROLE_USER` 或 `ROLE_ADMIN` ，则将其添加到生成的 `{@link Authentication}` 中。

```java
 http.authorizeRequests()
     .antMatchers("/**").hasRole("USER")
     .and()
     .jee().mappableRoles("ROLE_USER", "ROLE_ADMIN");
```

### 8、x509()

- **说明**：配置基于 `X509` 的预认证。
- **示例**：这个示例将尝试从 `X509` 证书中提取用户名。需要配置Servlet容器来请求客户端证书

```java
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and()
    .x509();
```

### 9、rememberMe()

- **说明**：配置 Remember Me 认证。
- **示例**：在进行身份验证时，如果名为 remember-me 的HTTP参数存在，那么即使在他们的 {@link javax.servlet.http.HttpSession} 过期失效之后，用户也会被记住。

### 10、authorizeRequests()

- **说明**：配置基于 `{@link HttpServletRequest}` 使用限制访问

### 11、requestCache()

- **说明**：允许配置 请求缓存 。例如，一个受保护的页面 /protected 可能会在身份验证之前被请求。应用程序将用户重定向到登录页面，身份验证之后，Spring Security将用户重定向到最初请求的受保护页面 /protected 。当使用 {@link WebSecurityConfigurerAdapter} 时，会被默认激活。

### 12、exceptionHandling()

- **说明**：允许配置 `异常处理` 。当使用 `{@link WebSecurityConfigurerAdapter}` 时，会被默认激活。

### 13、securityContext()

- **说明**：在 {@link HttpServletRequest} 之间的 {@link SecurityContext} 上建立 {@link SecurityContextHolder} 的管理。当使用 {@link WebSecurityConfigurerAdapter} 时，会被默认激活。

### 14、servletApi()

- **说明**：将 `{@link HttpServletRequest}` 方法与 `{@link SecurityContext}` 上的值集成起来。当使用 `{@link WebSecurityConfigurerAdapter}` 时，会被默认激活。

### 15、csrf()

- **说明**：添加 `CSRF` 支持
- 当使用 `{@link WebSecurityConfigurerAdapter}` 时，会被默认激活。你可以禁用(disable)它

### 16、logout()

- **说明**：提供 注销 的支持。当使用 {@link WebSecurityConfigurerAdapter} 时，会被默认激活。默认情况下，访问URL /logout 将使HTTP会话失效，清除配置的所有 {@link HttpSecurity#rememberMe()} 身份验证，清除 {@link SecurityContextHolder} ，然后重定向到 /login?success，从而使用户退出。

- **示**例：下面的配置，当 /custom-logout 接口被调用时，会走注销流程。注销将删除名为 remove 的cookie，清除 SecurityContexHolder，但是不会使 HttpSession 失效，在完成上面动作之后重定向到 /logout-success。

  ```
  http.authorizeRequests().antMatchers("/**").hasRole("USER")
                  .and().formLogin()
                  .and()
                  .logout().deleteCookies("remove").invalidateHttpSession(false)
                  .logoutUrl("/custom-logout")
                  .logoutSuccessUrl("/logout-success");
  ```

### 17、anonymous()

- **说明**：允许配置 匿名用户 的表示方式。当使用 {@link WebSecurityConfigurerAdapter} 时，会被默认激活。默认情况下，匿名用户将用 {@link org.springframework.security.authentication.AnonymousAuthenticationToken} ，包含角色 ROLE_ANONYMOUS。
- **示例**1：下面的配置演示了如何指定匿名用户应该包含角色 ROLE_ANON。

```java
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and().formLogin()
    .and()
    .anonymous().authorities("ROLE_ANON");
```

- **示例**2：下面演示了如何将匿名用户表示为空。注意，假设启用了匿名身份验证可能会导致代码中出现空指针异常。

```
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and().formLogin()
    .and()
    .anonymous().disable();
```

### 18、requiresChannel()

- **说明**：配置 `通道安全`（HTTPS访问）。为了使该配置有用，至少必须提供一个到所需通道的映射。
- **示例**：下面的例子演示了如何为每个请求要求HTTPS。不建议只支持某些请求需要HTTPS，因为允许HTTP的应用程序会引入许多安全漏洞。

```java
http.authorizeRequests()
                .antMatchers("/**").hasRole("USER")
                .and().formLogin()
                .and().requiresChannel().anyRequest().requiresSecure();
```

### 19、httpBasic()

- **说明**：配置 `HTTP基本认证` 。
- **示例**：下面的示例演示如何为应用程序配置HTTP基本身份验证。默认的领域是 `Spring Security Application` ，但是可以使用 `{@link HttpBasicConfigurer#realmName()}` 自定义。

```java
http.authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and()
    .httpBasic();
```

### 20、requestMatchers()

- **说明**：如果只需要一个 {@link RequestMatcher}，可以考虑使用 {@link #mvcMatcher()} 、{@link #antMatcher()}、 {@link #regexMatcher()}、或{@link #requestMatcher()}。调用 {@link #requestMatchers()} 不会覆盖之前对 {@link #mvcMatcher()}、{@link #requestMatchers()}、{@link #antMatcher()}、{@link #regexMatcher()} 和 {@link #requestMatcher()} 的调用。

```java
//下面配置了以 /api/ 和 /oauth/ 开头的URL，无需认证
http
    .requestMatchers()
    .antMatchers("/api/**", "/oauth/**")
    .and()
    .authorizeRequests()
    .antMatchers("/**").hasRole("USER")
    .and()
    .httpBasic();
```

### 21、addFilterAt()

- 说明：在指定的过滤器位置添加过滤器。

### 22、requestMatcher()

- 说明：允许将 `{@link HttpSecurity}` 配置为只在匹配所提供的 `{@link RequestMatcher}` 时被调用。如果需要更高级的配置，可以考虑使用`{@link #requestMatchers()}`。

### 23、antMatcher()

- 说明：允许将 `{@link HttpSecurity}` 配置为只在匹配所提供的 `ant` 模式时被调用。如果需要更高级的配置，可以考虑使用 `{@link #requestMatchers()}` 或 `{@link #requestMatcher()}`。

### 24、mvcMatcher()

- 说明：允许将 {@link HttpSecurity} 配置为只在匹配所提供的 Spring MVC 模式时被调用。如果需要更高级的配置，可以考虑使用 {@link #requestMatchers()} 或 {@link #requestMatcher(requestMatcher)} 。

### 25、regexMatcher()

- 说明：允许将 `{@link HttpSecurity}` 配置为只在匹配所提供的 `正则表达式` 模式时被调用。如果需要更高级的配置，可以考虑使用 `{@link #requestMatchers()}` 或 `{@link #requestMatcher()}`。

### 26、getOrApply()

- 说明：如果 `{@link SecurityConfigurer}` 已经被指定获取原始的，否则应用新的 `{@link SecurityConfigurerAdapter}` 配置。

### 27、setSharedObject()

- 说明：设置分布式对象SharedObject
- SharedObject是Spring Security提供的一个非常好用的功能，如果你在不同的地方需要对一个对象重复使用就可以将它注册为SharedObject，甚至直接注入Spring IoC像开头那样获取就可以了。这个特性能够简化配置，提高代码的可读性，也为Spring Security的DSL特性打下了基础

### 28、beforeConfigure()

- 说明：本身是空方法，可以注入配置 在 doBuild 方法执行时调用

### 29、performBuild()

- 说明：实现的父类 `AbstractConfiguredSecurityBuilder` 的抽象接口，目的是创建SecurityFilterChain实例

### 30、authenticationProvider()

- 说明：可设置认证流程

### 31、userDetailsService()

- 说明：可设置用户 服务

### 32、getAuthenticationRegistry()

- 说明：获取身份验证注册表

### 33、addFilterAfter()

- 说明：在某个过滤器之后添加一个新的过滤器

### 34、addFilterBefore()

- 说明：在某个过滤器之前添加一个新的过滤器

### 35、addFilter()

- 说明：添加过滤器

# 原理

## SpringSecurity 过滤器介绍

SpringSecurity 采用的是**责任链**的设计模式，它有一条很长的过滤器链。现在对这条过滤器链的 **15** 个过滤器进行说明:

1、 **WebAsyncManagerIntegrationFilter**：将 Security 上下文与 Spring Web 中用于 处理异步请求映射的 **WebAsyncManager** 进行集成。 

2、 **SecurityContextPersistenceFilter**：在**每次请求处理之前**将该请求相关的安全上下文信息加载到 **SecurityContextHolder** 中，然后在该次请求处理完成之后，将 SecurityContextHolder 中关于这次请求的信息存储到一个“仓储”中，然后将 SecurityContextHolder 中的信息清除，例如在 Session 中维护一个用户的安全信息就是这个过滤器处理的。 

3、 **HeaderWriterFilter**：用于将头信息加入响应中。 

4、 **CsrfFilter**：用于处理跨站请求伪造。 

5、**LogoutFilter**：用于处理退出登录。 

6、**UsernamePasswordAuthenticationFilter**：用于处理基于表单的登录请求，从表单中获取用户名和密码。默认情况下处理来自 /login 的请求。从表单中获取用户名和密码时，默认使用的表单 name 值为 username 和 password，这两个值可以通过设置这个过滤器的 **usernameParameter** 和 **passwordParameter** 两个参数的值进行修改。 

7、**DefaultLoginPageGeneratingFilter**：如果没有配置登录页面，那系统初始化时就会配置这个过滤器，并且用于在需要进行登录时生成一个登录表单页面。 

8、**BasicAuthenticationFilter**：检测和处理 http basic 认证。 

9、**RequestCacheAwareFilter**：用来处理请求的缓存。 

10、**SecurityContextHolderAwareRequestFilter**：主要是包装请求对象 request。 

11、**AnonymousAuthenticationFilter**：检测 SecurityContextHolder 中是否存在 Authentication 对象，如果不存在为其提供一个匿名 Authentication。 

12、**SessionManagementFilter**：管理 session 的过滤器 

13、**ExceptionTranslationFilter**：处理 AccessDeniedException 和 AuthenticationException 异常。 

14、**FilterSecurityInterceptor**：可以看做过滤器链的出口。

 15、**RememberMeAuthenticationFilter**：当用户没有登录而直接访问资源时, 从 cookie  里找出用户的信息，如果 Spring Security 能够识别出用户提供的 remember me cookie，用户将不必填写用户名和密码，而是直接登录进入，该过滤器默认不开启。

## SpringSecurity 基本流程

Spring Security 采取过滤链实现认证与授权，只有当前过滤器通过，才能进入下一个 过滤器

![image-20211004145621759](images\Spring-Security笔记.assets\image-20211004145621759.png)

绿色部分是**认证过滤器**，需要我们自己配置，可以配置**多个**认证过滤器。

认证过滤器可以 使用 Spring Security 提供的认证过滤器，也可以自定义过滤器（例如：短信验证）。

认证过滤器要在 **configure(HttpSecurity http)**方法中配置，没有配置不生效。

例如：

UsernamePasswordAuthenticationFilter 过滤器：该过滤器会拦截前端提交的 **POST** 方式 的登录表单请求，并进行身份认证。 

ExceptionTranslationFilter 过滤器：该过滤器不需要我们配置，对于前端提交的请求会直接放行，捕获后续抛出的异常并进行处理（例如：权限访问限制）。 

FilterSecurityInterceptor 过滤器：该过滤器是过滤器链的最后一个过滤器，根据资源权限配置来判断当前请求是否有权限访问对应的资源。如果访问受限会抛出相关异常，并由 ExceptionTranslationFilter 过滤器进行捕获和处理。

## SpringSecurity 认证流程

### 具体流程

认证流程是在 **UsernamePasswordAuthenticationFilter** 过滤器中处理的，具体流程如下所示： 

![image-20211004145850135](images\Spring-Security笔记.assets\image-20211004145850135.png)

### UsernamePasswordAuthenticationFilter源码

当前端提交的是一个 **POST** 方式的登录表单请求，就会被该过滤器拦截，并进行身份认证。该过滤器的 **doFilter()** 方法实现在其抽象父类**AbstractAuthenticationProcessingFilter** 中。

### AbstractAuthenticationProcessingFilter源码

#### doFilter()

相关源码： 

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
    throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    // 判断是否为POST请求，如果是则继续，不是则过滤器链继续
    // 使用的是AntPathRequestMatcher，在子类构造器
    if (!requiresAuthentication(request, response)) {
        chain.doFilter(request, response);
        return;
    }

    if (logger.isDebugEnabled()) {
        logger.debug("Request is to process authentication");
    }

    // 用于存储用户认证信息的类
    Authentication authResult;

    try {
        // 调用子类重写过的方法进行身份认证
        // 返回的authResult对象，封装用户认证信息
        // 源码见下文
        authResult = attemptAuthentication(request, response);
        if (authResult == null) {
            // return immediately as subclass has indicated that it hasn't completed
            // authentication
            return;
        }
        // session处理策略，如果有设置用户session最大并发数量，则在此判断并处理
        sessionStrategy.onAuthentication(authResult, request, response);
    } catch (InternalAuthenticationServiceException failed) {
        logger.error(
            "An internal error occurred while trying to authenticate the user.",
            failed);
        // 认证失败
        unsuccessfulAuthentication(request, response, failed);

        return;
    }
    catch (AuthenticationException failed) {
        // 认证失败
        unsuccessfulAuthentication(request, response, failed);

        return;
    }

    // 认证成功
    if (continueChainBeforeSuccessfulAuthentication) {
        // 默认的continueChainBeforeSuccessfulAuthentication为false，所以认证成功后，不进入下一个过滤器
        chain.doFilter(request, response);
    }
			
    successfulAuthentication(request, response, chain, authResult);
}
```

### attemptAuthentication()

UsernamePasswordAuthenticationFilter 的 **attemptAuthentication()** 方法

源码如下：

```java
// 默认用户名为username
public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
// 默认密码名为password
public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
// 默认只能为POST请求
private boolean postOnly = true;

//默认登陆表单的请求地址为/login，请求方式为POST
public UsernamePasswordAuthenticationFilter() {
    super(new AntPathRequestMatcher("/login", "POST"));
}

//doFilter()方法会调用此方法
public Authentication attemptAuthentication(HttpServletRequest request,
                                            HttpServletResponse response) 
    throws AuthenticationException {
    // 默认情况下，如果请求方式不是POST则会抛出异常
    if (postOnly && !request.getMethod().equals("POST")) {
        throw new AuthenticationServiceException(
            "Authentication method not supported: " + request.getMethod());
    }
	
    // 获取请求携带的信息
    String username = obtainUsername(request);
    String password = obtainPassword(request);

    if (username == null) {
        username = "";
    }

    if (password == null) {
        password = "";
    }

    username = username.trim();
	// 使用请求携带的用户信息构造Authentication对象，并标记为未认证
    UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

    // 将请求中的别的信息设置到Authentication对象中，例如remember me、sessionId
    setDetails(request, authRequest);
	// 调用ProviderManager类的authenticate()方法进行身份认证
    return this.getAuthenticationManager().authenticate(authRequest);
}
```

创建的 **UsernamePasswordAuthenticationToken** 是 **Authentication** 接口的实现类。

该类有**两个构造器**：

- 一个用于封装前端请求传入的未认证的用户信息
- 一个用于封装认证成功后的用户信息

```java
// 用于封装前端请求传入的未认证的用户信息，前面的authRequest对象，就是调用该构造器构造的
public UsernamePasswordAuthenticationToken(Object principal, Object credentials) {
    // 用户权限为null
    super(null);
    // 前端传入的用户名
    this.principal = principal;
    // 前端传入的密码
    this.credentials = credentials;
    // 标记为未认证
    setAuthenticated(false);
}
// 用于封装认证成功后的用户信息
public UsernamePasswordAuthenticationToken(Object principal, Object credentials,
                                           Collection<? extends GrantedAuthority> authorities) {
    // 用户权限集合
    super(authorities);
    // 封装认证用户信息的UserDetails对象，不再是用户名
    this.principal = principal;
    // 前端传入的面膜
    this.credentials = credentials;
    // 标记认证成功
    super.setAuthenticated(true); // must use super, as we override
}
```

### Authentication源码

 **Authentication** 接口的实现类用于**存储用户认证信息**。

#### 查看该接口具体定义

```java
// 用户权限集合
Collection<? extends GrantedAuthority> getAuthorities();
// 用户密码
Object getCredentials();
// 未认证时前端传入的用户名，认证成功后为封装认证用户信息的UserDetails对象
Object getPrincipal();
// 请求携带的一些属性信息，例如：remoteAddress、sessionId
Object getDetails();
// 判断是否被认证（T：认证成功，F：未认证）
boolean isAuthenticated();
// 设置是否被认证（T：认证成功，F：未认证）
void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException;
```

### ProviderManager 源码

**UsernamePasswordAuthenticationFilter** 过滤器的 **attemptAuthentication()** 方法的最后一步将未认证的 **Authentication** 对象传入 **ProviderManager** 类的 **authenticate()** 方法进行身份认证。

ProviderManager 是 **AuthenticationManager** 接口的实现类，该接口是认证相关的核心接口，也是**认证的入口**。

在实际开发中，我们可能有多种不同的认证方式，例如：用户名+ 密码、邮箱+密码、手机号+验证码等，而这些认证方式的入口始终只有一个，那就是 **AuthenticationManager接口**。

在该接口的常用实现类 ProviderManager 内部会维护一个 **List\<AuthenticationProvider>列表**，存放多种认证方式，实际上这是**委托者模式** （Delegate）的应用。**每种认证方式对应着一个 AuthenticationProvider**， **AuthenticationManager 根据认证方式的不同（根据传入的 Authentication 类型判断）委托对应的 AuthenticationProvider 进行用户认证。** 

```java
// 传入未认证的Authentication对象
public Authentication authenticate(Authentication authentication)
    throws AuthenticationException {
    // 获取 Authentication对象 的类型，根据上文我们传入的是 UsernamePasswordAuthenticationToken类
    Class<? extends Authentication> toTest = authentication.getClass();
    AuthenticationException lastException = null;
    AuthenticationException parentException = null;
    Authentication result = null;
    Authentication parentResult = null;
    boolean debug = logger.isDebugEnabled();
	
    // 获取认证方式列表即为 List<AuthenticationProvider>列表
    for (AuthenticationProvider provider : getProviders()) {
        // 判断当前的 AuthenticationProvider 是否使用于 Authentication对象
        if (!provider.supports(toTest)) {
            continue;
        }
        if (debug) {
            logger.debug("Authentication attempt using " + provider.getClass().getName());
        }
		// 找到适配的 AuthenticationProvider，此处应该为 DaoAuthenticationProvider
        try {
            // 调用 DaoAuthenticationProvider 的 authenticate()方法
            //如果认证成功，会返回一个已认证的 Authentication对象
            result = provider.authenticate(authentication);

            if (result != null) {
                // 认证成功后，会将传入的 Authentication对象详细信息，复制到已认证的 Authentication对象
                copyDetails(authentication, result);
                break;
            }
        }
        catch (AccountStatusException | InternalAuthenticationServiceException e) {
            prepareException(e, authentication);
            // SEC-546: Avoid polling additional providers if auth failure is due to
            // invalid account status
            throw e;
        } catch (AuthenticationException e) {
            lastException = e;
        }
    }
	
    // 如果认证失败，调用 父类AuthenticationManager 进行验证
    if (result == null && parent != null) {
        // Allow the parent to try.
        try {
            result = parentResult = parent.authenticate(authentication);
        }
        catch (ProviderNotFoundException e) {
            // ignore as we will throw below if no other exception occurred prior to
            // calling parent and the parent
            // may throw ProviderNotFound even though a provider in the child already
            // handled the request
        }
        catch (AuthenticationException e) {
            lastException = parentException = e;
        }
    }

    // 认证成功后
    if (result != null) {
        // 认证成功后，去除result的敏感信息，要求相关类实现 CredentialsContainer接口
        if (eraseCredentialsAfterAuthentication && (result instanceof CredentialsContainer)) {
            // Authentication is complete. Remove credentials and other secret data
            // from authentication
            // 去除过程就是调用 CredentialsContainer 的 eraseCredentials()方法
            ((CredentialsContainer) result).eraseCredentials();
        }

        // If the parent AuthenticationManager was attempted and successful than it will publish an AuthenticationSuccessEvent
        // This check prevents a duplicate AuthenticationSuccessEvent if the parent AuthenticationManager already published it
        if (parentResult == null) {
            // 发布认证成功时间
            eventPublisher.publishAuthenticationSuccess(result);
        }
        // 返回结果
        return result;
    }

    // Parent was null, or didn't authenticate (or throw an exception).
	// 认证失败后，抛出异常信息
    if (lastException == null) {
        lastException = new ProviderNotFoundException(messages.getMessage(
            "ProviderManager.providerNotFound",
            new Object[] { toTest.getName() },
            "No AuthenticationProvider found for {0}"));
    }

    // If the parent AuthenticationManager was attempted and failed than it will publish an AbstractAuthenticationFailureEvent
    // This check prevents a duplicate AbstractAuthenticationFailureEvent if the parent AuthenticationManager already published it
    if (parentException == null) {
        prepareException(lastException, authentication);
    }

    throw lastException;
}
```

上文认证成功之后的剔除敏感信息过程，调用 CredentialsContainer 接口定义的 eraseCredentials() 方法去除敏感信息。

查看 UsernamePasswordAuthenticationToken 实现的 eraseCredentials() 方法，该方法实现在其父类中：

```java
// 实现了 CredentialsContainer接口
public abstract class AbstractAuthenticationToken implements Authentication, CredentialsContainer {
	
    public void eraseCredentials() {
        // 前端传入的密码，会被置为null
        eraseSecret(getCredentials());
        // principal在已认证的Authentication中是 userDetails实现类。
        //如果该实现类想要去除敏感信息，需要实现 credentialsContainer接口的 eraseCredentials()方法
        //由于我们自定义的user类没有实现该接口，所以不进行任何操作。
        eraseSecret(getPrincipal());
        //
        eraseSecret(details);
    }

    private void eraseSecret(Object secret) {
        if (secret instanceof CredentialsContainer) {
            ((CredentialsContainer) secret).eraseCredentials();
        }
    }
}
```

接下来回到 UsernamePasswordAuthenticationFilter 过滤器的 doFilter() 方法，查看认证成功/失败的处理

即查看查看**successfulAuthentication()**和**unsuccessfulAuthentication()**方法源码︰

```java
// 认证失败处理器
protected void unsuccessfulAuthentication(HttpServletRequest request,
                                          HttpServletResponse response, 
                                          AuthenticationException failed)
    									  throws IOException, ServletException {
    // 清除该线程在 SecurityContextHolder 中对应的 SecurityContext对象
    SecurityContextHolder.clearContext();
    
    if (logger.isDebugEnabled()) {
        logger.debug("Authentication request failed: " + failed.toString(), failed);
        logger.debug("Updated SecurityContextHolder to contain null Authentication");
        logger.debug("Delegating to authentication failure handler " + failureHandler);
    }
    // remember处理
    rememberMeServices.loginFail(request, response);
    // 调用认证失败处理器
    failureHandler.onAuthenticationFailure(request, response, failed);
}

// 认证成功处理器
protected void successfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response, 
                                        FilterChain chain, 
                                        Authentication authResult)
    									throws IOException, ServletException {

    if (logger.isDebugEnabled()) {
        logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                     + authResult);
    }
	// 将认证成功的 Authentication对象 封装入 SecurityContext对象中
    // 并存入SecurityContextHolder，该类是对ThreadLocal的一个封装
    SecurityContextHolder.getContext().setAuthentication(authResult);
	// remember me处理
    rememberMeServices.loginSuccess(request, response, authResult);

    // Fire event
    if (this.eventPublisher != null) {
        // 发布认证成功事件
        eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
            authResult, this.getClass()));
    }
	// 调用认证成功处理器
    successHandler.onAuthenticationSuccess(request, response, authResult);
}
```

![image-20211004163712075](\images\Spring-Security笔记.assets\image-20211004163712075.png)

## SpringSecurity 权限访问流程

主要是对 **ExceptionTranslationFilter** 过滤器和 **FilterSecurityInterceptor** 过滤器进行介绍。

### ExceptionTranslationFilter 过滤器

该过滤器是用于处理异常的，不需要我们配置，对于前端提交的请求会直接放行，捕获后续抛出的异常并进行处理（例如：权限访问限制）。

具体源码如下：

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
	// 对前端的请求不拦截直接放行
    try {
        chain.doFilter(request, response);
        logger.debug("Chain processed normally");
    }
    catch (IOException ex) {
        throw ex;
    }
    catch (Exception ex) {
        // 捕获后续的异常进行处理
        Throwable[] causeChain = throwableAnalyzer.determineCauseChain(ex);
        // 访问需要认证的资源，但当前请求未认证所抛出的异常
        RuntimeException ase = (AuthenticationException) throwableAnalyzer 		
            				   .getFirstThrowableOfType(AuthenticationException.class, causeChain);
        if (ase == null) {
            // 访问权限受限抛出的异常
            ase = (AccessDeniedException) throwableAnalyzer.getFirstThrowableOfType(
                AccessDeniedException.class, causeChain);
        }
        if (ase != null) {
            if (response.isCommitted()) {
                throw new ServletException("Unable to handle the Spring Security Exception because the response is already committed.", ex);
            }
            handleSpringSecurityException(request, response, chain, ase);
        }
        else {
            // Rethrow ServletExceptions and RuntimeExceptions as-is
            if (ex instanceof ServletException) {
                throw (ServletException) ex;
            }
            else if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
            // Wrap other Exceptions. This shouldn't actually happen
            // as we've already covered all the possibilities for doFilter
            throw new RuntimeException(ex);
        }
    }
}
```

###  FilterSecurityInterceptor 过滤器

FilterSecurityInterceptor 是过滤器链的最后一个过滤器，该过滤器是过滤器链的最后一个过滤器，根据资源权限配置来判断当前请求是否有权限访问对应的资源。如果访问受限会抛出相关异常，最终所抛出的异常会由前一个过滤器 ExceptionTranslationFilter 进行捕获和处理。

在**FilterSecurityInterceptor**中有一个封装了**过滤链**、**request**以及**response**的**FilterInvocation**对象进行操作，在**FilterSecurityInterceptor**，主要由**invoke()**方法调用其父类**AbstractSecurityInterceptor**的方法。

具体源码如下：

```java
public void doFilter(ServletRequest request, 
                     ServletResponse response,
                     FilterChain chain) 
    				 throws IOException, ServletException {
    FilterInvocation fi = new FilterInvocation(request, response, chain);
    invoke(fi);
}
```

```java
public void invoke(FilterInvocation fi) throws IOException, ServletException {
    if ((fi.getRequest() != null)
        && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
        && observeOncePerRequest) {
        // filter already applied to this request and user wants us to observe
        // once-per-request handling, so don't re-do security checking
        fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    }
    else {
        // first time this request being called, so perform security checking
        if (fi.getRequest() != null && observeOncePerRequest) {
            fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
        }
         // 获取accessDecisionManager权限决策后结果状态、以及权限属性
		// 根据资源权限的配置来判断当前请求是否权限访问，如果不能访问，则抛出相应异常
        InterceptorStatusToken token = super.beforeInvocation(fi);

        try {
            // 访问资源，通过SpringMVC的核心组件 DispatcherServlet进行访问
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        }
        finally {
            super.finallyInvocation(token);
        }

        super.afterInvocation(token, null);
    }
}
```

**AbstractSecurityInterceptor** 的授权过滤器主要方法**beforeInvocation()**,**afterInvocation()**以及**authenticateIfRequired()**,其最主要的方法**beforeInvocation()** 分析如下：

```java
protected InterceptorStatusToken beforeInvocation(Object object) {
       ....
    // 从SecurityMetadataSource读取权限属性
    Collection<ConfigAttribute> attributes = this.obtainSecurityMetadataSource().getAttributes(object);
    if (attributes == null || attributes.isEmpty()) {
             .....
        publishEvent(new PublicInvocationEvent(object));
        return null; // no further work post-invocation
    }

    //调用认证环节获取authenticated（包含用户的详细信息）
    Authentication authenticated = authenticateIfRequired();

    // Attempt authorization
    try {
        //进行关键的一步：授权的最终决策  
        this.accessDecisionManager.decide(authenticated, object, attributes);
    }
    catch (AccessDeniedException accessDeniedException) {
        publishEvent(new AuthorizationFailureEvent(object, attributes, authenticated,
                accessDeniedException));

        throw accessDeniedException;
    }

    // Attempt to run as a different user
    Authentication runAs = this.runAsManager.buildRunAs(authenticated, object,
            attributes);

    if (runAs == null) {
        if (debug) {
            logger.debug("RunAsManager did not change Authentication object");
        }

        // no further work post-invocation
        return new InterceptorStatusToken(SecurityContextHolder.getContext(), false,
                attributes, object);
    }
    else {
        if (debug) {
            logger.debug("Switching to RunAs Authentication: " + runAs);
        }

        SecurityContext origCtx = SecurityContextHolder.getContext();
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
        SecurityContextHolder.getContext().setAuthentication(runAs);

        // need to revert to token.Authenticated post-invocation 
        return new InterceptorStatusToken(origCtx, true, attributes, object);
    }
}
```

### SecurityMetadataSource

**SecurityMetadataSource**是Spring Security的一个概念模型接口。用于表示对受权限保护的"安全对象"的权限设置信息。一个该类对象可以被理解成一个映射表，映射表中的每一项包含如下信息 ：

- 安全对象
- 安全对象所需权限信息

**SecurityMetadataSource**是从数据库或者其他数据源中加载**ConfigAttribute**，为了在**AccessDecisionManager.decide()** 最终决策中进行match。其有三个方法：

- 获取某个受保护的安全对象object的所需要的权限信息，是一组**ConfigAttribute**对象的集合，如果该安全对象object不被当前SecurityMetadataSource对象支持，则抛出异常IllegalArgumentException。
  该方法通常配合boolean supports(Class<?> clazz)一起使用，先使用boolean supports(Class<?\> clazz)确保安全对象能被当前SecurityMetadataSource支持，然后再调用该方法。

```java
Collection<ConfigAttribute> getAttributes(Object var1) throws IllegalArgumentException;//加载权限资源
```

- 获取该SecurityMetadataSource对象中保存的针对所有安全对象的权限信息的集合。该方法的主要目的是被**AbstractSecurityInterceptor**用于启动时校验每个**ConfigAttribute**对象。

```java
Collection<ConfigAttribute> getAllConfigAttributes();//加载所有权限资源
```

- 这里clazz表示安全对象的类型，该方法用于告知调用者当前SecurityMetadataSource是否支持此类安全对象，只有支持的时候，才能对这类安全对象调用getAttributes方法。

```java
boolean supports(Class<?> var1);
```

Spring Security对SecurityMetadataSource提供了两个子接口 ：

**MethodSecurityMetadataSource** 由Spring Security Core定义，用于**表示安全对象是方法**调用(**MethodInvocation**)的安全元数据源。

**FilterInvocationSecurityMetadataSource** 由Spring Security Web定义，用于**表示安全对象是Web请求**调用(**FilterInvocation**)的安全元数据源。



## SpringSecurity 请求间共享认证信息

![image-20211005193608380](images\Spring-Security笔记.assets\image-20211005193608380.png)

### successfulAuthentication()

在认证成功后的处理方法中successfulAuthentication()，有以下代码：

```java
protected void successfulAuthentication(HttpServletRequest request,
                                        HttpServletResponse response, 
                                        FilterChain chain, 
                                        Authentication authResult)
    									throws IOException, ServletException {

    if (logger.isDebugEnabled()) {
        logger.debug("Authentication success. Updating SecurityContextHolder to contain: "
                     + authResult);
    }
	// 将已认证的 Authentication对象 封装进 SecurityContext对象中，存入SecurityContextHolder
    SecurityContextHolder.getContext().setAuthentication(authResult);

    rememberMeServices.loginSuccess(request, response, authResult);

    // Fire event
    if (this.eventPublisher != null) {
        eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(
            authResult, this.getClass()));
    }

    successHandler.onAuthenticationSuccess(request, response, authResult);
}
```

查 看 **SecurityContext** 接 口 及 其 实 现 类 **SecurityContextImpl** ， 该 类 其 实 就 是 对 **Authentication** 的封装

查 看 **SecurityContextHolder** 类 ， 该 类 其 实 是 对 **ThreadLocal** 的 封 装 ， 存 储 **SecurityContext** 对象

### **SecurityContextHolder**源码

```java
public class SecurityContextHolder {
    private static SecurityContextHolderStrategy strategy;
    private static int initializeCount = 0;

    private static void initialize() {
        if (!StringUtils.hasText(strategyName)) {
            // Set default
            // 默认使用 MODE_THREADLOCAL 模式
            strategyName = MODE_THREADLOCAL;
        }

        if (strategyName.equals(MODE_THREADLOCAL)) {
            // 默认使用 ThreadLocalSecurityContextHolderStrategy 创建 strategy
            // 其内部使用 ThreadLocal 管理 SecurityContext
            strategy = new ThreadLocalSecurityContextHolderStrategy();
        }
        else if (strategyName.equals(MODE_INHERITABLETHREADLOCAL)) {
            strategy = new InheritableThreadLocalSecurityContextHolderStrategy();
        }
        else if (strategyName.equals(MODE_GLOBAL)) {
            strategy = new GlobalSecurityContextHolderStrategy();
        }
        else {
            // Try to load a custom strategy
            try {
                Class<?> clazz = Class.forName(strategyName);
                Constructor<?> customStrategy = clazz.getConstructor();
                strategy = (SecurityContextHolderStrategy) customStrategy.newInstance();
            }
            catch (Exception ex) {
                ReflectionUtils.handleReflectionException(ex);
            }
        }

        initializeCount++;
    }
    
    public static SecurityContext getContext() {
        // 需要注意，如果当前线程对应的ThreadLocal<SecurityContext> 没有任何对象存储
        // strategy.getContext() 会创建并返回一个空的 SecurityContext对象
        // 并且该空的 SecurityContext对象 会存入 ThreadLocal<SecurityContext>
		return strategy.getContext();
	}
    
    public static void setContext(SecurityContext context) {
        // 设置当前线程对应的 ThreadLocal<SecurityContext> 的存储
		strategy.setContext(context);
	}
    
    public static void clearContext() {
        // 清除当前线程对应的 ThreadLocal<SecurityContext> 的存储
		strategy.clearContext();
	}
}
```

### **ThreadLocalSecurityContextHolderStrategy**源码

```java
final class ThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    // 使用 ThreadLocal 存储 SecurityContext
    private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

    public SecurityContext getContext() {
        // 需要注意，如果当前线程对应的ThreadLocal<SecurityContext> 没有任何对象存储
        // strategy.getContext() 会创建并返回一个空的 SecurityContext对象
        // 并且该空的 SecurityContext对象 会存入 ThreadLocal<SecurityContext>
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }
    
    public void setContext(SecurityContext context) {
        // 设置当前线程对应的 ThreadLocal<SecurityContext> 的存储
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    public SecurityContext createEmptyContext() {
        // 创建一个空的 SecurityContext
        return new SecurityContextImpl();
    }

    public void clearContext() {
        // 清除当前线程对应的 ThreadLocal<SecurityContext> 的存储
        contextHolder.remove();
    }


}
```

### SecurityContextPersistenceFilter 过滤器

在 UsernamePasswordAuthenticationFilter 过滤器认证成功之后，会在认证成功的处理方法中将已认证的用户信息对象 Authentication 封装进 SecurityContext，并存入 SecurityContextHolder。 之后，响应会通过 SecurityContextPersistenceFilter 过滤器，该过滤器的位置在所有过滤器的最前面，请求到来先进它，响应返回最后一个通过它，所以在该过滤器中处理已认证的用户信息对象 **Authentication 与 Session 绑定**。

**认证成功的响应**通过 SecurityContextPersistenceFilter 过滤器时，会从 SecurityContextHolder 中取出封装了已认证用户信息对象 Authentication 的 SecurityContext，**放进 Session 中**。当请求再次到来时，请求首先经过该过滤器，该过滤器会判断当前请求的 **Session 是否存有 SecurityContext 对象**，如果有则将该对象取出再次放入 SecurityContextHolder 中，之后该请求所在的线程获得认证用户信息，后续的资源访问不需要进行身份认证；当响应再次返回时，该过滤器同样从 SecurityContextHolder 取出 SecurityContext 对象，**放入 Session 中** 

源码如下：

```java
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (request.getAttribute(FILTER_APPLIED) != null) {
        // ensure that filter is only applied once per request
        chain.doFilter(request, response);
        return;
    }

    final boolean debug = logger.isDebugEnabled();

    request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

    if (forceEagerSessionCreation) {
        HttpSession session = request.getSession();

        if (debug && session.isNew()) {
            logger.debug("Eagerly created session: " + session.getId());
        }
    }

    HttpRequestResponseHolder holder = new HttpRequestResponseHolder(request, response);
   	// 当请求到来时， 检查当前Session是否存有SecurityContext对象
    // 如果有则取出，如果没有则创建一个空的
    SecurityContext contextBeforeChainExecution = repo.loadContext(holder);

    try {
        // 将获取到的SecurityContext对象存入SecurityContextHolder
        SecurityContextHolder.setContext(contextBeforeChainExecution);
		// 进入下一个过滤器中
        chain.doFilter(holder.getRequest(), holder.getResponse());

    }
    finally {
        // 响应返回时再次取出SecurityContext对象
        SecurityContext contextAfterChainExecution = SecurityContextHolder.getContext();
        // Crucial removal of SecurityContextHolder contents - do this before anything
        // else.
        // 将SecurityContextHolder中的SecurityContext对象移除
        SecurityContextHolder.clearContext();
        // 将取出的SecurityContext对象放入Session
        repo.saveContext(contextAfterChainExecution, holder.getRequest(), holder.getResponse());
        request.removeAttribute(FILTER_APPLIED);

        if (debug) {
            logger.debug("SecurityContextHolder now cleared, as request processing completed");
        }
    }
}
```

## AuthenticationProvider

实现 **AuthenticationProvider** 接口的主要用于解析特定 **Authentication**。

有两个接口方法：

```java
Authentication authenticate(Authentication authentication) throws AuthenticationException;
```

- 与 **AuthenticationManager** 中的 **authenticate** 声明及功能完全一致
- 返回包含凭据的完整身份验证对象 **authentication**。但是，如果 AuthenticationProvider 不支持给定的 Authentication 的话，该方法可能会返回 null。在此情况下，下一个支持 authentication 的 AuthenticationProvider 将会被尝试。

```java
boolean supports(Class<?> authentication);
```

- 如果 AuthenticationProvider 支持给定的 Authentication 的话，会返回 true。
- 并不保证 AuthenticationProvider 能够对给定的 Authentication 进行身份认证，它只是表明它可以支持对其进行更深入的评估，AuthenticationProvider 依然可以返回 null，以指示应尝试另一个 AuthenticationProvider。
- 此方法是用以选择一个能够匹配 Authentication 以胜任身份认证工作的 AuthenticationProvider，交给 **ProviderManager** 来执行。

## AbstractUserDetailsAuthenticationProvider

用于解析 **UsernamePasswordAuthenticationToken** 以进行身份认证的基础 **AuthenticationProvider**。

该类实现了 AuthenticationProvider 接口的 **authenticate**(Authentication authentication) 方法

**源码如下**：

```java
public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    
    // 判断 authentication对象 类型
    Assert.isInstanceOf(UsernamePasswordAuthenticationToken.class, 
                        authentication,
                        () -> messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.onlySupports",
                            "Only UsernamePasswordAuthenticationToken is supported"));

    // 获取用户名
    String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
	// 从缓存中获取 UserDetails对象
    boolean cacheWasUsed = true;
    UserDetails user = this.userCache.getUserFromCache(username);
	// 如果缓存中获取的 UserDetails对象 为null，则从子类实现的retrieveUser()方法中获取
    if (user == null) {
        cacheWasUsed = false;

        try {
            user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
        }
        // 如果找不到 UserDetails对象，会抛出错误，处理办法有两个
        catch (UsernameNotFoundException notFound) {
            logger.debug("User '" + username + "' not found");
            
 		// 1、如果 hideUserNotFoundExceptions 为 true（默认为 true），即隐藏用户未找到异常，
            if (hideUserNotFoundExceptions) {
                //则会重新抛出凭据/密码错误异常，异常信息为 Spring Security框架已定义好的提示信息。
                throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
            }
        // 2、如果不隐藏用户未找到异常，则直接抛出 UsernameNotFoundException 异常。
            else {
                throw notFound;
            }
        }

        Assert.notNull(user, "retrieveUser returned null - a violation of the interface contract");
    }

    try {
        // 前置身份检查，源码如下。
        preAuthenticationChecks.check(user);
        // 额外身份认证校验，对于 UsernamePasswordAuthenticationToken 来说就是凭据/密码校验。
        // 具体的校验逻辑在 DaoAuthenticationProvider 中
        additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
    }
    // 如果认证过程中发生异常，会有如下处理逻辑
    catch (AuthenticationException exception) {
        // 如果当前的用户是从用户缓存中取出的，则使用原有的用户信息再进行一次身份认证，即获取用户信息、前置身份认证检查、额外身份认证检查。
        if (cacheWasUsed) {
            // There was a problem, so try again after checking
            // we're using latest data (i.e. not from the cache)
            cacheWasUsed = false;
            user = retrieveUser(username, (UsernamePasswordAuthenticationToken) authentication);
            preAuthenticationChecks.check(user);
            additionalAuthenticationChecks(user, (UsernamePasswordAuthenticationToken) authentication);
        }
        // 如果不是从用户缓存中取出的，则直接抛出异常。
        else {
            throw exception;
        }
    }
	// 后置身份认证检查。源码如下。
    postAuthenticationChecks.check(user);
	// 将当前用户放入用户缓存，如果当前用户还没有被用户缓存缓存的话。
    if (!cacheWasUsed) {
        this.userCache.putUserInCache(user);
    }
	// 转换 principal 为字符串类型。不过，需要 forcePrincipalAsString 参数为 true（默认为 false）。
    Object principalToReturn = user;

    if (forcePrincipalAsString) {
        principalToReturn = user.getUsername();
    }
	// 创建身份认证成功的 Authentication对象
    return createSuccessAuthentication(principalToReturn, authentication, user);
}
```

默认前置身份检查源码：

```java
private class DefaultPreAuthenticationChecks implements UserDetailsChecker {
    public void check(UserDetails user) {
        // 校验账户是否锁定
        if (!user.isAccountNonLocked()) {
            logger.debug("User account is locked");

            throw new LockedException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.locked",
                "User account is locked"));
        }
		// 校验账户是否可用
        if (!user.isEnabled()) {
            logger.debug("User account is disabled");

            throw new DisabledException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.disabled",
                "User is disabled"));
        }
		// 校验账户是否过期
        if (!user.isAccountNonExpired()) {
            logger.debug("User account is expired");

            throw new AccountExpiredException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.expired",
                "User account has expired"));
        }
    }
}
```

默认后置身份检查源码：

```java
// 默认的后置身份认证检查逻辑如下
private class DefaultPostAuthenticationChecks implements UserDetailsChecker {
    public void check(UserDetails user) {
        // 检查一下用户的凭据/密码是否过期。
        if (!user.isCredentialsNonExpired()) {
            logger.debug("User account credentials have expired");

            throw new CredentialsExpiredException(messages.getMessage(
                "AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                "User credentials have expired"));
        }
    }
}
```

默认的创建身份认证成功的 Authentication对象 逻辑如下。

```java
// 创建了一个新的 UsernamePasswordAuthenticationToken
// 与未认证的区别就是 principal 变成了检索到的用户详细信息（或者用户名，强制字符串principal）。
protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
    UsernamePasswordAuthenticationToken result = 
        new UsernamePasswordAuthenticationToken(principal, 
                                                authentication.getCredentials(),
                                                authoritiesMapper.mapAuthorities(user.getAuthorities()));

    result.setDetails(authentication.getDetails());

    return result;
}
```

**注意**：此方法是 protected 类型的，子类可以重写。因为，子类通常在 Authentication对象 中存储用户提供的原始凭据/密码，而非加盐、加密过的。

## DaoAuthenticationProvider

**DaoAuthenticationProvider** 是用于解析并认证 **UsernamePasswordAuthenticationToken** 的这样一个认证服务提供者。

**最终目的**：就是根据 **UsernamePasswordAuthenticationToken**对象 获取到 **username**属性，然后调用 **UserDetailsService** 检索用户详细信息。

**构造方法**：

在 DaoAuthenticationProvider 创建之时，会制定一个默认的 PasswordEncoder，如果我们没有配置任何 PasswordEncoder，将使用这个默认的 PasswordEncoder，如果我们自定义了 PasswordEncoder 实例，那么会使用我们自定义的 PasswordEncoder 实例。

```java
private PasswordEncoder passwordEncoder;
private volatile String userNotFoundEncodedPassword;
public DaoAuthenticationProvider() {
    setPasswordEncoder(PasswordEncoderFactories.createDelegatingPasswordEncoder());
}
public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
    this.passwordEncoder = passwordEncoder;
    this.userNotFoundEncodedPassword = null;
}
```

DaoAuthenticationProvider 的初始化是在 **InitializeUserDetailsManagerConfigurer**#**configure** 方法中完成的

源码如下：

```java
public void configure(AuthenticationManagerBuilder auth) throws Exception {
    if (auth.isConfigured()) {
        return;
    }
    UserDetailsService userDetailsService = getBeanOrNull(
        UserDetailsService.class);
    if (userDetailsService == null) {
        return;
    }
    // 首先去调用 getBeanOrNull()方法获取一个 PasswordEncoder 实例，getBeanOrNull() 方法实际上就是去 Spring 容器中查找对象。
    PasswordEncoder passwordEncoder = getBeanOrNull(PasswordEncoder.class);
    UserDetailsPasswordService passwordManager = getBeanOrNull(UserDetailsPasswordService.class);
    // 接下来直接 new 一个 DaoAuthenticationProvider 对象
    // 在 new 的过程中，DaoAuthenticationProvider 中默认的 PasswordEncoder 已经被创建出来了。
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    //如果一开始从 Spring 容器中获取到了 PasswordEncoder 实例，则将之赋值给 DaoAuthenticationProvider 实例
    // 否则就是用 DaoAuthenticationProvider 自己默认创建的 PasswordEncoder。
    if (passwordEncoder != null) {
        provider.setPasswordEncoder(passwordEncoder);
    }
    if (passwordManager != null) {
        provider.setUserDetailsPasswordService(passwordManager);
    }
    provider.afterPropertiesSet();
    auth.authenticationProvider(provider);
}
```

额外的身份认证检查方法，也即 **additionalAuthenticationChecks()，密码检查**。源码如下：

```java
protected void additionalAuthenticationChecks(UserDetails userDetails,
                                              UsernamePasswordAuthenticationToken authentication)
    										  throws AuthenticationException {
    // 校验密码是否为空
    if (authentication.getCredentials() == null) {
        logger.debug("Authentication failed: no credentials provided");

        throw new BadCredentialsException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials",
            "Bad credentials"));
    }
	
    // 将密码转为字符串
    String presentedPassword = authentication.getCredentials().toString();
	
    // 将输入的密码与查询得到的密码进行比对
    if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
        logger.debug("Authentication failed: password does not match stored value");

        throw new BadCredentialsException(messages.getMessage(
            "AbstractUserDetailsAuthenticationProvider.badCredentials",
            "Bad credentials"));
    }
}
```

用户检索，即 **retrieveUser()**。源码如下：

需要调用 **UserDetailsService** 检索用户详细信息，如权限列表、存储密码等

```java
protected final UserDetails retrieveUser(String username,
                                         UsernamePasswordAuthenticationToken authentication)
    									 throws AuthenticationException {
    // 定时攻击保护
    prepareTimingAttackProtection();
    try {
        UserDetails loadedUser = this.getUserDetailsService().loadUserByUsername(username);
        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }
    catch (UsernameNotFoundException ex) {
        // 定时攻击保护
        mitigateAgainstTimingAttack(authentication);
        throw ex;
    }
    catch (InternalAuthenticationServiceException ex) {
        throw ex;
    }
    catch (Exception ex) {
        throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
    }
}
```

DaoAuthenticationProvider 还重写了基类的 **createSuccessAuthentication()** 方法。

```java
protected Authentication createSuccessAuthentication(Object principal,
                                                     Authentication authentication, 
                                                     UserDetails user) {
    
    boolean upgradeEncoding = this.userDetailsPasswordService != null
        && this.passwordEncoder.upgradeEncoding(user.getPassword());
    // 更新一下 User 中的密码
    if (upgradeEncoding) {
        String presentedPassword = authentication.getCredentials().toString();
        // 默认会使用BCryptPasswordEncoder编码
        // 由PasswordEncoderFactories创建
        String newPassword = this.passwordEncoder.encode(presentedPassword);
        user = this.userDetailsPasswordService.updatePassword(user, newPassword);
    }
    return super.createSuccessAuthentication(principal, authentication, user);
}
```

## PasswordEncoder

```java
// 该方法提供了明文密码的加密处理，加密后密文的格式主要取决于PasswordEncoder接口实现类实例。
String encode(CharSequence rawPassword);
// 匹配存储的密码以及登录时传递的密码（登录密码是经过加密处理后的字符串）是否匹配，如果匹配该方法则会返回true。
boolean matches(CharSequence rawPassword, String encodedPassword);
// 对编码的密码再次编码
default boolean upgradeEncoding(String encodedPassword) {return false;}
```

![image-20211005151227407](C:\Users\zzp84\Desktop\Spring-Security笔记\images\Spring-Security笔记.assets\image-20211005151227407.png)

**DelegatingPasswordEncoder**：默认加载的**委派密码编码器**，内部其实是一个**Map集合**，根据传递的**Key**（Key为加密方式）获取Map集合的Value，而Value则是具体的PasswordEncoder实现类。也就是说它将具体编码的实现根据要求委派给不同的算法，以此来实现不同编码算法之间的兼容和变化协调。

- 默认加载进DaoAuthenticationProvider。

There is no PasswordEncoder mapped for the id "null"

**构造方法**：

```java
public DelegatingPasswordEncoder(String idForEncode, Map<String, PasswordEncoder> idToPasswordEncoder) {
    // idForEncode决定密码编码器的类型
    if(idForEncode == null) {
        throw new IllegalArgumentException("idForEncode cannot be null");
    }
    // idToPasswordEncoder决定判断匹配时兼容的类型
    // idToPasswordEncoder必须包含idForEncode(不然加密后就无法匹配了)
    if(!idToPasswordEncoder.containsKey(idForEncode)) {
        throw new IllegalArgumentException("idForEncode " + idForEncode + "is not found in idToPasswordEncoder " + idToPasswordEncoder);
    for(String id : idToPasswordEncoder.keySet()) {
        if(id == null) {
            continue;
        }
        if(id.contains(PREFIX)) {
            throw new IllegalArgumentException("id " + id + " cannot contain " + PREFIX);
        }
        if(id.contains(SUFFIX)) {
            throw new IllegalArgumentException("id " + id + " cannot contain " + SUFFIX);
        }
    }
    this.idForEncode = idForEncode;
    this.passwordEncoderForEncode = idToPasswordEncoder.get(idForEncode);
    this.idToPasswordEncoder = new HashMap<>(idToPasswordEncoder);
}
```

**使用工厂构造**：

```java
PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
```

```java
// 具体实现
public static PasswordEncoder createDelegatingPasswordEncoder() {
    // 默认使用BCryptPasswordEncoder编码
    String encodingId = "bcrypt";
    Map<String, PasswordEncoder> encoders = new HashMap<>();
    encoders.put(encodingId, new BCryptPasswordEncoder());
    encoders.put("ldap", new LdapShaPasswordEncoder());
    encoders.put("MD4", new Md4PasswordEncoder());
    encoders.put("MD5", new MessageDigestPasswordEncoder("MD5"));
    encoders.put("noop", NoOpPasswordEncoder.getInstance());
    encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
    encoders.put("scrypt", new SCryptPasswordEncoder());
    encoders.put("SHA-1", new MessageDigestPasswordEncoder("SHA-1"));
    encoders.put("SHA-256", new MessageDigestPasswordEncoder("SHA-256"));
    encoders.put("sha256", new StandardPasswordEncoder());

    return new DelegatingPasswordEncoder(encodingId, encoders);
}
```

遇到新密码，**DelegatingPasswordEncoder**会委托给**BCryptPasswordEncoder**（encodingId为bcryp）进行加密，同时，对历史上使用ldap、MD4、MD5等等加密算法的密码认证保持兼容（如果数据库里的密码使用的是MD5算法，那使用matches方法认证仍可以通过，但新密码会使用bcrypt进行储存）。

**密码存储格式**：{encodingId}encodedPassword，encodingId标识**PaswordEncoder**的种类，**encodedPassword**是原密码被编码后的密码。

```java
// {bcrypt}格式会委托给BCryptPasswordEncoder加密类
{bcrypt}$2a$10$iMz8sMVMiOgRgXRuREF/f.ChT/rpu2ZtitfkT5CkDbZpZlFhLxO3y
// {pbkdf2}格式会委托给Pbkdf2PasswordEncoder加密类
{pbkdf2}cc409867e39f011f6332bbb6634f58e98d07be7fceefb4cc27e62501594d6ed0b271a25fd9f7fc2e
// {MD5}格式会委托给MessageDigestPasswordEncoder加密类
{MD5}e10adc3949ba59abbe56e057f20f883e
// {noop}明文方式，委托给NoOpPasswordEncoder
{noop}123456
// ...
```

**注意**：

- **rawPassword**相当于密码字符原序列 ”123456”
- **encodedPassword**是使用encodingId对应的密码编码器，将密码字符原序列编码后的加密字符串，假设为 ”xxxxx” 存储的密码 
- **prefixEncodedPassword**是在数据库中，我们所能见到的形式如 ”{bcrypt}$2a$10$iMz8sMVMiOgRgXRuREF/f.ChT/rpu2ZtitfkT5CkDbZpZlFhLxO3y”

**注意**：

- 若没有在DelegatingPasswordEncoder中设置defaultPasswordEncoderForMatches，数据库中的密码必须按照存储格式储存。
- 若设置了defaultPasswordEncoderForMatches，则不一定需要按照密码格式储存。
- 

**密码编码与匹配**：

```java
// 编码
// 通过前缀，获取对应编码器编码
private static final String PREFIX = "{";
private static final String SUFFIX = "}";

@Override
public String encode(CharSequence rawPassword) {
    return PREFIX + this.idForEncode + SUFFIX + this.passwordEncoderForEncode.encode(rawPassword);
}
```

```java
// 匹配
@Override
public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
    if(rawPassword == null && prefixEncodedPassword == null) {
        return true;
    }
    //取出编码算法的encodingId
    String id = extractId(prefixEncodedPassword);
    //根据编码算法的encodingId从支持的密码编码器Map(构造时传入)中取出对应编码器
    PasswordEncoder delegate = this.idToPasswordEncoder.get(id);
    if(delegate == null) {
    //如果找不到对应的密码编码器则使用默认密码编码器进行匹配判断，此时比较的密码字符串是 prefixEncodedPassword
        // 默认编码器源码见下文
        return this.defaultPasswordEncoderForMatches.matches(rawPassword, prefixEncodedPassword);
    }
    //从 prefixEncodedPassword 中提取获得 encodedPassword 
    String encodedPassword = extractEncodedPassword(prefixEncodedPassword);
    //使用对应编码器进行匹配判断，此时比较的密码字符串是 encodedPassword ,不携带编码算法encodingId头
    return delegate.matches(rawPassword, encodedPassword);
}
```

**defaultPasswordEncoderForMatches及 id为null异常**：

```java
// PREFIX与SUFFIX都为常量
private static final String PREFIX = "{";
private static final String SUFFIX = "}";
// idForEncode、passwordEncoderForEncode、idToPasswordEncoder都是在构造方法中传入
private final String idForEncode;
private final PasswordEncoder passwordEncoderForEncode;
private final Map<String, PasswordEncoder> idToPasswordEncoder;
// 只有defaultPasswordEncoderForMatches有一个set方法可以修改
private PasswordEncoder defaultPasswordEncoderForMatches = new UnmappedIdPasswordEncoder();

public void setDefaultPasswordEncoderForMatches(
    PasswordEncoder defaultPasswordEncoderForMatches) {
    if(defaultPasswordEncoderForMatches == null) {
        throw new IllegalArgumentException("defaultPasswordEncoderForMatches cannot be null");
    }
    this.defaultPasswordEncoderForMatches = defaultPasswordEncoderForMatches;
}

// 私有的默认实现
// 唯一作用就是抛出异常提醒你要自己选择一个默认密码编码器来取代它
private class UnmappedIdPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        throw new UnsupportedOperationException("encode is not supported");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
        String id = extractId(prefixEncodedPassword);
        throw new IllegalArgumentException("There is no PasswordEncoder mapped for the id \"" + id + "\"");
    }
}
```

## JdbcTokenRepositoryImpl

```java
@Autowired
private UserDetailsService myUserDetailServiceImpl; // 用户信息服务

@Autowired
private DataSource dataSource; // 数据源

@Override
protected void configure(HttpSecurity http) throws Exception {
    // formLogin()是默认的登录表单页，如果不配置 loginPage(url)，则使用 spring security
    // 默认的登录页，如果配置了 loginPage()则使用自定义的登录页
    http.formLogin() // 表单登录
        .loginPage(SecurityConst.AUTH_REQUIRE)
        .loginProcessingUrl(SecurityConst.AUTH_FORM) // 登录请求拦截的url,也就是form表单提交时指定的action
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
        .and()
        .rememberMe()
        // 重点
        .userDetailsService(myUserDetailServiceImpl) // 设置userDetailsService
        .tokenRepository(persistentTokenRepository()) // 设置数据访问层
        .tokenValiditySeconds(60 * 60) // 记住我的时间(秒)
        .and()
        .authorizeRequests() // 对请求授权
        .antMatchers(SecurityConst.AUTH_REQUIRE, securityProperty.getBrowser().getLoginPage())
        .permitAll() // 允许所有人访问login.html和自定义的登录页
        .anyRequest() // 任何请求
        .authenticated()// 需要身份认证
        .and()
        .csrf().disable(); // 关闭跨站伪造
}

/**
* 持久化token
*
* Security中，默认是使用PersistentTokenRepository的子类InMemoryTokenRepositoryImpl，将token放在内存中
* 如果使用JdbcTokenRepositoryImpl，会创建表persistent_logins，将token持久化到数据库
*/
@Bean
public PersistentTokenRepository persistentTokenRepository() {
    JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
    tokenRepository.setDataSource(dataSource); // 设置数据源
    //tokenRepository.setCreateTableOnStartup(true); // 启动创建表，创建成功后注释掉
    return tokenRepository;
}
```

## AuthorityUtils

此类一般用于UserDetailsService的实现类中的loadUserByUsername方法

此工具类一共有三个方法：

- **commaSeparatedStringToAuthorityList** 作用为给user账户添加一个或多个权限，用逗号分隔，底层调用的是**createAuthorityList**方法，唯一区别在于此方法把所有的权限包含进一个字符串参数中，只不过用逗号分隔。

```java
commaSeparatedStringToAuthorityList()
// 例子
return new User(username,pass,AuthorityUtils.commaSeparatedStringToAuthorityList("admin,normal"));

```

- **createAuthorityList** 将权限转换为List

```java
List<GrantedAuthority> list=AuthorityUtils.createAuthorityList("admin","normal");//一个权限一个参数
return new User(username,pass,list);
```

- **authorityListToSet** 将GrantedAuthority对象的数组转换为Set

```java
List<GrantedAuthority> list=AuthorityUtils.createAuthorityList("admin","normal");
Set<String> set=AuthorityUtils.authorityListToSet(list);
```

## AuthenticationManager

AuthenticationManager这个接口方法，入参和返回值的类型都是**Authentication**。该接口的作用是对用户的未授信凭据进行认证，认证通过则返回授信状态的凭据，否则将抛出认证异常AuthenticationException。

初始化流程：

**WebSecurityConfigurerAdapter**中的**void configure(AuthenticationManagerBuilder auth)**是配置AuthenticationManager 的地方。

```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(weChatSecurityConfigProperties.getUserDetailsService());
    daoAuthenticationProvider.setPasswordEncoder(multiPasswordEncoder());
    auth.authenticationProvider(daoAuthenticationProvider);
}
```

![image-20211122084026271](images/image-20211122084026271.png)

认证过程：

![image-20211122084304705](images/image-20211122084304705.png)

## AccessDecisionManager

AccessDecisionManager本身并不完成相关的逻辑，全部交由其管理的**AccessDecisionVoter**依次去判断与执行。被**AbstractSecurityInterceptor** 拦截器调用进行最终访问控制决策。

而根据**decide()**方法的逻辑规则不同，Spring Security中分别存在三种不同decide决策规则。

- AffirmativeBased（在Spring Security默认使用）一票通过
- UnanimousBased 一票否决
- ConsensusBased 少数服从多数

接口源码：

```java
public interface AccessDecisionManager {
    void decide(Authentication authentication, 
                Object object,
				Collection<ConfigAttribute> configAttributes) 
        throws AccessDeniedException, InsufficientAuthenticationException;
    boolean supports(ConfigAttribute attribute);
    boolean supports(Class<?> clazz);
}
```

- **ConfigAttribute**负责表述规则
- **AccessDecisionVoter**负责为规则表决

**PS**：最终的访问授权是否通过是由AccessDecisionManager进行决策的。

在框架设计中AccessDecisionManager是AccessDecisionVoter的集合类，管理着对于不同规则进行判断与表决的AccessDecisionVoter们。但是，AccessDecisionVoter分别都只会对自己支持的规则进行表决，如一个资源的访问规则存在多个并行时，便不能以某一个AccessDecisionVoter的表决作为最终的访问授权结果。AccessDecisionManager的职责便是在这种场景下，汇总所有AccessDecisionVoter的表决结果后给出一个最终的决策。从而导致框架中预设了三种不同决策规则的AccessDecisionManager的实现类。

![image-20211122085632086](images/image-20211122085632086.png)

## AccessDecisionVoter

AccessDecisionVoter 是一个投票器，负责对授权决策进行表决。然后，最终由唱票者AccessDecisionManager 统计所有的投票器表决后，来做最终的授权决策。

**AccessDecisionManager.decide()**将使用**AccessDecisionVoter**进行投票决策。

**AccessDecisionVoter**进行投票访问控制决策，访问不通过就抛出**AccessDeniedException**。

**AccessDecisionVoter**的**核心方法vote()** 通常是获取**Authentication的GrantedAuthority**与**已定义好的ConfigAttributes**进行**match**，如果成功为投同意票，匹配不成功为拒绝票，当ConfigAttributes中无属性时，才投弃票。

**AccessDecisionVoter**用三个静态变量表示voter投票情况：

- **ACCESS_ABSTAIN：** 弃权
- **ACCESS_DENIED：** 拒绝访问
- **ACCESS_GRANTED：** 允许访问

**PS**：**当所有voter都弃权时使用变量allowIfEqualGrantedDeniedDecisions来判断，true为通过，false抛出AccessDeniedException。** 



几种常用的投票器

### WebExpressionVoter

**Spring Security** 框架<u>默认</u> **FilterSecurityInterceptor** 实例中 **AccessDecisionManager** 默认的投票器 **WebExpressionVoter**。其实，就是对使用 **http.authorizeRequests()** 基于 Spring-EL进行控制权限的的授权决策类。

```java
http
    .authorizeRequests()
    .anyRequest()
    .authenticated()
    .antMatchers().permitAll()
    .antMatchers().hasRole()
    .antMatchers().hasAuthority()
```

### AuthenticatedVoter

针对 **ConfigAttribute#getAttribute()** 中配置为 **IS_AUTHENTICATED_FULLY、IS_AUTHENTICATED_REMEMBERED、IS_AUTHENTICATED_ANONYMOUSLY** 权限标识时的授权决策。因此，其投票策略比较简单：

```java
public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		int result = ACCESS_ABSTAIN;

		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;

				if (IS_AUTHENTICATED_FULLY.equals(attribute.getAttribute())) {
					if (isFullyAuthenticated(authentication)) {
						return ACCESS_GRANTED;
					}
				}

				if (IS_AUTHENTICATED_REMEMBERED.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isRememberMe(authentication)
							|| isFullyAuthenticated(authentication)) {
						return ACCESS_GRANTED;
					}
				}

				if (IS_AUTHENTICATED_ANONYMOUSLY.equals(attribute.getAttribute())) {
					if (authenticationTrustResolver.isAnonymous(authentication)
							|| isFullyAuthenticated(authentication)
							|| authenticationTrustResolver.isRememberMe(authentication)) {
						return ACCESS_GRANTED;
					}
				}
			}
		}

		return result;
	}
}
```

### PreInvocationAuthorizationAdviceVoter

用于处理基于注解 **@PreFilter** 和 **@PreAuthorize** 生成的 **PreInvocationAuthorizationAdvice**，来处理授权决策的实现。

```java
public int vote(Authentication authentication, MethodInvocation method,
                Collection<ConfigAttribute> attributes) {

    // Find prefilter and preauth (or combined) attributes
    // if both null, abstain
    // else call advice with them

    PreInvocationAttribute preAttr = findPreInvocationAttribute(attributes);

    if (preAttr == null) {
        // No expression based metadata, so abstain
        return ACCESS_ABSTAIN;
    }

    boolean allowed = preAdvice.before(authentication, method, preAttr);

    return allowed ? ACCESS_GRANTED : ACCESS_DENIED;
}
```

### RoleVoter

角色投票器。用于 **ConfigAttribute#getAttribute()** 中配置为角色的授权决策。其默认前缀为 ROLE_，可以自定义，也可以设置为空，直接使用角色标识进行判断。这就意味着，任何属性都可以使用该投票器投票，也就偏离了该投票器的本意，是不可取的。

```java
public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
    if (authentication == null) {
        return ACCESS_DENIED;
    }
    int result = ACCESS_ABSTAIN;
    Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

    for (ConfigAttribute attribute : attributes) {
        if (this.supports(attribute)) {
            result = ACCESS_DENIED;

            // Attempt to find a matching granted authority
            for (GrantedAuthority authority : authorities) {
                if (attribute.getAttribute().equals(authority.getAuthority())) {
                    return ACCESS_GRANTED;
                }
            }
        }
    }

    return result;
}
```

**注意：决策策略比较简单，用户只需拥有任一当前请求需要的角色即可，不必全部拥有**。

### RoleHierarchyVoter

基于 RoleVoter，唯一的不同就是该投票器中的角色是**附带上下级关系的**。也就是说，角色A包含角色B，角色B包含角色C，此时，如果用户拥有角色A，那么理论上可以同时拥有角色B、角色C的全部资源访问权限。

**注意：同 RoleVoter 的决策策略，用户只需拥有任一当前请求需要的角色即可，不必全部拥有**。

## **FilterSecurityInterceptor** 

自定义的 **FilterSecurityInterceptor** 要么是在默认 **FilterSecurityInterceptor** 实例之前，要么是在之后。

默认的 **FilterSecurityInterceptor** 实例初始化逻辑：

### SecurityMetadataSource

**Spring Security** 主要通过如下方法来配置默认的 **FilterSecurityInterceptor** 实例的 **SecurityMetadataSource**。

```java
http
    .authorizeRequests()
    .anyRequest()
    .authenticated()
    .antMatchers().permitAll()
    .antMatchers().hasRole()
    .antMatchers().hasAuthority()
```

首先，在 **AbstractInterceptUrlConfigurer** 类的 configure 方法中定义实例。

```java
@Override
public void configure(H http) throws Exception {
    FilterInvocationSecurityMetadataSource metadataSource = createMetadataSource(http);
    if (metadataSource == null) {
        return;
    }
    FilterSecurityInterceptor securityInterceptor = createFilterSecurityInterceptor(
        http, metadataSource, http.getSharedObject(AuthenticationManager.class));
    if (filterSecurityInterceptorOncePerRequest != null) {
        securityInterceptor.setObserveOncePerRequest(filterSecurityInterceptorOncePerRequest);
    }
    securityInterceptor = postProcess(securityInterceptor);
    http.addFilter(securityInterceptor);
    http.setSharedObject(FilterSecurityInterceptor.class, securityInterceptor);
}
```

**该实例也会放到 sharedObject 中**。

**FilterSecurityInterceptor** 实例的创建是调用的 **AbstractInterceptUrlConfigurer** 类的 **createFilterSecurityInterceptor** 方法，创建逻辑如下：

```java
private FilterSecurityInterceptor createFilterSecurityInterceptor(H http,
			FilterInvocationSecurityMetadataSource metadataSource,
			AuthenticationManager authenticationManager) throws Exception {
    FilterSecurityInterceptor securityInterceptor = new FilterSecurityInterceptor();
    securityInterceptor.setSecurityMetadataSource(metadataSource);
    securityInterceptor.setAccessDecisionManager(getAccessDecisionManager(http));
    securityInterceptor.setAuthenticationManager(authenticationManager);
    securityInterceptor.afterPropertiesSet();
    return securityInterceptor;
}
```

**SecurityMetadataSource** 是通过 **AbstractInterceptUrlConfigurer** 类的抽象 **createMetadataSource** 方法来创建。

```java
abstract FilterInvocationSecurityMetadataSource createMetadataSource(H http);
```

其具体逻辑，是由 **AbstractInterceptUrlConfigurer** 类的子类 **ExpressionUrlAuthorizationConfigurer** 提供。看看最前面的如何配置 **FilterSecurityInterceptor** 实例的 SecurityMetadataSource。

```java
@Override
final ExpressionBasedFilterInvocationSecurityMetadataSource createMetadataSource(H http) {
    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = REGISTRY.createRequestMap();
    if (requestMap.isEmpty()) {
        throw new IllegalStateException(
            "At least one mapping is required (i.e. authorizeRequests().anyRequest().authenticated())");
    }
    return new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,getExpressionHandler(http));
}
```

**REGISTRY** 正是 **ExpressionInterceptUrlRegistry**。可以看看 **http.authorizeRequests()** 返回值类型，正是 **ExpressionInterceptUrlRegistry**。而该类，正是 **ExpressionUrlAuthorizationConfigurer** 类的子类。

```java
public class ExpressionInterceptUrlRegistry extends ExpressionUrlAuthorizationConfigurer<H>.
    AbstractInterceptUrlRegistry<ExpressionInterceptUrlRegistry, AuthorizedUrl>
```

那再来看一下 **requestMap** 是如何创建的。requestMap 是由抽象类 **AbstractConfigAttributeRequestMatcherRegistry** 创建的。这个抽象类是 **AbstractInterceptUrlRegistry** 类的基类，而 **AbstractInterceptUrlRegistry** 类，正是 **ExpressionInterceptUrlRegistry** 类的基类。

```java
final LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> createRequestMap() {
    if (unmappedMatchers != null) {
        throw new IllegalStateException(
            "An incomplete mapping was found for "
            + unmappedMatchers
            + ". Try completing it with something like requestUrls().<something>.hasRole('USER')");
    }

    LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
    for (UrlMapping mapping : getUrlMappings()) {
        RequestMatcher matcher = mapping.getRequestMatcher();
        Collection<ConfigAttribute> configAttrs = mapping.getConfigAttrs();
        requestMap.put(matcher, configAttrs);
    }
    return requestMap;
}
```

正是把前面 http 配置的 **authorizeRequests**，转化为 **UrlMappings**，然后再转换为 **LinkedHashMap<RequestMatcher, Collection<ConfigAttribute\>>**。

而 **ExpressionUrlAuthorizationConfigurer** 类的 interceptUrl，正是向 UrlMappings 中添加内容。

**SecurityMetadataSource** 的初始化基本完成。

### AccessDecisionManager

默认的 AccessDecisionManager 初始化也是由 **AbstractInterceptUrlConfigurer** 类创建的。

```java
private AccessDecisionManager createDefaultAccessDecisionManager(H http) {
    AffirmativeBased result = new AffirmativeBased(getDecisionVoters(http));
    return postProcess(result);
}
private AccessDecisionManager getAccessDecisionManager(H http) {
    if (accessDecisionManager == null) {
        accessDecisionManager = createDefaultAccessDecisionManager(http);
    }
    return accessDecisionManager;
}
```

如果没有设置自定义的 accessDecisionManager，则会创建默认的 **AffirmativeBased** 实例。

### AccessDecisionVoter

**AccessDecisionVoters** 也是由**AbstractInterceptUrlConfigurer** 类的抽象方法 **getDecisionVoters** 提供。

```java
abstract List<AccessDecisionVoter<? extends Object>> getDecisionVoters(H http);
```

而真正的逻辑实现，是由其子类 **ExpressionUrlAuthorizationConfigurer** 提供。

```java
@Override
@SuppressWarnings("rawtypes")
final List<AccessDecisionVoter<? extends Object>> getDecisionVoters(H http) {
    List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<AccessDecisionVoter<? extends Object>>();
    WebExpressionVoter expressionVoter = new WebExpressionVoter();
    expressionVoter.setExpressionHandler(getExpressionHandler(http));
    decisionVoters.add(expressionVoter);
    return decisionVoters;
}
```

可以看到，decisionVoters 只有 WebExpressionVoter 实例。

### ExpressionHandler

**ExpressionHandler** 的初始化逻辑是由 **ExpressionUrlAuthorizationConfigurer** 类的 **getExpressionHandler** 方法实现。

```java
private SecurityExpressionHandler<FilterInvocation> getExpressionHandler(H http) {
    if (expressionHandler == null) {
        DefaultWebSecurityExpressionHandler defaultHandler = new DefaultWebSecurityExpressionHandler();
        AuthenticationTrustResolver trustResolver = http
            .getSharedObject(AuthenticationTrustResolver.class);
        if (trustResolver != null) {
            defaultHandler.setTrustResolver(trustResolver);
        }
        ApplicationContext context = http.getSharedObject(ApplicationContext.class);
        if (context != null) {
            String[] roleHiearchyBeanNames = context.getBeanNamesForType(RoleHierarchy.class);
            if (roleHiearchyBeanNames.length == 1) {
                defaultHandler.setRoleHierarchy(context.getBean(roleHiearchyBeanNames[0], RoleHierarchy.class));
            }
            String[] grantedAuthorityDefaultsBeanNames = context.getBeanNamesForType(GrantedAuthorityDefaults.class);
            if (grantedAuthorityDefaultsBeanNames.length == 1) {
                GrantedAuthorityDefaults grantedAuthorityDefaults = context.getBean(grantedAuthorityDefaultsBeanNames[0], GrantedAuthorityDefaults.class);
                defaultHandler.setDefaultRolePrefix(grantedAuthorityDefaults.getRolePrefix());
            }
            String[] permissionEvaluatorBeanNames = context.getBeanNamesForType(PermissionEvaluator.class);
            if (permissionEvaluatorBeanNames.length == 1) {
                PermissionEvaluator permissionEvaluator = context.getBean(permissionEvaluatorBeanNames[0], PermissionEvaluator.class);
                defaultHandler.setPermissionEvaluator(permissionEvaluator);
            }
        }

        expressionHandler = postProcess(defaultHandler);
    }

    return expressionHandler;
}
```

首先，**expressionHandler** 是 **DefaultWebSecurityExpressionHandler** 实例。

如果存在 **AuthenticationTrustResolver** 实例，则设置到 **DefaultWebSecurityExpressionHandler** 实例中。

如果存在 **RoleHierarchy** 实例（**隶属关系角色**），同样设置到 **DefaultWebSecurityExpressionHandler** 实例中。

如果存在 **GrantedAuthorityDefaults** 实例（**设置角色前缀的类**），则设置该实例中定义的角色前缀到 **DefaultWebSecurityExpressionHandler** 实例中。

如果存在 **PermissionEvaluator** 实例，同样设置到 **DefaultWebSecurityExpressionHandler** 实例中。

PermissionEvaluator 用于确定用户是否具有权限或给定域对象的权限。

## 登陆成功页面跳转原理

首先，框架默认的 **AuthenticationSuccessHandler** 为 **SavedRequestAwareAuthenticationSuccessHandler。**

判断当前Request是否缓存（另外需要看 Spring Security 是否开启了 Request 缓存，默认是开启的）。

```java
SavedRequest savedRequest = requestCache.getRequest(request, response);

if (savedRequest == null) {
    super.onAuthenticationSuccess(request, response, authentication);

    return;
}
```

如果 Spring Security 关闭了 Request 缓存，或者当前 Request 并没有被缓存，那么就走**默认的认证成功逻辑**。

否则，继续根据 **alwaysUseDefaultTargetUrl** 判断是否永远重定向到 **defaultTargetUrl；**亦或是，如果配置了**targetUrlParameter 且当前 request 存在该参数值**，那么，**从缓存中移除当前request，并走默认的认证成功逻辑。**

```java
String targetUrlParameter = getTargetUrlParameter();
if (isAlwaysUseDefaultTargetUrl()
    || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
    requestCache.removeRequest(request, response);
    super.onAuthenticationSuccess(request, response, authentication);

    return;
}    
```

以上情况都不满足，**即 Spring Security 开启了 Request 缓存，且当前 request 被缓存了，框架即重定向到缓存 request 对应的地址**。

```java
String targetUrl = savedRequest.getRedirectUrl();
logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
getRedirectStrategy().sendRedirect(request, response, targetUrl);
```

默认的认证成功逻辑，就是判断要重定向的地址这个逻辑。

```java
protected String determineTargetUrl(HttpServletRequest request,
                                    HttpServletResponse response) {
    if (isAlwaysUseDefaultTargetUrl()) {
        return defaultTargetUrl;
    }

    // Check for the parameter and use that if available
    String targetUrl = null;

    if (targetUrlParameter != null) {
        targetUrl = request.getParameter(targetUrlParameter);

        if (StringUtils.hasText(targetUrl)) {
            logger.debug("Found targetUrlParameter in request: " + targetUrl);

            return targetUrl;
        }
    }

    if (useReferer && !StringUtils.hasLength(targetUrl)) {
        targetUrl = request.getHeader("Referer");
        logger.debug("Using Referer header: " + targetUrl);
    }

    if (!StringUtils.hasText(targetUrl)) {
        targetUrl = defaultTargetUrl;
        logger.debug("Using default Url: " + targetUrl);
    }

    return targetUrl;
}
```

就是如果 **alwaysUseDefaultTargetUrl** 为true，则重定向 **defaultTargetUrl；**如果配置了 **targetUrlParameter** 且其对应的值不为空，则重定向到该地址；如果配置的 **useReferer** 为 **true 且其值不为空**，则重定向到该地址；否则，则重定向到 **defaultTargetUrl**。

![loginsuccessdefault](images/loginsuccessdefault.jpg)

























