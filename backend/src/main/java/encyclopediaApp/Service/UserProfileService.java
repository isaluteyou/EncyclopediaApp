package encyclopediaApp.Service;

import encyclopediaApp.DTO.UserProfileDTO;
import encyclopediaApp.Model.User;
import encyclopediaApp.Model.UserProfile;
import encyclopediaApp.Repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserProfileService {
    @Autowired
    UserProfileRepository userProfileRepository;

    public Optional<UserProfile> getUserProfile(int userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }
}
