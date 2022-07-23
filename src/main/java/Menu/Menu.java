package Menu;

import Marketplace.Customer;
import Marketplace.Marketplace;
import Marketplace.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private final Marketplace marketplace;

    private final List<Customer> customers;

    private final List<Product> products;

    public Menu() {
        marketplace = new Marketplace();
        customers = new ArrayList<>(
                List.of(
                        new Customer("Hulda", "Serafini", 1000),
                        new Customer("Calixta", "Vilario", 1000),
                        new Customer("Maryanne", "Storstrand", 1000)
                )
        );
        products = new ArrayList<>(
                List.of(
                        new Product("Milk", 15),
                        new Product("Eggs", 10),
                        new Product("Pasta", 11)
                )
        );
    }

    public void call() {
        //TODO: reconsider this solution
        while(promptMenu());
    }

    private boolean promptMenu(){
        System.out.printf("""
                
                %1$sMain menu%1$s
                Choose the operation to perform:
                1) Buy a product
                2) Display list of all customers
                3) Display list of all products
                4) Display list of user products by user id
                5) Display list of users that bought product by product id
                6) Exit
                %2$s
                Your option:""", "-".repeat(40), "-".repeat(89));

        var chosenOption = requireInput("");

        System.out.println("-".repeat(89));

        switch (chosenOption) {
            case 1 -> buyProduct();
            case 2 -> customers.forEach(System.out::println);
            case 3 -> products.forEach(System.out::println);
            case 4 -> customerProducts();
            case 5 -> customersThatBoughtTheProduct();
            case 6 -> {return false;}
        }

        return true;
    }

    private void buyProduct() {
        var customerID = requireInput("Enter the id of a customer: ");
        var productID = requireInput("Enter the id of a product: ");

        var customer = customers.stream().filter(curr -> curr.getID() == customerID).toList().get(0);
        var product = products.stream().filter(curr -> curr.getID() == productID).toList().get(0);

        //TODO: correct runtime exception
        if(customer.getBalance() < product.getPrice())
            throw new RuntimeException("not enough money to buy product");

        customer.setBalance(customer.getBalance() - product.getPrice());
        marketplace.buyProduct(customerID, productID);

        System.out.printf("Operation successful!\nCustomer: %s bought product: %s", customer, product);
    }

    private void customersThatBoughtTheProduct() {
        var productID = requireInput("Enter the product ID: ");
        var customersIDs = marketplace.getCustomersIDsThatBoughtTheProduct(productID);

        var product = products.stream().filter(curr -> curr.getID() == productID).toList().get(0);
        var customers = this.customers.stream().filter(cs -> customersIDs.contains(cs.getID())).toList();

        System.out.printf("Customers that bought the product %s:\n%s\n", product, customers.stream().map(cs -> cs.toString()).collect(Collectors.joining("\n")));
    }

    private void customerProducts() {
        var customerID = requireInput("Enter the id of a customer: ");
        var productIDs = marketplace.getCustomerProductsIDs(customerID);

        var customer = customers.stream().filter(curr -> curr.getID() == customerID).toList().get(0);
        var products = this.products.stream().filter(product -> productIDs.contains(product.getID())).toList();
        System.out.printf("Customer: %s has bought products:\n%s\n", customer, products.stream().map(c -> c.toString()).collect(Collectors.joining("\n")));
    }

    private int requireInput(String message){
        System.out.print(message);
        return new Scanner(System.in).nextInt();
    }
}
