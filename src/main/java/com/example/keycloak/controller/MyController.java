package com.example.keycloak.controller;

import com.example.keycloak.model.UserRequestDTO;
import com.example.keycloak.serice.KeycloakService;
import jakarta.annotation.security.RolesAllowed;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class MyController {

    private final KeycloakService keycloakService;

    @GetMapping("/admin")
    @RolesAllowed("admin")
    public String admin(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "admin";
    }

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        model.addAttribute("username", principal.getName());
        return "user";
    }

    @GetMapping("/create")
    public String createUser() {
        return "create-user";
    }

    @PostMapping("/create")
    @ResponseBody
    public String createUser(@RequestBody UserRequestDTO userRequestDTO) {
        keycloakService.addUser(userRequestDTO);
        return "index";
    }

}
