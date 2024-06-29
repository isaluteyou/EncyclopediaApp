package encyclopediaApp.Model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "banned_users")
public class BannedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "banned_by", nullable = false)
    private User bannedBy;

    @Column
    private String reason;

    @Column(nullable = false)
    private LocalDateTime banDate;

    @Column
    private LocalDateTime banExpire;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public User getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(User bannedBy) {
        this.bannedBy = bannedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getBanDate() {
        return banDate;
    }

    public void setBanDate(LocalDateTime banDate) {
        this.banDate = banDate;
    }

    public LocalDateTime getBanExpire() {
        return banExpire;
    }

    public void setBanExpire(LocalDateTime banExpire) {
        this.banExpire = banExpire;
    }
}
