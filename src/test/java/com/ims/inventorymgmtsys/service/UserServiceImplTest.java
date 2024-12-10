package com.ims.inventorymgmtsys.service;

import com.ims.inventorymgmtsys.controller.SessionController;
import com.ims.inventorymgmtsys.entity.User;
import com.ims.inventorymgmtsys.exception.UserAlreadyExistsException;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        doNothing().when(userRepository).save(userArgumentCaptor.capture());

        User user = new User();
        user.setUserName("aaaaaaaaaaaaaUser");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmailAddress("test@gmail.com");
        userService.createUser(user);
        verify(userRepository).save(any());

        User userB = userArgumentCaptor.getValue();

        assertThat(userB.getUserName()).isEqualTo("aaaaaaaaaaaaaUser");
        assertThat(userB.getPassword()).isEqualTo(passwordEncoder.encode("1234"));
        assertThat(userB.getEmailAddress()).isEqualTo("test@gmail.com");

        verify(userRepository, times(1)).save(any());

    }

    @Test
    void test_registerUser_ExistsUser() {
        User user = new User();
        user.setUserName("existsUser");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmailAddress("test@gmail.com");

        doReturn(user).when(userRepository).findByUserName(user.getUserName());
        doReturn(Optional.of(user)).when(userRepository).findByEmail(user.getEmailAddress());

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void test_registerUser_NotExistsUser() {
        User user = new User();
        user.setUserName("notExistsUser");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmailAddress("test@gmail.com");

        doNothing().when(userRepository).save(any());

        userService.createUser(user);

        assertThat(user.getUserName()).isEqualTo("notExistsUser");
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void test_getCurrentUser() {
        User user = new User();
        UUID currentId = UUID.randomUUID();
        user.setId(currentId);
        user.setUserName("currentUser");

        doReturn(currentId).when(securityUtils).getCurrentId();
        doReturn(user).when(userRepository).findById(currentId);

        User actualUser = userService.getCurrentUser();

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getUserName()).isEqualTo("currentUser");
        verify(securityUtils, times(1)).getCurrentId();
        verify(userRepository, times(1)).findById(currentId);
    }

    @Test
    void test_userUpdate() {
        User user = new User();
        user.setUserName("updateUser");
        user.setPassword(passwordEncoder.encode("1234"));
        user.setEmailAddress("test@gmail.com");
        userRepository.save(user);

        doReturn(user).when(userRepository).findByUserName("updateUser");
//        when(userRepository.findByUserName("updateUser")).thenReturn(user);


        User updateUser = userRepository.findByUserName("updateUser");

        doReturn(true).when(userRepository).update(updateUser);
        updateUser.setAddress("tokyo");

        userRepository.update(updateUser);

        assertThat(updateUser.getAddress()).isEqualTo("tokyo");
        verify(userRepository, times(1)).update(any());


    }

}
