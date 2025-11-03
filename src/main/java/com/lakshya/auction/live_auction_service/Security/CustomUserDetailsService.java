package com.lakshya.auction.live_auction_service.Security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.lakshya.auction.live_auction_service.Repository.UserRepo;

public class CustomUserDetailsService implements UserDetailsService {
    // define the properties
    private final UserRepo repo;

    // define the constructor
    public CustomUserDetailsService(UserRepo repo){
        this.repo = repo;
    }
    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {
        return repo.findByEmail(Email).orElseThrow(()-> new UsernameNotFoundException("User not found with email: \" + username"));
    }
    
}
