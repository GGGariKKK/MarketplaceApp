package Menu;


import Marketplace.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private final Marketplace marketplace;

    private List<Customer> customers;

    private List<Product> products;

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
        while (promptMenu()) ;
    }

    private boolean promptMenu() {
        System.out.printf("""
                                
                %1$sMain menu%1$s
                Choose the operation to perform:
                1) Add customer
                2) Add product
                3) Buy a product
                4) Display list of all customers
                5) Display list of all products
                6) Display list of customer's products by customer id
                7) Display list of customers that bought product by product id
                8) Remove customer
                9) Remove product
                10) Exit
                %2$s
                """, "-".repeat(40), "-".repeat(89));

        var chosenOption = requirePositiveIntegerInput("Your option: ");

        System.out.println("-".repeat(89));

        try {
            switch (chosenOption) {
                case 1 -> addCustomer();
                case 2 -> addProduct();
                case 3 -> buyProduct();
                case 4 -> customers.forEach(System.out::println);
                case 5 -> products.forEach(System.out::println);
                case 6 -> customerProducts();
                case 7 -> customersThatBoughtTheProduct();
                case 8 -> removeCustomer();
                case 9 -> removeProduct();
                case 10 -> {
                    return false;
                }
            }
        } catch (Exception ex) {
            System.out.printf("Error (%s): %s", ex.getClass().getSimpleName(), ex.getMessage());
        }

        return true;
    }

    private void addCustomer() {
        var firstName = requireStringInput("Enter first name: ");
        var lastName = requireStringInput("Enter last name: ");
        var balance = requirePositiveIntegerInput("Enter the balance: ");

        var customer = new Customer(firstName, lastName, balance);
        customers.add(customer);

        System.out.println("Customer " + customer + " added successfully");
    }

    private void addProduct() {
        var name = requireStringInput("Enter the name of the product: ");
        var price = requirePositiveIntegerInput("Enter the price: ");

        var product = new Product(name, price);
        products.add(product);

        System.out.println("Product " + product + " was added successfully");
    }

    private void buyProduct() throws CustomerNotFoundException, ProductNotFoundException, NotEnoughFundsException {
        var customerID = requirePositiveIntegerInput("Enter the id of a customer: ");
        var productID = requirePositiveIntegerInput("Enter the id of a product: ");

        var foundCustomer = customers.stream().filter(curr -> curr.getID() == customerID).findFirst();
        if (foundCustomer.isEmpty())
            throw new CustomerNotFoundException("Customer with ID: " + customerID + " was not found");

        var foundProduct = products.stream().filter(curr -> curr.getID() == productID).findFirst();
        if (foundProduct.isEmpty())
            throw new ProductNotFoundException("Product with ID: " + productID + " was not found");

        var customer = foundCustomer.get();
        var product = foundProduct.get();

        if (customer.getBalance() < product.getPrice())
            throw new NotEnoughFundsException("not enough money to buy product");

        customer.setBalance(customer.getBalance() - product.getPrice());
        marketplace.buyProduct(customerID, productID);

        System.out.printf("Operation successful!\nCustomer: %s bought product: %s", customer, product);
    }

    private void customersThatBoughtTheProduct() throws ProductNotFoundException {
        var productID = requirePositiveIntegerInput("Enter the product ID: ");
        var customersIDs = marketplace.getCustomersIDsThatBoughtTheProduct(productID);

        var foundProductList = products.stream().filter(curr -> curr.getID() == productID).findFirst();
        if (foundProductList.isEmpty())
            throw new ProductNotFoundException("The product with ID " + productID + " was not found");

        var customers = this.customers.stream().filter(cs -> customersIDs.contains(cs.getID())).toList();

        System.out.printf("Customers that bought the product %s:\n%s\n", foundProductList.get(), customers.stream().map(cs -> cs.toString()).collect(Collectors.joining("\n")));
    }

    private void customerProducts() throws CustomerNotFoundException {
        var customerID = requirePositiveIntegerInput("Enter the id of a customer: ");
        var productIDs = marketplace.getCustomerProductsIDs(customerID);

        var foundCustomersList = customers.stream().filter(curr -> curr.getID() == customerID).findFirst();
        if (foundCustomersList.isEmpty())
            throw new CustomerNotFoundException("The customer with ID " + customerID + " was not found");


        var products = this.products.stream().filter(product -> productIDs.contains(product.getID())).toList();

        System.out.printf("Customer: %s has bought such products:\n%s\n", foundCustomersList.get(), products.stream().map(c -> c.toString()).collect(Collectors.joining("\n")));
    }

    private void removeCustomer() {
        var customerID = requirePositiveIntegerInput("Enter the ID of a customer: ");
        customers = customers.stream().filter(cs -> cs.getID() != customerID).collect(Collectors.toList());
        marketplace.removeCustomer(customerID);
        System.out.println("Removal successful");
    }

    private void removeProduct() {
        var productID = requirePositiveIntegerInput("Enter the ID of a product: ");
        products = products.stream().filter(product -> product.getID() != productID).collect(Collectors.toList());
        marketplace.removeProductFromCustomersPurchases(productID);
        System.out.println("Removal successful");
    }

    private int requirePositiveIntegerInput(String message) {
        System.out.print(message);

        var scanner = new Scanner(System.in);
        String temp = "";
        while (!(temp = scanner.nextLine()).matches("\\d+"))
            System.out.print("Try again. " + message);

        return Integer.parseInt(temp);
    }

    private String requireStringInput(String message) {
        System.out.print(message);
        var scanner = new Scanner(System.in);

        String input = "";
        while ((input = scanner.nextLine()).isBlank())
            System.out.print("Try again. " + message);

        return input;
    }
}
