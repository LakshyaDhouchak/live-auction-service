package com.lakshya.auction.live_auction_service.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lakshya.auction.live_auction_service.Entity.User;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}
