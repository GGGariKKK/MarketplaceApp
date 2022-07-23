package Marketplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Marketplace {
    private final Map<Integer, List<Integer>> customersPurchases = new HashMap<>();

    public void buyProduct(int customerID, int productID) {
        customersPurchases.compute(customerID, (IDcustomer, boughtProductsList) -> {
            if (boughtProductsList == null) boughtProductsList = new ArrayList<>(List.of(productID));
            else if (!boughtProductsList.contains(productID)) boughtProductsList.add(productID);
            return boughtProductsList;
        });
    }

    public List<Integer> getCustomerProductsIDs(int customerID) {
        return customersPurchases.getOrDefault(customerID, List.of());
    }

    public List<Integer> getCustomersIDsThatBoughtTheProduct(int productID) {
        return customersPurchases.entrySet().stream().filter(entry -> entry.getValue().contains(productID)).map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
