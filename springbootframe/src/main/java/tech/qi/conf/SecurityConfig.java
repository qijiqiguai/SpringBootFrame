package tech.qi.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.qi.core.Constants;
import tech.qi.dal.repository.UserRepository;



/**
 * @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
 */
@Configuration
class SecurityConfig {

    @Configuration
    @Order(Ordered.LOWEST_PRECEDENCE - 90)
    protected static class UserAuthenticationConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UserRepository userRepository;

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
                        .antMatchers("/webview/pub/**").permitAll()
                        .antMatchers("/api/v1/order/**", "/api/v1/student/**").hasRole(Constants.USER_ROLE_STUDENT)
                        .antMatchers("/api/v1/file/**", "/api/v1/auth/password/change", "/api/v2/auth/password/change").hasRole(Constants.SPRING_SECURITY_USER_ROLE)
                        .antMatchers("/api/v1/auth/**").permitAll() //Put this here is on purpose: AntMatcher apply rule in order
                        .antMatchers("/api/v2/auth/**").permitAll() //Put this here is on purpose: AntMatcher apply rule in order
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
            auth.userDetailsService(userRepository)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
    }

}


