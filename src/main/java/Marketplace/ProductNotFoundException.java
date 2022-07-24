package Marketplace;

import java.io.IOException;

public class ProductNotFoundException extends IOException {
    public ProductNotFoundException(String message){
        super(message);
    }
}
