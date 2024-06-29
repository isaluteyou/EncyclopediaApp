package encyclopediaApp.Service;

import encyclopediaApp.DTO.BanUserDTO;
import encyclopediaApp.Exception.CustomException;
import encyclopediaApp.Model.BannedUser;
import encyclopediaApp.Model.User;
import encyclopediaApp.Repository.BannedUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BannedUserService {

    @Autowired
    private BannedUserRepository bannedUserRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public BannedUser banUser(String username, String bannedByUser, String reason, ZonedDateTime banExpire) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));

        User bannedBy = userService.findByUsername(bannedByUser)
                .orElseThrow(() -> new CustomException("Admin user not found with ID: " + bannedByUser));

        Optional<BannedUser> existingBan = bannedUserRepository.findByUserAndIsActiveTrue(user);
        if (existingBan.isPresent()) {
            throw new CustomException("User is already banned");
        }

        ZonedDateTime nowInBucharest = ZonedDateTime.now(ZoneId.of("Europe/Bucharest"));
        if (banExpire.isBefore(nowInBucharest)) {
            throw new CustomException("Ban expiration date must be in the future");
        }

        BannedUser bannedUser = new BannedUser();
        bannedUser.setUser(user);
        bannedUser.setBannedBy(bannedBy);
        bannedUser.setReason(reason);
        bannedUser.setBanDate(nowInBucharest.toLocalDateTime());
        bannedUser.setBanExpire(banExpire.withZoneSameInstant(ZoneId.of("Europe/Bucharest")).toLocalDateTime());
        bannedUser.setActive(true);

        return bannedUserRepository.save(bannedUser);
    }

    @Scheduled(fixedRate = 180000) // 3 minutes
    public void expireBans() {
        List<BannedUser> activeBans = bannedUserRepository.findByIsActive(true);
        LocalDateTime now = LocalDateTime.now();
        for (BannedUser ban : activeBans) {
            if (ban.getBanExpire().isBefore(now)) {
                ban.setActive(false);
                bannedUserRepository.save(ban);
            }
        }
    }

    public BannedUser unbanUser(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new CustomException("User not found"));

        BannedUser bannedUser = bannedUserRepository.findByUserAndIsActiveTrue(user)
                .orElseThrow(() -> new CustomException("Active ban not found for user: " + username));

        bannedUser.setActive(false);
        return bannedUserRepository.save(bannedUser);
    }

    public List<BanUserDTO> getAllActiveBans() {
        List<BannedUser> activeBans = bannedUserRepository.findByIsActive(true);
        List<BanUserDTO> bannedUsers = new ArrayList<>();

        for (BannedUser activeBan: activeBans) {
            bannedUsers.add(new BanUserDTO(
                    activeBan.getUser().getUsername(),
                    activeBan.getBannedBy().getUsername(),
                    activeBan.getReason(),
                    ZonedDateTime.of(activeBan.getBanExpire(), ZoneId.of("Europe/Bucharest"))
            ));
        }
        return bannedUsers;
    }

    public boolean isUserBanned(String username) {
        return bannedUserRepository.findByUserUsernameAndIsActiveTrue(username).isPresent();
    }
}