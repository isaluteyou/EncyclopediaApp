package encyclopediaApp.Repository;

import encyclopediaApp.Model.BannedUser;
import encyclopediaApp.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannedUserRepository extends JpaRepository<BannedUser, Integer> {
    List<BannedUser> findByIsActive(boolean isActive);
    Optional<BannedUser> findByUserAndIsActiveTrue(User user);
    Optional<BannedUser> findByUserUsernameAndIsActiveTrue(String username);
}