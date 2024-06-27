package encyclopediaApp.Service;

import encyclopediaApp.DTO.ArticleCommentaryDTO;
import encyclopediaApp.DTO.UserContributionDTO;
import encyclopediaApp.Events.ArticleEditedEvent;
import encyclopediaApp.Model.ArchivedArticle;
import encyclopediaApp.Model.Article;
import encyclopediaApp.Model.User;
import encyclopediaApp.Repository.ArchivedArticleRepository;
import encyclopediaApp.Repository.ArticleRepository;
import encyclopediaApp.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private ArchivedArticleRepository archivedArticleRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(ObjectId id) {
        return articleRepository.findById(id);
    }

    public Optional<Article> getArticleByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Transactional
    public Article saveArticle(Article article, String username) {
        String capitalizedTitle = article.getTitle().substring(0, 1).toUpperCase() +
                article.getTitle().substring(1); // capitalize first letter before saving to collection

        article.setTitle(capitalizedTitle);
        Optional<Article> existingArticle = articleRepository.findByTitle(capitalizedTitle);
        if (existingArticle.isPresent()) {
            throw new IllegalArgumentException("An article with this title already exists.");
        }

        // add history
        Article.EditHistory editHistory = new Article.EditHistory();
        editHistory.setUsername(username);
        editHistory.setTimestamp(LocalDateTime.now());
        editHistory.setOldContent("");
        article.setEditHistory(List.of(editHistory));

        // publish an event that the article has been created
        eventPublisher.publishEvent(new ArticleEditedEvent(this, username, capitalizedTitle));
        return articleRepository.save(article);
    }

    public void deleteArticle(String title, String deletedBy) {
        Optional<Article> article = articleRepository.findByTitle(title);
        if (article.isPresent()) {
            ArchivedArticle archivedArticle = new ArchivedArticle();
            archivedArticle.setTitle(article.get().getTitle());
            archivedArticle.setContent(article.get().getContent());
            archivedArticle.setDeletedBy(deletedBy);
            archivedArticle.setDeletedAt(LocalDateTime.now());
            archivedArticle.setEditHistory(article.get().getEditHistory());

            archivedArticleRepository.save(archivedArticle);
            articleRepository.delete(article.get());
        }
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

            // publish an event that the article has been modified
            eventPublisher.publishEvent(new ArticleEditedEvent(this, username, articleDetails.getTitle()));
            return articleRepository.save(article);
        } else {
            return null;
        }
    }

    public List<UserContributionDTO> getUserContributions(String username) {
        List<Article> articles = articleRepository.findAll();
        List<UserContributionDTO> contributions = new ArrayList<>();

        for (Article article : articles) {
            if (article.getEditHistory() != null) {
                for (Article.EditHistory edit : article.getEditHistory()) {
                    if (edit.getUsername().equals(username)) {
                        UserContributionDTO contribution = new UserContributionDTO();
                        contribution.setTitle(article.getTitle());
                        contribution.setTimestamp(edit.getTimestamp());
                        contributions.add(contribution);
                    }
                }
            }
        }

        contributions.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));
        return contributions;
    }

    public void addCategories(String title, List<String> categories) {
        Article article = articleRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        Set<String> uniqueCategories = new HashSet<>(article.getCategories());
        uniqueCategories.addAll(categories);
        article.setCategories(new ArrayList<>(uniqueCategories));
        articleRepository.save(article);
    }

    public void removeCategories(String title, List<String> categories) {
        Article article = articleRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Article not found"));
        Set<String> uniqueCategories = new HashSet<>(article.getCategories());
        categories.forEach(uniqueCategories::remove);
        article.setCategories(new ArrayList<>(uniqueCategories));
        articleRepository.save(article);
    }

    public Article addCommentary(String title, String username, String content) {
        Optional<Article> article = articleRepository.findByTitle(title);
        Article.Commentary commentary = new Article.Commentary();
        commentary.setUsername(username);
        commentary.setTimestamp(LocalDateTime.now());
        commentary.setContent(content);
        if(article.isPresent()) {
            article.get().getCommentaries().add(commentary);
            return articleRepository.save(article.get());
        } else {
            return null;
        }
    }

    public List<ArticleCommentaryDTO> getCommentaries(String title) {
        Article article = articleRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        return article.getCommentaries().stream()
                .sorted((c1, c2) -> c2.getTimestamp().compareTo(c1.getTimestamp()))
                .map(commentary -> {
                    ArticleCommentaryDTO dto = new ArticleCommentaryDTO();
                    dto.setUsername(commentary.getUsername());
                    dto.setTimestamp(commentary.getTimestamp());
                    dto.setContent(commentary.getContent());
                    dto.setAvatar(userProfileService.getUserAvatar(commentary.getUsername()));
                    return dto;
                }).collect(Collectors.toList());
    }

    public void deleteCommentary(String title, String username, LocalDateTime timestamp) {
        Article article = articleRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        article.getCommentaries().removeIf(commentary ->
                commentary.getUsername().equals(username) && commentary.getTimestamp().equals(timestamp)
        );

        articleRepository.save(article);
    }

    public List<Article> searchArticles(String query) {
        return articleRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(query, query);
    }

    public List<Article> getArticlesByUsername(String username) {
        return articleRepository.findByEditHistoryUsername(username);
    }
}