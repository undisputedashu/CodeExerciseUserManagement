package com.crud.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.crud.user.entity.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

	Role findByRole(String role);
}