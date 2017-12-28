package tech.qi.core.security;

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
        RestAuthenticationEntryPoint restAuthenticationEntryPoint;

        @Autowired
        private CustomAuthenticationProvider authProvider;

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
                        .antMatchers("/api/v1/auth/**").permitAll()
                        .antMatchers("/api/v1/**").hasRole(Constants.SPRING_SECURITY_BUYER_ROLE)
                        .anyRequest().authenticated()
                    .and().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                    .accessDeniedPage("/403")
                    .and().formLogin().loginPage("/login")
                        .successHandler(restAuthenticationSuccessHandler)
                        .failureHandler(restAuthenticationFailureHandler)
                        .permitAll()
                    .and().logout().logoutUrl("/logout")
                        .logoutSuccessHandler(restSuccessLogoutHandler)
                        .permitAll();
        }

        @Override
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(authProvider);
        }
    }

}


