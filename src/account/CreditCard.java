package src.account;

public class CreditCard {
    // private variables
    private String cardDigits; // The digits of the credit card number
    private String cardExpirationDate; // The expiration date of the credit card
    private String cardSecurityCode; // The security code of the credit card

    // constructor
    public CreditCard(String cardDigits, String cardExpirationDate, String cardSecurityCode) {
        this.cardDigits = cardDigits;
        this.cardExpirationDate = cardExpirationDate;
        this.cardSecurityCode = cardSecurityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditCard that = (CreditCard) o;
        return cardDigits.equals(that.cardDigits) &&
               cardExpirationDate.equals(that.cardExpirationDate) &&
               cardSecurityCode.equals(that.cardSecurityCode);
    }

    // getters
    public String getCardDigits() {
        return cardDigits;
    }

    public String getCardExpirationDate() {
        return cardExpirationDate;
    }

    public String getCardSecurityCode() {
        return cardSecurityCode;
    }
}
