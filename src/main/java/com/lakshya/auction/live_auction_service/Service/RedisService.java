package com.lakshya.auction.live_auction_service.Service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    // define the properties
    private final StringRedisTemplate redisTemplate;
    private static final String KeyPrefix = "auction:currentBid";

    // define the constructor
    public RedisService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    // define the getKey() methord
    public String getKey(Long auctionItemId){
        return KeyPrefix + auctionItemId;
    }

    // define the setAuction() methord
    public void setAuction(Long auctionItemId , BigDecimal amount){
        String key = getKey(auctionItemId);
        redisTemplate.opsForValue().set(key, amount.toPlainString());
    }

    // define the GetAuction() methord
    public Optional<BigDecimal> getAuction(Long auctionItemId){
        String key = getKey(auctionItemId);
        String StrAmount = redisTemplate.opsForValue().get(key);

        // define the condition
        if(StrAmount == null){
            return Optional.empty();
        }
        try{
            return Optional.of(new BigDecimal(StrAmount));
        }
        catch(NumberFormatException ex){
            return Optional.empty();
        }
    }

    // define the delete() methord
    public void deleteBid(Long auctionItemId){
        String Key = getKey(auctionItemId);
        redisTemplate.delete(Key);
    }
}
