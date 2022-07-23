package Marketplace;

import java.util.Objects;

public class Customer {
    private static int idCounter = 1;
    private final int ID;
    private final String firstName;
    private final String lastName;
    private int balance;

    public Customer(String firstName, String lastName, int balance) {
        this.ID = idCounter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, firstName, lastName, balance);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer otherCustomer)
            return hashCode() == otherCustomer.hashCode();
        return false;
    }

    @Override
    public String toString() {
        return String.format("#%d %s %s (Balance: %d)", ID, firstName, lastName, balance);
    }
}
