package Marketplace;

import java.io.IOException;

public class CustomerNotFoundException extends IOException {
    public CustomerNotFoundException(String message){
        super(message);
    }
}
