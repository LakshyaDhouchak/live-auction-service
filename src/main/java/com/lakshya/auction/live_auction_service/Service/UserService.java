package com.lakshya.auction.live_auction_service.Service;

import java.util.List;

import com.lakshya.auction.live_auction_service.DTO.CreateUserDTO;
import com.lakshya.auction.live_auction_service.DTO.UserResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.UserUpdateDTO;

public interface UserService {
    // define the methord
    UserResponseDTO createUser(CreateUserDTO dto);
    UserResponseDTO findById(Long id);
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO updateUser(Long id,UserUpdateDTO dto);
    void deleteUser(Long id);

}
