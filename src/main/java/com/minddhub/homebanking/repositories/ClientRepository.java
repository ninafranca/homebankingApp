package com.minddhub.homebanking.repositories;

import com.minddhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {

    //Busco por apellido ignorando las mayúsculas y minúsculas
    Optional<Client> findByLastNameIgnoreCase(String lastName);

    //Busco por nombre ignorando las mayúsculas y minúsculas
    List<Client> findByFirstNameIgnoreCase(String firstName);

    //Busco Client por nombre y e-mail ignorando mayúsculas y minúsculas en ambos
    Optional<Client> findByFirstNameIgnoreCaseAndEmailIgnoreCase(String firstName, String email);

    //Busco por email
    Optional<Client> findByEmail(String email);

}