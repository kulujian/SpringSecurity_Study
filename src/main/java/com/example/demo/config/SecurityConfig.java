package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.handle.MyAccessDeniedHandler;

// 若要自訂登入邏輯則要繼承 WebSecurityConfigurerAdapter
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;
	
	// 自訂義的流程都要寫在這裡面
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 表單提交
		http.formLogin()
			// loginpage.html 表單 action 內容
			.loginProcessingUrl("/login")
			// 自定義登入頁面
			.loginPage("/loginpage")
			// 登入成功之後要造訪的頁面
			.successForwardUrl("/")  // welcome 頁面
			// 登入失敗之後要造訪的頁面
			.failureForwardUrl("/fail");

		// 授權認證
		http.authorizeHttpRequests()
			// 不需要被認證的頁面： /loginpage
			.antMatchers("/loginpage").permitAll()
			// 權限判斷
			// 必須要有 admin 權限才可以訪問
			.antMatchers("/adminpage").hasAuthority("admin")
			// 必須要有 manager 角色才可以訪問
			.antMatchers("/managerpage").hasRole("manager")
			// 其他任意角色都可以訪問
			.antMatchers("/employeepage").hasAnyRole("manager", "employee")
			// 其他的都要被認證
			.anyRequest().authenticated();
		
		// http.csrf().disable()    關閉 csrf 防護，不建意使用 
		
		// 登出
		http.logout()
			.deleteCookies("JSESSIONID")
			.logoutSuccessUrl("/loginpage")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));  // 可以使用任何的 HTTP 方法登出 ( 不建義，危險)
		
		// 異常處理，下面兩個只能二選一
		http.exceptionHandling()
			//.accessDeniedPage("/異常處理頁面");		// 請自行撰寫
			.accessDeniedHandler(myAccessDeniedHandler);
		
		
		// 勿忘我 (remember-me)，若是session過期時，還可以保持登入
		http.rememberMe()
			.userDetailsService(userDetailsService)
			.tokenValiditySeconds(60);  // 通常都會大於 session timeout 的時間，設定預設單位是秒

		// 回家作業，測試一下當你remember-me時間還在，你改了密碼還能不能登入
		
	}

	
	// 注意！規定！要建立密碼演算的實例！
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
