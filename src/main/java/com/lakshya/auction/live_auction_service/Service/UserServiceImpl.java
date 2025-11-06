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
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // Helper method to convert entity to DTO
    private UserResponseDTO mapToResponse(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    @Transactional
    public UserResponseDTO createUser(CreateUserDTO dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new DataConflictException("Email " + dto.getEmail() + " is already in use");
        }

        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setUserName(dto.getUserName());
        newUser.setRole("USER"); // Use consistent uppercase roles

        User savedUser = userRepo.save(newUser);
        return mapToResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateDTO dto) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (dto.getEmail() != null && !dto.getEmail().equals(existingUser.getEmail())) {
            if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
                throw new DataConflictException("Email already in use: " + dto.getEmail());
            }
            existingUser.setEmail(dto.getEmail());
        }

        if (dto.getUserName() != null) {
            existingUser.setUserName(dto.getUserName());
        }

        if (dto.getRole() != null) {
            existingUser.setRole(dto.getRole());
        }

        User updatedUser = userRepo.save(existingUser);
        return mapToResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }
}
