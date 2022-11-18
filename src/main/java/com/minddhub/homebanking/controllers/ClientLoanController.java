package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.dtos.ClientLoanDTO;
import com.minddhub.homebanking.models.Client;
import com.minddhub.homebanking.repositories.ClientLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta de inicio
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    //GET de todos los ClientLoans
    @GetMapping("/clientLoans")
    public List<ClientLoanDTO> getAccounts() {
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en AccountDTO
        return clientLoanRepository.findAll().stream().map(ClientLoanDTO::new).collect(toList());
    }

    //GET de un ClientLoan por ID
    @GetMapping("/clientLoans/{id}")
    public ClientLoanDTO getClientLoans(@PathVariable Long id) {
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    //GET de los ClientLoans de un Client
    @GetMapping("/clientLoans/client")
    public List<ClientLoanDTO> getClientLoansByClient(@RequestParam Client client) {
        return clientLoanRepository.findByClient(client).stream().map(ClientLoanDTO::new).collect(toList());
    }

    //GET de los ClientLoans con amount mayor a X
    @GetMapping("/clientLoans/amountGreaterThan")
    public List<ClientLoanDTO> getClientLoansByAmountGreaterThan(@RequestParam double amount) {
        return clientLoanRepository.findByAmountGreaterThan(amount).stream().map(ClientLoanDTO::new).collect(toList());
    }

    @GetMapping("/clientLoans/client/amountLessThan")
    public List<ClientLoanDTO> getByClientAndAmountLessThan(@RequestParam Client client, @RequestParam double amount) {
        return clientLoanRepository.findByClientAndAmountLessThan(client, amount).stream().map(ClientLoanDTO::new).collect(toList());
    }

}
