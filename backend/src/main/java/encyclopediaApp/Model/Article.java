package encyclopediaApp.Model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "articles")
public class Article {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String title;
    private String content;
    private List<EditHistory> editHistory = new ArrayList<>();

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<EditHistory> getEditHistory() {
        return editHistory;
    }

    public void setEditHistory(List<EditHistory> editHistory) {
        this.editHistory = editHistory;
    }

    public static class EditHistory {
        private String username;
        private LocalDateTime timestamp;
        private String oldContent;

        public String getUsername() {
            return username;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getOldContent() {
            return oldContent;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public void setOldContent(String oldContent) {
            this.oldContent = oldContent;
        }
    }
}