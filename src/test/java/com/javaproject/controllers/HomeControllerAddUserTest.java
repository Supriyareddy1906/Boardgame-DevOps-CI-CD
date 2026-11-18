
package com.javaproject.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.javaproject.database.DatabaseAccess;

@ExtendWith(MockitoExtension.class)
class HomeControllerAddUserTest {

    @Mock
    private DatabaseAccess da;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JdbcUserDetailsManager jdbcUserDetailsManager;

    @InjectMocks
    private HomeController homeController;

    @Test
    void testAddNewUser() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttrs =
                new RedirectAttributesModelMap();

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(jdbcUserDetailsManager.userExists("testuser"))
                .thenReturn(false);

        String result = homeController.addUser(
                "testuser",
                "password123",
                new String[]{"ROLE_USER"},
                model,
                redirectAttrs
        );

        assertEquals("redirect:/", result);
        assertEquals(
                "User succesfully added!",
                redirectAttrs.getFlashAttributes().get("userAddedMsg")
        );

        verify(passwordEncoder).encode("password123");
        verify(jdbcUserDetailsManager).userExists("testuser");
        verify(jdbcUserDetailsManager).createUser(any(User.class));
    }

    @Test
    void testAddExistingUser() {
        ExtendedModelMap model = new ExtendedModelMap();
        RedirectAttributesModelMap redirectAttrs =
                new RedirectAttributesModelMap();

        when(passwordEncoder.encode("password123"))
                .thenReturn("encodedPassword");

        when(jdbcUserDetailsManager.userExists("existinguser"))
                .thenReturn(true);

        String result = homeController.addUser(
                "existinguser",
                "password123",
                new String[]{"ROLE_USER", "ROLE_MANAGER"},
                model,
                redirectAttrs
        );

        assertEquals("new-user", result);
        assertEquals(
                "User name already Exists. Try a different user name.",
                model.get("errorMsg")
        );
        assertEquals(2, ((List<?>) model.get("authorities")).size());

        verify(passwordEncoder).encode("password123");
        verify(jdbcUserDetailsManager).userExists("existinguser");
        verify(jdbcUserDetailsManager, never()).createUser(any(User.class));
    }
}
