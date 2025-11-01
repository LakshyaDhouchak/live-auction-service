package com.lakshya.auction.live_auction_service.ExceptionHandling;

public class BidValidationException  extends RuntimeException{
    // calling the constructor
    public BidValidationException(String s){
        super(s);
    }
}
