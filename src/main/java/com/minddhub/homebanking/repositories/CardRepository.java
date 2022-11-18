package com.minddhub.homebanking.repositories;

import com.minddhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {

    //Busco por número de Card
    Optional<Card> findByNumber(String number);

}
