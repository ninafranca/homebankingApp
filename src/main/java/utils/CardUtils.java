package utils;

public final class CardUtils {

    private CardUtils() {
    }

    public static String generateRandomNumbers(int quantity) {
        String cardNumber = "";
        for(int i = 0; i < quantity; i++) {
            int newNumber = (int) (Math.random() * 10);
            cardNumber += String.valueOf(newNumber);
        }
        return cardNumber;
    }

    public static String generateCardNumberWithHyphen() {
        String cardNumber = "";
        for(int i = 0; i < 3; i++) {
            cardNumber += generateRandomNumbers(4) + "-";
        }
        cardNumber += generateRandomNumbers(4);
        return cardNumber;
    }

}
