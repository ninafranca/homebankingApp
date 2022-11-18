package utils;

import com.minddhub.homebanking.models.TransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public final class TransactionUtils {

    private TransactionUtils() {
    }

    //Devuelve un debit con un - adelante para diferenciar la Transaction de un credit
    public static double getAmountWithType(TransactionType type, double amount) {
        if(type == TransactionType.DEBIT) {
            return - amount;
        } else {
            return amount;
        }
    }

    //Devuelve la description de la Transaction más el type más la cantidad de días atrás que
    //se realizó o si se realizó hoy devuelve "Current"
    public static String getDescriptionWithDate(String description, TransactionType type, LocalDateTime date) {
        String finalDescription = description;
        switch (type) {
            case CREDIT : finalDescription += " - CREDIT";
                break;
            case DEBIT:  finalDescription += " - DEBIT";
                break;
            default: finalDescription += " - error";
        }
        if (date.toLocalDate().isEqual(LocalDate.now())) {
            finalDescription += " - Current";
        } else if (date.toLocalDate().isBefore(LocalDate.now())) {
            long daysBetween = ChronoUnit.DAYS.between(date.toLocalDate(), LocalDate.now());
            if(daysBetween == 1) {
                finalDescription += " - " + daysBetween + " day ago";
            } else {
                finalDescription += " - " + daysBetween + " days ago";
            }
        } else {
            finalDescription += "Error";
        }
        return finalDescription;
    }

}
