package com.minddhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;
import utils.CardUtils;

import javax.persistence.*;
import java.time.LocalDate;

//Creo la Entity de Card
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private String cardHolder;
    private CardType type;
    private CardColor color;
    private String number;
    private String cvv;
    private LocalDate thruDate;
    private LocalDate fromDate;

    //Relación muchos a uno entre Cards y Client
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    public Card() {
    }

    public Card(Client client, CardType type, CardColor color) {
        this.client = client;
        //Genera cardHolder por defecto con el nombre de Client y su apellido
        this.cardHolder = client.getFirstName() + " " + client.getLastName();
        this.type = type;
        this.color = color;
        //Genero 16 números random para la Card con guiones
        this.number = CardUtils.generateCardNumberWithHyphen();
        //Genero 3 números random para crear el CVV
        this.cvv = CardUtils.generateRandomNumbers(3);
        //A partir de la fecha de creación de la Card, le agrego 5 años para su fecha de vencimiento
        this.thruDate = LocalDate.now().plusYears(5);
        //Fecha de creación de la Card por defecto
        this.fromDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public CardType getType() {
        return type;
    }

    public void setCard(CardType card) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}