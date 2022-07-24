package Menu;


import Marketplace.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Class that represents all the logic and operations to work with {@link Marketplace} through command line interface
 */
public class Menu {
    private final Marketplace marketplace;

    private List<Customer> customers;

    private List<Product> products;

    /**
     * One and the only one constructor for creating menu object
     * @param fillWithDefaultValues {@code true} to fill this instance of an object with 3 default values of products and customers, {@code false} otherwise
     */
    public Menu(boolean fillWithDefaultValues) {
        marketplace = new Marketplace();
        customers = new ArrayList<>();
        products = new ArrayList<>();

        if (fillWithDefaultValues) {
            customers.addAll(List.of(
                    new Customer("Hulda", "Serafini", 1000),
                    new Customer("Calixta", "Vilario", 1000),
                    new Customer("Maryanne", "Storstrand", 1000)
            ));
            products.addAll(List.of(
                    new Product("Milk", 15),
                    new Product("Eggs", 10),
                    new Product("Pasta", 11)
            ));
        }
    }

    /**
     * Start command line application
     */
    public void call() {
        while (promptMenu()) ;
    }

    /**
     * Textual interface of the menu. Every chosen item leads to corresponding action in the program
     * @return {@code false} to exit application and {@code true} otherwise
     */
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

    /**
     * Adding customer to the current object of {@link Menu} class. IDs of customers are generated automatically
     */
    private void addCustomer() {
        var firstName = requireStringInput("Enter first name: ");
        var lastName = requireStringInput("Enter last name: ");
        var balance = requirePositiveIntegerInput("Enter the balance: ");

        var customer = new Customer(firstName, lastName, balance);
        customers.add(customer);

        System.out.println("Customer " + customer + " added successfully");
    }

    /**
     * Adding product to the current object of {@link Menu} class. IDs of products are generated automatically
     */
    private void addProduct() {
        var name = requireStringInput("Enter the name of the product: ");
        var price = requirePositiveIntegerInput("Enter the price: ");

        var product = new Product(name, price);
        products.add(product);

        System.out.println("Product " + product + " was added successfully");
    }

    /**
     * Menu item that handles the purchase of a new product by customer
     * @throws CustomerNotFoundException if specified {@code customerID} is not contained in the current instance of menu object
     * @throws ProductNotFoundException if specified {@code productID} is not contained in the current instance of menu object
     * @throws NotEnoughFundsException if the price of the product with specified {@code productID} is higher than the balance of the customer with {@code customerID}
     */
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

    /**
     * Display list of customers that bought product by product id
     * @throws ProductNotFoundException if a product with specified {@code productID} is not present in the {@link Menu#products}(i.e. such product does not exist)
     */
    private void customersThatBoughtTheProduct() throws ProductNotFoundException {
        var productID = requirePositiveIntegerInput("Enter the product ID: ");
        var customersIDs = marketplace.getCustomersIDsThatBoughtTheProduct(productID);

        var foundProductList = products.stream().filter(curr -> curr.getID() == productID).findFirst();
        if (foundProductList.isEmpty())
            throw new ProductNotFoundException("The product with ID " + productID + " was not found");

        var customers = this.customers.stream().filter(cs -> customersIDs.contains(cs.getID())).toList();

        System.out.printf("Customers that bought the product %s:\n%s\n", foundProductList.get(), customers.stream().map(cs -> cs.toString()).collect(Collectors.joining("\n")));
    }

    /**
     * Display list of customer products by customer id
     * @throws CustomerNotFoundException if a customer with specified {@code customerID} is not present in the {@link Menu#customers}
     */
    private void customerProducts() throws CustomerNotFoundException {
        var customerID = requirePositiveIntegerInput("Enter the id of a customer: ");
        var productIDs = marketplace.getCustomerProductsIDs(customerID);

        var foundCustomersList = customers.stream().filter(curr -> curr.getID() == customerID).findFirst();
        if (foundCustomersList.isEmpty())
            throw new CustomerNotFoundException("The customer with ID " + customerID + " was not found");


        var products = this.products.stream().filter(product -> productIDs.contains(product.getID())).toList();

        System.out.printf("Customer: %s has bought such products:\n%s\n", foundCustomersList.get(), products.stream().map(c -> c.toString()).collect(Collectors.joining("\n")));
    }

    /**
     * Deletion of a customer by its {@code customerID}. Customer is being deleted both from {@link Menu#customers} and his/her records from {@link Marketplace}. In case specified {@code customerID} does not exist method does nothing and still prints to console about a successful removal
     */
    private void removeCustomer() {
        var customerID = requirePositiveIntegerInput("Enter the ID of a customer: ");
        customers = customers.stream().filter(cs -> cs.getID() != customerID).collect(Collectors.toList());
        marketplace.removeCustomer(customerID);
        System.out.println("Removal successful");
    }

    /**
     * Deletion of a product by its {@code productID}. Product is being deleted both from {@link Menu#products} and its records from {@link Marketplace}. In case specified {@code productID} does not exist method does nothing and still prints to console about a successful removal
     */
    private void removeProduct() {
        var productID = requirePositiveIntegerInput("Enter the ID of a product: ");
        products = products.stream().filter(product -> product.getID() != productID).collect(Collectors.toList());
        marketplace.removeProductFromCustomersPurchases(productID);
        System.out.println("Removal successful");
    }

    /**
     * Utility method to shorten the process of inputting data.
     * @param message message to show before asking to input data
     * @return positive integer value (including 0)
     */
    private int requirePositiveIntegerInput(String message) {
        System.out.print(message);

        var scanner = new Scanner(System.in);
        String temp = "";
        while (!(temp = scanner.nextLine()).matches("\\d+"))
            System.out.print("Try again. " + message);

        return Integer.parseInt(temp);
    }

    /**
     * Utility method to shorten the process of inputting data.
     * @param message message to show before asking to input data
     * @return non-empty {@link String} value
     */
    private String requireStringInput(String message) {
        System.out.print(message);
        var scanner = new Scanner(System.in);

        String input = "";
        while ((input = scanner.nextLine()).isBlank())
            System.out.print("Try again. " + message);

        return input;
    }
}
