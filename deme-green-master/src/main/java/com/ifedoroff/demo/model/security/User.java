package com.ifedoroff.demo.model.security;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Rostik on 30.07.2017.
 */
@Document(collection = "users")
public class User {
    String role;
    String name;
    String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
