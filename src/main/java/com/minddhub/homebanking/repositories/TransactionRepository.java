package com.minddhub.homebanking.repositories;

import com.minddhub.homebanking.models.Transaction;
import com.minddhub.homebanking.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    //Buscar Transactions entre dos fechas
    List<Transaction> findByDateBetween(LocalDateTime date1, LocalDateTime date2);

    //Buscar Transactions con amounts entre valor1 y valor2
    List<Transaction> findByAmountBetween(double amount1, double amount2);

    //Buscar Transactions por type
    List<Transaction> findByType(TransactionType type);

}
