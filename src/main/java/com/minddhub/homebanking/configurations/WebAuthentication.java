package com.minddhub.homebanking.configurations;

import com.minddhub.homebanking.models.Client;
import com.minddhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration

//Creo la clase que contiene las configuraciones para realizar autenticaciones en este caso para los Clients
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Configuration

    class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

        @Autowired
        ClientRepository clientRepository;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(inputName -> {
                //Busco en la DB el Client que se quiere autenticar por medio de su email y valido su password
                Optional<Client> client = clientRepository.findByEmail(inputName);
                if (client.isPresent()) {

                    return new User(client.get().getEmail(), client.get().getPassword(), AuthorityUtils.createAuthorityList("CLIENT"));
                    //En caso de configurar el role en el model de Client
                    //return new User(client.get().getEmail(), client.get().getPassword(), client.get().getAuthorities());
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + inputName);
                }
            });
        }

    }

}
