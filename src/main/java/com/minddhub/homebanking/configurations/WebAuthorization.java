package com.minddhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration

//Seteo las configuraciones para las autorizaciones según el rol
public class WebAuthorization{

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Si quiero acceder con ("/web/index.html", "SIN ROL") puedo hacerlo, pero
        // si cambio el orden y pongo esta regla primero '.antMatchers("/**").hasAuthority("CLIENT")'
        // me va a rechazar la autorización, por que analiza primero esa linea
        http.authorizeRequests()
                .antMatchers("/web/index.html", "/web/css/**", "/web/img/**", "/web/js/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login", "/api/clients").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients/current/accounts/", "/api/clients/current/accounts/accountType/" ).hasAuthority("CLIENT")
                .antMatchers("/**").hasAuthority("CLIENT");


        //axios.post('/api/login',`email=${this.email}&password=${this.password}`)
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.exceptionHandling().authenticationEntryPoint( (req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED) );
        http.formLogin().successHandler((req, res, exc) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

        return http.build();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}