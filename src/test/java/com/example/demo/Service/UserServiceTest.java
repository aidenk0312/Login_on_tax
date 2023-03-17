package com.example.demo.Service;

import com.example.demo.Repository.UserRepository;
import com.example.demo.User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User validUser;
    private User invalidUser;

    @BeforeEach
    void setUp() {
        validUser = new User("hong12", "123456", "홍길동", "860824-1655068");
        invalidUser = new User("invalid", "123456", "Invalid User", "123456-7890123");
    }

    @Test
    void signUp_success() {
        lenient().when(userRepository.findByUserId(validUser.getUserId())).thenReturn(Optional.empty());
        lenient().when(userRepository.save(validUser)).thenReturn(validUser);

        User createdUser = userService.signUp(validUser);
        assertNotNull(createdUser);
        assertEquals(validUser.getName(), createdUser.getName());
    }

    @Test
    void signUp_failure() {
        lenient().when(userRepository.findByUserId(invalidUser.getUserId())).thenReturn(Optional.empty());

        User createdUser = userService.signUp(invalidUser);
        assertNull(createdUser);
    }
}