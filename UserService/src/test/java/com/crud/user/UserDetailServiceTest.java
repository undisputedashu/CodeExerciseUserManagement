package com.crud.user;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crud.user.controller.PasswordConstants;
import com.crud.user.entity.User;
import com.crud.user.repository.RoleRepository;
import com.crud.user.repository.UserRepository;
import com.crud.user.service.UserDetailService;


public class UserDetailServiceTest {

	@InjectMocks
	private UserDetailService userDetailService;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PasswordEncoder bCryptPasswordEncoder;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		 Mockito.when(bCryptPasswordEncoder.encode("abcd123")).thenReturn("abcd123");
		 Mockito.when(bCryptPasswordEncoder.encode("hjkhkd")).thenReturn("abcd123");
	}
	
	@Test
	public void testFindUserByEmail_negative_null() {
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Email id cannot be null or empty.");

		userDetailService.findUserByEmail(null);
	}

	@Test
	public void testFindUserByEmail_negative_empty() {
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Email id cannot be null or empty.");

		userDetailService.findUserByEmail("");
	}
	
	@Test
	public void testChangePassword_success() {
		User user = new User();
		user.setPassword("abcd123");
		
		String result = userDetailService.changePassword(user, "abcd123", "fdeb22");
		Assert.assertTrue(PasswordConstants.SUCCESSFUL.equals(result));
	}
	
	@Test
	public void testSaveUser_negative_null() {
		expectedEx.expect(NullPointerException.class);
	    expectedEx.expectMessage("User cannot be null.");
		userDetailService.saveUser(null);
	}

	@Test
	public void testDeleteUser_negative_null() {
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Email id cannot be null or empty.");

		userDetailService.deleteUser(null);
	}
	
	@Test
	public void testChangePassword_invalid() {
		User user = new User();
		user.setPassword("hjkhkd");
		
		String result = userDetailService.changePassword(user, "hjkhkd", "fdeb22");
		Assert.assertTrue(PasswordConstants.INVALIDPASSWORD.equals(result));
	}

	@Test
	public void testDeleteUser_negative_empty() {
		expectedEx.expect(IllegalArgumentException.class);
	    expectedEx.expectMessage("Email id cannot be null or empty.");

		userDetailService.deleteUser("");
	}

}