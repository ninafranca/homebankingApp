package com.minddhub.homebanking.controllers;

import com.minddhub.homebanking.dtos.CardDTO;
import com.minddhub.homebanking.models.*;
import com.minddhub.homebanking.repositories.CardRepository;
import com.minddhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
//Defino la ruta base
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;

    //GET de todas las Cards
    @GetMapping("/cards")
    public List<CardDTO> getAccounts(){
        //stream() procesa y transforma cada elemento de la lista que retorna findAll()
        //y map() ejecuta el método constructor que está en AccountDTO
        return cardRepository.findAll().stream().map(CardDTO::new).collect(toList());
    }

    //GET de todas las Cards de un Client por ID
    @GetMapping("/clients/{id}/cards")
    public List<CardDTO> getCards(@PathVariable Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        if(optionalClient.isPresent()) {
            return optionalClient.get().getCards().stream().map(CardDTO::new).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    //Creo una Card con un POST
    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,
                                             @RequestParam CardColor cardColor,
                                             Authentication authentication) {
        try {
            //Busco Client y si existe valido que no tenga más de 3 Cards de este tipo
            Optional<Client> client = this.clientRepository.findByEmail(authentication.getName());
            //Si tiene 3 Cards o más devuelvo error
            if (client.get().getCards().stream().filter(card -> card.getType() == cardType).count() >= 3) {
                return new ResponseEntity<>("You already have 3 " + cardType.toString().toLowerCase() + " cards", HttpStatus.FORBIDDEN);
            } else {
                //Si tiene menos de 3 Cards le creo una nueva
                Card card;
                //Valido que no se cree una Card con number ya existente
                do {
                    card = new Card(client.get(), cardType, cardColor);
                } while(cardRepository.findByNumber(card.getNumber()).isPresent());
                cardRepository.save(card);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } catch (Exception exception) {
            //Si algo falla devuelvo error
            System.out.println(exception.getMessage());
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //DELETE de una Card por ID
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<Object> deleteCard(@PathVariable Long id) {
        cardRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
