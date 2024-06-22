package encyclopediaApp.Controller;

import encyclopediaApp.DTO.ArticleRequest;
import encyclopediaApp.Model.Article;
import encyclopediaApp.Service.ArticleService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Article> getArticleById(@PathVariable ObjectId id) {
        Optional<Article> article = articleService.getArticleById(id);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Article> getArticleByTitle(@PathVariable String title) {
        Optional<Article> article = articleService.getArticleByTitle(title);
        return article.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'EDITOR')")
    public ResponseEntity<?> createArticle(@RequestBody ArticleRequest articleRequest) {
        try {
            Article article = articleService.saveArticle(articleRequest.getArticle(), articleRequest.getUsername());
            return ResponseEntity.ok(article);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Article> updateArticle(@PathVariable ObjectId id, @RequestBody Article articleDetails, @RequestParam String username) {
        Article updatedArticle = articleService.updateArticle(id, articleDetails, username);

        if (updatedArticle != null) {
            return ResponseEntity.ok(updatedArticle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/title/{title}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'EDITOR')")
    public ResponseEntity<Article> updateArticle(@PathVariable String title, @RequestBody Article articleDetails, @RequestParam String username) {
        Optional<Article> article = articleService.getArticleByTitle(title);
        ObjectId articleId;
        if(article.isPresent()) {
            articleId = article.get().getId();
            Article updatedArticle = articleService.updateArticle(articleId, articleDetails, username);

            if (updatedArticle != null) {
                return ResponseEntity.ok(updatedArticle);
            }
            else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/title/{title}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<Void> deleteArticleByTitle(@PathVariable String title) {
        Optional<Article> article = articleService.getArticleByTitle(title);
        if (article.isPresent()) {
            articleService.deleteArticle(article.get().getId());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exists/{title}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR', 'EDITOR')")
    public ResponseEntity<Boolean> checkArticleExists(@PathVariable String title) {
        Optional<Article> existingArticle = articleService.getArticleByTitle(title);
        return ResponseEntity.ok(existingArticle.isPresent());
    }
}
