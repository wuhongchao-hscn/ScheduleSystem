package com.hitachi.schedule.config.configuration;


import com.hitachi.schedule.controller.handler.gsax.GSAXS010Handler;
import com.hitachi.schedule.service.impl.GSAXSLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private GSAXSLoginService loginService;

    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()  //authorizeRequests定义哪些URL需要被保护、哪些不需要被保护
//                .antMatchers("/**").permitAll() //任意资源 全放行 测试用
                .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll() //静态资源 全放行
                .antMatchers("/common**").permitAll() // 共通 全放行
                .anyRequest().access("@uriAccessService.canAccess(request, authentication)")

                .and().formLogin()    //需要登录
                .loginPage("/GSAXS010Display")  //需要登录到跳转页面
                .loginProcessingUrl("/GSAXS010Login") //请求时，发出的action请求内容
                .usernameParameter("strUserId")
                .passwordParameter("strUserPassword")
                .permitAll()   //登录页面每个人都可以访问
                .successHandler(loginSuccessHandler()) // 请求成功之后，存储session，并重定向到menu画面

                .and().logout()
                .logoutSuccessUrl("/commonLogoutSucess") //登出后的默认网址
                .permitAll() //登出页面每个人都可以访问
                .invalidateHttpSession(true) //销毁session
                .deleteCookies() // 删除cookie

                .and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // 以session方式管理csrf

                .and().rememberMe() //登录后记住用户下次自动登录
                .tokenValiditySeconds(1209600) //token有效时间
                .tokenRepository(tokenRepository())//制定记住登录信息所使用的数据源
                .userDetailsService(loginService);
        ;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder());
        auth.eraseCredentials(false);//不要删除凭证，以便记住用户
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);  //给密码加密
    }

    @Bean
    public JdbcTokenRepositoryImpl tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 如果token表不存在，使用下面语句可以初始化该表；若存在，请注释掉这条语句，否则会报错。
//        tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }

    @Bean
    public GSAXS010Handler loginSuccessHandler() {
        return new GSAXS010Handler();
    }
}