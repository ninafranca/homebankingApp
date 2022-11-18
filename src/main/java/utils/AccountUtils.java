package utils;

import com.minddhub.homebanking.models.Account;

import java.util.Random;

public final class AccountUtils {

    private AccountUtils() {
    }

    //Actualizo el balance de una Account
    public static void updateBalance(Account account, double amount){
        double total = account.getBalance();
        //Los credit se restan por defecto porque tienen un - adelante,
        //si no estuvieran seteados con ese menos, debería cambiar la siguiente línea por el switch
        total += amount;
        /*switch (type) {
            case DEBIT : total += amount;
                break;
            case CREDIT : total += amount;
                break;
        }*/
        account.setBalance(total);
    }

    //Creo un número al azar en un rango de X a X para pasar por parámetro
    //a generateRandomNumbers(el_numero_al_azar_aca) asi me crea esa cantidad de números random
    //que uso para crear un número de Account. Toma el min incluido y el max - 1
    public static int generateRandomNumber(int min, int max) {
        Random random = new Random();
        int randomNumber = random.nextInt(max - min) + min;
        return randomNumber;
    }

}