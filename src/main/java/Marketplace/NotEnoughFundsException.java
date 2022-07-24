package Marketplace;

import java.io.IOException;

public class NotEnoughFundsException extends IOException {
    public NotEnoughFundsException(String message){
        super(message);
    }
}
