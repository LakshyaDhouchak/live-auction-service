package com.lakshya.auction.live_auction_service.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakshya.auction.live_auction_service.DTO.CreateUserDTO;
import com.lakshya.auction.live_auction_service.DTO.UserResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.UserUpdateDTO;
import com.lakshya.auction.live_auction_service.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
    // define the properties
    private final UserService userService;

    // define the constructor
    public UserController(UserService userService){
        this.userService = userService;
    }

    // define the methord
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto){
        UserResponseDTO user = userService.createUser(dto);
        return new ResponseEntity<>(user,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id){
        UserResponseDTO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUser(){
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id ,@Valid @RequestBody UserUpdateDTO dto){
        UserResponseDTO newUser = userService.updateUser(id,dto);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
