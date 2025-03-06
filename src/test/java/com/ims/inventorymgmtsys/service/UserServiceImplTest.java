package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.controller.SessionController;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.input.CartItemInput;
import com.ims.inventorymgmtsys.repository.UserRepository;
import com.ims.inventorymgmtsys.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    SecurityUtils securityUtils;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void test_findById() {
        User user = new User();
        user.setUserName("mkasatest");
        doReturn(user).when(userRepository).findByUserName(user.getUserName());
        User actual = userService.findByUserName(user.getUserName());
        assertThat(actual.getUserName()).isEqualTo("mkasatest");

    }

    @Test
    void test_save() {
        User user = new User();
        user.setUserName("aaaaaaaaaaaaaUser");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmailAddress("test@gmail.com");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(userRepository).save(userArgumentCaptor.capture());

        userService.createUser(user);

        User userB = userArgumentCaptor.getValue();

        assertThat(userB.getUserName()).isEqualTo("aaaaaaaaaaaaaUser");
        assertThat(userB.getPassword()).isEqualTo(passwordEncoder.encode("1234"));
        assertThat(userB.getEmailAddress()).isEqualTo("test@gmail.com");

        verify(userRepository, times(1)).save(any());


    }

}
