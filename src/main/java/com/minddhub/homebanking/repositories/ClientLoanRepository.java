package com.minddhub.homebanking.repositories;

import com.minddhub.homebanking.models.Client;
import com.minddhub.homebanking.models.ClientLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ClientLoanRepository extends JpaRepository<ClientLoan, Long> {

    //Buscar ClientLoans por Client
    List<ClientLoan> findByClient(Client client);

    //Busco ClientLoans con amount mayor a X
    List<ClientLoan> findByAmountGreaterThan(double amount);

    //Buscar por Client y amount menor a X
    List<ClientLoan> findByClientAndAmountLessThan(Client client, double amount);

}