package com.crud.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crud.user.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	User findByEmail(String email);

}