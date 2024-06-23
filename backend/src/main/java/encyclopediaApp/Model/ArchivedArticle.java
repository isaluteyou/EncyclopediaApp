package encyclopediaApp.Model;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "articles_archive")
public class ArchivedArticle {
    @Id
    private ObjectId id;

    private String title;
    private String content;
    private String deletedBy;
    private LocalDateTime deletedAt;
    private List<Article.EditHistory> editHistory = new ArrayList<>();

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

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public List<Article.EditHistory> getEditHistory() {
        return editHistory;
    }

    public void setEditHistory(List<Article.EditHistory> editHistory) {
        this.editHistory = editHistory;
    }
}
