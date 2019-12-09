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
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

@Service
public class UserDetailService implements UserDetailsService {

	private static final String USER_ROLE = "USER";
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user != null) {
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
		Preconditions.checkArgument(!Strings.isNullOrEmpty(email), "Email id cannot be null or empty.");
		return userRepository.findByEmail(email);
	}

	public void saveUser(User user) {
		Preconditions.checkNotNull(user, "User cannot be null.");
		saveUser(user, USER_ROLE);
	}

	private void saveUser(User user, String role) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole(role);
		user.setRoles(new HashSet<>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public void deleteUser(String emailId) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(emailId), "Email id cannot be null or empty.");
		User user = userRepository.findByEmail(emailId);
		userRepository.deleteById(user.getId());
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

	public void updateUser(User user) {
		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser == null) {
			throw new RuntimeException("Unable to find user!");
		} 
		user.setPassword(existingUser.getPassword());
		userRepository.save(user);
	}

}