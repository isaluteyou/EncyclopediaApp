package encyclopediaApp.DTO;

import java.time.LocalDateTime;

public class UserContributionDTO {
    private String title;
    private LocalDateTime timestamp;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}