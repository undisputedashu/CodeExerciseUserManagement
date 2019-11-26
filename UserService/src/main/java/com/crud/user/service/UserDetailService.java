package com.crud.user.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.user.controller.PasswordConstants;
import com.crud.user.entity.Role;
import com.crud.user.entity.User;
import com.crud.user.repository.RoleRepository;
import com.crud.user.repository.UserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
	    if(user != null) {
	        List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
	        return buildUserForAuthentication(user, authorities);
	    } else {
	        throw new UsernameNotFoundException("username not found");
	    }
	}

	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
	    Set<GrantedAuthority> roles = new HashSet<>();
	    userRoles.forEach((role) -> {
	        roles.add(new SimpleGrantedAuthority(role.getRole()));
	    });

	    List<GrantedAuthority> grantedAuthorities = new ArrayList<>(roles);
	    return grantedAuthorities;
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
	    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}
	
	public User findUserByEmail(String email) {
	    return userRepository.findByEmail(email);
	}
	
	public void saveUser(User user) {
		saveUser(user, "USER");
	}
	
	private void saveUser(User user, String role) {
		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			user.setPassword(existingUser.getPassword());
		} else {
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		    Role userRole = roleRepository.findByRole(role);
		    user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		}
	    
	    userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteProduct(String id) {
		userRepository.deleteById(id);
	}

	public String changePassword(User user, String currentPassword, String newPassword) {
		String password = bCryptPasswordEncoder.encode(currentPassword);
		if (!password.equals(user.getPassword())) {
			return PasswordConstants.INVALIDPASSWORD;
		}
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		userRepository.save(user);
		return PasswordConstants.SUCCESSFUL;
	}

}
