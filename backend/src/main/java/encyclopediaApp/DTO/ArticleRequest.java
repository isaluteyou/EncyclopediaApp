package encyclopediaApp.DTO;

import encyclopediaApp.Model.Article;

public class ArticleRequest {

    private Article article;
    private String username;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}