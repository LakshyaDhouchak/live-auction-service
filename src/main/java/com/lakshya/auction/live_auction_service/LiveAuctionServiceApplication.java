package com.lakshya.auction.live_auction_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.lakshya.auction.live_auction_service.Repository")
@EntityScan(basePackages = "com.lakshya.auction.live_auction_service.Entity")

public class LiveAuctionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiveAuctionServiceApplication.class, args);
	}

}
