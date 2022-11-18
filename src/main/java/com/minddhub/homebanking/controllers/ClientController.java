package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.models.Account;
import com.minddhub.homebanking.models.AccountType;
import com.minddhub.homebanking.models.Client;
import com.minddhub.homebanking.repositories.AccountRepository;
import com.minddhub.homebanking.repositories.ClientRepository;
import com.minddhub.homebanking.dtos.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
//En caso de configurar el role en el model de Client
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta base
@RequestMapping("/api")
public class ClientController {

    //@Autowired: permite inyectar unas dependencias con otras
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //Defino la ruta donde hacer el GET de Clients después de la ruta base
    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en ClientDTO
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    //GET de Client por ID
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    //GET del Client que está autenticado
    @GetMapping("/clients/current")
    public ClientDTO getClient(Authentication authentication) {
        Client client = this.clientRepository.findByEmail(authentication.getName()).get();
        return new ClientDTO(client);
    }

    //GET de un Client por apellido
    @GetMapping("/clients/lastName")
    public ClientDTO getClientByLastName(@RequestParam String lastName) {
        return clientRepository.findByLastNameIgnoreCase(lastName).map(ClientDTO::new).orElse(null);
    }

    //GET de un Client por nombre
    @GetMapping("/clients/firstName")
    public List<ClientDTO> getClientByFirstName(@RequestParam String firstName) {
        return clientRepository.findByFirstNameIgnoreCase(firstName).stream().map(ClientDTO::new).collect(toList());
    }

    //GET de un Client por email
    @GetMapping("/clients/email")
    public ClientDTO getClient(@RequestParam String email) {
        return clientRepository.findByEmail(email).map(ClientDTO::new).orElse(null);
    }

    //GET de un Client por nombre y email
    @GetMapping("/clients/firstName/email")
    public ClientDTO getClientByFirstNameAndEmail(@RequestParam String firstName,
                                                  @RequestParam String email) {
        return clientRepository.findByFirstNameIgnoreCaseAndEmailIgnoreCase(firstName, email).map(ClientDTO::new).orElse(null);
    }

    //POST para crear un Client
    @PostMapping("/clients")
    public ResponseEntity<Object> createClient(@RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestParam String email,
                                               @RequestParam String password) {
        //Si falta algún parámetro devuelvo mensaje de error
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        //Si ya existe el email en la DB devuelvo error
        if (clientRepository.findByEmail(email).isPresent()) {
            return new ResponseEntity<>("e-mail already exists", HttpStatus.FORBIDDEN);
        }
        //Valido que la contraseña tenga mínimo 6 caracteres y al menos una mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return new ResponseEntity<>("Password must contain at least 6 characters and one capital letter", HttpStatus.FORBIDDEN);
        }
        if ((password.length() < 6)) {
            return new ResponseEntity<>("Password must contain at least 6 characters and one capital letter", HttpStatus.FORBIDDEN);
        }
        //Englobo toodo en un primer try catch
        try {
            //Intento crear un Client nuevo, si falla atrapo el error en un catch
            try {
                //En caso de configurar el role en el model de Client tenría que pasar:
                //Client client = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password), AuthorityUtils.createAuthorityList("CLIENT")));
                Client client = clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
                //Declaro la variable account
                //Compruebo que no esté repetido el número de cuenta, si lo está se realiza nuevamente el ciclo
                Account account;
                //Instancio un Account y hago un loop que se rompe solo si
                //valido que su número creado no existe en la DB y ahi recién la guardo
                do {
                    //Le paso el AccountType como CURRENT a la nueva instancia de Account
                    account = new Account(AccountType.CURRENT, 0.00, client);
                } while(accountRepository.findByNumber(account.getNumber()).isPresent());
                accountRepository.save(account);
                new ResponseEntity<>("Account created successfully", HttpStatus.CREATED);
            } catch (Exception exception) {
                //Imprimir error si falló el registro
                System.out.println(exception.getMessage());
                exception.printStackTrace();
                return new ResponseEntity<>("Client registration error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
            //Si toodo salió bien. Retorno un estado CREATED que es correcto
            return new ResponseEntity<>("Client created successfully", HttpStatus.CREATED);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return new ResponseEntity<>("Client sign up error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}