package com.lakshya.auction.live_auction_service.ExceptionHandling;

public class DataConflictException extends RuntimeException{
    // define the constructor
    public DataConflictException(String s){
        super(s);
    }
}
