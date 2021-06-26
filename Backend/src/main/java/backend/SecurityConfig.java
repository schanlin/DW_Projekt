package backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll();
/*        http
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/user/**", "/student/**", "/klasse/**", "/subject/**").hasAuthority("Admin")
//                .antMatchers(HttpMethod.POST, "/test/**").hasAuthority("Admin")
//                .antMatchers(HttpMethod.POST, "/test").hasAuthority("Lehrende")
//                .antMatchers(HttpMethod.GET,  "/user/**", "user/{id}", "/student/**", "/klasse/**", "/subject/**", "/test/**").hasAuthority("Admin")
//                .antMatchers(HttpMethod.GET, "/user/{id}").hasAuthority("Lehrende")
//                .antMatchers(HttpMethod.GET, "/user/{id}").hasAuthority("Lernende")
//                .antMatchers(HttpMethod.PUT, "/user", "/student", "/klasse", "/subject").hasAuthority("Admin")
//                .antMatchers(HttpMethod.PUT, "/test").hasAuthority("Admin")
//                .antMatchers(HttpMethod.PUT, "/test").hasAuthority("Lehrende")
//                .antMatchers(HttpMethod.DELETE, "/user", "/student", "/klasse", "/subject").hasAuthority("Admin")
//                .antMatchers(HttpMethod.DELETE, "/test").hasAuthority("Admin")
//                .antMatchers(HttpMethod.DELETE, "/test").hasAuthority("Lehrende")
//                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic()
                .and().logout();

 */
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,passwort, true as enabled "
                        + "from user "
                        + "where username = ?")
                .authoritiesByUsernameQuery("select username,rolle "
                        + "from user "
                        + "where username = ?");
    }

/*   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,passwort, true as enabled "
                    + "from user "
                    + "where username = ?")
                .authoritiesByUsernameQuery("select username,rolle "
                    + "from user "
                    + "where username = ?");
    }


 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
