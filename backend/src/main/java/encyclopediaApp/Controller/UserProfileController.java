package encyclopediaApp.Controller;

import encyclopediaApp.DTO.UserProfileDTO;
import encyclopediaApp.Model.User;
import encyclopediaApp.Model.UserProfile;
import encyclopediaApp.Service.FileStorageService;
import encyclopediaApp.Service.UserProfileService;
import encyclopediaApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/user_profile")
public class UserProfileController {

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private UserService userService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable int userId) {
        Optional<UserProfile> userProfile = userProfileService.getUserProfile(userId);
        return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfile> getUserProfileByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            Optional<UserProfile> userProfile = userProfileService.getUserProfile(user.get().getId());
            return userProfile.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/username/{username}/additional")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable String username) {
        UserProfileDTO userProfile = userService.getUserProfileInfo(username);
        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/username/{username}/edit")
    @PreAuthorize("authentication.name == #username or hasRole('ADMIN')")
    public ResponseEntity<UserProfile> updateUserProfile(@PathVariable String username,
                                                         @RequestParam(value = "file", required = false) MultipartFile file,
                                                         @RequestParam("age") int age,
                                                         @RequestParam("gender") String gender,
                                                         @RequestParam("location") String location,
                                                         @RequestParam("about") String about,
                                                         Principal principal) {
        System.out.println("Principal: " + principal.getName());
        Optional<User> user = userService.findByUsername(username);

        if (user.isPresent()) {
            Optional<UserProfile> userProfileOpt = userProfileService.getUserProfile(user.get().getId());
            if (userProfileOpt.isPresent()) {
                UserProfile userProfile = userProfileOpt.get();
                if (file != null) {
                    String fileName = fileStorageService.storeFile(file);
                    userProfile.setAvatar(fileName);
                }
                userProfile.setAge(age);
                userProfile.setGender(gender);
                userProfile.setLocation(location);
                userProfile.setAbout(about);
                return ResponseEntity.ok(userProfileService.saveUserProfile(userProfile));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/upload")
    @PreAuthorize("#username == authentication.principal.username or hasRole('ADMIN')")
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }
        String fileName = fileStorageService.storeFile(file);
        return ResponseEntity.ok("File uploaded successfully: " + fileName);
    }
}
