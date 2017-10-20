package com.jhon.rain.security.browser;

import com.jhon.rain.security.core.properties.SecurityConstants;
import com.jhon.rain.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * <p>功能描述</br> PC端安全配置 </p>
 *
 * @author jiangy19
 * @version v1.0
 * @FileName BrowserSecurityConfig
 * @date 2017/10/18 19:44
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired
	private AuthenticationSuccessHandler rainAuthenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler rainAuthenticationFailerHandler;

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
						.loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL) /** 自定义登录请求地址**/
						.loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM) /** 自定义登录验证的接口 **/
						.successHandler(rainAuthenticationSuccessHandler)
						.failureHandler(rainAuthenticationFailerHandler)
						.and()
						.authorizeRequests()
							.antMatchers(
									SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
									securityProperties.getBrowser().getLoginPage()
							).permitAll()
						.anyRequest()
						.authenticated()
						.and()
							.csrf().disable(); /** CSRF 功能禁用 **/
	}
}
