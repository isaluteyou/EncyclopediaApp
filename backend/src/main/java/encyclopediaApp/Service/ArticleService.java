package encyclopediaApp.Service;

import encyclopediaApp.Model.Article;
import encyclopediaApp.Repository.ArticleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(ObjectId id) {
        return articleRepository.findById(id);
    }

    public Optional<Article> getArticleByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    public Article saveArticle(Article article, String username) {
        Article.EditHistory editHistory = new Article.EditHistory();
        editHistory.setUsername(username);
        editHistory.setTimestamp(LocalDateTime.now());
        editHistory.setOldContent("");

        article.setEditHistory(List.of(editHistory));
        return articleRepository.save(article);
    }

    public void deleteArticle(ObjectId id) {
        articleRepository.deleteById(id);
    }

    public Article updateArticle(ObjectId id, Article articleDetails, String username) {
        Optional<Article> articleOptional = articleRepository.findById(id);

        if (articleOptional.isPresent()) {
            Article article = articleOptional.get();

            // saving the history of the current state
            Article.EditHistory history = new Article.EditHistory();
            history.setUsername(username);
            history.setTimestamp(LocalDateTime.now());
            history.setOldContent(article.getContent());
            article.getEditHistory().add(history);

            // updating article with new content and title
            article.setTitle(articleDetails.getTitle());
            article.setContent(articleDetails.getContent());

            return articleRepository.save(article);
        } else {
            return null;
        }
    }
}