package encyclopediaApp.Controller;

import encyclopediaApp.DTO.BanUserDTO;
import encyclopediaApp.Model.BannedUser;
import encyclopediaApp.Service.BannedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/banned-users")
public class BannedUserController {

    @Autowired
    private BannedUserService bannedUserService;

    @PostMapping("/ban")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannedUser> banUser(@RequestBody BanUserDTO banUserDTO) {
        BannedUser bannedUser = bannedUserService.banUser(
                banUserDTO.getUsername(),
                banUserDTO.getBannedBy(),
                banUserDTO.getReason(),
                banUserDTO.getBanExpire()
        );
        return ResponseEntity.ok(bannedUser);
    }

    @PostMapping("/unban/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BannedUser> unbanUser(@PathVariable String username) {
        BannedUser unbannedUser = bannedUserService.unbanUser(username);
        return ResponseEntity.ok(unbannedUser);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BanUserDTO>> getAllActiveBans() {
        List<BanUserDTO> bannedUsers = bannedUserService.getAllActiveBans();
        return ResponseEntity.ok(bannedUsers);
    }
}