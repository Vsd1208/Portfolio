package com.ecommerce.service;

import com.ecommerce.model.dto.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceIntegrationTest {
    @Autowired
    private UserService userService;

    @Test
    void seededCustomerCanLogIn() {
        var response = userService.login(new LoginRequest("customer@example.com", "password"));

        assertThat(response.email()).isEqualTo("customer@example.com");
    }
}
