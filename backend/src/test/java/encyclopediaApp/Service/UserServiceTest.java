package encyclopediaApp.Service;

import encyclopediaApp.Model.User;
import encyclopediaApp.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.saveUser(user);
        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }
}
