package encyclopediaApp.DTO;

import java.time.ZonedDateTime;

public class BanUserDTO {
    private String username;
    private String bannedBy;
    private String reason;
    private ZonedDateTime banExpire;

    public BanUserDTO(String username, String bannedBy, String reason, ZonedDateTime banExpire) {
        this.username = username;
        this.bannedBy = bannedBy;
        this.reason = reason;
        this.banExpire = banExpire;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public void setBannedBy(String bannedBy) {
        this.bannedBy = bannedBy;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ZonedDateTime getBanExpire() {
        return banExpire;
    }

    public void setBanExpire(ZonedDateTime banExpire) {
        this.banExpire = banExpire;
    }
}
