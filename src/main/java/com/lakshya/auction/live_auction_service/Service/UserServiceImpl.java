package com.lakshya.auction.live_auction_service.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lakshya.auction.live_auction_service.DTO.CreateUserDTO;
import com.lakshya.auction.live_auction_service.DTO.UserResponseDTO;
import com.lakshya.auction.live_auction_service.DTO.UserUpdateDTO;
import com.lakshya.auction.live_auction_service.Entity.User;
import com.lakshya.auction.live_auction_service.ExceptionHandling.DataConflictException;
import com.lakshya.auction.live_auction_service.ExceptionHandling.ResourceNotFoundException;
import com.lakshya.auction.live_auction_service.Repository.UserRepo;

@Service
public class UserServiceImpl implements UserService{
    // define the properties
    private final UserRepo repo;
    private final PasswordEncoder PasswordEncoder;

    // define the constructor
    public UserServiceImpl(UserRepo repo , PasswordEncoder password ){
        this.repo = repo;
        this.PasswordEncoder = password;
    }

    // define the mapToResponse() methord
    private UserResponseDTO mapToResponse(User user){
        // calling the UserResponseDTO 
        UserResponseDTO dto =  new UserResponseDTO();
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    // define the methord

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {
        // define the condition
        if(repo.findByEmail(dto.getEmail()).isPresent()){
            throw new DataConflictException("Email "+ dto.getEmail() +" is already used");
        }
        User createdUser = new User();
        createdUser.setEmail(dto.getEmail());
        String HashedPassword = PasswordEncoder.encode(dto.getPassword());
        createdUser.setPassword(HashedPassword);
        createdUser.setUserName(dto.getUserName());
        createdUser.setRole("User");
        User storedUser = repo.save(createdUser);
        return mapToResponse(storedUser);
    }

    @Override
    @Transactional
    public UserResponseDTO findById(Long id) {
        return repo.findById(id).map(this::mapToResponse).orElseThrow(() -> new ResourceNotFoundException("User not Found with id: "+ id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return repo.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        // define the base condition
        User existUser =  repo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User not Found with id: "+ id));
        if(dto.getEmail()!= null && !dto.getEmail().equals(existUser.getEmail())){

            // define the condition
            if(repo.findByEmail(dto.getEmail()).isPresent()){
                throw new DataConflictException("Email : "+dto.getEmail()+" is already use in another account");
            }
            if(dto.getUserName()!= null){
                existUser.setUserName(dto.getUserName());
            }
            if(dto.getEmail() != null){
                existUser.setEmail(dto.getEmail());
            }
            if(dto.getRole()!= null){
                existUser.setRole(dto.getRole());
            }  
        }
        User savedUser = repo.save(existUser);
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional()
    public void deleteUser(Long id) {
        // define the condition
        if(!repo.findById(id).isPresent()){
            throw new ResourceNotFoundException("User not found with id: "+ id);
        }
        repo.deleteById(id);
    }
}
