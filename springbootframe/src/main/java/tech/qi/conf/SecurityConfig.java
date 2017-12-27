package tech.qi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.qi.core.Constants;
import tech.qi.dal.UserDetailsServiceImpl;



/**
 * @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 */
@Configuration
class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 90)
    protected static class UserAuthenticationConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        PasswordEncoder passwordEncoder;

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Autowired
        RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

        @Autowired
        RestSuccessLogoutHandler restSuccessLogoutHandler;

        @Autowired
        private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

        @Autowired
        private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        /**
         *  /css/**, /images/**, /js/**, and  favicon.ico are already configured as ignoring by Spring Boot
         **/
        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers("/plugins/**", "/up/**");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                        // AntMatcher 是按照从前到后的顺序来进行匹配的
                        .antMatchers("/pub/**").permitAll()
                        .antMatchers("/api/v1/file/**", "/api/v1/auth/password/change", "/api/v2/auth/password/change").hasRole(Constants.SPRING_SECURITY_USER_ROLE)
                        .antMatchers("/api/v1/auth/**").permitAll()
                        .antMatchers("/api/v2/auth/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(restAuthenticationEntryPoint)
                    .accessDeniedPage("/403")
                    .and()
                    .formLogin()
                    .loginPage("/api/v1/login")
                    .successHandler(restAuthenticationSuccessHandler)
                    .failureHandler(restAuthenticationFailureHandler)
                    .permitAll()
                    .and()
                    .logout()
                    .logoutUrl("/api/v1/logout")
                        .logoutSuccessHandler(restSuccessLogoutHandler);
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        }
    }

}


