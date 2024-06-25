package encyclopediaApp.Events;

import org.springframework.context.ApplicationEvent;

public class ArticleEditedEvent extends ApplicationEvent {
    private String username;
    private String articleTitle;

    public ArticleEditedEvent(Object source, String username, String articleTitle) {
        super(source);
        this.username = username;
        this.articleTitle = articleTitle;
    }

    public String getUsername() {
        return username;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
}
