package encyclopediaApp.Service;

import encyclopediaApp.DTO.UserProfileDTO;
import encyclopediaApp.Model.Role;
import encyclopediaApp.Model.User;
import encyclopediaApp.Model.UserProfile;
import encyclopediaApp.Repository.RoleRepository;
import encyclopediaApp.Repository.UserProfileRepository;
import encyclopediaApp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(mapRolesToAuthorities(user.getRoles()))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    public void createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        Role defaultRole = findRoleByName("EDITOR").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRoles(roles);

        user.setCreatedAt(LocalDateTime.now());
        user.setNumberOfEdits(0);

        User savedUser = userRepository.save(user);

        // create a profile for each user
        UserProfile userProfile = new UserProfile();
        userProfile.setUser(savedUser);
        userProfileRepository.save(userProfile);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User updateUserEdits(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNumberOfEdits(user.getNumberOfEdits() + 1);
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    // Roles
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    // User Profiles
    public Optional<UserProfile> getUserProfile(int userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfileDTO getUserProfileInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setNumberOfEdits(user.getNumberOfEdits());
        userProfileDTO.setCreatedAt(user.getCreatedAt());

        return userProfileDTO;
    }
}
