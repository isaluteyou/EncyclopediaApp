package encyclopediaApp.Repository;

import encyclopediaApp.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Integer> {
    Optional<UserProfile> findByUserId(int userId);
    Optional<UserProfile> findByUser_Username(String username);
}
