package Marketplace;

import java.util.Objects;

public class Product {
    private final int ID;
    private final String name;
    private final int price;

    private static int idCounter = 1;

    public Product(String name, int price) {
        this.ID = idCounter++;
        this.name = name;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID, name, price);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Product otherProduct)
            return hashCode() == otherProduct.hashCode();
        return false;
    }

    @Override
    public String toString() {
        return String.format("#%d %s (Price: %d)", ID, name, price);
    }
}
