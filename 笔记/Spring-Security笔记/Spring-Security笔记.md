# 扩展

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
http.formLogin() // 表单登录
    .loginPage("/login") // 配置哪个url是登录页 GET
    .loginProcessingUrl("/login") // 配置哪个是提交登陆url POST
    .successForwardUrl("/succeed") // 登陆成功跳转的url POST
    .failureForwardUrl("/fail") // 登陆失败跳转的url PO
 
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



## FilterSecurityInterceptor 

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

























