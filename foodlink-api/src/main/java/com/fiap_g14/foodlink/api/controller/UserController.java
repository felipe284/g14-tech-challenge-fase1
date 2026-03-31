package com.fiap_g14.foodlink.api.controller;

import com.fiap_g14.foodlink.api.dto.UserResponseDTO;
import com.fiap_g14.foodlink.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping()
    public List<UserResponseDTO> getAllUsers() {
        return service.getAllUsers();
    }
}
