package com.evoluction.App_auth_keycloak.controllers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping("/")
    @PreAuthorize("hasRole('user')")
    public String list() {
        return "Listando produtos";
    }

    @PostMapping("/")
     @PreAuthorize("hasRole('admin')")
    public String create() {
        return "Cadastrando produtos";
    }
}
