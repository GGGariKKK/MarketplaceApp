package Marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class that represents marketplace notion in the system. It is wrapped in {@link Menu.Menu} to operate it with command line interface
 */
public class Marketplace {

    /**
     * Essential data structure that stores pairs (Customer ID, List of product IDs that the customer has bought)
     */
    private final Map<Integer, List<Integer>> customersPurchases = new HashMap<>();


    /**
     * Registers {@code productID} in {@link Marketplace#customersPurchases}. If such customer does not exist, then adds a new record to the data structure. In other cases checks whether value of {@link Marketplace#customersPurchases} that is a {@link List} is present and adds the {@code productID} in negative case
     *
     * @param customerID ID of the customer that wants to buy the product
     * @param productID  ID of the product
     */
    public void buyProduct(int customerID, int productID) {
        customersPurchases.compute(customerID, (IDcustomer, boughtProductsList) -> {
            if (boughtProductsList == null) boughtProductsList = new ArrayList<>(List.of(productID));
            else if (!boughtProductsList.contains(productID)) boughtProductsList.add(productID);
            return boughtProductsList;
        });
    }

    /**
     * Getting list of integers that are represented as IDs of products that a customer with {@code customerID} has bought
     *
     * @param customerID ID of a customer to view his/her bought products
     * @return List of bought products IDs
     */
    public List<Integer> getCustomerProductsIDs(int customerID) {
        return customersPurchases.getOrDefault(customerID, List.of());
    }

    /**
     * Customers that has bought the certain product
     *
     * @param productID product ID
     * @return list of customer's IDs that have bought the product with ID {@code productID}
     */
    public List<Integer> getCustomersIDsThatBoughtTheProduct(int productID) {
        return customersPurchases.entrySet().stream().filter(entry -> entry.getValue().contains(productID)).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Removing customer with {@code customerID} from {@link Marketplace#customersPurchases}
     *
     * @param customerID ID of a customer to remove from data structure
     */
    public void removeCustomer(int customerID) {
        customersPurchases.remove(customerID);
    }

    /**
     * Cleaning {@link Marketplace#customersPurchases} after the product with ID {@code productID} was deleted from application
     *
     * @param productID ID of the deleted product to remove it from the data structure
     */
    public void removeProductFromCustomersPurchases(int productID) {
        customersPurchases.forEach((k, v) -> v.remove((Object) productID));
    }
}
