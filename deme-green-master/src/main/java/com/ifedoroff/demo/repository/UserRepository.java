package com.ifedoroff.demo.repository;

import com.ifedoroff.demo.model.security.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Rostik on 30.07.2017.
 */

public interface UserRepository extends MongoRepository<User, String> {
    User findByName(String name);
}
