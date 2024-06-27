package encyclopediaApp.DTO;

import encyclopediaApp.Model.Article;

import java.util.List;

public class CategoryDetailDTO {
    private String name;
    private String description;
    private List<Article> articles;

    public CategoryDetailDTO(String name, String description, List<Article> articles) {
        this.name = name;
        this.description = description;
        this.articles = articles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
