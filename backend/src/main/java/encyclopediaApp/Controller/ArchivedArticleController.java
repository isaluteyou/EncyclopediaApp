package encyclopediaApp.Controller;


import encyclopediaApp.Model.ArchivedArticle;
import encyclopediaApp.Service.ArchivedArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/archived-articles")
public class ArchivedArticleController {
    @Autowired
    ArchivedArticleService archivedArticleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public List<ArchivedArticle> getAllArticles() {
        return archivedArticleService.getAllArchivedArticles();
    }

    @GetMapping("/title/{title}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<ArchivedArticle>> getAllArchivedArticlesByTitle(@PathVariable String title) {
        List<ArchivedArticle> archivedArticle = archivedArticleService.getArchivedArticlesByTitle(title);
        return ResponseEntity.ok(archivedArticle);
    }
}
