package encyclopediaApp.DTO;

import java.time.LocalDateTime;

public class RecentChanges {
    private String articleTitle;
    private String username;
    private LocalDateTime timestamp;

    public RecentChanges(String articleTitle, String username, LocalDateTime timestamp) {
        this.articleTitle = articleTitle;
        this.username = username;
        this.timestamp = timestamp;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
