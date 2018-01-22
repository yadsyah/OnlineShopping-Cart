package com.diansetiyadi.myapp.config;


import com.diansetiyadi.myapp.authentication.MyDBAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    MyDBAuthenticationService myDBAuthenticationService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myDBAuthenticationService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        //THE PAGES require login as Employee or Manager
        //if no login , it will redirect to /login page
        http.authorizeRequests().antMatchers("/orderList", "/order", "/accountInfo").access("hasAnyRole('ROLE_EMPLOYEE','ROLE_MANAGER')");


        //For Manager Only
        http.authorizeRequests().antMatchers("/product").access("hasRole('ROLE_MANAGER')");

        //when user has logged in as XX
        //but access a page require role YY
        //Access Denied Excpetion will throw
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        //config for login form
        http.authorizeRequests().and().formLogin()
                //submit url or login page
                .loginProcessingUrl("/j_spring_security_check") //submit url
                .loginPage("/login")
                .defaultSuccessUrl("/accountInfo")
                .failureUrl("/login?error=true")
                .usernameParameter("userName")
                .passwordParameter("password")
                //Config for Logout Page
                //(Go to home page)
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
    }
}
